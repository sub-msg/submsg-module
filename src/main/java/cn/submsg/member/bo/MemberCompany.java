package cn.submsg.member.bo;

import java.util.Date;
   /**
    * member_company 实体类
    */ 


public class MemberCompany{
	private Integer id;
	private Integer userId;
	private String comIndustryBig;
	private String comIndustrySmall;
	private String comName;
	private String comSite;
	private String comTelphone;
	private String comFax;
	private String comProvince;
	private String comCity;
	private String comTown;
	private String comAddressDetails;
	private String comPostCode;
	private Integer businessLicencePicId;
	private Integer organizationCodePicId;
	private Date updatedTime;
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
	public void setComIndustryBig(String comIndustryBig){
	this.comIndustryBig=comIndustryBig;
	}
	public String getComIndustryBig(){
		return comIndustryBig;
	}
	public void setComIndustrySmall(String comIndustrySmall){
	this.comIndustrySmall=comIndustrySmall;
	}
	public String getComIndustrySmall(){
		return comIndustrySmall;
	}
	public void setComName(String comName){
	this.comName=comName;
	}
	public String getComName(){
		return comName;
	}
	public void setComSite(String comSite){
	this.comSite=comSite;
	}
	public String getComSite(){
		return comSite;
	}
	public void setComTelphone(String comTelphone){
	this.comTelphone=comTelphone;
	}
	public String getComTelphone(){
		return comTelphone;
	}
	public void setComFax(String comFax){
	this.comFax=comFax;
	}
	public String getComFax(){
		return comFax;
	}
	public void setComProvince(String comProvince){
	this.comProvince=comProvince;
	}
	public String getComProvince(){
		return comProvince;
	}
	public void setComCity(String comCity){
	this.comCity=comCity;
	}
	public String getComCity(){
		return comCity;
	}
	public void setComTown(String comTown){
	this.comTown=comTown;
	}
	public String getComTown(){
		return comTown;
	}
	public void setComAddressDetails(String comAddressDetails){
	this.comAddressDetails=comAddressDetails;
	}
	public String getComAddressDetails(){
		return comAddressDetails;
	}
	public void setComPostCode(String comPostCode){
	this.comPostCode=comPostCode;
	}
	public String getComPostCode(){
		return comPostCode;
	}
	public void setBusinessLicencePicId(Integer businessLicencePicId){
	this.businessLicencePicId=businessLicencePicId;
	}
	public Integer getBusinessLicencePicId(){
		return businessLicencePicId;
	}
	public void setOrganizationCodePicId(Integer organizationCodePicId){
	this.organizationCodePicId=organizationCodePicId;
	}
	public Integer getOrganizationCodePicId(){
		return organizationCodePicId;
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
}

