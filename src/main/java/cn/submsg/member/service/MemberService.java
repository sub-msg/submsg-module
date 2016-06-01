package cn.submsg.member.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sr178.common.jdbc.bean.SqlParamBean;
import com.sr178.game.framework.exception.ServiceException;
import com.sr178.module.utils.ParamCheck;

import cn.submsg.member.bo.MallProducts;
import cn.submsg.member.bo.Member;
import cn.submsg.member.dao.MallProductDao;
import cn.submsg.member.dao.MemberDao;

public class MemberService {
    @Autowired
	private MallProductDao mallProductDao;
    @Autowired
    private MemberDao memberDao;
	
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
		 memberDao.add(member);
	}
}
