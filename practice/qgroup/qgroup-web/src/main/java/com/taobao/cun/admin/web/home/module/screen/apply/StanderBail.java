package com.taobao.cun.admin.web.home.module.screen.apply;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.alipay.corporate.AlipayBailService;
import com.taobao.cun.admin.dto.AlipayResponseDto;
import com.taobao.cun.admin.dto.RenderUrlDto;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.station.StationApplyDetailDto;
import com.taobao.cun.dto.station.enums.StationApplyProtocolConfirmingStepEnum;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by Fe on 14/11/28.
 */
public class StanderBail {
    @Resource
    private StationApplyService stationApplyService;

    @Autowired
    private HttpSession session;
    @Resource
    private AlipayBailService alipayBailService;

    public void execute(TurbineRunData rundata, Context context) {

        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);

        AlipayResponseDto alipayResponseDto = alipayBailService.freezeNotice(userId+"",loginId);
        String url = "/apply/applyFail.htm";
        if (alipayResponseDto != null) {
            if (alipayResponseDto.getData() != null) {
                url = (String)alipayResponseDto.getData();
            }
        }
        rundata.setRedirectLocation(url);

    }
}
