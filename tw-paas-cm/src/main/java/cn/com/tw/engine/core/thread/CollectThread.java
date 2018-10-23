package cn.com.tw.engine.core.thread;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.WarnInfo;
import cn.com.tw.engine.core.entity.eum.TermStatus;
import cn.com.tw.engine.core.exception.EngineException;
import cn.com.tw.engine.core.handler.tcp.active.lora.LoraActiveGetChannelManager;
import cn.com.tw.engine.core.thread.abr.AbstractMsgOpera;
import cn.com.tw.engine.core.utils.cons.Cons;

/**
 * 采集线程
 * @author admin
 *
 */
public class CollectThread extends AbstractMsgOpera implements Runnable{
	
	private static Logger logger = LoggerFactory.getLogger(CollectThread.class);
	
	private CountDownLatch countDownLatch;
	
	public CollectThread(ChannelBridge channelBridge){
		super(channelBridge);
	}
	
	public CollectThread(ChannelBridge channelBridge, CountDownLatch countDownLatch){
		super(channelBridge);
		this.countDownLatch = countDownLatch;
	}
	
	@Override
	public void run() {
		
		try {
			while(true){
				CmdRequest cmdReq = null;
				try {
					cmdReq = channelBridge.peek();
					
					if (cmdReq == null){
						break;
					}
					
					//如果有控制指令进来 则暂停1s 然后在执行
					if (cmdReq.getCmdLvl() >= Cons.LVL_CMD_CONTROL){
						
						logger.warn("control cmd is running, collect cmd sleep 1(s) !!!!!");
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {

							e.printStackTrace();
						}
						
						continue;
					}
					
					logger.debug(SysCons.LOG_START + " CollectThread sending cmd req, req = " + (cmdReq == null ? "" : cmdReq.toString()));
					
					sendMsg(cmdReq);
					
					logger.debug(SysCons.LOG_END + " CollectThread send cmd req success!!!");
					
					/** 暂时加在这里 **/
					//暂时加在这里， 一个线程消费队列中的数据， 如果是lorawan，每个指令请求间隔1.5s
					if (channelManaFactory.build() instanceof LoraActiveGetChannelManager){
						Thread.sleep(getLoraIntervalTime() * 1000);
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
					
					logger.error(SysCons.LOG_ERROR + "CollectThread send cmd req fail!!!, e = {}", e);
				} finally {
					if (cmdReq != null){
						channelBridge.remove(cmdReq);
					}
				}
			}
		} finally {
			if (countDownLatch != null){
				countDownLatch.countDown();
			}
		}
		
	}
	
	public void sendMsg(CmdRequest cmdReq) throws Exception{
		
		synchronized (channelBridge) {
			
			//获取返回值，如果没有30秒没有返回值  阻塞,返回null；
			CmdResponse cmdResponse = sendAndResp(cmdReq, channelBridge);
			
			//如果是Lora，获取返回值直接走回调接口，不执行下面的操作
			if (channelManaFactory.build() instanceof LoraActiveGetChannelManager) {
				return;
			}
			
			//如果返回为空
			if (cmdResponse == null){
				
				logger.error("send cmd req, collect result is error, request timeout " + getTimeOut(cmdReq.getMeterType()) + "(s)， retrying。。。。。。，, req = " + cmdReq.toString());
				msgAysncService.sendWarnInfo(new WarnInfo(cmdReq.getMeterAddr(), cmdReq.getMeterType(), TermStatus.close.getValue()));
				
				if (cmdReq.getCmdLvl() >= Cons.LVL_CMD_CONTROL){
					//return null;
				}else{
					if(cmdReq.getSendNum() < getSendNum()) {
						cmdReq.setSendNum(cmdReq.getSendNum() + 1);
						cmdReq.setDate(new Date());
						channelBridge.put(cmdReq);
					}
				}
				
				throw new EngineException("", "send cmd retrying " + cmdReq.getSendNum() + " times error !!!!!");
				
			}
			
			if (!cmdResponse.getStatusCode().equals(ResultCode.OPERATION_IS_SUCCESS)){
				
				logger.error("send cmd req, collect result code is error, cmdResponse = {}", cmdResponse.toString());
				
				return;
			}
			
			//获取仪表ID + 仪表类型 + 时间搓 + 数据项的数量 组成的唯一标识
			String uniqueId = cmdReq.getMeterAddr() + ":" + cmdReq.getCollecDate().getTime();
			cmdResponse.setUniqueId(uniqueId);
			
			msgAysncService.sendWarnInfo(new WarnInfo(cmdReq.getMeterAddr(), cmdReq.getMeterType(), TermStatus.open.getValue()));
			
			//如果是后续针数据项，继续读取后续数据
			cmdResponse = isFollowOperate(cmdReq, cmdResponse);
			
			msgAysncService.sendCollectRes(cmdResponse);
			
			//特殊处理，关于事件状态字，如果返回了事件状态字，这个时候异步去下发复位指令，
			eventStatusHandle(cmdResponse, cmdReq);
			logger.info("send sendMsg, collect result : {}", cmdResponse.toString());
			
		}
	}
	
	@Override
	protected int getTimeOut(String meterType) {
		
		if (channelManaFactory.build() instanceof LoraActiveGetChannelManager) {
			return 0;
		}
		
		return getTimeOutConfig(meterType);
	}

	@Override
	protected boolean isPrior() {
		return false;
	}

}
