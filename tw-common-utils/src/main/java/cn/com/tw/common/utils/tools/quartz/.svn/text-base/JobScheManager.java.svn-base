package cn.com.tw.common.utils.tools.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时调度核心类
 * @author admin
 *
 */
public class JobScheManager {

	private SchedulerFactory sf = null;
	
	private JobScheManager(){
		sf = new StdSchedulerFactory();
	}

	private static class Singleton {

		private static JobScheManager jobScheManager = null;

		static {
			jobScheManager = new JobScheManager();
		}

		public static JobScheManager getInstance() {
			return jobScheManager;
		}
	}

	public static JobScheManager getInstance() {
		return Singleton.getInstance();
	}

	public Scheduler getScheduler() throws SchedulerException {
		return sf.getScheduler();
	}
	
	public void start() throws SchedulerException{
	    getScheduler().start();
	}

	/**
	 * 初始化JOB <功能详细描述>
	 * 
	 * @return Scheduler Scheduler
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 * @see [类、类#方法、类#成员]
	 */
	public JobScheManager addJob(String jobName, String groupName, @SuppressWarnings("rawtypes") Class taskClass, String cronExpression) throws SchedulerException, ClassNotFoundException {
		Scheduler sched = getScheduler();
		if (jobName == null || groupName == null) {
			throw new SchedulerException();
		}

		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
		CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);

		if (trigger == null) {

			@SuppressWarnings("unchecked")
			JobDetail jobDetail = JobBuilder.newJob(taskClass).withIdentity(jobName, groupName).build();
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).startNow().withSchedule(scheduleBuilder).build();
			sched.scheduleJob(jobDetail, trigger);
		} else {
			// trigger已存在，则更新相应的定时设置
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			sched.rescheduleJob(triggerKey, trigger);
		}
		
		return Singleton.getInstance();
	}

	/**
	 * 暂停任务 <功能详细描述>
	 * 
	 * @param jobName
	 *            jobName
	 * @param groupName
	 *            groupName
	 * @throws SchedulerException
	 *             异常
	 * @see [类、类#方法、类#成员]
	 */
	public void pauseJob(String jobName, String groupName)
			throws SchedulerException {
		Scheduler scheduler = getScheduler();
		JobKey jobKey = JobKey.jobKey(jobName, groupName);
		scheduler.pauseJob(jobKey);
	}

	/**
	 * 恢复任务 <功能详细描述>
	 * 
	 * @param jobName jobName
	 * @param groupName groupName
	 * @throws SchedulerException 异常
	 * @see [类、类#方法、类#成员]
	 */
	public void resumeJob(String jobName, String groupName) throws SchedulerException {
		Scheduler scheduler = getScheduler();
		JobKey jobKey = JobKey.jobKey(jobName, groupName);
		scheduler.resumeJob(jobKey);
	}

	/**
	 * 删除任务 <功能详细描述>
	 * 
	 * @param jobName
	 *            jobName
	 * @param groupName
	 *            groupName
	 * @throws SchedulerException
	 *             异常
	 * @see [类、类#方法、类#成员]
	 */
	public void deleteJob(String jobName, String groupName) throws SchedulerException {
		Scheduler scheduler = getScheduler();
		JobKey jobKey = JobKey.jobKey(jobName, groupName);
		scheduler.deleteJob(jobKey);
	}

	/**
	 * 立即运行任务 <功能详细描述>
	 * 
	 * @param jobName jobName
	 * @param groupName groupName
	 * @throws SchedulerException 异常
	 * @see [类、类#方法、类#成员]
	 */
	public void runJob(String jobName, String groupName) throws SchedulerException {
		Scheduler scheduler = getScheduler();
		JobKey jobKey = JobKey.jobKey(jobName, groupName);
		scheduler.triggerJob(jobKey);
	}

	/**
	 * @param runType
	 *            ，job的执行类型。1,系统配置触发执行；2,人工触发执行。默认系统触发执行
	 */
	public void runJob(String jobName, String groupName, String runType) throws SchedulerException {
		Scheduler scheduler = getScheduler();
		JobKey jobKey = JobKey.jobKey(jobName, groupName);
		JobDetail job = scheduler.getJobDetail(jobKey);
		JobDataMap map = job.getJobDataMap();
		if ("2".equals(runType)) {
			map.put("runType", "2");
		} else {
			map.put("runType", "1");
		}
		scheduler.triggerJob(jobKey, map);
	}

	/**
	 * 更新任务 <功能详细描述>
	 * 
	 * @param jobName
	 *            jobName
	 * @param groupName
	 *            groupName
	 * @param cronExpression
	 *            cronExpression
	 * @throws SchedulerException
	 *             异常
	 * @see [类、类#方法、类#成员]
	 */
	public void updateJob(String jobName, String groupName, String cronExpression) throws SchedulerException {
		Scheduler scheduler = getScheduler();

		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);

		// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		if (trigger == null) {
			throw new SchedulerException("jobName and groupName not exists in jobs ");
		}

		// 表达式调度构建器
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

		// 按新的cronExpression表达式重新构建trigger
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

		// 按新的trigger重新设置job执行
		scheduler.rescheduleJob(triggerKey, trigger);
	}

	/**
	 * 任务是否存在 <功能详细描述>
	 * 
	 * @param jobName jobName
	 * @param groupName groupName
	 * @throws SchedulerException
	 * @see [类、类#方法、类#成员]
	 */
	public boolean isJobExists(String jobName, String groupName)
			throws SchedulerException {

		Scheduler scheduler = getScheduler();

		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);

		// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
		Trigger trigger = scheduler.getTrigger(triggerKey);

		if (trigger == null) {
			return false;
		}

		return true;
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @param jobName
	 * @param groupName
	 * @return
	 * @throws SchedulerException
	 * @see [类、类#方法、类#成员]
	 */
	public String getJobState(String jobName, String groupName) throws SchedulerException {
		Scheduler scheduler = getScheduler();

		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);

		TriggerState state = scheduler.getTriggerState(triggerKey);

		return state.name();
	}

	/**
	 * 释放资源 <功能详细描述>
	 * 
	 * @throws SchedulerException
	 * @see [类、类#方法、类#成员]
	 */
	public void shutdown() throws SchedulerException {
		Scheduler scheduler = getScheduler();
		scheduler.shutdown(true);
	}

	public boolean isShutdown() throws SchedulerException {
		return getScheduler().isShutdown();
	}

}
