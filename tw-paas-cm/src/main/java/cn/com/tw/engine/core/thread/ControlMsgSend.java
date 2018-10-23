package cn.com.tw.engine.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.WarnInfo;
import cn.com.tw.engine.core.entity.eum.TermStatus;
import cn.com.tw.engine.core.exception.EngineException;
import cn.com.tw.engine.core.exception.code.EngineCode;
import cn.com.tw.engine.core.thread.abr.AbstractMsgOpera;
import cn.com.tw.engine.core.utils.cons.Cons;

/**
 * 控制指令线程
 * @author admin
 *
 */
public class ControlMsgSend extends AbstractMsgOpera{
	
	private static Logger logger = LoggerFactory.getLogger(ControlMsgSend.class);
	
	private CmdRequest currentCmdReq;
	
	public ControlMsgSend(ChannelBridge channelBridge, CmdRequest currentCmdReq){
		super(channelBridge);
		this.currentCmdReq = currentCmdReq;
	}

	public CmdResponse excute() throws InterruptedException{
		
		try {
			logger.debug("ControlMsgSend sending cmd req, req = " + (currentCmdReq == null ? "" : currentCmdReq.toString()));
			
			if (currentCmdReq.getCmdLvl() == Cons.LVL_CMD_COLLECT){
				throw new EngineException(EngineCode.CHANNEL_CONTRO_SEND_ERROR, "cmd control operate error!!, cmd type must be 20");
			}
			
			CmdResponse cmdRes = sendMsg(currentCmdReq);
			
			logger.debug("ControlMsgSend send cmd req complete!!!");
			
			return cmdRes;
		} finally {
			channelBridge.remove(currentCmdReq);
		}
		
	}
	
	private CmdResponse sendMsg(CmdRequest cmdReq) throws InterruptedException{
		
		synchronized (channelBridge) {
			
			logger.debug("get lock, begin sendAndResp...");
				
			//获取返回值，如果没有30秒没有返回值  阻塞,返回null；
			CmdResponse cmdResponse = sendAndResp(cmdReq, channelBridge);
			
			if (cmdResponse == null){
				
				if (cmdReq.getCmdLvl() >= Cons.LVL_CMD_CONTROL){
					
					logger.error("send cmd req api, control result is null, gw request meter  timeout " + getTimeOut(cmdReq.getMeterType()) + "(s)!!!!");
					
					msgAysncService.sendWarnInfo(new WarnInfo(cmdReq.getMeterAddr(), cmdReq.getMeterType(), TermStatus.close.getValue()));
					
					throw new EngineException(EngineCode.CHANNEL_REQUEST_TIMEOUT, "request meter timeout !! ");
					
				}
				
			}
			
			if (StringUtils.isEmpty(cmdResponse.getUniqueId())) {
				//获取仪表ID + 仪表类型 + 时间搓 + 数据项的数量 组成的唯一标识
				String uniqueId = cmdReq.getMeterAddr() + ":" + cmdReq.getDate().getTime();
				cmdResponse.setUniqueId(uniqueId);
			}
			
			msgAysncService.sendWarnInfo(new WarnInfo(cmdReq.getMeterAddr(), cmdReq.getMeterType(), TermStatus.open.getValue()));
			
			//修改----通过response返回的后续帧判断是否有后续帧，控制码如果是B1，就有后续帧，如果不是就没有后续帧
			cmdResponse = isFollowOperate(cmdReq, cmdResponse);
			
			//特殊处理，关于事件状态字，如果返回了事件状态字，这个时候异步去下发复位指令，
			eventStatusHandle(cmdResponse, cmdReq);
			
			/*Object data = cmdResponse.getData();
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
				
			}*/
			
			return cmdResponse;
				
		}
	}

	@Override
	protected int getTimeOut(String meterType) {
		
		return getTimeOutConfig(meterType);
	}

	@Override
	protected boolean isPrior() {
		return true;
	}
	
}
