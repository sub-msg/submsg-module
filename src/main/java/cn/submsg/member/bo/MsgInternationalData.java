package cn.submsg.member.bo;

   /**
    * msg_International_data 实体类
    */ 


public class MsgInternationalData{
	private String country;
	private String countryName;
	private String countryCode;
	private Integer regionCode;
	private double price;
	public void setCountry(String country){
	this.country=country;
	}
	public String getCountry(){
		return country;
	}
	public void setCountryName(String countryName){
	this.countryName=countryName;
	}
	public String getCountryName(){
		return countryName;
	}
	public void setCountryCode(String countryCode){
	this.countryCode=countryCode;
	}
	public String getCountryCode(){
		return countryCode;
	}
	public void setRegionCode(Integer regionCode){
	this.regionCode=regionCode;
	}
	public Integer getRegionCode(){
		return regionCode;
	}
	public void setPrice(double price){
	this.price=price;
	}
	public double getPrice(){
		return price;
	}
}

