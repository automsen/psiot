package cn.com.tw.common.core.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqUtils {
	
	@Autowired
	private MqHandler mqHandler;
	
	public void send(Object destinationName, String msg) {
		mqHandler.send(destinationName, msg);
	}
	
	public void sendDelay(Object destinationName, String msg, long delayTimeSeconds) {
		mqHandler.sendDelay(destinationName, msg, delayTimeSeconds);
	}

}
