package com.taobao.cun.admin.web.webx.interceptor;

import com.alibaba.citrus.extension.rpc.RPCRequestContext;
import com.alibaba.citrus.extension.rpc.security.AbstractCSRFCheckInterceptor;
import com.alibaba.citrus.extension.rpc.validation.ErrorContext;
import com.taobao.security.token.webx3.CsrfTokenForWeb3;

/**
 * @author guolei.maogl 2015Äê4ÔÂ14ÈÕ
 */
public class CuntaoCSRFCheckInterceptor extends AbstractCSRFCheckInterceptor {

	@Override
	protected boolean isValidCSRFToken(RPCRequestContext requestContext, ErrorContext errorContext) {
		if (requestContext.isCheckCSRF()) {
			return CsrfTokenForWeb3.check(requestContext.getRequest());
		}

		return true;
	}

}
