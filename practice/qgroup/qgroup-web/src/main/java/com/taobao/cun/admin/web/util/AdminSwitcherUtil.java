package com.taobao.cun.admin.web.util;

public class AdminSwitcherUtil {
//	/**
//	 * 收银台白名单开关
//	 */
//	private static ConditionalSwitcher<String> cashierFunctionConditionalSwitcher;
//	/**
//	 * 收银台开关
//	 */
//	private static Switcher cashierFunctionBasicSwitcher;
//
//	/**
//	 * 判断支付宝钱包当面付功能是否开启
//	 *
//	 * @param nickname
//	 * @return
//	 */
//	public static boolean isAlipayFeatureOn(String nickname) {
//		if (nickname == null) {
//			return false;
//		}
//		if (cashierFunctionConditionalSwitcher == null) {
//			cashierFunctionConditionalSwitcher = (ConditionalSwitcher<String>) SpringContext.getBean("cashierFunctionConditionalSwitcher");
//		}
//		if (cashierFunctionBasicSwitcher == null) {
//			cashierFunctionBasicSwitcher = (Switcher) SpringContext.getBean("cashierFunctionBasicSwitcher");
//		}
//		//1 收银台开关限制开关开启
//		//2 收银台白名单才生效
//		if (cashierFunctionBasicSwitcher.isSwitchedOn()) {
//			return cashierFunctionConditionalSwitcher.isSwitchingOn(nickname);
//		} else {
//			return true;
//		}
//	}
}
