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
}
