package cn.com.tw.paas.monit.thread.ibl;

import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;


/**
 * 重试命令执行
 * @author liming
 * 2017年10月12日15:01:37
 */
public interface RetryCmdHandle {

	/**
	 * 重试执行命令，并设置特定的回调
	 * @param cmd
	 * @param callback
	 */
	public void execCmd(BaseCmdEXE cmd);
}
