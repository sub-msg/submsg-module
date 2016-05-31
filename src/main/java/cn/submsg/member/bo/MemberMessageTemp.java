package cn.submsg.member.bo;

import java.util.Date;
   /**
    * member_message_temp 实体类
    */ 


public class MemberMessageTemp{
	private Integer id;
	private Integer userId;
	private String tempId;
	private String tempTitle;
	private String tempContent;
	private Integer tempStatus;
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
}

