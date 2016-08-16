package cn.submsg.member.bo;

import java.util.Date;
   /**
    * msg_send_log 实体类
    */ 


public class MsgSendLog{
	private Integer id;
	private Integer userId;
	//appid
	private Integer projectId;
	//我放生成的id
	private String sendId;
	//发送成功后从网关得到的id
	private String msgId;
	//api的名称
	private String apiName;
	//消息完整内容
	private String msgContent;
	//签名内容
	private String msgSign;
	//消费的短信条数
	private Integer bill;
	//短信的发送价格（国际短信才有的参数）
	private Double price;
	//发到的手机号
	private String toMobile;
	//0 移动卓望    1 submail
	private int sendType;
	//状态
	private Integer status;
	//去请求时间
	private Date reqTime;
	
	private Date sendTime;
	//响应时间
	private Date resTime;
	//响应代码
	private String resCode;
	//创建时间
	private Date createdTime;
	
	public static final int ST_CREATE = 0;//创建
	public static final int ST_SEND = 1;//正在发送
	public static final int ST_SUCCESS = 2;//发送成功
	public static final int ST_FAIL = 3;//发送失败
	public static final int ST_DELEVER_SUCCESS = 4;//投递成功 即受到了响应消息
	
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
	public void setSendId(String sendId){
	this.sendId=sendId;
	}
	public String getSendId(){
		return sendId;
	}
	public void setMsgId(String msgId){
	this.msgId=msgId;
	}
	public String getMsgId(){
		return msgId;
	}
	public void setApiName(String apiName){
	this.apiName=apiName;
	}
	public String getApiName(){
		return apiName;
	}
	public void setMsgContent(String msgContent){
	this.msgContent=msgContent;
	}
	public String getMsgContent(){
		return msgContent;
	}
	public void setMsgSign(String msgSign){
	this.msgSign=msgSign;
	}
	public String getMsgSign(){
		return msgSign;
	}
	public void setBill(Integer bill){
	this.bill=bill;
	}
	public Integer getBill(){
		return bill;
	}
	public void setToMobile(String toMobile){
	this.toMobile=toMobile;
	}
	public String getToMobile(){
		return toMobile;
	}
	public void setStatus(Integer status){
	this.status=status;
	}
	public Integer getStatus(){
		return status;
	}
	public void setReqTime(Date reqTime){
	this.reqTime=reqTime;
	}
	public Date getReqTime(){
		return reqTime;
	}
	public void setResTime(Date resTime){
	this.resTime=resTime;
	}
	public Date getResTime(){
		return resTime;
	}
	public void setResCode(String resCode){
	this.resCode=resCode;
	}
	public String getResCode(){
		return resCode;
	}
	public void setCreatedTime(Date createdTime){
	this.createdTime=createdTime;
	}
	public Date getCreatedTime(){
		return createdTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public int getSendType() {
		return sendType;
	}
	public void setSendType(int sendType) {
		this.sendType = sendType;
	}
	public MsgSendLog(Integer userId, Integer projectId, String sendId,  String apiName, String msgContent,
			String msgSign, Integer bill, String toMobile,int sendType, Integer status, Date reqTime, Date createdTime) {
		super();
		this.userId = userId;
		this.projectId = projectId;
		this.sendId = sendId;
		this.apiName = apiName;
		this.msgContent = msgContent;
		this.msgSign = msgSign;
		this.bill = bill;
		this.toMobile = toMobile;
		this.sendType = sendType;
		this.status = status;
		this.reqTime = reqTime;
		this.createdTime = createdTime;
	}
	public MsgSendLog() {
		super();
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
}

