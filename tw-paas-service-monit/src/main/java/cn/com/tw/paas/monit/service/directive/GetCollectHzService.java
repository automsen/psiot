package cn.com.tw.paas.monit.service.directive;

import cn.com.tw.paas.monit.service.dlt645.CmdCallBack;
import cn.com.tw.paas.monit.thread.entity.PageCmdResult;
import cn.com.tw.paas.monit.thread.ibl.RetryCmdHandle;

public interface GetCollectHzService extends RetryCmdHandle{

	PageCmdResult getCollectHz(String netEquipAddr, CmdCallBack callback);

}
