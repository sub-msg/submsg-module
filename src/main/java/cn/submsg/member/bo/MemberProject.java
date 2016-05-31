package cn.submsg.member.bo;

import java.util.Date;
   /**
    * member_project 实体类
    */ 


public class MemberProject{
	private Integer id;
	private Integer userId;
	private String projectName;
	private String projectKey;
	private Integer maxSendNumDaily;
	private String whiteIp;
	private Integer status;
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
	public void setProjectName(String projectName){
	this.projectName=projectName;
	}
	public String getProjectName(){
		return projectName;
	}
	public void setProjectKey(String projectKey){
	this.projectKey=projectKey;
	}
	public String getProjectKey(){
		return projectKey;
	}
	public void setMaxSendNumDaily(Integer maxSendNumDaily){
	this.maxSendNumDaily=maxSendNumDaily;
	}
	public Integer getMaxSendNumDaily(){
		return maxSendNumDaily;
	}
	public void setWhiteIp(String whiteIp){
	this.whiteIp=whiteIp;
	}
	public String getWhiteIp(){
		return whiteIp;
	}
	public void setStatus(Integer status){
	this.status=status;
	}
	public Integer getStatus(){
		return status;
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

