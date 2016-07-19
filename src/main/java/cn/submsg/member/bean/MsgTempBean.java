package cn.submsg.member.bean;

import java.util.Date;

public class MsgTempBean {

	private String tempId;
	private String tempTitle;
	private String tempContent;
	private int tempStatus;
	
	private Integer signId;
	private String signContent;
	private Integer signStatus;
	private String signNum;
	
	private Date updatedTime;
	private Date createdTime;
	public String getTempId() {
		return tempId;
	}
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}
	public String getTempTitle() {
		return tempTitle;
	}
	public void setTempTitle(String tempTitle) {
		this.tempTitle = tempTitle;
	}
	public String getTempContent() {
		return tempContent;
	}
	public void setTempContent(String tempContent) {
		this.tempContent = tempContent;
	}
	public int getTempStatus() {
		return tempStatus;
	}
	public void setTempStatus(int tempStatus) {
		this.tempStatus = tempStatus;
	}
	public Integer getSignId() {
		return signId;
	}
	public void setSignId(Integer signId) {
		this.signId = signId;
	}
	public String getSignContent() {
		return signContent;
	}
	public void setSignContent(String signContent) {
		this.signContent = signContent;
	}
	public Integer getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(Integer signStatus) {
		this.signStatus = signStatus;
	}
	public String getSignNum() {
		return signNum;
	}
	public void setSignNum(String signNum) {
		this.signNum = signNum;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
}
