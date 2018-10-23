package cn.com.tw.paas.monit.mapper.base;

import cn.com.tw.paas.monit.entity.db.base.CmdIns;

public interface CmdInsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CmdIns record);

    int insertSelective(CmdIns record);

    CmdIns selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CmdIns record);

    int updateByPrimaryKey(CmdIns record);
}