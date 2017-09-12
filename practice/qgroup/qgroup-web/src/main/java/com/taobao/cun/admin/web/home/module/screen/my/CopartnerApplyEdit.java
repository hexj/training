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
import com.alibaba.common.lang.ArrayUtil;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.primitives.Longs;
import com.taobao.cun.admin.adapter.cuntao.center.PartnerServiceAdapter;
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

public class CopartnerApplyEdit {

	public static final Logger logger = LoggerFactory.getLogger(CopartnerApplyEdit.class);

	@Autowired
	private HttpSession session;

	@Autowired
	private PartnerServiceAdapter partnerServiceAdapter;

	@Autowired
	private StationApplyQueryService stationApplyQueryService;
	
	@Autowired
	private AttachementService attachmentService;

	public void execute(TurbineRunData rundata, Context context, Navigator navigator) {
		// ֻ�б������˲ſ��Է��ʸ�ҳ�棬δ�������Ѿ�¼������ˣ������Խ�ȥ
		boolean result = validateNotApplyUser(context);
		if (!result) {
			return;
		}

		Long taobaoId = WebUtil.getUserId(session);
		ContextDto contextDto = PartnerUtil.assemberContext(taobaoId);
		PartnerApplyDto partnerApplyDto = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, contextDto);

		// ѧ���б�
        context.put("educationList", PartnerApplyEducationEnum.getUnmodifiableRecords());
        // ������ò�б�
        context.put("politicsList", PartnerApplyPoliticsEnum.getUnmodifiableRecords());
        // �����б�
        List<Map<String, Object>> fromChannelList = PartnerApplyFromChannelEnum.getModifiableRecords();
        context.put("fromChannelList", fromChannelList);
        // ������б�
        context.put("areaRangeList", PartnerApplyVillageAreaEnum.getUnmodifiableRecords());
        // ���˿��б�
        context.put("populationList", PartnerApplyVillagePopulationEnum.getUnmodifiableRecords());
        // ���˾������б�
        context.put("perIncomeRangeList", PartnerApplyVillagePerIncomeEnum.getUnmodifiableRecords());
        // ������Ҫ�����б�
        context.put("ageRangeList", PartnerApplyVillageAgeEnum.getUnmodifiableRecords());
        // ��������ƽ̨�б�
        List<Map<String, Object>> netPlatList = PartnerApplyNetPlatEnum.getModifiableRecords();
        context.put("netPlatList", netPlatList);
		
		if (null != partnerApplyDto) {
			context.put("id", partnerApplyDto.getId());
			context.put("applyName", partnerApplyDto.getName());
			context.put("idCardNum", SensitiveDataUtil.idCardNoHide(partnerApplyDto.getIdenNum()));
			context.put("alipayAccount", SensitiveDataUtil.alipayLogonIdHide(partnerApplyDto.getAlipayAccount()));
			context.put("manageAddress", partnerApplyDto.getAddressDetail());
			context.put("gender", partnerApplyDto.getGender());

			context.put("isExitPlace", partnerApplyDto.getHasLocation());
			context.put("manageArea", partnerApplyDto.getLocationArea());

			if (StringUtil.isNotEmpty(partnerApplyDto.getStationAddressDetailCode())) {
				String[] details = partnerApplyDto.getStationAddressDetailCode().split("\\|");
				if (details.length > 0) context.put("province", details[0]);
				if (details.length > 1) context.put("city", details[1]);
				if (details.length > 2) context.put("county", details[2]);
				if (details.length > 3) context.put("town", details[3]);
			}
			if (StringUtil.isNotEmpty(partnerApplyDto.getStationAddressDetail())) {
				String[] details = partnerApplyDto.getStationAddressDetail().split("\\|");
				if (details.length > 0) context.put("provinceDetail", details[0]);
				if (details.length > 1) context.put("cityDetail", details[1]);
				if (details.length > 2) context.put("countyDetail", details[2]);
				if (details.length > 3) context.put("townDetail", details[3]);
			}
			context.put("stationAddressDetail", partnerApplyDto.getStationAddressDetail());
			context.put("partnerApplyState", partnerApplyDto.getPartnerApplyStateEnum().getCode());
			context.put("stationAddressDetailExt", partnerApplyDto.getStationAddressDetailExt());
			context.put("mobile", partnerApplyDto.getPhone());
			context.put("email", partnerApplyDto.getEmail());
			// ---  ѧ��
			context.put("education", partnerApplyDto.getEducation());
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
				for (String cd : codes) {
					boolean isEm = false;
					for (Map<String, Object> em : fromChannelList) {
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
			}
			// ����ƽ̨����
			if (StringUtils.isNotEmpty(partnerApplyDto.getNetPlatformExp())) {
				String[] codes = partnerApplyDto.getNetPlatformExp().split("\\|");
				for (String cd : codes) {
					boolean isEm = false;
					for (Map<String, Object> em : netPlatList) {
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
					
			}
			// ������ò
			context.put("politics", partnerApplyDto.getPolitics());
			// �������״̬��Χ�ϻ�����Ϣ�Ƿ�����༭(���ڳ�����ߴ�����״̬������༭)
			context.put("canEdit", PartnerApplyStateEnum.STATE_APPLY_WAIT.equals(partnerApplyDto.getPartnerApplyStateEnum())||PartnerApplyStateEnum.STATE_APPLY_INTERVIEW.equals(partnerApplyDto.getPartnerApplyStateEnum()));
		}
	}
	
	/**
	 *	����fids��ȡ������Ϣ 
	 */
	private List<Map<String, Object>> getAttachmentInfo(String fids, boolean isImg) {
		if (StringUtils.isEmpty(fids)) return null;
		String[] fidAry = fids.split("\\|");
		List<Map<String, Object>> attachements = new ArrayList<Map<String, Object>>();
		for (String fid : fidAry) {
			Long id = Longs.tryParse(fid);
			if (null != id) {
				 com.taobao.cun.crius.common.resultmodel.ResultModel<Attachement> result = attachmentService.getAttachement(id);
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

	private boolean validateNotApplyUser(Context context) {
		Long taobaoId = WebUtil.getUserId(session);

		ContextDto contextDto = PartnerUtil.assemberContext(taobaoId);

		// �Ѿ�¼����ģ������ٱ���,��Ϊ¼����ģ������ڱ����ı���Ҳ�����ڣ�����Ҫ���ж�
		ResultModel<Boolean> resultModel = stationApplyQueryService.isExistsStationApplyByTaobaoUserId(taobaoId, contextDto);
		if (null != resultModel && resultModel.isSuccess() && resultModel.getResult()) {
			context.put("repeatApply", "alreadyBePartner");
			return false;
		}

		PartnerApplyDto existPartner = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, contextDto);

		// δ����
		if (null == existPartner) {
			context.put("repeatApply", "notApply");
			return false;
		}
		return true;
	}
}
