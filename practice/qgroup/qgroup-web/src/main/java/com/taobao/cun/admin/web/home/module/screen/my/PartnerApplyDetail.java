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
        // 只有报名的人才可以访问该页面，未报名和已经录入村点的人，不可以进去
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
                context.put("gender", "男");
            } else {
                context.put("gender", "女");
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

            // 审核意见
            if (null != partnerApplyDto.getAuditOpinion()) {
                context.put("remark", partnerApplyDto.getAuditOpinion());
            }
            context.put("mobile", partnerApplyDto.getPhone());
            context.put("email", partnerApplyDto.getEmail());

            context.put("status", partnerApplyDto.getPartnerApplyStateEnum().getCode());

            // 审核拒绝，显示审核详情
            if (PartnerApplyStateEnum.STATE_APPLY_REFUSE.equals(partnerApplyDto.getPartnerApplyStateEnum())) {
                context.put("statusInfo", "审核详情");
            } else {
                context.put("statusInfo", partnerApplyDto.getPartnerApplyStateEnum().getDesc());
            }

            if ("y".equals(partnerApplyDto.getShoppingOnline())) {
                context.put("onlineShopping", "有");
            } else if ("n".equals(partnerApplyDto.getShoppingOnline())) {
                context.put("onlineShopping", "无");
            }

            if ("y".equals(partnerApplyDto.getKnowTaobao())) {
                context.put("isKnowTaobao", "是");
            } else if ("n".equals(partnerApplyDto.getKnowTaobao())) {
                context.put("isKnowTaobao", "否");
            }

            // --- 学历
            context.put("education", PartnerApplyEducationEnum.getNameByCode(partnerApplyDto.getEducation()));
            // --- 名族
            context.put("nation", partnerApplyDto.getNation() != null ? partnerApplyDto.getNation() : "");
            // 年龄范围
            context.put("ageRange", PartnerApplyVillageAgeEnum.getNameByCode(partnerApplyDto.getAgeRange()));
            // 人口范围
            context.put("population", PartnerApplyVillagePopulationEnum.getNameByCode(partnerApplyDto.getPopulation()));
            // 经营图片地址
            context.put("locationImgs", getAttachmentInfo(partnerApplyDto.getLocationImgs()));
            // 经营计划书
            context.put("busProposal", getAttachmentInfo(partnerApplyDto.getBusProposal()));
            // 工作经历
            context.put("workExp", partnerApplyDto.getWorkExp());
            // 存面积范围
            context.put("areaRange", PartnerApplyVillageAreaEnum.getNameByCode(partnerApplyDto.getAreaRange()));
            // 人均收入范围
            context.put("perIncomeRange",
                        PartnerApplyVillagePerIncomeEnum.getNameByCode(partnerApplyDto.getPerIncomeRange()));
            // 获取渠道
            if (StringUtils.isNotEmpty(partnerApplyDto.getFromChannels())) {
                String[] codes = partnerApplyDto.getFromChannels().split("\\|");
                List<String> codeNames = new ArrayList<String>();
                for (String code : codes) {
                    String codeName = PartnerApplyFromChannelEnum.getNameByCode(code);
                    if (StringUtils.isNotEmpty(codeName)) {
                        codeNames.add(codeName);
                    } else { // 其他数据
                        codeNames.add(code);
                    }
                }
                context.put("fromChannels", codeNames);
            }
            // 网络平台经验
            if (StringUtils.isNotEmpty(partnerApplyDto.getNetPlatformExp())) {
                String[] codes = partnerApplyDto.getNetPlatformExp().split("\\|");
                List<String> codeNames = new ArrayList<String>();
                for (String code : codes) {
                    String codeName = PartnerApplyNetPlatEnum.getNameByCode(code);
                    if (StringUtils.isNotEmpty(codeName)) {
                        codeNames.add(codeName);
                    } else { // 其他数据
                        codeNames.add(code);
                    }
                }
                context.put("netPlatformExp", codeNames);
            }
            // 政治面貌
            context.put("politics", PartnerApplyPoliticsEnum.getNameByCode(partnerApplyDto.getPolitics()));
        }
    }

    /**
     * 根据fids获取附件信息
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

        // 已经录入村点的，不能再报名,因为录入村点的，可能在报名的表中也不存在，所以要先判断
        ResultModel<Boolean> resultModel = stationApplyQueryService.isExistsStationApplyByTaobaoUserId(taobaoId,
                                                                                                       contextDto);
        if (null != resultModel && resultModel.isSuccess() && resultModel.getResult()) {
            context.put("repeatApply", "alreadyBePartner");
            return false;
        }

        PartnerApplyDto existPartner = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, contextDto);

        // 未报名
        if (null == existPartner) {
            context.put("repeatApply", "notApply");
            return false;
        }
        return true;
    }
}
