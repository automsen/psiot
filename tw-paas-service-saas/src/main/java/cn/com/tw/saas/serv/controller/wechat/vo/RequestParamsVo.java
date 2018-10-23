package cn.com.tw.saas.serv.controller.wechat.vo;

import java.io.Serializable;

public class RequestParamsVo implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1063035168848834410L;

	private String customerId;
	
	private String meterAddr;
	
	private String energyType;
	
	private String startTime;
	
	private String endTime;
	
	private String freezeTd;
	
	private String status;
	
	private String dataItem;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getMeterAddr() {
		return meterAddr;
	}

	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}

	public String getEnergyType() {
		return energyType;
	}

	public void setEnergyType(String energyType) {
		this.energyType = energyType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFreezeTd() {
		return freezeTd;
	}

	public void setFreezeTd(String freezeTd) {
		this.freezeTd = freezeTd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDataItem() {
		return dataItem;
	}

	public void setDataItem(String dataItem) {
		this.dataItem = dataItem;
	}

}
