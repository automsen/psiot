package cn.com.tw.paas.monit.service.inn.callback;

import cn.com.tw.paas.monit.entity.db.org.CmdExe;

/**
 * 统一指令回调执行规范
 * @author liming
 * 2018年3月7日17:01:25
 */
public interface CmdCallback {

	
	/**
	 * 指令下发成功回调
	 * @param ins
	 * @param result http返回的字符串解析
	 */
	void insCallbackSuccess(CmdExe cmd,String result) throws Exception;
	/**
	 * 指令重试完全失败后执行
	 * @param ins
	 */
	void insCallbackError(CmdExe cmd,Exception e)throws Exception;
	/**
	  * 命令完整执行完毕 成功
	  * @param cmd
	  */
	void cmdSuccess(CmdExe cmd)throws Exception;
	
	/**
	  * 命令完整执行完毕 失败
	  * @param cmd
	  */
	void cmdError(CmdExe cmd,Exception e)throws Exception;
	
	
}
