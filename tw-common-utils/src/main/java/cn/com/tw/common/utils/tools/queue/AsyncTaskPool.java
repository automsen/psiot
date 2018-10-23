package cn.com.tw.common.utils.tools.queue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTaskPool {
	
	private ExecutorService threadPool = null;
	
	private AsyncTaskPool(){
		this.threadPool = Executors.newFixedThreadPool(5);
	}
	
	private static class Singlon{
		private static AsyncTaskPool task = null;
		
		static {
			task = new AsyncTaskPool();
		}
		
		public static AsyncTaskPool getInstance(){
			return task;
		}
	}
	
	public static AsyncTaskPool getInstance(){
		return Singlon.getInstance();
	}
	
	public void excuteTask(Runnable obj){
		threadPool.execute(obj);
	}

}
