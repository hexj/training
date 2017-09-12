package com.taobao.cun.admin.web.partner.module.util;

import com.taobao.cun.admin.web.vo.TpApplyVo;
import com.taobao.security.util.SensitiveDataUtil;

public class TpApplySensitiveUtil {

	public static void hidenSensitiveInfo(TpApplyVo tpApplyVo) {
		String alipayAccount = tpApplyVo.getAlipayAccount();
		if (null != alipayAccount) {
			tpApplyVo.setAlipayAccount(SensitiveDataUtil.alipayLogonIdHide(alipayAccount));
		}
		
	}

}
