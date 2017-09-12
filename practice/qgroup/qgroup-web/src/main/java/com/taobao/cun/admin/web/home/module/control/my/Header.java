package com.taobao.cun.admin.web.home.module.control.my;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.dto.ApplyDto;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;

/**
 * @author guolei.maogl 2015Äê1ÔÂ17ÈÕ
 */
public class Header {
	private static final Logger logger = LoggerFactory.getLogger(Header.class);

	@Autowired
	private HttpSession session;
	@Autowired
	private StationApplyService stationApplyService;

	public void execute(TurbineRunData rundata, Context context, Navigator nav) {
		String loginId = WebUtil.getLoginId(session);
		Long userId = WebUtil.getUserId(session);

		try {
			if (userId != null && StringUtils.isNotBlank(loginId)) {
				ApplyDto applyDto = stationApplyService.getStationApplyDetailSafed(userId + "", loginId);
				if (applyDto != null) {
					context.put("name", applyDto.getName());
				}
			}
		} catch (RuntimeException e) {
			logger.warn(e.getMessage(), e);
		}
	}
}
