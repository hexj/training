package com.taobao.cun.admin.web.vo;
/**
 * 
 * @author linguozhanglg
 *  
 */
public class PartnerOrderStatusVo {
	private String picUrl ; //ͷ��url
	private String nickName; //�Ա�nick
	private String cunName;  //���������
	/**
	 * ����������
	 */
	private int notPayCount;

	/**
	 * ��������תurl
	 */
	private String notPayUrl;

	/**
	 * ����������
	 */
	private int unConsignedCount;

	/**
	 * ��������תurl
	 */
	private String unConsignedUrl;

	/**
	 * ���ջ�����
	 */
	private int consignedCount;

	/**
	 * ���ջ���תurl
	 */
	private String consignedUrl;

	/**
	 * ����������
	 */
	private int notRateCount;

	/**
	 * ��������תurl
	 */
	private String notRateUrl;
	
	
	/**
	 * �˿�����
	 */
	private int refundCount;

	/**
	 * �˿���תurl
	 */
	private String refundUrl;

	/**
	 * �ҵ��Ա�������תurl
	 */
	private String myTaobaoOrderUrl;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCunName() {
		return cunName;
	}

	public void setCunName(String cunName) {
		this.cunName = cunName;
	}

	public int getNotPayCount() {
		return notPayCount;
	}

	public void setNotPayCount(int notPayCount) {
		this.notPayCount = notPayCount;
	}

	public String getNotPayUrl() {
		return notPayUrl;
	}

	public void setNotPayUrl(String notPayUrl) {
		this.notPayUrl = notPayUrl;
	}

	public int getUnConsignedCount() {
		return unConsignedCount;
	}

	public void setUnConsignedCount(int unConsignedCount) {
		this.unConsignedCount = unConsignedCount;
	}

	public String getUnConsignedUrl() {
		return unConsignedUrl;
	}

	public void setUnConsignedUrl(String unConsignedUrl) {
		this.unConsignedUrl = unConsignedUrl;
	}

	public int getConsignedCount() {
		return consignedCount;
	}

	public void setConsignedCount(int consignedCount) {
		this.consignedCount = consignedCount;
	}

	public String getConsignedUrl() {
		return consignedUrl;
	}

	public void setConsignedUrl(String consignedUrl) {
		this.consignedUrl = consignedUrl;
	}

	public int getNotRateCount() {
		return notRateCount;
	}

	public void setNotRateCount(int notRateCount) {
		this.notRateCount = notRateCount;
	}

	public String getNotRateUrl() {
		return notRateUrl;
	}

	public void setNotRateUrl(String notRateUrl) {
		this.notRateUrl = notRateUrl;
	}

	public int getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(int refundCount) {
		this.refundCount = refundCount;
	}

	public String getRefundUrl() {
		return refundUrl;
	}

	public void setRefundUrl(String refundUrl) {
		this.refundUrl = refundUrl;
	}

	public String getMyTaobaoOrderUrl() {
		return myTaobaoOrderUrl;
	}

	public void setMyTaobaoOrderUrl(String myTaobaoOrderUrl) {
		this.myTaobaoOrderUrl = myTaobaoOrderUrl;
	}

}
