package com.taobao.cun.admin.web.home.module.control.my;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.cun.admin.menu.service.IcuntaoMenuServiceImpl;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.common.resultmodel.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

public class AsideMenu {
    public static final Logger logger = LoggerFactory.getLogger(AsideMenu.class);

    @Autowired
    private HttpSession session;
    @Resource
    private IcuntaoMenuServiceImpl icuntaoMenuService;

    public void execute(TurbineRunData rundata, Context context, Navigator nav) {

		String loginId = WebUtil.getLoginId(session);
		Long userId = WebUtil.getUserId(session);
        String uri = rundata.getRequest().getRequestURI();
        //当前菜单项标示设置
        String currentIndex = "";
        if(uri.contains("agentCard")) currentIndex = "card";
        if(uri.contains("agentOrderList")) currentIndex = "order";
        if(uri.contains("applyDetail") || uri.contains("partnerQuitApply")) currentIndex = "my";
        if(uri.contains("assetsList")) currentIndex = "assets";
        if(uri.contains("copartnerApplyDetail")) currentIndex = "partner";
        if(uri.contains("copartnerApplySuccess")) currentIndex = "partner";
        if(uri.contains("couponList")) currentIndex = "coupon";
        if(uri.contains("exam")) currentIndex = "exam";
        if(uri.contains("fullscreenManage")) currentIndex = "fullscreen";
        if(uri.contains("mobileAppDownload")) currentIndex = "app";
        if(uri.contains("receiptDetail")) currentIndex = "receipt";
        if(uri.contains("receiptHistory")) currentIndex = "receiptHistory";
        if(uri.contains("stationBillDetail") || uri.contains("stationBillNotice") || uri.contains("stationBillProtocol")) currentIndex = "income";

        ResultModel<String> resultModel = icuntaoMenuService.renderMenu(userId,loginId,currentIndex);
        if(resultModel.isSuccess()){
            context.put("menu",resultModel.getResult());
        }
        else{
            logger.error("icuntao menu rendering failed.");
//            IcuntaoMenuVo menuVo = icuntaoMenuService.buildMenuVoForUser(userId,loginId);
//            if(menuVo!=null){
//                context.put("userId",menuVo.getUserId());
//                context.put("loginId",menuVo.getLongId());
//                context.put("hasStationApply",menuVo.getHasStationApply());
//                context.put("showAssets",menuVo.getShowAssets());
//                context.put("hasPartnerApply",menuVo.getHasPartnerApply());
//                context.put("tpa",menuVo.getTpa());
//                context.put("grayTpa",menuVo.getGrayTpa());
//                context.put("viewAgentOrderMenu",menuVo.getViewAgentOrderMenu());
//                context.put("scorderOn",menuVo.getScorderOn());
//                context.put("alipayFeature",menuVo.getAlipayFeature());
//                context.put("icuntaoDomain",menuVo.getIcuntaoDomain());
//                context.put("cuntaoAdminDomain",menuVo.getCuntaoAdminDomain());
//            }
        }

    }

}
