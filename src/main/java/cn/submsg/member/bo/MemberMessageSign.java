package cn.submsg.member.bo;

import java.util.Date;
   /**
    * member_message_sign 实体类
    */ 


public class MemberMessageSign{
	private Integer id;
	private Integer userId;
	private String signContent;
	private Integer signStatus;
	private Integer signPosition;
	private String signNum;
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
	public void setSignContent(String signContent){
	this.signContent=signContent;
	}
	public String getSignContent(){
		return signContent;
	}
	public void setSignStatus(Integer signStatus){
	this.signStatus=signStatus;
	}
	public Integer getSignStatus(){
		return signStatus;
	}
	public void setSignPosition(Integer signPosition){
	this.signPosition=signPosition;
	}
	public Integer getSignPosition(){
		return signPosition;
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
	public String getSignNum() {
		return signNum;
	}
	public void setSignNum(String signNum) {
		this.signNum = signNum;
	}
}

