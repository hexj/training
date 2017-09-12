package com.taobao.cun.admin.web.home.module.screen.status;


import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.common.switcher.ConditionalSwitcher;
import com.taobao.cun.common.switcher.Switcher;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 网站开关状态列表
 *
 * @author junlong.zhangjl
 * @since 2015/1/14.
 */
public class SwitcherList {
    @Autowired
    private HttpSession session;

    @Resource
    private Map<String, Switcher> cuntaoAdminBasicSwitcherMap;

    @Resource
    private ConditionalSwitcher<String> cashierFunctionConditionalSwitcher;

    public void execute(TurbineRunData rundata, Context context, Navigator nav) {
        for (Map.Entry<String, Switcher> entry : cuntaoAdminBasicSwitcherMap.entrySet()) {
            context.put("loginId", WebUtil.getLoginId(session));
            context.put("cuntaoAdminBasicSwitcherMap", cuntaoAdminBasicSwitcherMap);
            context.put("cashierFunctionConditionalSwitcher",cashierFunctionConditionalSwitcher);
        }
    }
}
