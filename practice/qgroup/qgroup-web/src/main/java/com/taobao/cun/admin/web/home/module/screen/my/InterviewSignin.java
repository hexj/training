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
	private final String MSG_NOT_LOGIN = "��ȡtaobaoIdʧ�ܣ����ȵ�¼";

	private static final Map<String, String> stateMessage = new HashMap<String, String>(10);

	static {
		//stateMessage.put("STATE_NOT_APPLY", "��δ��������ʱ����ǩ��");
		stateMessage.put("STATE_APPLY_WAIT", "�����ı�������ϵͳ����У���ʱ�޷�ǩ���������ĵȴ���˽����");
		stateMessage.put("STATE_APPLY_REFUSE", "�ǳ���Ǹ�����ı�������δͨ�����޷�ǩ��");

		stateMessage.put("STATE_APPLY_INTERVIEW", "��ϲ����ǩ���ɹ�");
		stateMessage.put("STATE_APPLY_NOTIFY", "��ϲ����ǩ���ɹ�");
		stateMessage.put("STATE_APPLY_NOT_SIGNED", "��ϲ����ǩ���ɹ�");

		stateMessage.put("STATE_APPLY_SIGNED", "����ǩ���������ظ�ǩ��");

		stateMessage.put("STATE_APPLY_REFUSE_INTERVIEW", "�ǳ���Ǹ�����Ѿ�����ͨ����δ֪ͨ�����ԣ��޷�ǩ��");
		stateMessage.put("STATE_APPLY_SUCC", "�ǳ���Ǹ�����Ѿ�����ͨ����δ֪ͨ�����ԣ��޷�ǩ��");
		stateMessage.put("STATE_APPLY_COOPERATION", "�ǳ���Ǹ�����Ѿ�����ͨ����δ֪ͨ�����ԣ��޷�ǩ��");
	}

	public void execute(TurbineRunData rundata, Context context, Navigator navigator) {
		Long taobaoId = 0l;
		try {
			taobaoId = WebUtil.getUserId(session);
			if (taobaoId == null || taobaoId.equals(0l)) {
				// δ��¼��ת��taobao��¼ҳ��
				rundata.setRedirectLocation(LOGIN_URL);
				context.put(MSG, MSG_NOT_LOGIN);
				return;
			}

			ContextDto contextDto = PartnerUtil.assemberContext(taobaoId);
			PartnerApplyDto partnerApplyDto = partnerServiceAdapter.queryPartnerByTaobaoUserId(taobaoId, contextDto);
			if (null == partnerApplyDto || null == partnerApplyDto.getId() || partnerApplyDto.getId() < 1) {
				// û�������¼

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
						// ǩ��
						partnerApplyDto.setPartnerApplyStateEnum(PartnerApplyStateEnum.STATE_APPLY_SIGNED);
						partnerServiceAdapter.updatePartnerApply(partnerApplyDto, contextDto);
					}
				} else {
					context.put(MSG, "δ��ȡ����Ӧ����״̬��ǩ��ʧ��");
				}
			}
		} catch (Exception e1) {
			logger.error("ǩ��ʧ��: " + e1.getMessage());
			// rundata.setRedirectLocation(ViewConstants.LOGIN_URL);
			context.put(MSG, "ǩ��ʧ��");
			return;
		}
	}
}
