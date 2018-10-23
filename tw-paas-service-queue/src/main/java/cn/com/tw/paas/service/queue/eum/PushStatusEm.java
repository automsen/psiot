package cn.com.tw.paas.service.queue.eum;

public enum PushStatusEm {
	
	PUSH_SUCCESS("推送成功", "000"),
	
	PUSH_SUCCESS_NO_ARRIVE("推送成功,但是消息被拦截,无法到达用户", "001"),

	PUSH_FAIL("推送失败", "111"),
	
	PUSH_HTTP_FAIL("请求超时,等待重发", "110"),
	
	PUSH_HTTP_SUCCESS_BUT_FAIL("推送处理失败,等待重发", "101"), 
	
	PUSH_CREATE("推送记录创建", "100");
	
	private String name;
	
	private String value;
	
	PushStatusEm(String name, String value){
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
