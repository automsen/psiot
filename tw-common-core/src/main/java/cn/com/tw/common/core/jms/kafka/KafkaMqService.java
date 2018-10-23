package cn.com.tw.common.core.jms.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import cn.com.tw.common.core.jms.MqHandler;

@Component
public class KafkaMqService implements MqHandler{
	
	public static final Logger logger = LoggerFactory.getLogger(KafkaMqService.class);
	
	@Autowired
	private KafkaTemplate<String, String> template;

	@Override
	public void send(Object destinationName, String msg) {
		
		try {
			if (destinationName instanceof KafkDestinate) {
				KafkDestinate kafkDest = (KafkDestinate) destinationName;
				if (kafkDest.getPartition() == null) {
					template.send(kafkDest.getTopic(), kafkDest.getKey(), msg);
				}else {
					template.send(kafkDest.getTopic(), kafkDest.getPartition(), kafkDest.getKey(), msg);
				}
			} else {
				template.send((String)destinationName, msg);
			}
		} catch (Exception e) {
			logger.error("{}",e);
		}
	}

	@Override
	public void sendDelay(Object destinationName, String msg,
			long delayTimeSeconds) {
		try {
			template.send((String) destinationName, msg);
		} catch (Exception e) {
			logger.error("{}",e);
		}
	}

}
