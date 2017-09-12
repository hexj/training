package com.taobao.cun.admin.web.home.module;

public enum ErrorCode {

	USERID_NOT_EXISTS("1", "���Ա��˺Ų����ڻ�״̬�쳣�����������˺�ʵ"), 
	USER_NOT_BIND_PAY_ACCOUNT("2", "���Ա��˺�û�а�֧�����˺�"), 
	USER_NOT_AUTH("3", "���Ա��˺�δ����ʵ����֤"), 
	REPEAT_APPLY("4", "�Ѿ��ύ�������������ظ�����"), 
	HAS_BEEN_PARTNER("5", "�Ѿ��Ǻϻ���"), 
	UNDER_AGE_18("6", "����δ��18���꣬��л����עũ���Ա�"), 
	NOT_APPLY("7", "��û�б�������ˢ��ҳ����д��һ��������Ϣ"), 
	FAILED_TO_APPLY("8", "����δ�ɹ�"), 
	
	NAME_IS_NULL("9", "��������Ϊ��"), 
	PAY_ACCOUNT_IS_NULL("10", "֧�����˺Ų���Ϊ��"), 
	IDCARDNUM_IS_NULL("11", "���֤���벻��Ϊ��"), 
	IS_EXIST_PLACE_IS_NULL("12", "Ӫҵ��������Ϊ��"), 

	MOBILE_IS_NULL("14", "��ϵ�绰Ϊ�ջ���Ч"),
	MANAGE_ADDRESS_IS_NULL("15","�־�ס�ز���Ϊ��"),
	GENDER_IS_NULL("16","�Ա���Ϊ��"),
	
	BUSINESS_ADDRESS_PROVINCE_IS_NULL("13", "Ӫҵ��ַ����ѡ��ʡ"), 
	BUSINESS_ADDRESS_CITY_IS_NULL("17", "Ӫҵ��ַ����ѡ����"), 
	BUSINESS_ADDRESS_TOWN_IS_NULL("18", "Ӫҵ��ַ����ѡ����"),
	BUSINESS_ADDRESS_STREET_IS_NULL("19", "�ֵ���ַ����Ϊ��"),
	BUSINESS_ADDRESS_COUNTY_IS_NULL("37", "������ַ����Ϊ��"),
	
	ORG_NAME_IS_INVALID("20","��˾����Ϊ�ջ���Ч"),
	ADDRESS_AREA_IS_INVALID("21","��ѡ��Ӫ��ַ"),
	ADDRESS_STREET_IS_INVALID("22","��ϸ��ַΪ�ջ���Ч"),
	LEAGELPERSON_NAME_IS_INVALID("23","���˴�������Ϊ�ջ���Ч"),
	TEAM_NUMBER_IS_INVALID("24","��ѵ������Ϊ�ջ���Ч"),
	TEACHER_NUMBER_IS_INVALID("25","����ʦ������Ϊ�ջ���Ч"),
	SERVE_AREA_IS_INVALID("26","��������Ϊ�ջ���Ч"),
	BUSSINESS_TYPE_IS_INVALID("27","ҵ��ΧΪ�ջ���Ч"),
	APPLY_NAME_IS_INVALID("28","������Ϊ�ջ���Ч"),
	EMAIL_IS_INVALID("29","����������Ϊ�ջ���Ч"),
	APPLY_JOB_IS_INVALID("30","������ְλΪ�ջ���Ч"),
	NOT_LOGIN("31","���¼������"),
	
	NATION_IS_NULL("32", "���岻��Ϊ��"),
    EDUCATION_IS_INVALID("33","ѧ��Ϊ�ջ���Ч"),
    POLITICS_IS_INVALID("34","������òΪ�ջ���Ч"),
	
	REQUEST_IS_INVALID("35","��Ч������"), 
	
	IDCARDIMG_IS_NULL("36","���֤��������ƬΪ�ջ���Ч"),
	
	MANAGE_AREA_IS_NULL("40","Ӫҵ�������Ϊ��");
	
	
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
