package com.taobao.cun.admin.web.home.module.layout.my;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.partner.PartnerApplyDto;
import com.taobao.cun.dto.station.StationApplyDetailDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenhuilich on 15/8/31.
 */
public class Default {

    @Autowired
    private HttpServletRequest request;

    public void execute(TurbineRunData rundata, Context context, Navigator nav) {
       String reuestUrl = request.getRequestURI();
        String spmBnum = "7758618";
        context.put("spmBNum", spmBnum);

    }
}
