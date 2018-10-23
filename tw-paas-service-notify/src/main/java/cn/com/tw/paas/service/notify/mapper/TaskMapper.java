package cn.com.tw.paas.service.notify.mapper;

import java.util.List;

import cn.com.tw.common.web.core.IBaseMapper;
import cn.com.tw.paas.service.notify.entity.Task;

public interface TaskMapper extends IBaseMapper<Task>{

	List<Task> selecTasks();
	
	void updateByTaskNameAndGroupName(Task task);
	
	List<Task> selectByBeanParam(Task task);
  /*  int deleteByPrimaryKey(String id);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);*/
}