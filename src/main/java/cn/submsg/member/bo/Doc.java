package cn.submsg.member.bo;

import java.util.Date;
   /**
    * doc 实体类
    */ 


public class Doc{
	private Integer id;
	private String docId;
	private String docTitle;
	private String docContnet;
	private Integer type;
	private int orderNum;
	private int level;
	private int parentId;
	private String adminUser;
	private Date updatedTime;
	private Date createdTime;
	public void setId(Integer id){
	this.id=id;
	}
	public Integer getId(){
		return id;
	}
	public void setDocId(String docId){
	this.docId=docId;
	}
	public String getDocId(){
		return docId;
	}
	public void setDocTitle(String docTitle){
	this.docTitle=docTitle;
	}
	public String getDocTitle(){
		return docTitle;
	}
	public void setDocContnet(String docContnet){
	this.docContnet=docContnet;
	}
	public String getDocContnet(){
		return docContnet;
	}
	public void setType(Integer type){
	this.type=type;
	}
	public Integer getType(){
		return type;
	}
	public void setAdminUser(String adminUser){
	this.adminUser=adminUser;
	}
	public String getAdminUser(){
		return adminUser;
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
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
}

