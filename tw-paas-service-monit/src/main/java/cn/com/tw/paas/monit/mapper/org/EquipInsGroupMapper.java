package cn.com.tw.paas.monit.mapper.org;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.org.EquipInsGroup;

public interface EquipInsGroupMapper {
    int deleteByPrimaryKey(String id);

    int insert(EquipInsGroup record);

    int insertSelective(EquipInsGroup record);

    EquipInsGroup selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(EquipInsGroup record);

    int updateByPrimaryKey(EquipInsGroup record);

	List<EquipInsGroup> selectByAll(EquipInsGroup equipInsGroup);
}