package com.taobao.cun.admin.web.partner.module.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.admin.web.constants.ViewConstants;
import com.taobao.cun.admin.web.vo.PartnerVo;
import com.taobao.cun.dto.partner.PartnerApplyDto;
import com.taobao.cun.dto.partner.PartnerApplyStateEnum;

/**
 * PartnerVo和PartnerApplyDto之间的转换器
 * 
 * @author haihu.fhh
 */
public final class PartnerVoConverter {

	private PartnerVoConverter() {

	}

	public static PartnerVo convert2PartnerVoStep(HttpServletRequest request, PartnerApplyStateEnum state) {
		if (null == request) {
			return null;
		}
		PartnerVo partnerVo = new PartnerVo();
		partnerVo.setCurrentStep(request.getParameter("currentStep"));
		if (ViewConstants.STEP_BASIC.equals(partnerVo.getCurrentStep())) { // 基本信息
			partnerVo.setApplyName(request.getParameter("applyName"));
			partnerVo.setGender(request.getParameter("gender"));
			partnerVo.setIdCardNum(request.getParameter("idCardNum"));
			partnerVo.setAlipayAccount(request.getParameter("alipayAccount"));
			partnerVo.setMobile(request.getParameter("mobile"));
			partnerVo.setEducation(request.getParameter("education"));

			partnerVo.setAddress(request.getParameter("address")); // 经营详细地址
			// 计划经营地址
			partnerVo.setProvince(request.getParameter("province"));
			partnerVo.setProvinceDetail(request.getParameter("provinceDetail"));
			partnerVo.setCity(request.getParameter("city"));
			partnerVo.setCityDetail(request.getParameter("cityDetail"));
			partnerVo.setCounty(request.getParameter("county"));
			partnerVo.setCountyDetail(request.getParameter("countyDetail"));
			partnerVo.setTown(request.getParameter("town"));
			partnerVo.setTownDetail(request.getParameter("townDetail"));
			partnerVo.setStationVillageCode(request.getParameter("stationVillageCode"));
			partnerVo.setStationVillageDetail(request.getParameter("stationVillageDetail"));
			partnerVo.setIdCardImgsFace(request.getParameter("idCardImgsFace")); // 身份证正面图片
			partnerVo.setIdCardImgsBack(request.getParameter("idCardImgsBack")); // 身份证反面图片

		} else if (ViewConstants.STEP_ADVANCE.equals(partnerVo.getCurrentStep())) { // 详细信息

			partnerVo.setPolitics(request.getParameter("politics"));

			// 现居住地 manageAddress
			partnerVo.setManageProvince(request.getParameter("manageProvince"));
			partnerVo.setManageProvinceDetail(request.getParameter("manageProvinceDetail"));
			partnerVo.setManageCity(request.getParameter("manageCity"));
			partnerVo.setManageCityDetail(request.getParameter("manageCityDetail"));
			partnerVo.setManageCounty(request.getParameter("manageCounty"));
			partnerVo.setManageCountyDetail(request.getParameter("manageCountyDetail"));
			partnerVo.setManageTown(request.getParameter("manageTown"));
			partnerVo.setManageTownDetail(request.getParameter("manageTownDetail"));
			partnerVo.setManageVillageCode(request.getParameter("manageVillageCode"));
			partnerVo.setManageVillageDetail(request.getParameter("manageVillageDetail"));

			partnerVo.setManageAddress(request.getParameter("manageAddress")); // 现居住地详细地址
			partnerVo.setPopulation(request.getParameter("population"));

			partnerVo.setIsExitPlace(request.getParameter("isExitPlace"));
			partnerVo.setLocationImgs(request.getParameter("LocationImgs")); // 经营地照片
			partnerVo.setManageArea(request.getParameter("manageArea")); // 营业面积

			partnerVo.setEmail(request.getParameter("email"));
			partnerVo.setNation(request.getParameter("nation"));
			partnerVo.setAreaRange(request.getParameter("areaRange")); // 村面积范围
			partnerVo.setPerIncomeRange(request.getParameter("perIncomeRange"));// 平均收入
			partnerVo.setAgeRange(request.getParameter("ageRange")); // 主要年龄
			partnerVo.setNetworkCoverage(request.getParameter("isExistNetwork")); // 网络是否覆盖
			partnerVo.setWorkExp(request.getParameter("workExp")); // 工作经历
			partnerVo.setBusProposal(request.getParameter("busProposal")); // 经营计划书
			if (request.getParameter("fromChannels") != null) {
				partnerVo.setFromChannels(request.getParameter("fromChannels")); // 得知渠道单项
			} else if (request.getParameterValues("fromChannels[]") != null) {
				partnerVo.setFromChannels(StringUtil.join(request.getParameterValues("fromChannels[]"), "|")); // 得知渠道多项
			}

			if (request.getParameter("netPlatformExp") != null) {
				// 单个其他平台销售经验
				partnerVo.setNetPlatformExp(request.getParameter("netPlatformExp"));
			} else if (request.getParameterValues("netPlatformExp[]") != null) {
				partnerVo.setNetPlatformExp(StringUtil.join(request.getParameterValues("netPlatformExp[]"), "|")); // 多个参数其他平台销售经验
			}
			partnerVo.setOnlineShopping(request.getParameter("onlineShopping"));
			partnerVo.setIsKnowTaobao(request.getParameter("isKnowTaobao"));
		}

		return partnerVo;
	}

	public static PartnerVo convert2PartnerVo(HttpServletRequest request, PartnerApplyStateEnum state) {
		if (null == request) {
			return null;
		}
		PartnerVo partnerVo = new PartnerVo();
		partnerVo.setCurrentStep(request.getParameter("currentStep"));
		partnerVo.setApplyName(request.getParameter("applyName"));
		partnerVo.setGender(request.getParameter("gender"));
		partnerVo.setIdCardNum(request.getParameter("idCardNum"));
		partnerVo.setAlipayAccount(request.getParameter("alipayAccount"));
		// 现居住地 manageAddress
		partnerVo.setManageProvince(request.getParameter("manageProvince"));
		partnerVo.setManageProvinceDetail(request.getParameter("manageProvinceDetail"));
		partnerVo.setManageCity(request.getParameter("manageCity"));
		partnerVo.setManageCityDetail(request.getParameter("manageCityDetail"));
		partnerVo.setManageCounty(request.getParameter("manageCounty"));
		partnerVo.setManageCountyDetail(request.getParameter("manageCountyDetail"));
		partnerVo.setManageTown(request.getParameter("manageTown"));
		partnerVo.setManageTownDetail(request.getParameter("manageTownDetail"));
		partnerVo.setManageVillageCode(request.getParameter("manageVillageCode"));
		partnerVo.setManageVillageDetail(request.getParameter("manageVillageDetail"));

		partnerVo.setManageAddress(request.getParameter("manageAddress"));
		partnerVo.setIsExitPlace(request.getParameter("isExitPlace"));

		if (state == null || PartnerApplyStateEnum.STATE_APPLY_WAIT.equals(state)) {
			partnerVo.setProvince(request.getParameter("province"));
			partnerVo.setProvinceDetail(request.getParameter("provinceDetail"));
			partnerVo.setCity(request.getParameter("city"));
			partnerVo.setCityDetail(request.getParameter("cityDetail"));
			partnerVo.setCounty(request.getParameter("county"));
			partnerVo.setCountyDetail(request.getParameter("countyDetail"));
			partnerVo.setTown(request.getParameter("town"));
			partnerVo.setTownDetail(request.getParameter("townDetail"));
			// --add new fields
			partnerVo.setStationVillageCode(request.getParameter("stationVillageCode"));
			partnerVo.setStationVillageDetail(request.getParameter("stationVillageDetail"));
		}

		partnerVo.setAddress(request.getParameter("address")); // 经营详细地址

		partnerVo.setManageArea(request.getParameter("manageArea"));
		partnerVo.setMobile(request.getParameter("mobile"));
		partnerVo.setEmail(request.getParameter("email"));
		partnerVo.setOnlineShopping(request.getParameter("onlineShopping"));
		partnerVo.setIsKnowTaobao(request.getParameter("isKnowTaobao"));
		// -------------- 新增报名内容
		partnerVo.setEducation(request.getParameter("education"));
		partnerVo.setAgeRange(request.getParameter("ageRange"));
		partnerVo.setPopulation(request.getParameter("population"));
		partnerVo.setLocationImgs(request.getParameter("locationImgs")); // 经营地照片
		partnerVo.setBusProposal(request.getParameter("busProposal"));
		partnerVo.setWorkExp(request.getParameter("workExp"));
		partnerVo.setAreaRange(request.getParameter("areaRange"));
		partnerVo.setPerIncomeRange(request.getParameter("perIncomeRange"));
		partnerVo.setFromChannels(request.getParameter("fromChannels"));
		partnerVo.setNetPlatformExp(request.getParameter("netPlatformExp"));
		partnerVo.setNation(request.getParameter("nation"));
		partnerVo.setPolitics(request.getParameter("politics"));
		return partnerVo;
	}

	public static PartnerApplyDto convert2PartnerApplyDto(PartnerVo partnerVo, PartnerApplyStateEnum state) {
		if (null == partnerVo) {
			return null;
		}
		PartnerApplyDto partnerApplyDto = new PartnerApplyDto();

		partnerApplyDto.setName(partnerVo.getApplyName());
		partnerApplyDto.setGender(partnerVo.getGender());
		partnerApplyDto.setIdenNum(partnerVo.getIdCardNum());
		partnerApplyDto.setAlipayAccount(partnerVo.getAlipayAccount());
		// 现在居住地
		partnerApplyDto.setAddressDetail(partnerVo.getManageAddress());
		partnerApplyDto.setHasLocation(partnerVo.getIsExitPlace());
		// 无营业场地时，将营业面积设置为空
		if ("n".equals(partnerVo.getIsExitPlace())) {
			partnerApplyDto.setLocationArea("");
			partnerApplyDto.setLocationImgs("");
		} else {
			partnerApplyDto.setLocationArea(partnerVo.getManageArea());
			partnerApplyDto.setLocationImgs(partnerVo.getLocationImgs());
		}
		if (state == null || PartnerApplyStateEnum.STATE_APPLY_WAIT.equals(state)) {
			partnerApplyDto.setStationAddressDetailCode(assemblerStationAddressDetailCode(partnerVo));
			partnerApplyDto.setStationAddressDetail(assemblerStationAddressDetail(partnerVo));
		}
		partnerApplyDto.setStationAddressDetailExt(partnerVo.getAddress());

		partnerApplyDto.setPhone(partnerVo.getMobile());
		partnerApplyDto.setEmail(partnerVo.getEmail());
		partnerApplyDto.setTaobaoUserId(partnerVo.getTaobaoUserId());
		partnerApplyDto.setKnowTaobao(partnerVo.getIsKnowTaobao());
		partnerApplyDto.setShoppingOnline(partnerVo.getOnlineShopping());
		partnerApplyDto.setApplyTime(new Date());
		// ----------------- new fields -----------------
		partnerApplyDto.setEducation(partnerVo.getEducation());
		partnerApplyDto.setAgeRange(partnerVo.getAgeRange());
		partnerApplyDto.setPopulation(partnerVo.getPopulation());
		// locationImgs 字段在上文
		partnerApplyDto.setBusProposal(partnerVo.getBusProposal());
		partnerApplyDto.setWorkExp(partnerVo.getWorkExp());
		partnerApplyDto.setAreaRange(partnerVo.getAreaRange());
		partnerApplyDto.setPerIncomeRange(partnerVo.getPerIncomeRange());
		partnerApplyDto.setFromChannels(partnerVo.getFromChannels());
		partnerApplyDto.setNetPlatformExp(partnerVo.getNetPlatformExp());
		partnerApplyDto.setNation(partnerVo.getNation());
		partnerApplyDto.setPolitics(partnerVo.getPolitics());
		if (state != null) {
			partnerApplyDto.setPartnerApplyStateEnum(state);
		}
		// ------------
		partnerApplyDto.setNetworkCoverage(partnerVo.getNetworkCoverage());
		return partnerApplyDto;
	}

	/** 按步骤转换Vo成Dto */
	public static PartnerApplyDto convert2PartnerApplyDtoStep(PartnerVo partnerVo, PartnerApplyDto partnerApplyDto) {
		if (null == partnerVo) {
			return null;
		}

		partnerApplyDto.setApplyStep(partnerVo.getCurrentStep());
		
		if (ViewConstants.STEP_BASIC.equals(partnerVo.getCurrentStep())) {
			if (StringUtil.isNotBlank(partnerVo.getApplyName())) {
				partnerApplyDto.setName(partnerVo.getApplyName());
			}
			if (StringUtil.isNotBlank(partnerVo.getGender())) {
				partnerApplyDto.setGender(partnerVo.getGender());
			}
			if (StringUtil.isNotBlank(partnerVo.getIdCardNum())) {
				partnerApplyDto.setIdenNum(partnerVo.getIdCardNum());
			}
			if (StringUtil.isNotBlank(partnerVo.getAlipayAccount())) {
				partnerApplyDto.setAlipayAccount(partnerVo.getAlipayAccount());
			}
			if (StringUtil.isNotBlank(partnerVo.getMobile())) {
				partnerApplyDto.setPhone(partnerVo.getMobile());
			}
			if (StringUtil.isNotBlank(partnerVo.getEducation())) {
				partnerApplyDto.setEducation(partnerVo.getEducation());
			}
			if (null == partnerApplyDto.getPartnerApplyStateEnum()
					|| PartnerApplyStateEnum.STATE_APPLY_WAIT.equals(partnerApplyDto.getPartnerApplyStateEnum())) {
				partnerApplyDto.setStationAddressDetailCode(assemblerStationAddressDetailCode(partnerVo));
				partnerApplyDto.setStationAddressDetail(assemblerStationAddressDetail(partnerVo));
				partnerApplyDto.setStationAddressDetailExt(partnerVo.getAddress());
			}
			if (StringUtil.isNotBlank(partnerVo.getTown())) { // 营业地址 街镇
				partnerApplyDto.setStationVillageCode(partnerVo.getStationVillageCode());
				partnerApplyDto.setStationVillageDetail(partnerVo.getStationVillageDetail());
			} else {
				partnerApplyDto.setStationVillageCode("");
				partnerApplyDto.setStationVillageDetail("");
			}
			partnerApplyDto.setIdCardImgsFace(partnerVo.getIdCardImgsFace());
			partnerApplyDto.setIdCardImgsBack(partnerVo.getIdCardImgsBack());
			
		} else if (ViewConstants.STEP_ADVANCE.equals(partnerVo.getCurrentStep())) {
			partnerApplyDto.setPolitics(partnerVo.getPolitics());
			// 现在居住地
			partnerApplyDto.setAddressDetail(partnerVo.getManageAddress()); // 详情
			partnerApplyDto.setAddressDetailCode(assemblerManageAddressDetailCode(partnerVo));
			partnerApplyDto.setAddressDetailName(assemblerManageAddressDetail(partnerVo));
			if (StringUtil.isNotBlank(partnerVo.getManageTown())) {
				partnerApplyDto.setAddressVillageCode(partnerVo.getManageVillageCode());
				partnerApplyDto.setAddressVillageDetail(partnerVo.getManageVillageDetail());
			} else {
				partnerApplyDto.setAddressVillageCode("");
				partnerApplyDto.setAddressVillageDetail("");
			}

			partnerApplyDto.setPopulation(partnerVo.getPopulation()); // 村人口
			partnerApplyDto.setHasLocation(partnerVo.getIsExitPlace());
			// 无营业场地时，将营业面积设置为空
			if ("n".equals(partnerVo.getIsExitPlace())) {
				partnerApplyDto.setLocationArea("");
				partnerApplyDto.setLocationImgs("");
			} else {
				partnerApplyDto.setLocationArea(partnerVo.getManageArea());
				partnerApplyDto.setLocationImgs(partnerVo.getLocationImgs());
			}
			partnerApplyDto.setEmail(partnerVo.getEmail());
			partnerApplyDto.setNation(partnerVo.getNation());
			partnerApplyDto.setAreaRange(partnerVo.getAreaRange());
			partnerApplyDto.setPerIncomeRange(partnerVo.getPerIncomeRange()); // 村人均收入
			partnerApplyDto.setAgeRange(partnerVo.getAgeRange()); // 村民主要年龄 :
			partnerApplyDto.setNetworkCoverage(partnerVo.getNetworkCoverage()); // 网络是否覆盖
			partnerApplyDto.setWorkExp(partnerVo.getWorkExp()); // 工作经历

			partnerApplyDto.setBusProposal(partnerVo.getBusProposal());// 经营计划书
			partnerApplyDto.setFromChannels(partnerVo.getFromChannels()); // 通过什么渠道了解到的村淘
			partnerApplyDto.setNetPlatformExp(partnerVo.getNetPlatformExp()); // 其他平台销售经验
		}

		// partnerApplyDto.setTaobaoUserId(partnerVo.getTaobaoUserId());
		// partnerApplyDto.setKnowTaobao(partnerVo.getIsKnowTaobao());
		// partnerApplyDto.setShoppingOnline(partnerVo.getOnlineShopping());

		return partnerApplyDto;
	}

	private static String assemblerManageAddressDetail(PartnerVo partnerVo) {
		List<String> address = new ArrayList<String>();
		if (StringUtil.isNotEmpty(partnerVo.getManageProvinceDetail())) {
			address.add(partnerVo.getManageProvinceDetail());
		} else {
			address.add("");
		}
		if (StringUtil.isNotEmpty(partnerVo.getManageCityDetail())) {
			address.add(partnerVo.getManageCityDetail());
		} else {
			address.add("");
		}
		if (StringUtil.isNotEmpty(partnerVo.getManageCountyDetail())) {
			address.add(partnerVo.getManageCountyDetail());
		} else {
			address.add("");
		}
		if (StringUtil.isNotEmpty(partnerVo.getManageTownDetail())) {
			address.add(partnerVo.getManageTownDetail());
		} else {
			address.add("");
		}
		return splitByVerticalBar(address);
	}

	private static String assemblerManageAddressDetailCode(PartnerVo partnerVo) {
		List<String> address = new ArrayList<String>();

		if (StringUtil.isNotEmpty(partnerVo.getManageProvince())) {
			address.add(partnerVo.getManageProvince());
		} else {
			address.add("");
		}

		if (StringUtil.isNotEmpty(partnerVo.getManageCity())) {
			address.add(partnerVo.getManageCity());
		} else {
			address.add("");
		}

		if (StringUtil.isNotEmpty(partnerVo.getManageCounty())) {
			address.add(partnerVo.getManageCounty());
		} else {
			address.add("");
		}

		if (StringUtil.isNotEmpty(partnerVo.getManageTown())) {
			address.add(partnerVo.getManageTown());
		} else {
			address.add("");
		}

		return splitByVerticalBar(address);
	}

	private static String assemblerStationAddressDetailCode(PartnerVo partnerVo) {
		List<String> address = new ArrayList<String>();

		if (StringUtil.isNotEmpty(partnerVo.getProvince())) {
			address.add(partnerVo.getProvince());
		} else {
			address.add("");
		}

		if (StringUtil.isNotEmpty(partnerVo.getCity())) {
			address.add(partnerVo.getCity());
		} else {
			address.add("");
		}

		if (StringUtil.isNotEmpty(partnerVo.getCounty())) {
			address.add(partnerVo.getCounty());
		} else {
			address.add("");
		}

		if (StringUtil.isNotEmpty(partnerVo.getTown())) {
			address.add(partnerVo.getTown());
		} else {
			address.add("");
		}

		return splitByVerticalBar(address);
	}

	private static String assemblerStationAddressDetail(PartnerVo partnerVo) {
		List<String> address = new ArrayList<String>();
		if (StringUtil.isNotEmpty(partnerVo.getProvinceDetail())) {
			address.add(partnerVo.getProvinceDetail());
		} else {
			address.add("");
		}
		if (StringUtil.isNotEmpty(partnerVo.getCityDetail())) {
			address.add(partnerVo.getCityDetail());
		} else {
			address.add("");
		}
		if (StringUtil.isNotEmpty(partnerVo.getCountyDetail())) {
			address.add(partnerVo.getCountyDetail());
		} else {
			address.add("");
		}
		if (StringUtil.isNotEmpty(partnerVo.getTownDetail())) {
			address.add(partnerVo.getTownDetail());
		} else {
			address.add("");
		}
		return splitByVerticalBar(address);
	}

	private static String splitByVerticalBar(List<String> adress) {
		StringBuilder codeDetail = new StringBuilder();
		for (int i = 0, length = adress.size(), lastIndex = length - 1; i < length; i++) {
			codeDetail.append(adress.get(i));
			if (i < lastIndex) {
				codeDetail.append("|");
			}
		}

		return codeDetail.toString();
	}

}
