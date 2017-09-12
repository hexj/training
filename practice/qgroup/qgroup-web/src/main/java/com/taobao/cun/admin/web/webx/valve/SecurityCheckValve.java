package com.taobao.cun.admin.web.webx.valve;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.support.AbstractValve;
import com.alibaba.citrus.service.uribroker.URIBrokerService;
import com.alibaba.citrus.service.uribroker.uri.URIBroker;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.common.category.util.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.alibaba.citrus.turbine.util.TurbineUtil.getTurbineRunData;

public class SecurityCheckValve extends AbstractValve {

    private static Logger log = LoggerFactory.getLogger(SecurityCheckValve.class);
    public static final String SYSERROR_JSON = "{error:-2,data:null,errorMsg:''}";
    private static final String REDIRECT_URL = "redirectURL";
    private static final String TAOBAO_LOGIN_URL = "taobaoLoginURL";
    private static final String LOGIN = "login";

    private Set<String> unProtectedURLs = new HashSet<String>();
    private Set<String> returnJsonURLs = new HashSet<String>();

    @Autowired
    private HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    private URIBrokerService uriBrokerService;

    public Set<String> getReturnJsonURLs() {
        return returnJsonURLs;
    }

    public void setReturnJsonURLs(Set<String> returnJsonURLs) {
        this.returnJsonURLs = returnJsonURLs;
    }

    public boolean isURLUnProtected(String target) {
        for (String url : unProtectedURLs) {
            if (url.endsWith(target)) {
                return true;
            }
        }
        return false;
    }

    public boolean isReturnJsonURLs(String target) {
        return returnJsonURLs.contains(target);
    }

    private boolean isLogin() {
        HttpSession session = request.getSession();
        return Convert.asBoolean(session.getAttribute(LOGIN));
    }

    private boolean isTargetProtected(final String target) {
        return !isURLUnProtected(target);
    }

    public String getFullURL() {
        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append('?');
            url.append(request.getQueryString());
        }
        return url.toString();
    }

    public void invoke(PipelineContext pipelineContext) throws Exception {
        TurbineRunData runData = getTurbineRunData(request);
        if (isTargetProtected(request.getRequestURI())) {
            runData.getRequest().setAttribute("protectedUrl", "1");
            if (!isLogin()) {
                final String redirectURL = getFullURL();
                URIBroker loginUrl = uriBrokerService.getURIBroker(TAOBAO_LOGIN_URL);
                String target = loginUrl.addQueryData(REDIRECT_URL, redirectURL).render();
                runData.setRedirectLocation(target);
            }
        }
        pipelineContext.invokeNext();
    }

    public Set<String> getUnProtectedURLs() {
        return unProtectedURLs;
    }

    public void setUnProtectedURLs(Set<String> unProtectedURLs) {
        this.unProtectedURLs = unProtectedURLs;
    }

}
