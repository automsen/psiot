package cn.com.tw.common.enm.notify;

/**
 * 
 * @author admin
 *
 */
public enum NotifyBusTypeEm {
	
	login("登录", 1600),
	
	recharge("充值", 1601),
	
	openAccount("开户", 1602),
	
	qwe_changeEvent("信息变更", 1603),
	
	rechargeError("充值异常", 1604),
	
	//以下错误码 paas平台触发异常，saas平台做回显
	termNoContactError("设备失联异常", 1605),
	
	//指令下发异常，需要指定是什么操作，是充值，通断电还是其他的操作, 需要在data中指定code
	termCmdError("指令下发异常", 1606),
	
	termValError("止码异常", 1607),
	
	termBatterFlowError("水表电池欠压", 1608),
	
	termElecUnderOrOver("电表电压异常", 1609);
	
	NotifyBusTypeEm(String name, int value){
		this.setName(name);
		this.setValue(value);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private String name;
	
	private int value;
	

}