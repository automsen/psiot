package cn.com.tw.paas.service.notify.queue;

import java.io.IOException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.enm.notify.NotifyTypeEm;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.common.utils.tools.http.HttpGetReq;
import cn.com.tw.common.utils.tools.http.HttpPostReq;
import cn.com.tw.common.utils.tools.http.entity.HttpCode;
import cn.com.tw.common.utils.tools.http.entity.HttpRes;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.paas.service.notify.entity.NotifyRecord;
import cn.com.tw.paas.service.notify.eum.NotifyStatusEm;

/**
 * 线程通知类
 * @author admin
 *
 */
public class NotifyTask implements Runnable, Delayed{
	
	public static final Logger log = LoggerFactory.getLogger(NotifyTask.class);
	
	private long executeTime;
	
	/**
	 * 通知信息
	 */
	private NotifyRecord notifyRecord;
	
	private TaskUtils taskUtils;
	
	private AsyncTaskDelayQueue taskQueue = AsyncTaskDelayQueue.getInstance();
	
	private String hostName = "";
	
	private String port = "";
	
	public NotifyTask(NotifyRecord notifyRecord){
		
		this.notifyRecord = notifyRecord;
		
		this.taskUtils = (TaskUtils) SpringContext.getBean("taskUtils");
		
		this.executeTime = getExecuTime();
		
		hostName = Env.getVal("http.proxy.hostname");
		
		port = Env.getVal("http.proxy.port");
	}
	
	private long getExecuTime(){
		long time = notifyRecord.getLastNotifyTime().getTime();
		return time;
	}
	
	@Override
	public void run() {
		log.debug("{} NotifyTask run(), notifying url is {} ", new Object[]{SysCons.LOG_START, (notifyRecord != null ? notifyRecord.toString() : "null")});
		
		log.info("DelayQueue removing task ：　{} ", this);
		//先移除在队列的自己
		taskQueue.remove(this);
		
		try {
			
			notifyRecord.setNotifyTimes(notifyRecord.getNotifyTimes() + 1);
		
			HttpRes result = null;
			//如果是平台推送 直接推送给平台， 不经过gw服务
			if (NotifyTypeEm.PLAT.getValue() == notifyRecord.getNotifyType()) {
				StringEntity entity = new StringEntity(notifyRecord.getNotifyContent(),"utf-8");//解决中文乱码问题    
		        entity.setContentEncoding("UTF-8");    
		        entity.setContentType("application/json");
		        result = new HttpPostReq(notifyRecord.getNotifyUrl(), null, entity).setProxy(hostName, Integer.parseInt(port)).excuteReturnObj();
			}else {
				result = new HttpGetReq(Env.getVal("gw.server.url") + "/" + notifyRecord.getNotifyUrl()).excuteReturnObj();
			}
		
			log.debug("NotifyTask HttpGetReq, http result = {}", result.toString());
			
			if (!HttpCode.RES_SUCCESS.equals(result.getCode())){
				taskUtils.updateNotiStatusTimes(notifyRecord.getUuid(), NotifyStatusEm.NOTIFY_HTTP_FAIL.getValue(), notifyRecord.getNotifyTimes());
				taskUtils.buildTask(notifyRecord);
				return;
			}
			
			String content = result.getData();
			
			Response<?> resp = JsonUtils.jsonToPojo(content, Response.class);
			
			if (resp == null || !ResultCode.OPERATION_IS_SUCCESS.equals(resp.getStatusCode())){
				
				if ("G100000".equals(resp.getStatusCode())){
					
					taskUtils.updateNotiStatusTimes(notifyRecord.getUuid(), NotifyStatusEm.NOTIFY_SUCCESS_NO_ARRIVE.getValue(), notifyRecord.getNotifyTimes());
					
					return;
				}
				
				taskUtils.updateNotiStatusTimes(notifyRecord.getUuid(), NotifyStatusEm.NOTIFY_HTTP_SUCCESS_BUT_FAIL.getValue(), notifyRecord.getNotifyTimes());
				taskUtils.buildTask(notifyRecord);
				return;
			}
			
			taskUtils.updateNotiStatusTimes(notifyRecord.getUuid(), NotifyStatusEm.NOTIFY_SUCCESS.getValue(), notifyRecord.getNotifyTimes());
			log.debug("{} NotifyTask run(), DelayQueu task completed.", SysCons.LOG_END);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("{} NotifyTask run(), DelayQueu task execute fail, exception = {}.", new Object[]{SysCons.LOG_ERROR, e.toString()});
			
			taskUtils.updateNotiStatusTimes(notifyRecord.getUuid(), NotifyStatusEm.NOTIFY_HTTP_FAIL.getValue(), notifyRecord.getNotifyTimes());
			taskUtils.buildTask(notifyRecord);
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("{} NotifyTask run(), DelayQueu task execute fail, exception = {}.", new Object[]{SysCons.LOG_ERROR, e.toString()});
			return;
		}
		
		
	}

	/**
	 * 排序用
	 */
	@Override
	public int compareTo(Delayed o) {
		NotifyTask task = (NotifyTask) o;
		return executeTime > task.executeTime ? 1 : (executeTime < task.executeTime ? -1 : 0);
	}

	/**
	 * 返回与此对象相关的剩余延迟时间，以给定的时间单位表示
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.SECONDS);
	}

	public long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}

}
