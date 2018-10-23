package cn.com.tw.paas.service.notify.queue;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.tw.paas.service.notify.entity.NotifyRecord;
import cn.com.tw.paas.service.notify.eum.NotifyStatusEm;
import cn.com.tw.paas.service.notify.service.NotifyService;

/**
 * 工具类
 * @author admin
 *
 */
@Component
public class TaskUtils {
	
	@Autowired
	private NotifyService notifyService;
	
	@Autowired
	private NotifyConfig notifyConfig;
	
	/**
	 * 构建任务，设置任务属性, 重新放入队列
	 * @param notifyRecord
	 * @return
	 */
	public void buildTask(NotifyRecord notifyRecord){
		if(notifyRecord == null){
			return;
		}
		
		//获取通知次数
		int notifyTimes = notifyRecord.getNotifyTimes();
		
		//获取最大通知次数
		//int maxNotifyTimes = notifyConfig.getMaxNotifyTimes();
		int maxNotifyTimes = notifyRecord.getLimitNotifyTimes();
		
		//判断是否第一次放入队列,第一设置默认时间
		if(notifyRecord.getIsFirst() == 0){
			notifyRecord.setLastNotifyTime(new Date());
		}
		
		long lastNotifyTime = notifyRecord.getLastNotifyTime().getTime();
		
		//如果通知次数没有超过最大次数，每下一次通知延长通知时间
		if (notifyTimes < maxNotifyTimes){
			//通知次数 +1
			notifyTimes = notifyTimes + 1;
			
			//获取次数对应的时间值
			int timeMulti = notifyConfig.getParamsValue(notifyTimes);
			
			notifyRecord.setLastNotifyTime(new Date(lastNotifyTime + 1000 * 60 * timeMulti));
			
			notifyRecord.setIsFirst((byte) 1);
			AsyncTaskDelayQueue.getInstance().put(new NotifyTask(notifyRecord));
		}else {
			notifyRecord.setStatus(NotifyStatusEm.NOTIFY_FAIL.getValue());
			notifyRecord.setNotifyTimes(notifyTimes);
			try {
				updateNotiStatusTimes(notifyRecord.getUuid(), notifyRecord.getStatus(), notifyRecord.getNotifyTimes());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 修改记录状态
	 * @param notifyRecord
	 */
	public void createNotifyToDB(NotifyRecord notifyRecord){
		notifyService.insert(notifyRecord);
	}
	
	public void updateNotifyToDB(NotifyRecord notifyRecord){
		notifyService.updateSelect(notifyRecord);
	}
	
	public void updateNotiStatusTimes(String uuid, String status, int notifyTimes){
		notifyService.updateNotiStatusTimes(uuid, status, notifyTimes);
	}
}
