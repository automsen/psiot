package cn.com.tw.saas.serv.service.sys.impl;

import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.tools.quartz.JobScheManager;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.sys.Task;
import cn.com.tw.saas.serv.mapper.sys.TaskMapper;
import cn.com.tw.saas.serv.service.sys.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskMapper taskMapper;
	
	private JobScheManager jobScheManager = JobScheManager.getInstance();
	
	@Override
	public int insert(Task paramT) {
		paramT.setId(CommUtils.getUuid());
		Date saveDate = new Date();
		paramT.setCreateTime(saveDate);
		try {
			if(1 == paramT.getStatus()){
				Class<?> taskClass = Class.forName("cn.com.tw.monit.common.utils.quartz.job." + paramT.getTaskCode() + "Job");
				jobScheManager.addJob(paramT.getJobName(), paramT.getGroupName(), taskClass, paramT.getCronExpression());
			}
			taskMapper.insertSelective(paramT);
		} catch (ClassNotFoundException | SchedulerException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	@Transactional
	public int deleteById(String paramString) {
		try {
			Task task = taskMapper.selectByPrimaryKey(paramString);
			jobScheManager.deleteJob(task.getJobName(), task.getGroupName());
			taskMapper.deleteByPrimaryKey(paramString);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Task selectById(String paramString) {
		// TODO Auto-generated method stub
		return taskMapper.selectByPrimaryKey(paramString);
	}

	@Override
	
	public int updateSelect(Task paramT) {
		taskMapper.updateByPrimaryKeySelective(paramT);
		return 0;
	}
	
	@Override
	@Transactional
	public int updateTask(Task paramT) {
		int ret = taskMapper.updateByPrimaryKeySelective(paramT);
		try {
			jobScheManager.updateJob(paramT.getJobName(), paramT.getGroupName(), paramT.getCronExpression());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
		return ret;
	}

	@Override
	public List<Task> selectByPage(Page paramPage) {
		return taskMapper.selectByPage(paramPage);
	}

	@Override
	@Transactional
	public int pauseTask(Task task){
		try {
			task.setStatus((byte) 0);
			taskMapper.updateByTaskNameAndGroupName(task);
			jobScheManager.pauseJob(task.getJobName(), task.getGroupName());
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
		return 0;
	}
	
	@Override
	@Transactional
	public int resumeTask(Task task){
		try {
			task.setStatus((byte) 1);
			taskMapper.updateByTaskNameAndGroupName(task);
			jobScheManager.resumeJob(task.getJobName(), task.getGroupName());
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
		return 0;
	}

	@Override
	public List<Task> selecTasks() {
		return taskMapper.selecTasks();
	}
	
	@Override
	public List<Task> selectByBeanParam(Task task){
		return taskMapper.selectByBeanParam(task); 
	}

	@Override
	public int insertSelect(Task t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Task t) {
		// TODO Auto-generated method stub
		return 0;
	}

}
