package com.taobao.cun.admin.web.vo;

public class MyPerformanceVo {
	private String levelType ; // 8������е�һ�֣� ��ǰ�˻�ȡ�������Ϣƴװ   
	private String  gmvDiff  ; //gmv����xx�ﵽxx�ϻ��� ����λΪԪ   ����levelType  ���ж��Ƿ��ȡ��ֵ    
	private int   rankedDiff ; //��Ҫ�ϳ�xx�� �ﵽxx�ϻ��ˣ� ����levelType  ���ж��Ƿ��ȡ��ֵ 
	private int   rankedDiff2 ; //��Ҫ�ϳ�xx�� �ﵽxx�ϻ��ˣ� ����levelType  ���ж��Ƿ��ȡ��ֵ 
	private long todayOrderCount; //���ն�����
	private String todayOrderGmv ; //����gmv,��λΪԪ
    private long thisMonthOrderCount; //���¶�����
    private String thisMonthOrderGmv ; //����gmv,��λΪԪ
	private String  goodPartnerLevel; // ����ϻ��˵�gmv level  ��λΪԪ
	private String  starPartnerLevel; //���Ǻϻ��˵�gmv level ��λΪԪ
	private String curWeek; //��ǰ����
	private String curMonthDay; //��ǰ����
	private String curYearMonth;//��ǰ����
	
	private long starPartnerTop;//���Ǻϻ�����������
	
	private long goodPartnerTop;//����ϻ�����������
	
	private long starPartnerOrderNum;//���Ǻϻ������趩����
	
	private long goodPartnerOrderNum;//����ϻ������趩����
	
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
