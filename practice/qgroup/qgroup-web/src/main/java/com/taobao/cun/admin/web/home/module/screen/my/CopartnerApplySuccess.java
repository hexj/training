package com.taobao.cun.admin.web.home.module.screen.my;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.csp.courier.StringUtils;
import com.taobao.cun.admin.adapter.cuntao.center.PartnerServiceAdapter;
import com.taobao.cun.admin.web.partner.module.util.PartnerUtil;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.partner.PartnerApplyDto;

public class CopartnerApplySuccess {

	@Autowired
	private HttpSession session;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PartnerServiceAdapter partnerServiceAdapter;

	public void execute(TurbineRunData rundata, Context context, Navigator nav) {
		Long taobaoUserId = WebUtil.getUserId(session);
		String isEdit = request.getParameter("isEdit");
		if(StringUtils.isNotBlank(isEdit)){
			context.put("isEdit", isEdit);
		}
		ContextDto contextDto = PartnerUtil.assemberContext(taobaoUserId);
		PartnerApplyDto existPartner = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoUserId, contextDto);
		if (null == existPartner) {
			rundata.setRedirectLocation("//err.taobao.com/error.html");
		}
	}

}
