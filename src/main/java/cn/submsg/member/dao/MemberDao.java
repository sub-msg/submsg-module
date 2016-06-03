package cn.submsg.member.dao;

import com.sr178.common.jdbc.SqlParameter;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bo.Member;

public class MemberDao extends SubMsgBaseDao<Member> {
	/**
	 * 更新账户的激活状态
	 * @param userId
	 * @param status
	 * @return
	 */
	public boolean updateMemberStatus(int userId,int status){
		String sql = "update "+ super.getTable() + " set status=? where id=? limit 1";
		return this.getJdbc().update(sql, SqlParameter.Instance().withInt(status).withInt(userId))>0;
	}
	
	/**
	 * 更改用户邮箱
	 * @param userId
	 * @param email
	 * @return
	 */
	public boolean updateMemberEmail(int userId,String email){
		String sql = "update "+ super.getTable() + " set user_name=?,email=? where id=? limit 1";
		return this.getJdbc().update(sql, SqlParameter.Instance().withString(email).withString(email).withInt(userId))>0;
	}
	/**
	 * 更新密码
	 * @param userId
	 * @param passWord
	 * @return
	 */
	public boolean updatePassword(int userId,String passWord){
		String sql = "update "+ super.getTable() + " set password=?  where id=? limit 1";
		return this.getJdbc().update(sql, SqlParameter.Instance().withString(passWord).withInt(userId))>0;
	}
}
