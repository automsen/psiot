package cn.com.tw.common.web.entity;

import java.io.Serializable;

import cn.com.tw.common.utils.cons.ResultCode;

public class Response<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * code
	 */
	private String statusCode;
	
	/**
	 * 错误描述
	 */
	private String message;
	
	/**
	 * 数据
	 */
	private T data;
	
	public Response(){
		
	}
	
	public Response(String code){
		this.statusCode = code;
	}
	
	public Response(String code, T data){
		this.statusCode = code;
		this.data = data;
	}
	
	public Response(String code, String message, T data){
		this.statusCode = code;
		this.message = message;
		this.data = data;
	}
	
	@SuppressWarnings("rawtypes")
	public static Response ok(){
		return new Response(ResultCode.OPERATION_IS_SUCCESS);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Response ok(Object data){
		return new Response(ResultCode.OPERATION_IS_SUCCESS, data);
	}
	
	@SuppressWarnings("rawtypes")
	public static Response retn(String code){
		return new Response(code);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Response retn(String code, String message){
		return new Response(code, message, null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Response retn(String code, String message, Object data){
		return new Response(code, message, data);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Response retnRemoteError(){
		return new Response(ResultCode.REMOTE_CALL_ERROR, "remote call error!!!", null);
	}

	@Override
	public String toString() {
		return "Response [statusCode=" + statusCode + ", data=" + data + "]";
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
