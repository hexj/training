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
 * 合伙人申请表单
 * 
 * @author xiaoxi.bxx 2015-8-21 下午2:32:59
 */
public class PartnerApply {

    public static final Logger logger = LoggerFactory.getLogger(PartnerApply.class);

    public void execute(TurbineRunData rundata, Context context, Navigator navigator) {
        // 学历列表
        context.put("educationList", PartnerApplyEducationEnum.getUnmodifiableRecords());
        // 政治面貌列表
        context.put("politicsList", PartnerApplyPoliticsEnum.getUnmodifiableRecords());
        // 渠道列表
        context.put("fromChannelList", PartnerApplyFromChannelEnum.getUnmodifiableRecords());
        // 村面积列表
        context.put("areaRangeList", PartnerApplyVillageAreaEnum.getUnmodifiableRecords());
        // 村人口列表
        context.put("populationList", PartnerApplyVillagePopulationEnum.getUnmodifiableRecords());
        // 村人均收入列表
        context.put("perIncomeRangeList", PartnerApplyVillagePerIncomeEnum.getUnmodifiableRecords());
        // 村民主要年龄列表
        context.put("ageRangeList", PartnerApplyVillageAgeEnum.getUnmodifiableRecords());
        // 电子商务平台列表
        context.put("netPlatList", PartnerApplyNetPlatEnum.getUnmodifiableRecords());
    }

}
