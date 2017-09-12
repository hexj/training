package com.taobao.cun.admin.web.webx.valve;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.PipelineException;
import com.alibaba.citrus.service.pipeline.support.AbstractValve;
import com.alibaba.citrus.turbine.support.TurbineRunDataImpl;
import com.alibaba.citrus.turbine.util.TurbineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * User: wentuo
 * Date: 3/13/13
 * wentuo@taobao.com
 */
public class ExceptionHandleValve extends AbstractValve {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandleValve.class);

    private static final Logger logStatics = LoggerFactory.getLogger("StaticsLog");
    @Autowired
    private HttpServletRequest request;

    public void invoke(PipelineContext pipelineContext) throws PipelineException {

        TurbineRunDataImpl rundata = (TurbineRunDataImpl) TurbineUtil.getTurbineRunData(request);
        rundata.setRedirectLocation("//err.taobao.com/error2.html");
        logStatics.error("[error]process error,url="+request.getRequestURL());
        try{
            rundata.getResponse().resetBuffer();
        }catch (IllegalStateException e){
            log.error("ExceptionHandleValve response resetBuffer() error!",e);
        }
        Exception e = (Exception)pipelineContext.getAttribute("exception");
        if(null != e){
            log.error("ExceptionHandleValve cache error!\n",e);
        }
        pipelineContext.invokeNext();
    }
}
