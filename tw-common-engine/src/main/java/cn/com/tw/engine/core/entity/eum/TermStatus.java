package cn.com.tw.engine.core.entity.eum;

public enum TermStatus {
	close(0), open(1);
	
	private int value;
	
	TermStatus(int value){
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
}
