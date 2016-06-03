package cn.submsg.member.constant;

public enum MailTempType {
	//用户激活
	 ActiveMember("active_title.html","active_content.html"),ResetPwd("resetpwd_title.html","resetpwd_content.html");
    
    private String titleFileName;
    private String contentFileName;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    MailTempType(String titleFileName,String contentFileName) {
        this.titleFileName = titleFileName;
        this.contentFileName = contentFileName;
    }

	public String getTitleFileName() {
		return titleFileName;
	}

	public String getContentFileName() {
		return contentFileName;
	}	

}
