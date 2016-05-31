package cn.submsg.member.bo;

import java.util.Date;
   /**
    * api_req_error_log 实体类
    */ 


public class ApiReqErrorLog{
	private Integer id;
	private Integer userId;
	private Integer projectId;
	private String apiName;
	private String errorCode;
	private String errorDesc;
	private String reqIp;
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
	public void setApiName(String apiName){
	this.apiName=apiName;
	}
	public String getApiName(){
		return apiName;
	}
	public void setErrorCode(String errorCode){
	this.errorCode=errorCode;
	}
	public String getErrorCode(){
		return errorCode;
	}
	public void setErrorDesc(String errorDesc){
	this.errorDesc=errorDesc;
	}
	public String getErrorDesc(){
		return errorDesc;
	}
	public void setReqIp(String reqIp){
	this.reqIp=reqIp;
	}
	public String getReqIp(){
		return reqIp;
	}
	public void setCreatedTime(Date createdTime){
	this.createdTime=createdTime;
	}
	public Date getCreatedTime(){
		return createdTime;
	}
}

