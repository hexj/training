package com.taobao.cun.admin.web.vo;

import java.io.Serializable;

import com.taobao.cun.admin.dto.partnerperformance.IndicatorChartDto;

public class PartnerIndicatorChartVo implements Serializable{

	private static final long serialVersionUID = 1124234546L;
	
	private boolean isParticipateInRanking = true;

	private IndicatorChartDto country ;
	
	private IndicatorChartDto county;

	public boolean isParticipateInRanking() {
		return isParticipateInRanking;
	}

	public IndicatorChartDto getCountry() {
		return country;
	}

	public IndicatorChartDto getCounty() {
		return county;
	}

	public void setParticipateInRanking(boolean isParticipateInRanking) {
		this.isParticipateInRanking = isParticipateInRanking;
	}

	public void setCountry(IndicatorChartDto country) {
		this.country = country;
	}

	public void setCounty(IndicatorChartDto county) {
		this.county = county;
	}
	
	public static PartnerIndicatorChartVo inChart(IndicatorChartDto country, IndicatorChartDto county){
		PartnerIndicatorChartVo vo = new PartnerIndicatorChartVo();
		vo.setParticipateInRanking(true);
		vo.setCountry(country);
		vo.setCounty(county);
		return vo;
	}
	
	public static PartnerIndicatorChartVo notInChart(IndicatorChartDto country, IndicatorChartDto county){
		PartnerIndicatorChartVo vo = new PartnerIndicatorChartVo();
		vo.setParticipateInRanking(false);
		vo.setCountry(country);
		vo.setCounty(county);
		return vo;
	}
	
	public PartnerIndicatorChartVo(){};
	
}
