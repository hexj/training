package com.taobao.cun.admin.web.home.module.rpc;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.ws.WebFault;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.common.ajax.AjaxPagedResult;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.partneractivity.adapter.PartnerActivityAdapter;
import com.taobao.cun.admin.partneractivity.vo.MyPartnerActivityVo;
import com.taobao.cun.admin.util.DateUtil;
import com.taobao.cun.admin.web.home.module.BaseController;

/**
 * Created by chenhui.lich on 2015/6/3.
 */
@WebResource("partner")
public class PartnerActivityRpc extends BaseController {

    @Resource
    private PartnerActivityAdapter partnerActivityAdapter;

    @ResourceMapping(value = "/getActivitys")
    @Security(checkCSRF = true)
    public AjaxResult<List<MyPartnerActivityVo>> getPartnerPerformance(@RequestParam(name = "success", defaultValue = "true") Boolean success, @RequestParam(name = "level", defaultValue = "1") Integer level, @RequestParam(name = "gmv", defaultValue = "9999") Long gmv) {
        return getPartnerPerformance();
    }

    public AjaxResult<List<MyPartnerActivityVo>> getPartnerPerformance() {
        String loginId = this.getLoginId();
        Long userId = this.getUserId();
        return partnerActivityAdapter.getPartnerActivityInfo(userId, loginId);
    }


}
