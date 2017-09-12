package com.taobao.cun.admin.web.home.module.control.my;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.admin.partneractivity.adapter.PartnerActivityAdapter;
import com.taobao.cun.admin.partneractivity.vo.MyPartnerActivityVo;
import com.taobao.cun.admin.util.DateUtil;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.partner.PartnerApplyDto;
import com.taobao.cun.dto.partneractivity.PartnerActivityDto;
import com.taobao.cun.dto.station.StationApplyDetailDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhui.lich on 2015/6/5.
 */
public class MyActivity {

    @Autowired
    private HttpSession session;

    @Resource
    private PartnerActivityAdapter partnerActivityAdapter;

    public void execute(TurbineRunData rundata, Context context, Navigator nav) {

        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        AjaxResult<List<MyPartnerActivityVo>> ajaxResult = partnerActivityAdapter.getPartnerActivityInfo(userId, loginId);
        List<MyPartnerActivityVo> partnerActivityVoList = ajaxResult.getData();
        context.put("partnerActivityVoList", partnerActivityVoList);
    }
}
