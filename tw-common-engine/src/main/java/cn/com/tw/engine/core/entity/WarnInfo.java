package cn.com.tw.engine.core.entity;

public class WarnInfo {
	
	private String deviceId;
	
	/**
	 * 0901 为网关， 0301为电表 0302为水表
	 */
	private String deviceType;
	
	/**
	 * 0是开 1是关
	 */
	private int status;
	
	public WarnInfo(){
		
	}
	
	public WarnInfo(String deviceId, String deviceType, int status){
		
		this.deviceId = deviceId;
		
		this.deviceType = deviceType;
		
		this.status = status;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "WarnInfo [deviceId=" + deviceId + ", deviceType=" + deviceType
				+ ", status=" + status + "]";
	}

}
