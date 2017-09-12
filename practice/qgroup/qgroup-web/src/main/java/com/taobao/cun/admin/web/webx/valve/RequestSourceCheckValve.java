package com.taobao.cun.admin.web.webx.valve;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taobao.cun.admin.web.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.support.AbstractValve;

public class RequestSourceCheckValve extends AbstractValve {
    public static final String REG_EX_URL = "^(http|https)://([a-zA-Z0-9-_]+\\.){0,2}(DOMAIN_PLACEHOLDER)(:[0-9]{1,5})?((/[\\s\\S]*)|(\\?[\\s\\S]*)|$)";
    public static final String REG_EX_HOST = "^([a-zA-Z0-9-_]+\\.){0,2}(DOMAIN_PLACEHOLDER)$";
    public static final String REG_EX_DOMAIN_PLACEHOLDER = "DOMAIN_PLACEHOLDER";
    private Set<String> unProtectedURLs = new HashSet<String>();

    private boolean checkHost;
    private boolean checkReferer;

    private Pattern requiredResourceTypesPattern;
    private Pattern allowedRefererRegExPattern;
    private Pattern allowedHostRegExPattern;



    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    public void setCheckReferer(boolean checkReferer) {
        this.checkReferer = checkReferer;
    }

    public void setCheckHost(boolean checkHost) {
        this.checkHost = checkHost;
    }

    public void setRequiredResourceTypes(String requestResourceTypes) {
        requestResourceTypes = requestResourceTypes.replace(",", "|");
        requiredResourceTypesPattern = Pattern.compile(requestResourceTypes);
    }

    public void setAllowedDomains(String allowedDomains) {
        allowedDomains = WebUtil.escapeDomainRegExChars(allowedDomains);
        String allowedRefererRegex = REG_EX_URL.replace(REG_EX_DOMAIN_PLACEHOLDER, allowedDomains);
        allowedRefererRegExPattern = Pattern.compile(allowedRefererRegex);
        allowedHostRegExPattern = Pattern.compile(allowedDomains);
    }

    public boolean isURLUnProtected(String target) {
        for (String url : unProtectedURLs) {
            if (url.endsWith(target)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTargetProtected(final String target) {
        return !isURLUnProtected(target);
    }


    @Override
    public void invoke(PipelineContext pipelineContext) throws Exception {
        if (isTargetProtected(request.getRequestURI())) {
            if (isCheckingRequired(request)) {
                boolean hostPermitted = true;
                boolean refererPermitted = true;

                if (checkHost)
                    hostPermitted = checkHost();

                if (checkReferer)
                    refererPermitted = checkReferer();

                if (hostPermitted && refererPermitted)
                    pipelineContext.invokeNext();
                else
                    response.setStatus(403);
            } else {
                pipelineContext.invokeNext();
            }
        } else {
            pipelineContext.invokeNext();
        }
    }

    private boolean isCheckingRequired(HttpServletRequest request) {
        Matcher matcher = requiredResourceTypesPattern.matcher(request.getRequestURI());
        if (matcher.find()){
        	return true;
        } else if (StringUtils.isNotBlank(request.getQueryString())) {
        	if (requiredResourceTypesPattern.matcher(request.getQueryString()).find()) {
        		 return true;
        	}
        }

        return false;
    }

    private boolean checkReferer() {
        String referer = request.getHeader("Referer");

        return check(referer, allowedRefererRegExPattern);
    }

    private boolean checkHost() {
        String host = request.getHeader("Host");

        return check(host, allowedHostRegExPattern);
    }

    private boolean check(String url, Pattern pattern) {

        boolean permitted = false;

        if (StringUtils.isNotEmpty(url)) {
            if (isAllowedSource(url, pattern)) {
                permitted = true;
            }
        }

        return permitted;
    }

    private boolean isAllowedSource(String referer, Pattern pattern) {
        Matcher matcher = pattern.matcher(referer);

        return matcher.find();
    }

    public Set<String> getUnProtectedURLs() {
        return unProtectedURLs;
    }

    public void setUnProtectedURLs(Set<String> unProtectedURLs) {
        this.unProtectedURLs = unProtectedURLs;
    }
}
