package cn.com.tw.paas.service.queue.customer;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.utils.tools.queue.AsyncTaskPool;
import cn.com.tw.paas.service.queue.service.ReadDataService;
import cn.com.tw.paas.service.queue.service.TerminalService;
import cn.com.tw.paas.service.queue.thread.PushMsgThread;

@Component
public class KfkConsumer {
	
	private static Logger logger = LoggerFactory.getLogger(KfkConsumer.class);

	@Autowired
	private ReadDataService readService;
	
	@Autowired
	private MqHandler mqHandler;
	
	@Value("${http.proxy.hostname:}")
	private String hostname;
	
	@Value("${http.proxy.port:0}")
	private int port;
	
	@Autowired
	private TerminalService terminalService;
	
	@KafkaListener(topics = "${mq.queue.push.data}")
	public void listenT2(ConsumerRecord<?, ?> cr){
		logger.info("------>> queue-push {} - {} : {}", cr.topic(), cr.key(), cr.value());
		String msg = (String) cr.value();
		//请求时间
		AsyncTaskPool.getInstance().excuteTask(new PushMsgThread(readService, mqHandler, msg).setProxy(hostname, port));
			
	}
	
	//"${mq.queue.net.status}"
	@KafkaListener(topics = "${mq.queue.net.status}")
	public void listenStatus(ConsumerRecord<?, ?> cr){
		
		logger.info("------>> queue-status {} - {} : {}", cr.topic(), cr.key(), cr.value());
		
		try {
			String msg = (String) cr.value();
			
			// 接收到的msg转为map格式
			@SuppressWarnings("unchecked")
			Map<String, Object> msgMap = JsonUtils.jsonToPojo(msg, Map.class);
			String diveceId = (String) msgMap.get("diveceId");
			String status = (String) msgMap.get("status");
			if(!StringUtils.isEmpty(diveceId)&&!StringUtils.isEmpty(status)
					&&(status.equals("1")||status.equals("0"))){
				logger.debug("<<<<<<<<<<<<<<<< receive net status ok!");
				terminalService.updateStatus(diveceId, status);
				return;
			}
			
			logger.warn("receive net status over! data loss");
		} catch (Exception e) {
			logger.error("update net status error!, {}", e);
		}
		
		
	}
}