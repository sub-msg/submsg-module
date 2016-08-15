package cn.submsg.api.bean;

public class SendMessageResult {
	private String sendId;
	private int fee;
	private int msgNum;
	private Double msgBalance;
	public String getSendId() {
		return sendId;
	}
	public void setSendId(String sendId) {
		this.sendId = sendId;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
	public int getMsgNum() {
		return msgNum;
	}
	public void setMsgNum(int msgNum) {
		this.msgNum = msgNum;
	}
	public Double getMsgBalance() {
		return msgBalance;
	}
	public void setMsgBalance(Double msgBalance) {
		this.msgBalance = msgBalance;
	}
}
