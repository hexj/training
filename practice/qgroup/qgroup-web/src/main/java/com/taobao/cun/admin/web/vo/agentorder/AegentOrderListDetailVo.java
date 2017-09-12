package com.taobao.cun.admin.web.vo.agentorder;

/**
 * Created by wangbowangb on 15-1-6.
 */
public class AegentOrderListDetailVo {
    private Long agentOrderId;//代购单id
    private String state;//代购单状态
    private String stateDetail;//代购单状态描述
    private String aegentOrderedTime;//代购单下单时间
    private String buyerName;//下单人姓名
    private String buyerMobile;//下单人手机号码
//    private String taobaoUserId;//下单人taobaoUserId
    private String agentItemId;//代购商品id
    private String agentPicUrl;//代购图片的url
    private String agentName;//代购商品名称
    private String agentSkuId;//商品skuid
    private String aegentSku;//商品sku
    private String agentPrice;//商品价格
    private String agentNum;//商品数量
    private String nickName;//淘宝旺旺号,nick

	private String remarks;// 备注
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
