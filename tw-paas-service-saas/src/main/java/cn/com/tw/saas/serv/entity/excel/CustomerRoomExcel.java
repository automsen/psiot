package cn.com.tw.saas.serv.entity.excel;

public class CustomerRoomExcel {

	private String roomNumber;
	
	private String customerName;
	
	private String customerPhone;
	
	private String customerNumber;
	
	private String message;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName == null ? null : customerName.trim();
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone == null ? null : customerPhone.trim();
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber == null ? null : customerNumber.trim();
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber == null ? null : roomNumber.trim();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
