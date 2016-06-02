package cn.submsg.member.bo;

import java.util.Date;
   /**
    * member_verify 实体类
    */ 


public class MemberVerify{
	private Integer id;
	private String verifyStr;
	private Integer userId;
	private Integer effectTime;
	private Integer type;
	private Date verifyTime;
	private Date createdTime;
	public void setId(Integer id){
	this.id=id;
	}
	public Integer getId(){
		return id;
	}
	public void setVerifyStr(String verifyStr){
	this.verifyStr=verifyStr;
	}
	public String getVerifyStr(){
		return verifyStr;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setEffectTime(Integer effectTime){
	this.effectTime=effectTime;
	}
	public Integer getEffectTime(){
		return effectTime;
	}
	public void setType(Integer type){
	this.type=type;
	}
	public Integer getType(){
		return type;
	}
	public void setCreatedTime(Date createdTime){
	this.createdTime=createdTime;
	}
	public Date getCreatedTime(){
		return createdTime;
	}
	public Date getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}
}

