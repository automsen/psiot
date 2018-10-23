package cn.com.tw.paas.service.notify.queue.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.core.jms.cons.MqCons;
import cn.com.tw.common.core.jms.cons.PsMqCons;
import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.paas.service.notify.entity.NotifyRecord;
import cn.com.tw.paas.service.notify.eum.NotifyStatusEm;
import cn.com.tw.paas.service.notify.queue.TaskUtils;
import cn.com.tw.paas.service.notify.service.NotifyService;

/**
 * 消费通知记录
 * @author admin
 *
 */
@Component
public class NotifyConsumer {
	
	private static Logger log = LoggerFactory.getLogger(NotifyConsumer.class);
	
	@Autowired
	private TaskUtils taskUtils;
	
	@Autowired
	private NotifyService notifyService;
	
	@Autowired
	private MqHandler mqHandler;
	
	/*@JmsListener(destination = PsMqCons.QUEUE_NOTIFY_NAME)  
    public void receiveNotifyQueue(String msg) {*/
	@KafkaListener(topics = PsMqCons.QUEUE_NOTIFY_NAME)
	public void receiveNotifyQueue(ConsumerRecord<?, ?> cr){
		log.info("------>> queue-receive message:  {} - {} : {}", cr.topic(), cr.key(), cr.value());
		String msg = (String) cr.value();
		
		NotifyRecord notifyRecord = JsonUtils.jsonToPojo(msg, NotifyRecord.class);
		notifyRecord.setStatus(NotifyStatusEm.NOTIFY_CREATE.getValue());
		// 判断数据库中是否已有通知记录
		if (StringUtils.isEmpty(notifyRecord.getUuid())) {
			try {
				
				String uuid = CommUtils.getUuid();
				notifyRecord.setUuid(uuid);;
				
				// 将获取到的通知先保存到数据库中
				taskUtils.createNotifyToDB(notifyRecord);
				
				//添加到通知队列
				taskUtils.buildTask(notifyRecord);
			} catch (BusinessException e){
				
				mqHandler.sendDelay(MqCons.QUEUE_NOTIFY_NAME, msg, 300);
				
				log.error(SysCons.LOG_ERROR + " BusinessException: create notify to database error : {} ", e.toString());
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
			}
		}
		
		
	}
	
	/**
	 * 初始化获取数据库数据库
	 */
	/*@PostConstruct
	public void init(){
		log.debug("{} NotifyConsumer: initDB()", SysCons.LOG_START);
		
		Page page = new Page();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("status", new String[]{"111","101","110","100"});
		paramMap.put("times", new Integer[]{0,1,2,3,4});
		page.setParamObj(paramMap);
		
		page = notifyService.queryListNotify(page);
	    int totalPage = page.getTotalPage();
	    
	    int currentPage = 1;
	    while (currentPage <= totalPage) {
	    	@SuppressWarnings("unchecked")
			List<NotifyRecord> notifyRecordList = (List<NotifyRecord>) page.getData();
	    	for(NotifyRecord notifyRecord : notifyRecordList){
	    		notifyRecord.setLastNotifyTime(new Date());
	    		taskUtils.buildTask(notifyRecord);
	    	}
	    	
	    	currentPage++;
	    	page.setPage(currentPage);
	    	page = notifyService.queryListNotify(page);
	    }
		
		//获取数据库中每页的数据
		log.debug("NotifyTaskInit: data from db : {} ", new Object());
		//插入数据到
		log.debug("{} NotifyTaskInit: initDB()", SysCons.LOG_END);
	}*/
		
}
