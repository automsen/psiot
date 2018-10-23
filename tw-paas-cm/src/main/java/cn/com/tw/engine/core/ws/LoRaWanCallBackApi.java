package cn.com.tw.engine.core.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.protocol.ParseEnum;
import cn.com.tw.common.protocol.ParseFactory;
import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.utils.ByteUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.engine.core.ChannelManaFactory;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.enm.ModeEnum;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.CmdResult;
import cn.com.tw.engine.core.handler.http.lorawarn.param.SimpleResultBean;
import cn.com.tw.engine.core.utils.MsgAysncService;


/**
 * lorawan callback
 * @author liming
 * 2017年9月21日15:10:15
 */
@RestController
@RequestMapping("ws/lorawan")
public class LoRaWanCallBackApi {
	
	private static Logger logger = LoggerFactory.getLogger(LoRaWanCallBackApi.class);
	
	@Autowired
	public MsgAysncService msgAysncService;
	
	@Value("${engine.mode}")
	private String modeType;
	
	@PostMapping(value="simple")
	public Map<String,Object> simpleCallback(@RequestBody(required=false) SimpleResultBean bean){
		
		logger.debug("---------->> receive lorawan plat callback, params SimpleResultBean = {}", bean);
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		/*resultMap.put("msg", "fail");
		resultMap.put("code", 1);*/
		resultMap.put("msg", "success");
		resultMap.put("code", 0);
		
		if (!ModeEnum.lorawan_active_get.name().equals(modeType)){
			/*resultMap.put("code", 0);
			resultMap.put("msg", "This service is unavailable");*/
			logger.debug("<<---------- return info, result = {}", resultMap.toString());
			return resultMap;
		}
		
		
		//返回的心跳执行返回执行成功
		if("223".equals(bean.getData_type()) || "0".equals(bean.getData_type())){
			/*resultMap.put("code", 0);
			resultMap.put("msg", "success");*/
			logger.debug("<<---------- return info, result = {}", resultMap.toString());
			return resultMap;
		}
		
		try {
			CmdResponse cmdRes = new CmdResponse();
		
			//执行结果
			if("2".equals(bean.getData_type())){
				
				//判断当前命令执行的指令
				String c = bean.getData();
				
				try {
					Dlt645Data data= (Dlt645Data) ParseFactory.build(ParseEnum.DLT645V2007).decode(ByteUtils.toByteArray(c));
					
					if (data == null || StringUtils.isEmpty(data.getAddr())) {
						logger.error("Parse data data is null or dataMarker or addr is null, {}", data == null ? null : data.toString());
						logger.debug("<<---------- return info, result = {}", resultMap.toString());
						return resultMap;
					}
					
					/*if (!data.isSuccess()) {
						logger.error("Parse data isSuccess = false , {}", data == null ? null : data.toString());
						return resultMap;
					}*/
					
					cmdRes.setStatusCode(ResultCode.OPERATION_IS_SUCCESS);
					cmdRes.setData(data);
					String uniqueId = data.getAddr() + ":" + new Date().getTime();
					cmdRes.setUniqueId(uniqueId);
					cmdRes.setExt("mac", bean.getMac());
				} catch(Exception e){
					logger.error("ParseFactory Exception e = {}", e);
					logger.debug("<<---------- return info, result = {}", resultMap.toString());
					return resultMap;
				}
				
				//放入结果队列
				ChannelBridge bridge = ChannelManaFactory.getInstanc().build().get(bean.getMac());
				//ChannelBridge bridge = ChannelManaFactory.getBridge(bean.getMac());
				
				/** 该判断暂时加上， 后面可能去掉 **/
				if (bridge.getPriorityQueue().size() > 0) {
					bridge.putResultRemDuplicate(new CmdResult(cmdRes));
				}
				
				//将采集的数据放入mq
				msgAysncService.sendCollectRes(cmdRes);
				
				logger.debug("<<---------- return info, result = {}", resultMap.toString());
				return resultMap;
			}
		} catch (Exception e) {
			//cmdRes.setStatusCode(ResultCode.UNKNOW_ERROR);
			//cmdRes.setMessage(e.getMessage());
			//resultMap.put("msg", "fail");
			//resultMap.put("code", 1);
			logger.error("LoRaWanCallBackApi Exception e = {}", e);
		}
		
		logger.debug("<<---------- return info, result = {}", resultMap.toString());
		return resultMap;
	}
}
