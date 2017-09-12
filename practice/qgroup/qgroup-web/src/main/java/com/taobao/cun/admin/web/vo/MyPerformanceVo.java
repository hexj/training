package com.taobao.cun.admin.web.vo;

public class MyPerformanceVo {
	private String levelType ; // 8种情况中的一种， 由前端获取后进行信息拼装   
	private String  gmvDiff  ; //gmv还差xx达到xx合伙人 ，单位为元   根据levelType  来判断是否获取此值    
	private int   rankedDiff ; //需要赶超xx人 达到xx合伙人， 根据levelType  来判断是否获取此值 
	private int   rankedDiff2 ; //需要赶超xx人 达到xx合伙人， 根据levelType  来判断是否获取此值 
	private long todayOrderCount; //今日订单量
	private String todayOrderGmv ; //今日gmv,单位为元
    private long thisMonthOrderCount; //今月订单量
    private String thisMonthOrderGmv ; //今月gmv,单位为元
	private String  goodPartnerLevel; // 优秀合伙人的gmv level  单位为元
	private String  starPartnerLevel; //明星合伙人的gmv level 单位为元
	private String curWeek; //当前星期
	private String curMonthDay; //当前月日
	private String curYearMonth;//当前年月
	
	private long starPartnerTop;//明星合伙人排名条件
	
	private long goodPartnerTop;//优秀合伙人排名条件
	
	private long starPartnerOrderNum;//明星合伙人所需订单数
	
	private long goodPartnerOrderNum;//优秀合伙人所需订单数
	
	public String getCurWeek() {
		return curWeek;
	}
	public void setCurWeek(String curWeek) {
		this.curWeek = curWeek;
	}
	public String getCurMonthDay() {
		return curMonthDay;
	}
	public void setCurMonthDay(String curMonthDay) {
		this.curMonthDay = curMonthDay;
	}
	public String getCurYearMonth() {
		return curYearMonth;
	}
	public void setCurYearMonth(String curYearMonth) {
		this.curYearMonth = curYearMonth;
	}
	public String getGoodPartnerLevel() {
		return goodPartnerLevel;
	}
	public void setGoodPartnerLevel(String goodPartnerLevel) {
		this.goodPartnerLevel = goodPartnerLevel;
	}
	public String getStarPartnerLevel() {
		return starPartnerLevel;
	}
	public void setStarPartnerLevel(String starPartnerLevel) {
		this.starPartnerLevel = starPartnerLevel;
	}
	public String getLevelType() {
		return levelType;
	}
	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}
	public String getGmvDiff() {
		return gmvDiff;
	}
	public void setGmvDiff(String gmvDiff) {
		this.gmvDiff = gmvDiff;
	}
	public int getRankedDiff() {
		return rankedDiff;
	}
	public void setRankedDiff(int rankedDiff) {
		this.rankedDiff = rankedDiff;
	}
	
	public long getTodayOrderCount() {
		return todayOrderCount;
	}
	public void setTodayOrderCount(long todayOrderCount) {
		this.todayOrderCount = todayOrderCount;
	}
	public String getTodayOrderGmv() {
		return todayOrderGmv;
	}
	public void setTodayOrderGmv(String todayOrderGmv) {
		this.todayOrderGmv = todayOrderGmv;
	}
	public long getThisMonthOrderCount() {
		return thisMonthOrderCount;
	}
	public void setThisMonthOrderCount(long thisMonthOrderCount) {
		this.thisMonthOrderCount = thisMonthOrderCount;
	}
	public String getThisMonthOrderGmv() {
		return thisMonthOrderGmv;
	}
	public void setThisMonthOrderGmv(String thisMonthOrderGmv) {
		this.thisMonthOrderGmv = thisMonthOrderGmv;
	}
	public int getRankedDiff2() {
		return rankedDiff2;
	}
	public void setRankedDiff2(int rankedDiff2) {
		this.rankedDiff2 = rankedDiff2;
	}
	public long getStarPartnerTop() {
		return starPartnerTop;
	}
	public void setStarPartnerTop(long starPartnerTop) {
		this.starPartnerTop = starPartnerTop;
	}
	public long getGoodPartnerTop() {
		return goodPartnerTop;
	}
	public void setGoodPartnerTop(long goodPartnerTop) {
		this.goodPartnerTop = goodPartnerTop;
	}
	public long getStarPartnerOrderNum() {
		return starPartnerOrderNum;
	}
	public void setStarPartnerOrderNum(long starPartnerOrderNum) {
		this.starPartnerOrderNum = starPartnerOrderNum;
	}
	public long getGoodPartnerOrderNum() {
		return goodPartnerOrderNum;
	}
	public void setGoodPartnerOrderNum(long goodPartnerOrderNum) {
		this.goodPartnerOrderNum = goodPartnerOrderNum;
	}

}
