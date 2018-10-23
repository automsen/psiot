package cn.com.tw.engine.core.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import cn.com.tw.common.core.jms.cons.MqCons;
import cn.com.tw.engine.core.entity.eum.EnumType;

@Component
@ConfigurationProperties(prefix="engine.config")
public class EngineConfig {
	
	/**
	 * 分类对应队列名
	 */
	private Map<String, String> queues = new HashMap<String, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{/** 参数类， 事件类， 电量类， 谐波变量类，需量类，曲线类, 通信状态类 **/
			put(EnumType.param.name(), MqCons.QUEUE_COLLECT_METER);
			put(EnumType.event.name(), MqCons.QUEUE_COLLECT_METER);
			put(EnumType.elec.name(), MqCons.QUEUE_COLLECT_METER);
			put(EnumType.harm_var.name(), MqCons.QUEUE_COLLECT_METER);
			put(EnumType.demand.name(), MqCons.QUEUE_COLLECT_METER);
			put(EnumType.curve.name(), MqCons.QUEUE_COLLECT_METER);
			put(EnumType.alarm.name(), MqCons.QUEUE_DEVICE_EXCEPTION);
			put(EnumType.heart.name(), MqCons.QUEUE_HEARTBEAT_DATA);
		}
	};
	
	/**
	 * 电表发送超时时间
	 */
	private int elecSendTimeout;
	
	/**
	 * 水表发送超时时间
	 */
	private int waterSendTimeout;

	@Override
	public String toString() {
		return "EngineConfig [queues=" + queues + ", elecSendTimeout="
				+ elecSendTimeout + ", waterSendTimeout=" + waterSendTimeout
				+ "]";
	}

	public Map<String, String> getQueues() {
		return queues;
	}

	public void setQueues(Map<String, String> queues) {
		this.queues = queues;
	}

	public int getElecSendTimeout() {
		return elecSendTimeout;
	}

	public void setElecSendTimeout(int elecSendTimeout) {
		this.elecSendTimeout = elecSendTimeout;
	}

	public int getWaterSendTimeout() {
		return waterSendTimeout;
	}

	public void setWaterSendTimeout(int waterSendTimeout) {
		this.waterSendTimeout = waterSendTimeout;
	}
}
