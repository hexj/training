package com.taobao.cun.admin.web.home.module.rpc;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.NoneResultDecrator;
import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.da.pbserver.client.dubbo.result.PBResult;
import com.alibaba.da.pbserver.server.dubbo.api.IPBHsfService;
import com.taobao.cun.admin.adapter.cuntao.center.CuntaoOrgServiceAdapter;
import com.taobao.cun.admin.adapter.cuntao.center.PartnerChartQueryServiceAdapter;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.dto.CuntaoOrgDTO;
import com.taobao.cun.admin.dto.partnerperformance.IndicatorChartDto;
import com.taobao.cun.admin.dto.partnerperformance.PartnerOrderStatusDto;
import com.taobao.cun.admin.enums.Chart.SortRanges;
import com.taobao.cun.admin.service.PartnerOrderStatusService;
import com.taobao.cun.admin.service.PartnerPerformanceService;
import com.taobao.cun.admin.util.DateUtil;
import com.taobao.cun.admin.vo.PartnerPerformanceVo;
import com.taobao.cun.admin.web.home.module.BaseController;
import com.taobao.cun.admin.web.partner.module.util.PartnerUtil;
import com.taobao.cun.admin.web.vo.MyPerformanceVo;
import com.taobao.cun.admin.web.vo.PartnerIndicatorChartVo;
import com.taobao.cun.admin.web.vo.PartnerOrderStatusVo;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.station.StationDetailDto;
import com.taobao.cun.service.station.StationQueryService;

/**
 * 合伙人业绩rpc
 * 
 * @author quanzhu.wangqz
 */
@WebResource("partner")
public class PartnerPerformanceRpc extends BaseController {

    public static final Logger              logger = LoggerFactory.getLogger(PartnerPerformanceRpc.class);

    @Autowired
    private PartnerPerformanceService       partnerPerformanceService;

    @Autowired
    private PartnerChartQueryServiceAdapter partnerChartQueryServiceAdapter;

    @Autowired
    private CuntaoOrgServiceAdapter         cuntaoOrgServiceAdapter;

    @Autowired
    private PartnerOrderStatusService       partnerOrderStatusService;

    @Autowired
    StationQueryService                     stationQueryService;

    @Autowired
    IPBHsfService                           pbService;

    @ResourceMapping(value = "/getPerformance")
    @Security(checkCSRF = true)
    public AjaxResult<MyPerformanceVo> getPartnerPerformance() {
        AjaxResult<MyPerformanceVo> res = new AjaxResult<MyPerformanceVo>();
        if (!isLogin()) {
            return AjaxResult.unSuccess("Login again please!");
        }
        try {
            // Long userId =
            // PartnerPerformanceSwitchConfig.PARTNER_PERFORMANCE_TB_USER_ID.equals("")?getUserId():Long.valueOf(PartnerPerformanceSwitchConfig.PARTNER_PERFORMANCE_TB_USER_ID);
            PartnerPerformanceVo ppfVo = partnerPerformanceService.getPartnerPerformance(getUserId());
            MyPerformanceVo myVo = buildMoneyStyle(ppfVo);
            buildTime(myVo);
            res.setSuccess(true);
            res.setData(myVo);
        } catch (Exception e) {
            logger.error("getPartnerPerformance error!, taobaoUserId: {}", getUserId(), e);
            res.setSuccess(false);
            res.setExceptionDesc("查询合伙人业绩异常");
        }
        return res;
    }

    private MyPerformanceVo buildMoneyStyle(PartnerPerformanceVo ppfVo) {
        MyPerformanceVo myVo = new MyPerformanceVo();
        myVo.setGoodPartnerLevel(changeF2Y(ppfVo.getGoodPartnerLevel()));
        myVo.setStarPartnerLevel(changeF2Y(ppfVo.getStarPartnerLevel()));
        myVo.setGmvDiff(changeF2Y(ppfVo.getGmvDiff()));
        myVo.setThisMonthOrderGmv(changeF2Y(ppfVo.getThisMonthOrderGmv()));
        myVo.setTodayOrderGmv(changeF2Y(ppfVo.getTodayOrderGmv()));
        myVo.setLevelType(ppfVo.getLevelType());
        myVo.setThisMonthOrderCount(ppfVo.getThisMonthOrderCount());
        myVo.setTodayOrderCount(ppfVo.getTodayOrderCount());
        myVo.setRankedDiff(ppfVo.getRankedDiff());
        myVo.setRankedDiff2(ppfVo.getRankedDiff2());
        myVo.setStarPartnerTop(ppfVo.getStarPartnerTop());
        myVo.setStarPartnerOrderNum(ppfVo.getStarPartnerOrderNum());
        myVo.setGoodPartnerTop(ppfVo.getGoodPartnerTop());
        myVo.setGoodPartnerOrderNum(ppfVo.getGoodPartnerOrderNum());
        return myVo;
    }

    private void buildTime(MyPerformanceVo myVo) {
        Calendar c = Calendar.getInstance();
        myVo.setCurMonthDay(getMonthDay(c));
        myVo.setCurWeek(getWeek(c.get(Calendar.DAY_OF_WEEK)));
        myVo.setCurYearMonth(getYearMonth(c));
    }

    /**
     * 将分为单位的转换为元 （除100）
     * 
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(Long amount) {
        return BigDecimal.valueOf(amount).divide(new BigDecimal(100)).toString();
    }

    private static String getMonthDay(Calendar c) {
        StringBuilder sb = new StringBuilder();
        return sb.append(c.get(Calendar.MONTH) + 1).append("月").append(c.get(Calendar.DATE)).append("日").toString();

    }

    private static String getYearMonth(Calendar c) {
        StringBuilder sb = new StringBuilder();
        return sb.append(c.get(Calendar.YEAR)).append("年").append(c.get(Calendar.MONTH) + 1).append("月").toString();

    }

    private static String getWeek(int week) {
        String x = "";
        switch (week) {
            case 1:
                x = "星期日";
                break;
            case 2:
                x = "星期一";
                break;
            case 3:
                x = "星期二";
                break;
            case 4:
                x = "星期三";
                break;
            case 5:
                x = "星期四";
                break;
            case 6:
                x = "星期五";
                break;
            case 7:
                x = "星期六";
                break;
        }
        return x;
    }

    /**
     * 查询合伙人指标排行榜
     * 
     * @author zixing.liangzx
     * @param userId taobaoUserId
     * @return
     */
    @ResourceMapping(value = "/getPartnerIndicatorChart")
    @Security(checkCSRF = true)
    @NoneResultDecrator
    public AjaxResult<PartnerIndicatorChartVo> getPartnerIndicatorChart() {
        try {
            // 登陆校验
            if (!isLogin()) {
                return AjaxResult.unSuccess("Login again please!");
            }

            // ----------- 获取国排行榜 ---------------
            IndicatorChartDto countryChart = queryChart(SortRanges.country, 50);
            if (countryChart == null) return AjaxResult.unSuccess("未查询到合伙人国家排行榜信息，请稍后重试！");

            // ------------ 获取县排行榜 -------------
            IndicatorChartDto countyChart = queryChart(SortRanges.county, 30);
            if (countyChart == null) return AjaxResult.unSuccess("未查询到合伙人县排行榜信息，请稍后重试！");

            // 未参加排行
            if (notParticipateInRanking(countyChart)) {
                return AjaxResult.success(PartnerIndicatorChartVo.notInChart(countryChart, countyChart));
            }

            return AjaxResult.success(PartnerIndicatorChartVo.inChart(countryChart, countyChart));
        } catch (Exception e) {
            logger.error("Qery partnerIndicatorChart error!, taobaoUserId: {}", getUserId(), e);
            return AjaxResult.unSuccess(e.getMessage());
        }
    }

    private IndicatorChartDto queryChart(SortRanges sortRange, int size) {

        IndicatorChartDto result = partnerChartQueryServiceAdapter.queryThisMonthGMVChartBy(getUserId(), sortRange, size);

        setSortRangeValues(result);

        return result;
    }

    private void setSortRangeValues(IndicatorChartDto indicatorChart) {
        if (indicatorChart == null) return;

        if (SortRanges.country.equals(indicatorChart.getSortRange())) indicatorChart.setSortRangeValue("全国所有村服务点");

        if (SortRanges.county.equals(indicatorChart.getSortRange())) indicatorChart.setSortRangeValue(queryOrgNameBy(indicatorChart.getCountyId(), indicatorChart.getTaobaoUserId()));

    }

    private String queryOrgNameBy(Long orgId, Long taobaoUserId) {
        CuntaoOrgDTO cuntaoOrg = cuntaoOrgServiceAdapter.queryBy(orgId, taobaoUserId);

        if (cuntaoOrg == null || StringUtil.isBlank(cuntaoOrg.getName())) {
            logger.error("Cannot find cuntaoOrg by this taobaoUserId: " + taobaoUserId + ", orgId : " + orgId);
            throw new RuntimeException("无法获取合伙人组织信息！请稍后重试！");
        }

        return cuntaoOrg.getName();
    }

    /**
     * 合伙人未参与排行
     * 
     * @param countryChart
     * @return
     */
    private boolean notParticipateInRanking(IndicatorChartDto indicatorChartDto) {
        return indicatorChartDto.getSortIndex() <= 0;
    }

    /**
     * 查询合伙人指标排行榜
     * 
     * @param userId taobaoUserId
     * @return
     */
    @ResourceMapping(value = "/getPartnerOrderStatus")
    @Security(checkCSRF = true)
    @NoneResultDecrator
    public AjaxResult<PartnerOrderStatusVo> getPartnerOrderStatus() {
        if (!isLogin()) {
            return AjaxResult.unSuccess("Login again please!");
        }
        try {
            PartnerOrderStatusDto partnerOrderStatusDto = partnerOrderStatusService.getPartnerOrderStatus(getUserId());
            PartnerOrderStatusVo partnerOrderStatusVo = buildPartnerOrderStatusVo(partnerOrderStatusDto);
            return AjaxResult.success(partnerOrderStatusVo);
        } catch (Exception e) {
            logger.error("Query getPartnerOrderStatus error!, taobaoUserId: {}", getUserId(), e);
            return AjaxResult.unSuccess(e.getMessage());
        }
    }

    private PartnerOrderStatusVo buildPartnerOrderStatusVo(PartnerOrderStatusDto partnerOrderStatusDto) {
        PartnerOrderStatusVo partnerOrderStatusVo = new PartnerOrderStatusVo();
        partnerOrderStatusVo.setPicUrl(partnerOrderStatusDto.getPicUrl());
        partnerOrderStatusVo.setNickName(partnerOrderStatusDto.getNickName());
        partnerOrderStatusVo.setCunName(partnerOrderStatusDto.getCunName());
        partnerOrderStatusVo.setNotPayCount(partnerOrderStatusDto.getNotPayCount());
        partnerOrderStatusVo.setNotPayUrl(partnerOrderStatusDto.getNotPayUrl());
        partnerOrderStatusVo.setUnConsignedCount(partnerOrderStatusDto.getUnConsignedCount());
        partnerOrderStatusVo.setUnConsignedUrl(partnerOrderStatusDto.getUnConsignedUrl());
        partnerOrderStatusVo.setConsignedCount(partnerOrderStatusDto.getConsignedCount());
        partnerOrderStatusVo.setConsignedUrl(partnerOrderStatusDto.getConsignedUrl());
        partnerOrderStatusVo.setNotRateCount(partnerOrderStatusDto.getNotRateCount());
        partnerOrderStatusVo.setNotRateUrl(partnerOrderStatusDto.getNotRateUrl());
        partnerOrderStatusVo.setRefundCount(partnerOrderStatusDto.getRefundCount());
        partnerOrderStatusVo.setRefundUrl(partnerOrderStatusDto.getRefundUrl());
        partnerOrderStatusVo.setMyTaobaoOrderUrl(partnerOrderStatusDto.getMyTaobaoOrderUrl());
        return partnerOrderStatusVo;
    }

    /**
     * 查询合伙人军师数据
     * 
     * @author xiaoxi.bxx
     * @param userId taobaoUserId
     * @return
     */
    @ResourceMapping(value = "/getPartnerAdviserData")
    @Security(checkCSRF = true)
    @NoneResultDecrator
    public AjaxResult<Map<String, Object>> getPartnerAdviserData() {
        try {
            // 登陆校验
            if (!isLogin()) {
                return AjaxResult.unSuccess("Login again please!");
            }
            // 获取合伙人信息
            ContextDto context = PartnerUtil.assemberContext(getUserId());
            com.taobao.cun.common.resultmodel.ResultModel<StationDetailDto> result = stationQueryService.getStationsByTaobaoUserId(context);
            if (null != result && result.isSuccess() && null != result.getResult()) {
                Long stationId = result.getResult().getStationId();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("stationId", stationId);
                params.put("offset", 0);
                params.put("limit", 1);
                params.put("beforeThanBizMonth", DateUtil.format(new Date(), "yyyyMM"));
                params.put("orderBy", "bizDate desc");
                PBResult<Map<String, Object>> pbResult = pbService.queryForSingle("F6C67E100A7D35264E437615E1941ADC", params);
                if (null != pbResult) {
                    if (pbResult.isSuccessed()) {
                        return AjaxResult.success(pbResult.getReturnValue());
                    } else {
                        return AjaxResult.unSuccess(pbResult.getReturnMessage() != null ? pbResult.getReturnMessage() : "Query pbService error!");
                    }
                } else {
                    return AjaxResult.unSuccess("Query pbService error!");
                }
            } else {
                return AjaxResult.unSuccess("无合伙人信息.");
            }
        } catch (Exception e) {
            logger.error("Qery partnerIndicatorChart error!, taobaoUserId: {}", getUserId(), e);
            return AjaxResult.unSuccess(null != e ? e.getMessage() : "Qery partnerIndicatorChart error!");
        }
    }
}
