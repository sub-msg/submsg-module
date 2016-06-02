package cn.submsg.member.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.sr178.game.framework.log.LogSystem;

public class MailTempService {
	
	private static final String MAIL_TEMP_DIR = "mailtemp";
	
	

	private Map<String,String> tempCache = Maps.newHashMap();
	
	
	/**
	 * 获取模板邮件
	 * @param tempName
	 * @return
	 * @throws IOException
	 */
	private String getMailTemp(String tempName) throws IOException{
		if(tempCache.containsKey(tempName)){
			return tempCache.get(tempName);
		}else{
			String sDirF = System.getProperty("file.separator");
			InputStream is1 = MailTempService.class.getResourceAsStream("/mailtemp/"+tempName);
			InputStream is2 = MailTempService.class.getResourceAsStream("/mailtemp/"+tempName);
			String content = inputStream2String(is1,"utf-8");
			String contentGbk = inputStream2String(is2,"gb2312");
			System.out.println(content);
			System.out.println(contentGbk);
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
	public static final String[] typesToName = new String[]{"active.html"};
	public String getActiveMailTemp(int tp,String ... params){
	    String content = null;
		try {
			content = getMailTemp(typesToName[tp-1]);
			for(int i=0;i<params.length;i++){
				content = content.replace("@{"+i+"}", params[i]);
			}
			return content;
		} catch (IOException e) {
			LogSystem.error(e, "");
		}
	    return null;
	}
}
