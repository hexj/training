package com.taobao.cun.admin.web.partner.module.util;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.csp.courier.StringUtils;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.web.home.module.ErrorCode;
import com.taobao.cun.admin.web.vo.FileUploadVo;
import com.taobao.cun.admin.web.vo.TpApplyVo;
import com.taobao.cun.crius.common.dto.ContextDto;
import com.taobao.cun.crius.common.enums.SystemTypeEnum;
import com.taobao.cun.crius.common.enums.UserTypeEnum;
import com.taobao.cun.crius.train.enums.TPBusinessEnum;

public class TpApplyUtil {
	private static final String ERROR_MSG_IMG_NUMBER = "请检查图片上传数量";
	private static final String ERROR_MSG_ATTACHMENT_NUMBER = "请检查附件上传数量";
	private static final Pattern phoneNumberPattern = Pattern.compile("[0-9\\-]+");

	public static ContextDto assemberContext(Long loginId) {
		ContextDto context = new ContextDto();
		context.setLoginId(String.valueOf(loginId));
		context.setAppId(String.valueOf(loginId));
		context.setUserType(UserTypeEnum.HAVANA);
		context.setSystemType(SystemTypeEnum.CUNTAO_ADMIN);
		return context;
	}

	public static AjaxResult<TpApplyVo> assembleTpApplyVo(HttpServletRequest request) {
		TpApplyVo vo = new TpApplyVo();

		vo.setAlipayAccount(request.getParameter("alipayAccount"));
		if (StringUtils.isBlank(vo.getAlipayAccount())) {
			return AjaxResult.unSuccess(ErrorCode.PAY_ACCOUNT_IS_NULL.getErrorDesc(),
					ErrorCode.PAY_ACCOUNT_IS_NULL.getErrorCode());
		}
		vo.setApplyerJob(request.getParameter("applyerJob"));
		if (StringUtils.isBlank(vo.getApplyerJob()) || !isProperLength(vo.getApplyerJob(), 1, 30)) {
			return AjaxResult.unSuccess(ErrorCode.APPLY_JOB_IS_INVALID.getErrorDesc(),
					ErrorCode.APPLY_JOB_IS_INVALID.getErrorCode());
		}
		vo.setApplyerName(request.getParameter("applyerName"));
		if (StringUtils.isBlank(vo.getApplyerName()) || !isProperLength(vo.getApplyerName(), 1, 30)) {
			return AjaxResult.unSuccess(ErrorCode.APPLY_NAME_IS_INVALID.getErrorDesc(),
					ErrorCode.APPLY_NAME_IS_INVALID.getErrorCode());
		}
		vo.setBusinessType(request.getParameter("businessType"));
		if (StringUtils.isBlank(vo.getBusinessType())
				|| !isValidBusinessType(vo.getBusinessType())) {
			return AjaxResult.unSuccess(ErrorCode.BUSSINESS_TYPE_IS_INVALID.getErrorDesc(),
					ErrorCode.BUSSINESS_TYPE_IS_INVALID.getErrorCode());
		}

		vo.setProvince(request.getParameter("province"));
		vo.setProvinceDetail(request.getParameter("provinceDetail"));
		vo.setCity(request.getParameter("city"));
		vo.setCityDetail(request.getParameter("cityDetail"));
		vo.setCounty(request.getParameter("county"));
		vo.setCountyDetail(request.getParameter("countyDetail"));
		vo.setTown(request.getParameter("town"));
		vo.setTownDetail(request.getParameter("townDetail"));
		vo.setAddress(request.getParameter("address"));
		vo.setAddressDetailCode(request.getParameter("addressDetailCode"));
		if (StringUtil.isEmpty(vo.getProvince()) || StringUtil.isEmpty(vo.getProvinceDetail())) {
			return AjaxResult.unSuccess(ErrorCode.ADDRESS_AREA_IS_INVALID.getErrorDesc(),
					ErrorCode.ADDRESS_AREA_IS_INVALID.getErrorCode());
		}
		if (StringUtil.isEmpty(vo.getAddress()) || !isProperLength(vo.getAddress(), 1, 100)) {
			return AjaxResult.unSuccess(ErrorCode.ADDRESS_STREET_IS_INVALID.getErrorDesc(),
					ErrorCode.ADDRESS_STREET_IS_INVALID.getErrorCode());
		}

		vo.setLegalPersonName(request.getParameter("legalPersonName"));
		if (StringUtils.isBlank(vo.getLegalPersonName()) || !isProperLength(vo.getLegalPersonName(), 1, 30)) {
			return AjaxResult.unSuccess(ErrorCode.LEAGELPERSON_NAME_IS_INVALID.getErrorDesc(),
					ErrorCode.LEAGELPERSON_NAME_IS_INVALID.getErrorCode());
		}
		vo.setOrgName(request.getParameter("orgName"));
		if (StringUtils.isBlank(vo.getOrgName()) || !isProperLength(vo.getOrgName(), 1, 100)) {
			return AjaxResult.unSuccess(ErrorCode.ORG_NAME_IS_INVALID.getErrorDesc(),
					ErrorCode.ORG_NAME_IS_INVALID.getErrorCode());
		}
		vo.setServeArea(request.getParameter("serveArea"));
		if (StringUtils.isBlank(vo.getServeArea()) || !isProperLength(vo.getServeArea(), 1, 25)) {
			return AjaxResult.unSuccess(ErrorCode.SERVE_AREA_IS_INVALID.getErrorDesc(),
					ErrorCode.SERVE_AREA_IS_INVALID.getErrorCode());
		}
		vo.setTel(request.getParameter("tel"));
		if (StringUtils.isBlank(vo.getTel()) || !isPhoneNumber(vo.getTel()) || !isProperLength(vo.getTel(), 1, 20)) {
			return AjaxResult.unSuccess(ErrorCode.MOBILE_IS_NULL.getErrorDesc(),
					ErrorCode.MOBILE_IS_NULL.getErrorCode());
		}
		vo.setEmail(request.getParameter("email"));
		if (StringUtils.isBlank(vo.getEmail()) || !isEmail(vo.getEmail()) || !isProperLength(vo.getEmail(), 1, 50)) {
			return AjaxResult.unSuccess(ErrorCode.EMAIL_IS_INVALID.getErrorDesc(),
					ErrorCode.EMAIL_IS_INVALID.getErrorCode());
		}

		String attachmentListStr = request.getParameter("fileData");
		String businessLicenceListStr = request.getParameter("imgData");

		try {
			vo.setTeacherNum(Integer.parseInt(request.getParameter("teacherNum")));
		} catch (Exception e) {
			return AjaxResult.unSuccess(ErrorCode.TEACHER_NUMBER_IS_INVALID.getErrorDesc(),
					ErrorCode.TEACHER_NUMBER_IS_INVALID.getErrorCode());
		}
		try {
			vo.setTeamNum(Integer.parseInt(request.getParameter("teamNum")));
		} catch (Exception e) {
			return AjaxResult.unSuccess(ErrorCode.TEAM_NUMBER_IS_INVALID.getErrorDesc(),
					ErrorCode.TEAM_NUMBER_IS_INVALID.getErrorCode());
		}
		try {
			List<FileUploadVo> attachmentList = JSON.parseArray(attachmentListStr, FileUploadVo.class);
			vo.setAttachmentList(attachmentList);
			if (!checkListSize(attachmentList, 1, 5)) {
				return AjaxResult.unSuccess(ERROR_MSG_ATTACHMENT_NUMBER, ErrorCode.FAILED_TO_APPLY.getErrorCode());
			}

			List<FileUploadVo> businessLicenceList = JSON.parseArray(businessLicenceListStr, FileUploadVo.class);
			vo.setBusinessLicenceList(businessLicenceList);
			if (!checkListSize(businessLicenceList, 1, 5)) {
				return AjaxResult.unSuccess(ERROR_MSG_IMG_NUMBER, ErrorCode.FAILED_TO_APPLY.getErrorCode());
			}

		} catch (Exception e) {
			return AjaxResult.unSuccess(ErrorCode.FAILED_TO_APPLY.getErrorDesc(),
					ErrorCode.FAILED_TO_APPLY.getErrorCode());
		}

		return AjaxResult.success(vo);
	}

	private static boolean isValidBusinessType(String businessType) {
		if(null == TPBusinessEnum.valueof(businessType)){
			return false;
		}
		return true;
	}

	private static boolean isProperLength(String str, int min, int max) {
		if (null == str) {
			return false;
		}
		return (str.length() >= min) && (str.length() <= max);
	}

	private static boolean isPhoneNumber(String tel) {
		if (null == tel) {
			return false;
		}
		return phoneNumberPattern.matcher(tel).matches();
	}

	private static boolean isEmail(String email) {
		if (null == email) {
			return false;
		}
		return email.indexOf('@') > 0;
	}

	private static <T> boolean checkListSize(List<T> list, int min, int max) {
		return (null != list && list.size() >= min && list.size() <= max);
	}

	public static void main(String[] args) {
		System.out.println(phoneNumberPattern.matcher("233-232").matches());
		System.out.println(isProperLength("我们e", 1, 2));
	}

}
