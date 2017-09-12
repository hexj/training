package com.taobao.cun.admin.web.home.module.screen.invite;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.adapter.cuntao.center.TrainTpServiceAdapter;
import com.taobao.cun.admin.web.partner.module.util.TpApplySensitiveUtil;
import com.taobao.cun.admin.web.partner.module.util.TpApplyVoConverter;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.admin.web.vo.TpApplyVo;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.train.dto.ApplyDetailODto;

public class TpApplyDetail {
	public static final Logger logger = LoggerFactory.getLogger(TpApplyDetail.class);
	@Autowired
	private HttpSession session;
	@Autowired
	private TrainTpServiceAdapter trainTpServiceAdapter;

	public void execute(TurbineRunData rundata, Context context, Navigator navigator) {
		Long taobaoUserId = WebUtil.getUserId(session);
		ResultModel<ApplyDetailODto> applyDetailResult = trainTpServiceAdapter.getApplyDetail(taobaoUserId);
		if (null == applyDetailResult || !applyDetailResult.isSuccess()) {
			logger.info("getApplyDetail by HSF failed");
			throw new RuntimeException("getApplyDetail by HSF failed, taobaoUserId=" + taobaoUserId);
		}

		ApplyDetailODto applyDetailODto = applyDetailResult.getResult();
		if(null == applyDetailODto){
			rundata.setRedirectLocation("/invite/tpApply.htm");
			return;
		}
		
		TpApplyVo tpApplyVo = TpApplyVoConverter.convertFromApplyDetailODto(applyDetailODto);
		TpApplySensitiveUtil.hidenSensitiveInfo(tpApplyVo);
		context.put("tpApply", tpApplyVo);
	 }

}
