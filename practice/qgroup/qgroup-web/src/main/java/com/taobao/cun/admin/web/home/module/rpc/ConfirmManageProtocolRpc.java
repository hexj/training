package com.taobao.cun.admin.web.home.module.rpc;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.Security;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.common.logging.Logger;
import com.alibaba.common.logging.LoggerFactory;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.common.resultmodel.ResultModel;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiandong.zjd on 2015/6/27.
 */
@WebResource("my")
public class ConfirmManageProtocolRpc {
    public static final Logger logger = LoggerFactory.getLogger(ConfirmManageProtocolRpc.class);

    @Resource
    private StationApplyService stationApplyService;
    @Resource
    private HttpSession session;

    @Security(checkCSRF=true)
    @ResourceMapping(value = "/doConfirmManageProtocol")
    public Map<String,Object> doManageProtocolConfirm(@RequestParam(name = "stationApplyId")String  stationApplyId,
                                                      @RequestParam(name = "manageProtocolVersion")String manageProtocolVersion) {
        Map<String,Object> resultMap = new HashMap<String, Object>();

        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);

        //TODO 完成管理协议的签约
        ResultModel<Boolean> res = stationApplyService.confirmManageProtocol(userId + "", loginId,stationApplyId,manageProtocolVersion);
        resultMap.put("succ", true);
        // 成不成功都跳转到首页index判断逻辑
        resultMap.put("url","/my/index.htm");
        if(!res.isSuccess()) {
            resultMap.put("succ", false);
            resultMap.put("url","/my/confirmManageProtocol.htm");
        }
        return resultMap;
    }


}
