package cn.submsg.member.bo;

import java.util.Date;
   /**
    * payment_order 实体类
    */ 


public class PaymentOrder{
	private Integer id;
	private String orderId;
	private Integer userId;
	private Integer productId;
	private Integer productNum;
	private String productDesc;
	private double productAmount;
	private Integer invoiceId;
	private Integer status;
	private Integer payUserId;
	private Integer payType;
	private String bankOrderId;
	private Date updatedTime;
	private Date createdTime;
	public void setId(Integer id){
	this.id=id;
	}
	public Integer getId(){
		return id;
	}
	public void setOrderId(String orderId){
	this.orderId=orderId;
	}
	public String getOrderId(){
		return orderId;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setProductId(Integer productId){
	this.productId=productId;
	}
	public Integer getProductId(){
		return productId;
	}
	public void setProductNum(Integer productNum){
	this.productNum=productNum;
	}
	public Integer getProductNum(){
		return productNum;
	}
	public void setProductDesc(String productDesc){
	this.productDesc=productDesc;
	}
	public String getProductDesc(){
		return productDesc;
	}
	public void setProductAmount(double productAmount){
	this.productAmount=productAmount;
	}
	public double getProductAmount(){
		return productAmount;
	}
	public Integer getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}
	public void setStatus(Integer status){
	this.status=status;
	}
	public Integer getStatus(){
		return status;
	}
	public void setPayUserId(Integer payUserId){
	this.payUserId=payUserId;
	}
	public Integer getPayUserId(){
		return payUserId;
	}
	public void setPayType(Integer payType){
	this.payType=payType;
	}
	public Integer getPayType(){
		return payType;
	}
	public void setBankOrderId(String bankOrderId){
	this.bankOrderId=bankOrderId;
	}
	public String getBankOrderId(){
		return bankOrderId;
	}
	public void setUpdatedTime(Date updatedTime){
	this.updatedTime=updatedTime;
	}
	public Date getUpdatedTime(){
		return updatedTime;
	}
	public void setCreatedTime(Date createdTime){
	this.createdTime=createdTime;
	}
	public Date getCreatedTime(){
		return createdTime;
	}
	public PaymentOrder(String orderId, Integer userId, Integer productId, Integer productNum, String productDesc,
			double productAmount, Integer invoiceId, Integer status, Date createdTime) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.productId = productId;
		this.productNum = productNum;
		this.productDesc = productDesc;
		this.productAmount = productAmount;
		this.invoiceId = invoiceId;
		this.status = status;
		this.createdTime = createdTime;
	}
	public PaymentOrder() {
		super();
	}
	
}

