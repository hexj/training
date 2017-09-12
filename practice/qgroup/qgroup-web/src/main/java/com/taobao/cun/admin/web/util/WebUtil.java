package com.taobao.cun.admin.web.util;

import com.taobao.cun.admin.switches.CuntaoAdminConfig;
import com.taobao.session.SessionKeyConstants;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WebUtil {

    public static final String DEFAULT_JSONP_KEY = "callback";

    public static Long getUserId(HttpSession session) {
        if(!"NULL".equals(CuntaoAdminConfig.DEFAULT_TAOBAO_USER_ID)){
                return  Long.parseLong(CuntaoAdminConfig.DEFAULT_TAOBAO_USER_ID);
        }
        if (session.getAttribute(SessionKeyConstants.ATTRIBUTE_USER_ID_NUM) != null) {
            return Long.valueOf((String) session
                    .getAttribute(SessionKeyConstants.ATTRIBUTE_USER_ID_NUM));
        }
        return 0l;
    }

    public static String getLoginId(HttpSession session) {
        if(!"NULL".equals(CuntaoAdminConfig.DEFAULT_TAOBAO_NICK)){
            return  CuntaoAdminConfig.DEFAULT_TAOBAO_NICK;
        }
        String loginId = StringUtils.EMPTY;

        if (session.getAttribute(SessionKeyConstants.ATTRIBUTE_TRACK_NICK) != null) {
            loginId = (String) session
                    .getAttribute(SessionKeyConstants.ATTRIBUTE_TRACK_NICK);
        }

        loginId = StringEscapeUtils.unescapeJava(loginId);

        return loginId;
    }


    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    public static String escapeDomainRegExChars(String domain) {
        if (StringUtils.isBlank(domain))
            return domain;
        else
            return domain.replace(".", "\\.").replace("?", "\\?");
    }


    public static String getRefer(HttpServletRequest request) {
        return request.getHeader("referer");
    }
    
    //https改造, 代购单表中存的url目前还是http的, 需要更新成https的
    public static String convertToHttpsUrl(String picUrl){
		picUrl = picUrl.replaceAll("http://img\\d*.taobaocdn.com", "//img.alicdn.com");
    	if(picUrl.startsWith("http://")){
    		picUrl = picUrl.replaceAll("http://", "//");
    	}
    	return picUrl;
    }
}
