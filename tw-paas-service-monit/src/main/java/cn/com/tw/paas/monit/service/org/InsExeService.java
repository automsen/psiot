package cn.com.tw.paas.monit.service.org;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.org.InsExe;

public interface InsExeService  {

	
	void updateIns(InsExe ins);
	
	List<InsExe> selectByEntity(InsExe ins);
}
