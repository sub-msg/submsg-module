package cn.submsg.member.bo;

import java.util.Date;
   /**
    * member 实体类
    */ 


public class Member{
	private Integer id;
	private String userName;
	private String password;
	private String firstName;
	private String secondName;
	private Integer headPicId;
	private Integer sex;
	private String birthDay;
	private String mobile;
	private String email;
	private String idCardNum;
	private Integer idCardPicId;
	private String province;
	private String city;
	private String area;
	private String addressDetails;
	private String postCode;
	private Integer lang;
	private Integer serviceType;
    private int status;
	private Date updatedTime;
	private Date createdTime;
	public void setId(Integer id){
	this.id=id;
	}
	public Integer getId(){
		return id;
	}
	public void setUserName(String userName){
	this.userName=userName;
	}
	public String getUserName(){
		return userName;
	}
	public void setPassword(String password){
	this.password=password;
	}
	public String getPassword(){
		return password;
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
	public void setHeadPicId(Integer headPicId){
	this.headPicId=headPicId;
	}
	public Integer getHeadPicId(){
		return headPicId;
	}
	public void setSex(Integer sex){
	this.sex=sex;
	}
	public Integer getSex(){
		return sex;
	}
	public void setBirthDay(String birthDay){
	this.birthDay=birthDay;
	}
	public String getBirthDay(){
		return birthDay;
	}
	public void setMobile(String mobile){
	this.mobile=mobile;
	}
	public String getMobile(){
		return mobile;
	}
	public void setEmail(String email){
	this.email=email;
	}
	public String getEmail(){
		return email;
	}
	public void setIdCardNum(String idCardNum){
	this.idCardNum=idCardNum;
	}
	public String getIdCardNum(){
		return idCardNum;
	}
	public void setIdCardPicId(Integer idCardPicId){
	this.idCardPicId=idCardPicId;
	}
	public Integer getIdCardPicId(){
		return idCardPicId;
	}
	public void setProvince(String province){
	this.province=province;
	}
	public String getProvince(){
		return province;
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
	public void setAddressDetails(String addressDetails){
	this.addressDetails=addressDetails;
	}
	public String getAddressDetails(){
		return addressDetails;
	}
	public void setPostCode(String postCode){
	this.postCode=postCode;
	}
	public String getPostCode(){
		return postCode;
	}
	public void setLang(Integer lang){
	this.lang=lang;
	}
	public Integer getLang(){
		return lang;
	}
	public void setServiceType(Integer serviceType){
	this.serviceType=serviceType;
	}
	public Integer getServiceType(){
		return serviceType;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}

