package com.taobao.cun.admin.web.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Trust Partner
 * @author jianke.ljk
 */
public class TpApplyVo implements Serializable{
	
	private static final long serialVersionUID = -5934011839452126832L;
	
	private Long taobaoUserId;
	/** 支付宝账号 */
	private String alipayAccount;
	/** 支付宝用户id */
	private Long alipayUserId;
	/** 企业名称 */
	private String orgName;	
	/** 4级地址 */
	private String province;
	private String provinceDetail;
	private String city;
	private String cityDetail;
	private String county;
	private String countyDetail;
	private String town;
	private String townDetail;
	/** 详细地址 */
	private String address;	
	/** 公司所在地邮编 */
	private String addressDetailCode;
	/** 法人代表 */
	private String legalPersonName;
	/** 培训团队人数 */
	private Integer teamNum;
	/** 自有师资人数 */
	private Integer teacherNum;
	/** 服务区域 */
	private String serveArea;
	/** 业务范围 */
	private String businessType;

	/** 申请人 */
	private String applyerName;
	/** 申请人职位 */
	private String applyerJob;
	/** 联系电话 */
	private String tel;
	/** 邮箱 */
	private String email;
	/** 申请进度状态 */
	private String status;
	
	
	/** 营业执照列表 */
	private List<FileUploadVo> businessLicenceList;
	/** 其他附件列表 */
	private List<FileUploadVo> attachmentList;
	private String businessLicenceListStr;
	private String attachmentListStr;

	
	public Long getTaobaoUserId() {
		return taobaoUserId;
	}
	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}
	public String getAlipayAccount() {
		return alipayAccount;
	}
	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}
	
	public Long getAlipayUserId() {
		return alipayUserId;
	}
	public void setAlipayUserId(Long alipayUserId) {
		this.alipayUserId = alipayUserId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getProvinceDetail() {
		return provinceDetail;
	}
	public void setProvinceDetail(String provinceDetail) {
		this.provinceDetail = provinceDetail;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityDetail() {
		return cityDetail;
	}
	public void setCityDetail(String cityDetail) {
		this.cityDetail = cityDetail;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getCountyDetail() {
		return countyDetail;
	}
	public void setCountyDetail(String countyDetail) {
		this.countyDetail = countyDetail;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getTownDetail() {
		return townDetail;
	}
	public void setTownDetail(String townDetail) {
		this.townDetail = townDetail;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressDetailCode() {
		return addressDetailCode;
	}
	public void setAddressDetailCode(String addressDetailCode) {
		this.addressDetailCode = addressDetailCode;
	}
	public String getLegalPersonName() {
		return legalPersonName;
	}
	public void setLegalPersonName(String legalPersonName) {
		this.legalPersonName = legalPersonName;
	}
	
	public Integer getTeamNum() {
		return teamNum;
	}
	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}
	public Integer getTeacherNum() {
		return teacherNum;
	}
	public void setTeacherNum(Integer teacherNum) {
		this.teacherNum = teacherNum;
	}
	public String getServeArea() {
		return serveArea;
	}
	public void setServeArea(String serveArea) {
		this.serveArea = serveArea;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getApplyerName() {
		return applyerName;
	}
	public void setApplyerName(String applyerName) {
		this.applyerName = applyerName;
	}
	public String getApplyerJob() {
		return applyerJob;
	}
	public void setApplyerJob(String applyerJob) {
		this.applyerJob = applyerJob;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<FileUploadVo> getBusinessLicenceList() {
		return businessLicenceList;
	}
	public void setBusinessLicenceList(List<FileUploadVo> businessLicenceList) {
		this.businessLicenceList = businessLicenceList;
	}
	public List<FileUploadVo> getAttachmentList() {
		return attachmentList;
	}
	public void setAttachmentList(List<FileUploadVo> attachmentList) {
		this.attachmentList = attachmentList;
	}
	public String getBusinessLicenceListStr() {
		return businessLicenceListStr;
	}
	public void setBusinessLicenceListStr(String businessLicenceListStr) {
		this.businessLicenceListStr = businessLicenceListStr;
	}
	public String getAttachmentListStr() {
		return attachmentListStr;
	}
	public void setAttachmentListStr(String attachmentListStr) {
		this.attachmentListStr = attachmentListStr;
	}

	
	

}
