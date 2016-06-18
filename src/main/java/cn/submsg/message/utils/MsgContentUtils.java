package cn.submsg.message.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sr178.game.framework.log.LogSystem;

public class MsgContentUtils {
	
    public static final int STATUS_OK=1;
    public static final int STATUS_NOT=0;
    
	// ucs2编码
	public static final String CHARSET_UCS2 = "UTF-16BE";
    /**
     * 获取短信内容
     * @param tempContent
     * @param vars
     * @param signContent
     * @return
     */
	public static String getContent(String tempContent,String vars,String signContent){
		String result = tempContent;
		JSONObject jsonObject = JSON.parseObject(vars);
		Set<Entry<String, Object>> sets = jsonObject.entrySet();
		for(Entry<String, Object> entry:sets){
			result = result.replace("@var("+entry.getKey()+")", entry.getValue().toString());
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
		return contentLength%138;
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
		String msgContent = "我2【";
		System.out.println(msgContent.getBytes(CHARSET_UCS2).length);
	}
}
