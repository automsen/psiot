package cn.com.tw.paas.monit.service.command;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;

public interface BaseCmdEXEService extends IBaseSerivce<BaseCmdEXE>{

	List<BaseCmdEXE> selectAll(BaseCmdEXE baseCmdEXE);

}
