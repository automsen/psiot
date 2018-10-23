package cn.com.tw.paas.monit.service.dlt645;

import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;


public interface CmdHandleService {

	public void execute(BaseCmdEXE cmd,CmdCallBack callback) throws Exception;
	
	public void executeNoInsert(BaseCmdEXE cmd,CmdCallBack callback) throws Exception;
}
