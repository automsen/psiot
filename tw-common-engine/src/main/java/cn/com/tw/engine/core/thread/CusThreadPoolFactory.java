package cn.com.tw.engine.core.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.com.tw.engine.core.entity.CmdResponse;

/**
 * 线程池工厂
 * @author admin
 *
 */
public class CusThreadPoolFactory {
	
	private ExecutorService collectPool;
	
	private ExecutorService controlPool;
	
	private CusThreadPoolFactory(){
		this.collectPool = Executors.newFixedThreadPool(5);
		this.controlPool = Executors.newFixedThreadPool(2);
	}
	
	private static class Singlon{
		private static CusThreadPoolFactory poolFactory= null;
		
		static {
			poolFactory = new CusThreadPoolFactory();
		}
		
		public static CusThreadPoolFactory getInstance(){
			return poolFactory;
		}
	}
	
	public static CusThreadPoolFactory build(){
		return Singlon.getInstance();
	}
	
	public void excuteControlTask(Runnable obj){
		controlPool.execute(obj);
	}
	
	public Future<CmdResponse> submitControlTask(Callable<CmdResponse> obj){
		return controlPool.submit(obj);
	}
	
	public void excuteCollectTask(Runnable obj){
		collectPool.execute(obj);
	}
	
	public Future<CmdResponse> submitCollectTask(Callable<CmdResponse> obj){
		return collectPool.submit(obj);
	}
	
	public ExecutorService getCollectPool() {
		return collectPool;
	}

	public ExecutorService getControlPool() {
		return controlPool;
	}

	public void shutdown(){
		collectPool.shutdown();
		controlPool.shutdown();
	}
	
}
