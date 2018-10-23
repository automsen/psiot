package cn.com.tw.paas.monit.service.inn;

import cn.com.tw.paas.monit.service.inn.idl.CmdHandleErrorCode;

public class CmdHandleException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code = "000997";
	private String message;

	public CmdHandleException() {
	}

	public CmdHandleException(String message) {
		super(message);
		this.message = message;
	}

	public CmdHandleException(String code, String message) {
		super(message);
		this.message = message;
		this.code = code;
	}
	public CmdHandleException(CmdHandleErrorCode code) {
		super(code.getMsg());
		this.message = code.getMsg();
		this.code = code.getCode();
	}
	

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
