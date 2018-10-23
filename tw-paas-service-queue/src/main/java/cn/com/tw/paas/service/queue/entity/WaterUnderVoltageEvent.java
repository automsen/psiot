package cn.com.tw.paas.service.queue.entity;

public class WaterUnderVoltageEvent extends Event{

	private String voltageValue;

	public String getVoltageValue() {
		return voltageValue;
	}

	public void setVoltageValue(String voltageValue) {
		this.voltageValue = voltageValue;
	}
	
	@Override
	public String toString() {
		return "WaterUnderVoltageEvent [voltageValue=" + voltageValue
				+ ", getTermNo()=" + getTermNo() + ", getOrgId()=" + getOrgId()
				+ ", getpUrl()=" + getpUrl() + ", getTermType()="
				+ getTermType() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
}
