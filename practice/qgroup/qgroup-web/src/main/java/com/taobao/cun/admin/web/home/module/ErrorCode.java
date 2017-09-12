package com.taobao.cun.admin.web.home.module;

public enum ErrorCode {

	USERID_NOT_EXISTS("1", "该淘宝账号不存在或状态异常，请与申请人核实"), 
	USER_NOT_BIND_PAY_ACCOUNT("2", "该淘宝账号没有绑定支付宝账号"), 
	USER_NOT_AUTH("3", "该淘宝账号未进行实名认证"), 
	REPEAT_APPLY("4", "已经提交过报名，不能重复报名"), 
	HAS_BEEN_PARTNER("5", "已经是合伙人"), 
	UNDER_AGE_18("6", "您尚未满18周岁，感谢您关注农村淘宝"), 
	NOT_APPLY("7", "还没有报名，请刷新页面填写第一步基本信息"), 
	FAILED_TO_APPLY("8", "报名未成功"), 
	
	NAME_IS_NULL("9", "姓名不能为空"), 
	PAY_ACCOUNT_IS_NULL("10", "支付宝账号不能为空"), 
	IDCARDNUM_IS_NULL("11", "身份证号码不能为空"), 
	IS_EXIST_PLACE_IS_NULL("12", "营业场所不能为空"), 

	MOBILE_IS_NULL("14", "联系电话为空或无效"),
	MANAGE_ADDRESS_IS_NULL("15","现居住地不能为空"),
	GENDER_IS_NULL("16","性别不能为空"),
	
	BUSINESS_ADDRESS_PROVINCE_IS_NULL("13", "营业地址必须选择省"), 
	BUSINESS_ADDRESS_CITY_IS_NULL("17", "营业地址必须选择市"), 
	BUSINESS_ADDRESS_TOWN_IS_NULL("18", "营业地址必须选择镇"),
	BUSINESS_ADDRESS_STREET_IS_NULL("19", "街道地址不能为空"),
	BUSINESS_ADDRESS_COUNTY_IS_NULL("37", "县区地址不能为空"),
	
	ORG_NAME_IS_INVALID("20","公司名称为空或无效"),
	ADDRESS_AREA_IS_INVALID("21","请选择经营地址"),
	ADDRESS_STREET_IS_INVALID("22","详细地址为空或无效"),
	LEAGELPERSON_NAME_IS_INVALID("23","法人代表姓名为空或无效"),
	TEAM_NUMBER_IS_INVALID("24","培训团人数为空或无效"),
	TEACHER_NUMBER_IS_INVALID("25","自由师资人数为空或无效"),
	SERVE_AREA_IS_INVALID("26","服务区域为空或无效"),
	BUSSINESS_TYPE_IS_INVALID("27","业务范围为空或无效"),
	APPLY_NAME_IS_INVALID("28","申请人为空或无效"),
	EMAIL_IS_INVALID("29","申请人邮箱为空或无效"),
	APPLY_JOB_IS_INVALID("30","申请人职位为空或无效"),
	NOT_LOGIN("31","请登录后再试"),
	
	NATION_IS_NULL("32", "民族不能为空"),
    EDUCATION_IS_INVALID("33","学历为空或无效"),
    POLITICS_IS_INVALID("34","政治面貌为空或无效"),
	
	REQUEST_IS_INVALID("35","无效的请求"), 
	
	IDCARDIMG_IS_NULL("36","身份证正反面照片为空或无效"),
	
	MANAGE_AREA_IS_NULL("40","营业面积不能为空");
	
	
	private String errorCode;

	private String errorDesc;

	ErrorCode(String errorCode, String errorDesc) {
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

}
