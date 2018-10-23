package cn.com.tw.paas.service.notify.eum;

public enum NotifyStatusEm {
	
	NOTIFY_SUCCESS("通知成功", "000"),
	
	NOTIFY_SUCCESS_NO_ARRIVE("通知成功,但是消息被拦截,无法到达用户", "001"),

	NOTIFY_FAIL("通知失败", "111"),
	
	NOTIFY_HTTP_FAIL("请求超时,等待重发", "110"),
	
	NOTIFY_HTTP_SUCCESS_BUT_FAIL("通知处理失败,等待重发", "101"), 
	
	NOTIFY_CREATE("通知记录创建", "100");
	
	private String name;
	
	private String value;
	
	NotifyStatusEm(String name, String value){
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
