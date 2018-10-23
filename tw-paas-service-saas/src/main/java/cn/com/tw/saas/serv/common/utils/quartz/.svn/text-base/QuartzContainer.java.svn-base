package cn.com.tw.saas.serv.common.utils.quartz;

import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.utils.tools.quartz.JobScheManager;
import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.saas.serv.entity.db.sys.Task;
import cn.com.tw.saas.serv.service.sys.TaskService;

public class QuartzContainer {

	 private TaskService taskService;
	    
	 public static final Logger log = LoggerFactory.getLogger(QuartzContainer.class);	
	    
	    public QuartzContainer()
	    {
	        this.taskService = (TaskService) SpringContext.getBean("taskServiceImpl");
	    }
	    
	    /**
	     * 重启定时任务
	     * <功能详细描述>
	     * @throws SchedulerException
	     * @see [类、类#方法、类#成员]
	     */
	    public void resetJobs() throws SchedulerException, Exception
	    {
	        List<Task> taskList = taskService.selecTasks();
	        
	        JobScheManager jobScheManager = JobScheManager.getInstance();
	        
	        byte tinyint = 0;
	        //判断是否关闭，不是关闭 ，先关闭
	        if (!jobScheManager.isShutdown())
	        {
	            jobScheManager.shutdown();
	        }
	        
	        for (Task task : taskList)
	        {
	            try
	            {
	            	Class<?> taskClass = Class.forName("cn.com.tw.saas.serv.common.utils.quartz.job." + task.getTaskCode() + "Job");
	                jobScheManager.addJob(task.getJobName(), task.getGroupName(), taskClass, task.getCronExpression());
	                
	                //暂停状态为1的任务
	                if (tinyint == task.getStatus())
	                {
	                    jobScheManager.pauseJob(task.getJobName(), task.getGroupName());
	                }
	            }
	            catch (Exception e)
	            {
	                e.printStackTrace();
	                continue;
	            }
	        }
	        
	        //启动
	        jobScheManager.start();
	    }
}
