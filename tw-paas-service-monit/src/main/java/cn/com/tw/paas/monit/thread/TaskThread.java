package cn.com.tw.paas.monit.thread;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;


public class TaskThread<T extends HandleService> implements Runnable{
	
	public final static Logger logger = LoggerFactory.getLogger(TaskThread.class);
	
	private long executeTime;
	
	private int priority;
	
	private T t;

	private BaseCmdEXE command;

	/**
	 * 
	 * @param taskProcess 当前执行的任务
	 * @param handleService 回调函数
	 * @param executeTime 当前任务执行的时间点（或者说是当前线程的执行时间点）
	 */
	public TaskThread(BaseCmdEXE command,T t,long executeTime,int priority){
		this.executeTime = executeTime;
		this.priority = priority;
		this.t = t;
		this.command = command;
	}
	
	@Override
	public void run() {
		HandleService service = (HandleService) t;
		service.exceResult(command);
	}


	public long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}


	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public BaseCmdEXE getCommand() {
		return command;
	}

	public void setCommand(BaseCmdEXE command) {
		this.command = command;
	}
	
}
