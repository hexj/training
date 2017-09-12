package com.taobao.cun.admin.web.home.module.rpc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.NoneResultDecrator;
import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.primitives.Longs;
import com.taobao.cun.admin.adapter.cuntao.center.PartnerServiceAdapter;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.dto.UploadResultDto;
import com.taobao.cun.admin.service.TfsService;
import com.taobao.cun.admin.util.PicMerge;
import com.taobao.cun.admin.web.constants.ViewConstants;
import com.taobao.cun.admin.web.home.module.BaseController;
import com.taobao.cun.admin.web.home.module.ErrorCode;
import com.taobao.cun.admin.web.partner.module.util.PartnerUtil;
import com.taobao.cun.admin.web.partner.module.util.PartnerVoConverter;
import com.taobao.cun.admin.web.util.PersonalCardUtil;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.admin.web.vo.PartnerVo;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.crius.attachement.domain.Attachement;
import com.taobao.cun.crius.attachement.service.AttachementService;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.partner.PartnerApplyDto;
import com.taobao.cun.dto.partner.PartnerApplyEducationEnum;
import com.taobao.cun.dto.partner.PartnerApplyPoliticsEnum;
import com.taobao.cun.dto.partner.PartnerApplyStateEnum;
import com.taobao.cun.service.station.StationApplyQueryService;
import com.taobao.security.util.SensitiveDataUtil;
import com.taobao.tradeback.util.JSONUtil;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

@WebResource("partner")
public class PartnerRpc extends BaseController {

	public static final Logger logger = LoggerFactory.getLogger(PartnerRpc.class);

	@Autowired
	private PartnerServiceAdapter partnerServiceAdapter;

	@Autowired
	private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;

	@Autowired
	private UicReadServiceClient uicReadServiceClient;

	@Autowired
	private StationApplyQueryService stationApplyQueryService;

	@Autowired
	private TfsService tfsService;

	@Autowired
	private AttachementService attachmentService;

	@Security(checkCSRF = true)
	@ResourceMapping(value = "/submitPartnerStep")
	@NoneResultDecrator
	public AjaxResult<Boolean> submitPartnerStep() {
		PartnerVo partnerVo = PartnerVoConverter.convert2PartnerVoStep(request, null);
		partnerVo.setTaobaoUserId(getUserId());
		// 校验客户端的参数值
		AjaxResult<PartnerVo> validateResult = validateClientStepParam(partnerVo, null);

		if (!validateResult.isSuccess()) {
			return AjaxResult.unSuccess(validateResult.getExceptionDesc(), validateResult.getErrorCode());
		}
		// 校验重复报名
		AjaxResult<Boolean> repeatResult = validateRepeatApply();
		if (!repeatResult.isSuccess()) {
			return AjaxResult.unSuccess(repeatResult.getExceptionDesc(), repeatResult.getErrorCode());
		}
		// 校验PartnerVo
		AjaxResult<PartnerVo> validatePartnerResult = validatePartnerVo(partnerVo);
		if (!validatePartnerResult.isSuccess()) {
			return AjaxResult.unSuccess(validatePartnerResult.getExceptionDesc(), validatePartnerResult.getErrorCode());
		}
		Long taobaoId = getUserId();
		// 添加一下淘宝用户Id
		partnerVo.setTaobaoUserId(taobaoId);

		ContextDto context = PartnerUtil.assemberContext(taobaoId);

		PartnerApplyDto partnerApplyDto = PartnerVoConverter.convert2PartnerApplyDtoStep(partnerVo,
				new PartnerApplyDto());

		partnerApplyDto.setInfoLevel(ViewConstants.INFO_BASIC); // 新增时加入基本信息
		PartnerApplyStateEnum submitState = PartnerApplyStateEnum.STATE_APPLY_WAIT;
		partnerApplyDto.setPartnerApplyStateEnum(submitState);

		partnerApplyDto.setTaobaoUserId(getUserId());
		partnerApplyDto.setTaobaoNick(getLoginId());
		partnerApplyDto.setApplyTime(new Date());
		boolean result = partnerServiceAdapter.savePartnerApply(partnerApplyDto, context);

		if (result) {
			String areaCode = partnerVo.getProvince() + "|" + partnerVo.getCity() + "|" + partnerVo.getCounty();
			boolean dispatchResult = partnerServiceAdapter.dispatchExam(taobaoId, getLoginId(), areaCode);
			if (!dispatchResult) {
				logger.error("dispatchExam failed, userId = " + taobaoId + ", areaCode = " + areaCode);
			}
			PartnerApplyDto applyDto = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, context);
			partnerApplyDto.setId(applyDto.getId());
			// 保存身份证照片
			savePartnerApplyIdCardImgs(partnerApplyDto, context);
			return AjaxResult.success(Boolean.TRUE);
		}
		return AjaxResult.unSuccess(ErrorCode.FAILED_TO_APPLY.getErrorDesc(), ErrorCode.FAILED_TO_APPLY.getErrorCode());
	}

	private IdCardImgIdsVo savePartnerApplyIdCardImgs(PartnerApplyDto partnerApplyDto, ContextDto context) {
		IdCardImgIdsVo imgIdVo = new IdCardImgIdsVo();
		if (null == partnerApplyDto) {
			return imgIdVo;
		}

		JSONArray firstary = JSONUtil.fastJsonDeSerializeArray(partnerApplyDto.getIdCardImgsFace());
		JSONObject firstJson = firstary.getJSONObject(0);
		JSONArray secondarr = JSONUtil.fastJsonDeSerializeArray(partnerApplyDto.getIdCardImgsBack());
		JSONObject secondJson = secondarr.getJSONObject(0);
		if (null == firstJson || null == secondJson) {
			return imgIdVo;
		}
		Long partnerApplyId = partnerApplyDto.getId();

		// 身份证正反面信息
		InputStream firstFileStream = null;
		InputStream secondFileStream = null;
		ByteArrayOutputStream outputFileStream = new ByteArrayOutputStream(5000);
		try {
			Long fisrtFileId = Longs.tryParse(firstJson.getString("attachementId"));
			String outputFileType = "jpg";
			if (StringUtil.isNotBlank(firstJson.getString("fileType"))) {
				outputFileType = firstJson.getString("fileType");
			}
			imgIdVo.setFirstImgId(fisrtFileId);
			Long secondFileId = Longs.tryParse(secondJson.getString("attachementId"));
			imgIdVo.setFirstImgId(secondFileId);

			// 删除之前的照片重新合成
			deleteFaceAndBack(partnerApplyId, fisrtFileId, secondFileId);

			byte[] firstFileBytes = getFileBytes(firstJson);
			byte[] secondFileBytes = getFileBytes(secondJson);
			if (firstFileBytes != null && secondFileBytes != null) {
				firstFileStream = new ByteArrayInputStream(firstFileBytes);
				secondFileStream = new ByteArrayInputStream(secondFileBytes);
				// 合成照片，合并文件
				PicMerge.yPic(firstFileStream, secondFileStream, outputFileStream, outputFileType);
				byte[] jointFileBytes = outputFileStream.toByteArray();
				UploadResultDto uploadresult = tfsService.uploadFile(jointFileBytes, outputFileType);
				// save attachment to db
				Attachement att = new Attachement();
				att.setCreator(String.valueOf(getUserId()));
				att.setModifier(String.valueOf(getUserId()));
				att.setFsId(uploadresult.getFileName());
				att.setFileType(outputFileType);
				att.setTitle(uploadresult.getFileName());
				Date now = new Date();
				att.setGmtCreate(now);
				att.setGmtModified(now);
				att.setIsDeleted("n");
				att.setBizType(ViewConstants.IDCARD_JOINTIMG_BIZTYPE);
				att.setObjectId(partnerApplyId);
				com.taobao.cun.crius.common.resultmodel.ResultModel<Long> attRm = attachmentService.addAttachement(att);
				if (attRm == null || !attRm.isSuccess() || attRm.getResult() == null) {
					throw new RuntimeException("addAttachement failed" + JSON.toJSONString(attRm));
				}
				Long jointIdCardImgsId = attRm.getResult().longValue();
				imgIdVo.setJointImgId(jointIdCardImgsId);
			}
			// 更新正反面
			com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement> firstFileRm = attachmentService
					.getAttachement(fisrtFileId);
			com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement> secondFileRm = attachmentService
					.getAttachement(secondFileId);
			Attachement firstAtt = firstFileRm.getResult();
			Attachement secondAtt = secondFileRm.getResult();
			firstAtt.setBizType(ViewConstants.IDCARD_IMGS_FACE_BIZTYPE);
			firstAtt.setObjectId(partnerApplyId);
			secondAtt.setBizType(ViewConstants.IDCARD_IMGS_BACK_BIZTYPE);
			secondAtt.setObjectId(partnerApplyId);

			firstAtt.setModifier(String.valueOf(getUserId()));
			secondAtt.setModifier(String.valueOf(getUserId()));
			attachmentService.updateAttachement(firstAtt);
			attachmentService.updateAttachement(secondAtt);
		} catch (Exception e) {
			logger.error("身份证合并保存失败: " + e.getMessage());
		} finally {
			IOUtils.closeQuietly(outputFileStream);
			IOUtils.closeQuietly(firstFileStream);
			IOUtils.closeQuietly(secondFileStream);
		}
		return imgIdVo;

	}

	private void deleteFaceAndBack(Long partnerApplyId, Long fisrtFileId, Long secondFileId) {
		com.taobao.cun.crius.common.resultmodel.ResultModel<List<Attachement>> idcardImgsFaceRm = attachmentService
				.getAttachmentsByObjId(partnerApplyId, ViewConstants.IDCARD_IMGS_FACE_BIZTYPE);
		com.taobao.cun.crius.common.resultmodel.ResultModel<List<Attachement>> idcardImgsBackRm = attachmentService
				.getAttachmentsByObjId(partnerApplyId, ViewConstants.IDCARD_IMGS_BACK_BIZTYPE);

		com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement> jointimgsRm = attachmentService
				.getAttachmentByObjectId(partnerApplyId, ViewConstants.IDCARD_JOINTIMG_BIZTYPE);
		if (jointimgsRm.isSuccess()) {
			Attachement oldattr = jointimgsRm.getResult();
			if (oldattr != null) {
				attachmentService.delete(oldattr.getId());
			}
		}
		deleteImgWithRm(idcardImgsFaceRm, fisrtFileId, partnerApplyId, ViewConstants.IDCARD_IMGS_FACE_BIZTYPE);
		deleteImgWithRm(idcardImgsBackRm, secondFileId, partnerApplyId, ViewConstants.IDCARD_IMGS_BACK_BIZTYPE);
	}

	private void deleteImgWithRm(com.taobao.cun.crius.common.resultmodel.ResultModel<List<Attachement>> idcardImgsRm,
			Long sbmtFileId, Long objectId, String bizType) {
		if (idcardImgsRm.isSuccess()) {
			List<Attachement> idcardImgs = idcardImgsRm.getResult();
			if (idcardImgs != null) {
				for (Attachement att : idcardImgs) {
					Long oldid = att.getId();
					if (oldid != null) {
						if (oldid.equals(sbmtFileId)) { // 如果是当前提交的文件
							// 更新 bizType与 objId
							if (oldid.equals(sbmtFileId)) {
								att.setBizType(bizType);
							}
							att.setObjectId(objectId);
							att.setIsDeleted("n");
							att.setGmtModified(new Date());
							att.setModifier(getLoginId()); // Taobao Nick
							attachmentService.updateAttachement(att);
						} else { // 没有使用的记录
							attachmentService.delete(oldid);
						}
					}
				}
			}
		}
	}

	// 根据单个Json对象获取文件字节
	private byte[] getFileBytes(JSONObject jsonObject) {
		String fsId = jsonObject.getString("fsId");
		String fileName = fsId.replace(".tfsprivate", "");
		String fileType = jsonObject.getString("fileType");
		byte[] bytes = tfsService.doGetFileFromTfs(fileName, fileType);
		return bytes;
	}

	@Security(checkCSRF = true)
	@ResourceMapping("/submitPartner")
	@NoneResultDecrator
	public AjaxResult<Boolean> submitPartner() {
		PartnerVo partnerVo = PartnerVoConverter.convert2PartnerVo(request, null);
		// 校验客户端的参数值
		AjaxResult<PartnerVo> validateResult = validateClientStepParam(partnerVo, null);
		if (!validateResult.isSuccess()) {
			return AjaxResult.unSuccess(validateResult.getExceptionDesc(), validateResult.getErrorCode());
		}
		// 校验重复报名
		AjaxResult<Boolean> repeatResult = validateRepeatApply();
		if (!repeatResult.isSuccess()) {
			return AjaxResult.unSuccess(repeatResult.getExceptionDesc(), repeatResult.getErrorCode());
		}
		// 校验PartnerVo
		AjaxResult<PartnerVo> validatePartnerResult = validatePartnerVo(partnerVo);
		if (!validatePartnerResult.isSuccess()) {
			return AjaxResult.unSuccess(validatePartnerResult.getExceptionDesc(), validatePartnerResult.getErrorCode());
		}

		Long taobaoId = getUserId();
		// 添加一下淘宝用户Id
		partnerVo.setTaobaoUserId(taobaoId);

		ContextDto context = PartnerUtil.assemberContext(taobaoId);

		PartnerApplyDto partnerApplyDto = PartnerVoConverter.convert2PartnerApplyDto(partnerVo, null);
		partnerApplyDto.setInfoLevel(ViewConstants.INFO_BASIC);
		boolean result = partnerServiceAdapter.savePartnerApply(partnerApplyDto, context);

		if (result) {
			String areaCode = partnerVo.getProvince() + "|" + partnerVo.getCity() + "|" + partnerVo.getCounty();
			boolean dispatchResult = partnerServiceAdapter.dispatchExam(taobaoId, getLoginId(), areaCode);
			if (!dispatchResult) {
				logger.error("dispatchExam failed, userId = " + taobaoId + ", areaCode = " + areaCode);
			}
			return AjaxResult.success(Boolean.TRUE);
		}
		return AjaxResult.unSuccess(ErrorCode.FAILED_TO_APPLY.getErrorDesc(), ErrorCode.FAILED_TO_APPLY.getErrorCode());
	}

	@Security(checkCSRF = true)
	@ResourceMapping(value = "/updatePartnerStep")
	@NoneResultDecrator
	public AjaxResult<Boolean> updatePartnerStep() {
		Long taobaoId = WebUtil.getUserId(session);
		ContextDto contextDto = PartnerUtil.assemberContext(taobaoId);
		// ---- 校验合法性
		PartnerApplyDto oldApplyDto = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, contextDto);
		// 校验状态
		if (null == oldApplyDto) {
			return AjaxResult.unSuccess(ErrorCode.NOT_APPLY.getErrorDesc(), ErrorCode.NOT_APPLY.getErrorCode());
		}

		PartnerVo partnerVo = PartnerVoConverter.convert2PartnerVoStep(request, oldApplyDto.getPartnerApplyStateEnum());

		// 校验客户端的参数值
		AjaxResult<PartnerVo> validateResult = validateClientStepParam(partnerVo,
				oldApplyDto.getPartnerApplyStateEnum());
		if (!validateResult.isSuccess()) {
			return AjaxResult.unSuccess(validateResult.getExceptionDesc(), validateResult.getErrorCode());
		}
		// 校验PartnerVo
		AjaxResult<PartnerVo> validatePartnerResult = validatePartnerVo(partnerVo);
		if (!validatePartnerResult.isSuccess()) {
			return AjaxResult.unSuccess(validatePartnerResult.getExceptionDesc(), validatePartnerResult.getErrorCode());
		}

		// 添加一下淘宝用户Id
		partnerVo.setTaobaoUserId(taobaoId);

		ContextDto context = PartnerUtil.assemberContext(taobaoId);

		PartnerApplyDto partnerApplyDto = PartnerVoConverter.convert2PartnerApplyDtoStep(partnerVo, oldApplyDto);
		if (ViewConstants.STEP_ADVANCE.equals(partnerVo.getCurrentStep())) {
			partnerApplyDto.setInfoLevel(ViewConstants.INFO_ADVANCE);
		} else if (ViewConstants.STEP_BASIC.equals(partnerVo.getCurrentStep())) {
			// 保存身份证照片
			savePartnerApplyIdCardImgs(partnerApplyDto, context);
		}
		boolean result = partnerServiceAdapter.updatePartnerApply(partnerApplyDto, context);

		if (result) {
			return AjaxResult.success(Boolean.TRUE);
		}
		return AjaxResult.unSuccess(ErrorCode.FAILED_TO_APPLY.getErrorDesc(), ErrorCode.FAILED_TO_APPLY.getErrorCode());
	}

	@Security(checkCSRF = true)
	@ResourceMapping("/updatePartner")
	@NoneResultDecrator
	public AjaxResult<Boolean> updatePartner() {
		Long taobaoId = WebUtil.getUserId(session);
		ContextDto contextDto = PartnerUtil.assemberContext(taobaoId);
		// ---- 校验合法性
		PartnerApplyDto oldApplyDto = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, contextDto);
		// 校验状态
		if (null == oldApplyDto) {
			return AjaxResult.unSuccess(ErrorCode.NOT_APPLY.getErrorDesc(), ErrorCode.NOT_APPLY.getErrorCode());
		}
		if (!(PartnerApplyStateEnum.STATE_APPLY_WAIT.equals(oldApplyDto.getPartnerApplyStateEnum())
				|| PartnerApplyStateEnum.STATE_APPLY_INTERVIEW.equals(oldApplyDto.getPartnerApplyStateEnum()))) {
			return AjaxResult.unSuccess("您的申请已不是初审或待面试状态，无法编辑", ErrorCode.REQUEST_IS_INVALID.getErrorCode());
		}

		PartnerVo partnerVo = PartnerVoConverter.convert2PartnerVo(request, oldApplyDto.getPartnerApplyStateEnum());
		// 校验客户端的参数值
		AjaxResult<PartnerVo> validateResult = validateClientParam(partnerVo, oldApplyDto.getPartnerApplyStateEnum());
		if (!validateResult.isSuccess()) {
			return AjaxResult.unSuccess(validateResult.getExceptionDesc(), validateResult.getErrorCode());
		}
		// 校验PartnerVo
		AjaxResult<PartnerVo> validatePartnerResult = validatePartnerVo(partnerVo);
		if (!validatePartnerResult.isSuccess()) {
			return AjaxResult.unSuccess(validatePartnerResult.getExceptionDesc(), validatePartnerResult.getErrorCode());
		}

		// 添加一下淘宝用户Id
		partnerVo.setTaobaoUserId(taobaoId);

		ContextDto context = PartnerUtil.assemberContext(taobaoId);

		PartnerApplyDto partnerApplyDto = PartnerVoConverter.convert2PartnerApplyDto(partnerVo,
				oldApplyDto.getPartnerApplyStateEnum());
		partnerApplyDto.setApplyTime(oldApplyDto.getApplyTime());
		// 添加 id
		partnerApplyDto.setId(oldApplyDto.getId());
		if ((PartnerApplyStateEnum.STATE_APPLY_REFUSE.equals(oldApplyDto.getPartnerApplyStateEnum()))) {
			return AjaxResult.unSuccess(oldApplyDto.getAuditOpinion(), ErrorCode.REQUEST_IS_INVALID.getErrorCode());
		}
		boolean result = partnerServiceAdapter.updatePartnerApply(partnerApplyDto, context);

		if (result) {
			return AjaxResult.success(Boolean.TRUE);
		}
		return AjaxResult.unSuccess(ErrorCode.FAILED_TO_APPLY.getErrorDesc(), ErrorCode.FAILED_TO_APPLY.getErrorCode());
	}

	/**
	 * 校验客户端请求报名的参数值
	 * 
	 * @param partnerVo
	 * @return
	 */
	private AjaxResult<PartnerVo> validateClientStepParam(PartnerVo partnerVo, PartnerApplyStateEnum state) {
		if (null == partnerVo) {
			return AjaxResult.unSuccess(ErrorCode.FAILED_TO_APPLY.getErrorDesc(),
					ErrorCode.FAILED_TO_APPLY.getErrorCode());
		}

		if (StringUtil.isEmpty(partnerVo.getCurrentStep())) {
			partnerVo.setCurrentStep(ViewConstants.STEP_BASIC);
		}

		if (ViewConstants.STEP_BASIC.equals(partnerVo.getCurrentStep())) {
			// 基本信息

			if (StringUtil.isEmpty(partnerVo.getApplyName())) {// 姓名
				return AjaxResult.unSuccess(ErrorCode.NAME_IS_NULL.getErrorDesc(),
						ErrorCode.NAME_IS_NULL.getErrorCode());
			}

			if (StringUtil.isEmpty(partnerVo.getGender())) {// 性别
				return AjaxResult.unSuccess(ErrorCode.GENDER_IS_NULL.getErrorDesc(),
						ErrorCode.GENDER_IS_NULL.getErrorCode());
			}
			if (StringUtil.isEmpty(partnerVo.getIdCardNum())) { // 身份证号
				return AjaxResult.unSuccess(ErrorCode.IDCARDNUM_IS_NULL.getErrorDesc(),
						ErrorCode.IDCARDNUM_IS_NULL.getErrorCode());
			}
			if (StringUtil.isEmpty(partnerVo.getAlipayAccount())) {// 支付宝账号
				return AjaxResult.unSuccess(ErrorCode.PAY_ACCOUNT_IS_NULL.getErrorDesc(),
						ErrorCode.PAY_ACCOUNT_IS_NULL.getErrorCode());
			}
			if (StringUtil.isEmpty(partnerVo.getMobile())) { // 联系电话
				return AjaxResult.unSuccess(ErrorCode.MOBILE_IS_NULL.getErrorDesc(),
						ErrorCode.MOBILE_IS_NULL.getErrorCode());
			}
			if (PartnerApplyEducationEnum.getEnum(partnerVo.getEducation()) == null) { // 学历
				return AjaxResult.unSuccess(ErrorCode.EDUCATION_IS_INVALID.getErrorDesc(),
						ErrorCode.EDUCATION_IS_INVALID.getErrorCode());
			}

			// 计划经营地址 断省市，区镇
			// 省市县必填，镇乡街与村与详细地址非必填
			if (null == state || PartnerApplyStateEnum.STATE_APPLY_WAIT.equals(state)) {
				// canEditAddress
				if (StringUtil.isEmpty(partnerVo.getProvince())) {
					return AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_PROVINCE_IS_NULL.getErrorDesc(),
							ErrorCode.BUSINESS_ADDRESS_PROVINCE_IS_NULL.getErrorCode());
				}
				if (StringUtil.isEmpty(partnerVo.getCity())) {
					return AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_CITY_IS_NULL.getErrorDesc(),
							ErrorCode.BUSINESS_ADDRESS_CITY_IS_NULL.getErrorCode());
				}
				if (StringUtil.isEmpty(partnerVo.getCounty())) {
					return AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_COUNTY_IS_NULL.getErrorDesc(),
							ErrorCode.BUSINESS_ADDRESS_COUNTY_IS_NULL.getErrorCode());
				}
				// if (StringUtil.isEmpty(partnerVo.getTown())) {
				// return
				// AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_STREET_IS_NULL.getErrorDesc(),
				// ErrorCode.BUSINESS_ADDRESS_STREET_IS_NULL.getErrorCode());
				// }

			}

			if (StringUtil.isEmpty(partnerVo.getIdCardImgsFace())
					|| StringUtil.isEmpty(partnerVo.getIdCardImgsFace())) { // 身份证照片
				return AjaxResult.unSuccess(ErrorCode.IDCARDIMG_IS_NULL.getErrorDesc(),
						ErrorCode.IDCARDIMG_IS_NULL.getErrorCode());
			}

		} else if (ViewConstants.STEP_ADVANCE.equals(partnerVo.getCurrentStep())) {
			// 详细信息

			if (PartnerApplyPoliticsEnum.getEnum(partnerVo.getPolitics()) == null) { // 政治面貌
				return AjaxResult.unSuccess(ErrorCode.POLITICS_IS_INVALID.getErrorDesc(),
						ErrorCode.POLITICS_IS_INVALID.getErrorCode());
			}

			// 现居住地
			if (StringUtil.isEmpty(partnerVo.getManageProvince())) {
				return AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_PROVINCE_IS_NULL.getErrorDesc(),
						ErrorCode.BUSINESS_ADDRESS_PROVINCE_IS_NULL.getErrorCode());
			}
			if (StringUtil.isEmpty(partnerVo.getManageCity())) {
				return AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_CITY_IS_NULL.getErrorDesc(),
						ErrorCode.BUSINESS_ADDRESS_CITY_IS_NULL.getErrorCode());
			}
			if (StringUtil.isEmpty(partnerVo.getManageCounty())) {
				return AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_COUNTY_IS_NULL.getErrorDesc(),
						ErrorCode.BUSINESS_ADDRESS_COUNTY_IS_NULL.getErrorCode());
			}
			// if (StringUtil.isEmpty(partnerVo.getManageTown())) {
			// return
			// AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_STREET_IS_NULL.getErrorDesc(),
			// ErrorCode.BUSINESS_ADDRESS_STREET_IS_NULL.getErrorCode());
			// }

			// if (StringUtil.isEmpty(partnerVo.getManageAddress())) {//
			// 现居住地详细地址
			// return
			// AjaxResult.unSuccess(ErrorCode.MANAGE_ADDRESS_IS_NULL.getErrorDesc(),
			// ErrorCode.MANAGE_ADDRESS_IS_NULL.getErrorCode());
			// }

			if (StringUtil.isEmpty(partnerVo.getIsExitPlace())) {// 是否有营业场所
				return AjaxResult.unSuccess(ErrorCode.IS_EXIST_PLACE_IS_NULL.getErrorDesc(),
						ErrorCode.IS_EXIST_PLACE_IS_NULL.getErrorCode());
			}

			if ("y".equalsIgnoreCase(partnerVo.getIsExitPlace()) && StringUtil.isEmpty(partnerVo.getManageArea())) {// 是否有营业场所
				return AjaxResult.unSuccess(ErrorCode.MANAGE_AREA_IS_NULL.getErrorDesc(),
						ErrorCode.MANAGE_AREA_IS_NULL.getErrorCode());
			}

			// if (StringUtil.isEmpty(partnerVo.getNation())) { // 民族
			// return
			// AjaxResult.unSuccess(ErrorCode.NATION_IS_NULL.getErrorDesc(),
			// ErrorCode.NATION_IS_NULL.getErrorCode());
			// }
		}

		return AjaxResult.success(partnerVo);
	}

	/**
	 * /** 校验客户端请求报名的参数值
	 * 
	 * @param partnerVo
	 * @return
	 */
	private AjaxResult<PartnerVo> validateClientParam(PartnerVo partnerVo, PartnerApplyStateEnum state) {
		if (null == partnerVo) {
			return AjaxResult.unSuccess(ErrorCode.FAILED_TO_APPLY.getErrorDesc(),
					ErrorCode.FAILED_TO_APPLY.getErrorCode());
		}

		if (StringUtil.isEmpty(partnerVo.getApplyName())) {
			return AjaxResult.unSuccess(ErrorCode.NAME_IS_NULL.getErrorDesc(), ErrorCode.NAME_IS_NULL.getErrorCode());
		}

		if (StringUtil.isEmpty(partnerVo.getIdCardNum())) {
			return AjaxResult.unSuccess(ErrorCode.IDCARDNUM_IS_NULL.getErrorDesc(),
					ErrorCode.IDCARDNUM_IS_NULL.getErrorCode());
		}

		if (StringUtil.isEmpty(partnerVo.getAlipayAccount())) {
			return AjaxResult.unSuccess(ErrorCode.PAY_ACCOUNT_IS_NULL.getErrorDesc(),
					ErrorCode.PAY_ACCOUNT_IS_NULL.getErrorCode());
		}

		if (StringUtil.isEmpty(partnerVo.getManageAddress())) {
			return AjaxResult.unSuccess(ErrorCode.MANAGE_ADDRESS_IS_NULL.getErrorDesc(),
					ErrorCode.MANAGE_ADDRESS_IS_NULL.getErrorCode());
		}

		if (StringUtil.isEmpty(partnerVo.getIsExitPlace())) {
			return AjaxResult.unSuccess(ErrorCode.IS_EXIST_PLACE_IS_NULL.getErrorDesc(),
					ErrorCode.IS_EXIST_PLACE_IS_NULL.getErrorCode());
		}

		// 只判断省市，区镇不判断
		if (state == null || PartnerApplyStateEnum.STATE_APPLY_WAIT.equals(state)) {
			if (StringUtil.isEmpty(partnerVo.getProvince())) {
				return AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_PROVINCE_IS_NULL.getErrorDesc(),
						ErrorCode.BUSINESS_ADDRESS_PROVINCE_IS_NULL.getErrorCode());
			}
			if (StringUtil.isEmpty(partnerVo.getCity())) {
				return AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_CITY_IS_NULL.getErrorDesc(),
						ErrorCode.BUSINESS_ADDRESS_CITY_IS_NULL.getErrorCode());
			}
		}

		// if (StringUtil.isEmpty(partnerVo.getTown())) {
		// return
		// AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_TOWN_IS_NULL.getErrorDesc(),
		// ErrorCode.BUSINESS_ADDRESS_TOWN_IS_NULL.getErrorCode());
		// }

		if ("y".equalsIgnoreCase(partnerVo.getIsExitPlace()) && StringUtil.isEmpty(partnerVo.getAddress())) {
			return AjaxResult.unSuccess(ErrorCode.BUSINESS_ADDRESS_STREET_IS_NULL.getErrorDesc(),
					ErrorCode.BUSINESS_ADDRESS_STREET_IS_NULL.getErrorCode());
		}

		if (StringUtil.isEmpty(partnerVo.getMobile())) {
			return AjaxResult.unSuccess(ErrorCode.MOBILE_IS_NULL.getErrorDesc(),
					ErrorCode.MOBILE_IS_NULL.getErrorCode());
		}

		if (StringUtil.isEmpty(partnerVo.getGender())) {
			return AjaxResult.unSuccess(ErrorCode.GENDER_IS_NULL.getErrorDesc(),
					ErrorCode.GENDER_IS_NULL.getErrorCode());
		}

		// ------------------------ edit by xiaoxi

		if (StringUtil.isEmpty(partnerVo.getNation())) {
			return AjaxResult.unSuccess(ErrorCode.NATION_IS_NULL.getErrorDesc(),
					ErrorCode.NATION_IS_NULL.getErrorCode());
		}

		if (PartnerApplyEducationEnum.getEnum(partnerVo.getEducation()) == null) {
			return AjaxResult.unSuccess(ErrorCode.EDUCATION_IS_INVALID.getErrorDesc(),
					ErrorCode.EDUCATION_IS_INVALID.getErrorCode());
		}

		if (PartnerApplyPoliticsEnum.getEnum(partnerVo.getPolitics()) == null) {
			return AjaxResult.unSuccess(ErrorCode.POLITICS_IS_INVALID.getErrorDesc(),
					ErrorCode.POLITICS_IS_INVALID.getErrorCode());
		}

		return AjaxResult.success(partnerVo);
	}

	@ResourceMapping(value = "/findBasicInfo")
	@NoneResultDecrator
	public AjaxResult<PartnerVo> findBasicInfo() {
		// 校验重复报名
		AjaxResult<Boolean> repeatResult = validateRepeatApply();
		if (!repeatResult.isSuccess()) {
			return AjaxResult.unSuccess(repeatResult.getExceptionDesc(), repeatResult.getErrorCode());
		}
		// 校验PartnVo
		PartnerVo partnerVo = new PartnerVo();
		AjaxResult<PartnerVo> result = validatePartnerVo(partnerVo);

		// 安全
		partnerVo.setIdCardNum(SensitiveDataUtil.idCardNoHide(partnerVo.getIdCardNum()));
		partnerVo.setAlipayAccount(SensitiveDataUtil.alipayLogonIdHide(partnerVo.getAlipayAccount()));

		return result;
	}

	private AjaxResult<PartnerVo> validatePartnerVo(PartnerVo partnerVo) {
		// 从UIC用户中心，获取基础用户信息
		ResultDO<BaseUserDO> baseUserDOresult = uicReadServiceClient.getBaseUserByUserId(getUserId());
		// 用户账号校验
		AjaxResult<Boolean> validateResult = validateUser(baseUserDOresult);
		if (!validateResult.isSuccess()) {
			return AjaxResult.unSuccess(validateResult.getExceptionDesc(), validateResult.getErrorCode());
		}

		BaseUserDO baseUserDO = baseUserDOresult.getModule();

		// 根据淘宝账号，获取支付宝账号信息
		ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult = uicPaymentAccountReadServiceClient
				.getAccountByUserId(baseUserDO.getUserId());
		// 支付宝绑定校验
		validateResult = validatePayAccount(baseUserDO, basePaymentAccountDOResult);
		if (!validateResult.isSuccess()) {
			return AjaxResult.unSuccess(validateResult.getExceptionDesc(), validateResult.getErrorCode());
		}

		// 重置一下支付宝账号和身份证号
		partnerVo.setApplyName(baseUserDO.getFullname());
		partnerVo.setIdCardNum(baseUserDO.getIdCardNumber());
		partnerVo.setAlipayAccount(basePaymentAccountDOResult.getModule().getOutUser());

		// 未满18周岁校验 只有支付宝实名认证后，才有身份证号码
		if (!PersonalCardUtil.checkAgeIsAdult(partnerVo.getIdCardNum())) {
			return AjaxResult.unSuccess(ErrorCode.UNDER_AGE_18.getErrorDesc(), ErrorCode.UNDER_AGE_18.getErrorCode());
		}

		return AjaxResult.success(partnerVo);
	}

	/**
	 * 校验用户是否存在
	 * 
	 * @param baseUserDOresult
	 * @return
	 */
	private AjaxResult<Boolean> validateUser(ResultDO<BaseUserDO> baseUserDOresult) {
		if (baseUserDOresult == null || !baseUserDOresult.isSuccess() || baseUserDOresult.getModule() == null) {
			return AjaxResult.unSuccess(ErrorCode.USERID_NOT_EXISTS.getErrorDesc(),
					ErrorCode.USERID_NOT_EXISTS.getErrorCode());
		}
		BaseUserDO baseUserDO = baseUserDOresult.getModule();
		if (baseUserDO.getUserId() == null || baseUserDO.getUserId() == 0) {
			return AjaxResult.unSuccess(ErrorCode.USERID_NOT_EXISTS.getErrorDesc(),
					ErrorCode.USERID_NOT_EXISTS.getErrorCode());
		}
		return AjaxResult.success(Boolean.TRUE);

	}

	/**
	 * 支付宝账号校验：淘宝账号是否绑定支付宝，是否实名认证
	 * 
	 * @param baseUserDO
	 * @param basePaymentAccountDOResult
	 * @return
	 */
	private AjaxResult<Boolean> validatePayAccount(BaseUserDO baseUserDO,
			ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult) {
		// 未绑定支付宝账号
		if (!PartnerUtil.hasBindToPayAccount(basePaymentAccountDOResult)) {
			return AjaxResult.unSuccess(ErrorCode.USER_NOT_BIND_PAY_ACCOUNT.getErrorDesc(),
					ErrorCode.USER_NOT_BIND_PAY_ACCOUNT.getErrorCode());
		}

		// 未进行过实名认证
		if (PartnerUtil.isNotRealNameAuth(baseUserDO)) {
			return AjaxResult.unSuccess(ErrorCode.USER_NOT_AUTH.getErrorDesc(), ErrorCode.USER_NOT_AUTH.getErrorCode());
		}
		return AjaxResult.success(Boolean.TRUE);
	}

	/**
	 * 重复报名校验
	 * 
	 * @return
	 */
	private AjaxResult<Boolean> validateRepeatApply() {
		Long taobaoId = getUserId();

		ContextDto context = PartnerUtil.assemberContext(taobaoId);

		// 已经录入村点的，不能再报名
		ResultModel<Boolean> resultModel = stationApplyQueryService.isExistsStationApplyByTaobaoUserId(taobaoId,
				context);
		if (null != resultModel && resultModel.isSuccess() && resultModel.getResult()) {
			return AjaxResult.unSuccess(ErrorCode.HAS_BEEN_PARTNER.getErrorDesc(),
					ErrorCode.HAS_BEEN_PARTNER.getErrorCode());
		}

		PartnerApplyDto existPartner = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, context);

		// 已经是合伙人
		// if (PartnerUtil.hasBeenPartner(existPartner)) {
		// return
		// AjaxResult.unSuccess(ErrorCode.HAS_BEEN_PARTNER.getErrorDesc(),
		// ErrorCode.HAS_BEEN_PARTNER.getErrorCode());
		// }

		// 已经报过名了
		if (PartnerUtil.hasEnrolled(existPartner)) {
			return AjaxResult.unSuccess(ErrorCode.REPEAT_APPLY.getErrorDesc(), ErrorCode.REPEAT_APPLY.getErrorCode());
		}
		return AjaxResult.success(Boolean.TRUE);
	}
}

class IdCardImgIdsVo {
	private Long firstImgId;
	private Long secondImgId;
	private Long jointImgId;

	public Long getFirstImgId() {
		return firstImgId;
	}

	public void setFirstImgId(Long firstImgId) {
		this.firstImgId = firstImgId;
	}

	public Long getSecondImgId() {
		return secondImgId;
	}

	public void setSecondImgId(Long secondImgId) {
		this.secondImgId = secondImgId;
	}

	public Long getJointImgId() {
		return jointImgId;
	}

	public void setJointImgId(Long jointImgId) {
		this.jointImgId = jointImgId;
	}

	public boolean haveAllIds() {
		return firstImgId > 0 && secondImgId > 0 && jointImgId > 0;
	}
}