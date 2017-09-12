package com.taobao.cun.admin.web.vo;

import java.io.Serializable;

public class PartnerVo implements Serializable {

	private static final long serialVersionUID = 1684473678127470211L;

	private Long taobaoUserId;

	private String applyName;

	private String gender;

	private String idCardNum;

	private String alipayAccount;

	// �־�ס��
	private String manageProvince;  //�־�סʡ��
	private String manageProvinceDetail;
	private String manageCity; //�־�ס����
	private String manageCityDetail; 
	private String manageCounty;  //�־�ס����
	private String manageCountyDetail;
	private String manageTown;//�־�ס�ֵ�
	private String manageTownDetail;
	
	private String manageVillageCode; //�־�ס�� ��
	private String manageVillageDetail;
	
	// �־�ס��
	private String manageAddress;

	// �Ƿ���Ӫҵ����
	private String isExitPlace;


	// Ӫҵ���
	private String manageArea;

	private String mobile;

	private String email;

	private String onlineShopping;

	private String isKnowTaobao;

	// Ӫҵ��ַ
	private String province;  //ʡ��
	private String provinceDetail;
	private String city; //����
	private String cityDetail; 
	private String county;  //����
	private String countyDetail;
	private String town;//�ֵ�
	private String townDetail;
	
	private String stationVillageCode; // Ӫҵ��ַ ��
	private String stationVillageDetail;
	// Ӫҵ��չ��ַ
	private String address;
	
	/**
     * ѧ��
     */
    private String education;
    
    /**
     * ����Ҫ�����
     */
    private String ageRange;
    
    /**
     * ���˿���
     */
    private String population;

    /**
     * ��Ӫ�����ֳ���Ƭ
     */
    private String locationImgs;
    
    /**
     * ��ҵ����Ӫ���ƻ���
     */
    private String busProposal;
    
    /**
     * ��Ҫ��������
     */
    private String workExp;
    
    /**
     * �������Χ
     */
    private String areaRange;
    
    /**
     * ���˾�������
     */
    private String perIncomeRange;
    
    /**
     * ͨ��ʲô�����˽⵽�Ĵ���
     */
    private String fromChannels;
    
    /**
     * ����ƽ̨���۾���
     */
    private String netPlatformExp;

    /**
     * ����
     */
    private String nation;
    
    /**
     * ������ò
     */
    private String politics;
    
    /**
     * ���֤������������ƬJson
     */
    private String idCardImgsFace;
    private String idCardImgsBack;
 
    /**
     * ���֤������ϳ���Ƭ
     */
    private String jointIdCardImg;

    private String networkCoverage;
    
    private String currentStep;
    
	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getIdCardNum() {
		return idCardNum;
	}

	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getManageProvince() {
		return manageProvince;
	}

	public void setManageProvince(String manageProvince) {
		this.manageProvince = manageProvince;
	}

	public String getManageProvinceDetail() {
		return manageProvinceDetail;
	}

	public void setManageProvinceDetail(String manageProvinceDetail) {
		this.manageProvinceDetail = manageProvinceDetail;
	}

	public String getManageCity() {
		return manageCity;
	}

	public void setManageCity(String manageCity) {
		this.manageCity = manageCity;
	}

	public String getManageCityDetail() {
		return manageCityDetail;
	}

	public void setManageCityDetail(String manageCityDetail) {
		this.manageCityDetail = manageCityDetail;
	}

	public String getManageCounty() {
		return manageCounty;
	}

	public void setManageCounty(String manageCounty) {
		this.manageCounty = manageCounty;
	}

	public String getManageCountyDetail() {
		return manageCountyDetail;
	}

	public void setManageCountyDetail(String manageCountyDetail) {
		this.manageCountyDetail = manageCountyDetail;
	}

	public String getManageTown() {
		return manageTown;
	}

	public void setManageTown(String manageTown) {
		this.manageTown = manageTown;
	}

	public String getManageTownDetail() {
		return manageTownDetail;
	}

	public void setManageTownDetail(String manageTownDetail) {
		this.manageTownDetail = manageTownDetail;
	}

	public String getManageVillageCode() {
		return manageVillageCode;
	}

	public void setManageVillageCode(String manageVillageCode) {
		this.manageVillageCode = manageVillageCode;
	}

	public String getManageVillageDetail() {
		return manageVillageDetail;
	}

	public void setManageVillageDetail(String manageVillageDetail) {
		this.manageVillageDetail = manageVillageDetail;
	}

	public String getManageAddress() {
		return manageAddress;
	}

	public void setManageAddress(String manageAddress) {
		this.manageAddress = manageAddress;
	}

	public String getIsExitPlace() {
		return isExitPlace;
	}

	public void setIsExitPlace(String isExitPlace) {
		this.isExitPlace = isExitPlace;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOnlineShopping() {
		return onlineShopping;
	}

	public void setOnlineShopping(String onlineShopping) {
		this.onlineShopping = onlineShopping;
	}

	public String getIsKnowTaobao() {
		return isKnowTaobao;
	}

	public void setIsKnowTaobao(String isKnowTaobao) {
		this.isKnowTaobao = isKnowTaobao;
	}

	public String getManageArea() {
		return manageArea;
	}

	public void setManageArea(String manageArea) {
		this.manageArea = manageArea;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

    
    public String getEducation() {
        return education;
    }

    
    public void setEducation(String education) {
        this.education = education;
    }

    
    public String getAgeRange() {
        return ageRange;
    }

    
    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    
    public String getPopulation() {
        return population;
    }

    
    public void setPopulation(String population) {
        this.population = population;
    }

    
    public String getLocationImgs() {
        return locationImgs;
    }

    
    public void setLocationImgs(String locationImgs) {
        this.locationImgs = locationImgs;
    }

    
    public String getBusProposal() {
        return busProposal;
    }

    
    public void setBusProposal(String busProposal) {
        this.busProposal = busProposal;
    }

    
    public String getWorkExp() {
        return workExp;
    }

    
    public void setWorkExp(String workExp) {
        this.workExp = workExp;
    }

    
    public String getAreaRange() {
        return areaRange;
    }

    
    public void setAreaRange(String areaRange) {
        this.areaRange = areaRange;
    }

    
    public String getPerIncomeRange() {
        return perIncomeRange;
    }

    
    public void setPerIncomeRange(String perIncomeRange) {
        this.perIncomeRange = perIncomeRange;
    }

    
    public String getFromChannels() {
        return fromChannels;
    }

    
    public void setFromChannels(String fromChannels) {
        this.fromChannels = fromChannels;
    }

    
    public String getNetPlatformExp() {
        return netPlatformExp;
    }

    
    public void setNetPlatformExp(String netPlatformExp) {
        this.netPlatformExp = netPlatformExp;
    }

    
    public String getNation() {
        return nation;
    }

    
    public void setNation(String nation) {
        this.nation = nation;
    }

    
    public String getPolitics() {
        return politics;
    }

    
    public void setPolitics(String politics) {
        this.politics = politics;
    }

	public String getJointIdCardImg() {
		return jointIdCardImg;
	}

	public void setJointIdCardImg(String jointIdCardImg) {
		this.jointIdCardImg = jointIdCardImg;
	}

	public String getNetworkCoverage() {
		return networkCoverage;
	}

	public void setNetworkCoverage(String networkCoverage) {
		this.networkCoverage = networkCoverage;
	}

	public String getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}

	public String getIdCardImgsFace() {
		return idCardImgsFace;
	}

	public void setIdCardImgsFace(String idCardImgsFace) {
		this.idCardImgsFace = idCardImgsFace;
	}

	public String getIdCardImgsBack() {
		return idCardImgsBack;
	}

	public void setIdCardImgsBack(String idCardImgsBack) {
		this.idCardImgsBack = idCardImgsBack;
	}

	public String getStationVillageCode() {
		return stationVillageCode;
	}

	public void setStationVillageCode(String stationVillageCode) {
		this.stationVillageCode = stationVillageCode;
	}

	public String getStationVillageDetail() {
		return stationVillageDetail;
	}

	public void setStationVillageDetail(String stationVillageDetail) {
		this.stationVillageDetail = stationVillageDetail;
	}
}
