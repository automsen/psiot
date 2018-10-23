package cn.com.tw.paas.monit.service.directive;


import java.math.BigDecimal;

import cn.com.tw.paas.monit.service.dlt645.CmdCallBack;
import cn.com.tw.paas.monit.thread.entity.PageCmdResult;

public interface MeterPayService {
	/**
	 * 缴费
	 * 表计预付费
	 * @return
	 */
	PageCmdResult meterRecharge(String commAddr,CmdCallBack callback,BigDecimal money);
	
	
}
