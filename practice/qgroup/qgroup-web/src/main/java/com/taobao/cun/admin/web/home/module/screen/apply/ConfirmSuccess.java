package com.taobao.cun.admin.web.home.module.screen.apply;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.alipay.corporate.AlipayBailService;
import com.taobao.cun.admin.dto.RenderUrlDto;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.station.StationApplyDetailDto;
import com.taobao.cun.dto.station.enums.StationApplyProtocolConfirmingStepEnum;

public class ConfirmSuccess {
	public static final Logger logger = LoggerFactory.getLogger(ConfirmSuccess.class);
	@Resource
	private StationApplyService stationApplyService;

	@Autowired
	private HttpSession session;
	@Resource
	private AlipayBailService alipayBailService;
 

	public void execute(TurbineRunData rundata, Context context) {

		String loginId = WebUtil.getLoginId(session);
		Long userId = WebUtil.getUserId(session);

		StationApplyDetailDto applyDto = stationApplyService.getStationDetail(
				userId + "", loginId);

		if (applyDto == null) {
			throw new IllegalArgumentException(
					"applyService.getStationDetail result is null");
		}
		if (StationApplyProtocolConfirmingStepEnum.FROZEN.equals(applyDto
				.getProtocolConfirmingStep())) {
			// 已冻结成功，跳转到成功页面
			rundata.setRedirectLocation("/apply/applySuccess.htm");
			return;
		}
		if (!StationApplyProtocolConfirmingStepEnum.CONFIRMED.equals(applyDto
				.getProtocolConfirmingStep())) {
			throw new IllegalArgumentException("has no confirmed staion apply");
		}
		context.put("name", applyDto.getName());
		context.put("loginId", applyDto.getLoginId());
		context.put("alipayAccount", applyDto.getAlipayAccount());
		context.put("applierName", applyDto.getApplierName());
		try {
			Calendar cal = new GregorianCalendar();
			cal.setTime(applyDto.getConfirmedTime());
			cal.add(Calendar.DATE, 10);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			context.put("limitDay", sdf.format(cal.getTime()));
		} catch (Exception e) {
			logger.error("parse limitDay error");
		}

		RenderUrlDto renderUrlDto = alipayBailService.renderUrl(userId + "",
				loginId);
		context.put("jumpUrl", renderUrlDto.getUrl());
		context.put("needOpenNewWindow", renderUrlDto.isNeedOpenNewWindow());
		//准备冻结的金额，这时候开始，冻结金额已经是确认了的
		context.put("frozenMoney", applyDto.getFrozenMoney());

	}
}
