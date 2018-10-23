package cn.com.tw.paas.monit.service.sys;

import org.springframework.dao.DataAccessException;

/**
 * 命令指令缓存
 * @author liming
 * 2018年10月15日 18:12:52
 */
public interface CmdInsCacheService {

	
	Object selectCmdById(String cmdCode);
	
	Object selectInsByDataMarker(String cmdId);
	
	void refreshAll() throws DataAccessException;
	
	
	
}
