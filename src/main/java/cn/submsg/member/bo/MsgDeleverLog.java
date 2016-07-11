package cn.submsg.member.bo;

import java.util.Date;

public class MsgDeleverLog {

	private Integer id;
	private String msgId;
	private String stat;
	private String destnationId;
	private Date createdTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getDestnationId() {
		return destnationId;
	}
	public void setDestnationId(String destnationId) {
		this.destnationId = destnationId;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
