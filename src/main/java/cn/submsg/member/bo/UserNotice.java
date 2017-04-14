package cn.submsg.member.bo;

import java.util.Date;
   /**
    * user_notice 实体类
    */ 


public class UserNotice{
	private Integer id;
	private Integer userId;
	private Integer noticeId;
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
	public void setNoticeId(Integer noticeId){
	this.noticeId=noticeId;
	}
	public Integer getNoticeId(){
		return noticeId;
	}
	public void setCreatedTime(Date createdTime){
	this.createdTime=createdTime;
	}
	public Date getCreatedTime(){
		return createdTime;
	}
}

