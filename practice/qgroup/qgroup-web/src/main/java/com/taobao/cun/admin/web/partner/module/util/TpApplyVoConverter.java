package com.taobao.cun.admin.web.partner.module.util;

import java.util.List;

import com.alibaba.alimonitor.jmonitor.utils.StringUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.taobao.cun.admin.web.vo.FileUploadVo;
import com.taobao.cun.admin.web.vo.TpApplyVo;
import com.taobao.cun.crius.attachement.domain.Attachement;
import com.taobao.cun.crius.train.dto.ApplyDetailODto;
import com.taobao.cun.crius.train.dto.TrainTpApplyDto;
import com.taobao.cun.crius.train.enums.TPBusinessEnum;

public class TpApplyVoConverter {

	public static TrainTpApplyDto convertToDto(TpApplyVo vo) {
		if (null == vo) {
			return null;
		}
		TrainTpApplyDto dto = new TrainTpApplyDto();
		dto.setAddressName(getAddressName(vo));
		dto.setAddressDetailCode(getAddressDetailCode(vo));
		dto.setAddressExt(vo.getAddress());
		dto.setAlipay(vo.getAlipayAccount());
		dto.setApplyerJob(vo.getApplyerJob());
		dto.setApplyerName(vo.getApplyerName());

		dto.setBusinessType(vo.getBusinessType());
		dto.setEmail(vo.getEmail());
		dto.setLegalPersonName(vo.getLegalPersonName());
		dto.setOrgName(vo.getOrgName());
		dto.setServeArea(vo.getServeArea());
		dto.setStatus(vo.getStatus());
		dto.setTaobaoUserId(vo.getTaobaoUserId());
		dto.setTeacherNum(vo.getTeacherNum());
		dto.setTeamNum(vo.getTeamNum());
		dto.setTel(vo.getTel());

		dto.setAttachmentList(FileUploadVoConverter.convertToFileUploadDtoList(vo.getAttachmentList()));
		dto.setBusinessLicenceList(FileUploadVoConverter.convertToFileUploadDtoList(vo.getBusinessLicenceList()));

		return dto;
	}

	public static TpApplyVo convertFromApplyDetailODto(ApplyDetailODto dto) {
		if (null == dto) {
			return null;
		}
		TpApplyVo vo = new TpApplyVo();
		vo.setOrgName(dto.getOrgName());
		vo.setAlipayAccount(dto.getAliPay());
		vo.setAddress(dto.getAddress());
		vo.setLegalPersonName(dto.getLegalPersionName());
		vo.setTeamNum(dto.getMemberNum());
		vo.setTeacherNum(dto.getTeacherNum());
		vo.setServeArea(dto.getServeArea());
		TPBusinessEnum businessType = TPBusinessEnum.valueof(dto.getBusiness());
		if(null == businessType){
			throw new NullPointerException("invalid business type:" + dto.getBusiness());
		}
		vo.setBusinessType(businessType.getDesc());
		vo.setAttachmentList(convertFromAttachmentList(dto.getAttachements()));
		vo.setBusinessLicenceList(convertFromAttachmentList(dto.getImages()));
		vo.setApplyerName(dto.getApplyerName());
		vo.setTel(dto.getTel());
		vo.setEmail(dto.getEmail());
		vo.setStatus(dto.getState());
		return vo;
	}

	private static FileUploadVo convertFromAttachment(Attachement attach) {
		if (null == attach) {
			return null;
		}
		FileUploadVo vo = new FileUploadVo();
		vo.setFileType(attach.getFileType());
		vo.setFsId(attach.getFsId());
		vo.setTitle(attach.getTitle());
		return vo;
	}

	private static List<FileUploadVo> convertFromAttachmentList(List<Attachement> attachList) {
		if (null == attachList) {
			return null;
		}
		List<FileUploadVo> list = Lists.newArrayList();
		for (Attachement attach : attachList) {
			if (null != attach) {
				list.add(convertFromAttachment(attach));
			}
		}
		return list;
	}

	private static String getAddressName(TpApplyVo vo) {
		String province = vo.getProvinceDetail();
		String city = vo.getCityDetail();
		String county = vo.getCountyDetail();
		String town = vo.getTownDetail();
		List<String> list = Lists.newArrayList();
		if (StringUtils.isNotBlank(province)) {
			list.add(province);
		}
		if (StringUtils.isNotBlank(city)) {
			list.add(city);
		}
		if (StringUtils.isNotBlank(county)) {
			list.add(county);
		}
		if (StringUtils.isNotBlank(town)) {
			list.add(town);
		}
		return Joiner.on("|").join(list);
	}

	private static String getAddressDetailCode(TpApplyVo vo) {
		String province = vo.getProvince();
		String city = vo.getCity();
		String county = vo.getCounty();
		String town = vo.getTown();
		List<String> list = Lists.newArrayList();
		if (StringUtils.isNotBlank(province)) {
			list.add(province);
		}
		if (StringUtils.isNotBlank(city)) {
			list.add(city);
		}
		if (StringUtils.isNotBlank(county)) {
			list.add(county);
		}
		if (StringUtils.isNotBlank(town)) {
			list.add(town);
		}
		return Joiner.on("|").join(list);
	}

}
