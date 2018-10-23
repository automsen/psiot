package cn.com.tw.paas.monit.mapper.sys;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.sys.Area;

public interface AreaMapper {
    int deleteByPrimaryKey(String id);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);

	List<Area> selectAreaByParentId(String parentId);
}