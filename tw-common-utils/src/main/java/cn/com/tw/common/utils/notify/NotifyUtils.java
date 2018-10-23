package cn.com.tw.common.utils.notify;

import java.util.HashMap;
import java.util.Map;

import cn.com.tw.common.enm.notify.NotifyBusTypeEm;
import cn.com.tw.common.enm.notify.NotifyTypeEm;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.tools.json.JsonUtils;

import com.alibaba.fastjson.JSONObject;

public class NotifyUtils {
	
	public static String sendTermEventMsg(String equipNumner, String equipType, NotifyBusTypeEm eventType, Object data, long happenTime){
		return sendTermEventMsg(equipNumner, equipType, null, eventType, data, happenTime);
	}
	
	@SuppressWarnings("unchecked")
	public static String sendTermEventMsg(String equipNumner, String equipType, String businessNo, NotifyBusTypeEm eventType, Object data, long happenTime){
		
		// 封装推送数据
		Map<String, Object> pushData = new HashMap<String, Object>();
		pushData.put("equipNumber", equipNumner);
		pushData.put("lastSaveTime", happenTime);
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		switch (eventType) {
		case termCmdError:
			if (data != null) {
				dataMap.put("code", data);
			}
			break;
		default:
			dataMap = (Map<String, Object>) data;
			break;
		}
		
		pushData.put("data", dataMap);
		pushData.put("equipType", equipType);
		pushData.put("businessNo", businessNo);
		//221 表示仪表采集数据, 1604.1605.1606 表示异常警告数据
		pushData.put("dataType", eventType.getValue());
		pushData.put("ext", new HashMap<String, Object>());
		//pushData.put("commType", netTypeCode);
		
		return JsonUtils.objectToJson(pushData);
	}
	
	/**
	 * 发送模板短信(通用)
	 * @param phone 电话号码
	 * @param templNo  模板
	 * @param sendMessage  发送信息内容
	 * @return
	 */
	public static String sendSMSTemplate(String phone, String templNo, String sendMessage, int notifyLvl, String notifyBusiNo, String orgId){
		JSONObject json = new JSONObject();
		json.put("phoneNum", phone);
		json.put("templNum", templNo);
		json.put("message", sendMessage);
		String message = JSONObject.toJSONString(json);
		message = CommUtils.base64Encode(message);
		return buildNotifyRecord(NotifyTypeEm.SMS.getValue(), phone, notifyBusiNo, "send/sms?message=" + message, notifyLvl, orgId);
	}
	
	private static String buildNotifyRecord(int notifyType, String notifyNo, String notifyBusinessNo, String url, int notifyLevel, String orgId){
		JSONObject notifyRecord = new JSONObject();
		notifyRecord.put("notifyType", NotifyTypeEm.SMS.getValue());
		notifyRecord.put("notifyNo", notifyNo);
		notifyRecord.put("notifyBusinessNo", notifyBusinessNo);
		notifyRecord.put("url", url);
		notifyRecord.put("notifyLevel", notifyLevel);
		notifyRecord.put("orgId", orgId);
		return JSONObject.toJSONString(notifyRecord);
	}

}
