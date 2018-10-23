/*package cn.com.tw.paas.service.queue.customer;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.core.jms.cons.MqCons;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.http.HttpPostReq;
import cn.com.tw.common.utils.tools.http.entity.HttpCode;
import cn.com.tw.common.utils.tools.http.entity.HttpRes;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.utils.tools.security.MD5Utils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.service.queue.common.cache.CacheLocalService;
import cn.com.tw.paas.service.queue.common.utils.EntityUtils;
import cn.com.tw.paas.service.queue.eum.HisColEnum;
import cn.com.tw.paas.service.queue.eum.PushStatusEm;
import cn.com.tw.paas.service.queue.service.ReadDataService;
import cn.com.tw.paas.service.queue.service.TerminalService;

@Component
public class MqConsumer {

	private static Logger logger = LoggerFactory.getLogger(MqConsumer.class);

	@Autowired
	private ReadDataService readService;
	
	@Autowired
	private CacheLocalService cacheService;
	
	@Autowired
	private TerminalService terminalService;
	
	@Autowired
	private EquipNetStatusService equipNetStatusService;

	@Autowired
	private PushLogMapper pushLogMapper;

	@Autowired
	private MqHandler mqHandler;
	
	@Value("${http.proxy.hostname:}")
	private String hostname;
	
	@Value("${http.proxy.port:0}")
	private int port;

	// @Autowired
	// private OperationLogService operationLogService;

	*//**
	 * 接收采集数据上报队列的数据
	 * 
	 * @param msg
	 *//*
	@SuppressWarnings("unchecked")
	@JmsListener(destination = "${mq.queue.meter.data}")
	public void receiveMeterQueue(String msg) {
		logger.debug("-------------->> receive meter data start! msg:"+msg);
		// 接收到的msg转为map格式
		Map<String, Object> msgMap = JsonUtils.jsonToPojo(msg, Map.class);

		String statusCode = (String) msgMap.get("statusCode");
		
		String businessNo = (String) msgMap.get("businessNo");
		// 采集失败的msg
		if (StringUtils.isEmpty(statusCode) || !statusCode.equals("000000")) {
			logger.error("**************** receive meter data over! http fail");
			return;
		}
		// 获取仪表ID + 采集时间搓 组成的唯一标识
		String uniqueId = (String) msgMap.get("uniqueId");
		if (uniqueId == null || uniqueId.split(":").length < 2) {
			logger.error("**************** receive meter data over! uniqueId loss");
			return;
		}
		// msg中有价值的数据
		Map<String, Object> dataMap = (Map<String, Object>) msgMap.get("data");
		if (null != dataMap) {
			// succes表示指令执行结果正常
			Boolean success = (Boolean) dataMap.get("success");
			if (null == success || !success) {
				logger.debug("<<------------- receive meter data over! ins fail");
				return;
			}
			if (success) {
				dataMap.put("businessNo", businessNo);
				//msgCache.put(uniqueId, dataMap);
				excuteInsert(uniqueId, dataMap);
				logger.debug("<<------------ receive meter data ok!");
			}
		} else{
			logger.debug("<<-------------- receive meter data over! data loss");
			return;
		}
	}
	
	*//**
	 * 
	 * @param uniqueId
	 * @param dataMap
	 *//*
	private void excuteInsert(String uniqueId, Map<String, Object> dataMap) {
		
		String[] uniqueIds = uniqueId.split(":");
		// 仪表地址
		//String meterAddr = uniqueIds[0];
		// 时间戳
		String readTimeStr = uniqueIds[1];
		
		// 数据标识
		String dataMarker = (String) dataMap.get("dataMarker");
		//控制码
		String cotrolCode = (String) dataMap.get("cotrolCode");
		// 数据
		String dataValueStr = (String) dataMap.get("dataValues");
		//业务号
		String businessNo = (String) dataMap.get("businessNo");
		// 表号
		//仪表地址
		String meterAddr = (String) dataMap.get("addr");
		
		//请求获取 查询仪表信息
		Response<?> equipInfo = terminalService.selectByEquipNum(meterAddr);
		@SuppressWarnings("unchecked")
		Map<String, Object> terminalEquip = (Map<String, Object>) equipInfo.getData();
		if (terminalEquip == null) {
			return;
		}
		//TerminalEquip terminalEquip = terminalService.selectByEquipNumber(meterAddr);
		String elecMeterType = (String) terminalEquip.get("elecMeterTypeCode");
		String orgId = (String) terminalEquip.get("orgId");
		String appId = (String) terminalEquip.get("appId");
		String equipType = (String) terminalEquip.get("equipTypeCode");
		int elecPt = (int)terminalEquip.get("elecPt");
		int elecCt = (int)terminalEquip.get("elecCt");
		String pUrl = (String) terminalEquip.get("callbackUrl");
		String appKey = (String) terminalEquip.get("appKey");
		String appName = (String) terminalEquip.get("appName");
		String orgName = (String) terminalEquip.get("orgName");
		String netTypeCode = (String)terminalEquip.get("netTypeCode");
		
		//如果数据标识为空，数值为空，
		if (StringUtils.isEmpty(dataMarker) || StringUtils.isEmpty(dataValueStr)) {
			
			Map<String, Object> storeDataMap = null;
			
			String isOff = "";
			
			//推送
			Map<String, Object> pContent = new HashMap<String, Object> ();
			
			//正对于加密，如果是通电,返回的控制码为91，表示操作成功，更新为开闸状态
			if ("ABABABAA".equals(dataMarker)) {
				if ("83".equals(cotrolCode)) {
					isOff = "0";
				}
			}else if ("ABABABBB".equals(dataMarker)) {//如果关闸,返回91表示操作成功，更新为关闸状态
				if ("83".equals(cotrolCode)) {
					isOff = "1";
				}
			}
			
			//如果是业务NO为空 说明是第三方应用调用，推送信息
			if (!StringUtils.isEmpty(businessNo)) {
				
				if (dataMap.containsKey("success")) {
					pContent.put("success", dataMap.get("success"));
				}
				
				storeDataMap = EntityUtils.storeData(orgId, appId, appKey, orgName, appName, equipType, elecMeterType, meterAddr, netTypeCode, pUrl, null, null, readTimeStr, elecPt, elecCt, businessNo, JsonUtils.objectToJson(pContent));
				if (!StringUtils.isEmpty(isOff)) {
					storeDataMap.put("isOff", isOff);
				}
				readService.save(storeDataMap);
				
				push(meterAddr, appKey, readTimeStr, businessNo, netTypeCode, equipType, pUrl, (String)storeDataMap.get(HisColEnum.pTimes.name()), appId, pContent, (long) storeDataMap.get(HisColEnum.createTime.name()));
			
			}
			
			if (storeDataMap == null) {
				storeDataMap = new HashMap<String, Object>();
				storeDataMap.put(HisColEnum.termNo.name(), meterAddr);
				storeDataMap.put("isOff", isOff);
			}
			
			try {
				//更新最后一条记录
				readService.putLast(storeDataMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return;
		}
		
		//请求获取根据数据标识查询相关数据项
		List<Map<String, Object>> insList = cacheService.getInsCmdCache(dataMarker);
		if (null == insList) {
			logger.error("**************** receive meter data over! http fail");
			return;
		}
		
		Map<String, Object> storeDataMap = EntityUtils.storeData(orgId, appId, appKey, orgName, appName, equipType, elecMeterType, meterAddr, netTypeCode, pUrl, dataValueStr, insList, readTimeStr, elecPt, elecCt, businessNo, null);
		
		readService.save(storeDataMap);
		
		*//** ====================== 推送数据 =============================**//*
		String dSet = (String) storeDataMap.get(HisColEnum.dSet.name());
		//获取推送数据
		Map<String, Object> pushItemData = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : storeDataMap.entrySet()) {
			if (dSet.contains(entry.getKey())) {
				pushItemData.put(entry.getKey(), entry.getValue());
			}
		}
		
		push(meterAddr, appKey, readTimeStr, (String) storeDataMap.get(HisColEnum.busNo.name()), netTypeCode, equipType, pUrl, (String)storeDataMap.get(HisColEnum.pTimes.name()), appId, pushItemData, (long) storeDataMap.get(HisColEnum.createTime.name()));
		
		String busNo = (String) storeDataMap.get(HisColEnum.busNo.name());
		
		//封装推送数据
		Map<String, Object> pushData = new HashMap<String, Object>();
		pushData.put("equipNumber", meterAddr);
		pushData.put("appKey", appKey);
		pushData.put("lastSaveTime", readTimeStr);
		pushData.put("data", pushItemData);
		pushData.put("dataType", "221");
		pushData.put("commType", netTypeCode);
		if (!StringUtils.isEmpty(busNo)) {
			pushData.put("businessNo", businessNo);
		}
		
		Map<String, Object> pushInfo = new HashMap<String, Object> ();
		long currTime = (long) storeDataMap.get(HisColEnum.createTime.name());//读取时间
		long time = Long.MAX_VALUE - currTime;
		StringBuffer sb = new StringBuffer(MD5Utils.digest(appId)).append("|").append(time).append("|")
				.append(equipType).append("|").append(MD5Utils.digest(meterAddr));
		pushInfo.put("rowKey", sb.toString());
		pushInfo.put(HisColEnum.pUrl.name(), storeDataMap.get(HisColEnum.pUrl.name()));
		pushInfo.put("pushData", pushData);
		pushInfo.put(HisColEnum.pTimes.name(), storeDataMap.get(HisColEnum.pTimes.name()));
		
		mqHandler.send(MqCons.QUEUE_PUSH_RETRY, JsonUtils.objectToJson(pushInfo));
		
		try {
			//更新最后一条记录
			readService.putLast(storeDataMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void push(String meterAddr, String appKey, String readTimeStr, String businessNo, String netTypeCode, String equipType, String pUrl, String pTimes, String appId, Map<String, Object> pushItemData, long currTime){
		
		//封装推送数据
		Map<String, Object> pushData = new HashMap<String, Object>();
		pushData.put("equipNumber", meterAddr);
		pushData.put("appKey", appKey);
		pushData.put("lastSaveTime", readTimeStr);
		pushData.put("data", pushItemData);
		pushData.put("equipType", equipType);
		pushData.put("dataType", "221");
		pushData.put("commType", netTypeCode);
		if (!StringUtils.isEmpty(businessNo)) {
			pushData.put("businessNo", businessNo);
		}
		
		Map<String, Object> pushInfo = new HashMap<String, Object> ();
		
		
		long time = Long.MAX_VALUE - Long.parseLong(readTimeStr);
		StringBuffer sb = new StringBuffer(MD5Utils.digest(appId)).append("|").append(time).append("|")
				.append(equipType).append("|").append(MD5Utils.digest(meterAddr));
		pushInfo.put("rowKey", sb.toString());
		
		
		pushInfo.put(HisColEnum.pUrl.name(), pUrl);
		pushInfo.put("pushData", pushData);
		pushInfo.put(HisColEnum.pTimes.name(), pTimes);
		
		mqHandler.send(MqCons.QUEUE_PUSH_RETRY, JsonUtils.objectToJson(pushInfo));
	}

	*//**
	 * 接收QUEUE_PUSH_RETRY队列的数据
	 * 
	 * @param msg
	 *//*
	@SuppressWarnings("unchecked")
	@JmsListener(destination = MqCons.QUEUE_PUSH_RETRY)
	public void pushLogQueue(String msg) {
		// 接收到的msg转为map格式
		Map<String, Object> msgMap = JsonUtils.jsonToPojo(msg, Map.class);

		String reqDatas = JsonUtils.objectToJson(msgMap.get("pushData"));
		
		String pTimesStr = (String) msgMap.get(HisColEnum.pTimes.name());
		
		int pTimes = pTimesStr == null ? 0 : Integer.parseInt(pTimesStr) + 1;
		
		String pUrl = (String) msgMap.get(HisColEnum.pUrl.name());
		
		StringEntity entity = new StringEntity(reqDatas, "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");

		Map<String, Object> putInfo = new HashMap<String, Object>();
		putInfo.put("rowKey", msgMap.get("rowKey"));
		putInfo.put(HisColEnum.pTimes.name(), String.valueOf(pTimes));
		putInfo.put(HisColEnum.pTime.name(), new Date().getTime());
		try {
			
			*//**
			 * 如果推送次数大于等于5次 则不做请求
			 *//*
			if (pTimes > 2) {
				logger.warn("-------------->> push time gt {} ! msg:{}", pTimes, "push times exceeds limit");
				return;
			}
			
			logger.debug("------------>> pushing data!, pushMap = {}", msgMap.toString());

			//请求时间
			HttpRes result = new HttpPostReq(pUrl, null, entity).setProxy(hostname, port).excuteReturnObj();

			*//**
			 * 判断Http请求是否请求成功 失败则往MQ里发送数据
			 *//*
			if (!HttpCode.RES_SUCCESS.equals(result.getCode())) {
				
				putInfo.put(HisColEnum.pStatus.name(), PushStatusEm.PUSH_HTTP_FAIL.getValue());
				readService.updatePushInfo(putInfo);
				
				msgMap.put(HisColEnum.pTimes.name(), String.valueOf(pTimes));
				// 往Mq里发送数据
				mqHandler.sendDelay(MqCons.QUEUE_PUSH_RETRY, JsonUtils.objectToJson(msgMap), 5);
				return;
			}

			String content = result.getData();

			Response<?> resp = JsonUtils.jsonToPojo(content, Response.class);

			*//**
			 * 判断返回数据状态
			 *//*
			if (resp == null || !ResultCode.OPERATION_IS_SUCCESS.equals(resp.getStatusCode())) {
				putInfo.put(HisColEnum.pStatus.name(), PushStatusEm.PUSH_FAIL.getValue());
				readService.updatePushInfo(putInfo);
				
				msgMap.put(HisColEnum.pTimes.name(), String.valueOf(pTimes));

				//往Mq里发送数据
				mqHandler.sendDelay(MqCons.QUEUE_PUSH_RETRY, JsonUtils.objectToJson(msgMap), 5);
				return;
			}

			//成功
			putInfo.put(HisColEnum.pStatus.name(), PushStatusEm.PUSH_SUCCESS.getValue());
			readService.updatePushInfo(putInfo);

			logger.debug("<<------------push success!");
		} catch (IOException e) {

			logger.error("<<------------push fail!, IOException e = {}",e);
			putInfo.put(HisColEnum.pStatus.name(), PushStatusEm.PUSH_HTTP_FAIL.getValue());
			
			readService.updatePushInfo(putInfo);
			msgMap.put(HisColEnum.pTimes.name(), String.valueOf(pTimes));
			// 往Mq里发送数据
			mqHandler.sendDelay(MqCons.QUEUE_PUSH_RETRY, JsonUtils.objectToJson(msgMap), 5);

			e.printStackTrace();
			return;
		} catch (Exception e) {
			logger.error("<<------------push fail!, Exception e = {}",e);
			
			putInfo.put(HisColEnum.pStatus.name(), PushStatusEm.PUSH_FAIL.getValue());
			readService.updatePushInfo(putInfo);
			msgMap.put(HisColEnum.pTimes.name(), String.valueOf(pTimes));
			// 往Mq里发送数据
			mqHandler.sendDelay(MqCons.QUEUE_PUSH_RETRY, JsonUtils.objectToJson(msgMap), 5);
			return;
		}

	}

	@JmsListener(destination = MqCons.QUEUE_LOG_OPERA)
	public void operationLog(String msg){
	 EntityUtils.apiLogData(orgId, appId, orgName, appName, apiUrl, authToken, reqIp, appBusNo, reqData, reqTime, busType, busNo, status, respData, createTime)
	}
	
	*//**
	 * 监听设备网络状态
	 * 
	 * @param msg
	 *//*
	@SuppressWarnings("unchecked")
	@JmsListener(destination = "${mq.queue.net.status}")
	public void receiveNetQueue(String msg) {
		logger.debug("--------->> receive net status start! msg:"+msg);
		// 接收到的msg转为map格式
		Map<String, Object> msgMap = JsonUtils.jsonToPojo(msg, Map.class);
		String diveceId = (String) msgMap.get("diveceId");
		String status = (String) msgMap.get("status");
		if(!StringUtils.isEmpty(diveceId)&&!StringUtils.isEmpty(status)
				&&(status.equals("1")||status.equals("0"))){
			logger.debug("<<------- receive net status ok!");
			//equipNetStatusService.updateStatus(diveceId, status);
		}
		else {
			logger.debug("<<<<<<<<<<<<<<<< receive net status over! data loss");
		}
	}

}*/