package cn.com.tw.saas.serv.common.utils.cons;

public enum RequestType {
	wxReq("wxReq"),
	appReq("appReq"),
	nativeReq("nativeReq");
	
	private String value;
	
	private RequestType(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}