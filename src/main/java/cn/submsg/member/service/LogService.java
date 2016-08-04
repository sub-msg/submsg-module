package cn.submsg.member.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.sr178.common.jdbc.bean.IPage;
import com.sr178.module.utils.DateUtils;

import cn.submsg.member.bo.MsgSendLog;
import cn.submsg.member.bo.PaymentOrder;
import cn.submsg.member.dao.MsgSendLogDao;
import cn.submsg.member.dao.PayMentOrderDao;

public class LogService {
    @Autowired
	private PayMentOrderDao payMentOrderDao;
    @Autowired
	private MsgSendLogDao msgSendLogDao;
    
    
    /**
     * 短信发送日志
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @param startDate
     * @param endDate
     * @return
     */
    public IPage<MsgSendLog> getMsgSendLog(int userId,int pageIndex,int pageSize,String startDate,String endDate){
    	if(!Strings.isNullOrEmpty(startDate)&&!Strings.isNullOrEmpty(endDate)){
    		startDate = startDate+" 00:00:00";
			endDate = endDate + " 23:59:59";
		}else{
			String todayStr = DateUtils.getDate(new Date());
			startDate = todayStr+" 00:00:00";
			endDate = todayStr+" 23:59:59";
		}
    	return msgSendLogDao.getMsgSendLogPage(userId, pageSize, pageIndex, startDate, endDate);
    }
    /**
     * 充值日志
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @param startDate
     * @param endDate
     * @return
     */
    public IPage<PaymentOrder> getPayMentLog(int userId,int pageIndex,int pageSize,String startDate,String endDate){
    	if(!Strings.isNullOrEmpty(startDate)&&!Strings.isNullOrEmpty(endDate)){
    		startDate = startDate+" 00:00:00";
			endDate = endDate + " 23:59:59";
		}else{
			String todayStr = DateUtils.getDate(new Date());
			startDate = todayStr+" 00:00:00";
			endDate = todayStr+" 23:59:59";
		}
    	return payMentOrderDao.getPayMentOrderPage(userId, pageSize, pageIndex,startDate, endDate);
    }
}
