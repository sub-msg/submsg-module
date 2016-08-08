package cn.submsg.member.bo;

import java.util.Date;
   /**
    * doc_reply 实体类
    */ 


public class DocReply{
	private Integer id;
	private Integer docKeyId;
	private Integer userId;
	private String name;
	private Integer replyId;
	private String replContent;
	private Date createdTime;
	public void setId(Integer id){
	this.id=id;
	}
	public Integer getId(){
		return id;
	}
	public void setDocKeyId(Integer docKeyId){
	this.docKeyId=docKeyId;
	}
	public Integer getDocKeyId(){
		return docKeyId;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setName(String name){
	this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setReplyId(Integer replyId){
	this.replyId=replyId;
	}
	public Integer getReplyId(){
		return replyId;
	}
	public void setReplContent(String replContent){
	this.replContent=replContent;
	}
	public String getReplContent(){
		return replContent;
	}
	public void setCreatedTime(Date createdTime){
	this.createdTime=createdTime;
	}
	public Date getCreatedTime(){
		return createdTime;
	}
}

