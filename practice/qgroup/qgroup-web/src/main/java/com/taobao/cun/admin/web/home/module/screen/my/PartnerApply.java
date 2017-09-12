package com.taobao.cun.admin.web.home.module.screen.my;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.dto.partner.PartnerApplyEducationEnum;
import com.taobao.cun.dto.partner.PartnerApplyFromChannelEnum;
import com.taobao.cun.dto.partner.PartnerApplyNetPlatEnum;
import com.taobao.cun.dto.partner.PartnerApplyPoliticsEnum;
import com.taobao.cun.dto.partner.PartnerApplyVillageAgeEnum;
import com.taobao.cun.dto.partner.PartnerApplyVillageAreaEnum;
import com.taobao.cun.dto.partner.PartnerApplyVillagePerIncomeEnum;
import com.taobao.cun.dto.partner.PartnerApplyVillagePopulationEnum;

/**
 * �ϻ��������
 * 
 * @author xiaoxi.bxx 2015-8-21 ����2:32:59
 */
public class PartnerApply {

    public static final Logger logger = LoggerFactory.getLogger(PartnerApply.class);

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
    }

}
