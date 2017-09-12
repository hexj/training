package com.taobao.cun.admin.web.home.module.screen.my;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.service.CuntaoTpaService;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.station.StationApplyDetailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

public class AssetsList {

	public static final Logger logger = LoggerFactory
			.getLogger(ApplyDetail.class);
	@Autowired
	private StationApplyService stationApplyService;
	@Autowired
	private HttpSession session;
	@Resource
	private CuntaoTpaService cuntaoTpaService;
	public void execute(TurbineRunData rundata,Context context) {
		String loginId = WebUtil.getLoginId(session);
		Long userId = WebUtil.getUserId(session);
		StationApplyDetailDto applyDto = stationApplyService.getStationDetail(userId + "", loginId);

		if (applyDto == null) {
			throw new IllegalArgumentException(
					"applyService.getStationApply result is null");
		}
		//淘帮手无权访问
		if(true == cuntaoTpaService.isCuntaoTpa(userId))
			rundata.setRedirectLocation("http://err.taobao.com/error2.html");
		context.put("stationName", applyDto.getName());
		context.put("countyName", applyDto.getCuntaoOrg() != null? applyDto.getCuntaoOrg().getName():null);
		context.put("applierName", applyDto.getApplierName());
		context.put("stationApplyId", applyDto.getStationApplyId());
		context.put("stationStatus", applyDto.getState().getCode()); // 获取当前申请单的状态
		context.put("stationStatus", applyDto.getState().getCode()); 
		if (applyDto.getCuntaoOrg() != null){
			context.put("countyId", applyDto.getCuntaoOrg().getId()); 
		}
	}
}
