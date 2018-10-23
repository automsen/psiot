package cn.com.tw.common.protocol.dlt645.cons;

public enum Dlt645DataMarker {

	EncryptControl("ABABAB"),
	EncryptPay("0701"),
	ON("1C"),OFF("1A");
	
	private String value;
	
	Dlt645DataMarker(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
