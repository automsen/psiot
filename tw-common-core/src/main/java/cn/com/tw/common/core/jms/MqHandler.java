package cn.com.tw.common.core.jms;

public interface MqHandler {
	
	void send(Object destinationName, String msg);
	
	void sendDelay(Object destinationName, String msg, long delayTimeSeconds);

}
