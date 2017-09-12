package com.taobao.cun.admin.web.home.module.screen.apply;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.taobao.cun.admin.alipay.AlipayErrorCode;

/**
 * Created by Fe on 14/11/25.
 */
public class ApplyFail {
    public static final Logger logger = LoggerFactory.getLogger(ApplyFail.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;


    /**
     * ≤Èø¥…Í«ÎœÍ«È
     * @param context
     */
    public void execute(Context context) {
        String code = request.getParameter("code");

        String errorMessage = "";
        if (StringUtils.isEmpty(code)) {
            code = "SYSTEM_ERROR";
        }

        String codeMessage = AlipayErrorCode.getErrorCodeMessage(code);
        if (StringUtils.isNotBlank(codeMessage)) {
            errorMessage = codeMessage;
        } else {
            errorMessage = code;
        }

        context.put("errorMessage",errorMessage);
        if("0003".equals(code)){
        	context.put("showAlipyLink", true);
        }

    }
}
