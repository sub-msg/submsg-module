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
	/**
	 * 更新订单至已发货
	 * @param orderId
	 * @param pay_type
	 * @param bank_order_id
	 * @param pay_user_id
	 * @return
	 */
	public boolean updateOrderToSuccess(String orderId,int pay_type,String bank_order_id,int pay_user_id){
		String sql = "update "+super.getTable()+" set status=1,pay_type=?,bank_order_id=?,pay_user_id=? where order_id=? and status=0 limit 1";
		return this.getJdbc().update(sql, SqlParameter.Instance().withInt(pay_type).withString(bank_order_id).withInt(pay_user_id).withString(orderId))>0;
	}
}
