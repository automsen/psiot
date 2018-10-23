package cn.com.tw.paas.monit.entity.business.ins;

import org.hibernate.validator.constraints.NotEmpty;

import cn.com.tw.paas.monit.entity.business.base.BaseParam;

public class NetMeterData extends BaseParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String netEquipAddr;
	
	@NotEmpty
	private String powerData;

	public String getNetEquipAddr() {
		return netEquipAddr;
	}

	public void setNetEquipAddr(String netEquipAddr) {
		this.netEquipAddr = netEquipAddr;
	}

	public String getPowerData() {
		return powerData;
	}

	public void setPowerData(String powerData) {
		this.powerData = powerData;
	}

}
