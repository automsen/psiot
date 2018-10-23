package cn.com.tw.common.utils.tools.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步队列
 * @author admin
 *
 */
public class AsyncTaskQueue {
	
	private ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(50);
	
	private ExecutorService threadPool = Executors.newFixedThreadPool(3);
	
	private AsyncTaskQueue(){
		
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				
				while (true) {
					
					try {
						Runnable obj = queue.take();
						threadPool.execute(obj);
					} catch (InterruptedException e) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
					
				}
				
			}
		});
		
	}
	
	private static class Singlon{
		private static AsyncTaskQueue queue= null;
		
		static {
			queue = new AsyncTaskQueue();
		}
		
		public static AsyncTaskQueue getInstance(){
			return queue;
		}
	}
	
	public static AsyncTaskQueue getInstance(){
		return Singlon.getInstance();
	}
	
	public void excuteTask(Runnable obj){
		threadPool.execute(obj);
	}
	
	/**
	 * 插入 阻塞
	 * @param obj
	 */
	public void put(Runnable obj){
		try {
			queue.put(obj);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}
