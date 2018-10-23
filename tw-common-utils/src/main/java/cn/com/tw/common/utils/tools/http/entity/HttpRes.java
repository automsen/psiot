package cn.com.tw.common.utils.tools.http.entity;

public class HttpRes {
	
	private String code;
	
	private String data;
	
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "HttpRes [code=" + code + ", data=" + data + ", message="
				+ message + "]";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
