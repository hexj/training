package com.taobao.cun.admin.web.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author guolei.maogl 2014��12��9��
 */
public class BillVo implements Serializable {
	private static final long serialVersionUID = 4931569252031517264L;
	private Long taobaoUserId;
	private Long cuntaoStationId;
	private String cuntaoStationName;
	private String billPeriod;// ����
	private String afterTaxAmount;// ˰���ܶ�
	private String preTaxAmount;// ˰ǰ�ܶ�
	private String deductionAmount;// �ۼ��ܶ�
	private SubjectItemVo tax;// ˰��
	private List<SubjectItemVo> subjectItemList;

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Long getCuntaoStationId() {
		return cuntaoStationId;
	}

	public void setCuntaoStationId(Long cuntaoStationId) {
		this.cuntaoStationId = cuntaoStationId;
	}

	public String getCuntaoStationName() {
		return cuntaoStationName;
	}

	public void setCuntaoStationName(String cuntaoStationName) {
		this.cuntaoStationName = cuntaoStationName;
	}

	public String getBillPeriod() {
		return billPeriod;
	}

	public void setBillPeriod(String billPeriod) {
		this.billPeriod = billPeriod;
	}

	public String getAfterTaxAmount() {
		return afterTaxAmount;
	}

	public void setAfterTaxAmount(String afterTaxAmount) {
		this.afterTaxAmount = afterTaxAmount;
	}

	public String getPreTaxAmount() {
		return preTaxAmount;
	}

	public void setPreTaxAmount(String preTaxAmount) {
		this.preTaxAmount = preTaxAmount;
	}

	public String getDeductionAmount() {
		return deductionAmount;
	}

	public void setDeductionAmount(String deductionAmount) {
		this.deductionAmount = deductionAmount;
	}

	public SubjectItemVo getTax() {
		return tax;
	}

	public void setTax(SubjectItemVo tax) {
		this.tax = tax;
	}

	public List<SubjectItemVo> getSubjectItemList() {
		return subjectItemList;
	}

	public void setSubjectItemList(List<SubjectItemVo> subjectItemList) {
		this.subjectItemList = subjectItemList;
	}

}
