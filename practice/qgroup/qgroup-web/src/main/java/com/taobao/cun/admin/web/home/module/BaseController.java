package com.taobao.cun.admin.web.home.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.admin.web.util.WebUtil;
import com.taobao.cun.dto.HavanaContext;

public class BaseController {

	@Autowired
	protected HttpSession session;
	
	@Autowired
	protected HttpServletRequest request;

	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	protected boolean isLogin() {
		return StringUtil.isNotBlank(WebUtil.getLoginId(session));
	}
	
	protected Long getUserId(){
		return WebUtil.getUserId(session);
	}
	
	protected String getLoginId(){
		return WebUtil.getLoginId(session);
	}

	protected HavanaContext buildHavanaContext() {
		HavanaContext context = new HavanaContext();
		context.setLoginId(getLoginId());
		context.setTaobaoUserId("" + getUserId());
		return context;
	}
}
