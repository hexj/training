package com.taobao.cun.admin.web.vo;

import java.io.Serializable;

/**
 * @author guolei.maogl 2014��12��9��
 */
public class SubjectItemVo implements Serializable {
	private static final long serialVersionUID = 1401220822087855112L;

	private String itemId;
	private String itemName;
	/**
	 * ITEM_AMOUNT ��Ŀ���
	 */
	private String itemAmount = "0";
	/**
	 * INCOME_RATE ������ռ����
	 */
	private String incomeRate;

	private String comment;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(String itemAmount) {
		this.itemAmount = itemAmount;
	}

	public String getIncomeRate() {
		return incomeRate;
	}

	public void setIncomeRate(String incomeRate) {
		this.incomeRate = incomeRate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
