package com.taobao.cun.admin.web.webx.valve;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.support.AbstractValve;
import com.alibaba.citrus.service.uribroker.URIBrokerService;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.alibaba.citrus.turbine.util.TurbineUtil.getTurbineRunData;

public class StationApplyCheckValve extends AbstractValve {

    private static Logger logger = LoggerFactory.getLogger(StationApplyCheckValve.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    private URIBrokerService uriBrokerService;
    @Autowired
    private HttpSession session;
    @Autowired
    private StationApplyService stationApplyService;

    private List<String> checkUrls;


    public String getFullURL() {
        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append('?');
            url.append(request.getQueryString());
        }
        return url.toString();
    }

    public void invoke(PipelineContext pipelineContext) throws Exception {

        String uri = request.getRequestURI();
        if (isCheck(uri)) {
            String stationApplyId = request.getParameter("stationApplyId");
            if (StringUtils.isNotBlank(stationApplyId)) {
                String loginId = WebUtil.getLoginId(session);
                Long userId = WebUtil.getUserId(session);

                boolean flag = stationApplyService.hasStationApplyConfirm(Long.parseLong(stationApplyId),userId+"",loginId);

                if (!flag) {
                    logger.error("has no confirm staion apply, stationApplyId:{},userId:{},loginId:{}",new Object[]{stationApplyId,userId,loginId});
                    throw new IllegalArgumentException("has no confirm staion apply");
                }
            }
        }
        pipelineContext.invokeNext();


    }

    public boolean isCheck(String uri) {
        return checkUrls.contains(uri);
    }



}
