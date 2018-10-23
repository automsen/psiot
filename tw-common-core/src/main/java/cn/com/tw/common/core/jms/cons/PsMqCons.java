package cn.com.tw.common.core.jms.cons;

public class PsMqCons {
	
	/**
	 * 短信，邮件，推送
	 */
	public final static String QUEUE_NOTIFY_NAME = "ps.queue.notify.all";
	
	/**
	 * 仪表数据
	 */
	public final static String QUEUE_COLLECT_METER = "paas.queue.meter.data";
	
	/**
	 * 设备监控数据
	 */
	public final static String QUEUE_DEVICE_EXCEPTION = "paas.queue.device.monit";
	
	/**
	 * 日志数据
	 */
	public final static String QUEUE_LOG_OPERA = "ps.queue.log.info";
	
	/**
	 * 数据推送，重试队列
	 */
	public final static String QUEUE_PUSH_RETRY = "paas.queue.push.retry";
	
	/**
	 * 设备监控数据
	 */
	public final static String QUEUE_HEARTBEAT_DATA = "paas.queue.heartbeat.data";

}
