package cn.com.tw.saas.serv.mapper.sys;

import cn.com.tw.saas.serv.entity.db.sys.NotifyRecord;

public interface NotifyRecordMapper {
    int deleteByPrimaryKey(String uuid);

    int insert(NotifyRecord record);

    int insertSelective(NotifyRecord record);

    NotifyRecord selectByPrimaryKey(String uuid);

    int updateByPrimaryKeySelective(NotifyRecord record);

    int updateByPrimaryKey(NotifyRecord record);
}