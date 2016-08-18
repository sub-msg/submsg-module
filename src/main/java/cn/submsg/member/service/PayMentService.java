package cn.submsg.member.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;

import com.sr178.common.jdbc.bean.SqlParamBean;
import com.sr178.game.framework.exception.ServiceException;
import com.sr178.game.framework.log.LogSystem;
import com.sr178.module.utils.ParamCheck;

import cn.submsg.member.bo.MallProducts;
import cn.submsg.member.bo.Member;
import cn.submsg.member.bo.MemberInvoice;
import cn.submsg.member.bo.PaymentOrder;
import cn.submsg.member.constant.PayType;
import cn.submsg.member.dao.MallProductDao;
import cn.submsg.member.dao.MemberDao;
import cn.submsg.member.dao.MemberInvoiceDao;
import cn.submsg.member.dao.MemberMsgInfoDao;
import cn.submsg.member.dao.PayMentOrderDao;
import cn.submsg.pay.alipay.directpay.config.AlipayConfig;
import cn.submsg.pay.alipay.directpay.utils.AlipayNotify;
import cn.submsg.pay.alipay.directpay.utils.AlipaySubmit;

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
	
	/**
	 * 充值成功后发货
	 * @param orderId
	 * @param payType
	 * @param bankOrderId
	 * @param payUserId
	 * @return
	 */
	public boolean afterOrderSuccess(String orderId,PayType payType,String bankOrderId,int payUserId){
		PaymentOrder order = payMentOrderDao.get(new SqlParamBean("order_id", orderId));
		if(order==null){
			LogSystem.warn("支付错误，订单号不存在：orderid="+orderId);
			return false;
		}
		if(payMentOrderDao.updateOrderToSuccess(orderId, payType.getType(), bankOrderId, payUserId)){//更新成功
			LogSystem.info("订单状态更新成功，开始发货");
			MallProducts mallProducts = mallProductDao.get(new SqlParamBean("id", order.getProductId()));
	    	if(mallProducts==null){
	    		LogSystem.info("支付错误，产品找不到"+orderId+",productId="+order.getProductId());
	    		return false;
	    	}
			boolean result = memberMsgInfoDao.addMsgNum(order.getUserId(), order.getProductNum()*mallProducts.getNums());
			if(!result){
				LogSystem.info("支付错误，用户更新发送服务失败［"+orderId+"］,userId="+order.getUserId());
				return false;
			}
			LogSystem.info("发货成功！处理完毕，orderId="+orderId);
			return true;
		}else{
			LogSystem.info("重复发货，不处理！orderId="+orderId);
			return false;
		}
	}
	/**
	 * ali即时到帐支付 请求
	 * @param orderId
	 * @return
	 */
	public String alipayRequest(String orderId){
		   //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = orderId;
        PaymentOrder order = payMentOrderDao.get(new SqlParamBean("order_id", orderId));
        //订单名称，必填
        String subject = "购买短信发送服务";
        //付款金额，必填
        String total_fee = order.getProductAmount()+"";
        //商品描述，可空
        String body = order.getProductDesc();
		//////////////////////////////////////////////////////////////////////////////////
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", AlipayConfig.notify_url);
		sParaTemp.put("return_url", AlipayConfig.return_url);
		sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		//其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
        //如sParaTemp.put("参数名","参数值");
		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
		return sHtmlText;
	}
	/**
	 * 支付宝异步回调
	 * @param out_trade_no
	 * @param trade_no
	 * @param trade_status
	 * @param seller_id
	 * @param params
	 * @return
	 */
	public boolean aliPayNotify(String out_trade_no, String trade_no, String trade_status, String seller_id,
			Map<String, String> params) {
		try {
			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			if (AlipayNotify.verify(params)) {// 验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				// 请在这里加上商户的业务逻辑程序代码
				// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				if (trade_status.equals("TRADE_FINISHED")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
					// 如果有做过处理，不执行商户的业务程序
					// 注意：
					// 退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					LogSystem.info("一个状态为退款的状态，没有做任何处理"+out_trade_no);
				} else if (trade_status.equals("TRADE_SUCCESS")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
					// 如果有做过处理，不执行商户的业务程序
					if (seller_id.equals(AlipayConfig.seller_id)) {
						afterOrderSuccess(out_trade_no, PayType.AliPay, trade_no, 0);
					}
					LogSystem.info("订单成功处理完毕！");
					// 注意：
					// 付款完成后，支付宝系统发送该交易状态通知
				}
				// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				return true; // 请不要修改或删除
				//////////////////////////////////////////////////////////////////////////////////////////
			} else {// 验证失败
				return false;
			}
		} catch (Exception e) {
			LogSystem.error(e, "异常回调！～～");
			return false;
		}
	}

}
