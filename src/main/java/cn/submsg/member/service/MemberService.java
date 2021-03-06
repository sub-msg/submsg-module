package cn.submsg.member.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sr178.common.jdbc.bean.IPage;
import com.sr178.common.jdbc.bean.SqlParamBean;
import com.sr178.game.framework.exception.ServiceException;
import com.sr178.game.framework.log.LogSystem;
import com.sr178.module.utils.MD5Security;
import com.sr178.module.utils.ParamCheck;
import com.sr178.module.utils.SendMailUtils;

import cn.submsg.admin.bo.AdminSign;
import cn.submsg.admin.dao.AdminSignDao;
import cn.submsg.common.listener.VerifyLisner;
import cn.submsg.member.bean.MsgTempBean;
import cn.submsg.member.bo.MallProducts;
import cn.submsg.member.bo.Member;
import cn.submsg.member.bo.MemberMessageSign;
import cn.submsg.member.bo.MemberMessageTemp;
import cn.submsg.member.bo.MemberMsgInfo;
import cn.submsg.member.bo.MemberProject;
import cn.submsg.member.bo.MemberVerify;
import cn.submsg.member.bo.Notice;
import cn.submsg.member.bo.UserNotice;
import cn.submsg.member.constant.VerifyType;
import cn.submsg.member.dao.MallProductDao;
import cn.submsg.member.dao.MemberDao;
import cn.submsg.member.dao.MemberMessageSignDao;
import cn.submsg.member.dao.MemberMessageTempDao;
import cn.submsg.member.dao.MemberMsgInfoDao;
import cn.submsg.member.dao.MemberProjectDao;
import cn.submsg.member.dao.MemberVerifyDao;
import cn.submsg.member.dao.NoticeDao;
import cn.submsg.member.dao.UserNoticeDao;
import cn.submsg.message.utils.MsgContentUtils;

public class MemberService {
    @Autowired
	private MallProductDao mallProductDao;
    @Autowired
    private MemberDao memberDao;
    
    private static final String ALIDM_SMTP_HOST = "smtp.submsg.cn";
    private static final int ALIDM_SMTP_PORT = 25;
    private static final String MAIL_USER="service@submsg.cn";
    private static final String MAIL_PASSWORD="Aa222222";
    private static final String MAIL_NICKER_NAME="SUBMSG";
    
    
    @Autowired
    private MemberVerifyDao memberVerifyDao;
    @Autowired
    private MailTempService mailTempService;
    @Autowired
    private MemberMsgInfoDao memberMsgInfoDao; 
    @Autowired
    private MemberMessageTempDao memberMessageTempDao;
    @Autowired
    private MemberProjectDao memberProjectDao;
    @Autowired
    private MemberMessageSignDao memberMessageSignDao;
    @Autowired
    private AdminSignDao adminSignDao;
    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private UserNoticeDao userNoticeDao;

    /**
     * 获取所有产品列表
     * @return
     */
	public List<MallProducts> getProductList(){
		return mallProductDao.getList("order by id asc", new SqlParamBean("status", 0));
	}
	
	
	public MallProducts getProductById(int id){
		return mallProductDao.get(new SqlParamBean("id", id));
	}
	
	
	/**
	 * 查询用户名及用户
	 * @param userName
	 * @param pasword
	 */
	public Member getMember(String email,String pasword){
		 Member member = memberDao.get(new SqlParamBean("email", email),new SqlParamBean("and", "password", pasword));
		 return member;
	}
	/**
	 * 注册
	 * @param firstname
	 * @param lastname
	 * @param password
	 * @param email
	 */
	@Transactional
	public void signup(String firstname,String lastname,String password,String email){
		 ParamCheck.checkString(firstname, 1, "姓不能为空");
		 ParamCheck.checkString(lastname, 2, "名不能为空");
		 ParamCheck.checkString(email, 3, "邮件不能为空");
		 ParamCheck.checkString(password, 4, "密码不能为空");
		 Member member = memberDao.get(new SqlParamBean("email", email));
		 if(member!=null){
			 throw new ServiceException(5, "邮件已存在！");
		 }
		 member = new Member();
		 member.setUserName(email);
		 member.setFirstName(firstname);
		 member.setSecondName(lastname);
		 member.setPassword(MD5Security.md5_32_Small(password));
		 member.setEmail(email);
		 Date now = new Date();
		 member.setCreatedTime(now);
		 member.setUpdatedTime(now);
		 member.setStatus(Member.NOT_ACTIVED);
		 int userId = memberDao.addBackKey(member);
		 
		 
		 MemberMsgInfo memberMsgInfo = new MemberMsgInfo(userId, 0, 0d, 0, 0, -1, new Date());
		 memberMsgInfoDao.add(memberMsgInfo);
		 
		 //发送激活邮件
		 sendVerifyEmail(email, VerifyType.MemberActive);
	}
    /**
     * 根据验证码获取用户email
     * @param code
     * @return
     */
	public String getEmailByVerifyCode(String code){
		MemberVerify memberVerify = memberVerifyDao.get(new SqlParamBean("verify_str", code));
		if(memberVerify==null){
			throw new ServiceException(1,"无效的验证码");
		}
		Member member = memberDao.get(new SqlParamBean("id", memberVerify.getUserId()));
		return member.getEmail();
	}
	/**
	 * 激活用户
	 * @param code
	 */
	public void activeMember(String code){
		this.verify(code,new VerifyLisner() {
			@Override
			public void success(String code, MemberVerify memberVerify) {
				 memberDao.updateMemberStatus(memberVerify.getUserId(), Member.ACTIVED);
			}
			@Override
			public void fail(String code, MemberVerify memberVerify) {
				Member member = memberDao.get(new SqlParamBean("id", memberVerify.getUserId()));
				if(member.getStatus()==Member.NOT_ACTIVED){
					throw new ServiceException(1, "激活失败");
				}
					
			}
		});
	}
	/**
	 * 重置密码
	 * @param code
	 */
	public void resetPwd(String code,final String newPassword){
		this.verify(code,new VerifyLisner() {
			@Override
			public void success(String code, MemberVerify memberVerify) {
				 memberDao.updatePassword(memberVerify.getUserId(), MD5Security.md5_32_Small(newPassword));
			}
			@Override
			public void fail(String code, MemberVerify memberVerify) {
					throw new ServiceException(1, "重置失败！验证码过期");
					
			}
		});
	}
	
	/**
	 * 发送激活邮件
	 * @param userName
	 */
	public void sendActiveEmail(String email){
		 Member member = memberDao.get(new SqlParamBean("email", email));
		 if(member.getStatus()==Member.ACTIVED){
			 throw new ServiceException(1,"该账号已激活");
		 }
		 //发送验证邮件
		 sendVerifyEmail(email,  VerifyType.MemberActive);
	}
	/**
	 * 邮件校验
	 * @param email
	 */
	public void sendPwdResetEmail(String email){
		 Member member = memberDao.get(new SqlParamBean("email", email));
		 if(member==null){
			 throw new ServiceException(1,"该账号不存在");
		 }
		 //发送验证邮件
		 sendVerifyEmail(email,  VerifyType.MemberPwdReset);
	}
	/**
	 * 发送激活邮件
	 * @param userName
	 */
	private void sendVerifyEmail(String email,VerifyType verifyType){
		 Member member = memberDao.get(new SqlParamBean("email", email));
		 if(member==null){
			 throw new ServiceException(100,"用户不存在");
		 }
		 //发送激活邮件
		 String verifyStr = addMemberVerify(member.getId(), verifyType);
		 String mailTitle = mailTempService.getMailTemp(verifyType.getMailTempType().getTitleFileName(),verifyStr);
		 String mailContent = mailTempService.getMailTemp(verifyType.getMailTempType().getContentFileName(),verifyStr);
		 sendMail(member.getEmail(), mailTitle, mailContent);
	}
	
	/**
	 * 更改邮箱
	 * @param email
	 * @param password
	 */
	public void changeEmail(String email,String password,String newEmail){
		 ParamCheck.checkString(newEmail, 1, "新邮件地址不能为空");
		 if(email.toLowerCase().equals(newEmail.toLowerCase())){
			 throw new ServiceException(6, "新邮箱地址不能与旧邮箱地址相同");
		 }
		 Member member = memberDao.get(new SqlParamBean("email", email));
		 if(member== null){
			 throw new ServiceException(2, "用户邮箱不存在");
		 }
		 if(!MD5Security.md5_32_Small(password).equals(member.getPassword())){
			 throw new ServiceException(3, "密码错误");
		 }
		 
		 if(member.getStatus()==Member.ACTIVED){
			 throw new ServiceException(4, "改邮箱已激活 无法更改");
		 }
		 
		 Member member2 = memberDao.get(new SqlParamBean("email", newEmail));
		 if(member2!=null){
			 throw new ServiceException(5, "改邮箱已被占用请重新选择");
		 }
		 if(memberDao.updateMemberEmail(member.getId(), newEmail)){
			 sendActiveEmail(newEmail);
		 }
	}
	
	public void pwdReset(String email){
		 Member member = memberDao.get(new SqlParamBean("email", email));
		 if(member== null){
			 throw new ServiceException(1, "无效的邮件地址");
		 }
		 
	}
	/**
	 * 校验
	 * @param code
	 * @param lisner
	 */
	public void verify(String code,VerifyLisner lisner){
		MemberVerify memberVerify = memberVerifyDao.get(new SqlParamBean("verify_str", code));
		if(memberVerify==null){
			lisner.fail(code, memberVerify);
		}
		if(memberVerify.getCreatedTime().getTime()+memberVerify.getEffectTime()*60*1000<System.currentTimeMillis()){
			lisner.fail(code, memberVerify);
		}
		if(memberVerifyDao.updateVerifyTime(code)){
			lisner.success(code, memberVerify);
		}else{
			lisner.fail(code, memberVerify);
		}
	}
	
	
	
	/**
	 * 生成校验码
	 * @param userId  用户id
	 * @param type    类型  1 用户激活
	 * @param effectTime 有效时间  单位分钟
	 * @return
	 */
	public String addMemberVerify(int userId,VerifyType verifyType){
		MemberVerify verify = new MemberVerify();
		String result = generatorVerifyStr(userId+"");
		verify.setVerifyStr(result);
		verify.setEffectTime(verifyType.getEffectTime());
		verify.setType(verifyType.getType());
		verify.setUserId(userId);
		verify.setCreatedTime(new Date());
		memberVerifyDao.add(verify);
		return result;
	}
	
	private String generatorVerifyStr(String param){
		return MD5Security.md5_16_Small(System.currentTimeMillis()+"881asdf@!2331123"+param);
	}
	/**
	 * 邮件发送接口
	 * @param toAddress
	 * @param title
	 * @param content
	 */
	public static void sendMail(String toAddress,String title,String content){
		try {
			SendMailUtils.sendMail(ALIDM_SMTP_HOST, ALIDM_SMTP_PORT, MAIL_USER, MAIL_PASSWORD, MAIL_NICKER_NAME, toAddress, title, content);
		} catch (MessagingException e) {
			LogSystem.error(e, "发送邮件失败-->toAddress=["+toAddress+"],title=["+title+"],content=["+content+"]");
		}
	}
	/**
	 * 获取用户信息
	 * @param userName
	 * @return
	 */
	public Member getMemberByUserName(String userName){
		return memberDao.get(new SqlParamBean("user_name", userName));
	}
	/**
	 * 获取用户短信信息
	 * @param userName
	 * @return
	 */
	public MemberMsgInfo getMemberMsgInfo(int userId){
		return memberMsgInfoDao.get(new SqlParamBean("user_id", userId));
	}
	/**
	 * 获取用户最近使用的模板列表
	 * @param userId
	 * @return
	 */
	public List<MsgTempBean> getUserMsgTempList(int userId,int limit){
		return memberMessageTempDao.getMsgTempBeanList(userId,limit);
	}
	

	/**
	 * 获取用户模版列表 各种条件
	 * @param userId
	 * @param pageSize
	 * @param pageIndex
	 * @param tag
	 * @param searchString
	 * @return
	 */
	private static Map<String,Integer> statusTagMap = Maps.newHashMap();
	{
		//all 所有 unsendverify 没有送审的 verifying 正在送审核的  verifyed 审核通过的  unverifyed 审核不通过的
		statusTagMap.put("unsendverify", 0);
		statusTagMap.put("verifying", -1);
		statusTagMap.put("unverifyed", -2);
		statusTagMap.put("verifyed", 1);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<MsgTempBean> getUserMsgTempListByPage(int userId,int pageSize,int pageIndex,String tag,String searchString){
		boolean isAll = false;
		int status = 0;
		if(tag.equals("all")){
			isAll = true;
		}else{
			status = statusTagMap.get(tag);
		}
		IPage<MsgTempBean> page = memberMessageTempDao.getMsgTempBeanPageList(userId, isAll, status, pageSize, pageIndex,searchString);
		if(page!=null){
			return (List)page.getData();
		}
		return Lists.newArrayList();
	}
	/**
	 * 更新模板标题
	 * @param userId
	 * @param tempId
	 * @param tempTitle
	 */
	public void updateTempTitle(int userId,String tempId,String tempTitle){
		if(!memberMessageTempDao.updateTempTitle(userId, tempId, tempTitle)){
			throw new ServiceException(1, "更新失败");
		}
	}
	/**
	 * 删除模板
	 * @param userId
	 * @param tempId
	 */
	public void deleteTempByTempId(int userId,String tempId){
		if(!memberMessageTempDao.deleteTempByTempId(userId, tempId)){
			throw new ServiceException(1, "更新失败");
		}
	}
	/**
	 * 添加短信模板
	 * @param userId
	 * @param signId
	 * @param tempContent
	 * @param appId
	 */
	public void addTemp(int userId, int signId, String tempContent,int tempStatus) {
		MemberMessageSign sign = memberMessageSignDao.get(new SqlParamBean("id", signId),
				new SqlParamBean("and", "user_id", userId));
		if (sign == null) {
			throw new ServiceException(2, "无效的签名id");
		}
		MemberMessageTemp memberMessageTemp = new MemberMessageTemp();
		memberMessageTemp.setAppId(0);
		memberMessageTemp.setCreatedTime(new Date());
		memberMessageTemp.setSignId(signId);
		memberMessageTemp.setTempContent(tempContent);
		memberMessageTemp.setTempId(getStringRandom(6));
		if (tempStatus == 0 || tempStatus == -1) {
			memberMessageTemp.setTempStatus(tempStatus);
		} else {
			memberMessageTemp.setTempStatus(0);
		}
		memberMessageTemp.setUpdatedTime(new Date());
		memberMessageTemp.setUserId(userId);

		if (!memberMessageTempDao.add(memberMessageTemp)) {
			throw new ServiceException(3, "添加短信模板失败");
		}
	}
	/**
	 * 添加短信模板
	 * @param userId
	 * @param signId
	 * @param tempContent
	 * @param appId
	 */
	public void editTemp(String tempId,int userId, int signId, String tempContent,  int tempStatus) {
		MemberMessageSign sign = memberMessageSignDao.get(new SqlParamBean("id", signId),
				new SqlParamBean("and", "user_id", userId));
		if (sign == null) {
			throw new ServiceException(2, "无效的签名id");
		}
		
		MemberMessageTemp memberMessageTemp = memberMessageTempDao.get(new SqlParamBean("temp_id", tempId),
				new SqlParamBean("and", "user_id", userId));
		if(memberMessageTemp==null){
			throw new ServiceException(3, "无效的模板id");
		}
		
		if (tempStatus != 0 && tempStatus != -1) {
			tempStatus = 0;
		}
		if(!memberMessageTempDao.updateTemp(tempId, userId, tempContent, signId, tempStatus)){
			throw new ServiceException(3, "更新短信模板失败");
		}
	}
	/**
	 * 获取用户
	 * @param userId
	 * @param tempId
	 * @return
	 */
	public MsgTempBean getMsgTempBean(int userId,String tempId){
		return memberMessageTempDao.getMsgTempBean(userId, tempId);
	}
	//生成随机数字和字母,  
    public static String getStringRandom(int length) {  
        String val = "";  
        Random random = new Random();  
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }  
	/**
	 * 获取用户应用列表
	 * @param userId
	 * @return
	 */
	public List<MemberProject> getMemberProjectList(int userId){
		return memberProjectDao.getList(" order by created_time desc", new SqlParamBean("user_id", userId));
	}
	
	/**
	 * 获取用户应用列表
	 * @param userId
	 * @return
	 */
	public MemberProject getMemberProject(int userId,int appId){
		return memberProjectDao.get(new SqlParamBean("id", appId),new SqlParamBean("and","user_id", userId));
	}
	/**
	 * 删除app
	 * @param appId
	 */
	public void deleteApp(int userId,int appId){
		if(!memberProjectDao.delete(new SqlParamBean("id", appId),new SqlParamBean("and", "user_id", userId))){
			throw new ServiceException(1,"发生了错误");
		}
	}
	/**
	 * 修改应用信息
	 * @param appId
	 * @param projectName
	 * @param whiteIp
	 * @param maxSendNumDaily
	 * @param status
	 */
	public void updateMemberProject(int userId,int appId,String projectName,String whiteIp,int maxSendNumDaily,int status){
		memberProjectDao.update(userId,appId, projectName,whiteIp, maxSendNumDaily, status);
	}
	/**
	 * 更新appKey
	 * @param userId
	 * @param appId
	 */
	public String updateMemberProjectAppKey(int userId,int appId){
		String appKey = generatorAppKey();
		if(memberProjectDao.updateAppKey(userId, appId, appKey)){
			return appKey;
		}
		throw new ServiceException(1,"发生了错误");
	}
	/**
	 * 创建应用
	 * @return
	 */
	private String generatorAppKey(){
		return MD5Security.md5_32_Small(System.currentTimeMillis()+"881asdf@!2331123");
	}
	/**
	 * 创建应用
	 * @param userId
	 * @param appName
	 * @param maxSendNumDaily
	 */
	public void createMemberProject(int userId,String appName,int maxSendNumDaily){
		MemberProject memberProject = new MemberProject();
		memberProject.setCreatedTime(new Date());
		memberProject.setMaxSendNumDaily(maxSendNumDaily);
		memberProject.setProjectKey(generatorAppKey());
		memberProject.setProjectName(appName);
		memberProject.setStatus(1);
		memberProject.setUpdatedTime(new Date());
		memberProject.setWhiteIp("");
		memberProject.setUserId(userId);
		if(!memberProjectDao.add(memberProject)){
			throw new ServiceException(1,"发生了错误");
		}
	}
	/**
	 * 获取用户所有签名
	 * @param userId
	 * @return
	 */
	public List<MemberMessageSign> getMemberSignList(int userId){
		return memberMessageSignDao.getList(new SqlParamBean("user_id", userId));
	}
	
	/**
	 * 添加签名
	 * @param userId
	 * @param signContent
	 * @return
	 */
	@Transactional
	public void addMemberSign(int userId,String signContent){
		List<MemberMessageSign> signList = memberMessageSignDao.getList(new SqlParamBean("user_id", userId));
		if(signList.size()>=10){
			throw new ServiceException(3,"每个用户签名数不能超过10个");
		}
		MemberMessageSign memberSign = memberMessageSignDao.get(new SqlParamBean("user_id", userId),new SqlParamBean("and","sign_content", signContent));
		if(memberSign!=null){//已经存在了
			throw new ServiceException(1,"签名已经存在，请不要重复添加");
		}
		AdminSign adminSign = adminSignDao.get(new SqlParamBean("sign_content", signContent));
		MemberMessageSign signNew = new MemberMessageSign();
		signNew.setUserId(userId);
		signNew.setSignContent(signContent);
		signNew.setCreatedTime(new Date());
		signNew.setUpdatedTime(new Date());
		signNew.setSignPosition(0);
		signNew.setSignStatus(MsgContentUtils.STATUS_NOT);
		if(adminSign!=null){
			signNew.setSignNum(adminSign.getSignNum());
			signNew.setSignStatus(adminSign.getSignStatus());
		}else{
			adminSign = new AdminSign();
			adminSign.setCreatedTime(new Date());
			adminSign.setSignContent(signContent);
			adminSign.setSignStatus(MsgContentUtils.STATUS_NOT);
			adminSignDao.add(adminSign);
		}
		if(!memberMessageSignDao.add(signNew)){
			throw new ServiceException(2,"签名添加失败");
		}
	}
	/**
	 * 删除签名
	 * @param userId
	 * @param signId
	 */
	public void deleteMemberSign(int userId,int signId){
		if(!memberMessageSignDao.delete(new SqlParamBean("id", signId),new SqlParamBean("and", "user_id", userId))){
			throw new ServiceException(1,"签名删除失败");
		}
	}
    /**
     * 获取最近一条未读通知
     * @param userId
     * @return
     */
	public Notice getLastNotReadNotice(int userId){
		Notice notice = noticeDao.getLastNotice();
		UserNotice userNotice = userNoticeDao.get(new SqlParamBean("user_id",userId),new SqlParamBean("and", "notice_id", notice.getId()));
		if(userNotice!=null){
			return null;
		}
		return notice;
	}
	/**
	 * 已读
	 * @param userId
	 * @param noticeId
	 */
	public void readNotice(int userId,int noticeId){
		UserNotice userNotice = userNoticeDao.get(new SqlParamBean("user_id",userId),new SqlParamBean("and", "notice_id", noticeId));
		Notice notice = noticeDao.get(new SqlParamBean("id", noticeId));
        if(userNotice==null&&notice!=null){
    		userNotice = new UserNotice();
    		userNotice.setCreatedTime(new Date());
    		userNotice.setNoticeId(noticeId);
    		userNotice.setUserId(userId);
    		userNoticeDao.add(userNotice);	
        }
	}
	
	public static void main(String[] args) {
//		String cc= "xxxxxx${code}";
//		System.out.println(cc.replace("${code}", "1223"));
		for(int i=0;i<100;i++){
			System.out.println(getStringRandom(6));
		}
	}
}
