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
}

