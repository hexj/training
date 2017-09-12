package com.taobao.cun.admin.web.home.module.screen.my;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.primitives.Longs;
import com.taobao.cun.admin.adapter.cuntao.center.PartnerServiceAdapter;
import com.taobao.cun.admin.web.constants.ViewConstants;
import com.taobao.cun.admin.web.partner.module.util.PartnerUtil;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.admin.web.vo.FileUploadVo;
import com.taobao.cun.crius.attachement.domain.Attachement;
import com.taobao.cun.crius.attachement.service.AttachementService;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.partner.PartnerApplyDto;
import com.taobao.cun.dto.partner.PartnerApplyEducationEnum;
import com.taobao.cun.dto.partner.PartnerApplyFromChannelEnum;
import com.taobao.cun.dto.partner.PartnerApplyNetPlatEnum;
import com.taobao.cun.dto.partner.PartnerApplyPoliticsEnum;
import com.taobao.cun.dto.partner.PartnerApplyStateEnum;
import com.taobao.cun.dto.partner.PartnerApplyVillageAgeEnum;
import com.taobao.cun.dto.partner.PartnerApplyVillageAreaEnum;
import com.taobao.cun.dto.partner.PartnerApplyVillagePerIncomeEnum;
import com.taobao.cun.dto.partner.PartnerApplyVillagePopulationEnum;
import com.taobao.security.util.SensitiveDataUtil;
import com.tmall.lsp.locationservice.common.domain.division.VillageVO;
import com.tmall.lsp.locationservice.common.service.DivisionManagerService;

/**
 * �ϻ��������
 * 
 */
public class CopartnerApplyStation {

	public static final Logger logger = LoggerFactory.getLogger(CopartnerApplyStation.class);

	@Autowired
	protected HttpSession session;

	@Autowired
	private PartnerServiceAdapter partnerServiceAdapter;

	@Autowired
	private AttachementService attachmentService;

	@Autowired
	private DivisionManagerService divisionManagerService;

	public void execute(TurbineRunData rundata, Context context, Navigator navigator) {
		// ѧ���б�
		context.put("educationList", PartnerApplyEducationEnum.getUnmodifiableRecords());
		// ������ò�б�
		context.put("politicsList", PartnerApplyPoliticsEnum.getUnmodifiableRecords());
		// �����б�
		context.put("fromChannelList", PartnerApplyFromChannelEnum.getUnmodifiableRecords());
		// ������б�
		context.put("areaRangeList", PartnerApplyVillageAreaEnum.getUnmodifiableRecords());
		// ���˿��б�
		context.put("populationList", PartnerApplyVillagePopulationEnum.getUnmodifiableRecords());
		// ���˾������б�
		context.put("perIncomeRangeList", PartnerApplyVillagePerIncomeEnum.getUnmodifiableRecords());
		// ������Ҫ�����б�
		context.put("ageRangeList", PartnerApplyVillageAgeEnum.getUnmodifiableRecords());
		// ��������ƽ̨�б�
		context.put("netPlatList", PartnerApplyNetPlatEnum.getUnmodifiableRecords());

		// �Ƿ�༭
		String isEdit = rundata.getParameters().getString("isEdit");
		// ��ǰ�༭����
		String step = rundata.getParameters().getString("currentStep");

		Long taobaoId = WebUtil.getUserId(session);
		ContextDto contextDto = PartnerUtil.assemberContext(taobaoId);
		PartnerApplyDto partnerApplyDto = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, contextDto);

		context.put("canEditAddress", Boolean.TRUE.toString());

		context.put("showStationVilliage", Boolean.FALSE.toString());
		context.put("showManageVilliage", Boolean.FALSE.toString());

		if (ViewConstants.IS_EDIT.equalsIgnoreCase(isEdit)) {
			context.put("isEdit", Boolean.TRUE.toString());

			if (null != partnerApplyDto && partnerApplyDto.getId() > 0) {
				if (ViewConstants.STEP_BASIC.equalsIgnoreCase(step)) {
					context.put("currentStep", ViewConstants.STEP_BASIC);
				} else if (ViewConstants.STEP_ADVANCE.equalsIgnoreCase(step)) {
					if (partnerApplyDto.getInfoLevel() != null
							&& (ViewConstants.INFO_BASIC.toString().equals(partnerApplyDto.getInfoLevel())
									|| ViewConstants.INFO_ADVANCE.toString().equals(partnerApplyDto.getInfoLevel()))) {
						context.put("currentStep", ViewConstants.STEP_ADVANCE);
					} else {
						context.put("currentStep", ViewConstants.STEP_BASIC);
					}
				} else { // ��ǰΪ�༭, step����1��2ʱ������쳣
					rundata.setRedirectLocation(ViewConstants.PAGE_DETAIL); // �鿴��������
				}

				// �������״̬��Χ�ϻ�����Ϣ�Ƿ�����༭(���ڳ���״̬������༭)
				if (null == partnerApplyDto.getPartnerApplyStateEnum()
						|| PartnerApplyStateEnum.STATE_APPLY_WAIT.equals(partnerApplyDto.getPartnerApplyStateEnum())) {
				} else {
					context.put("canEditAddress", Boolean.FALSE.toString());
				}
				loadEditData(partnerApplyDto, context);
			} else {
				context.put("isEdit", Boolean.FALSE.toString());
				context.put("currentStep", ViewConstants.STEP_BASIC);
			}

		} else {

			if (null != partnerApplyDto && partnerApplyDto.getId() > 0) {
				if (ViewConstants.INFO_BASIC.equals(partnerApplyDto.getInfoLevel())
						|| ViewConstants.INFO_ADVANCE.equals(partnerApplyDto.getInfoLevel())) { // �Ѿ�����Ϣ���鿴����
					rundata.setRedirectLocation(ViewConstants.PAGE_DETAIL); // �鿴��������
				}
			}
			context.put("isEdit", Boolean.FALSE.toString());
			context.put("currentStep", ViewConstants.STEP_BASIC);
			// �ظ��Լ���� findbasic rpc ��
		}

	}

	private void loadEditData(PartnerApplyDto partnerApplyDto, Context context) {
		context.put("objectId", partnerApplyDto.getId());
		context.put("applyName", partnerApplyDto.getName());
		context.put("gender", partnerApplyDto.getGender());
		context.put("idCardNum", SensitiveDataUtil.idCardNoHide(partnerApplyDto.getIdenNum()));
		context.put("alipayAccount", SensitiveDataUtil.alipayLogonIdHide(partnerApplyDto.getAlipayAccount()));
		context.put("mobile", partnerApplyDto.getPhone());
		// --- ѧ��
		context.put("education", partnerApplyDto.getEducation());
		// Ӫҵ��ַ
		context.put("stationAddressDetail", partnerApplyDto.getStationAddressDetail());
		context.put("stationAddressDetailCode", partnerApplyDto.getStationAddressDetailCode());
		context.put("stationAddressDetailExt", partnerApplyDto.getStationAddressDetailExt());
		context.put("stationVillageCode", partnerApplyDto.getStationVillageCode());
		context.put("stationVillageDetail", partnerApplyDto.getStationVillageDetail());

		// ���֤��Ƭ
		getIdCardImgs(partnerApplyDto.getId(), context);
		context.put("isExitPlace", partnerApplyDto.getHasLocation());
		// �����Ƿ񸲸�
		context.put("isExistNetwork", partnerApplyDto.getNetworkCoverage());
		// ��Ӫ�����
		context.put("manageArea", partnerApplyDto.getLocationArea());

		// �־�ס��
		context.put("manageAddress", partnerApplyDto.getAddressDetail());
		context.put("manageAddressCode", partnerApplyDto.getAddressDetailCode());
		context.put("manageAddressExt", partnerApplyDto.getAddressDetailName());
		context.put("manageVillageCode", partnerApplyDto.getAddressVillageCode());
		context.put("manageVillageDetail", partnerApplyDto.getAddressVillageDetail());

		// Ӫҵ��ַcode(ʡ������)
		String showStationVilliage = Boolean.TRUE.toString();
		if (StringUtil.isNotEmpty(partnerApplyDto.getStationAddressDetailCode())) {
			String[] details = partnerApplyDto.getStationAddressDetailCode().split("\\|");
			if (details.length > 0)
				context.put("province", details[0]);
			if (details.length > 1)
				context.put("city", details[1]);
			if (details.length > 2)
				context.put("county", details[2]);
			if (details.length > 3) {
				String townCode = details[3];
				if (StringUtil.isNotBlank(townCode)) {
					context.put("town", townCode);
					try {
						Collection<VillageVO> villages = divisionManagerService
								.getChildrenByTownId(Long.valueOf(townCode));
						// Ӫҵ��ַ���б�
						context.put("stationVillageList", villages);
						if (CollectionUtils.isEmpty(villages)) {
							showStationVilliage = Boolean.FALSE.toString();
						} else {
							showStationVilliage = Boolean.TRUE.toString();
						}
					} catch (Exception e) {
						logger.error("�����ȡ���б�ʧ��, townCode : " + townCode);
					}
				}
			} else {
				showStationVilliage = Boolean.FALSE.toString();
			}
		} else {
			showStationVilliage = Boolean.FALSE.toString();
		}
		context.put("showStationVilliage", showStationVilliage);
		// Ӫҵ��ַ(ʡ������)
		if (StringUtil.isNotEmpty(partnerApplyDto.getStationAddressDetail())) {
			String[] details = partnerApplyDto.getStationAddressDetail().split("\\|");
			if (details.length > 0)
				context.put("provinceDetail", details[0]);
			if (details.length > 1)
				context.put("cityDetail", details[1]);
			if (details.length > 2)
				context.put("countyDetail", details[2]);
			if (details.length > 3)
				context.put("townDetail", details[3]);
		}

		// �־�ס��ַcode(ʡ������)
		String showManageVilliage = Boolean.TRUE.toString();
		if (StringUtil.isNotEmpty(partnerApplyDto.getAddressDetailCode())) {
			String[] details = partnerApplyDto.getAddressDetailCode().split("\\|");
			if (details.length > 0)
				context.put("manageProvince", details[0]);
			if (details.length > 1)
				context.put("manageCity", details[1]);
			if (details.length > 2)
				context.put("manageCounty", details[2]);
			if (details.length > 3) {
				String townCode = details[3];
				context.put("manageTown", townCode);
				try {
					Collection<VillageVO> villages = divisionManagerService.getChildrenByTownId(Long.valueOf(townCode));
					// �־�ס��ַ���б�
					context.put("manageVillageList", villages);
					if (CollectionUtils.isEmpty(villages)) {
						showManageVilliage = Boolean.FALSE.toString();
					}
				} catch (Exception e) {
					logger.error("�����ȡ�־�ס��ַ���б�ʧ��, townCode : " + townCode);
				}
			} else {
				showManageVilliage = Boolean.FALSE.toString();
			}
		} else {
			showManageVilliage = Boolean.FALSE.toString();
		}
		context.put("showManageVilliage", showManageVilliage);

		if (StringUtil.isNotEmpty(partnerApplyDto.getAddressDetailName())) {
			String[] details = partnerApplyDto.getAddressDetailName().split("\\|");
			if (details.length > 0)
				context.put("manageProvinceDetail", details[0]);
			if (details.length > 1)
				context.put("manageCityDetail", details[1]);
			if (details.length > 2)
				context.put("manageCountyDetail", details[2]);
			if (details.length > 3)
				context.put("manageTownDetail", details[3]);
		}

		context.put("partnerApplyState", partnerApplyDto.getPartnerApplyStateEnum().getCode());
		context.put("stationAddressDetailExt", partnerApplyDto.getStationAddressDetailExt());
		context.put("email", partnerApplyDto.getEmail());
		// --- ����
		context.put("nation", partnerApplyDto.getNation());
		// ���䷶Χ
		context.put("ageRange", partnerApplyDto.getAgeRange());
		// �˿ڷ�Χ
		context.put("population", partnerApplyDto.getPopulation());
		// ��ӪͼƬ��ַ
		context.put("locationImgs", JSON.toJSONString(getAttachmentInfo(partnerApplyDto.getLocationImgs(), true)));
		// ��Ӫ�ƻ���
		context.put("busProposal", JSON.toJSONString(getAttachmentInfo(partnerApplyDto.getBusProposal(), false)));
		// ��������
		context.put("workExp", partnerApplyDto.getWorkExp());
		// �������Χ
		context.put("areaRange", partnerApplyDto.getAreaRange());
		// �˾����뷶Χ
		context.put("perIncomeRange", partnerApplyDto.getPerIncomeRange());
		// ��ȡ����
		if (StringUtils.isNotEmpty(partnerApplyDto.getFromChannels())) {
			String[] codes = partnerApplyDto.getFromChannels().split("\\|");
			List<Map<String, Object>> channels = PartnerApplyFromChannelEnum.getModifiableRecords();
			for (String cd : codes) {
				boolean isEm = false;
				for (Map<String, Object> em : channels) {
					String code = (String) em.get("code");
					if (code.equals(cd)) {
						em.put("checked", isEm = true);
						isEm = true;
						break;
					}
				}
				if (!isEm) {
					context.put("otherChannel", cd);
				}
			}
			context.put("fromChannelList", channels);

		}
		// ����ƽ̨����
		if (StringUtils.isNotEmpty(partnerApplyDto.getNetPlatformExp())) {
			String[] codes = partnerApplyDto.getNetPlatformExp().split("\\|");
			List<Map<String, Object>> netplats = PartnerApplyNetPlatEnum.getModifiableRecords();
			for (String cd : codes) {
				boolean isEm = false;
				for (Map<String, Object> em : netplats) {
					String code = (String) em.get("code");
					if (code.equals(cd)) {
						em.put("checked", isEm = true);
						break;
					}
				}
				if (!isEm) {
					context.put("otherPlat", cd);
				}
			}
			context.put("netPlatList", netplats);

		}
		// ������ò
		context.put("politics", partnerApplyDto.getPolitics());
		// �������״̬��Χ�ϻ�����Ϣ�Ƿ�����༭(���ڳ�����ߴ�����״̬������༭)

		context.put("infoLevel", partnerApplyDto.getInfoLevel());
	}

	/**
	 * ����fids��ȡ������Ϣ
	 */
	private List<Map<String, Object>> getAttachmentInfo(String fids, boolean isImg) {
		if (StringUtils.isEmpty(fids))
			return null;
		String[] fidAry = fids.split("\\|");
		List<Map<String, Object>> attachements = new ArrayList<Map<String, Object>>();
		for (String fid : fidAry) {
			Long id = Longs.tryParse(fid);
			if (null != id) {
				com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement> result = attachmentService
						.getAttachement(id);
				if (null != result && result.isSuccess() && null != result.getResult()) {
					Attachement attachement = result.getResult();
					FileUploadVo vo = new FileUploadVo();
					vo.setAttachementId(attachement.getId());
					vo.setFileType(attachement.getFileType());
					vo.setFsId(attachement.getFsId());
					vo.setTitle(attachement.getTitle());
					Map<String, Object> map = new HashMap<String, Object>();
					if (isImg) {
						map.put("url", vo.getUrl());
					} else {
						map.put("url", vo.getDownloadUrl());
					}
					map.put("attachementId", attachement.getId());
					map.put("fsId", vo.getFsId());
					map.put("title", vo.getTitle());
					map.put("fileType", vo.getFileType());
					map.put("desc", vo.getTitle());
					map.put("name", vo.getTitle());
					attachements.add(map);
				}
			}
		}
		return attachements;
	}

	/**
	 * ����PartnerApply id��ȡ���֤��Ϣ
	 */
	private List<Map<String, String>> getIdCardImgs(Long objId, Context context) {

		final String FACE_IMG_TYPE = "idCardImgsFace"; // context key
		final String BACK_IMG_TYPE = "idCardImgsBack"; // context key
		final String JOINT_IMG_TYPE = "idCardImgsBackJoint"; // context key

		List<Map<String, String>> attachements = new ArrayList<Map<String, String>>();
		if (objId == null || objId < 1) {
			return attachements;
		}
		com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement> faceImgRm = attachmentService
				.getAttachmentByObjectId(objId, ViewConstants.IDCARD_IMGS_FACE_BIZTYPE);
		com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement> backImgRm = attachmentService
				.getAttachmentByObjectId(objId, ViewConstants.IDCARD_IMGS_BACK_BIZTYPE);
		com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement> jointImgRm = attachmentService
				.getAttachmentByObjectId(objId, ViewConstants.IDCARD_JOINTIMG_BIZTYPE);
		putAttachments(context, faceImgRm, FACE_IMG_TYPE);
		putAttachments(context, backImgRm, BACK_IMG_TYPE);
		putAttachments(context, jointImgRm, JOINT_IMG_TYPE);

		return attachements;
	}

	private void putAttachments(Context context,
			com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement> idCardImgRm, String imgType) {
		if (null != idCardImgRm && idCardImgRm.isSuccess() && null != idCardImgRm.getResult()) {
			Attachement att = idCardImgRm.getResult();
			FileUploadVo vo = new FileUploadVo();
			vo.setAttachementId(att.getId());
			vo.setFileType(att.getFileType());
			vo.setFsId(att.getFsId());
			vo.setTitle(att.getTitle());
			Map<String, String> map = new HashMap<String, String>();
			map.put("url", vo.getUrl());
			map.put("attachementId", String.valueOf(att.getId()));
			map.put("fsId", vo.getFsId());
			map.put("title", vo.getTitle());
			map.put("fileType", vo.getFileType());
			map.put("desc", vo.getTitle());
			map.put("name", vo.getTitle());
			// ��ʾ���棬���棬�ϳ���
			map.put("imgType", imgType);
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			list.add(map);
			context.put(imgType, JSON.toJSONString(list));
		}

	}
}
