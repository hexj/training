package com.taobao.cun.admin.web.home.module.rpc;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.fastjson.JSON;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.bill.adapter.BillQueryServiceAdapter;
import com.taobao.cun.admin.bill.vo.BillDetailVo;
import com.taobao.cun.admin.bill.vo.BillVo;
import com.taobao.cun.admin.common.ajax.AjaxPagedResult;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.common.switcher.CuntaoBillSwitchConfig;
import com.taobao.cun.admin.util.DateUtil;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.HavanaContext;
import com.taobao.cun.service.bill.BillQueryService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * @author guolei.maogl 2014��12��6��
 */
@WebResource("bill")
public class BillRpc {
    private static final Logger logger = LoggerFactory.getLogger(BillRpc.class);

    @Autowired
    private HttpSession session;
    @Resource
    private BillQueryServiceAdapter billQueryServiceAdapter;

	

    @ResourceMapping("/getBill")
    public AjaxResult<BillVo> getBill(@RequestParam(name = "billPeriod") String billPeriod) {


        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        if (StringUtils.isNotBlank(CuntaoBillSwitchConfig.CUNTAO_BILL_TB_USER_ID)) {
            userId = Long.valueOf(CuntaoBillSwitchConfig.CUNTAO_BILL_TB_USER_ID);
        }

        HavanaContext context = getContext(String.valueOf(userId), loginId);

        if (StringUtils.isNotBlank(billPeriod)) {
            try {
                Date date = DateUtils.parseDate(billPeriod, new String[]{"yyyy��M��"});
                billPeriod = DateUtil.format(date, "yyyyMM");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        AjaxResult<BillVo> result = new AjaxResult<BillVo>();
        try {
            result = billQueryServiceAdapter.getBill(userId,loginId, billPeriod);
            if (!result.isSuccess()) {
                logger.error("getBill context: " + JSON.toJSONString(context) + ", userId: " + userId + ", billPeriod"
                        + billPeriod + ", prm: " + JSON.toJSONString(result));
            }

        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return result;
    }

    /**
     * ��ѯ�˵���ϸ�ӿ�
     *
     * @param billPeriod ����
     * @param pageSize   ÿҳ��ҳ��Ŀ
     * @param pageNum    ҳ��
     * @param pageOrder  ����˳��
     * @return List<BillDetailVo>
     * BillDetailVo: �ؼ��ֶ�:
     * <p/>
     * tbParentTradeId ������
     * tbAuctionTitle ��Ʒ����
     * tbSellerShopTitle ��������
     * tbTradeCreateTime �µ�ʱ��
     * tbTradeFinishTime ȷ���ջ�ʱ��
     * tbFinishTotalPrice ʵ�ʳɽ����
     * amount �����
     */
    @ResourceMapping("/getBillDetail")
    public AjaxPagedResult<List<BillDetailVo>> getBillDetail(@RequestParam(name = "billPeriod") String billPeriod,
                                                             @RequestParam(name = "pageSize") Integer pageSize,
                                                             @RequestParam(name = "pageNum") Integer pageNum,
                                                             @RequestParam(name = "pageOrder") String pageOrder
    ) {
        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        HavanaContext context = getContext(String.valueOf(userId), loginId);
        if (StringUtils.isNotBlank(billPeriod)) {
            try {
                Date date = DateUtils.parseDate(billPeriod, new String[]{"yyyy��M��"});
                billPeriod = DateUtil.format(date, "yyyyMM");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        AjaxPagedResult ajaxPagedResult = null;

        try {
            ajaxPagedResult = billQueryServiceAdapter.getBillDetailList(userId, loginId, billPeriod, pageSize, pageNum, pageOrder);
            if (ajaxPagedResult.isSuccess()) {
                logger.error("getBill context: " + JSON.toJSONString(context) + ", userId: " + userId + ", billPeriod"
                        + billPeriod + ", prm: " + JSON.toJSONString(ajaxPagedResult));
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return ajaxPagedResult;
    }

    private HavanaContext getContext(String userId, String loginId) {
        HavanaContext context = new HavanaContext();
        context.setTaobaoUserId(userId);
        context.setLoginId(loginId);
        return context;
    }



    String format(BigDecimal bd) {
        if (bd != null)
            return NumberFormat.getInstance().format(bd);
        return "0";
    }
}
