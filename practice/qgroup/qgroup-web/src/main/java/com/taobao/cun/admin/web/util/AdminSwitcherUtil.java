package com.taobao.cun.admin.web.util;

public class AdminSwitcherUtil {
//	/**
//	 * ����̨����������
//	 */
//	private static ConditionalSwitcher<String> cashierFunctionConditionalSwitcher;
//	/**
//	 * ����̨����
//	 */
//	private static Switcher cashierFunctionBasicSwitcher;
//
//	/**
//	 * �ж�֧����Ǯ�����渶�����Ƿ���
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
//		//1 ����̨�������ƿ��ؿ���
//		//2 ����̨����������Ч
//		if (cashierFunctionBasicSwitcher.isSwitchedOn()) {
//			return cashierFunctionConditionalSwitcher.isSwitchingOn(nickname);
//		} else {
//			return true;
//		}
//	}
}
