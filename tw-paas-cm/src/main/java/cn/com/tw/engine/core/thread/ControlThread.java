package cn.com.tw.engine.core.thread;
/*package cn.com.tw.engine.g2.thread;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.WarnInfo;
import cn.com.tw.engine.core.entity.eum.TermStatus;
import cn.com.tw.engine.core.exception.EngineException;
import cn.com.tw.engine.core.exception.code.EngineCode;
import cn.com.tw.engine.core.utils.cons.Cons;
import cn.com.tw.engine.g2.thread.abr.AbstractMsgOpera;

*//**
 * 控制指令线程
 * @author admin
 *
 *//*
public class ControlThread extends AbstractMsgOpera implements Callable<CmdResponse>{
	
	private static Logger logger = LoggerFactory.getLogger(ControlThread.class);
	
	private CmdRequest currentCmdReq;
	
	public ControlThread(ChannelBridge channelBridge, CmdRequest currentCmdReq){
		super(channelBridge);
		this.currentCmdReq = currentCmdReq;
	}

	@Override
	public CmdResponse call() throws InterruptedException{
		
		logger.debug("ControlThread sending cmd req, req = " + (currentCmdReq == null ? "" : currentCmdReq.toString()));
		
		CmdRequest req = channelBridge.peek();
		
		if (currentCmdReq.getCmdLvl() == Cons.LVL_CMD_COLLECT){
			throw new EngineException(EngineCode.CHANNEL_CONTRO_SEND_ERROR, "cmd control operate error!!, cmd type must be 20");
		}
		
		if (!currentCmdReq.equals(req)){
			
			while (true){
				
				try {
					Thread.sleep(200);
					
					req = channelBridge.peek();
					if (currentCmdReq.equals(req)){
						break;
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		channelBridge.remove(req);
		
		channelBridge.remove(currentCmdReq);
		
		CmdResponse cmdRes = sendMsg(currentCmdReq);
		
		logger.debug("ControlThread send cmd req complete!!!");
		
		return cmdRes;
		
	}
	
	public CmdResponse sendMsg(CmdRequest cmdReq) throws InterruptedException{
		
		synchronized (channelBridge) {
				
			//获取返回值，如果没有30秒没有返回值  阻塞,返回null；
			CmdResponse cmdResponse = sendAndResp(cmdReq, channelBridge);
			
			if (cmdResponse == null){
				
				if (cmdReq.getCmdLvl() >= Cons.LVL_CMD_CONTROL){
					
					logger.error("send cmd req api, control result is null, gw request meter  timeout " + getTimeOut(cmdReq.getMeterType()) + "(s)!!!!");
					
					msgAysncService.sendWarnInfo(new WarnInfo(cmdReq.getMeterAddr(), cmdReq.getMeterType(), TermStatus.close.getValue()));
					
					throw new EngineException(EngineCode.CHANNEL_REQUEST_TIMEOUT, "request meter timeout !! ");
					
				}
				
			}
			
			//获取仪表ID + 仪表类型 + 时间搓 + 数据项的数量 组成的唯一标识
			String uniqueId = cmdReq.getMeterAddr() + ":" + cmdReq.getDate().getTime();
			cmdResponse.setUniqueId(uniqueId);
			
			msgAysncService.sendWarnInfo(new WarnInfo(cmdReq.getMeterAddr(), cmdReq.getMeterType(), TermStatus.open.getValue()));
			
			//如果是后续针数据项，继续读取后续数据
			cmdResponse = isFollowOperate(cmdReq, cmdResponse);
			
			Object data = cmdResponse.getData();
			//如果是645Data
			if (data instanceof DLT645Data){
				
				DLT645Data dlt645Data = (DLT645Data)data;
				
				if (cmdReq.getMeterAddr().equals(dlt645Data.getAddr())){
					return cmdResponse;
				}else{
					CmdResponse cmdResp = new CmdResponse();
					
					cmdResp.setStatusCode(EngineCode.CHANNEL_DATA_MATCH_ERROR);
					
					cmdResp.setMessage("request and reponse data match error!!");
					
					logger.error("request and reponse data match error!! , response = {}", cmdResp.toString());
					
					return cmdResp;
				}
				
			}
			
			return cmdResponse;
				
		}
	}

	@Override
	protected int getTimeOut(String meterType) {
		
		return getTimeOutConfig(meterType);
	}
	
}
*/