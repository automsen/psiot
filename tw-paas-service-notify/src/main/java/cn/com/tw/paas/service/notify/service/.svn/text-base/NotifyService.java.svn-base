package cn.com.tw.paas.service.notify.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.notify.entity.NotifyRecord;
import cn.com.tw.paas.service.notify.mapper.NotifyRecordMapper;

@Service
public class NotifyService implements IBaseSerivce<NotifyRecord>{
	
	@Autowired
	private NotifyRecordMapper notifyRecordMapper;
	
	public void updateNotiStatusTimes(String uuid, String status, int notifyTimes){
		NotifyRecord notice = new NotifyRecord();
		notice.setUuid(uuid);
		notice.setStatus(status);
		notice.setNotifyTimes(notifyTimes);
		notice.setLastNotifyTime(new Date());
		notifyRecordMapper.updateByPrimaryKeySelective(notice);
	}

	@Override
	public int insert(NotifyRecord notice) {
		notice.setCreateTime(new Date());
		return notifyRecordMapper.insert(notice);
	}

	@Override
	public int insertSelect(NotifyRecord notice) {
		notice.setCreateTime(new Date());
		return notifyRecordMapper.insertSelective(notice);
	}

	@Override
	public int deleteById(String uuid) {
		return notifyRecordMapper.deleteByPrimaryKey(uuid);
	}

	@Override
	public NotifyRecord selectById(String uuid) {
		return notifyRecordMapper.selectByPrimaryKey(uuid);
	}

	@Override
	public int updateSelect(NotifyRecord notice) {
		notice.setLastNotifyTime(new Date());
		return notifyRecordMapper.updateByPrimaryKeySelective(notice);
	}

	@Override
	public int update(NotifyRecord notice) {
		notice.setLastNotifyTime(new Date());
		return notifyRecordMapper.updateByPrimaryKey(notice);
	}

	@Override
	public List<NotifyRecord> selectByPage(Page page) {
		List<NotifyRecord> notifyList = notifyRecordMapper.selectByPage(page);
		page.setData(notifyList);
		return notifyList;
	}
	
	public Page queryListNotify(Page page){
		List<NotifyRecord> notifyList = notifyRecordMapper.queryListNotify(page);
		page.setData(notifyList);
		return page;
	}

	
	public int clearNotifyRecord(String notifyType) {
		notifyRecordMapper.clearNotifyRecord(notifyType);
		return 0;
	}


}