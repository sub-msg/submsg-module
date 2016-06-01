package cn.submsg.member.bo;

import java.util.Date;
   /**
    * mall_products 实体类
    */ 


public class MallProducts{
	private Integer id;
	private Integer nums;
	private double price;
	private String icon;
	private Date createdTime;
	public void setId(Integer id){
	this.id=id;
	}
	public Integer getId(){
		return id;
	}
	public void setNums(Integer nums){
	this.nums=nums;
	}
	public Integer getNums(){
		return nums;
	}
	public void setPrice(double price){
	this.price=price;
	}
	public double getPrice(){
		return price;
	}
	public void setIcon(String icon){
	this.icon=icon;
	}
	public String getIcon(){
		return icon;
	}
	public void setCreatedTime(Date createdTime){
	this.createdTime=createdTime;
	}
	public Date getCreatedTime(){
		return createdTime;
	}
}

