package cn.submsg.admin.bean;

public class DailySendNum {

	private String time;//日期
	private String times;//发送次数
	private int loseTimes;//发送失败次数
	private int nums;//发送条数
	private int loseNums;//发送失败数量
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getNums() {
		return nums;
	}
	public void setNums(int nums) {
		this.nums = nums;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public int getLoseNums() {
		return loseNums;
	}
	public void setLoseNums(int loseNums) {
		this.loseNums = loseNums;
	}
	public int getLoseTimes() {
		return loseTimes;
	}
	public void setLoseTimes(int loseTimes) {
		this.loseTimes = loseTimes;
	}
}
