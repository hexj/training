package com.taobao.cun.admin.web.partner.module.util;

import org.apache.commons.lang.StringUtils;

import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.SystemTypeEnum;
import com.taobao.cun.dto.UserTypeEnum;
import com.taobao.cun.dto.partner.PartnerApplyDto;
import com.taobao.cun.dto.partner.PartnerApplyStateEnum;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;

/**
 * ֻ��sceen��rpc�Ĺ�������Ĺ�����
 * 
 * @author haihu.fhh
 * 
 */
public final class PartnerUtil {

	//������֤
	private static final int ALIPAY_PSERON_PROMOTED_TYPE = 512;
	private static final int ALIPAY_COMPANY_PROMOTED_TYPE = 4;


	/**
	 * �Ƿ���й�����ʵ����֤
	 * 
	 * @param baseUserDO
	 * @return
	 */
	public static boolean isNotRealNameAuth(BaseUserDO baseUserDO) {
		int promotedType = baseUserDO.getPromotedType();
		return (((promotedType & ALIPAY_PSERON_PROMOTED_TYPE) != ALIPAY_PSERON_PROMOTED_TYPE) || StringUtils.isBlank(baseUserDO.getFullname()) || StringUtils
				.isBlank(baseUserDO.getIdCardNumber()));
	}
	/**
	 * �Ƿ���й���ҵʵ����֤
	 * @param baseUserDO
	 * @return
	 */
	public static boolean isCompanyRealNameAuth(BaseUserDO baseUserDO){
		int promotedType = baseUserDO.getPromotedType();
		return (promotedType & ALIPAY_COMPANY_PROMOTED_TYPE )== ALIPAY_COMPANY_PROMOTED_TYPE;
	}

	/**
	 * �ж��Ƿ��֧�����˺�
	 * 
	 * @param result
	 * @return
	 */
	public static boolean hasBindToPayAccount(ResultDO<BasePaymentAccountDO> result) {
		return null != result && result.isSuccess() && null != result.getModule();
	}

	/**
	 * �Ѿ�������,��û�����ͨ����
	 */
	public static boolean hasEnrolled(PartnerApplyDto existPartner) {
		return null != existPartner && (!existPartner.getPartnerApplyStateEnum().equals(PartnerApplyStateEnum.STATE_APPLY_SUCC));
	}

	/**
	 * �Ѿ��Ǻϻ���
	 * 
	 * @return
	 */
	public static boolean hasBeenPartner(PartnerApplyDto existPartner) {
		return null != existPartner && (existPartner.getPartnerApplyStateEnum().equals(PartnerApplyStateEnum.STATE_APPLY_SUCC));
	}

	public static ContextDto assemberContext(Long loginId) {
		ContextDto context = new ContextDto();

		context.setLoginId(String.valueOf(loginId));
		context.setAppId(String.valueOf(loginId));
		context.setUserType(UserTypeEnum.HAVANA);
		context.setSystemType(SystemTypeEnum.CUNTAO_ADMIN);
		return context;
	}

	private PartnerUtil() {

	}
}
