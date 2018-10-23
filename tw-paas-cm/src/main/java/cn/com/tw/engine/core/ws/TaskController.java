package cn.com.tw.engine.core.ws;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.quartz.JobScheManager;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.engine.core.entity.Task;
import cn.com.tw.engine.core.exception.code.EngineCode;


@RestController
@RequestMapping("task")
public class TaskController {
	
private static Logger logger = LoggerFactory.getLogger(TaskController.class);
	
	@PostMapping("pause/{type}")
	public Response<?> pauseTask(@PathVariable String type){
		
		try {
			
			Task task = new Task();
			
			if("real".equals(type)){
				JobScheManager.getInstance().pauseJob(task.getJobName(), task.getGroupName());
			} else if("day".equals(type)){
				JobScheManager.getInstance().pauseJob("dayCollect", task.getGroupName());
			} else if("month".equals(type)){
				JobScheManager.getInstance().pauseJob("monthCollect", task.getGroupName());
			} else {
				return Response.retn(ResultCode.UNKNOW_ERROR,  type + " no exist type, error");
			}
			
			return Response.ok();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			logger.error("pauseTask, Exception e = {}", e.toString());
			
			return Response.retn(EngineCode.CHANNEL_UNKNOW, e.toString());
		}
		
	}
	
	@PostMapping("excute/{type}")
	public Response<?> excuteTask(@PathVariable String type){
		
		try {
			
			Task task = new Task();
			
			if("real".equals(type)){
				JobScheManager.getInstance().runJob(task.getJobName(), task.getGroupName());
			} else if("day".equals(type)){
				JobScheManager.getInstance().runJob("dayCollect", task.getGroupName());
			} else if("month".equals(type)){
				JobScheManager.getInstance().runJob("monthCollect", task.getGroupName());
			} else {
				return Response.retn(ResultCode.UNKNOW_ERROR,  type + " no exist type, error");
			}
			
			return Response.ok();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			logger.error("pauseTask, Exception e = {}", e.toString());
			
			return Response.retn(EngineCode.CHANNEL_UNKNOW, e.toString());
		}
		
	}
	
	@GetMapping("{type}")
	public Response<?> getTask(@PathVariable String type){
		try {
			Task task = new Task();
			if("real".equals(type)){
				String jobState = JobScheManager.getInstance().getJobState(task.getJobName(), task.getGroupName());
				task.setStatus(jobState);
			} else if("day".equals(type)){
				String jobState = JobScheManager.getInstance().getJobState("dayCollect", task.getGroupName());
				task.setStatus(jobState);
			} else if("month".equals(type)){
				String jobState = JobScheManager.getInstance().getJobState("monthCollect", task.getGroupName());
				task.setStatus(jobState);
			} else {
				return Response.retn(ResultCode.UNKNOW_ERROR,  type + " no exist type, error");
			}
			
			return Response.ok(task);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.retn(EngineCode.CHANNEL_UNKNOW);
		
	}
	
	/*private static Logger logger = LoggerFactory.getLogger(TaskController.class);
	
	@PostMapping("update")
	public Response updateTask(@RequestBody Task task){
		
		logger.debug("updateTask, cronExp = {}", task != null ? task.getCronExp():"");
		
		if (StringUtils.isEmpty(task)){
			
			return Response.retn(EngineCode.CHANNEL_PARAMS_ERROR, "params is null");
		}
		
		if (StringUtils.isEmpty(task.getCronExp())){
			
			return Response.retn(EngineCode.CHANNEL_PARAMS_ERROR, "CronExp is null");
		}
		
		try {
			
			JobScheManager.getInstance().updateJob(task.getJobName(), task.getGroupName(), task.getCronExp());

			TaskUtils.writeTaskToFile(task);
			
			
			return Response.ok();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			logger.error("updateTask, Exception e = {}", e.toString());
			
			return Response.retn(EngineCode.CHANNEL_UNKNOW, e.toString());
		}
		
		
		
	}
	
	
	
	*//**
	 * 
	 * @return
	 *//*
	@GetMapping()
	public Response getTask(){
		try {
			
			Task task = TaskUtils.readTaskFromFile();
			String jobState = JobScheManager.getInstance().getJobState(task.getJobName(), task.getGroupName());
			task.setStatus(jobState);
			return Response.ok(task);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.retn(EngineCode.CHANNEL_UNKNOW);
		
	}*/

}
