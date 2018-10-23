package cn.com.tw.paas.service.queue.customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.core.jms.kafka.KafkaGroupConsumerHandler;
import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.utils.tools.security.MD5Utils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.service.queue.common.cache.CacheLocalService;
import cn.com.tw.paas.service.queue.common.utils.EntityUtils;
import cn.com.tw.paas.service.queue.eum.HisColEnum;
import cn.com.tw.paas.service.queue.service.ReadDataService;
import cn.com.tw.paas.service.queue.service.TerminalService;

@Component
public class CollectDataKafkaGroup1Consumer implements KafkaGroupConsumerHandler {

	private static Logger logger = LoggerFactory.getLogger(CollectDataKafkaGroup1Consumer.class);
	
	@Autowired
	private ReadDataService readService;
	
	@Autowired
	private CacheLocalService cacheService;
	
	@Autowired
	private TerminalService terminalService;
	
	@Autowired
	private MqHandler mqHandler;
	
	@Value("${mq.queue.push.data}")
	private String pushQueueName;
	
	@SuppressWarnings("unchecked")
	@Override
	public void listen(ConsumerRecord<?, ?> cr) {
		try {
			logger.info("-------------->> queue-receive-data - {} - {} : {}", cr.topic(), cr.key(), cr.value());

			String msg = (String) cr.value();

			// 接收到的msg转为map格式
			Map<String, Object> msgMap = JsonUtils.jsonToPojo(msg, Map.class);
			
			if (msgMap == null) {
				logger.error("receive data is null");
				return;
			}

			String statusCode = (String) msgMap.get("statusCode");

			String businessNo = (String) msgMap.get("businessNo");
			// 采集失败的msg
			if (StringUtils.isEmpty(statusCode) || !statusCode.equals("000000")) {
				logger.error("receive meter data error! statusCode = {}", statusCode);
				return;
			}
			// 获取仪表ID + 采集时间搓 组成的唯一标识
			String uniqueId = (String) msgMap.get("uniqueId");
			if (uniqueId == null || uniqueId.split(":").length < 2) {
				logger.error("receive meter data error! uniqueId loss");
				return;
			}
			// msg中有价值的数据
			Map<String, Object> dataMap = (Map<String, Object>) msgMap.get("data");
			
			if (null != dataMap) {
				// succes表示指令执行结果正常
				Boolean success = (Boolean) dataMap.get("success");
				if (null == success || !success) {
					logger.error("receive meter data error! ins return 'success = false'");
					return;
				}
				if (success) {
					dataMap.put("businessNo", businessNo);
					
					if (msgMap.containsKey("ext")) {
						
						Object extObj = msgMap.get("ext");
						
						if (extObj != null) {
							Map<String, Object> extMap = (Map<String, Object>) extObj;
							dataMap.put("mac", extMap != null && extMap.containsKey("mac") ? extMap.get("mac") : null);
						}
					}
					// msgCache.put(uniqueId, dataMap);
					excuteInsert(uniqueId, dataMap);
				}
			} else {
				logger.error("receive meter data error! data is null");
				return;
			}
		} catch (Exception e) {
			logger.error("queue-receive-data error , exception e = {}",e);
		}
	}

	private void excuteInsert(String uniqueId, Map<String, Object> dataMap) {

		String[] uniqueIds = uniqueId.split(":");
		// 仪表地址
		// String meterAddr = uniqueIds[0];
		// 时间戳
		String readTimeStr = uniqueIds[1];

		//判断是不是歌华协议
		boolean is645ModBus = isGeHua(readTimeStr, dataMap);
		if (is645ModBus) {
			return;
		}
		
		//判断是不是645协议
		boolean is645 = is645(readTimeStr, dataMap);
		
		if (is645) {
			return ;
		}
		
		logger.warn("protocol not support!!!");
		
	}
	
	/**
	 * 判断是不是645+modbus协议
	 * @param cotrolCode
	 * @param dataMap
	 */
	@SuppressWarnings("unchecked")
	private boolean isGeHua(String readTimeStr, Map<String, Object> dataMap){
		
		try {
			
			// 协议类型
			String protocolType = (String) dataMap.get("protocolType");
			
			if (!"GEHUA".equals(protocolType)) {
				return false;
			}
			
			// 数据标识
			String dataMarker = (String) dataMap.get("dataMarker");
			
			//暂时加在这,如果是告警 暂时不处理
			/*if ("04".equals(dataMarker)) {
				return true;
			}*/
			
			// 数据
			String dataVal = (String) dataMap.get("dataValues");
			
			if (StringUtils.isEmpty(dataVal)) {
				logger.error("dataVal = null");
				return true;
			}
			
			Map<String, Object> dataValMap = JsonUtils.jsonToPojo(dataVal, Map.class);
			
			if (dataValMap == null) {
				logger.error("dataValMap = null");
				return true;
			}
			
			// 业务号
			String businessNo = (String) dataMap.get("businessNo");
			
			// 表号 仪表地址,如果是歌华协议，热力泵DTU,终端没有编号，用MAC代替
			String meterAddr = (String) dataMap.get("mac");
		
			
			// 请求获取 查询仪表信息
			Response<?> equipInfo = terminalService.selectByEquipNum(meterAddr);
			Map<String, Object> terminalEquip = (Map<String, Object>) equipInfo.getData();
			if (terminalEquip == null) {
				logger.error("meterAddr is no exists!!, meterAddr = " + meterAddr);
				return true;
			}
			
			String elecMeterType = (String) terminalEquip.get("elecMeterTypeCode");
			String orgId = (String) terminalEquip.get("orgId");
			String appId = (String) terminalEquip.get("appId");
			String equipType = (String) terminalEquip.get("equipTypeCode");
			String pUrl = (String) terminalEquip.get("callbackUrl");
			String appKey = (String) terminalEquip.get("appKey");
			String appName = (String) terminalEquip.get("appName");
			String orgName = (String) terminalEquip.get("orgName");
			String netTypeCode = (String) terminalEquip.get("netTypeCode");
			
			
			Map<String, Object> storeDataMap = EntityUtils.geHuaStoreData(orgId, appId, appKey, orgName, appName, equipType, elecMeterType, meterAddr, netTypeCode, pUrl, readTimeStr, businessNo, dataMarker, protocolType, dataValMap);
			
			readService.save(storeDataMap);

			/** ====================== 推送数据 ============================= **/
			Map<String, Object> pushItemData = new HashMap<String, Object>();
			for (Map.Entry<String, Object> entry : storeDataMap.entrySet()) {
				
				try {
					HisColEnum.valueOf(entry.getKey());
				} catch (Exception e) {
					pushItemData.put(cacheCode(entry.getKey()), entry.getValue());
				}
			}
			
			//推送回路仪表信息
			push(meterAddr, appKey, (long)storeDataMap.get(HisColEnum.cTm.name()), (String) storeDataMap.get(HisColEnum.bNo.name()), netTypeCode, equipType, pUrl, (String) storeDataMap.get(HisColEnum.pTs.name()), appId,
					pushItemData, (String) storeDataMap.get(HisColEnum.lTy.name()));

			try {
				//更新最后一条记录
				readService.putLast(storeDataMap);
			} catch (Exception e) {
				logger.error("put last data, exception e = {}", e);
			}
			
			logger.debug("<<------------ receive meter data ok!");	
					
			return true;
			
		} catch (Exception e) {
			logger.error("Exception e = {}", e);	
		}
		
		return true;
	}
	
	/**
	 * 判断是不是645+modbus协议
	 * @param cotrolCode
	 * @param dataMap
	 */
	private boolean is645(String readTimeStr, Map<String, Object> dataMap){
		
		// 数据标识
		String dataMarker = (String) dataMap.get("dataMarker");
		// 控制码
		String cotrolCode = (String) dataMap.get("cotrolCode");
		// 数据
		String dataValueStr = (String) dataMap.get("dataValues");
		// 业务号
		String businessNo = (String) dataMap.get("businessNo");
		// 表号 仪表地址
		String meterAddr = (String) dataMap.get("addr");
		// 协议类型
		String protocolType = (String) dataMap.get("protocolType");

		// 请求获取 查询仪表信息
		Response<?> equipInfo = terminalService.selectByEquipNum(meterAddr);
		@SuppressWarnings("unchecked")
		Map<String, Object> terminalEquip = (Map<String, Object>) equipInfo.getData();
		if (terminalEquip == null) {
			logger.error("meterAddr is no exists!! {}", meterAddr);
			return true;
		}
		// TerminalEquip terminalEquip =
		// terminalService.selectByEquipNumber(meterAddr);
		String elecMeterType = (String) terminalEquip.get("elecMeterTypeCode");
		String orgId = (String) terminalEquip.get("orgId");
		String appId = (String) terminalEquip.get("appId");
		String equipType = (String) terminalEquip.get("equipTypeCode");
		int elecPt = (int) terminalEquip.get("elecPt");
		int elecCt = (int) terminalEquip.get("elecCt");
		String pUrl = (String) terminalEquip.get("callbackUrl");
		String appKey = (String) terminalEquip.get("appKey");
		String appName = (String) terminalEquip.get("appName");
		String orgName = (String) terminalEquip.get("orgName");
		String netTypeCode = (String) terminalEquip.get("netTypeCode");
		String elecVoltageRating = "" + (Double) terminalEquip.get("elecVoltageRating");
		

		/** 如果没有数据标识的处理， 主要是通断电 **/
		boolean isNext = checkNoMarkerData(meterAddr, dataMarker, dataValueStr, cotrolCode, readTimeStr, businessNo, protocolType, dataMap, terminalEquip);
		
		if (isNext) {
			return true;
		}
		
		/**
		 * 正常业务逻辑,-----------------------start
		 */
		// 请求获取根据数据标识查询相关数据项
		List<Map<String, Object>> insList = cacheService.getInsCmdCache(dataMarker);
		
		//如果是modbus，条件不满足，继续走下面
		if (null == insList && !"MODBUS".equals(protocolType)) {
			logger.error("receive meter data error! insList is null");
			return true;
		}

		Map<String, Object> storeDataMap = EntityUtils.storeData(orgId, appId,
				appKey, orgName, appName, equipType, elecMeterType, meterAddr,
				netTypeCode, pUrl, dataValueStr, insList, readTimeStr, elecPt,
				elecCt, businessNo, dataMarker, protocolType);

		readService.save(storeDataMap);

		/** ====================== 推送数据 ============================= **/
		Map<String, Object> pushItemData = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : storeDataMap.entrySet()) {
			
			try {
				HisColEnum.valueOf(entry.getKey());
			} catch (Exception e) {
				pushItemData.put(cacheCode(entry.getKey()), entry.getValue());
			}
			
		}
		
		//推送回路仪表信息
		push(meterAddr, appKey, (long)storeDataMap.get(HisColEnum.cTm.name()), (String) storeDataMap.get(HisColEnum.bNo.name()), netTypeCode, equipType, pUrl, (String) storeDataMap.get(HisColEnum.pTs.name()), appId,
				pushItemData, (String) storeDataMap.get(HisColEnum.lTy.name()));

		try {
			storeDataMap.put("elecVoltageRating", elecVoltageRating);
			//更新最后一条记录
			readService.putLast(storeDataMap);
		} catch (Exception e) {
			logger.error("put last data, exception e = {}", e);
		}
		
		logger.debug("<<------------ receive meter data ok!");
		
		return true;
	}
	
	private String cacheCode(String shrotCode) {
		Map<String, Map<String, String>> insMap = cacheService.itemAll();
		if (null == insMap) {
			throw new BusinessException(ResultCode.UNKNOW_ERROR, "获取缓存数据失败");
		}
		
		Map<String, String> childMap = insMap.get(shrotCode);
		//如果没有直接推送shrotCode
		if (childMap == null) {
			return shrotCode;
		}
		
		return childMap.get("itemCode");
	}

	private void push(String meterAddr, String appKey, long createTime,
			String businessNo, String netTypeCode, String equipType,
			String pUrl, String pTimes, String appId,
			Map<String, Object> pushItemData, String loopType) {

		Map<String, Object> pushData = EntityUtils.getPushData(meterAddr, appKey, String.valueOf(createTime), pushItemData, equipType, netTypeCode, businessNo, loopType);

		Map<String, Object> pushInfo = new HashMap<String, Object>();

		long time = Long.MAX_VALUE - createTime;
		StringBuffer sb = new StringBuffer(MD5Utils.digest(appId)).append("|")
				.append(time).append("|").append(equipType).append("|")
				.append(MD5Utils.digest(meterAddr));
		pushInfo.put("rowKey", sb.toString());

		pushInfo.put(HisColEnum.pUl.name(), pUrl);
		pushInfo.put("pushData", pushData);
		pushInfo.put(HisColEnum.pTs.name(), pTimes);

		mqHandler.send(pushQueueName, JsonUtils.objectToJson(pushInfo));
	}
	
	private boolean checkNoMarkerData(String meterAddr, String dataMarker, String dataValueStr, String cotrolCode, String readTimeStr, String businessNo, String protocolType, Map<String, Object> dataMap, Map<String, Object> terminalEquip){
		
		String elecMeterType = (String) terminalEquip.get("elecMeterTypeCode");
		String orgId = (String) terminalEquip.get("orgId");
		String appId = (String) terminalEquip.get("appId");
		String equipType = (String) terminalEquip.get("equipTypeCode");
		int elecPt = (int) terminalEquip.get("elecPt");
		int elecCt = (int) terminalEquip.get("elecCt");
		String pUrl = (String) terminalEquip.get("callbackUrl");
		String appKey = (String) terminalEquip.get("appKey");
		String appName = (String) terminalEquip.get("appName");
		String orgName = (String) terminalEquip.get("orgName");
		String netTypeCode = (String) terminalEquip.get("netTypeCode");
		
		// 如果数据标识为空，数值为空，特殊处理
		if (StringUtils.isEmpty(dataMarker) || StringUtils.isEmpty(dataValueStr)) {
			
			dataMarker = StringUtils.isEmpty(dataMarker) ? "" : dataMarker;
			
			logger.warn("dataMarker is {}, dataValueStr is {}", new Object[]{dataMarker, dataValueStr});
			
			String isOff = "";

			//仪表返回的指令结果解析不成功,不做入库操作
			if (!dataMap.containsKey("success")) {
				logger.error("meter return's ins result is not success, no insert db!");
				return true;
			}

			// 正对于加密，如果是通电,返回的控制码为91，表示操作成功，更新为开闸状态
			if ("ABABABAA".equals(dataMarker.toUpperCase())) {
				if ("83".equals(cotrolCode)) {
					isOff = "0";
				}
			} else if ("ABABABBB".equals(dataMarker.toUpperCase())) {// 如果关闸,返回83表示操作成功，更新为关闸状态
				if ("83".equals(cotrolCode)) {
					isOff = "1";
				}
			}
			
			if (StringUtils.isEmpty(isOff)) {
				logger.warn("dataMarker is not ABABABAA or ABABABBB, can not insert db!");
				return true;
			}
			
			Map<String, Object> storeDataMap = EntityUtils.storeData(orgId, appId, appKey, orgName, appName, equipType, elecMeterType, meterAddr, netTypeCode, pUrl, null, null, readTimeStr, elecPt, elecCt, businessNo, dataMarker, protocolType);
			//存储简称
			storeDataMap.put("IF", isOff);
			readService.save(storeDataMap);

			// 推送通断结果指令
			Map<String, Object> pContent = new HashMap<String, Object>();
			//推送全称
			pContent.put("isOff", isOff);
			push(meterAddr, appKey, (long)storeDataMap.get(HisColEnum.cTm.name()), businessNo, netTypeCode, equipType, pUrl, (String) storeDataMap.get(HisColEnum.pTs.name()), appId, pContent, (String) storeDataMap.get(HisColEnum.lTy.name()));

			try {
				//更新最后一条记录
				readService.putLast(storeDataMap);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;
		}
		
		
		/**
		 * 暂时加在这里 ======特殊处理，水表开关闸下发指令返回，数据标识“0460AA02”，如果返回的协议解析成功，则认为是开关阀 成功，
		 *目前 dataValues=000000.60,0,0,0,0,0,0,00,0,0,0,0,0,0,0,1。最后一位表示通断状态，0通、1断后面有可能协议调整取通断状态的位置会变
		 * */
		if ("0460AA02".equals(dataMarker.toUpperCase())) {
			
			Map<String, Object> storeDataMap = EntityUtils.storeData(orgId, appId, appKey, orgName, appName, equipType, elecMeterType, meterAddr, netTypeCode, pUrl, null, null, readTimeStr, elecPt, elecCt, businessNo, dataMarker, protocolType);
			
			if (!StringUtils.isEmpty(dataValueStr)) {
				String[] dataValueArray = dataValueStr.split(",");
				if (dataValueArray.length == 16) {
					storeDataMap.put("IF", dataValueArray[15]);
					readService.save(storeDataMap);

					// 推送通断结果指令
					Map<String, Object> pContent = new HashMap<String, Object>();
					pContent.put("isOff", dataValueArray[15]);
					push(meterAddr, appKey, (long)storeDataMap.get(HisColEnum.cTm.name()), businessNo, netTypeCode, equipType, pUrl, (String) storeDataMap.get(HisColEnum.pTs.name()), appId, pContent, (String) storeDataMap.get(HisColEnum.lTy.name()));

					try {
						//更新最后一条记录
						readService.putLast(storeDataMap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			return true;
			
		}
		
		return false;
	}
	
}