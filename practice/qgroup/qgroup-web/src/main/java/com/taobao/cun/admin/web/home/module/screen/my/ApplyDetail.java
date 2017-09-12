package com.taobao.cun.admin.web.home.module.screen.my;
import com.alibaba.citrus.turbine.Context;
import com.taobao.cun.admin.dto.ApplyDto;
import com.taobao.cun.admin.service.CuntaoTpaService;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.station.enums.StationApplyStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ApplyDetail {
	 private static final String CUN_QUIT_PROTOCOL = "null-agreements"; //村点退出的协议
	 private static final String CUN_QUIT_TYPE = "partner-quit";//合伙人主动退出
	 
	 public static final Logger logger = LoggerFactory.getLogger(ApplyDetail.class);
	    @Autowired
	    private StationApplyService stationApplyService;
	    @Autowired
	    private HttpServletRequest request;
	    @Autowired
	    private HttpSession session;
		@Resource
		private CuntaoTpaService cuntaoTpaService;

	    /**
	     * 查看申请详情
	     * @param context
	     */
	    public void execute(Context context) {
	        
	        String loginId = WebUtil.getLoginId(session);
	        Long userId = WebUtil.getUserId(session);
	        ApplyDto applyDto = stationApplyService.getStationApplyDetailSafed(userId+"",loginId);

	        if (applyDto == null) {
	            throw new IllegalArgumentException("applyService.getStationApply result is null");
	        }
	        
	        //我的村淘只有在冻结了保证金后才能看到
	        if(!StationApplyStateEnum.getInServiceStatusList().contains(applyDto.getState())) {
	        	throw new IllegalArgumentException("has no servicing staion apply");
	        }
			//淘帮手不能自主申请退出
			if(true == cuntaoTpaService.isCuntaoTpa(userId))
				context.put("tpa",true);
			else
				context.put("tpa",false);
	        context.put("name",applyDto.getName());
	        context.put("orgName",applyDto.getOrgName());
	        context.put("loginId",applyDto.getLoginId());
	        context.put("alipayAccount",applyDto.getAlipayAccount());
	        context.put("mobile",applyDto.getMobile());
	        context.put("applierName",applyDto.getApplierName());
	        context.put("idenNum",applyDto.getIdenNum());
			context.put("provinceDetail",applyDto.getProvinceDetail());
			context.put("cityDetail",applyDto.getCityDetail());
			context.put("countyDetail",applyDto.getCountyDetail());
			context.put("townDetail",applyDto.getTownDetail());
	        context.put("address",applyDto.getAddress());
	        context.put("imageUrls",applyDto.getIdCardImageUrl());
	        context.put("stationApplyContent",applyDto.getStationApplyContent());
	        context.put("applyStationId",applyDto.getStationApplyId());
	        context.put("version",applyDto.getVersion());
	        context.put("email",applyDto.getEmail());
	        context.put("status", applyDto.getState().getCode()); //获取当前申请单的状态
	        context.put("cunQuitProtocol", CUN_QUIT_PROTOCOL); //确认的协议
	        context.put("cunQuitType", CUN_QUIT_TYPE); //提交合伙人退出的来源
	        context.put("quitType", applyDto.getQuitType()); //提交合伙人退出的来源
	    }	    

}
