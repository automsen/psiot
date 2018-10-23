package cn.com.tw.paas.service.notify.mapper;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.notify.entity.NotifyRecord;

public interface NotifyRecordMapper {
    int deleteByPrimaryKey(String uuid);

    int insert(NotifyRecord record);

    int insertSelective(NotifyRecord record);

    NotifyRecord selectByPrimaryKey(String uuid);

    int updateByPrimaryKeySelective(NotifyRecord record);

    int updateByPrimaryKey(NotifyRecord record);
    
    List<NotifyRecord> selectByPage(Page page);
    
    List<NotifyRecord> queryListNotify(Page page);

	void clearNotifyRecord(String notifyType);
}