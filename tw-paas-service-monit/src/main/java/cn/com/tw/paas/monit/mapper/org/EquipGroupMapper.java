package cn.com.tw.paas.monit.mapper.org;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.org.EquipGroup;

public interface EquipGroupMapper {
    int deleteByPrimaryKey(EquipGroup equipGroup);

    int insert(EquipGroup record);

    int insertSelective(EquipGroup record);

    EquipGroup selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(EquipGroup record);

    int updateByPrimaryKey(EquipGroup record);

	EquipGroup selectByCommAddr(String commAddr);

	List<EquipGroup> selectAll(EquipGroup equipGroup);

	void updateByChildCommAddr(EquipGroup equip1);
}