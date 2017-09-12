package com.taobao.cun.admin.web.home.module.screen.my;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.alibaba.fastjson.JSON;
import com.taobao.cun.admin.agentorder.AgentOrderAgentServiceAdapter;
import com.taobao.cun.admin.station.StationServiceAdapter;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.HavanaContext;
import com.taobao.cun.dto.StationHavanaContext;
import com.taobao.cun.dto.order.CuntaoConfirmOrderDto;
import com.taobao.cun.dto.order.CuntaoConfirmOrderResult;
import com.taobao.cun.service.order.CuntaoOrderService;

/**
 * @author guolei.maogl 2015-5-4
 */
public class ConfirmOrder {
	public static final Logger logger = LoggerFactory.getLogger(ConfirmOrder.class);
	@Autowired
	private HttpSession session;
	@Resource
	private StationServiceAdapter stationServiceAdapter;
	@Autowired
	private CuntaoOrderService cuntaoOrderService;
	@Autowired
	private AgentOrderAgentServiceAdapter agentOrderAgentServiceAdapter;

	public void execute(TurbineRunData rundata, Context context, Navigator navigator,
			@Param(name = "agentorderid") String agentOrderId) {
		if (StringUtils.isBlank(agentOrderId)) {
			logger.warn("confirmOrder error agentorderid: " + agentOrderId);
			rundata.setRedirectLocation("http://err.taobao.com/error2.html");
			return;
		}

		StationHavanaContext stationHavanaContext = getStationHavanaContextByHavanaContext(getHavanaContext());
		CuntaoConfirmOrderDto cuntaoConfirmOrderDto = new CuntaoConfirmOrderDto();
		cuntaoConfirmOrderDto.setAgentOrderId(Long.parseLong(agentOrderId.trim()));
		ResultModel<CuntaoConfirmOrderResult> rm = cuntaoOrderService.confirmOrder(stationHavanaContext,
				cuntaoConfirmOrderDto);
		if (rm.isSuccess() && rm.getResult() != null) {
			CuntaoConfirmOrderResult cuntaoConfirmOrderResult = rm.getResult();
			if ("GET".equalsIgnoreCase(cuntaoConfirmOrderResult.getHttpMethod())) {
				rundata.setRedirectLocation(cuntaoConfirmOrderResult.getUrl());
				return;
			}

			String fullUrl = cuntaoConfirmOrderResult.getUrl();
			String url = null;
			if (fullUrl.indexOf("?") > 0) {
				url = fullUrl.substring(0, fullUrl.indexOf("?"));
			} else {
				url = fullUrl;
			}
			context.put("action", url);

			if (fullUrl.indexOf("?") > 0) {
				String params = fullUrl.substring(fullUrl.indexOf("?") + 1);
				String[] pas = params.split("&");
				for (String pa : pas) {
					String[] a = pa.split("=");
					if (a.length > 1) {
						context.put(a[0], a[1]);
					}
				}
			}

			if (url.indexOf("tmall.com") > 0) {
				context.put("loginBuyUrl", agentOrderAgentServiceAdapter.getTmallLoginBuyUrl());
			} else if (url.indexOf("tmall.hk") > 0) {
				context.put("loginBuyUrl", agentOrderAgentServiceAdapter.getTmallHkLoginBuyUrl());
			}

			return;
		} else {
			logger.error("confirmOrder error agentorderid: " + agentOrderId + ", rm: " + JSON.toJSONString(rm),
					rm.getException());
			rundata.setRedirectLocation("http://err.taobao.com/error2.html");
			return;
		}
	}

	private HavanaContext getHavanaContext() {
		String loginId = WebUtil.getLoginId(session);
		Long userId = WebUtil.getUserId(session);
		HavanaContext havanaContext = new HavanaContext();
		havanaContext.setLoginId(loginId);
		havanaContext.setTaobaoUserId(userId.toString());
		return havanaContext;
	}

	private StationHavanaContext getStationHavanaContextByHavanaContext(HavanaContext havanaContext) {
		Long stationId = stationServiceAdapter.getStationIdByTaobaoUserId(havanaContext);
		StationHavanaContext stationHavanaContext = new StationHavanaContext();
		stationHavanaContext.setStationId(stationId);
		stationHavanaContext.setTaobaoUserId(havanaContext.getTaobaoUserId());
		stationHavanaContext.setLoginId(havanaContext.getLoginId());
		return stationHavanaContext;
	}
}
