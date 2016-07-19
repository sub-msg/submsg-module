package cn.submsg.message.service;

import com.sr178.module.utils.JedisUtils;

import cn.submsg.member.bo.MsgDeleverLog;
import cn.submsg.message.bean.MsgBean;

public class MessageQueueService {
	
	private static final String MESSAGE_QUEUE_REQ_KEY = "sub_msg_pool_req_key";
	private static final String MESSAGE_QUEUE_RES_KEY = "sub_msg_pool_res_key";
	
	
	private static final String MESSAGE_QUEUE_DELEVER_REQ_KEY = "sub_msg_pool_delever_req_key";
	
	private MessageQueue<MsgBean> reqQueue = new MessageQueue<MsgBean>(JedisUtils.getPool(), MESSAGE_QUEUE_REQ_KEY,MsgBean.class);
	
	private MessageQueue<MsgBean> resQueue = new MessageQueue<MsgBean>(JedisUtils.getPool(), MESSAGE_QUEUE_RES_KEY,MsgBean.class);
	
	
	
	private MessageQueue<MsgDeleverLog> deleverReqQueue = new MessageQueue<MsgDeleverLog>(JedisUtils.getPool(), MESSAGE_QUEUE_DELEVER_REQ_KEY,MsgDeleverLog.class);
	
	/**
	 * 将请求消息插入队列
	 * @param msgBean
	 * @return
	 */
	public boolean pushReqMsg(MsgBean msgBean){
		 return reqQueue.pushMsg(msgBean)==null?false:true;
	}
	/**
	 * 阻塞弹出一条请求消息
	 * @return
	 */
	public MsgBean blockReqPopMsg(){
		return reqQueue.blockPopMsg();
	}
	/**
	 * 将响应消息插入队列
	 * @param msgBean
	 * @return
	 */
	public boolean pushResMsg(MsgBean msgBean){
		 return resQueue.pushMsg(msgBean)==null?false:true;
	}
	/**
	 * 阻塞弹出一条响应消息
	 * @return
	 */
	public MsgBean blockResPopMsg(){
		return resQueue.blockPopMsg();
	}
	/**
	 * 推送一条 通知消息
	 * @param msgDeleverLog
	 * @return
	 */
	public boolean pushDeleverReqMsg(MsgDeleverLog msgDeleverLog){
		return deleverReqQueue.pushMsg(msgDeleverLog)==null?false:true;
	}
	/**
	 * 弹出一条 通知消息
	 * @return
	 */
	public MsgDeleverLog blockDeleverResMsg(){
		return deleverReqQueue.blockPopMsg();
	}
	
}
