package cn.com.tw.paas.monit.service.directive;

import cn.com.tw.paas.monit.service.dlt645.CmdCallBack;
import cn.com.tw.paas.monit.thread.entity.PageCmdResult;
import cn.com.tw.paas.monit.thread.ibl.RetryCmdHandle;




/**
 * 电表开关闸指令
 * 一个命令一个指令的操作
 * @author liming
 * 2017年9月4日10:02:35
 *
 */
public interface MeterBreakerCmdService extends RetryCmdHandle{
	
	public PageCmdResult breakerOpenMeter(String meterId,CmdCallBack callback);
	
	public PageCmdResult breakerCloseMeter(String meterId,CmdCallBack callback);
}
