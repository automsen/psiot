package cn.com.tw.paas.service.notify.service.sys;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.paas.service.notify.entity.Task;

@Service
public interface TaskService extends IBaseSerivce<Task>{
	
	int resumeTask(Task task);
	
	int pauseTask(Task task);
	
	List<Task> selecTasks();
	
	List<Task> selectByBeanParam(Task task);
	
	int updateTask(Task task);
}
