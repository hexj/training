package com.taobao.cun.admin.web.home.module.screen.my;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.taobao.cun.admin.bill.adapter.BillQueryServiceAdapter;
import com.taobao.cun.admin.bill.vo.BillDetailVo;
import com.taobao.cun.admin.common.switcher.CuntaoBillSwitchConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.fastjson.JSON;
import com.taobao.cun.admin.util.DateUtil;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.admin.web.vo.BillVo;
import com.taobao.cun.admin.web.vo.SubjectItemVo;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.HavanaContext;
import com.taobao.cun.dto.bill.BillDto;
import com.taobao.cun.dto.bill.SubjectItemDto;
import com.taobao.cun.service.bill.BillQueryService;

/**
 * @author guolei.maogl 2014年12月9日
 */
public class StationBillDetail {
    private static final Logger logger = LoggerFactory.getLogger(StationBillDetail.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;
    @Resource
    private BillQueryService billQueryService;

    @Resource
    private BillQueryServiceAdapter billQueryServiceAdapter;

    public void execute(TurbineRunData rundata, Context context, Navigator nav) {
        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        if (StringUtils.isNotBlank(CuntaoBillSwitchConfig.CUNTAO_BILL_TB_USER_ID)) {
            userId = Long.valueOf(CuntaoBillSwitchConfig.CUNTAO_BILL_TB_USER_ID);
        }


        HavanaContext havanaContext = getContext(String.valueOf(userId), loginId);
        String billPeriod = rundata.getParameters().getString("billPeriod");
        try {
            //获取账单信息
            ResultModel<BillDto> billDtoRm = billQueryService.getBill(havanaContext, userId, billPeriod);
            if (!billDtoRm.isSuccess()) {
                logger.error("getBill context: " + JSON.toJSONString(havanaContext) + ", taobaoUserId: "
                        + JSON.toJSONString(userId) + ", billPeriod" + billPeriod + ", billDtoRm: "
                        + JSON.toJSONString(billDtoRm));
            }
            BillVo billVo = toBillVo(billDtoRm.getResult());
            context.put("billVo", billVo);

            //获取账单明细信息,默认取得第一页
//            if(billVo != null){
//                //获取账期
//                billPeriod = billVo.getBillPeriod();
//                ResultModel<List<BillDetailVo>> billDetailDtoRm = billQueryServiceAdapter.getBillDetailList(userId, loginId, billPeriod, null, null, null);
//                if (!billDetailDtoRm.isSuccess()) {
//                    logger.error("getBill context: " + JSON.toJSONString(context) + ", userId: " + userId
//                            + ", billPeriodRm: " + JSON.toJSONString(billDetailDtoRm));
//                }
//                context.put("billDetailVo", billDetailDtoRm.getResult());
//            }



            //获取账期信息
            ResultModel<List<String>> billPeriodRm = billQueryService.getBillPeriod(havanaContext, userId, "yyyy年M月");
            if (!billPeriodRm.isSuccess()) {
                logger.error("getBill context: " + JSON.toJSONString(context) + ", userId: " + userId
                        + ", billPeriodRm: " + JSON.toJSONString(billPeriodRm));
            }
            context.put("historyBillPeriodList", billPeriodRm.getResult());





        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private HavanaContext getContext(String userId, String loginId) {


        HavanaContext context = new HavanaContext();
        context.setTaobaoUserId(userId);
        context.setLoginId(loginId);
        return context;
    }

    private BillVo toBillVo(BillDto billDto) {
        if (billDto == null)
            return null;

        BillVo billVo = new BillVo();
        billVo.setTaobaoUserId(billDto.getTaobaoUserId());
        billVo.setCuntaoStationId(billDto.getCuntaoStationId());
        billVo.setCuntaoStationName(billDto.getCuntaoStationName());
        if (StringUtils.isNotBlank(billDto.getBillPeriod())) {
            String billPeriod = billDto.getBillPeriod();
            Date date = DateUtil.parseDate("yyyyMM", billPeriod);
            if (date != null) {
                billPeriod = DateUtil.format(date, "yyyy年M月");
            }
            billVo.setBillPeriod(billPeriod);
        }
        billVo.setAfterTaxAmount(format(billDto.getAfterTaxAmount()));
        billVo.setPreTaxAmount(format(billDto.getPreTaxAmount()));
        if (billDto.getDeductionAmount() != null) {
            billVo.setDeductionAmount(format(billDto.getDeductionAmount().abs()));
        }
        List<SubjectItemDto> items = billDto.getSubjectItemList();
        if (CollectionUtils.isNotEmpty(items)) {
            SubjectItemVo tax = new SubjectItemVo();
            if (billDto.getTaxAmount() != null) {
                tax.setItemAmount(format(billDto.getTaxAmount().abs()));
            }
            billVo.setTax(tax);

            List<SubjectItemVo> subjectItemList = new ArrayList<SubjectItemVo>();
            for (SubjectItemDto si : items) {
                if (si.getItemAmount() != null && si.getItemAmount().compareTo(new BigDecimal(0)) > 0) {
                    SubjectItemVo siVo = new SubjectItemVo();
                    siVo.setItemId(si.getItemId());
                    siVo.setItemName(si.getItemName());
                    siVo.setItemAmount(format(si.getItemAmount()));
                    siVo.setComment(si.getComment());

                    if (si.getIncomeRate() != null) {
                        DecimalFormat df = new DecimalFormat("#.##%");
                        siVo.setIncomeRate(df.format(si.getIncomeRate()));
                    }
                    subjectItemList.add(siVo);
                }
            }
            billVo.setSubjectItemList(subjectItemList);
        }
        return billVo;
    }

    String format(BigDecimal bd) {
        if (bd != null)
            return NumberFormat.getInstance().format(bd);
        return "0";
    }
}
