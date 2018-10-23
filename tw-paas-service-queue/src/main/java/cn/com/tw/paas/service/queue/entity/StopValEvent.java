package cn.com.tw.paas.service.queue.entity;

public class StopValEvent extends Event {
	
	/**
	 * 新止码
	 */
	private String newVal;
	
	/**
	 * 止码缩写标识
	 */
	private String shortCode;

	public String getNewVal() {
		return newVal;
	}

	public void setNewVal(String newVal) {
		this.newVal = newVal;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	@Override
	public String toString() {
		return "StopValEvent [newVal=" + newVal + ", shortCode=" + shortCode
				+ ", getTermNo()=" + getTermNo() + ", getOrgId()=" + getOrgId()
				+ ", getpUrl()=" + getpUrl() + ", getTermType()="
				+ getTermType() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
