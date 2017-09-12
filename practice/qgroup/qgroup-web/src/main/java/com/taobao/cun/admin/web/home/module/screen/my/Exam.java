package com.taobao.cun.admin.web.home.module.screen.my;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.adapter.cuntao.center.PartnerServiceAdapter;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.partner.module.util.PartnerUtil;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.partner.PartnerApplyDto;
import com.taobao.cun.dto.station.StationApplyDetailDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

public class Exam {

	@Autowired
	private HttpSession session;

	@Autowired
	private PartnerServiceAdapter partnerServiceAdapter;
	@Resource
    private StationApplyService stationApplyService;

	public void execute(TurbineRunData rundata, Context context, Navigator nav) {
		Long taobaoUserId = WebUtil.getUserId(session);
		String loginId = WebUtil.getLoginId(session);
		ContextDto contextDto = PartnerUtil.assemberContext(taobaoUserId);
		PartnerApplyDto existPartner = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoUserId, contextDto);
		StationApplyDetailDto stationApplyDetailDto =stationApplyService.getStationDetail(taobaoUserId + "", loginId);
		if(null == existPartner && stationApplyDetailDto == null){
			rundata.setRedirectLocation("/my/copartnerApplyStation.htm");
		}
	}	
}
