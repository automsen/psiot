package cn.com.tw.paas.monit.entity.business.ins;

import cn.com.tw.paas.monit.entity.business.base.BaseParam;

public class DirectDataItem extends BaseParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String commAddrs;
	
	private String powerdata;

	public String getCommAddrs() {
		return commAddrs;
	}

	public void setCommAddrs(String commAddrs) {
		this.commAddrs = commAddrs;
	}

	public String getPowerdata() {
		return powerdata;
	}

	public void setPowerdata(String powerdata) {
		this.powerdata = powerdata;
	}

	@Override
	public String toString() {
		return "DirectDataItem [commAddrs=" + commAddrs + ", powerdata="
				+ powerdata + "]";
	}

}
