package cn.com.tw.paas.service.queue.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.queue.dao.read.HApiLogDao;

@Service
public class ApiLogService {
	
	@Autowired
	private HApiLogDao hApiLogDao;
	
	public void createLogTable(){
		hApiLogDao.createApiLogTable();
	}
	
	public void queryLogByPage(Page page) {
		List<Map<String, Object>> result = hApiLogDao.queryLogByPage(page);
		page.setData(result);
	}
	
	public void addLog(Map<String, Object> apiLogData){
		hApiLogDao.putApiLog(apiLogData);
	}

}
