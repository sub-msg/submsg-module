package cn.submsg.api.service;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.sr178.common.jdbc.bean.SqlParamBean;
import com.sr178.game.framework.exception.ServiceException;
import com.sr178.module.sms.config.AppConfig;
import com.sr178.module.sms.util.RequestEncoder;
import com.sr178.module.utils.MD5Security;

import cn.submsg.api.bean.SendMessageResult;
import cn.submsg.member.bo.ApiReqErrorLog;
import cn.submsg.member.bo.MemberMessageSign;
import cn.submsg.member.bo.MemberMessageTemp;
import cn.submsg.member.bo.MemberMsgInfo;
import cn.submsg.member.bo.MemberProject;
import cn.submsg.member.bo.MsgSendLog;
import cn.submsg.member.dao.ApiReqErrorLogDao;
import cn.submsg.member.dao.MemberMessageSignDao;
import cn.submsg.member.dao.MemberMessageTempDao;
import cn.submsg.member.dao.MemberMsgInfoDao;
import cn.submsg.member.dao.MemberProjectDao;
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
	
	
	public static final String TO = "to";
	
	public static final String PROJECT = "tempid";
	
	public static final String VARS = "vars";
	
	public static final String APPID = "appid";
	
	public static final String TIMESTAMP = "timestamp";
	
	public static final String SIGN_TYPE = "sign_type";
	
	public static final String SIGNATURE = "signature";
    
	public SendMessageResult sendMsg(String appId, String tempId,String to,String timestamp, String signature, String sign_type, String vars,String apiName,String ip){
		SendMessageResult apiResult = new SendMessageResult();
		//查询出应用id
		MemberProject memberProject = memberProjectDao.get(new SqlParamBean("id", Integer.valueOf(appId)));
		if(memberProject==null){
			throw new ServiceException(1000,"无效的appid="+appId);
		}
		//是否过期
		if(System.currentTimeMillis()-Long.valueOf(timestamp)>6000){
			throw new ServiceException(1,"该消息已过期，时间超过6s");
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
			String[] whiteIps = memberProject.getWhiteIp().split(",");
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
		//校验手机号码
		if(!isMobile(to)){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "3", "手机号码不符合规则"+to, ip);
			throw new ServiceException(3,"手机号码不符合规则"+to);
		}
		//查询出模板id
		MemberMessageTemp  messageTemp = memberMessageTempDao.get(new SqlParamBean("temp_id", tempId));
		if(messageTemp==null||messageTemp.getTempStatus().intValue()==MsgContentUtils.STATUS_NOT||messageTemp.getUserId().intValue()!=memberProject.getUserId().intValue()||messageTemp.getAppId().intValue()!=messageTemp.getAppId().intValue()){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "4", "无效的模板id"+tempId, ip);
			throw new ServiceException(4,"无效的模板id"+tempId);
		}
		//消息签名
		MemberMessageSign messageSign = memberMessageSignDao.get(new SqlParamBean("id", messageTemp.getSignId()));
		if(messageSign==null||messageSign.getSignStatus().intValue()==MsgContentUtils.STATUS_NOT||messageSign.getUserId().intValue()!=memberProject.getUserId().intValue()){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "5", "无效的签名id"+messageTemp.getSignId(), ip);
			throw new ServiceException(5,"无效的签名id"+messageTemp.getSignId());
		}
		//减发送许可数量
		String msgContent = MsgContentUtils.getContent(messageTemp.getTempContent(), vars, messageSign.getSignContent());
		int fee = MsgContentUtils.getFeeNum(msgContent);
		if(!memberMsgInfoDao.reduceMsgNum(memberProject.getUserId(), fee)){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "6", "发送许可数量不足！", ip);
			throw new ServiceException(6,"发送许可数量不足！");
		}
		
		
		MemberMsgInfo memberMsgInfo = memberMsgInfoDao.get(new SqlParamBean("user_id", memberProject.getUserId()));
		//将发送请求加入到消息队列中
		
		//写入日志
		String sendId = MD5Security.md5_32_Small(System.nanoTime()+"");
		
		MsgSendLog msgSendLog = new MsgSendLog(memberProject.getUserId(), memberProject.getId(), sendId, apiName, msgContent, messageSign.getSignContent(), fee, to, MsgSendLog.ST_CREATE, new Date(), new Date());
		if(!msgSendLogDao.add(msgSendLog)){
			throw new ServiceException(9,"日志添加失败！");
		}
		
		MsgBean msgBean = new MsgBean(sendId,to, msgContent, messageSign.getSignNum());
		if(!messageQueueService.pushReqMsg(msgBean)){
//			addApiErrorLog(memberProject.getUserId(),Integer.valueOf(appId), apiName, "8", "队列添加失败！", ip);
			throw new ServiceException(8,"队列添加失败！");
		}
		
		
		apiResult.setFee(fee);
		apiResult.setMsgNum(memberMsgInfo.getMsgNum());
		apiResult.setSendId(sendId);
		return apiResult;
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
		if (AppConfig.TYPE_MD5.equals(type)) {
			return RequestEncoder.encode(RequestEncoder.MD5, jointData);
		} else if (AppConfig.TYPE_SHA1.equals(type)) {
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
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
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
