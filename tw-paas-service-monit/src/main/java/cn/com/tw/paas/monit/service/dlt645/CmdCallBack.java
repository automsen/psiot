package cn.com.tw.paas.monit.service.dlt645;

import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;

public interface CmdCallBack {

	public void cmdSuccess(BaseCmdEXE cmd) throws Exception;
	public void cmdError(BaseCmdEXE cmd)throws Exception;
}
