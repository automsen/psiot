package cn.com.tw.paas.service.queue.common.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.service.queue.service.CacheService;

@Component
public class CacheLocalService {
	
	//private static Logger logger = LoggerFactory.getLogger(CacheLocalService.class);
	
	private Map<String, Object> insCmdMap = new HashMap<String, Object>();
	
	private static  Map<String, String> gehuaMap = new HashMap<String, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = -6553857369312250929L;

		{
			put("eId", "下联设备ID");
			put("IDT", "室内温度");
			put("IF", "开关状态");
			put("IWT", "入水温度");
			put("ODT", "室外温度");
			put("OWT", "出水温度");
			put("RMD", "运行模式");
			put("STT", "设定温度");
			put("rTm", "读取时间");
			put("TAE", "组合有功总电能");
			put("WF", "水流量");
			
			put("WMD", "DTU工作模式");
			put("DFB", "DTU功能位");
			put("Baud", "DTU射频波特率");
			put("HBI", "DTU心跳时间间隔");
			put("ENUM", "设备数量");
			put("ENT", "环境温度");
			put("BTY", "电池电量");
			put("DWAC", "DTU告警代码");
			put("WAT", "告警产生时间");
			put("WAF", "故障标志");
			put("WAC", "DTU子设备故障代码");
			put("uTm", "采集时间");
			
			put("DID", "DTU编号");
			put("DHV", "DTU硬件版本号");
			put("DSV", "DTU软件版本号");
			put("WMD", "DTU工作模式");
			put("DFB", "DTU功能位");
			put("HBI", "DTU心跳时间间隔");
			put("EN", "设备数量");
			put("RI", "上报间隔");
			put("RGBR", "保留字节");
			
			put("FID1", "厂家ID1");
			put("ET1", "设备类型1");
			put("EID1", "设备ID1");
			put("SV1", "软件版本1");
			put("PV1", "协议版本1");
			put("RGBR1", "保留字节1");
			
			put("FID2", "厂家ID2");
			put("ET2", "设备类型2");
			put("EID2", "设备ID2");
			put("SV2", "软件版本2");
			put("PV2", "协议版本2");
			put("RGBR2", "保留字节2");
			
			put("FID3", "厂家ID3");
			put("ET3", "设备类型3");
			put("EID3", "设备ID3");
			put("SV3", "软件版本3");
			put("PV3", "协议版本3");
			put("RGBR3", "保留字节3");
			
			put("FID4", "厂家ID4");
			put("ET4", "设备类型4");
			put("EID4", "设备ID4");
			put("SV4", "软件版本4");
			put("PV4", "协议版本4");
			put("RGBR4", "保留字节4");
			
			put("FID5", "厂家ID5");
			put("ET5", "设备类型5");
			put("EID5", "设备ID5");
			put("SV5", "软件版本5");
			put("PV5", "协议版本5");
			put("RGBR5", "保留字节5");

		}
	};
	
	private String itemAllKey = "itemAll_key";
	
	@Autowired
	private CacheService cacheService;
	
	public void clear() {
		insCmdMap.clear();
	}
	
	public String getGeHuaMap(String shortCode){
		
		String shortCodeName = gehuaMap.get(shortCode);
		
		if (StringUtils.isEmpty(shortCodeName)) {
			return shortCode;
		}
		
		return shortCodeName;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getInsCmdCache(String dataMarker){
		
		List<Map<String, Object>> insList;
		try {
			Object obj = insCmdMap.get(dataMarker);
			if (obj != null) {
				return (List<Map<String, Object>>) obj;
			}
			
			Response<?> res = cacheService.insCache(dataMarker);
			insList = (List<Map<String, Object>>) res.getData();
			insCmdMap.put(dataMarker, insList);
			return insList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, String>> itemAll(){
		
		try {
			Object obj = insCmdMap.get(itemAllKey);
			if (obj != null) {
				return (Map<String, Map<String, String>>) obj;
			}
			
			Response<?> res = cacheService.itemAll();
			
			if (res.getData() == null) {
				return null;
			}
			
			List<Map<String, Object>> itemAllList = (List<Map<String, Object>>) res.getData();
			Map<String, Map<String, String>> itemMap = new HashMap<String, Map<String, String>>();
			for (Map<String, Object> itemM : itemAllList) {
				Map<String, String> itemChildMap = new HashMap<String, String>();
				itemChildMap.put("itemCode", (String) itemM.get("itemCode"));
				itemChildMap.put("itemName", (String) itemM.get("itemName"));
				itemMap.put((String) itemM.get("itemShortCode"), itemChildMap);
			}
			insCmdMap.put(itemAllKey, itemMap);
			return itemMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	/*@SuppressWarnings("unchecked")
	public List<Map<String, Object>> mdbusItemAll(String equipNumber){
		
		try {
			Object obj = insCmdMap.get(itemAllKey);
			if (obj != null) {
				return (Map<String, Map<String, String>>) obj;
			}
			
			Response<?> res = cacheService.modbusItems(equipNumber);
			
			logger.debug("645 + mdbus, mdbusItemAll method, Response res = {} ",(res == null ? "" : res.toString()));
			
			if (res.getData() == null) {
				return null;
			}
			
			List<Map<String, Object>> itemAllList = (List<Map<String, Object>>) res.getData();
			Map<String, Map<String, String>> itemMap = new HashMap<String, Map<String, String>>();
			for (Map<String, Object> itemM : itemAllList) {
				Map<String, String> itemChildMap = new HashMap<String, String>();
				itemChildMap.put("itemCode", (String) itemM.get("itemCode"));
				itemChildMap.put("itemName", (String) itemM.get("itemName"));
				itemMap.put((String) itemM.get("itemShortCode"), itemChildMap);
			}
			
			if (insCmdMap.containsKey(itemAllKey)) {
				Map<String, Map<String, String>> mdCmdMap = (Map<String, Map<String, String>>) insCmdMap.get(itemAllKey);
				mdCmdMap.putAll(itemMap);
			}
			return itemAllList;
		} catch (Exception e) {
			logger.error("645 + mdbus, mdbusItemAll method, e = {}", e);
		}
		
		return null;
		
	}*/

}
