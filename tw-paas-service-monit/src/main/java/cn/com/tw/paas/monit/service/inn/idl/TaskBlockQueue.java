package cn.com.tw.paas.monit.service.inn.idl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.InsExe;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 485 指令执行 队列
 * @author liming
 * 2017年9月22日10:18:53
 *
 */

public class TaskBlockQueue {

	public static final Logger log = LoggerFactory.getLogger(TaskBlockQueue.class);
	public static volatile ConcurrentMap<String, PriorityBlockingQueue<TaskThread<HandleService>>> tasks = new ConcurrentHashMap<String, PriorityBlockingQueue<TaskThread<HandleService>>>();

	
	private final static ExecutorService fixedThreadPool = Executors.newCachedThreadPool();  
	/**
	 * 定义DelayQueue队列
	 */
	private TaskBlockQueue(){
		
	
	}
	/**
	 * 放入任务
	 * @param taskThread
	 */
	@SuppressWarnings("unused")
	public synchronized static void put(TaskThread<HandleService> taskThread){
	    CmdExe cmd =  taskThread.getCommand();
	    InsExe currInn = cmd.getCurrIns();
	    String requestStr = currInn.getParam();
		JSONObject obj = JSONObject.parseObject(requestStr);
		String gwId = obj.getString("gwId");
		if(gwId == null){
			throw new BusinessException(ResultCode.DATA_IS_EMPTY_ERROR, "The current meter`s gwId is empty");
		}
		String gatewayType = obj.getString("gatewayType");
		// 所有lorawan 的指令放入一个队列
//		if(!StringUtils.isEmpty(gatewayType) && gatewayType.equals(GatewayTypeEum.LORAWAN.getValue())){
//			gwId = "LORAWAN";
//		}
		PriorityBlockingQueue<TaskThread<HandleService>> queue = tasks.get(gwId);
		
		if(queue == null){
			queue = new PriorityBlockingQueue<TaskThread<HandleService>>(40,new CmdComparator()) ;
			syncPush push = new syncPush(queue,gwId);
			fixedThreadPool.execute(push);
		}
		
		if (queue.size() >= 50) {
			throw new BusinessException(ResultCode.UNKNOW_ERROR, "req cmd queue is full, queue curr size greater than or equal to 128");
		}
		
		queue.add(taskThread);
		tasks.put(gwId, queue);
	}
		
		
	
		
	/**
	 * 移除网关任务
	 * @param task
	 */
	public synchronized static void remove(String ip){
		if(ip == null){
			return;
		}
		tasks.remove(ip);
	}
		
	/**
	 * 取出元素
	 * @return
	 */
	public synchronized static PriorityBlockingQueue<TaskThread<HandleService>> poll(String ip){
		PriorityBlockingQueue<TaskThread<HandleService>> queue = tasks.get(ip);
		return queue;
	}
		
	public static int size(){
		return tasks.size();
	}
	
  
	  /**
	   * 当有任务来时开启网关线程
	   *
	   */
	  public final static class syncPush implements Runnable{
		  
		  private PriorityBlockingQueue<TaskThread<HandleService>> queue ;
		  
		  
		  public syncPush(PriorityBlockingQueue<TaskThread<HandleService>> queue,String ip){
			  this.queue= queue;
		  }
	
		@Override
		public void run() {
			while(true){
				try {
					TaskThread<HandleService> item = queue.take();
					item.run();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		  
	}
}
