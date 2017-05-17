package cn.submsg.member.constant;

public enum PayType {
    AliPay(1,"阿里支付"),
    JdPay(2,"京东网银支付"),
    AdminPay(3,"管理员后台补单")
    ;
    
    private int type;
	private String desc;
	
	PayType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
