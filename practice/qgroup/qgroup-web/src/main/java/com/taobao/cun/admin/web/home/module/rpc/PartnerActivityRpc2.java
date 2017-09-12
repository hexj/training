package com.taobao.cun.admin.web.home.module.rpc;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.taobao.cun.admin.common.ajax.AjaxPagedResult;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.partneractivity.adapter.PartnerActivityAdapter;
import com.taobao.cun.admin.partneractivity.vo.MyPartnerActivityVo;
import com.taobao.cun.admin.web.home.module.BaseController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by chenhui.lich on 2015/6/3.
 */
@WebResource("partner")
public class PartnerActivityRpc2 extends BaseController {

    @Resource
    private PartnerActivityAdapter partnerActivityAdapter;

    @ResourceMapping(value = "/getActivitysTemp")
    @Security(checkCSRF = true)
    public AjaxResult<List<MyPartnerActivityVo>> getActivityInfo() {

        String loginId = this.getLoginId();
        Long userId = this.getUserId();
        return partnerActivityAdapter.getPartnerActivityInfo(userId, loginId);
    }
}
