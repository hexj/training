package com.taobao.cun.admin.web.vo.agentorder;

/**
 * Created by wangbowangb on 15-1-6.
 */
public class AegentOrderListDetailVo {
    private Long agentOrderId;//������id
    private String state;//������״̬
    private String stateDetail;//������״̬����
    private String aegentOrderedTime;//�������µ�ʱ��
    private String buyerName;//�µ�������
    private String buyerMobile;//�µ����ֻ�����
//    private String taobaoUserId;//�µ���taobaoUserId
    private String agentItemId;//������Ʒid
    private String agentPicUrl;//����ͼƬ��url
    private String agentName;//������Ʒ����
    private String agentSkuId;//��Ʒskuid
    private String aegentSku;//��Ʒsku
    private String agentPrice;//��Ʒ�۸�
    private String agentNum;//��Ʒ����
    private String nickName;//�Ա�������,nick

	private String remarks;// ��ע
	private String url;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getAgentOrderId() {
        return agentOrderId;
    }

    public void setAgentOrderId(Long agentOrderId) {
        this.agentOrderId = agentOrderId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateDetail() {
        return stateDetail;
    }

    public void setStateDetail(String stateDetail) {
        this.stateDetail = stateDetail;
    }

    public String getAegentOrderedTime() {
        return aegentOrderedTime;
    }

    public void setAegentOrderedTime(String aegentOrderedTime) {
        this.aegentOrderedTime = aegentOrderedTime;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
    }

//    public String getTaobaoUserId() {
//        return taobaoUserId;
//    }
//
//    public void setTaobaoUserId(String taobaoUserId) {
//        this.taobaoUserId = taobaoUserId;
//    }

    public String getAgentItemId() {
        return agentItemId;
    }

    public void setAgentItemId(String agentItemId) {
        this.agentItemId = agentItemId;
    }

    public String getAgentPicUrl() {
        return agentPicUrl;
    }

    public void setAgentPicUrl(String agentPicUrl) {
        this.agentPicUrl = agentPicUrl;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentSkuId() {
        return agentSkuId;
    }

    public void setAgentSkuId(String agentSkuId) {
        this.agentSkuId = agentSkuId;
    }

    public String getAegentSku() {
        return aegentSku;
    }

    public void setAegentSku(String aegentSku) {
        this.aegentSku = aegentSku;
    }

    public String getAgentPrice() {
        return agentPrice;
    }

    public void setAgentPrice(String agentPrice) {
        this.agentPrice = agentPrice;
    }

    public String getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(String agentNum) {
        this.agentNum = agentNum;
    }
    
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
