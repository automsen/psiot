package cn.com.tw.paas.monit.service.directive;

import cn.com.tw.paas.monit.service.dlt645.CmdCallBack;
import cn.com.tw.paas.monit.thread.entity.PageCmdResult;
import cn.com.tw.paas.monit.thread.ibl.RetryCmdHandle;

/**
 *采集器连接仪表
 */
public interface GatewayMeterGetService extends RetryCmdHandle{
	public PageCmdResult gatewayMeterGet(String equipId,CmdCallBack callback);
}
