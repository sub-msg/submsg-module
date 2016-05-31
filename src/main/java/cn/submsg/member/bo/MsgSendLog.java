package cn.submsg.member.bo;

import java.util.Date;
   /**
    * msg_send_log 实体类
    */ 


public class MsgSendLog{
	private Integer id;
	private Integer userId;
	private Integer projectId;
	private String sendId;
	private String msgId;
	private String apiName;
	private String msgContent;
	private String msgSign;
	private Integer bill;
	private String toMobile;
	private Integer status;
	private Date reqTime;
	private Date resTime;
	private String resCode;
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
	public void setProjectId(Integer projectId){
	this.projectId=projectId;
	}
	public Integer getProjectId(){
		return projectId;
	}
	public void setSendId(String sendId){
	this.sendId=sendId;
	}
	public String getSendId(){
		return sendId;
	}
	public void setMsgId(String msgId){
	this.msgId=msgId;
	}
	public String getMsgId(){
		return msgId;
	}
	public void setApiName(String apiName){
	this.apiName=apiName;
	}
	public String getApiName(){
		return apiName;
	}
	public void setMsgContent(String msgContent){
	this.msgContent=msgContent;
	}
	public String getMsgContent(){
		return msgContent;
	}
	public void setMsgSign(String msgSign){
	this.msgSign=msgSign;
	}
	public String getMsgSign(){
		return msgSign;
	}
	public void setBill(Integer bill){
	this.bill=bill;
	}
	public Integer getBill(){
		return bill;
	}
	public void setToMobile(String toMobile){
	this.toMobile=toMobile;
	}
	public String getToMobile(){
		return toMobile;
	}
	public void setStatus(Integer status){
	this.status=status;
	}
	public Integer getStatus(){
		return status;
	}
	public void setReqTime(Date reqTime){
	this.reqTime=reqTime;
	}
	public Date getReqTime(){
		return reqTime;
	}
	public void setResTime(Date resTime){
	this.resTime=resTime;
	}
	public Date getResTime(){
		return resTime;
	}
	public void setResCode(String resCode){
	this.resCode=resCode;
	}
	public String getResCode(){
		return resCode;
	}
	public void setCreatedTime(Date createdTime){
	this.createdTime=createdTime;
	}
	public Date getCreatedTime(){
		return createdTime;
	}
}

