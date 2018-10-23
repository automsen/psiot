package cn.com.tw.paas.service.es.service.realdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.es.dao.realdata.RealDataDao;

@Service
public class RealDataService {
	
	@Autowired
	private RealDataDao realDataDao;
	
	public Page queryRealHistroyPage(Page page) {
		page.setData(realDataDao.queryReadHistory(page));
		return page;
	}
	
	public void queryRealData() throws Exception {
		realDataDao.getEmployee();
	}

}
