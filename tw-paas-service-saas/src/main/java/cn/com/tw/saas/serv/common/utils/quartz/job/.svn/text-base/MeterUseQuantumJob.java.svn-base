package cn.com.tw.saas.serv.common.utils.quartz.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.saas.serv.entity.db.sys.Task;
import cn.com.tw.saas.serv.service.statistics.MeterUseQuantumService;
import cn.com.tw.saas.serv.service.sys.TaskService;

public class MeterUseQuantumJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(MeterUseQuantumJob.class);

	@Autowired
	private TaskService taskService;
	
	private MeterUseQuantumService meterUseQuantumService;

	public MeterUseQuantumJob() {
		this.taskService = (TaskService) SpringContext.getBean("taskServiceImpl");
		this.meterUseQuantumService = (MeterUseQuantumService) SpringContext.getBean("meterUseQuantumServiceImpl");
	}

	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		// 自动手动执行任务

		// 获取定时任务执行的时间
		Date lastTime = ctx.getScheduledFireTime();
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>start test  [{}]", sdf.format(lastTime));
		// 获取任务的group名字
		String group = ctx.getTrigger().getJobKey().getGroup();
		// 获取任务的job名字
		String name = ctx.getTrigger().getJobKey().getName();
		// 通过分组信息和任务名称查询任务

		Task task = new Task();
		task.setGroupName(group);
		task.setTaskName(name);

		List<Task> tempTasks = null;
		try {
			tempTasks = taskService.selectByBeanParam(task);
			task = tempTasks.get(0);
		} catch (Exception e) {
			logger.error("job-test job TaskId:({}) Task is undefiend exception: {}", task.getTaskName(), e.toString());
			return;
		}
		try {
			meterUseQuantumService.meterUseQuantumJob(sdf.format(lastTime));
			// 任务执行成功更新任务状态
			task.setIsLastSuccess(new Byte("1"));// 1 success 2 error;
			task.setLastTime(new Date(System.currentTimeMillis()));
			taskService.updateSelect(task);
			logger.info("<<<<<<<<<<<<< end autoDeductionJob");
		} catch (Exception e) {
			logger.error("job-test job TaskId:({}) job handle failed: {}", task.getId(), e.toString());
			// 更新任务状态为执行失败
			task.setIsLastSuccess(new Byte("2"));// 1 success 2 error;
			task.setLastTime(new Date(System.currentTimeMillis()));
			taskService.updateSelect(task);
		}
	}

}
