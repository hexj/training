package com.taobao.cun.admin.web.webx.valve;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.support.AbstractValve;
import com.alibaba.common.lang.StringUtil;
import com.taobao.cms.client.CMSTool;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理TMS静态页面资源的Valve
 * <p/>
 * 匹配到配置的路径时读取TMS的静态页面资源直接返回给客户端
 *
 * @author junlong.zhangjl
 */
public class TmsStaticPageHandleValve extends AbstractValve {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    private String urlMatchPattern = "^/tms/";
    private Pattern pattern = Pattern.compile(urlMatchPattern);
    private String pageDir = "/market/cun/";
    private int siteId = 1;
    private static boolean CACHE_ENABLED = true;

    public void setUrlMatchPattern(String urlMatchPattern) {
        this.urlMatchPattern = urlMatchPattern;
        pattern = Pattern.compile(urlMatchPattern);
    }

    public void setPageDir(String pageDir) {
        this.pageDir = pageDir;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public void setCacheEnabled(boolean enableCache) {
        CACHE_ENABLED = enableCache;
    }

    public static void setCacheStatus(boolean enableCache) {
        CACHE_ENABLED = enableCache;
    }

    @Override
    public void invoke(PipelineContext pipelineContext) throws Exception {
        String uri = request.getRequestURI();

        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            String fullFilePath = pageDir + matcher.replaceAll(StringUtil.EMPTY_STRING);

            String pageContent = CMSTool.importRgn(fullFilePath, siteId, CACHE_ENABLED);

            PrintWriter writer = response.getWriter();
            if (StringUtil.isNotEmpty(pageContent)) {

                response.setContentType("text/html");
                writer.print(pageContent);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

            writer.flush();
            writer.close();
        } else
            pipelineContext.invokeNext();
    }
}
