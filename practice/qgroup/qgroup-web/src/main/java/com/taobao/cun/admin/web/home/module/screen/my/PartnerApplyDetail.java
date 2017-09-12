package com.taobao.cun.admin.web.home.module.screen.my;

import java.util.ArrayList;
import java.util.List;

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

public class PartnerApplyDetail {

    public static final Logger       logger = LoggerFactory.getLogger(PartnerApplyDetail.class);

    @Autowired
    private HttpSession              session;

    @Autowired
    private PartnerServiceAdapter    partnerServiceAdapter;

    @Autowired
    private StationApplyQueryService stationApplyQueryService;

    @Autowired
    private AttachementService       attachmentService;

    public void execute(TurbineRunData rundata, Context context, Navigator navigator) {
        // ֻ�б������˲ſ��Է��ʸ�ҳ�棬δ�������Ѿ�¼������ˣ������Խ�ȥ
        boolean result = validateNotApplyUser(context);
        if (!result) {
            return;
        }

        Long taobaoId = WebUtil.getUserId(session);
        ContextDto contextDto = PartnerUtil.assemberContext(taobaoId);
        PartnerApplyDto partnerApplyDto = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, contextDto);

        if (null != partnerApplyDto) {
            context.put("applyName", partnerApplyDto.getName());
            context.put("idCardNum", SensitiveDataUtil.idCardNoHide(partnerApplyDto.getIdenNum()));
            context.put("alipayAccount", SensitiveDataUtil.alipayLogonIdHide(partnerApplyDto.getAlipayAccount()));
            context.put("manageAddress", partnerApplyDto.getAddressDetail());

            if ("M".equals(partnerApplyDto.getGender())) {
                context.put("gender", "��");
            } else {
                context.put("gender", "Ů");
            }

            if ("y".equals(partnerApplyDto.getHasLocation())) {
                context.put("isExitPlace", "y");
                context.put("manageArea", partnerApplyDto.getLocationArea());
            } else {
                context.put("isExitPlace", "n");
            }

            String addressDetail = "";
            if (StringUtil.isNotEmpty(partnerApplyDto.getStationAddressDetail())) {
                addressDetail = partnerApplyDto.getStationAddressDetail().replace("|", "/");
            }
            if (StringUtil.isNotEmpty(partnerApplyDto.getStationAddressDetailExt())) {
                if (StringUtil.isNotEmpty(addressDetail)) {
                    addressDetail = addressDetail + "/" + partnerApplyDto.getStationAddressDetailExt();
                } else {
                    addressDetail = partnerApplyDto.getStationAddressDetailExt();
                }
            }
            if (StringUtil.isNotEmpty(addressDetail)) {
                context.put("addressDetail", addressDetail);
            }

            // ������
            if (null != partnerApplyDto.getAuditOpinion()) {
                context.put("remark", partnerApplyDto.getAuditOpinion());
            }
            context.put("mobile", partnerApplyDto.getPhone());
            context.put("email", partnerApplyDto.getEmail());

            context.put("status", partnerApplyDto.getPartnerApplyStateEnum().getCode());

            // ��˾ܾ�����ʾ�������
            if (PartnerApplyStateEnum.STATE_APPLY_REFUSE.equals(partnerApplyDto.getPartnerApplyStateEnum())) {
                context.put("statusInfo", "�������");
            } else {
                context.put("statusInfo", partnerApplyDto.getPartnerApplyStateEnum().getDesc());
            }

            if ("y".equals(partnerApplyDto.getShoppingOnline())) {
                context.put("onlineShopping", "��");
            } else if ("n".equals(partnerApplyDto.getShoppingOnline())) {
                context.put("onlineShopping", "��");
            }

            if ("y".equals(partnerApplyDto.getKnowTaobao())) {
                context.put("isKnowTaobao", "��");
            } else if ("n".equals(partnerApplyDto.getKnowTaobao())) {
                context.put("isKnowTaobao", "��");
            }

            // --- ѧ��
            context.put("education", PartnerApplyEducationEnum.getNameByCode(partnerApplyDto.getEducation()));
            // --- ����
            context.put("nation", partnerApplyDto.getNation() != null ? partnerApplyDto.getNation() : "");
            // ���䷶Χ
            context.put("ageRange", PartnerApplyVillageAgeEnum.getNameByCode(partnerApplyDto.getAgeRange()));
            // �˿ڷ�Χ
            context.put("population", PartnerApplyVillagePopulationEnum.getNameByCode(partnerApplyDto.getPopulation()));
            // ��ӪͼƬ��ַ
            context.put("locationImgs", getAttachmentInfo(partnerApplyDto.getLocationImgs()));
            // ��Ӫ�ƻ���
            context.put("busProposal", getAttachmentInfo(partnerApplyDto.getBusProposal()));
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
        }
    }

    /**
     * ����fids��ȡ������Ϣ
     */
    private List<FileUploadVo> getAttachmentInfo(String fids) {
        if (StringUtils.isEmpty(fids)) return null;
        String[] fidAry = fids.split("\\|");
        List<FileUploadVo> attachements = new ArrayList<FileUploadVo>();
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
                    attachements.add(vo);
                }
            }
        }
        return attachements;
    }

    private boolean validateNotApplyUser(Context context) {
        Long taobaoId = WebUtil.getUserId(session);

        ContextDto contextDto = PartnerUtil.assemberContext(taobaoId);

        // �Ѿ�¼����ģ������ٱ���,��Ϊ¼����ģ������ڱ����ı���Ҳ�����ڣ�����Ҫ���ж�
        ResultModel<Boolean> resultModel = stationApplyQueryService.isExistsStationApplyByTaobaoUserId(taobaoId,
                                                                                                       contextDto);
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
