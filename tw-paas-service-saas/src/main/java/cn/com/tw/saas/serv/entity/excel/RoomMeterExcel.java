package cn.com.tw.saas.serv.entity.excel;

public class RoomMeterExcel {

	private String roomNumber;
	
	private String elecMeterAddr;
	
	private String waterMeterAddr;
	
	private String message;

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber == null ? null : roomNumber.trim();
	}

	public String getElecMeterAddr() {
		return elecMeterAddr;
	}

	public void setElecMeterAddr(String elecMeterAddr) {
		this.elecMeterAddr = elecMeterAddr == null ? null : elecMeterAddr.trim();
	}

	public String getWaterMeterAddr() {
		return waterMeterAddr;
	}

	public void setWaterMeterAddr(String waterMeterAddr) {
		this.waterMeterAddr = waterMeterAddr == null ? null : waterMeterAddr.trim();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
