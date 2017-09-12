package com.taobao.cun.admin.web.home.module.screen.invite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.adapter.cuntao.center.PartnerServiceAdapter;
import com.taobao.cun.admin.adapter.cuntao.center.TrainTpServiceAdapter;
import com.taobao.cun.admin.web.home.module.ErrorCode;
import com.taobao.cun.admin.web.partner.module.util.PartnerUtil;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.train.dto.ApplyDetailODto;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.partner.PartnerApplyDto;
import com.taobao.cun.service.station.StationApplyQueryService;
import com.taobao.security.util.SensitiveDataUtil;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

public class TpApply {
	public static final Logger logger = LoggerFactory.getLogger(TpApply.class);

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpSession session;
	@Autowired
	private UicReadServiceClient uicReadServiceClient;
	@Autowired
	private PartnerServiceAdapter partnerServiceAdapter;
	@Autowired
	private TrainTpServiceAdapter trainTpServiceAdapter;
	@Autowired
	private StationApplyQueryService stationApplyQueryService;
	@Autowired
	private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;

	public void execute(TurbineRunData rundata, Context context, Navigator navigator) {
		Long taobaoUserId = WebUtil.getUserId(session);
		ErrorCode errorCode = validateAccount(context);
		if (null != errorCode) {
			logger.info("tpApply validate failed, userId = {?}, error_code = {?}", taobaoUserId,
					errorCode.getErrorCode());
			context.put("errorCode", errorCode.getErrorCode());
		}
	}

	private ErrorCode validateAccount(Context context) {
		Long taobaoUserId = WebUtil.getUserId(session);
		ContextDto contextDto = PartnerUtil.assemberContext(taobaoUserId);

		/** check base user info */
		ResultDO<BaseUserDO> baseUserDOresult = uicReadServiceClient.getBaseUserByUserId(taobaoUserId);
		if (!isUserExists(baseUserDOresult)) {
			return ErrorCode.USERID_NOT_EXISTS;
		}

		/** check partner */
		PartnerApplyDto existPartner = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoUserId, contextDto);

		/** has been partner */
		if (PartnerUtil.hasBeenPartner(existPartner)) {
			return ErrorCode.HAS_BEEN_PARTNER;
		}
		
		/** is station apply exists*/
		if(isExistsStationApply(taobaoUserId)){
			return ErrorCode.HAS_BEEN_PARTNER;
		}

		/** check repeat apply */
		ResultModel<ApplyDetailODto> applyDetailResult = trainTpServiceAdapter.getApplyDetail(taobaoUserId);
		if (null == applyDetailResult || !applyDetailResult.isSuccess()) {
			logger.info("getApplyDetail by HSF failed");
			throw new RuntimeException("getApplyDetail by HSF failed, taobaoUserId=" + taobaoUserId);
		}
		ApplyDetailODto applyDetailODto = applyDetailResult.getResult();
		if (null != applyDetailODto) {
			return ErrorCode.REPEAT_APPLY;
		}

		ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult = uicPaymentAccountReadServiceClient
				.getAccountByUserId(taobaoUserId);

		/** check alipay binding */
		if (!PartnerUtil.hasBindToPayAccount(basePaymentAccountDOResult)) {
			return ErrorCode.USER_NOT_BIND_PAY_ACCOUNT;
		}

		/** check real name auth */
		if (PartnerUtil.isNotRealNameAuth(baseUserDOresult.getModule()) && !PartnerUtil.isCompanyRealNameAuth(baseUserDOresult.getModule())) {
			return ErrorCode.USER_NOT_AUTH;
		}

		context.put("alipayAccount", SensitiveDataUtil.alipayLogonIdHide(basePaymentAccountDOResult.getModule().getOutUser()));

		return null;
	}
	
	private boolean isExistsStationApply(Long taobaoUserId) {
		com.taobao.cun.dto.ContextDto cuntaoCenterContextDto = PartnerUtil.assemberContext(taobaoUserId);
		com.taobao.cun.common.resultmodel.ResultModel<Boolean> resultModel = stationApplyQueryService.isExistsStationApplyByTaobaoUserId(taobaoUserId, cuntaoCenterContextDto);
		if (null != resultModel && resultModel.isSuccess() && resultModel.getResult()) {
			return true;
		}
		return false;
	}

	private boolean isUserExists(ResultDO<BaseUserDO> baseUserDOresult) {
		if (baseUserDOresult == null || !baseUserDOresult.isSuccess() || baseUserDOresult.getModule() == null) {
			return false;
		}
		BaseUserDO baseUserDO = baseUserDOresult.getModule();
		if (baseUserDO.getUserId() == null || baseUserDO.getUserId() == 0) {
			return false;
		}
		return true;
	}

}
