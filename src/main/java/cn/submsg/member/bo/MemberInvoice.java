package cn.submsg.member.bo;

import java.util.Date;
   /**
    * member_invoice 实体类
    */ 


public class MemberInvoice{
	private Integer id;
	private Integer userId;
	private String invoiceName;
	private Integer invoiceType;
	private String taxpayerTag;
	private String comAddress;
	private String comPhone;
	private String comBankName;
	private String comBankAccount;
	private String firstName;
	private String secondName;
	private String provice;
	private String city;
	private String area;
	private String address;
	private String phone;
	private Date createdTime;
	
	
//	public MemberInvoice(){
//		id=1;
//		invoiceName="发票抬头";
//		taxpayerTag="税务编号";
//		comAddress="公司注册地址";
//		comPhone="公司电话";
//		comBankName="公司开户行";
//		comBankAccount="开户行帐号";
//		firstName="孟";
//		secondName="潮";
//		provice="湖南";
//		city="长沙";
//		area="宁乡县";
//		address="地址";
//		phone="电话";
//	}
	
	public void setId(Integer id){
	this.id=id;
	}
	public Integer getId(){
		return id;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setInvoiceName(String invoiceName){
	this.invoiceName=invoiceName;
	}
	public String getInvoiceName(){
		return invoiceName;
	}
	public void setInvoiceType(Integer invoiceType){
	this.invoiceType=invoiceType;
	}
	public Integer getInvoiceType(){
		return invoiceType;
	}
	public void setTaxpayerTag(String taxpayerTag){
	this.taxpayerTag=taxpayerTag;
	}
	public String getTaxpayerTag(){
		return taxpayerTag;
	}
	public void setComAddress(String comAddress){
	this.comAddress=comAddress;
	}
	public String getComAddress(){
		return comAddress;
	}
	public void setComPhone(String comPhone){
	this.comPhone=comPhone;
	}
	public String getComPhone(){
		return comPhone;
	}
	public void setComBankName(String comBankName){
	this.comBankName=comBankName;
	}
	public String getComBankName(){
		return comBankName;
	}
	public void setComBankAccount(String comBankAccount){
	this.comBankAccount=comBankAccount;
	}
	public String getComBankAccount(){
		return comBankAccount;
	}
	public void setFirstName(String firstName){
	this.firstName=firstName;
	}
	public String getFirstName(){
		return firstName;
	}
	public void setSecondName(String secondName){
	this.secondName=secondName;
	}
	public String getSecondName(){
		return secondName;
	}
	public void setProvice(String provice){
	this.provice=provice;
	}
	public String getProvice(){
		return provice;
	}
	public void setCity(String city){
	this.city=city;
	}
	public String getCity(){
		return city;
	}
	public void setArea(String area){
	this.area=area;
	}
	public String getArea(){
		return area;
	}
	public void setAddress(String address){
	this.address=address;
	}
	public String getAddress(){
		return address;
	}
	public void setPhone(String phone){
	this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setCreatedTime(Date createdTime){
	this.createdTime=createdTime;
	}
	public Date getCreatedTime(){
		return createdTime;
	}
	public MemberInvoice(Integer userId, String invoiceName, Integer invoiceType, String taxpayerTag, String comAddress,
			String comPhone, String comBankName, String comBankAccount, String firstName, String secondName,
			String provice, String city, String area, String address, String phone, Date createdTime) {
		super();
		this.userId = userId;
		this.invoiceName = invoiceName;
		this.invoiceType = invoiceType;
		this.taxpayerTag = taxpayerTag;
		this.comAddress = comAddress;
		this.comPhone = comPhone;
		this.comBankName = comBankName;
		this.comBankAccount = comBankAccount;
		this.firstName = firstName;
		this.secondName = secondName;
		this.provice = provice;
		this.city = city;
		this.area = area;
		this.address = address;
		this.phone = phone;
		this.createdTime = createdTime;
	}
	public MemberInvoice() {
		super();
	}
	public MemberInvoice(Integer userId, String invoiceName, Integer invoiceType, String firstName, String secondName,
			String provice, String city, String area, String address, String phone, Date createdTime) {
		super();
		this.userId = userId;
		this.invoiceName = invoiceName;
		this.invoiceType = invoiceType;
		this.firstName = firstName;
		this.secondName = secondName;
		this.provice = provice;
		this.city = city;
		this.area = area;
		this.address = address;
		this.phone = phone;
		this.createdTime = createdTime;
	}
}

