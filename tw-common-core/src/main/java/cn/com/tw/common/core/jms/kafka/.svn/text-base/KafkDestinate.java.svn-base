package cn.com.tw.common.core.jms.kafka;

public class KafkDestinate {
	
	private String topic;
	
	private Integer partition;
	
	private String key;
	
	public KafkDestinate () {
	}
	
	public KafkDestinate(String topic, Integer partition, String key) {
		this.topic = topic;
		this.partition = partition;
		this.key = key;
	}
	
	public KafkDestinate (String topic, String key) {
		this(topic, null, key);
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Integer getPartition() {
		return partition;
	}

	public void setPartition(Integer partition) {
		this.partition = partition;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
