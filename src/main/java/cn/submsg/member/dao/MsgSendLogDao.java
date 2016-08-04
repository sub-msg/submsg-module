package cn.submsg.member.dao;

import java.util.Date;


import com.sr178.common.jdbc.SqlParameter;
import com.sr178.common.jdbc.bean.IPage;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bo.MsgSendLog;

public class MsgSendLogDao extends SubMsgBaseDao<MsgSendLog> {
	/**
	 * 更新状态至成功
	 * @param sendId
	 * @param status
	 * @param resTime
	 * @param resCode
	 * @param msgId
	 * @return
	 */
	public boolean updateLogStatusToSuccess(String sendId,Date resTime,String resCode,String msgId){
		String sql = "update "+super.getTable()+" set msg_id=?,res_time=?,res_code=?,status="+MsgSendLog.ST_SUCCESS+" where send_id=? limit 1";
		boolean result = this.getJdbc().update(sql, SqlParameter.Instance().withString(msgId).withObject(resTime).withString(resCode).withString(sendId))>0;
		return result;
	}
	/**
	 * 更新状态至失败
	 * @param sendId
	 * @param resTime
	 * @return
	 */
	public boolean updateLogStatusToFail(String sendId,Date resTime){
		String sql = "update "+super.getTable()+" set res_time=?,status="+MsgSendLog.ST_FAIL+" where send_id=? limit 1";
		return this.getJdbc().update(sql, SqlParameter.Instance().withObject(resTime).withString(sendId))>0;
	}
	/**
	 * 更新状态至失败
	 * @param sendId
	 * @param resTime
	 * @return
	 */
	public boolean updateLogStatusToSend(String sendId,Date sendTime){
		String sql = "update "+super.getTable()+" set send_time=?,status="+MsgSendLog.ST_SEND+" where send_id=? limit 1";
		boolean result =  this.getJdbc().update(sql, SqlParameter.Instance().withObject(sendTime).withString(sendId))>0;
		return result;
	}
	
	/**
	 * 按页查询消息日志
	 * @param userId
	 * @param pageSize
	 * @param pageIndex
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public IPage<MsgSendLog> getMsgSendLogPage(int userId,int pageSize,int pageIndex,String startTime,String endTime){
		String sql = "select * from "+super.getTable()+" where user_id=? and req_time between ? and ?";
		return this.getJdbc().getListPage(sql, MsgSendLog.class, SqlParameter.Instance().withInt(userId).withString(startTime).withString(endTime), pageSize, pageIndex);
	}
}
