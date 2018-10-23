package cn.com.tw.paas.monit.service.inn.idl;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.paas.monit.entity.db.org.CmdExe;


public class TaskThread<T extends HandleService> implements Runnable{
	
	public final static Logger logger = LoggerFactory.getLogger(TaskThread.class);
	
	private long executeTime;
	
	private int priority;
	
	private T t;

	private CmdExe command;

	/**
	 * 
	 * @param taskProcess 当前执行的任务
	 * @param handleService 回调函数
	 * @param executeTime 当前任务执行的时间点（或者说是当前线程的执行时间点）
	 */
	public TaskThread(CmdExe command,T t,long executeTime,int priority){
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

	public CmdExe getCommand() {
		return command;
	}

	public void setCommand(CmdExe command) {
		this.command = command;
	}
	
}
