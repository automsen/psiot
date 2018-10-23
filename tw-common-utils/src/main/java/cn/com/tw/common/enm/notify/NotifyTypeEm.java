package cn.com.tw.common.enm.notify;

/**
 * 
 * @author admin
 *
 */
public enum NotifyTypeEm {
	
	SMS("短信", 10),
	
	EMAIL("邮件", 20),
	
	WECHAT("微信", 30),
	
	PLAT("平台推送", 40);
	
	NotifyTypeEm(String name, int value){
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