package com.taobao.cun.admin.web.home.module.screen.my;

import com.taobao.cun.admin.dto.ApplyDto;
import com.taobao.cun.admin.service.StationApplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.uribroker.URIBrokerService;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.admin.adapter.cuntao.center.TaobaoTradeOrderQueryServiceAdapter;
import com.taobao.cun.admin.enums.Pay.PaySource;
import com.taobao.cun.admin.web.home.module.BaseController;
import com.taobao.cun.admin.web.util.WebUtil;

public class ReceiptDetail extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ReceiptDetail.class);

	@Autowired
	private TaobaoTradeOrderQueryServiceAdapter taobaoTradeOrderQueryServiceAdapter;
	@Autowired
	private URIBrokerService uriBrokerService;
	@Autowired
	private StationApplyService stationApplyService;

	public void execute(@Param("orderNoOrMailNo") String orderNoOrMailNo, Context context, Navigator navigator) {
		try {

			String loginId = WebUtil.getLoginId(session);
			Long userId = WebUtil.getUserId(session);
			ApplyDto applyDto = stationApplyService.getStationApplyDetailSafed(userId+"",loginId);
			if (applyDto == null) {
				throw new IllegalArgumentException("applyService.getStationApply result is null");
			}
			
			context.put("orderNoOrMailNo", orderNoOrMailNo);
			
			addOtherThingsToContext(context);
			
		} catch (Exception e) {
			logger.error("prepare for receiptDetail vm error! param: orderNoOrMailNo:{}, longinId:{}",
					new Object[] {orderNoOrMailNo, getLoginId()}, e);
			navigator.forwardTo("//err.taobao.com/error2.html");
		}
	}
	
	private void addOtherThingsToContext(Context context) {
		if (StringUtil.isNotBlank(WebUtil.getRefer(request))){
			if (WebUtil.getRefer(request).contains(uriBrokerService.getURIBroker("cainiaoyzDomain").fullURI().toString()))
				context.put("source", PaySource.CAI_NIAO.name());
		}
	}
}
