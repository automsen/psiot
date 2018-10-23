package cn.com.tw.common.exception;

import cn.com.tw.common.utils.cons.ResultCode;

public class RequestParamValidException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 错误码
	 */
	private String code = ResultCode.PARAM_VALID_ERROR;
	
	private Object info;
	
	/**
	 * 消息
	 */
    private String message;  //异常对应的描述信息
     
    public RequestParamValidException() {
        super();
    }
 
    public RequestParamValidException(String message) {
        super(message);
        this.message = message;
    }
    
    public RequestParamValidException(Object info, String message) {
        super(message);
        this.message = message;
        this.setInfo(info);
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}
    
}
