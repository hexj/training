package com.taobao.cun.admin.web.home.module.screen.my;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.admin.adapter.cuntao.center.PartnerServiceAdapter;
import com.taobao.cun.admin.web.constants.ViewConstants;
import com.taobao.cun.admin.web.partner.module.util.PartnerUtil;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.partner.PartnerApplyDto;
import com.taobao.cun.dto.partner.PartnerApplyStateEnum;

public class InterviewSignin {

	public static final Logger logger = LoggerFactory.getLogger(InterviewSignin.class);

	@Autowired
	private HttpSession session;

	@Autowired
	private PartnerServiceAdapter partnerServiceAdapter;

	public static final String LOGIN_URL = "https://login.taobao.com/member/login.jhtml?redirectURL=https%3a%2f%2fcuntaoadmin.taobao.com%2fmy%2finterviewSignin.htm";
	private final String MSG = "msg";
	private final String MSG_NOT_LOGIN = "获取taobaoId失败，请先登录";

	private static final Map<String, String> stateMessage = new HashMap<String, String>(10);

	static {
		//stateMessage.put("STATE_NOT_APPLY", "您未报名，暂时不能签到");
		stateMessage.put("STATE_APPLY_WAIT", "“您的报名还在系统审核中，暂时无法签到，请耐心等待审核结果。");
		stateMessage.put("STATE_APPLY_REFUSE", "非常抱歉，您的报名初审未通过，无法签到");

		stateMessage.put("STATE_APPLY_INTERVIEW", "恭喜您，签到成功");
		stateMessage.put("STATE_APPLY_NOTIFY", "恭喜您，签到成功");
		stateMessage.put("STATE_APPLY_NOT_SIGNED", "恭喜您，签到成功");

		stateMessage.put("STATE_APPLY_SIGNED", "您已签到，无需重复签到");

		stateMessage.put("STATE_APPLY_REFUSE_INTERVIEW", "非常抱歉，您已经面试通过或未通知您面试，无法签到");
		stateMessage.put("STATE_APPLY_SUCC", "非常抱歉，您已经面试通过或未通知您面试，无法签到");
		stateMessage.put("STATE_APPLY_COOPERATION", "非常抱歉，您已经面试通过或未通知您面试，无法签到");
	}

	public void execute(TurbineRunData rundata, Context context, Navigator navigator) {
		Long taobaoId = 0l;
		try {
			taobaoId = WebUtil.getUserId(session);
			if (taobaoId == null || taobaoId.equals(0l)) {
				// 未登录跳转到taobao登录页面
				rundata.setRedirectLocation(LOGIN_URL);
				context.put(MSG, MSG_NOT_LOGIN);
				return;
			}

			ContextDto contextDto = PartnerUtil.assemberContext(taobaoId);
			PartnerApplyDto partnerApplyDto = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, contextDto);
			if (null == partnerApplyDto || null == partnerApplyDto.getId() || partnerApplyDto.getId() < 1) {
				// 没有申请记录

				context.put(MSG, stateMessage.get("STATE_NOT_APPLY"));
			} else {
				PartnerApplyStateEnum partnerApplyStateEnum = partnerApplyDto.getPartnerApplyStateEnum();
				if (partnerApplyStateEnum != null) {
					String msg = stateMessage.get(partnerApplyStateEnum.getCode());
					if (StringUtil.isNotBlank(msg)) {
						context.put(MSG, stateMessage.get(partnerApplyStateEnum.getCode()));
					} else {
						context.put(MSG, "" + partnerApplyStateEnum.getDesc());
					}
					if (PartnerApplyStateEnum.STATE_APPLY_INTERVIEW.equals(partnerApplyStateEnum)
							|| PartnerApplyStateEnum.STATE_APPLY_NOTIFY.equals(partnerApplyStateEnum)
							|| PartnerApplyStateEnum.STATE_APPLY_NOT_SIGNED.equals(partnerApplyStateEnum)) {
						// 签到
						partnerApplyDto.setPartnerApplyStateEnum(PartnerApplyStateEnum.STATE_APPLY_SIGNED);
						partnerServiceAdapter.updatePartnerApply(partnerApplyDto, contextDto);
					}
				} else {
					context.put(MSG, "未获取到对应申请状态，签到失败");
				}
			}
		} catch (Exception e1) {
			logger.error("签到失败: " + e1.getMessage());
			// rundata.setRedirectLocation(ViewConstants.LOGIN_URL);
			context.put(MSG, "签到失败");
			return;
		}
	}
}
