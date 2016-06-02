package cn.submsg.member.dao;

import java.util.Date;

import com.sr178.common.jdbc.SqlParameter;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bo.MemberVerify;

public class MemberVerifyDao extends SubMsgBaseDao<MemberVerify> {
	/**
	 * 更新成功校验的时间
	 * @param code
	 * @return
	 */
	public boolean updateVerifyTime(String code){
		String sql = "update "+ super.getTable()+" set verify_time=? where verify_str=? and verify_time is null limit 1";
		return super.getJdbc().update(sql,  SqlParameter.Instance().withObject(new Date()).withString(code))>0;
	}
}
