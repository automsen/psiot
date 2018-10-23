package cn.com.tw.paas.monit.service.org;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.ApiLogExpand;
import cn.com.tw.paas.monit.entity.db.org.ApiLog;

public interface ApiLogService extends IBaseSerivce<ApiLog>{

	List<ApiLogExpand> selectApiLogExpandByPage(Page page);
	
	List<ApiLogExpand> selectApiLogExpandByShow(Page page);
	
}
