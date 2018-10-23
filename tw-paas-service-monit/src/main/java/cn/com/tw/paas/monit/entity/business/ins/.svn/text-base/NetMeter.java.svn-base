package cn.com.tw.paas.monit.entity.business.ins;

import org.hibernate.validator.constraints.NotEmpty;

import cn.com.tw.paas.monit.entity.business.base.BaseParam;

public class NetMeter extends BaseParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String netEquipAddr;
	
	private String commAddrs;
	
	private String time;

	public String getNetEquipAddr() {
		return netEquipAddr;
	}

	public void setNetEquipAddr(String netEquipAddr) {
		this.netEquipAddr = netEquipAddr;
	}

	public String getCommAddrs() {
		return commAddrs;
	}

	public void setCommAddrs(String commAddrs) {
		this.commAddrs = commAddrs;
	}

	@Override
	public String toString() {
		return "NetMeter [netEquipAddr=" + netEquipAddr + ", commAddrs="
				+ commAddrs + ", time=" + time + "]";
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
