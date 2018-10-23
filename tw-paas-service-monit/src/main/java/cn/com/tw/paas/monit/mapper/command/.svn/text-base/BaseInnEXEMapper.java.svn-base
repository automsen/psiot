package cn.com.tw.paas.monit.mapper.command;

import java.util.List;

import cn.com.tw.common.web.core.IBaseMapper;
import cn.com.tw.paas.monit.entity.db.command.BaseInnEXE;

public interface BaseInnEXEMapper extends IBaseMapper<BaseInnEXE>{
	
	BaseInnEXE selectInnByStatus(String meterId);
	
	List<BaseInnEXE> selectInnAvgByMeterId(String meterId);

	List<BaseInnEXE> selectAvgByCode(String code);
	
	List<BaseInnEXE> selectByInnExe(BaseInnEXE innExe);
	
   /* int deleteByPrimaryKey(String innId);

    int insert(BaseInnEXE record);

    int insertSelective(BaseInnEXE record);

    BaseInnEXE selectByPrimaryKey(String innId);

    int updateByPrimaryKeySelective(BaseInnEXE record);

    int updateByPrimaryKey(BaseInnEXE record);*/
}