package cn.com.tw.paas.service.queue.common.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.com.tw.paas.service.queue.eum.HisColEnum;
import cn.com.tw.paas.service.queue.eum.LogColEnum;

public class EntityUtils {
	
	public static Map<String, Object> storeData(String orgId, String appId, String appKey, String orgName, String appName, String equipType, String elecMeterType, String meterAddr, String netTypeCode, String pUrl, String values, List<Map<String, Object>> dataMarkerInsList, String readTimeStr, int elecPt, int elecCt, String businessNo, String dataMarker, String protocolType){
		/**
		 * 设置历史存储信息
		 */
		Map<String, Object> hisStoreInfo = new HashMap<String, Object> ();
		hisStoreInfo.put(HisColEnum.oId.name(), orgId);//机构Id
		hisStoreInfo.put(HisColEnum.aId.name(), appId);//应用Id
		hisStoreInfo.put(HisColEnum.aKy.name(), appKey);//app_key
		hisStoreInfo.put(HisColEnum.oNm.name(), orgName);//机构名称
		hisStoreInfo.put(HisColEnum.aNm.name(), appName);//应用名称
		hisStoreInfo.put(HisColEnum.tTy.name(), equipType);//终端类型
		hisStoreInfo.put(HisColEnum.lTy.name(), "0");//回路类型
		if (!StringUtils.isEmpty(elecMeterType)) {
			hisStoreInfo.put(HisColEnum.tETy.name(), elecMeterType);//电表子分类
			setLoopType(hisStoreInfo, elecMeterType, dataMarker);
		}
		
		hisStoreInfo.put(HisColEnum.dm.name(), dataMarker);//数据标识，数据通过哪个数据标识采集的
		hisStoreInfo.put(HisColEnum.tNo.name(), meterAddr);//仪表编号
		hisStoreInfo.put(HisColEnum.tNNo.name(), netTypeCode);//网络编号
		hisStoreInfo.put(HisColEnum.pUl.name(), pUrl);//推送url
		
		if (!StringUtils.isEmpty(values)) {
			
			//如果是modbus协议
			if (StringUtils.isNotEmpty(protocolType) && "MODBUS".equals(protocolType)) {
				
				if ("304".equals(dataMarker) || "300".equals(dataMarker)) {
					String[] dataValues = values.split(",");
					hisStoreInfo.put("IF", String.valueOf(1 - Integer.parseInt(dataValues[0])));
				} else {
					throw new IllegalArgumentException("this message is modbus protocol, datamarker can not parse!!!");
				}
				
			}else{//如果是645协议
				String[] dataValues = values.split(",");
				
				// 默认倍率
				BigDecimal pt = new BigDecimal("1");
				BigDecimal ct = new BigDecimal("1");
				
				//判断是否电表，如果是拿到电压变比和电流变比，是否三相电表还是单相电表
				if (equipType.equals("110000")) {
					pt = new BigDecimal(elecPt);
					ct = new BigDecimal(elecCt);
				}
				
				//读取数据code集合
				//StringBuffer sb = new StringBuffer();
				for (Map<String, Object> ins : dataMarkerInsList) {
					// 有对应数据则保存
					try {
						BigDecimal readValue = new BigDecimal(dataValues[(int)ins.get("itemIndex") - 1]);
						if (ins.get("isMultiplyCt").equals((byte) 1)) {
							readValue = readValue.multiply(ct);
						}
						if (ins.get("isMultiplyCt").equals((byte) 1)) {
							readValue = readValue.multiply(pt);
						}
						
						String itemCode = (String)ins.get("itemShortCode");
						/*sb.append(itemCode).append(",");*/
						hisStoreInfo.put(itemCode, readValue.toString());
						
						//采集数据项的值
						/*hisStoreInfo.put(HisColEnum.dcode.name() + dCount, (String)ins.get("itemCode"));
						hisStoreInfo.put(HisColEnum.dval.name() + dCount, readValue.toPlainString());
						hisStoreInfo.put(HisColEnum.dname.name() + dCount, (String)ins.get("itemName"));*/
					} catch (Exception e) {
						continue;
					}
				}
			}
			
			//String dSet = sb.substring(0,sb.length() - 1);
			//100表示默认状态，创建状态
			//hisStoreInfo.put(HisColEnum.dSet.name(), dSet);//采集数据的数量 d表示read数据
		}
		
		
		if (!StringUtils.isEmpty(businessNo)) {
			hisStoreInfo.put(HisColEnum.bNo.name(), businessNo);
			hisStoreInfo.put(HisColEnum.at.name(), "1");
		}else{
			hisStoreInfo.put(HisColEnum.at.name(), "0");
		}
		hisStoreInfo.put(HisColEnum.dTy.name(), "221");//采集数据类型
		hisStoreInfo.put(HisColEnum.pUs.name(), "100");//推送状态 100表示创建 p表示推送
		hisStoreInfo.put(HisColEnum.pTs.name(), "0");//推送次数
		hisStoreInfo.put(HisColEnum.pTm.name(), new Date().getTime());
		
		hisStoreInfo.put(HisColEnum.rTm.name(), Long.parseLong(readTimeStr));//读取时间
		hisStoreInfo.put(HisColEnum.cTm.name(), new Date().getTime());//读取时间
		return hisStoreInfo;
	}
	
	
	public static Map<String, Object> geHuaStoreData(String orgId, String appId, String appKey, String orgName, String appName, String equipType, String elecMeterType, String meterAddr, String netTypeCode, String pUrl, String readTimeStr, String businessNo, String dataMarker, String protocolType, Map<String,Object> geHuaData){
		
		/**
		 * 设置历史存储信息
		 */
		Map<String, Object> hisStoreInfo = new HashMap<String, Object> ();
		
		if (geHuaData == null || geHuaData.isEmpty()) {
			return hisStoreInfo;
		}
		
		hisStoreInfo.put(HisColEnum.oId.name(), orgId);//机构Id
		hisStoreInfo.put(HisColEnum.aId.name(), appId);//应用Id
		hisStoreInfo.put(HisColEnum.aKy.name(), appKey);//app_key
		hisStoreInfo.put(HisColEnum.oNm.name(), orgName);//机构名称
		hisStoreInfo.put(HisColEnum.aNm.name(), appName);//应用名称
		hisStoreInfo.put(HisColEnum.tTy.name(), equipType);//终端类型
		hisStoreInfo.put(HisColEnum.lTy.name(), "0");//回路类型
		if (!StringUtils.isEmpty(elecMeterType)) {
			hisStoreInfo.put(HisColEnum.tETy.name(), elecMeterType);//电表子分类
			setLoopType(hisStoreInfo, elecMeterType, dataMarker);
		}
		
		hisStoreInfo.put(HisColEnum.dm.name(), dataMarker);//数据标识，数据通过哪个数据标识采集的
		hisStoreInfo.put(HisColEnum.tNo.name(), meterAddr);//仪表编号
		hisStoreInfo.put(HisColEnum.tNNo.name(), netTypeCode);//网络编号
		hisStoreInfo.put(HisColEnum.pUl.name(), pUrl);//推送url
		
		
		if (!StringUtils.isEmpty(businessNo)) {
			hisStoreInfo.put(HisColEnum.bNo.name(), businessNo);
			hisStoreInfo.put(HisColEnum.at.name(), "1");
		}else{
			hisStoreInfo.put(HisColEnum.at.name(), "0");
		}
		hisStoreInfo.put(HisColEnum.dTy.name(), "221");//采集数据类型
		hisStoreInfo.put(HisColEnum.pUs.name(), "100");//推送状态 100表示创建 p表示推送
		hisStoreInfo.put(HisColEnum.pTs.name(), "0");//推送次数
		hisStoreInfo.put(HisColEnum.pTm.name(), new Date().getTime());
		
		hisStoreInfo.put(HisColEnum.rTm.name(), Long.parseLong(readTimeStr));//读取时间
		hisStoreInfo.put(HisColEnum.cTm.name(), new Date().getTime());//读取时间
		//暂时用系统时间， 把歌华协议里面的时间暂时去掉
		geHuaData.remove(HisColEnum.rTm.name());
		
		//暂时加在这里，将IWT入水温度， ODT室外温度， OWT出水温度，STT设定温度，IDT室内温度 如果为空,设置填充为FFFF
		filterTemperature2FF(geHuaData);
		
		hisStoreInfo.putAll(geHuaData);
		return hisStoreInfo;
	}
	
	/**
	 * //将IWT入水温度， ODT室外温度， OWT出水温度，STT设定温度，IDT室内温度 如果为空设置为FF
	 * @param geHuaData
	 */
	private static void filterTemperature2FF(Map<String, Object> geHuaData){
		
		if (geHuaData == null) {
			return;
		}
		
		String[] filterItem = new String[]{"IWT","ODT","OWT","STT","IDT"};
		for (String item : filterItem) {
			
			//如果不存在该项，就不做任何处理，继续下一个
			if (!geHuaData.containsKey(item)) {
				continue;
			}
			
			//如果存在，获取对应的value，判断如果为空，设置为FFFF
			String itemVal = (String) geHuaData.get(item);
			
			if (StringUtils.isEmpty(itemVal)) {
				geHuaData.put(item, "FFFF");
			}
		}
		
	}
	
	/*public static Map<String, Object> mdBusStoreData(String orgId, String appId, String appKey, String orgName, String appName, String equipType, String elecMeterType, String meterAddr, String netTypeCode, String pUrl, String readTimeStr, String businessNo, String dataMarker, String protocolType, Map<String,String> mdbusData){
		
		*//**
		 * 设置历史存储信息
		 *//*
		Map<String, Object> hisStoreInfo = new HashMap<String, Object> ();
		
		if (mdbusData.isEmpty()) {
			return hisStoreInfo;
		}
		
		hisStoreInfo.put(HisColEnum.oId.name(), orgId);//机构Id
		hisStoreInfo.put(HisColEnum.aId.name(), appId);//应用Id
		hisStoreInfo.put(HisColEnum.aKy.name(), appKey);//app_key
		hisStoreInfo.put(HisColEnum.oNm.name(), orgName);//机构名称
		hisStoreInfo.put(HisColEnum.aNm.name(), appName);//应用名称
		hisStoreInfo.put(HisColEnum.tTy.name(), equipType);//终端类型
		hisStoreInfo.put(HisColEnum.lTy.name(), "0");//回路类型
		if (!StringUtils.isEmpty(elecMeterType)) {
			hisStoreInfo.put(HisColEnum.tETy.name(), elecMeterType);//电表子分类
			setLoopType(hisStoreInfo, elecMeterType, dataMarker);
		}
		
		hisStoreInfo.put(HisColEnum.dm.name(), dataMarker);//数据标识，数据通过哪个数据标识采集的
		hisStoreInfo.put(HisColEnum.tNo.name(), meterAddr);//仪表编号
		hisStoreInfo.put(HisColEnum.tNNo.name(), netTypeCode);//网络编号
		hisStoreInfo.put(HisColEnum.pUl.name(), pUrl);//推送url
		
		
		if (!StringUtils.isEmpty(businessNo)) {
			hisStoreInfo.put(HisColEnum.bNo.name(), businessNo);
			hisStoreInfo.put(HisColEnum.at.name(), "1");
		}else{
			hisStoreInfo.put(HisColEnum.at.name(), "0");
		}
		hisStoreInfo.put(HisColEnum.dTy.name(), "221");//采集数据类型
		hisStoreInfo.put(HisColEnum.pUs.name(), "100");//推送状态 100表示创建 p表示推送
		hisStoreInfo.put(HisColEnum.pTs.name(), "0");//推送次数
		hisStoreInfo.put(HisColEnum.pTm.name(), new Date().getTime());
		
		hisStoreInfo.put(HisColEnum.rTm.name(), Long.parseLong(readTimeStr));//读取时间
		hisStoreInfo.put(HisColEnum.cTm.name(), new Date().getTime());//读取时间
		
		hisStoreInfo.putAll(mdbusData);
		return hisStoreInfo;
	}*/
	
	private static void setLoopType(Map<String, Object> hisStoreInfo, String elecMeterType, String dataMarker){
		if ("111001".equals(elecMeterType) || "111002".equals(elecMeterType)) {
			if ("00000000".equals(dataMarker)) {
				hisStoreInfo.put(HisColEnum.lTy.name(), "0");
			}else if ("04601001".equals(dataMarker)) {
				hisStoreInfo.put(HisColEnum.lTy.name(), "1");
			}else if ("04601002".equals(dataMarker)) {
				hisStoreInfo.put(HisColEnum.lTy.name(), "2");
			}
		}else {
			hisStoreInfo.put(HisColEnum.lTy.name(), "0");
		}
		
	}
	
	public static Map<String, Object> apiLogData(String orgId, String appId, String orgName, String appName, String apiUrl, String authToken, String reqIp, String appBusNo, String reqData, String reqTime, String busType, String busNo, String status, String respData, String createTime) {
		Map<String, Object> logMap = new HashMap<String, Object> ();
		logMap.put(LogColEnum.appId.name(), appId);
		logMap.put(LogColEnum.orgId.name(), orgId);
		logMap.put(LogColEnum.orgName.name(), orgName);
		logMap.put(LogColEnum.appName.name(), appName);
		logMap.put(LogColEnum.apiUrl.name(), apiUrl);
		logMap.put(LogColEnum.authToken.name(), authToken);
		logMap.put(LogColEnum.reqIp.name(), reqIp);
		logMap.put(LogColEnum.appBusNo.name(), appBusNo);
		logMap.put(LogColEnum.reqData.name(), reqData);
		logMap.put(LogColEnum.reqTime.name(), reqTime);
		logMap.put(LogColEnum.busType.name(), busType);
		logMap.put(LogColEnum.busNo.name(), busNo);
		logMap.put(LogColEnum.status.name(), status);
		logMap.put(LogColEnum.respData.name(), respData);
		logMap.put(LogColEnum.createTime.name(), createTime);
		return logMap;
	}
	
	public static Map<String, Object> getPushData(String meterAddr, String appKey, String readTimeStr, Map<String, Object> pushItemData, String equipType, String netTypeCode, String businessNo, String loopType){
		// 封装推送数据
		Map<String, Object> pushData = new HashMap<String, Object>();
		pushData.put("equipNumber", meterAddr);
		pushData.put("appKey", appKey);
		pushData.put("lastSaveTime", readTimeStr);
		pushData.put("data", pushItemData);
		pushData.put("equipType", equipType);
		//221 表示正常数据, 110 表示异常警告数据
		pushData.put("dataType", "221");
		pushData.put("commType", netTypeCode);
		
		loopType = StringUtils.isEmpty(loopType) ? "0" : loopType;
		pushData.put("loopType", loopType);
		if (!StringUtils.isEmpty(businessNo)) {
			pushData.put("businessNo", businessNo);
		}
		
		return pushData;
	}

}
