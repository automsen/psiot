package cn.com.tw.paas.service.notify.queue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.notify.entity.NotifyRecord;
import cn.com.tw.paas.service.notify.service.NotifyService;

@Component
public class DataInitConfig implements CommandLineRunner {
	
	@Autowired
	private TaskUtils taskUtils;
	
	@Autowired
	private NotifyService notifyService;
	
	private static Logger log = LoggerFactory.getLogger(DataInitConfig.class);

	@Override
	public void run(String... arg0) throws Exception {
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
		
	}
	
}