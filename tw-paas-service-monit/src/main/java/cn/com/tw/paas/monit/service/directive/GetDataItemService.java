package cn.com.tw.paas.monit.service.directive;

import cn.com.tw.paas.monit.service.dlt645.CmdCallBack;
import cn.com.tw.paas.monit.thread.entity.PageCmdResult;
import cn.com.tw.paas.monit.thread.ibl.RetryCmdHandle;

/**
 *读写下联仪表
 */
public interface GetDataItemService extends RetryCmdHandle{

	public PageCmdResult getDataItem(String netEquipAddr, CmdCallBack callback);

}
