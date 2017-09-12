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
 * �ϻ���ҵ��rpc
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
            res.setExceptionDesc("��ѯ�ϻ���ҵ���쳣");
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
     * ����Ϊ��λ��ת��ΪԪ ����100��
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
        return sb.append(c.get(Calendar.MONTH) + 1).append("��").append(c.get(Calendar.DATE)).append("��").toString();

    }

    private static String getYearMonth(Calendar c) {
        StringBuilder sb = new StringBuilder();
        return sb.append(c.get(Calendar.YEAR)).append("��").append(c.get(Calendar.MONTH) + 1).append("��").toString();

    }

    private static String getWeek(int week) {
        String x = "";
        switch (week) {
            case 1:
                x = "������";
                break;
            case 2:
                x = "����һ";
                break;
            case 3:
                x = "���ڶ�";
                break;
            case 4:
                x = "������";
                break;
            case 5:
                x = "������";
                break;
            case 6:
                x = "������";
                break;
            case 7:
                x = "������";
                break;
        }
        return x;
    }

    /**
     * ��ѯ�ϻ���ָ�����а�
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
            // ��½У��
            if (!isLogin()) {
                return AjaxResult.unSuccess("Login again please!");
            }

            // ----------- ��ȡ�����а� ---------------
            IndicatorChartDto countryChart = queryChart(SortRanges.country, 50);
            if (countryChart == null) return AjaxResult.unSuccess("δ��ѯ���ϻ��˹������а���Ϣ�����Ժ����ԣ�");

            // ------------ ��ȡ�����а� -------------
            IndicatorChartDto countyChart = queryChart(SortRanges.county, 30);
            if (countyChart == null) return AjaxResult.unSuccess("δ��ѯ���ϻ��������а���Ϣ�����Ժ����ԣ�");

            // δ�μ�����
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

        if (SortRanges.country.equals(indicatorChart.getSortRange())) indicatorChart.setSortRangeValue("ȫ�����д�����");

        if (SortRanges.county.equals(indicatorChart.getSortRange())) indicatorChart.setSortRangeValue(queryOrgNameBy(indicatorChart.getCountyId(), indicatorChart.getTaobaoUserId()));

    }

    private String queryOrgNameBy(Long orgId, Long taobaoUserId) {
        CuntaoOrgDTO cuntaoOrg = cuntaoOrgServiceAdapter.queryBy(orgId, taobaoUserId);

        if (cuntaoOrg == null || StringUtil.isBlank(cuntaoOrg.getName())) {
            logger.error("Cannot find cuntaoOrg by this taobaoUserId: " + taobaoUserId + ", orgId : " + orgId);
            throw new RuntimeException("�޷���ȡ�ϻ�����֯��Ϣ�����Ժ����ԣ�");
        }

        return cuntaoOrg.getName();
    }

    /**
     * �ϻ���δ��������
     * 
     * @param countryChart
     * @return
     */
    private boolean notParticipateInRanking(IndicatorChartDto indicatorChartDto) {
        return indicatorChartDto.getSortIndex() <= 0;
    }

    /**
     * ��ѯ�ϻ���ָ�����а�
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
     * ��ѯ�ϻ��˾�ʦ����
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
            // ��½У��
            if (!isLogin()) {
                return AjaxResult.unSuccess("Login again please!");
            }
            // ��ȡ�ϻ�����Ϣ
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
                return AjaxResult.unSuccess("�޺ϻ�����Ϣ.");
            }
        } catch (Exception e) {
            logger.error("Qery partnerIndicatorChart error!, taobaoUserId: {}", getUserId(), e);
            return AjaxResult.unSuccess(null != e ? e.getMessage() : "Qery partnerIndicatorChart error!");
        }
    }
}
