package cn.submsg.member.constant;

public enum VerifyType {
	MemberActive(1,30,MailTempType.ActiveMember),MemberPwdReset(2,30,MailTempType.ResetPwd);
	
	VerifyType(int type,int effectTime,MailTempType mailTempType){
		this.type = type;
		this.effectTime = effectTime;
		this.mailTempType = mailTempType;
	}
	//校验类型  用户激活
	private int type;
	private int effectTime;
	private MailTempType mailTempType;
	public int getType() {
		return type;
	}
	public int getEffectTime() {
		return effectTime;
	}
	public MailTempType getMailTempType() {
		return mailTempType;
	}
}
