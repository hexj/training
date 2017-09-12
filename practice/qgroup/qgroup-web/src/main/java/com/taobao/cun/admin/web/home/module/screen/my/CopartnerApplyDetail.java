package com.taobao.cun.admin.web.home.module.screen.my;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.common.lang.StringUtil;
import com.google.common.primitives.Longs;
import com.taobao.cun.admin.adapter.cuntao.center.PartnerServiceAdapter;
import com.taobao.cun.admin.web.constants.ViewConstants;
import com.taobao.cun.admin.web.partner.module.util.PartnerUtil;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.admin.web.vo.FileUploadVo;
import com.taobao.cun.common.resultmodel.ResultModel;
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
import com.taobao.cun.service.station.StationApplyQueryService;
import com.taobao.security.util.SensitiveDataUtil;

/**
 * �ϻ��������
 * 
 */
public class CopartnerApplyDetail {

	public static final Logger logger = LoggerFactory.getLogger(CopartnerApplyDetail.class);

	@Autowired
	protected HttpSession session;

	@Autowired
	private PartnerServiceAdapter partnerServiceAdapter;

	@Autowired
	private StationApplyQueryService stationApplyQueryService;

	@Autowired
	private AttachementService attachmentService;

	private final String splitChar = " ";

	public void execute(TurbineRunData rundata, Context context, Navigator navigator) {

		// ֻ�б������˲ſ��Է��ʸ�ҳ�棬δ�������Ѿ�¼������ˣ������Խ�ȥ
		boolean result = validateNotApplyUser(context, rundata);
		if (!result) {
			return;
		}

		Long taobaoId = WebUtil.getUserId(session);
		ContextDto contextDto = PartnerUtil.assemberContext(taobaoId);
		PartnerApplyDto partnerApplyDto = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, contextDto);
		if (null != partnerApplyDto && partnerApplyDto.getId() > 0) {
			loadDetailData(partnerApplyDto, context);
		}

	}

	private void loadDetailData(PartnerApplyDto partnerApplyDto, Context context) {
		context.put("id", partnerApplyDto.getId());
		context.put("applyName", partnerApplyDto.getName());
		context.put("idCardNum", SensitiveDataUtil.idCardNoHide(partnerApplyDto.getIdenNum()));
		context.put("alipayAccount", SensitiveDataUtil.alipayLogonIdHide(partnerApplyDto.getAlipayAccount()));
		context.put("manageAddress", getManageAddress(partnerApplyDto));
		context.put("gender", partnerApplyDto.getGender());

		context.put("isExitPlace", partnerApplyDto.getHasLocation());
		// �����Ƿ񸲸�
		context.put("isExistNetwork", partnerApplyDto.getNetworkCoverage());

		context.put("manageArea", partnerApplyDto.getLocationArea());

		if (StringUtil.isNotEmpty(partnerApplyDto.getStationAddressDetailCode())) {
			String[] details = partnerApplyDto.getStationAddressDetailCode().split(splitChar);
			if (details.length > 0)
				context.put("province", details[0]);
			if (details.length > 1)
				context.put("city", details[1]);
			if (details.length > 2)
				context.put("county", details[2]);
			if (details.length > 3)
				context.put("town", details[3]);
		}
		if (StringUtil.isNotEmpty(partnerApplyDto.getStationAddressDetail())) {
			String[] details = partnerApplyDto.getStationAddressDetail().split(splitChar);
			if (details.length > 0)
				context.put("provinceDetail", details[0]);
			if (details.length > 1)
				context.put("cityDetail", details[1]);
			if (details.length > 2)
				context.put("countyDetail", details[2]);
			if (details.length > 3)
				context.put("townDetail", details[3]);
		}
		context.put("stationAddress", getStationAddress(partnerApplyDto));
		context.put("partnerApplyState", partnerApplyDto.getPartnerApplyStateEnum().getCode());
		context.put("stationAddressDetailExt", partnerApplyDto.getStationAddressDetailExt());
		context.put("mobile", partnerApplyDto.getPhone());
		context.put("email", partnerApplyDto.getEmail());
		// --- ѧ��
		context.put("education", PartnerApplyEducationEnum.getNameByCode(partnerApplyDto.getEducation()));
		// --- ����
		context.put("nation", partnerApplyDto.getNation());
		// ���䷶Χ
		context.put("ageRange", PartnerApplyVillageAgeEnum.getNameByCode(partnerApplyDto.getAgeRange()));
		// �˿ڷ�Χ
		context.put("population", PartnerApplyVillagePopulationEnum.getNameByCode(partnerApplyDto.getPopulation()));
		// ��ӪͼƬ��ַ
		context.put("locationImgs", getAttachmentInfo(partnerApplyDto.getLocationImgs(), true));
		// ��Ӫ�ƻ���
		context.put("busProposal", getAttachmentInfo(partnerApplyDto.getBusProposal(), false));
		// ���֤��Ƭ
		context.put("idCardImgs", getIdCardImgs(partnerApplyDto.getId()));
		// ��������
		context.put("workExp", partnerApplyDto.getWorkExp());
		// �������Χ
		context.put("areaRange", PartnerApplyVillageAreaEnum.getNameByCode(partnerApplyDto.getAreaRange()));
		// �˾����뷶Χ
		context.put("perIncomeRange",
				PartnerApplyVillagePerIncomeEnum.getNameByCode(partnerApplyDto.getPerIncomeRange()));
		// ��ȡ����
		if (StringUtils.isNotEmpty(partnerApplyDto.getFromChannels())) {
			String[] codes = partnerApplyDto.getFromChannels().split("\\|");
			List<String> codeNames = new ArrayList<String>();
			for (String code : codes) {
				String codeName = PartnerApplyFromChannelEnum.getNameByCode(code);
				if (StringUtils.isNotEmpty(codeName)) {
					codeNames.add(codeName);
				} else { // ��������
					codeNames.add(code);
				}
			}
			context.put("fromChannels", codeNames);
		}
		// ����ƽ̨����
		if (StringUtils.isNotEmpty(partnerApplyDto.getNetPlatformExp())) {
			String[] codes = partnerApplyDto.getNetPlatformExp().split("\\|");
			List<String> codeNames = new ArrayList<String>();
			for (String code : codes) {
				String codeName = PartnerApplyNetPlatEnum.getNameByCode(code);
				if (StringUtils.isNotEmpty(codeName)) {
					codeNames.add(codeName);
				} else { // ��������
					codeNames.add(code);
				}
			}
			context.put("netPlatformExp", codeNames);
		}
		// ������ò
		context.put("politics", PartnerApplyPoliticsEnum.getNameByCode(partnerApplyDto.getPolitics()));
		// �������״̬��Χ�ϻ�����Ϣ�Ƿ�����༭
		PartnerApplyStateEnum state = partnerApplyDto.getPartnerApplyStateEnum();
		context.put("auditStateDesc", state.getDesc());
		context.put("auditStateCode", state.getCode());
		if (PartnerApplyStateEnum.STATE_APPLY_REFUSE.equals(state)
				|| PartnerApplyStateEnum.STATE_APPLY_REFUSE_INTERVIEW.equals(state)) {
			context.put("refuseReason", partnerApplyDto.getAuditOpinion());
		}
		context.put("infoLevel", partnerApplyDto.getInfoLevel());
	}

	private Object getManageAddress(PartnerApplyDto partnerApplyDto) {
		StringBuffer sb = new StringBuffer();
		if (StringUtil.isNotBlank(partnerApplyDto.getAddressDetailName())) {
			sb.append(partnerApplyDto.getAddressDetailName()); // �־�ס��ַ����
		}
		if (StringUtil.isNotBlank(partnerApplyDto.getAddressVillageDetail())) { // �־�ס������������
			sb.append(splitChar);
			sb.append(partnerApplyDto.getAddressVillageDetail());
		}
		if (StringUtil.isNotBlank(partnerApplyDto.getAddressDetail())) {// �־�ס��ַ����
			sb.append(splitChar);
			sb.append(partnerApplyDto.getAddressDetail());
		}
		return sb.toString();
	}

	private Object getStationAddress(PartnerApplyDto partnerApplyDto) { // �ƻ�Ӫҵ��
		StringBuffer sb = new StringBuffer();
		if (StringUtil.isNotBlank(partnerApplyDto.getStationAddressDetail())) { // Ӫҵ��ַ(ʡ������)
			sb.append(partnerApplyDto.getStationAddressDetail());
		}
		if (StringUtil.isNotBlank(partnerApplyDto.getStationVillageDetail())) {// Ӫҵ��ַ*��*����
			sb.append(splitChar);
			sb.append(partnerApplyDto.getStationVillageDetail());
		}
		if (StringUtil.isNotBlank(partnerApplyDto.getStationAddressDetailExt())) { // Ӫҵ��ַ����
			sb.append(splitChar);
			sb.append(partnerApplyDto.getStationAddressDetailExt());
		}
		return sb.toString();
	}

	/** ��֤��ǰ�û��Ƿ��Ѿ����� */
	private boolean validateNotApplyUser(Context context, TurbineRunData rundata) {
		Long taobaoId = WebUtil.getUserId(session);

		ContextDto contextDto = PartnerUtil.assemberContext(taobaoId);

		// �Ѿ�¼����ģ������ٱ���,��Ϊ¼����ģ������ڱ����ı���Ҳ�����ڣ�����Ҫ���ж�
		ResultModel<Boolean> resultModel = stationApplyQueryService.isExistsStationApplyByTaobaoUserId(taobaoId,
				contextDto);
		if (null != resultModel && resultModel.isSuccess() && resultModel.getResult()) {
			context.put("repeatApply", ViewConstants.REPEAT_APPLY_BE_PARTNER);
			return false;
		}

		PartnerApplyDto existPartner = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, contextDto);

		// δ����
		if (null == existPartner) {
			// context.put("repeatApply", ViewConstants.REPEAT_APPLY_NOT_APPLY);
			rundata.setRedirectLocation("/my/copartnerApplyStation.htm");
			return false;
		}
		return true;
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
	private List<Map<String, String>> getIdCardImgs(Long objId) {

		List<Map<String, String>> attachements = new ArrayList<Map<String, String>>();
		if (objId == null || objId < 1) {
			return attachements;
		}
		com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement> faceImgRm = attachmentService
				.getAttachmentByObjectId(objId, ViewConstants.IDCARD_IMGS_FACE_BIZTYPE);
		com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement> backImgRm = attachmentService
				.getAttachmentByObjectId(objId, ViewConstants.IDCARD_IMGS_BACK_BIZTYPE);
		// com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement>
		// jointImgRm = attachmentService
		// .getAttachmentByObjectId(objId,
		// ViewConstants.IDCARD_JOINTIMG_BIZTYPE);
		putAttachments(attachements, faceImgRm, ViewConstants.FACE_IMG_TYPE);
		putAttachments(attachements, backImgRm, ViewConstants.BACK_IMG_TYPE);
		// putAttachments(attachements, jointImgRm,
		// ViewConstants.JOINT_IMG_TYPE);
		return attachements;
	}

	private void putAttachments(List<Map<String, String>> attachements,
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
			attachements.add(map);
		}

	}
}
