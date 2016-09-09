package cn.submsg.api.service;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sr178.common.jdbc.bean.SqlParamBean;
import com.sr178.game.framework.config.ConfigLoader;
import com.sr178.game.framework.exception.ServiceException;
import com.sr178.module.utils.DateUtils;
import com.sr178.module.utils.MD5Security;

import cn.submsg.api.bean.SendMessageResult;
import cn.submsg.api.utils.RequestEncoder;
import cn.submsg.member.bo.ApiReqErrorLog;
import cn.submsg.member.bo.MemberMessageSign;
import cn.submsg.member.bo.MemberMessageTemp;
import cn.submsg.member.bo.MemberMsgInfo;
import cn.submsg.member.bo.MemberProject;
import cn.submsg.member.bo.MsgInternationalData;
import cn.submsg.member.bo.MsgSendLog;
import cn.submsg.member.dao.ApiReqErrorLogDao;
import cn.submsg.member.dao.MemberMessageSignDao;
import cn.submsg.member.dao.MemberMessageTempDao;
import cn.submsg.member.dao.MemberMsgInfoDao;
import cn.submsg.member.dao.MemberProjectDao;
import cn.submsg.member.dao.MsgInternationalDataDao;
import cn.submsg.member.dao.MsgSendLogDao;
import cn.submsg.message.bean.MsgBean;
import cn.submsg.message.service.MessageQueueService;
import cn.submsg.message.utils.MsgContentUtils;

public class ApiService {
	@Autowired
	private MemberProjectDao memberProjectDao;
	@Autowired
	private MemberMessageSignDao memberMessageSignDao;
	@Autowired
	private MemberMessageTempDao memberMessageTempDao;
	@Autowired
	private ApiReqErrorLogDao apiReqErrorLogDao;
	@Autowired
	private MemberMsgInfoDao memberMsgInfoDao;
	@Autowired
	private MsgSendLogDao msgSendLogDao;
	@Autowired
	private MessageQueueService messageQueueService;
    @Autowired
    private MsgInternationalDataDao msgInternationalDataDao;
	
    private Cache<String,Long> timeMap = CacheBuilder.newBuilder().expireAfterAccess(60, TimeUnit.SECONDS).maximumSize(20000).build();
    
    
    private Cache<String,Integer> appDayMap = CacheBuilder.newBuilder().expireAfterAccess(25, TimeUnit.HOURS).maximumSize(20000).build();
	
	public static final String TO = "to";
	
	public static final String PROJECT = "tempid";
	
	public static final String VARS = "vars";
	
	public static final String APPID = "appid";
	
	public static final String TIMESTAMP = "timestamp";
	
	public static final String SIGN_TYPE = "sign_type";
	
	public static final String SIGNATURE = "signature";
	
	public static final String REGION_CODE = "region_code";
	
	
	public static final String TYPE_NORMAL = "normal";
	public static final String TYPE_MD5 = "md5";
	public static final String TYPE_SHA1 = "sha1";
    /**
     * 发送国内短信
     * @param appId
     * @param tempId
     * @param to
     * @param timestamp
     * @param signature
     * @param sign_type
     * @param vars
     * @param apiName
     * @param ip
     * @return
     */
	public SendMessageResult sendMsg(String appId, String tempId,String to,String timestamp, String signature, String sign_type, String vars,String apiName,String ip,int sendType){
		//默认卓望  该字段为测试字段
		if(sendType==0){
			sendType = ConfigLoader.getIntValue("send_type",MsgContentUtils.SENDTYPE_ZW);//MsgContentUtils.SENDTYPE_ZW;
		}
		SendMessageResult apiResult = new SendMessageResult();
		if(Strings.isNullOrEmpty(timestamp)){
			throw new ServiceException(8,"时间戳不能为空！");
		}
		if(Strings.isNullOrEmpty(to)){
			throw new ServiceException(11,"手机号码不能为空！");
		}
		if(Strings.isNullOrEmpty(sign_type)){
			sign_type = TYPE_NORMAL;
		}
		//查询出应用id
		MemberProject memberProject = memberProjectDao.get(new SqlParamBean("id", Integer.valueOf(appId)));
		if(memberProject==null){
			throw new ServiceException(1000,"无效的appid="+appId);
		}
		//是否过期
		if(System.currentTimeMillis()-Long.valueOf(timestamp)>30000){
			throw new ServiceException(1,"该消息已过期，时间超过30s");
		}
		//签名校验
		Map<String,Object> data = new TreeMap<String,Object>();
		data.put(TO, to);
		data.put(PROJECT, tempId);
		data.put(VARS, vars);
		data.put(APPID, appId);
		data.put(TIMESTAMP, timestamp);
		data.put(SIGN_TYPE, sign_type);
		String serverSign = createSignature(sign_type, data, appId, memberProject.getProjectKey());
		if(!serverSign.equals(signature)){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "2", "签名校验不正确", ip);
			throw new ServiceException(2,"签名校验不正确，serverSign=["+serverSign+"],clientSign = ["+signature+"]");
		}
		//校验ip白名单
		if(!Strings.isNullOrEmpty(memberProject.getWhiteIp())){
			String[] whiteIps = memberProject.getWhiteIp().split(";");
			boolean isCross = false;
			for(String whiteIp:whiteIps){
				if(whiteIp.equals(ip)){
					isCross = true;
				}
			}
			if(!isCross){
//				addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "7", "ip不在白名单内："+ip+",whiteIp=["+memberProject.getWhiteIp()+"]", ip);
				throw new ServiceException(7,"ip不在白名单内："+ip+",whiteIp=["+memberProject.getWhiteIp()+"]");
			}
		}
		
		//每日发送检查
		String appDayKey = getDailyAppSendKey(memberProject.getId());
		checkAppDaySendLimits(appDayKey, memberProject.getMaxSendNumDaily());
		
		//校验手机号码
		to = to.trim();
		if(!isMobile(to)){
//				addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "3", "手机号码不符合规则"+to, ip);
				throw new ServiceException(3,"手机号码不符合规则"+to);
		}
		//查询出模板id
		MemberMessageTemp  messageTemp = memberMessageTempDao.get(new SqlParamBean("temp_id", tempId));
		if(messageTemp==null||messageTemp.getTempStatus().intValue()!=MsgContentUtils.STATUS_OK||messageTemp.getUserId().intValue()!=memberProject.getUserId().intValue()||messageTemp.getAppId().intValue()!=messageTemp.getAppId().intValue()){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "4", "无效的模板id"+tempId, ip);
			throw new ServiceException(4,"无效的模板id"+tempId);
		}
		//消息签名
		MemberMessageSign messageSign = memberMessageSignDao.get(new SqlParamBean("id", messageTemp.getSignId()));
		if(messageSign==null||messageSign.getSignStatus().intValue()!=MsgContentUtils.STATUS_OK||messageSign.getUserId().intValue()!=memberProject.getUserId().intValue()){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "5", "无效的签名id"+messageTemp.getSignId(), ip);
			throw new ServiceException(5,"无效的签名id"+messageTemp.getSignId());
		}
		//减发送许可数量
		String msgContent = MsgContentUtils.getContent(messageTemp.getTempContent(), vars, messageSign.getSignContent());
		int fee = MsgContentUtils.getFeeNum(msgContent);
		int otherFee = MsgContentUtils.getOtherFeeNum(msgContent);
		if(fee<otherFee){
			fee = otherFee;
		}
		
		
		//检查消息的发送间隔时间
		checkSendDistance(to,msgContent);
		
		int msgNum = fee;
		if(memberProject.getUserId().intValue()==19){
			int count = ConfigLoader.getIntValue("times", 3);
			msgNum = count*fee;
		}
		if(!memberMsgInfoDao.reduceMsgNum(memberProject.getUserId(), msgNum)){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "6", "发送许可数量不足！", ip);
			throw new ServiceException(6,"发送服务数量不足！");
		}
		

		
		MemberMsgInfo memberMsgInfo = memberMsgInfoDao.get(new SqlParamBean("user_id", memberProject.getUserId()));
		//将发送请求加入到消息队列中
		//写入日志
		String sendId = MD5Security.md5_32_Small(System.nanoTime()+"");
		
		MsgSendLog msgSendLog = new MsgSendLog(memberProject.getUserId(), memberProject.getId(), sendId, apiName, msgContent, messageSign.getSignContent(), fee, to,sendType, MsgSendLog.ST_CREATE, new Date(), new Date());
		if(!msgSendLogDao.add(msgSendLog)){
			throw new ServiceException(9,"日志添加失败！");
		}
		MsgBean msgBean = new MsgBean(sendId,to, msgContent, messageSign.getSignNum(),tempId,vars,sendType);
		if(!messageQueueService.pushReqMsg(msgBean)){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "8", "队列添加失败！", ip);
			throw new ServiceException(10,"队列添加失败！");
		}
		
		this.increaseAppDailyTimes(appDayKey);
		
		apiResult.setFee(fee);
		apiResult.setMsgNum(memberMsgInfo.getMsgNum());
		apiResult.setSendId(sendId);
		apiResult.setMsgBalance(memberMsgInfo.getMsgBalance());
		return apiResult;
	}

	
    /**
     * 发送国内短信
     * @param appId
     * @param tempId
     * @param to
     * @param timestamp
     * @param signature
     * @param sign_type
     * @param vars
     * @param apiName
     * @param ip
     * @return
     */
	public SendMessageResult sendMsgInternational(String appId, String regionCode,String tempId,String to,String timestamp, String signature, String sign_type, String vars,String apiName,String ip,int sendType){
		//默认卓望  该字段为测试字段
		if(sendType==0){
			sendType = MsgContentUtils.SENDTYPE_SUBMAIL;
		}
		SendMessageResult apiResult = new SendMessageResult();
		if(Strings.isNullOrEmpty(timestamp)){
			throw new ServiceException(8,"时间戳不能为空！");
		}
		if(Strings.isNullOrEmpty(sign_type)){
			sign_type = TYPE_NORMAL;
		}
		//查询出应用id
		MemberProject memberProject = memberProjectDao.get(new SqlParamBean("id", Integer.valueOf(appId)));
		if(memberProject==null){
			throw new ServiceException(1000,"无效的appid="+appId);
		}
		//是否过期
		if(System.currentTimeMillis()-Long.valueOf(timestamp)>30000){
			throw new ServiceException(1,"该消息已过期，时间超过30s");
		}
		//签名校验
		Map<String,Object> data = new TreeMap<String,Object>();
		data.put(REGION_CODE, regionCode);
		data.put(TO, to);
		data.put(PROJECT, tempId);
		data.put(VARS, vars);
		data.put(APPID, appId);
		data.put(TIMESTAMP, timestamp);
		data.put(SIGN_TYPE, sign_type);
		String serverSign = createSignature(sign_type, data, appId, memberProject.getProjectKey());
		if(!serverSign.equals(signature)){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "2", "签名校验不正确", ip);
			throw new ServiceException(2,"签名校验不正确，serverSign=["+serverSign+"],clientSign = ["+signature+"]");
		}
		//校验ip白名单
		if(!Strings.isNullOrEmpty(memberProject.getWhiteIp())){
			String[] whiteIps = memberProject.getWhiteIp().split(";");
			boolean isCross = false;
			for(String whiteIp:whiteIps){
				if(whiteIp.equals(ip)){
					isCross = true;
				}
			}
			if(!isCross){
//				addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "7", "ip不在白名单内："+ip+",whiteIp=["+memberProject.getWhiteIp()+"]", ip);
				throw new ServiceException(7,"ip不在白名单内："+ip+",whiteIp=["+memberProject.getWhiteIp()+"]");
			}
		}
		
		//每日发送检查
		String appDayKey = getDailyAppSendKey(memberProject.getId());
		checkAppDaySendLimits(appDayKey, memberProject.getMaxSendNumDaily());
		
		//将+86字符替换掉
		if(regionCode.indexOf("+")!=-1){
			regionCode = regionCode.replace("+","");
		}
		// 屏蔽国内区号
        if(regionCode.equals("86")){
        	throw new ServiceException(3,"该接口不支持86区号！请使用国内短信接口");
		}
        
		//查询出模板id
		MemberMessageTemp  messageTemp = memberMessageTempDao.get(new SqlParamBean("temp_id", tempId));
		if(messageTemp==null||messageTemp.getTempStatus().intValue()!=MsgContentUtils.STATUS_OK||messageTemp.getUserId().intValue()!=memberProject.getUserId().intValue()||messageTemp.getAppId().intValue()!=messageTemp.getAppId().intValue()){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "4", "无效的模板id"+tempId, ip);
			throw new ServiceException(4,"无效的模板id"+tempId);
		}
		//消息签名
		MemberMessageSign messageSign = memberMessageSignDao.get(new SqlParamBean("id", messageTemp.getSignId()));
		if(messageSign==null||messageSign.getSignStatus().intValue()!=MsgContentUtils.STATUS_OK||messageSign.getUserId().intValue()!=memberProject.getUserId().intValue()){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "5", "无效的签名id"+messageTemp.getSignId(), ip);
			throw new ServiceException(5,"无效的签名id"+messageTemp.getSignId());
		}
		
		MsgInternationalData mdata = msgInternationalDataDao.get(new SqlParamBean("region_code", regionCode));
		if(mdata==null){
			throw new ServiceException(11,"不支持的区域码:"+regionCode);
		}
		
		//减国际短信余额
		String msgContent = MsgContentUtils.getContent(messageTemp.getTempContent(), vars, messageSign.getSignContent());
		
		//检查消息的发送间隔时间
		checkSendDistance(to,msgContent);
		

		int fee = MsgContentUtils.getFeeNum(msgContent);
		
		int otherFee = MsgContentUtils.getOtherFeeNum(msgContent);
		if(fee<otherFee){
			fee = otherFee;
		}
		
		if(!memberMsgInfoDao.reduceMsgBalance(memberProject.getUserId(), fee*mdata.getPrice())){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "6", "发送许可数量不足！", ip);
			throw new ServiceException(6,"余额不足！");
		}
		MemberMsgInfo memberMsgInfo = memberMsgInfoDao.get(new SqlParamBean("user_id", memberProject.getUserId()));
		//拼接国际短信
		to = "+"+regionCode+to;
		

		//将发送请求加入到消息队列中
		//写入日志
		String sendId = MD5Security.md5_32_Small(System.nanoTime()+"");
		MsgSendLog msgSendLog = new MsgSendLog(memberProject.getUserId(), memberProject.getId(), sendId, apiName, msgContent, messageSign.getSignContent(), fee, to,sendType, MsgSendLog.ST_CREATE, new Date(), new Date());
		msgSendLog.setPrice(mdata.getPrice());
		if(!msgSendLogDao.add(msgSendLog)){
			throw new ServiceException(9,"日志添加失败！");
		}
		MsgBean msgBean = new MsgBean(sendId,to, msgContent, messageSign.getSignNum(),tempId,vars,sendType);
		if(!messageQueueService.pushReqMsg(msgBean)){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "8", "队列添加失败！", ip);
			throw new ServiceException(10,"队列添加失败！");
		}
		this.increaseAppDailyTimes(appDayKey);
		apiResult.setFee(fee);
		apiResult.setMsgNum(memberMsgInfo.getMsgNum());
		apiResult.setSendId(sendId);
		apiResult.setMsgBalance(memberMsgInfo.getMsgBalance());
		return apiResult;
	}
	
	
	private void checkSendDistance(String to,String content){
		String md5Str = MD5Security.md5_32_Small(to+content);
		long now = System.currentTimeMillis();
		Long preTime = timeMap.getIfPresent(md5Str);
		if(preTime==null){
			timeMap.put(md5Str, now);
		}else{
			if(now-preTime<30*1000){
				throw new ServiceException(1001,"相同手机号，相同内容间隔时间不能小于30秒");
			}
			timeMap.put(md5Str, now);
		}
	}
	
	private String getDailyAppSendKey(int appId){
		String dayStr = DateUtils.getDate(new Date());
		return dayStr+"-"+appId;
	}
	
	private void checkAppDaySendLimits(String appDayKey,int limit){
		Integer times = appDayMap.getIfPresent(appDayKey);
		if(times==null){
			times = 0;
		}
		if(limit>0){//否则为不限制
			if(times>=limit){
				throw new ServiceException(1002,"超过当日限制条数！");
			}
		}
	}
	private void increaseAppDailyTimes(String appDayKey){
		Integer times = appDayMap.getIfPresent(appDayKey);
		if(times==null){
			times = 0;
		}
		appDayMap.put(appDayKey, times+1);
	}
	/**
	 * 校验签名
	 * @param data
	 * @return
	 */
	private String createSignature(String type,Map<String,Object> data,String appId,String appKey) {
		 return buildSignature(type, appId, appKey, RequestEncoder.formatRequest(data));
	}
	/**
	 * 构件签名
	 * @param type
	 * @param appId
	 * @param appKey
	 * @param data
	 * @return
	 */
	private String buildSignature(String type,String appId,String appKey,String data) {
		String jointData = appId + appKey + data + appId + appKey;
		if (TYPE_MD5.equals(type)) {
			return RequestEncoder.encode(RequestEncoder.MD5, jointData);
		} else if (TYPE_SHA1.equals(type)) {
			return RequestEncoder.encode(RequestEncoder.SHA1, jointData);
		}
		return appKey;
	}
	/**
	 * api请求错误日志
	 * @param userId
	 * @param projectId
	 * @param apiName
	 * @param errorCode
	 * @param errorDesc
	 * @param reqIp
	 */
	public void addApiErrorLog(Integer userId, Integer projectId, String apiName, String errorCode, String errorDesc,
			String reqIp){
		ApiReqErrorLog apiReqErrorLog = new ApiReqErrorLog(userId, projectId, apiName, errorCode, errorDesc, reqIp, new Date());
		apiReqErrorLogDao.add(apiReqErrorLog);
	}
	
	
	/**
	 * 手机号验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) { 
		Pattern p = null;
		Matcher m = null;
		boolean b = false; 
		p = Pattern.compile("^[1][0-9][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches(); 
		return b;
	}
	/**
	 * 电话号码验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) { 
		Pattern p1 = null,p2 = null;
		Matcher m = null;
		boolean b = false;  
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
		if(str.length() >9)
		{	m = p1.matcher(str);
 		    b = m.matches();  
		}else{
			m = p2.matcher(str);
 			b = m.matches(); 
		}  
		return b;
	}
	
}
