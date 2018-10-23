package cn.com.tw.common.enm.notify;

public enum NotifyPlatTypeEm {
	paas(1), saas(2);
	
	NotifyPlatTypeEm(int value){
		this.setValue(value);
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private int value;
}
