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
}
