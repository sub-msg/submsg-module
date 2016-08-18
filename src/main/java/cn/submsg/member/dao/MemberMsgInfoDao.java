package cn.submsg.member.dao;

import com.sr178.common.jdbc.SqlParameter;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bo.MemberMsgInfo;

public class MemberMsgInfoDao extends SubMsgBaseDao<MemberMsgInfo> {
    /**
     * 减发送许可数量
     * @param userId
     * @param reduceNum
     * @return
     */
	public boolean reduceMsgNum(int userId,int reduceNum){
		String sql = "update "+super.getTable()+" set msg_num=msg_num-? where user_id=? and msg_num-?>=0 limit 1";
		return super.getJdbc().update(sql, SqlParameter.Instance().withInt(reduceNum).withInt(userId).withInt(reduceNum))>0;
	}
	
	
	public boolean addMsgNum(int userId,int addNum){
		String sql = "update "+super.getTable()+" set msg_num=msg_num+? where user_id=? limit 1";
		return super.getJdbc().update(sql, SqlParameter.Instance().withInt(addNum).withInt(userId))>0;
	}
	
	
	public boolean reduceMsgBalance(int userId,double reduceNum){
		String sql = "update "+super.getTable()+" set msg_balance=msg_balance-? where user_id=? and msg_balance-?>=0 limit 1";
		return super.getJdbc().update(sql, SqlParameter.Instance().withDouble(reduceNum).withInt(userId).withDouble(reduceNum))>0;
	}
	
	
	public boolean addMsgBalance(int userId,double addNum){
		String sql = "update "+super.getTable()+" set msg_balance=msg_balance+? where user_id=?  limit 1";
		return super.getJdbc().update(sql, SqlParameter.Instance().withDouble(addNum).withInt(userId))>0;
	}
}
