package cn.com.tw.paas.monit.service.org.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.PushLogExpand;
import cn.com.tw.paas.monit.entity.db.org.PushLog;
import cn.com.tw.paas.monit.mapper.org.PushLogMapper;
import cn.com.tw.paas.monit.service.org.PushLogService;

@Service
public class PushLogServiceImpl implements PushLogService{
	
	@Autowired
	private PushLogMapper pushLogMapper;

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(PushLog arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(PushLog arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PushLog selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PushLog> selectByPage(Page page) {
		List<PushLog> pushLogs = pushLogMapper.selectByPage(page);
		return pushLogs;
	}

	@Override
	public int update(PushLog arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(PushLog arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<PushLogExpand> selectPushLogPage(Page page) {
		List<PushLogExpand> pushLogs = pushLogMapper.selectPushLogPage(page);
		return pushLogs;
	}

	@Override
	public List<PushLogExpand> selectPushLogShow(Page page) {
		int offset = (page.getPage() - 1) * page.getRows();
		Map<String, Object> params = new HashMap<String, Object> ();
		params.put("offset", offset);
		params.put("end", page.getRows());
		params.put("paramObj", page.getParamObj());
		List<PushLogExpand> pushLogs = pushLogMapper.selectPushLogShow(params);
		page.setTotalRecord(1000000);
		page.setData(pushLogs);
		return pushLogs;
	}

}
