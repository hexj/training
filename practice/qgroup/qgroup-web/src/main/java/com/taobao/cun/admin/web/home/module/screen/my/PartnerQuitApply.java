package com.taobao.cun.admin.web.home.module.screen.my;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.taobao.cun.admin.dto.ApplyDto;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;

/**
 * 村点退出申请，协议确认页面
 * @author yangwei.pengyw
 *
 */
public class PartnerQuitApply {
    @Autowired
    private StationApplyService stationApplyService;
    @Autowired
    private HttpSession session;
    
    private static final String CUN_QUIT_PROTOCOL = "partner-quit-agreements-v1"; //村点退出的协议
    private static final String CUN_QUIT_TYPE = "partner-quit";//合伙人主动退出
    
    /**
     * 查看申请详情
     * @param context
     */
    public void execute(Context context) {
        String loginId = WebUtil.getLoginId(session);
        Long userId = WebUtil.getUserId(session);
        ApplyDto applyDto = stationApplyService.getStationApplyDetailSafed(userId+"",loginId);
        
        //以下字段都是在提交申请单，调用后台RPC的时候使用，其实打开这个页面，如下的参数就已经是确定的了
        context.put("stationApplyId", applyDto.getStationApplyId()); //申请单id
        context.put("cunQuitProtocol", CUN_QUIT_PROTOCOL); //确认的协议
        context.put("cunQuitType", CUN_QUIT_TYPE); //提交合伙人退出的来源
    }
    
}
