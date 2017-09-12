package com.taobao.cun.admin.web.vo;

import java.io.Serializable;

public class PayParam implements Serializable{
	
	private static final long serialVersionUID = 11982974293L;
	
	private String orderNo;
	private String mailNo;
	private String paySource;
	private String payMethod;
	private String dynamicPayId;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	public String getPaySource() {
		return paySource;
	}
	public void setPaySource(String paySource) {
		this.paySource = paySource;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getDynamicPayId() {
		return dynamicPayId;
	}
	public void setDynamicPayId(String dynamicPayId) {
		this.dynamicPayId = dynamicPayId;
	}
}
