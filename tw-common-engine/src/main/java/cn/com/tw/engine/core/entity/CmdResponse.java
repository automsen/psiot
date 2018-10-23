package cn.com.tw.engine.core.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.com.tw.common.utils.cons.ResultCode;

public class CmdResponse implements Serializable{
	
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
	private Object data;
	
	private String uniqueId;
	
	private Map<String, Object> ext;
	
	public CmdResponse(){
		
	}
	
	public CmdResponse(String code){
		this.statusCode = code;
	}
	
	public CmdResponse(String code, Object data){
		this.statusCode = code;
		this.data = data;
	}
	
	public CmdResponse(String code, String message, Object data){
		this.statusCode = code;
		this.message = message;
		this.data = data;
	}
	
	public static CmdResponse ok(){
		return new CmdResponse(ResultCode.OPERATION_IS_SUCCESS);
	}
	
	public static CmdResponse ok(Object data){
		return new CmdResponse(ResultCode.OPERATION_IS_SUCCESS, data);
	}
	
	public static CmdResponse retn(String code){
		return new CmdResponse(code);
	}
	
	public static CmdResponse retn(String code, String message){
		return new CmdResponse(code, message, null);
	}
	
	public static CmdResponse retn(String code, String message, Object data){
		return new CmdResponse(code, message, data);
	}

	@Override
	public String toString() {
		return "CmdResponse [statusCode=" + statusCode + ", message=" + message
				+ ", data=" + data + ", uniqueId=" + uniqueId + ", ext=" + ext
				+ "]";
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

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public void setExt(String extName, Object value) {
		
		if (ext == null) {
			ext = new HashMap<String, Object>();
		}
		
		ext.put(extName, value);
	}

	public Map<String, Object> getExt() {
		return ext;
	}

	public void setExt(Map<String, Object> ext) {
		this.ext = ext;
	}
	
}
