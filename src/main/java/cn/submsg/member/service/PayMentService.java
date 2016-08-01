package cn.submsg.member.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;

import com.sr178.common.jdbc.bean.SqlParamBean;
import com.sr178.game.framework.exception.ServiceException;
import com.sr178.module.utils.ParamCheck;

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
     * 查询订单信息
     * @param orderId
     * @return
     */
    public PaymentOrder getPayMentOrderByOrderId(String orderId){
    	return payMentOrderDao.get(new SqlParamBean("order_id", orderId));
    }
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
    
    /**
     * 修改或创建发票信息
     * @param userId
     * @param invoiceId
     * @param type
     * @param title
     * @param lastname
     * @param firstname
     * @param provice
     * @param city
     * @param area
     * @param address
     * @param mob
     * @param s_address
     * @param s_mob
     * @param s_bank
     * @param s_account
     * @param s_taxcode
     */
	public void createOrEditInvoice(int userId, int invoiceId, String type, String title, String lastname,
			String firstname, String provice, String city, String area, String address, String mob, String s_address,
			String s_mob, String s_bank, String s_account, String s_taxcode) {
		ParamCheck.checkString(title, 1, "发票抬头不能为空");
		ParamCheck.checkString(lastname, 2, "名不能为空");
		ParamCheck.checkString(firstname, 3, "姓不能为空");
		ParamCheck.checkString(provice, 4, "省不能为空");
		ParamCheck.checkString(city, 5, "市不能为空");
		ParamCheck.checkString(area, 6, "县区不能为空");
		ParamCheck.checkString(address, 7, "地址不能为空");
		ParamCheck.checkString(mob, 8, "联系电话不能为空");
    	if(invoiceId==0){//创建
    		MemberInvoice memberInvoice = null;
    		if(type.equals("0")){//普通发票
    			memberInvoice = new MemberInvoice(userId, title, 1, firstname, lastname, provice, city, area, address, mob, new Date());
    		}else{
    			ParamCheck.checkString(s_taxcode, 9, "税务标识不能为空");
    			ParamCheck.checkString(s_address, 10, "公司地址不能为空");
    			ParamCheck.checkString(s_mob, 11, "公司联系电话不能为空");
    			ParamCheck.checkString(s_bank, 12, "公司开户行不能为空");
    			ParamCheck.checkString(s_account, 13, "公司对公账号不能为空");
    			memberInvoice = new MemberInvoice(userId, title, 2, s_taxcode, s_address, s_mob, s_bank, s_account, firstname, lastname, provice, city, area, address, mob, new Date());
    		}
    		if(!memberInvoiceDao.add(memberInvoice)){
    			throw new ServiceException(14, "添加发票信息失败！");
    		}
    	}else{
    		if(type.equals("0")){//普通发票
    			if(!memberInvoiceDao.updateCommonInvoice(invoiceId, userId, title, lastname, firstname, provice, city, area, address, mob)){
    				throw new ServiceException(15, "更新发票信息失败！");
    			}
    		}else{
    			if(!memberInvoiceDao.updateSpecialInvoice(invoiceId, userId,  title, lastname, firstname, provice, city, area, address, mob, s_address, s_mob, s_bank, s_account, s_taxcode)){
    				throw new ServiceException(15, "更新发票信息失败！");
    			}
    		}
    	}
    }
	
	/**
	 * 获取用户发票信息列表
	 * @param userId
	 * @return
	 */
	public List<MemberInvoice> getUserInvoiceList(int userId){
		return memberInvoiceDao.getList(new SqlParamBean("user_id", userId));
	}
	/**
	 * 获取单个用户发票信息
	 * @param id
	 * @return
	 */
	public MemberInvoice getInvoiceById(int id){
		return memberInvoiceDao.get(new SqlParamBean("id", id));
	}
	/**
	 * 删除
	 * @param id
	 */
	public boolean deleteInvoiceById(int id,int userId){
		return memberInvoiceDao.delete(new SqlParamBean("id", id),new SqlParamBean("and", "user_id", userId));
	}

}
