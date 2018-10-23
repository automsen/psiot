package cn.com.tw.engine.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.engine.core.config.EngineConfig;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.WarnInfo;
import cn.com.tw.engine.core.entity.eum.EnumType;

@Component
public class MsgAysncService {
	
	@Autowired
	private MqHandler myHandler;
	
	@Autowired
	private EngineConfig config;
	
	@SuppressWarnings({ "unchecked" })
	private Map<String, Integer> msgRecordMap = new LRUMap();
	
	private Map<String, String> queueCategMap = new HashMap<String, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(EnumType.param.name(), "04");
			put(EnumType.event.name(), "10,11,12,13,14,15,16,17,18,19,1A,1B,1C,1D,1E,1F,20,21,22,23,24,25,26,27,28,29");
			put(EnumType.elec.name(), "00,05");
			put(EnumType.harm_var.name(), "02,04");
			put(EnumType.demand.name(), "01");
			put(EnumType.curve.name(), "06");
		}
	};
	
	//====特殊处理 === 
	
	//状态字标识，放入谐波及变量
	private String statuMarker = "04000501,04000502,04000503,04000504,04000505,04000506,04000507,040005FF";
	
	//事件状态字，放入事件队列
	private String eventStatus = "04001501";
	
	public void sendWarnInfo(WarnInfo info){
		
		if (info == null || StringUtils.isEmpty(info.getDeviceId())){
			return;
		}
		
		String deviceId = info.getDeviceId();
		
		int status = info.getStatus();
		
		Integer deviceStatus = msgRecordMap.get(deviceId);
		
		if (deviceStatus == null || deviceStatus != status){
			msgRecordMap.put(deviceId, status);
			try {
				myHandler.send(config.getQueues().get(EnumType.alarm.name()), JsonUtils.objectToJson(info));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void sendCollectRes(CmdResponse resp){
		try {
			if (resp == null){
				return;
			}
			
			Map<String, Object> data = parseResult(resp.getData());
			
			
			if (data == null){
				return;
			}
			
			String resultDataMarker = (String)data.get("dataMarker");
			
			String cotrolCode = (String)data.get("cotrolCode");
			
			String dataField = (String)data.get("dataField");
			
			dataField = dataField == null ? "" : dataField;
			
			
			/**暂时加在这里，主要是处理645+modbus协议返回数据，现在只暂时处理控制码为8D **/
			if ("8D".equals(cotrolCode)){
				myHandler.send(config.getQueues().get(EnumType.event.name()), JsonUtils.objectToJson(resp));
				return;
			}
			
			//如果是心跳类
			if ("0A000090".equals(dataField.toUpperCase())){
				
				myHandler.send(config.getQueues().get(EnumType.heart.name()), JsonUtils.objectToJson(resp));
				
				return;
			}
			
			
			if (StringUtils.isEmpty(resultDataMarker)){
				return;
			}
			
			if (statuMarker.contains(resultDataMarker)){
				
				myHandler.send(config.getQueues().get(EnumType.harm_var.name()), JsonUtils.objectToJson(resp));
				
				return;
			}
			
			if (eventStatus.contains(resultDataMarker)){
				
				myHandler.send(config.getQueues().get(EnumType.event.name()), JsonUtils.objectToJson(resp));
				
				return;
			}
			
			String headTwo = resultDataMarker.substring(0,2);
		
			boolean isExist = false;
			for (Map.Entry<String, String> entry : queueCategMap.entrySet()){
				
				String cateStr = entry.getValue();
				
				if (cateStr.contains(headTwo)){
					isExist = true;
					myHandler.send(config.getQueues().get(entry.getKey()), JsonUtils.objectToJson(resp));
					break;
				}
				
			}
			
			if (!isExist) {
				/**暂时加在这里，主要是处理modbus协议返回数据，现在只暂时处理304数据标识（返回仪表通信状态） **/
				//if ("304".equals(resultDataMarker) || "300".equals(resultDataMarker)) {
					myHandler.send(config.getQueues().get(EnumType.event.name()), JsonUtils.objectToJson(resp));
					return;
				//}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Map<String, Object> parseResult(Object obj){
		
		if (obj == null){
			return null;
		}
		
		String JsonStr = JsonUtils.objectToJson(obj);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> result = JsonUtils.jsonToPojo(JsonStr, Map.class);
		
		return result;
		
	}

}
