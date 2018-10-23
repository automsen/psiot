package cn.com.tw.paas.monit.service.inn.idl;

public enum CmdHandleErrorCode {

	/**
	 *   命令相关指令不存在
	 */
	innsBeNull("012660","命令相关指令不存在!"),
	/**
	 *  设备网关信息不存在
	 */
	gateWayBeNull("012661","网关信息不存在!"),
	/**
	 *  正在执行中，请不要重复下发
	 */
	repeatCmdHandle("012666","正在执行中，请勿重复操作！"),
	/**
	 *  有关闸命令未完成不能执行开闸动作
	 */
	closeError("012663","开闸命令正在执行中,请稍后执行关闸操作！"),
	/**
	 *  有开闸命令未完成不能执行关闸动作
	 */
	openError("012664","关闸命令正在执行中,请稍后执行开闸操作！"),
	/**
	 *  系统异常
	 */
	unknowError("000999","系统异常！");
	
	private String code;
	
	private String msg;
	
	private CmdHandleErrorCode(String code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
