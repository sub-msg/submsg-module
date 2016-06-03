package cn.submsg.member.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.google.common.collect.Maps;
import com.sr178.game.framework.log.LogSystem;


public class MailTempService {
	
	private static final String MAIL_TEMP_DIR = "/mailtemp/";
	
	public static final int TITLE = 1;
	public static final int CONTENT = 2 ;
	

	private Map<String,String> tempCache = Maps.newHashMap();
	
	
	/**
	 * 获取模板邮件
	 * @param tempName
	 * @return
	 * @throws IOException
	 */
	private String getMailTempByFileName(String tempName) throws IOException{
		if(tempCache.containsKey(tempName)){
			return tempCache.get(tempName);
		}else{
			InputStream is1 = MailTempService.class.getResourceAsStream(MAIL_TEMP_DIR+tempName);
			String content = inputStream2String(is1,"utf-8");
			tempCache.put(tempName, content);
			return content;
		}
	}
	
	
	public static String inputStream2String(InputStream is,String charset) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return new String(baos.toByteArray(),charset);
	}

	/**
	 * 获取激活邮件模板内容
	 * @param verifyCode
	 * @return
	 * @throws IOException
	 */
	public String getMailTemp(String fileName,String ... params){
	    String content = null;
		try {
			content = getMailTempByFileName(fileName);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					content = content.replace("@{"+i+"}", params[i]);
				}
			}
			return content;
		} catch (IOException e) {
			LogSystem.error(e, "");
		}
	    return null;
	}
}
