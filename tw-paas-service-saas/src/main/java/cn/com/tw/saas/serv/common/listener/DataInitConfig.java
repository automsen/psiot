package cn.com.tw.saas.serv.common.listener;

import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cn.com.tw.common.utils.tools.quartz.JobScheManager;
import cn.com.tw.saas.serv.common.utils.cache.InsCacheMap;
import cn.com.tw.saas.serv.entity.db.base.Ins;
import cn.com.tw.saas.serv.entity.db.base.InsDataItem;
import cn.com.tw.saas.serv.entity.db.sys.Task;
import cn.com.tw.saas.serv.mapper.base.InsDataItemMapper;
import cn.com.tw.saas.serv.service.sys.TaskService;

@Component
public class DataInitConfig implements CommandLineRunner {
	
	public static final Logger logger = LoggerFactory.getLogger(DataInitConfig.class);

	@Autowired
	private TaskService taskService;
	
	private JobScheManager jobScheManager = JobScheManager.getInstance();
	
	@Autowired
	private InsDataItemMapper insDataItemMapper;
    @Override
    public void run(String... args) throws Exception {
    	// 加载数据项常量
    	List<InsDataItem> itemList =  insDataItemMapper.selectSaveTable();
		List<Ins> insList = insDataItemMapper.selectHaveDataItem();
		for (Ins temp: insList){
			InsCacheMap.insDataItemMap.put(temp.getDataMarker(), temp.getItems());
		}
		for (InsDataItem temp:itemList){
			InsCacheMap.itemSaveTable.put(temp.getItemCode(), temp);
		}
		logger.info("预加载"+insList.size()+"条指令与其包含的数据项关系");
		logger.info("预加载"+itemList.size()+"条数据项与其对应的存储表关系");
		
		// 加载定时调度
		logger.info(">>>>>>>>>enter initJob(),正在初始化定时调度。");
		List<Task> taskList = taskService.selecTasks();
		logger.debug("task count = " + taskList.size() + ", tasks = " + taskList.toString());
		byte tinyint = 0;
		for (Task task : taskList) {
			try {
				Class<?> taskClass = Class
						.forName("cn.com.tw.saas.serv.common.utils.quartz.job." + task.getTaskCode() + "Job");
				// 添加任务
				jobScheManager.addJob(task.getJobName(), task.getGroupName(), taskClass, task.getCronExpression());
				// 暂停状态为0的任务
				if (tinyint == task.getStatus()) {
					jobScheManager.pauseJob(task.getJobName(), task.getGroupName());
				}
			} catch (Exception e) {
				logger.error("initJob() exception，" + task.getTaskName() + "初始化定时调度失败，e=" + e.toString());
				e.printStackTrace();
				continue;
			}
		}

		try {
			// 启动任务
			jobScheManager.start();

			logger.info(">>>>>>>>>exit initJob();初始化定时调度成功。");
		} catch (SchedulerException e) {
			logger.error("initJob() exception，定时调度JobScheManager.start()启动失败，e=" + e.toString());
			e.printStackTrace();
		}
    }
}