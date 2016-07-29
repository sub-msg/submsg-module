package cn.submsg.member.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;

import com.sr178.common.jdbc.bean.SqlParamBean;
import com.sr178.game.framework.exception.ServiceException;

import cn.submsg.member.bo.MallProducts;
import cn.submsg.member.bo.Member;
import cn.submsg.member.bo.MemberInvoice;
import cn.submsg.member.bo.PaymentOrder;
import cn.submsg.member.dao.MallProductDao;
import cn.submsg.member.dao.MemberDao;
import cn.submsg.member.dao.MemberInvoiceDao;
import cn.submsg.member.dao.MemberMsgInfoDao;
import cn.submsg.member.dao.PayMentOrderDao;

public class PayMentService {
	@Autowired
	private PayMentOrderDao payMentOrderDao;
	
    @Autowired
	private MallProductDao mallProductDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberMsgInfoDao memberMsgInfoDao;
    @Autowired
    private MemberInvoiceDao memberInvoiceDao; 
    //订单序列号
    private AtomicInteger sequence = new AtomicInteger(1000);
    
    /**
     * 创建订单
     * @param userId
     * @param productId
     * @param num
     * @param invoiceId
     * @return
     */
    public String creatOrder(int userId,int productId,int num,int invoiceId){
    	String orderId = generatorPayOrder();
    	Member member = memberDao.get(new SqlParamBean("id", userId));
    	if(member==null){
    		throw new ServiceException(1, "用户不存在,userId="+userId);
    	}
    	MallProducts mallProducts = mallProductDao.get(new SqlParamBean("id", productId));
    	if(mallProducts==null){
    		throw new ServiceException(2, "产品id不存在,productId="+productId);
    	}
    	if(invoiceId>0){//大于0  是需要发票的   等于0是不需要发票的
    		MemberInvoice memberInvoice = memberInvoiceDao.get(new SqlParamBean("id", invoiceId));
    		if(memberInvoice==null){
    			throw new ServiceException(3, "发票id不存在,invoiceId="+invoiceId);
    		}
    	}
    	PaymentOrder paymentOrder = new PaymentOrder(orderId, userId, productId, num, "购买数量："+mallProducts.getNums(), mallProducts.getPrice()*num, invoiceId, 0, new Date());
    	if(!payMentOrderDao.add(paymentOrder)){
    		throw new ServiceException(4, "创建订单失败！");
    	}
    	return orderId;
    }
    /**
     * 创建订单
     * @return
     */
    private String generatorPayOrder(){
    	String orderNum = "SM";
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
    	orderNum = orderNum + sdf.format(new Date());
    	int num = sequence.incrementAndGet();
    	if(num>9999){
    		num = 1000;
    		sequence.set(1000);
    	}
    	orderNum = orderNum+num;
    	return orderNum;
    }

}
