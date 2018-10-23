package cn.com.tw.paas.service.notify.quartz.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.paas.service.notify.service.NotifyService;

@Component
public class ClearNotifyRecordJob{
	
	private static Logger logger = LoggerFactory.getLogger(ClearNotifyRecordJob.class);
	
	private String notifyType = Env.getVal("notify.record.clear.type");
	
	private NotifyService notifyService;
	
	public ClearNotifyRecordJob() {
		this.notifyService = (NotifyService) SpringContext.getBean("notifyService");
	}
	
	@Scheduled(cron = "0 0 0 1/1 * ?")
	public void scheduled(){
		logger.debug(">>>>>>> start ClearNotifyRecordJob {}", new Date());
		int result = notifyService.clearNotifyRecord(notifyType);
		logger.debug(">>>>>>> end ClearNotifyRecordJob, execute number = {}",result);
	}
}
