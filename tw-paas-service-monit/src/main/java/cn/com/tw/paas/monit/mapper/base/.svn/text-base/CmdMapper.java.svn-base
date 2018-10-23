package cn.com.tw.paas.monit.mapper.base;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.base.Cmd;

public interface CmdMapper {
    int deleteByPrimaryKey(String cmdId);

    int insert(Cmd record);

    int insertSelective(Cmd record);

    Cmd selectByPrimaryKey(String cmdId);

    int updateByPrimaryKeySelective(Cmd record);

    int updateByPrimaryKey(Cmd record);
    
    List<Cmd> selectByEntity(Cmd cmd);
}