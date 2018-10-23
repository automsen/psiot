package cn.com.tw.common.core.jms.kafka;

import java.util.Arrays;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaGroupConsumer{
	
	public static final Logger logger = LoggerFactory.getLogger(KafkaGroupConsumer.class);
	
	private KafkaConsumer<String, String> consumer;
	
	private Map<String, Object> config = null;
	
	public KafkaGroupConsumer(String groupId, Map<String, Object> config) {
		if (config != null) {
			config.put("group.id", groupId);
		} 
		this.setConfig(config);
	    this.consumer = new KafkaConsumer<>(config);
	}
	
	public void subscribe(String[] topics, KafkaGroupConsumerHandler kafkaGroupConsumer){
		 consumer.subscribe(Arrays.asList(topics));
		 
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
			         ConsumerRecords<String, String> records = consumer.poll(100);
			         for (ConsumerRecord<String, String> record : records) {
			        	try {
							kafkaGroupConsumer.listen(record);
						} catch (Exception e) {
							logger.error("{}",e);
						}
			         }
			     }
				
			}
		}).start();
	}

	public Map<String, Object> getConfig() {
		return config;
	}

	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}
}
