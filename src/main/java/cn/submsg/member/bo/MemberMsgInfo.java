package cn.submsg.member.bo;

import java.util.Date;
   /**
    * member_msg_info 实体类
    */ 


public class MemberMsgInfo{
	private Integer userId;
	private Integer msgNum;
	private Double msgBalance;
	private Integer timeZone;
	private Integer remindType;
	private Integer remindNum;
	private Date createdTime;
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setMsgNum(Integer msgNum){
	this.msgNum=msgNum;
	}
	public Integer getMsgNum(){
		return msgNum;
	}
	public void setTimeZone(Integer timeZone){
	this.timeZone=timeZone;
	}
	public Integer getTimeZone(){
		return timeZone;
	}
	public void setRemindType(Integer remindType){
	this.remindType=remindType;
	}
	public Integer getRemindType(){
		return remindType;
	}
	public void setRemindNum(Integer remindNum){
	this.remindNum=remindNum;
	}
	public Integer getRemindNum(){
		return remindNum;
	}
	public void setCreatedTime(Date createdTime){
	this.createdTime=createdTime;
	}
	public Date getCreatedTime(){
		return createdTime;
	}
	public Double getMsgBalance() {
		return msgBalance;
	}
	public void setMsgBalance(Double msgBalance) {
		this.msgBalance = msgBalance;
	}
}

