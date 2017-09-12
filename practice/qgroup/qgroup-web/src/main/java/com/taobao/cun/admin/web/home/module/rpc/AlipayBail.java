package com.taobao.cun.admin.web.home.module.rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.taobao.cun.admin.alipay.corporate.AlipayBailService;
import com.taobao.cun.admin.dto.AlipayResponseDto;

/**
 * Created by Fe on 14/11/24.
 */
@WebResource("alipay")
public class AlipayBail {

    public static final Logger logger = LoggerFactory.getLogger(AlipayBail.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpSession session;
    @Resource
    private AlipayBailService alipayBailService;

    /**
     * 支付宝签约同步通知接口
     */
    @ResourceMapping(value="/customerSign/receive_return")
    public void receiveReturn() {
        Map<String,String> paramMap = new HashMap<String, String>();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getParameter(key);
            paramMap.put(key,value);
        }

        try {
            AlipayResponseDto alipayResponseDto = alipayBailService.alipayCustomerBailReturn(paramMap);

            if (alipayResponseDto.isSuccess()) {
                //TODO 引导冻结成功页面
                redirect("/apply/applySuccess.htm");
            } else {
                //TODO 引导错误提示页面
                redirect("/apply/applyFail.htm?code=" + alipayResponseDto.getErrorCode());
            }
        } catch (Exception e) {
            logger.error("alipayBailService.alipayCustomerBailReturn has err , e : {}",e);
            redirect("/apply/applyFail.htm");
        }
    }

    /**
     * 支付宝签约异步通知接口
     */
    @ResourceMapping(value="/customerSign/receive_notify")
    public void receiveNotify() {
        Map<String,String> paramMap = new HashMap<String, String>();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getParameter(key);
            paramMap.put(key,value);
        }

        if (paramMap.size() == 0) {
            throw new IllegalArgumentException("receiveNotify request error !");
        }

        AlipayResponseDto alipayResponseDto = alipayBailService.alipayCustomerBailNotify(paramMap);
        if (alipayResponseDto.isSuccess()) {
            printSuccess();
        }
    }

    /**
     * 保证金冻结异步通知接口
     */
    @ResourceMapping(value="/standerBail/receive_notify")
    public void standerBailReceiveNotify() {

        Map<String,String> paramMap = new HashMap<String, String>();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getParameter(key);
            paramMap.put(key,value);
        }

        if (paramMap.size() == 0) {
            throw new IllegalArgumentException("receiveNotify request error !");
        }

        AlipayResponseDto alipayResponseDto = alipayBailService.alipayStandardBailNotify(paramMap);
        if (alipayResponseDto.isSuccess()) {
            printSuccess();
        }

    }

    private void redirect(String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            //TODO IGNORE
        }
    }

    private void printSuccess() {
        PrintWriter output = null;
        try {
            output = response.getWriter();
            output.write("success");
        } catch (IOException e) {
            logger.error("printSuccess error! ");
        } finally {
            if (output != null) {
                output.flush();
                output.close();
            }
        }
    }
}
