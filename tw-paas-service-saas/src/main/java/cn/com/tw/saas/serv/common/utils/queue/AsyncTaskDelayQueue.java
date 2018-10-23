package cn.com.tw.saas.serv.common.utils.queue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.saas.serv.common.utils.MeterOpenOrClose;

/**
 * 异步队列
 * @author admin
 *
 */
public class AsyncTaskDelayQueue {
	
	private static Logger log = LoggerFactory.getLogger(AsyncTaskDelayQueue.class);
	
	/**
	 * 定义DelayQueue队列
	 */
	private DelayQueue<MeterOpenOrClose> queue = new DelayQueue<MeterOpenOrClose>();
	
	private ExecutorService threadPool = Executors.newFixedThreadPool(3);
	
	private AsyncTaskDelayQueue(){
		
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				
				boolean isExce = false;
				
				while (true) {
					
					try{
					
						//如果发生异常 等待
						if(isExce){
							Thread.sleep(5000);
							isExce = false;
							continue;
						}
						
						MeterOpenOrClose task = queue.poll();
						if(task == null){
							Thread.sleep(5000);
							continue;
						}
						
						threadPool.execute(task);
					
					} catch (Exception e) {
						isExce = true;
						// TODO Auto-generated catch block
						log.error("{} NotifyTaskInit: single thread of threadTaskPool discovering exception : {} ", new Object[]{SysCons.LOG_ERROR, e.toString()});
						e.printStackTrace();
					}
				}
				
			}
		});
		
	}
	
	private static class Singlon{
		private static AsyncTaskDelayQueue queue= null;
		
		static {
			queue = new AsyncTaskDelayQueue();
		}
		
		public static AsyncTaskDelayQueue getInstance(){
			return queue;
		}
	}
	
	public static AsyncTaskDelayQueue getInstance(){
		return Singlon.getInstance();
	}
	
	/**
	 * 插入 阻塞
	 * @param obj
	 */
	public void put(MeterOpenOrClose obj){
		if (!queue.contains(obj)) {
			queue.put(obj);
		}
	}
	
	/**
	 * 取出 阻塞
	 * @return
	 */
	public Runnable take(){
		try {
			return queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void remove(MeterOpenOrClose obj){
		queue.remove(obj);
	}

}
