package com.taobao.cun.admin.web.home.module.screen.my;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.service.CuntaoTpaService;
import com.taobao.cun.admin.station.StationServiceAdapter;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.HavanaContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by wangbowangb on 15-1-6.
 */
public class AgentOrderList {
    public static final Logger logger = LoggerFactory.getLogger(AgentOrderList.class);
    @Autowired
    private HttpSession session;
    @Autowired
    private StationServiceAdapter stationServiceAdapter;
    @Resource
    private CuntaoTpaService cuntaoTpaService;

    public void execute(TurbineRunData rundata, Context context, Navigator nav) {
        HavanaContext havanaContext = getHavanaContext();
        //淘帮手无权访问
        if(true == cuntaoTpaService.isCuntaoTpa(WebUtil.getUserId(session)))
            rundata.setRedirectLocation("http://err.taobao.com/error2.html");
        String stationName= stationServiceAdapter.getStationNameByTaobaoUserId(havanaContext);
        context.put("name",stationName);
    }

    private HavanaContext getHavanaContext(){
        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        HavanaContext havanaContext = new HavanaContext();
        havanaContext.setLoginId(loginId);
        havanaContext.setTaobaoUserId(userId.toString());
        return havanaContext;
    }
}
