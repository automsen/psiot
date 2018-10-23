package cn.com.tw.engine.core.ws.service;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.engine.core.ChannelManaFactory;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.exception.EngineException;
import cn.com.tw.engine.core.exception.code.EngineCode;
import cn.com.tw.engine.core.handler.http.lorawarn.param.LoRaWanRequestParams;
import cn.com.tw.engine.core.thread.ControlMsgSend;

@Service
public class CmdService {
	
	@Autowired
	private LoRaWanRequestParams LoRaWanReqParams;
	
	public CmdResponse send(CmdRequest req) throws InterruptedException, ExecutionException{
		
		if (req.getDate() == null){
			Date date = new Date();
			req.setDate(date);
			req.setCollecDate(date);
		}
		
			
		String gwId = req.getGwId();
		
		//连接独木桥
		ChannelBridge bridge = ChannelManaFactory.getInstanc().build().get(gwId);
		
		if (bridge == null){
			throw new EngineException(EngineCode.CHANNEL_GW_TIMEOUT, "can't connect server, target connect fail! or device info non-existent!");
		}
		
		//先将信息放入队列
		bridge.putWrite(req);
		
		/*ControlThread controlThread = new ControlThread(bridge, req);*/
		
		//获取队列头,执行任务
		/*Future<CmdResponse> future = poolFactory.submitControlTask(controlThread);*/
		
		//等到结果返回
		//CmdResponse resp = future.get();
		
		ControlMsgSend msgSend = new ControlMsgSend(bridge, req);
		
		//等到结果返回
		CmdResponse resp = msgSend.excute();
		
		return resp;
	}

}
