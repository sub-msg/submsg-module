package cn.submsg.message.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.sr178.game.framework.log.LogSystem;

public class MsgContentUtils {
	
	public static final int SENDTYPE_ZW = 1;//发送类型  卓望
	public static final int SENDTYPE_SUBMAIL = 2;//发送类型  submail
	public static final int SENDTYPE_YP = 3;//云片主机
	public static final int SENDTYPE_MD = 4;//秒滴
	
    public static final int STATUS_OK=1;
    public static final int STATUS_NOT=0;
    
	// ucs2编码
	public static final String CHARSET_UCS2 = "UTF-16BE";
	
	public static final int MAX_ONE_MSG_LENGTH = 138;
    /**
     * 获取短信内容
     * @param tempContent
     * @param vars
     * @param signContent
     * @return
     */
	public static String getContent(String tempContent,String vars,String signContent){
		String result = tempContent;
		if (!Strings.isNullOrEmpty(vars)) {
			JSONObject jsonObject = JSON.parseObject(vars);
			Set<Entry<String, Object>> sets = jsonObject.entrySet();
			for (Entry<String, Object> entry : sets) {
				result = result.replace("@var(" + entry.getKey() + ")", entry.getValue().toString());
			}
		}
		return getFinallySignContent(signContent)+result;
	}
	
	/**
	 * 获取短信将消耗的发送许可数
	 * @param msgContent
	 * @return
	 */
	public static int getFeeNum(String msgContent){
		int contentLength=0;
		try {
			contentLength = msgContent.getBytes(CHARSET_UCS2).length;
		} catch (UnsupportedEncodingException e) {
			LogSystem.error(e, "");
		}
		//小于最大长度 直接返回1
		if(contentLength<=MAX_ONE_MSG_LENGTH){
			return 1;
		}
		//如果大于  则看能否被整除  如果能  则直接相除得到商  否则求商后加1
		if(contentLength%MAX_ONE_MSG_LENGTH==0){
			return contentLength/MAX_ONE_MSG_LENGTH;
		}else{
			return contentLength/MAX_ONE_MSG_LENGTH+1;
		}
	}
	
	public static int getOtherFeeNum(String msgContent) {
		if (msgContent == null) {
			return 1;
		}
		int contentLength = msgContent.length();
		// 小于最大长度 直接返回1
		if (contentLength <= 67) {
			return 1;
		}
		// 如果大于 则看能否被整除 如果能 则直接相除得到商 否则求商后加1
		if (contentLength % 67 == 0) {
			return contentLength / MAX_ONE_MSG_LENGTH;
		} else {
			return contentLength / 67 + 1;
		}

	}
	/**
	 * 获取签名
	 * @param signContent
	 * @return
	 */
	public static String getFinallySignContent(String signContent){
		return "【"+signContent.trim()+"】";
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String msgContent = "【积分游戏大平台】您卖出的一币对方已打款，请在48小时内登录用户中心确认,否则将扣除您一颗信誉星。5天后未进行操作将暂封您的一币卖出功能！";
		System.out.println(msgContent.getBytes(CHARSET_UCS2).length);
		
		System.out.println(msgContent.length());
	}
}
