package com.taobao.cun.admin.web.home.module.rpc;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.taobao.cun.admin.common.ajax.AjaxResult;
import com.taobao.cun.mobile.result.CuntaoApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.extension.rpc.annotation.WebResource;
import com.alibaba.intl.zurich.result.QRCodeServiceResult;
import com.alibaba.intl.zurich.result.ZurichApiResult;
import com.taobao.cun.admin.dto.ApplyDto;
import com.taobao.cun.admin.service.CuntaoQRCodeService;
import com.taobao.cun.admin.service.StationApplyService;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.admin.web.vo.AgentInfo;

/**
 * @author guolei.maogl 2015年3月11日
 */
@WebResource("agent")
public class AgentInfoRpc {
	private static final Logger logger = LoggerFactory.getLogger(AgentInfoRpc.class);

	@Autowired
	private HttpSession session;
	@Resource
	private CuntaoQRCodeService cuntaoQRCodeService;
	@Autowired
	private StationApplyService stationApplyService;

	@ResourceMapping("/getAgentInfo")
	public AjaxResult<AgentInfo> getAgentInfo() {
		String loginId = WebUtil.getLoginId(session);
		Long userId = WebUtil.getUserId(session);

		AjaxResult<AgentInfo> result = new AjaxResult<AgentInfo>();
		try {
			AgentInfo agentInfo = new AgentInfo();
			ApplyDto applyDto = stationApplyService.getStationApplyDetail(userId + "", loginId);
			if (applyDto != null) {
				agentInfo.setStationName(applyDto.getName());
				agentInfo.setName(applyDto.getApplierName());
				agentInfo.setTaobaoNick(applyDto.getLoginId());
			} else {
				result.setSuccess(false);
				result.setExceptionDesc("找不到服务点信息");
				return result;
			}

			CuntaoApiResult<String> zr = cuntaoQRCodeService.generateDomainQRCode(userId,
					applyDto.getStationId());
			if (zr.isSuccessful()) {
				String url = zr.getData();
				agentInfo.setUrl(url);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setExceptionDesc(zr.getMessage());
			}
			result.setData(agentInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setExceptionDesc(e.getMessage());
		}
		return result;
	}
}
