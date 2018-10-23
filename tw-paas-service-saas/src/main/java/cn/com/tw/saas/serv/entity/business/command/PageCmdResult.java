package cn.com.tw.saas.serv.entity.business.command;

import java.io.Serializable;


/**
 * 存储调用接口后的回调信息
 * 用来返回给页面做相关展示操作
 * @author liming
 * 2017年10月12日11:55:53
 *
 */
public class PageCmdResult implements Serializable{

	private static final long serialVersionUID = -5156579023772601137L;
	private String cmdId;
	
	private String cmdName;
	/**
	 * 执行时间
	 */
	private Long exeTime;
	/**
	 * 是否可重试
	 */
	private Boolean isRepeat;
	
	private String meterAddr;
	
	private String errorMsg;
	
	private Boolean success;
	
	private String orderId;

	public String getCmdId() {
		return cmdId;
	}

	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}

	public Long getExeTime() {
		return exeTime;
	}

	public void setExeTime(Long exeTime) {
		this.exeTime = exeTime;
	}

	public Boolean getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(Boolean isRepeat) {
		this.isRepeat = isRepeat;
	}

	public String getMeterAddr() {
		return meterAddr;
	}

	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getCmdName() {
		return cmdName;
	}

	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
