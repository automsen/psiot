package cn.com.tw.common.protocol.dlt645.cons;

public enum Dlt645ControlCode {
	
	READ("11"),READ_OK("91"),
	READ_CONTINUE("B1"),READ_ERR("D1"),
	READ_FOLLOW("12"),READ_FOLLOW_OVER("92"),
	READ_FOLLOW_CONTINUE("B2"),READ_FOLLOW_ERR("D2"),
	WRITE("14"),WRITE_OK("94"),WRITE_ERR("D4"),
	CONTROL("1C"),CONTROL_OK("9C"),CONTROL_ERR("DC"),
	ENCRYPT("03"),ENCRYPT_OK("83"),ENCRYPT_ERR("C3"),
	MODBUSDOWN("0D"),MODBUSUP("8D"),MODBUSUP_ERR("CD");
	
	private String value;
	
	Dlt645ControlCode(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
