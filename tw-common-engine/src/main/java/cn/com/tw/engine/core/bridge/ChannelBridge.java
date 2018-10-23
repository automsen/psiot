package cn.com.tw.engine.core.bridge;

import io.netty.channel.Channel;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.engine.core.bridge.compa.CmdComparator;
import cn.com.tw.engine.core.bridge.compa.ResultComparator;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.entity.CmdResult;
import cn.com.tw.engine.core.exception.EngineException;
import cn.com.tw.engine.core.exception.code.EngineCode;
import cn.com.tw.engine.core.utils.cons.Cons;

/**
 * 创建1个和网关的独木桥
 * @author admin
 *
 */
public class ChannelBridge{
	
	/**
	 * 定义通信管道
	 */
	private Channel channel;
	
	private PriorityBlockingQueue<CmdRequest> priorityQueue = null;
	
	/**
	 * 接受返回结果
	 */
	private PriorityBlockingQueue<CmdResult> resultPriorityQueue = null;
	
	/**
	 * 信道是否被占用
	 */
	private volatile boolean isEngross = false;
	
	/**
	 * 定义写命令 计数器
	 */
	private AtomicInteger writeCmdCount = new AtomicInteger();
	
	private byte[] lock = new byte[0];
	
	private byte[] pLock = new byte[0];
	
	public ChannelBridge(Channel channel){
		this.channel = channel;
		priorityQueue = new PriorityBlockingQueue<CmdRequest>(40, new CmdComparator());
		resultPriorityQueue = new PriorityBlockingQueue<CmdResult>(20, new ResultComparator());
	}
	
	public ChannelBridge(Channel channel, int reqQueueSize, int resQueueSize){
		this.channel = channel;
		priorityQueue = new PriorityBlockingQueue<CmdRequest>(reqQueueSize, new CmdComparator());
		resultPriorityQueue = new PriorityBlockingQueue<CmdResult>(resQueueSize, new ResultComparator());
	}
	
	public void setEngross(boolean isEngross) {
		this.isEngross = isEngross;
	}
	
	public boolean isEngross() {
		return isEngross;
	}
	
	/**
	 * 投放
	 * @param cmdTask
	 */
	public void put(CmdRequest cmdTask){
		priorityQueue.put(cmdTask);
	}
	
	public void putResult(CmdResult cmdResult){
		resultPriorityQueue.put(cmdResult);
	}
	
	public void putResultRemDuplicate(CmdResult cmdResult){
		synchronized (lock) {
			if (resultPriorityQueue.contains(cmdResult)){
				return;
			}
			
			resultPriorityQueue.put(cmdResult);
		}
	}
	
	public void removeResult(CmdResult cmdResult){
		resultPriorityQueue.remove(cmdResult);
	}
	
	public int getQueueSize(){
		return priorityQueue.size();
	}
	
	/**
	 * 去重
	 * @param cmdTask
	 */
	public void putRemDuplicate(CmdRequest cmdTask){
		
		synchronized (pLock) {
			if (priorityQueue.contains(cmdTask)){
				return;
			}
			
			priorityQueue.put(cmdTask);
		}
		
	}
	
	
	/**
	 * 投放
	 * @param cmdTask
	 */
	public void putWrite(CmdRequest cmdTask){
		
		int count = writeCmdCount.get();
		
		System.out.println(count);
		
		//如果存在写命令，则等待，重新插入数据
		if (cmdTask == null || cmdTask.getCmdLvl() == Cons.LVL_CMD_COLLECT){
			throw new EngineException(EngineCode.CHANNEL_INSERT_CONTRO_ERROR, " insert cmd is not contro cmd ");
		}
		
		int maxNum = 10;
		try {
			maxNum = Integer.parseInt(Env.getVal("engine.gw.downcmd.allow.maxNum"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if (count > maxNum){
			throw new EngineException(EngineCode.CHANNEL_ENGROSS, " cmd waiting count is too many, maxNum = " + maxNum + " and count = " + count );
		}
		
		priorityQueue.put(cmdTask);
		
		/**
		 * 递增
		 */
		writeCmdCount.incrementAndGet();
	}
	
	
	/**
	 * 取出
	 * @return
	 */
	public CmdRequest take(){
		
		try {
			
			CmdRequest cmd = priorityQueue.take();
			
			if (cmd != null && cmd.getCmdLvl() >= Cons.LVL_CMD_CONTROL) {
				writeCmdCount.decrementAndGet();
			}
			
			return cmd;
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 取出
	 * @return
	 */
	public CmdRequest pool(){
		CmdRequest cmd = priorityQueue.poll();
		
		if (cmd != null && cmd.getCmdLvl() >= Cons.LVL_CMD_CONTROL) {
			writeCmdCount.decrementAndGet();
		}
		return cmd;
	}
	
	/**
	 * 取出列头但不移除
	 * @return
	 */
	public CmdRequest peek(){
		
		CmdRequest cmd = priorityQueue.peek();
		
		return cmd;
	}
	
	/**
	 * 取出列头移除
	 * @return
	 */
	public CmdResult pollResult(int seconds){
		try {
			CmdResult cmd = resultPriorityQueue.poll(seconds, TimeUnit.SECONDS);
			return cmd;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 取出列头移除
	 * @return
	 */
	public CmdResult pollResult(){
		CmdResult cmd = resultPriorityQueue.poll();
		return cmd;
	}
	
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public void remove(CmdRequest cmdTask){
		
		boolean flag = priorityQueue.remove(cmdTask);
		
		if (flag){
			writeCmdCount.decrementAndGet();
		}
		
	}
	
	/**
	 * 发送消息
	 * @param cmdTask
	 * @return
	 */
	/*public synchronized CmdResponse sendMsg(CmdRequest cmdTask){
		
		String channelId = null;
		try {
			
			if (channel == null || !channel.isActive()){
				return null;
			}
			
			channelId = channel.id().asLongText();
			
			
			ResponseMap.put(channelId, new ArrayBlockingQueue<CmdResponse>(1));
			
			//将命令发送给网关
			channel.writeAndFlush(cmdTask);
			
			//然后等待服务器响应
			BlockingQueue<CmdResponse> bq = ResponseMap.get(channelId);
			
			//获取返回值，如果没有30秒没有返回值  阻塞,返回null；
			CmdResponse cmdResponse = bq.poll(5, TimeUnit.SECONDS);
			
			if (cmdResponse == null){
				
				if (cmdTask.getCmdLvl() >= Cons.LVL_CMD_CONTROL){
					
					return null;
					
				}else{
					
					if(cmdTask.getSendNum() >= 2) {
						return null;
					}
					
					cmdTask.setSendNum(cmdTask.getSendNum() + 1);
					cmdTask.setDate(new Date());
					put(cmdTask);
					
					return null;
				}
				
			}
			
			//获取仪表ID + 仪表类型 + 时间搓 + 数据项的数量 组成的唯一标识
			String uniqueId = cmdTask.getMeterAddr() + ":" + cmdTask.getDate().getTime();
			cmdResponse.setUniqueId(uniqueId);
			
			return cmdResponse;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {
			if(channelId != null){
				ResponseMap.del(channelId);
			}
		}
		
		return null;
		
	}*/
	
	public synchronized void channelColse(){
		if (channel != null){
			channel.close();
		}
	}
	
	public void updateChannel(Channel channel) {
		this.channel = channel;
	}

	public PriorityBlockingQueue<CmdRequest> getPriorityQueue() {
		return priorityQueue;
	}

	public void setPriorityQueue(PriorityBlockingQueue<CmdRequest> priorityQueue) {
		this.priorityQueue = priorityQueue;
	}

	public PriorityBlockingQueue<CmdResult> getResultPriorityQueue() {
		return resultPriorityQueue;
	}

	public void setResultPriorityQueue(
			PriorityBlockingQueue<CmdResult> resultPriorityQueue) {
		this.resultPriorityQueue = resultPriorityQueue;
	}

}
