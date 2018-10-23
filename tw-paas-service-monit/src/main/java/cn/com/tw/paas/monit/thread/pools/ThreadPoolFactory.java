package cn.com.tw.paas.monit.thread.pools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定义线程池
 * @author admin
 *
 */
public class ThreadPoolFactory {
	
	private static ExecutorService cachedThreadPool = null;
	
	private ThreadPoolFactory(){
		
	}
	
	/**
	 * 创建一个线程池实例
	 * @return
	 */
	public static ExecutorService build(){
		if(cachedThreadPool == null){
			synchronized (ThreadPoolFactory.class) {
				if(cachedThreadPool == null){
					cachedThreadPool = Executors.newCachedThreadPool();
				}
			}
		}
		return cachedThreadPool;
	}
	
	public static void excute(Runnable runnable){
		build().execute(runnable);
	}
}
