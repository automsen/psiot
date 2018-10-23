package cn.com.tw.saas.serv.common.utils.quartz.job;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.saas.serv.service.room.AllowanceService;

@Component
public class AllowanceJob{
	
	private static Logger logger = LoggerFactory.getLogger(AllowanceJob.class);
	
	private AllowanceService allowanceService;
	
	public AllowanceJob() {
		this.allowanceService = (AllowanceService) SpringContext.getBean("allowanceServiceImpl");
	}
	
	@Scheduled(cron = "0 1 0 1/1 * ?")
	public void scheduled(){
		logger.debug(">>>>>>> start AllowanceJob {}", new Date());
		int result = allowanceService.executePlan();
		logger.debug(">>>>>>> end AllowanceJob, execute number = {}",result);
	}
}
