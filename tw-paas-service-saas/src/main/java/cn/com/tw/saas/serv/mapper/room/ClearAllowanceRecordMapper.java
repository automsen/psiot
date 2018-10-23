package cn.com.tw.saas.serv.mapper.room;

import cn.com.tw.saas.serv.entity.room.ClearAllowanceRecord;

public interface ClearAllowanceRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(ClearAllowanceRecord record);

    int insertSelective(ClearAllowanceRecord record);

    ClearAllowanceRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ClearAllowanceRecord record);

    int updateByPrimaryKey(ClearAllowanceRecord record);
}