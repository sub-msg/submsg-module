package cn.submsg.member.bo;

import java.util.Date;
   /**
    * member_message_temp 实体类
    */ 


public class MemberMessageTemp{
	private Integer id;
	private Integer userId;
	private Integer appId;
	private String tempId;
	private String tempTitle;
	private String tempContent;
	private Integer tempStatus;// 0 没有提交审核  -1正在审核   -2 审核不通过   1 审核通过
	private Integer signId;
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
	public void setTempId(String tempId){
	this.tempId=tempId;
	}
	public String getTempId(){
		return tempId;
	}
	public void setTempTitle(String tempTitle){
	this.tempTitle=tempTitle;
	}
	public String getTempTitle(){
		return tempTitle;
	}
	public void setTempContent(String tempContent){
	this.tempContent=tempContent;
	}
	public String getTempContent(){
		return tempContent;
	}
	public void setTempStatus(Integer tempStatus){
	this.tempStatus=tempStatus;
	}
	public Integer getTempStatus(){
		return tempStatus;
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
	public Integer getSignId() {
		return signId;
	}
	public void setSignId(Integer signId) {
		this.signId = signId;
	}
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
}

