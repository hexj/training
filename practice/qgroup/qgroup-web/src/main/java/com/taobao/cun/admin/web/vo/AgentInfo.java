package com.taobao.cun.admin.web.vo;

import java.io.Serializable;

/**
 * @author guolei.maogl 2015Äê3ÔÂ11ÈÕ
 */
public class AgentInfo implements Serializable {
	private static final long serialVersionUID = -502457654898087567L;

	private String stationName;
	private String name;
	private String taobaoNick;
	private String url;

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
