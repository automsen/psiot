package cn.com.tw.paas.monit.mapper.command;

import java.util.List;

import cn.com.tw.common.web.core.IBaseMapper;
import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;

public interface BaseCmdEXEMapper extends IBaseMapper<BaseCmdEXE>{

	BaseCmdEXE selectCmdByStatus(String meterId);

	List<BaseCmdEXE> selectAvgByCode(String code);

	List<BaseCmdEXE> selectAvgByMeterId(String meterId);

	BaseCmdEXE selectStatusByCode(String code);

	List<BaseCmdEXE> selectAll(BaseCmdEXE baseCmdEXE);
  
}