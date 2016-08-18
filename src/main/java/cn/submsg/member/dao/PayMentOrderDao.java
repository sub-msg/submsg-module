package cn.submsg.member.dao;

import com.sr178.common.jdbc.SqlParameter;
import com.sr178.common.jdbc.bean.IPage;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bo.PaymentOrder;

public class PayMentOrderDao extends SubMsgBaseDao<PaymentOrder> {

	
	/**
	 * 按页查询充值日志
	 * @param userId
	 * @param pageSize
	 * @param pageIndex
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public IPage<PaymentOrder> getPayMentOrderPage(int userId,int pageSize,int pageIndex,String startTime,String endTime){
		String sql = "select * from "+super.getTable()+" where user_id=? and created_time between ? and ? order by created_time desc";
		return this.getJdbc().getListPage(sql, PaymentOrder.class, SqlParameter.Instance().withInt(userId).withString(startTime).withString(endTime), pageSize, pageIndex);
	}
}
