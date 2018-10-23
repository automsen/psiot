package cn.com.tw.paas.monit.mapper.org;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.org.TerminalEquipChildren;

public interface TerminalEquipChildrenMapper {
    int deleteByPrimaryKey(String equipId);

    int insert(TerminalEquipChildren record);

    int insertSelective(TerminalEquipChildren record);

    TerminalEquipChildren selectByPrimaryKey(String equipId);

    int updateByPrimaryKeySelective(TerminalEquipChildren record);

    int updateByPrimaryKey(TerminalEquipChildren record);
    
    List<TerminalEquipChildren> selectByEntity(TerminalEquipChildren record);
	
    TerminalEquipChildren selectByEquipNumber(String EquipNumber);

	int deleteByDtuId(String equipId);

	int deleteAndInsert(TerminalEquipChildren tec);
}