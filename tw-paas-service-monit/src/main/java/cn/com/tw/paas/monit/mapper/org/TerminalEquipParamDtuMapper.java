package cn.com.tw.paas.monit.mapper.org;

import cn.com.tw.paas.monit.entity.db.org.TerminalEquipParamDtu;

public interface TerminalEquipParamDtuMapper {
    int deleteByPrimaryKey(String equipNumber);

    int insert(TerminalEquipParamDtu record);

    int insertSelective(TerminalEquipParamDtu record);

    TerminalEquipParamDtu selectByPrimaryKey(String equipNumber);

    int updateByPrimaryKeySelective(TerminalEquipParamDtu record);

    int updateByPrimaryKey(TerminalEquipParamDtu record);
}