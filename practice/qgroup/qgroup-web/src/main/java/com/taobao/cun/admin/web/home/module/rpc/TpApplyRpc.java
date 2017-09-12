package com.taobao.cun.admin.web.home.module.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.NoneResultDecrator;
import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.taobao.cun.admin.adapter.cuntao.center.TrainTpServiceAdapter;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.web.home.module.BaseController;
import com.taobao.cun.admin.web.home.module.ErrorCode;
import com.taobao.cun.admin.web.partner.module.util.PartnerUtil;
import com.taobao.cun.admin.web.partner.module.util.TpApplyUtil;
import com.taobao.cun.admin.web.partner.module.util.TpApplyVoConverter;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.admin.web.vo.TpApplyVo;
import com.taobao.cun.crius.common.dto.ContextDto;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.train.dto.ApplyDetailODto;
import com.taobao.cun.crius.train.dto.TrainTpApplyDto;
import com.taobao.cun.service.station.StationApplyQueryService;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;

@WebResource("tp")
public class TpApplyRpc extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(TpApplyRpc.class);

	@Autowired
	private TrainTpServiceAdapter trainTpServiceAdapter;
	@Autowired
	private StationApplyQueryService stationApplyQueryService;
	
	@Autowired
	private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;

	@Security(checkCSRF = true)
	@ResourceMapping("/submitTpApply")
	@NoneResultDecrator
	public AjaxResult<Boolean> saveTpApply() {
		if (!isLogin()) {
			return AjaxResult.unSuccess(ErrorCode.NOT_LOGIN.getErrorDesc(), ErrorCode.NOT_LOGIN.getErrorCode());
		}

		Long taobaoUserId = WebUtil.getUserId(session);

		// check repeat apply
		ResultModel<ApplyDetailODto> applyDetailResult = trainTpServiceAdapter.getApplyDetail(taobaoUserId);
		if(null == applyDetailResult || !applyDetailResult.isSuccess()){
			return AjaxResult.unSuccess(ErrorCode.FAILED_TO_APPLY.getErrorDesc(),ErrorCode.FAILED_TO_APPLY.getErrorCode());
		}
		ApplyDetailODto applyDetailODto = applyDetailResult.getResult();
		if (null != applyDetailODto) {
			return AjaxResult.unSuccess(ErrorCode.REPEAT_APPLY.getErrorDesc(), ErrorCode.REPEAT_APPLY.getErrorCode());
		}
		
		//is station apply exists
		if(isExistsStationApply(taobaoUserId)){
			return AjaxResult.unSuccess(ErrorCode.HAS_BEEN_PARTNER.getErrorDesc(), ErrorCode.HAS_BEEN_PARTNER.getErrorCode());
		}

		// check request parameter,then assemble to TpApplyVo
		AjaxResult<TpApplyVo> parameterResult = TpApplyUtil.assembleTpApplyVo(request);
		if (!parameterResult.isSuccess()) {
			logger.info("TpApply invalid: userId = {?} , msg = {?} ", taobaoUserId, parameterResult.getExceptionDesc());
			return AjaxResult.unSuccess(parameterResult.getExceptionDesc(), parameterResult.getErrorCode());
		}
		TpApplyVo tpApplyVo = parameterResult.getData();

		// check Alipay info
		ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult = uicPaymentAccountReadServiceClient
				.getAccountByUserId(taobaoUserId);
		if (!PartnerUtil.hasBindToPayAccount(basePaymentAccountDOResult)) {
			logger.info("TpApply alipay invalid: userId = {?} , alipay = {?} ", taobaoUserId,
					tpApplyVo.getAlipayAccount());
			return AjaxResult.unSuccess(ErrorCode.FAILED_TO_APPLY.getErrorDesc(),
					ErrorCode.FAILED_TO_APPLY.getErrorCode());
		}
		//set alipay
		tpApplyVo.setAlipayAccount(basePaymentAccountDOResult.getModule().getOutUser());
		tpApplyVo.setTaobaoUserId(taobaoUserId);

		// save apply by hsf
		ContextDto criusContextDto = TpApplyUtil.assemberContext(taobaoUserId);
		TrainTpApplyDto trainTpApplyDto = TpApplyVoConverter.convertToDto(tpApplyVo);
		ResultModel<Long> result = trainTpServiceAdapter.save(trainTpApplyDto, criusContextDto);
		if (result.isSuccess()) {
			return AjaxResult.success(Boolean.TRUE);
		} else {
			logger.info("save apply by hsf failed: {?}", result.getException());
			return AjaxResult.unSuccess(ErrorCode.FAILED_TO_APPLY.getErrorDesc(),
					ErrorCode.FAILED_TO_APPLY.getErrorCode());
		}
	}

	private boolean isExistsStationApply(Long taobaoUserId) {
		com.taobao.cun.dto.ContextDto cuntaoCenterContextDto = PartnerUtil.assemberContext(taobaoUserId);
		com.taobao.cun.common.resultmodel.ResultModel<Boolean> resultModel = stationApplyQueryService.isExistsStationApplyByTaobaoUserId(taobaoUserId, cuntaoCenterContextDto);
		if (null != resultModel && resultModel.isSuccess() && resultModel.getResult()) {
			return true;
		}
		return false;
	}

}
