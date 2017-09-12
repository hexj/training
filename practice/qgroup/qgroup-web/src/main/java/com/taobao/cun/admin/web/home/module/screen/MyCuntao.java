package com.taobao.cun.admin.web.home.module.screen;

import com.alibaba.citrus.turbine.Context;
import com.taobao.cun.admin.service.StationApplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Fe on 14/11/22.
 */
public class MyCuntao {

    public static final Logger logger = LoggerFactory.getLogger(MyCuntao.class);

    @Resource
    private StationApplyService stationApplyService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;


    /**
     * 进入我的村淘页面
     * @param context
     */
    public void execute(Context context) {

    }
}
