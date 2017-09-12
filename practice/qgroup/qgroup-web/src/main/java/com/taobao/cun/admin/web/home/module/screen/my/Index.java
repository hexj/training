package com.taobao.cun.admin.web.home.module.screen.my;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.switches.CuntaoAdminConfig;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.motion.service.PartnerMotionService;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.SystemTypeEnum;
import com.taobao.cun.dto.UserTypeEnum;
import com.taobao.cun.dto.station.StationApplyDetailDto;
import com.taobao.cun.dto.station.enums.StationApplyStateEnum;

/**
 * Created by chenhui.lich on 2015/5/9.
 */
public class Index {
    @Autowired
    private HttpSession session;
    @Resource
    private StationApplyService stationApplyService;
    @Resource
    private PartnerMotionService partnerMotionService;
   


    public void execute(TurbineRunData rundata, Context context, Navigator nav) {

        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        StationApplyDetailDto stationApplyDetailDto = null;
        if (userId != null && StringUtils.isNotBlank(loginId)) {
            stationApplyDetailDto = stationApplyService.getStationDetail(userId + "", loginId);
        }

        // 如果不是合伙人，不允许访问此页面(默认的是)
        if (stationApplyDetailDto == null || !StationApplyStateEnum.getInServiceStatusList().contains(stationApplyDetailDto.getState())) {
            rundata.setRedirectLocation("http://err.taobao.com/error2.html");
            return;
        }
        
        ResultModel<Boolean> hasNewMotionResult=partnerMotionService.hasNewMotionInstances(userId.toString());
        if(hasNewMotionResult!=null&&hasNewMotionResult.isSuccess()){
        	context.put("hasNewTask", true);
        }
        else{
        	context.put("hasNewTask", false);
        }
        
        ResultModel<Integer> unFinishTaskCount=partnerMotionService.getUnFinishedMotionInstances(userId.toString());
        if(unFinishTaskCount!=null&&unFinishTaskCount.isSuccess()){
        	context.put("unFinishTaskCount", unFinishTaskCount.getResult());
        }
        else{
        	context.put("unFinishTaskCount",0);
        }
        // 管理协议的跳转，满足以下条件:
        // DECORATING,SERVICING,QUIT_APPLYING,QUIT_APPLY_CONFIRMED,QUITAUDITING跳转签管理协议
        // 已经签过管理协议的不用再签
        if(StringUtil.isBlank(stationApplyDetailDto.getManageProtocolVersion())||
                Integer.valueOf(stationApplyDetailDto.getManageProtocolVersion())<CuntaoAdminConfig.MANAGE_PROTOCOL_VERSION){
            String redirectUrl = buildCuntaoAdminManageProtocalUrl(stationApplyDetailDto.getStationApplyId());
            rundata.setRedirectLocation(redirectUrl);
        }
    }

    private ContextDto getContextDto(Long userId, String loginId) {
        ContextDto contextDto = new ContextDto();
        contextDto.setLoginId(userId + "");
        contextDto.setAppId(userId + "");
        contextDto.setSystemType(SystemTypeEnum.CUNTAO_ADMIN);
        contextDto.setUserType(UserTypeEnum.HAVANA);
        return contextDto;
    }

    /**
     * build管理协议的路径
     * @return
     */
    private String buildCuntaoAdminManageProtocalUrl(Long stationApplyId) {
        StringBuffer urlSb = new StringBuffer();
        urlSb.append("/my/confirmManageProtocol.htm");
        urlSb.append("?stationApplyId=");
        urlSb.append(stationApplyId);
        return urlSb.toString();
    }
}
