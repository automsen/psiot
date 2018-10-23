package cn.com.tw.paas.monit.service.org.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.ApiLogExpand;
import cn.com.tw.paas.monit.entity.db.org.ApiLog;
import cn.com.tw.paas.monit.mapper.org.ApiLogMapper;
import cn.com.tw.paas.monit.service.org.ApiLogService;

@Service
public class ApiLogServiceImpl implements ApiLogService{
	
	@Autowired
	private ApiLogMapper apiLogMapper;

	@Override
	public int deleteById(String paramString) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(ApiLog paramT) {
		// TODO Auto-generated method stub
		return apiLogMapper.insert(paramT);
	}

	@Override
	public int insertSelect(ApiLog paramT) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ApiLog selectById(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ApiLog> selectByPage(Page paramPage) {
		return null;
	}

	@Override
	public int update(ApiLog paramT) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(ApiLog paramT) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ApiLogExpand> selectApiLogExpandByPage(Page page) {
		List<ApiLogExpand> apiLogs = apiLogMapper.selectByPage(page);
		return apiLogs;
	}
	
	@Override
	public List<ApiLogExpand> selectApiLogExpandByShow(Page page) {
		int offset = (page.getPage() - 1) * page.getRows();
		Map<String, Object> params = new HashMap<String, Object> ();
		params.put("offset", offset);
		params.put("end", page.getRows());
		params.put("paramObj", page.getParamObj());
		List<ApiLogExpand> apiLogList = apiLogMapper.selectApiLogExpandByShow(params);
		page.setTotalRecord(1000000);
		page.setData(apiLogList);
		List<ApiLogExpand> apiLogs = apiLogMapper.selectByPage(page);
		return apiLogs;
	}

}
