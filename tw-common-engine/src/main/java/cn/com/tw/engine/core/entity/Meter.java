package cn.com.tw.engine.core.entity;

import java.io.Serializable;

public class Meter implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String meterAddr;
	
	private String meterType;
	
	private String protocolType;
	
	private String gwId;
	
	public String getMeterAddr() {
		return meterAddr;
	}

	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}


	public String getMeterType() {
		return meterType;
	}

	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}

	public String getGwId() {
		return gwId;
	}

	public void setGwId(String gwId) {
		this.gwId = gwId;
	}

	@Override
	public String toString() {
		return "Meter [meterAddr=" + meterAddr + ", meterType=" + meterType
				+ ", protocolType=" + protocolType + ", gwId=" + gwId + "]";
	}

}
