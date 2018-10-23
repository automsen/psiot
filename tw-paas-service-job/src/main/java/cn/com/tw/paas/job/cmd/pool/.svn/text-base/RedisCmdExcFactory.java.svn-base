package cn.com.tw.paas.job.cmd.pool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.com.tw.paas.job.cmd.service.inn.CmdHandle;

/**
 * 
 *  指令执行 队列
 * @author liming
 * 2017年9月22日10:18:53
 *
 */

@Component
public class RedisCmdExcFactory {

	public static final Logger log = LoggerFactory.getLogger(RedisCmdExcFactory.class);
	
	
	public static volatile ConcurrentMap<String,HandleCmd > tasks = new ConcurrentHashMap<String,HandleCmd>();

	private final static ExecutorService fixedThreadPool = Executors.newScheduledThreadPool(10);  
	
	
	@Autowired
	private CmdHandle baseCmdExecute;
	
	/**
	 * 定义DelayQueue队列
	 */
	private RedisCmdExcFactory(){
	}
	

	
	public synchronized void put(String orgGwId){
		
		// 判断指令来源
		HandleCmd currThread = tasks.get(orgGwId);
		if(currThread == null){
			currThread = new HandleCmd(orgGwId,baseCmdExecute);
			tasks.put(orgGwId,currThread);
			fixedThreadPool.execute(currThread);
			return;
		}
	}
	
	
	class HandleCmd implements Runnable{
		
		private String orgGwId;
		
		private CmdHandle handle;
		
		public HandleCmd(String orgGwId,CmdHandle handle){
			this.orgGwId = orgGwId;
			this.handle = handle;
		}

		@Override
		public void run() {
			try {
				handle.handle(orgGwId);
			} catch (Exception e) {
			}
		}
	}
}
