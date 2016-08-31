package cn.submsg.admin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.sr178.module.utils.DateUtils;

import cn.submsg.admin.bean.DailySendNum;
import cn.submsg.admin.bean.PayMentBean;
import cn.submsg.member.dao.MemberDao;
import cn.submsg.member.dao.MsgSendLogDao;
import cn.submsg.member.dao.PayMentOrderDao;

public class AdminService {
    @Autowired
	private MsgSendLogDao msgSendLogDao;
    @Autowired
    private PayMentOrderDao payMentOrderDao;
    @Autowired
    private MemberDao memberDao;
    
    
    /**
     * 查询每日发送量
     * @param startDate
     * @param endDate
     * @return
     */
    public List<DailySendNum> getDailySendNumListByTime(String startDate,String endDate){
    	if(!Strings.isNullOrEmpty(startDate)&&!Strings.isNullOrEmpty(endDate)){
    		startDate = startDate+" 00:00:00";
			endDate = endDate + " 23:59:59";
		}else{
			String sevendayStr =  DateUtils.getDate(DateUtils.addDay(new Date(), -31));
			String todayStr = DateUtils.getDate(new Date());
			startDate = sevendayStr+" 00:00:00";
			endDate = todayStr+" 23:59:59";
		}
    	return msgSendLogDao.getDailySendNumList(startDate, endDate);
    }
    
    /**
     * 获取最近几条充值记录
     * @param num
     * @return
     */
    public List<PayMentBean> getFinishPayMentOrderList(int num){
    	return payMentOrderDao.getPayMentBean(num);
    }
    
    /**
     * 总用户数
     * @return
     */
    public int getTotleMemberCount(){
    	return memberDao.getTotalMemberCount();
    }
    /**
     * 激活用户数
     * @return
     */
    public int getActiveMemberCount(){
    	return memberDao.getActiveMemberCount();
    }
    /**
     * 付款用户数
     * @return
     */
    public int getPaymentMemberCount(){
    	return payMentOrderDao.getTotalPayUserCount();
    }
    /**
     * 充值总额
     * @return
     */
    public Double getTotalPayAmount(){
    	return payMentOrderDao.getTotalPayAmount();
    }
}
