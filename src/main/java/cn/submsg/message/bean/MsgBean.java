package cn.submsg.message.bean;

import java.util.Date;

import cn.submsg.member.bo.MsgSendLog;

public class MsgBean {

	private String sendId;
	private String to;//发送目标手机号
	private String tempId;
	private String vars;
	private String content;//内容
	private String signNum;//签名的端口号
	private int status;
	private int sendType;//0 移动卓望    1 submail
	private String msgId;
	private String resCode;
	private Date reqTime;
	private Date sendTime;
	private Date resTime;
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSignNum() {
		return signNum;
	}
	public void setSignNum(String signNum) {
		this.signNum = signNum;
	}
	public String getSendId() {
		return sendId;
	}
	public void setSendId(String sendId) {
		this.sendId = sendId;
	}
	public MsgBean(String sendId, String to, String content, String signNum,String tempId,String vars,int sendType) {
		super();
		this.sendId = sendId;
		this.to = to;
		this.content = content;
		this.signNum = signNum;
		this.status = MsgSendLog.ST_CREATE;
		this.reqTime = new Date();
		this.tempId = tempId;
		this.vars = vars;
		this.sendType = sendType;
	}
	public MsgBean() {
		super();
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public Date getReqTime() {
		return reqTime;
	}
	public void setReqTime(Date reqTime) {
		this.reqTime = reqTime;
	}
	public Date getResTime() {
		return resTime;
	}
	public void setResTime(Date resTime) {
		this.resTime = resTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTempId() {
		return tempId;
	}
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getVars() {
		return vars;
	}
	public void setVars(String vars) {
		this.vars = vars;
	}
	
	public int getSendType() {
		return sendType;
	}
	public void setSendType(int sendType) {
		this.sendType = sendType;
	}
	@Override
	public String toString() {
		return "MsgBean [sendId=" + sendId + ", to=" + to + ", content=" + content + ", signNum=" + signNum
				+ ", status=" + status + ", msgId=" + msgId + ", resCode=" + resCode + ", reqTime=" + reqTime
				+ ", sendTime=" + sendTime + ", resTime=" + resTime + "]";
	}
}
