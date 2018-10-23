package cn.com.tw.engine.core.thread.abr;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.protocol.ParseEnum;
import cn.com.tw.common.protocol.ParseFactory;
import cn.com.tw.common.protocol.dlt645.application.LongRead;
import cn.com.tw.common.protocol.dlt645.cons.Dlt645ControlCode;
import cn.com.tw.common.protocol.utils.ByteUtils;
import cn.com.tw.common.utils.tools.http.HttpGetReq;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.engine.core.ChannelManaFactory;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.debug.DebugManager;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.CmdResult;
import cn.com.tw.engine.core.exception.EngineException;
import cn.com.tw.engine.core.exception.code.EngineCode;
import cn.com.tw.engine.core.handler.eum.ProtocolEum;
import cn.com.tw.engine.core.handler.http.lorawarn.param.LoRaWanRequestParams;
import cn.com.tw.engine.core.handler.tcp.active.etnet.EtnetActiveGetChannelManager;
import cn.com.tw.engine.core.handler.tcp.active.lora.LoraActiveGetChannelManager;
import cn.com.tw.engine.core.handler.tcp.active.lora.MqLoraActiveGetChannelManager;
import cn.com.tw.engine.core.handler.tcp.passive.etnet.EtnetChannelManager;
import cn.com.tw.engine.core.utils.MsgAysncService;
import cn.com.tw.engine.core.utils.cons.Cons;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractMsgOpera {
	
	public ChannelBridge channelBridge;
	
	public MsgAysncService msgAysncService;
	
	private static Logger logger = LoggerFactory.getLogger(AbstractMsgOpera.class);
	
	public ChannelManaFactory channelManaFactory = ChannelManaFactory.getInstanc();
	
	private LoRaWanRequestParams loRaWanReqParams;
	
	public AbstractMsgOpera(){
		
	}
	
	public AbstractMsgOpera(ChannelBridge channelBridge){
		
		this.channelBridge = channelBridge;
		
		this.msgAysncService = (MsgAysncService) SpringContext.getBean("msgAysncService");
		
		this.loRaWanReqParams = (LoRaWanRequestParams) SpringContext.getBean("loRaWanRequestParams");
	}
	
	/**
	 * 发送并返回结果
	 * @param cmdReq
	 * @param channelBridge
	 * @return
	 * @throws InterruptedException
	 */
	protected CmdResponse sendAndResp(CmdRequest cmdReq, ChannelBridge channelBridge) throws InterruptedException{
		
		Channel channel = channelBridge.getChannel();
		
		if (channelManaFactory.build() instanceof EtnetActiveGetChannelManager){
			logger.debug("method sendAndResp...EtnetActiveGetChannelManager,channel status = {}", channel.isActive());
			if (channel == null || !channel.isActive()){
				logger.error("channel is not active!!!!");
				return null;
			}
			
			//如果channel 断开 ，重连，没有断开 跳过
			((EtnetActiveGetChannelManager)channelManaFactory.build()).reconnect(cmdReq.getGwId(), channelBridge);
			
			logger.debug("channel writeAndFlush...");
			channel.writeAndFlush(cmdReq);
			
		} else if (channelManaFactory.build() instanceof EtnetChannelManager){
			
			ChannelGroup channelGroup = ((EtnetChannelManager)channelManaFactory.build()).getChannelGroup();
			
			channelGroup.writeAndFlush(cmdReq);
			
		} else if (channelManaFactory.build() instanceof LoraActiveGetChannelManager){
			
			//ChannelGroup channelGroup = ((LoraChannelManager)ChannelManaFactory.build()).getChannelGroup();
			try {
				lorawanSend(cmdReq);
			} catch (EngineException e){
				return CmdResponse.retn(e.getCode(), e.getMessage());
			} catch (Exception e) {
				return CmdResponse.retn(EngineCode.CHANNEL_UNKNOW, e.getMessage());
			}
			
		} else if (channelManaFactory.build() instanceof MqLoraActiveGetChannelManager){
			
			MqLoraActiveGetChannelManager mqLoraChannelManager = (MqLoraActiveGetChannelManager) channelManaFactory.build();
			
			mqLoraChannelManager.send(ali2Lorawan(cmdReq));
			
		} else {
			if (channel == null || !channel.isActive()){
				throw new EngineException(EngineCode.CHANNEL_GW_TIMEOUT, "channel is not active!!!!");
			}
			
			channel.writeAndFlush(cmdReq);
		}
		
		//获取请求结果
		CmdResponse cmdResponse = getResult(cmdReq);
		
		return cmdResponse;
	}
	
	protected abstract int getTimeOut(String meterType);
	
	/**
	 * 暂时只针对于慧联 lorawan
	 * @return
	 */
	protected abstract boolean isPrior();
	
	/**
	 * channel将信息写入管道
	 * @param cmdReq
	 * @param channelObj
	 * @param channelId
	 * @return
	 * @throws InterruptedException
	 */
	protected CmdResponse getResult(CmdRequest cmdReq) throws InterruptedException{
		
		try {
			
			int timeout = getTimeOut(cmdReq.getMeterType());
			
			long currTime = new Date().getTime();
			
			while (true){
				
				long runTime = new Date().getTime();
				
				//获取poll需要等待的剩余时间
				int pollTimeOut = timeout - (int)((runTime - currTime) / 1000);
				pollTimeOut = pollTimeOut > 0 ? pollTimeOut : 1;
				
				CmdResult cmdResult = channelBridge.pollResult(pollTimeOut);
				
				//如果超过timeout时间，则返回
				if (cmdResult == null){
					logger.warn("pollTimeOut = {}s, timeout = {}s", new Object[]{pollTimeOut, timeout});
					break;
				}
				
				//如果次数大于等于5 则删除
				/*if (cmdResult.getNum() > 5){
					continue;
				}*/
				
				CmdResponse resp = cmdResult.getCmdResponse();
				
				String mac = resp.getExt() == null ? "" : (String) resp.getExt().get("mac");
				
				//将返回结果的data转化map，便于获取
				Map<String, Object> result = parseResult(resp);
				
				if (result == null){
					continue;
				}
				
				String resultProtType = (String) result.get("protocolType");
				//如果是MONBUS协议，特殊处理，由于MODBUS返回协议时没有返回数据标识不知道如何去解析， 只能同步处理，通过请求的指令的数据标识来进行判断
				//如果是其他协议，默认以645协议进行处理，处理的数据已经是解析后的数据了
				if (ProtocolEum.MODBUS.name().equals(resultProtType)) {
					//获取请求的协议参数
					//content为：表号，功能码（03），数据标识（205）, 读取寄存器的位数（0001）
					String content = cmdReq.getContent();
					String[] contents = content.split(",");
					
					String reqAddr = contents[0];
					reqAddr = String.valueOf(Integer.parseInt(reqAddr));
					
					String reqCode = contents[1];
					
					String reqDataMarker = contents[2];
					
					String reqContent = contents[3];
					
					//获取返回的数据
					//获取返回的表地址
					String resultAddr = (String) result.get("addr");
					int resultDataLength = (int) result.get("dataLength");
					String resultCode = (String) result.get("cotrolCode");
					String dataValues = (String) result.get("dataValues");
					
					
					//如果控制码是“03”表示读指令
					if ("03".equals(reqCode)) {
						
						int reqDataLength = Integer.parseInt(contents[3]) * 2;
						
						logger.debug("modbus data match: reqAddr = {} VS resultAddr = {} . reqCode = {} VS resultCode = {}, reqDataLength = {} VS resultDataLength = {}", new Object[]{reqAddr, resultAddr, reqCode, resultCode, reqDataLength, resultDataLength});
						
						if (reqAddr.equals(resultAddr) && reqCode.equals(resultCode) && reqDataLength == resultDataLength) {
							
							String formatResultAddr = StringUtils.leftPad(resultAddr, 3, "0");
							result.put("addr", cmdReq.getGwId() + formatResultAddr);
							
							//如果请求的是止码
							if ("36".equals(reqDataMarker)) {
								
								if (StringUtils.isEmpty(dataValues)) {
									continue;
								}
								
								result.put("dataValues", Integer.parseInt(dataValues, 16) * 0.1);
								resp.setData(result);
								return resp;
							} else if ("304".equals(reqDataMarker) || "300".equals(reqDataMarker)) {
								
								if (StringUtils.isEmpty(dataValues)) {
									continue;
								}
								
								String r1 = dataValues.substring(0,4);
								String r2 = dataValues.substring(4, 8);
								
								result.put("dataValues", Integer.parseInt(r1) + "," + Integer.parseInt(r2));
								result.put("dataMarker", reqDataMarker);
								resp.setData(result);
							} 
							
							return resp;
							
						}
					} else if ("05".equals(reqCode)) {
						if (reqAddr.equals(resultAddr) && reqCode.equals(resultCode)) {
							
							//如果请求的是止码
							if (dataValues.equals(reqContent)) {
								return resp;
							}
						}
						
						return resp;
					}
					
				} else if (ParseEnum.GEHUA.name().equals(resultProtType)) {
					String reqMeterId = cmdReq.getMeterAddr();
					String resultAddr = mac == null ? "" : mac;
					
					String resultDataMarker = (String)result.get("dataMarker");
					
					//数据标识不为01,55的数据（55为阿迪克扩展） ，暂时都为主动上传的,
					if (!("01".equals(resultDataMarker) || "55".equals(resultDataMarker))) {
						logger.warn("gehua data : resultDataMarker = {}, It's Data auto uploaded");
						continue;
					}
					
					logger.debug("gehua data match: reqMeterId = {} VS resMeterId = {}", new Object[]{reqMeterId, resultAddr});
					if (reqMeterId.equals(resultAddr)){
						return resp;
					}
					
				} else {
					String content = cmdReq.getContent();
					String[] contents = content.split(",");
					//String controlCode = contents[1];
					String reqDataMarker = contents[2];
					String reqMeterId = cmdReq.getMeterAddr();
					
					
					String resultDataMarker = (String)result.get("dataMarker");
					
					String resultAddr = (String) result.get("addr");
					
					resultDataMarker = (resultDataMarker == null ? "" : resultDataMarker.toUpperCase());
					
					reqDataMarker = (reqDataMarker == null ? "" : reqDataMarker.toUpperCase());
					
					//判断是否有数据标识，如果有数据标识，判断仪表Id+数据标识是否有返回值， 如果没有，只判断仪表Id是否有返回值
					boolean isSuccess = (boolean) result.get("success");
					
					/**
					 * 特殊处理暂时放在这里
					 * 如果是写645设备地址，请求的是老地址，返回的是新地址，且没有数据项，匹配不上，通过mac来判断
					 * 14 04000401   写645设备地址
					 */
					if (reqDataMarker.equals("04000401")) {
						String reqMac = cmdReq.getGwId();
						Map<String, Object> extMap = resp.getExt();
						String resMac = (String) (extMap == null ? "" : extMap.get("mac"));
						logger.debug("645 data match: reqMac = {} VS resMac = {} . isSuccess = {}", new Object[]{reqMac, resMac, isSuccess});
						if (reqMac.equals(resMac)) {
							return resp;
						}
					}
					
					logger.debug("645 data match: reqMeterId = {} and reqDataMarker = {} VS resMeterId = {} and resDataMarker = {} . isSuccess = {}", new Object[]{reqMeterId, reqDataMarker, resultAddr, resultDataMarker, isSuccess});
					
					if (StringUtils.isEmpty(resultDataMarker) || !isSuccess){
						if (reqMeterId.equals(resultAddr)){
							return resp;
						}
					}
					
					//如果有相等的获取返回，如果没有找到匹配，重新放入队列中
					if (reqDataMarker.equals(resultDataMarker) && reqMeterId.equals(resultAddr)){
						return resp;
					}
				}
				
				
			}
			
			return null;
		} finally {
			/*if(channelId != null){
				ResponseMap.del(channelId);
			}*/
		}
	}
	
	/*private String[] hex2Array(String hexStr, int byteLength) {
		
		int size = byteLength / 2;
		
		String[] result = new String[size];
		
		for (int i = 0; i < size; i++) {
			result[i] = hexStr.substring(2 * i, 2 * i + 2);
		}
		
		return result;
	}*/
	
	/*private String hexStr2Int(String[] hexStrArray) {
		StringBuffer sb = new StringBuffer();
		for (String hexStr : hexStrArray) {
			int hex2Value = Integer.parseInt(hexStr, 16);
			sb.append(hex2Value *)
		}
	}*/
	
	protected Map<String, Object> parseResult(CmdResponse cmdRes){
		
		Object obj = cmdRes.getData();
		
		if (obj == null){
			return null;
		}
		
		String JsonStr = JsonUtils.objectToJson(obj);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> result = JsonUtils.jsonToPojo(JsonStr, Map.class);
		
		return result;
		
	}
	
	protected CmdResponse isFollowOperate(CmdRequest cmdReq, CmdResponse cmdResponse) throws InterruptedException{
		//如果是后续针数据项，继续读取后续数据
		Map<String, Object> result = parseResult(cmdResponse);
		String controlCode = (String) result.get("cotrolCode");
		String dataMarker = (String) result.get("dataMarker");
		boolean isLonger = (boolean) result.get("longer");
		
		if(StringUtils.isEmpty(controlCode)){
			return cmdResponse;
		}
		
		if (controlCode.equals(Dlt645ControlCode.READ_CONTINUE.getValue())){
			
			String dataValues = (String) result.get("dataValues");
			
			StringBuffer sb = new StringBuffer(dataValues);
			
			int times = 1;
			
			while (true) {
				
				String content = cmdReq.getContent();
				
				String[] contentArray = content.split(",");
				
				String newContent = contentArray[0] + ",12," + contentArray[2] + "," + times;
				
				cmdReq.setContent(newContent);
				
				CmdResponse followCmdRes = sendAndResp(cmdReq, channelBridge);
				
				if (followCmdRes == null) {
					continue;
				}
				
				Map<String, Object> followResult = parseResult(followCmdRes);
				
				String followControlCode = (String) followResult.get("cotrolCode");
				
				String follwDataValues = (String) followResult.get("dataValues");
				
				sb.append(";" + follwDataValues);
				
				if (Dlt645ControlCode.READ_FOLLOW_OVER.getValue().equals(followControlCode) || Dlt645ControlCode.READ_FOLLOW_ERR.getValue().equals(followControlCode)){
					break;
				} else if (Dlt645ControlCode.READ_FOLLOW_CONTINUE.getValue().equals(followControlCode)){
					times++;
					continue;
				}
			}
			
			//将事件后续帧具体的数据标识 配置在了longer中 所有为tue，曲线没有配置为false
			String fullDataValues = isLonger ? LongRead.decode(dataMarker, sb.toString().replace(";", "")) : sb.toString();
			
			result.put("dataValues", fullDataValues);
			
			result.put("cotrolCode", controlCode);
			
			cmdResponse.setData(result);
			
			return cmdResponse;
		}
		
		return cmdResponse;
	}
	
	protected int getSendNum(){
		
		int timeout = 0;
		try {
			timeout = Integer.valueOf(Env.getVal("engine.config.collectSendNum"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return timeout;
		
	}
	
	protected int getTimeOutConfig(String meterType){
		
		int timeout = 10;
		try {
			if (Cons.ELEC_METER_TYPE.equals(meterType) || "110000".equals(meterType)){
				timeout = Integer.valueOf(Env.getVal("engine.config.elecSendTimeout"));
			}else if (Cons.WATER_METER_TYPE.equals(meterType) || "120000".equals(meterType)){
				timeout = Integer.valueOf(Env.getVal("engine.config.waterSendTimeout"));
			}else if ("150000".equals(meterType)){
				timeout = Integer.valueOf(Env.getVal("engine.config.dtuSendTimeout"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return timeout;
		
	}
	
	/**
	 * senconds
	 * @return
	 */
	protected int getLoraIntervalTime(){
		int intervalTime = 3;
		try {
			intervalTime = Integer.valueOf(Env.getVal("engine.lorawan.cmd.interval.time"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return intervalTime;
	}
	
	private String ali2Lorawan(CmdRequest req){
		
		Map<String, Object> aliMsg = new HashMap<String, Object>();
		
		//转化成645协议帧
		Object[] contents = req.getContent().split(",");
		byte[] codeBuf = ParseFactory.build(ParseEnum.GEHUA).encode(contents);
		
		String hex = ByteUtils.bytes2hex(codeBuf).toUpperCase();
		
		//方便调测暂时放在这
		DebugManager.build().put("<< " + hex);
		aliMsg.put("data", hex);
		aliMsg.put("fport", 71);
		aliMsg.put("devEui", req.getGwId());
		aliMsg.put("confirmed", false);
		aliMsg.put("time", new Date().getTime());
		return JsonUtils.objectToJson(aliMsg);
	}
	
	
	private void lorawanSend(CmdRequest req){
		
		//转化成645协议帧
		Object[] contents = req.getContent().split(",");
		byte[] codeBuf = ParseFactory.build(ParseEnum.DLT645V2007).encode(contents);
		
		//组合参数
		StringBuffer requestParam = new StringBuffer();
		requestParam.append("?mac=").append(req.getGwId())
			.append("&token=").append(loRaWanReqParams.getToken())
			.append("&appeui=").append(loRaWanReqParams.getAppeui())
			.append("&payload=").append(ByteUtils.bytes2hex(codeBuf).toUpperCase())
			.append("&ttl=").append(loRaWanReqParams.getTtl())
			.append("&payloadType=").append(loRaWanReqParams.getPayloadType());
		
		if (isPrior()){
			requestParam.append("&confirmed=true&schedule=first");
		}
		
		//请求
		logger.debug("start request lorawarn server params = {}",  requestParam.toString());
		HttpGetReq getReq = new HttpGetReq(loRaWanReqParams.getServiceUrl() + requestParam.toString());
		String resultStr = null;
		try {
			resultStr = getReq.excuteReturnStr();
		} catch (Exception e) {
			logger.error("request lorawarn server ,e = {}",  e);
			throw new EngineException(EngineCode.CHANNEL_UNKNOW, "request lorawarn server ioexception!! connect error");
		}
		logger.debug("end request lorawarn server result = {}",  resultStr);
		
		//判断结果
		if(StringUtils.isEmpty(resultStr)){
			logger.error("request lorawarn server fail!!, result data is null");
			throw new EngineException(EngineCode.CHANNEL_UNKNOW, "request lorawarn server fail!!, result data is null");
		}
		
		JSONObject obj = JSONObject.parseObject(resultStr);
		
		if (obj == null){
			logger.error("request lorawarn server fail!!, parse result data is null");
			throw new EngineException(EngineCode.CHANNEL_UNKNOW, "request lorawarn server fail!!, parse result data is null");
		}
		
		//返回错误信息
		if(!obj.containsKey("code")){
			logger.error("request lorawarn server fail!!, There's no ‘code’ in result data, result obj = " + obj.toJSONString());
			String error = obj.getString("error");
			
			throw new EngineException(EngineCode.HTTP_LORAWARN_RETURN_NOCODE, "request lorawarn server fail!!, There's no ‘code’ in result data, error = " + error);
		}
		
		Integer code = obj.getInteger("code");
		
		//返回没成功
		if(code != 0){
			String errorMsg = obj.getString("msg");
			throw new EngineException(EngineCode.HTTP_LORAWARN_RETURN_ERROR, "request lorawarn server fail!!, result data ‘code’ is not 0 , errorMsg = " + errorMsg);
		}
		
	}

	protected void eventStatusHandle(CmdResponse cmdResponse, CmdRequest cmdReq){
		if (cmdReq.getContent().contains("04001501")) {
			CmdRequest cmdR = new CmdRequest();
			cmdR.setCmdLvl(10);
			
			Map<String, Object> result = parseResult(cmdResponse);
			String dataValues = (String) result.get("dataValues");
			if (StringUtils.isEmpty(dataValues) || dataValues.length() < 96) {
				logger.error("dataValues is null or dataValues length < 96 ");
				return;
			}
			
			String statusContent = dataValues.substring(0, 96);
			String content = cmdReq.getContent().split(",")[0] + ",14,04001503,00000002," + getOpposite(statusContent);
			cmdR.setContent(content);
			cmdR.setGwId(cmdReq.getGwId());
			cmdR.setMeterAddr(cmdReq.getMeterAddr());
			cmdR.setMeterType(cmdReq.getMeterType());
			cmdR.setProtocolType(cmdReq.getProtocolType());
			channelBridge.put(cmdR);
		}
	}
	
	private String getOpposite(String content){
		StringBuffer opposStr = new StringBuffer();
		for (int i = 0; i < content.length(); i++) {
			if (content.charAt(i) == '0'){
				opposStr.append("1");
			}else if (content.charAt(i) == '1') {
				opposStr.append("0");
			}else {
				throw new IllegalArgumentException();
			}
		}
		
		return opposStr.toString();
	}

}
