package cn.submsg.admin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.sr178.common.jdbc.bean.IPage;
import com.sr178.common.jdbc.bean.SqlParamBean;
import com.sr178.game.framework.exception.ServiceException;
import com.sr178.module.utils.DateUtils;

import cn.submsg.admin.bean.DailySendNum;
import cn.submsg.admin.bean.PayMentBean;
import cn.submsg.admin.bo.AdminSign;
import cn.submsg.admin.dao.AdminSignDao;
import cn.submsg.member.bean.AdminMsgTempBean;
import cn.submsg.member.bean.MsgTempBean;
import cn.submsg.member.dao.MemberDao;
import cn.submsg.member.dao.MemberMessageSignDao;
import cn.submsg.member.dao.MemberMessageTempDao;
import cn.submsg.member.dao.MsgSendLogDao;
import cn.submsg.member.dao.PayMentOrderDao;
import cn.submsg.message.utils.MsgContentUtils;

public class AdminService {
    @Autowired
	private MsgSendLogDao msgSendLogDao;
    @Autowired
    private PayMentOrderDao payMentOrderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private AdminSignDao adminSignDao;
    @Autowired
    private MemberMessageSignDao memberMessageSignDao;
    @Autowired
    private MemberMessageTempDao memberMessageTempDao;
    
    /**
     * 获取模板列表
     * @param searchContent
     * @param tempStatus
     * @param pageSize
     * @param pageIndex
     * @return
     */
    public IPage<AdminMsgTempBean> getTempPage(String searchContent,int tempStatus,int pageSize,int pageIndex){
    	return memberMessageTempDao.getAdminMsgTempBean(searchContent, pageSize, pageIndex, tempStatus);
    }
    
    /**
     * 审核模板
     * @param tempId
     * @param tempStatus
     * @param newTempId
     * @param unpassReason
     */
    public void updateTemp(String tempId,int tempStatus,String newTempId,String unpassReason){
    	
    	MsgTempBean msgTempBean = memberMessageTempDao.getAdminMsgTempBean(tempId);
    	if(msgTempBean==null){
    		throw new ServiceException(1,"不存在的模板");
    	}
    	if(msgTempBean.getSignStatus()==null){
    		throw new ServiceException(2,"请先审核签名，才能审核模板");
    	}
    	if(msgTempBean.getSignStatus()==MsgContentUtils.STATUS_NOT&&tempStatus==1){
    		throw new ServiceException(2,"请先审核签名，才能审核模板");
    	}
    	memberMessageTempDao.updateTempStatus(tempId, tempStatus, newTempId, unpassReason);
    }
    
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
     * 获取签名列表
     * @param pageSize
     * @param pageIndex
     * @return
     */
    public IPage<AdminSign> getAdminSignPage(String searchStr,int pageSize,int pageIndex){
    	return adminSignDao.getAdminSignList(searchStr,pageSize, pageIndex);
    }
    /**
     * 更新签名状态
     * @param id
     * @param signNum
     */
    @Transactional
    public void updateAdminSign(int id,String signNum){
    	AdminSign adminSign = adminSignDao.get(new SqlParamBean("id", id));
    	if(adminSign==null){
    		throw new ServiceException(1,"签名id不存在。id="+id);
    	}
    	int signStatus = MsgContentUtils.STATUS_OK;
    	if(Strings.isNullOrEmpty(signNum)){
    		signStatus = MsgContentUtils.STATUS_NOT;
    	}
    	adminSignDao.updateSign(id, signNum, signStatus);
    	memberMessageSignDao.updateSignStatus(adminSign.getSignContent(), signNum, signStatus);
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
