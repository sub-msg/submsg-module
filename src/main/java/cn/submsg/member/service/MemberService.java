package cn.submsg.member.service;

import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sr178.common.jdbc.bean.SqlParamBean;
import com.sr178.game.framework.exception.ServiceException;
import com.sr178.game.framework.log.LogSystem;
import com.sr178.module.utils.MD5Security;
import com.sr178.module.utils.ParamCheck;
import com.sr178.module.utils.SendMailUtils;

import cn.submsg.common.listener.VerifyLisner;
import cn.submsg.member.bo.MallProducts;
import cn.submsg.member.bo.Member;
import cn.submsg.member.bo.MemberVerify;
import cn.submsg.member.constant.VerifyType;
import cn.submsg.member.dao.MallProductDao;
import cn.submsg.member.dao.MemberDao;
import cn.submsg.member.dao.MemberVerifyDao;

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
    /**
     * 获取所有产品列表
     * @return
     */
	public List<MallProducts> getProductList(){
		return mallProductDao.getAll();
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
		 member.setPassword(password);
		 member.setEmail(email);
		 Date now = new Date();
		 member.setCreatedTime(now);
		 member.setUpdatedTime(now);
		 member.setStatus(Member.NOT_ACTIVED);
		 int userId = memberDao.addBackKey(member);
		 
		 //发送激活邮件
		 String verifyStr = addMemberVerify(userId, VerifyType.MEMBER_ACTIVIED, 180);
		 
		 String mailTitle = "激活您的账号";
		 String mailContent = mailTempService.getActiveMailTemp(VerifyType.MEMBER_ACTIVIED,verifyStr);
		 
		 sendMail(email, mailTitle, mailContent);
	}
	/**
	 * 激活用户
	 * @param code
	 */
	public String activeMember(String code){
		String result = null;
		MemberVerify memberVerify = memberVerifyDao.get(new SqlParamBean("verify_str", code));
		if(memberVerify!=null){
			Member member = memberDao.get(new SqlParamBean("id", memberVerify.getUserId()));
			result = member.getEmail();
		}
		this.verify(code,new VerifyLisner() {
			@Override
			public void success(String code, MemberVerify memberVerify) {
				 memberDao.updateMemberStatus(memberVerify.getUserId(), Member.ACTIVED);
			}
			@Override
			public void fail(String code, MemberVerify memberVerify) {
//				if(member.getStatus()==Member.NOT_ACTIVED){
					throw new ServiceException(1, "激活失败");
//				}
					
			}
		});
		return result;
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
	public String addMemberVerify(int userId,int type,int effectTime){
		MemberVerify verify = new MemberVerify();
		String result = generatorVerifyStr(userId+"");
		verify.setVerifyStr(result);
		verify.setEffectTime(effectTime);
		verify.setType(type);
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
	
	public static void main(String[] args) {
		String cc= "xxxxxx${code}";
		System.out.println(cc.replace("${code}", "1223"));
	}
}
