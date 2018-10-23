package cn.com.tw.paas.service.queue.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.security.MD5Utils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.queue.common.cache.CacheLocalService;
import cn.com.tw.paas.service.queue.dao.read.HReadHisDao;
import cn.com.tw.paas.service.queue.dao.read.HReadLastDao;
import cn.com.tw.paas.service.queue.entity.ElecUnderVoltageEvent;
import cn.com.tw.paas.service.queue.entity.StopValEvent;
import cn.com.tw.paas.service.queue.entity.WaterUnderVoltageEvent;
import cn.com.tw.paas.service.queue.eum.HisColEnum;

@Service
public class ReadDataService {
	
	private static Logger logger = LoggerFactory.getLogger(ReadDataService.class);
	
	@Autowired
	private CacheLocalService cacheService;
	
	@Autowired
	private TerminalService terminalService;
	
	@Autowired
	private HReadHisDao hReadHisDao;
	
	@Autowired
	private HReadLastDao headLastDao;
	
	@Autowired
	private MqHandler mqHandler;
	
	@Autowired
	private EventService eventService;
	
	public void createHisTable(){
		hReadHisDao.createReadHisTable();
	}
	
	public void createLastTable(){
		headLastDao.createReadHisTable();
	}
	
	public void putLast(Map<String, Object> storeDataMap){
		
		String pUl = (String) storeDataMap.get(HisColEnum.pUl.name());
		String elecVoltageRating = (String) storeDataMap.get("elecVoltageRating");
		
		storeDataMap.remove(HisColEnum.pUl.name());
		storeDataMap.remove(HisColEnum.dTy.name());
		storeDataMap.remove(HisColEnum.pUs.name());
		storeDataMap.remove(HisColEnum.pTs.name());
		storeDataMap.remove("dSet");
		storeDataMap.remove(HisColEnum.pTm.name());
		storeDataMap.remove(HisColEnum.bNo.name());
		storeDataMap.remove("elecVoltageRating");
		
		String meterAddr = (String) storeDataMap.get(HisColEnum.tNo.name());
		if (StringUtils.isEmpty(meterAddr)) {
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数不能为空");
		}
		
		String loopType = (String)storeDataMap.get(HisColEnum.lTy.name());
		loopType = StringUtils.isEmpty(loopType) ? "0" : loopType;
		StringBuffer sb = new StringBuffer(MD5Utils.digest(meterAddr)).append("|").append(loopType);
		
		try {
			event(storeDataMap, pUl, elecVoltageRating);
		} catch (Exception e) {
			logger.error("putlast method : event exception, e = {} ", e);
		}
		
		headLastDao.putLast(sb.toString(), storeDataMap);
	}
	
	/**
	 * 判断异常
	 */
	private void event(Map<String, Object> storeDataMap, String pUl, String elecVoltageRating) {
		//止码异常推送
		String meterAddr = (String) storeDataMap.get(HisColEnum.tNo.name());
		String termType = (String) storeDataMap.get(HisColEnum.tTy.name());
		String shortCode = null;
		if ("110000".equals(termType)) {
			shortCode = "TAE";
		}else if ("120000".equals(termType)){
			shortCode = "WF";
		}
		String newValue = (String) storeDataMap.get(shortCode);
		StopValEvent valEvent = new StopValEvent();
		valEvent.setTermNo(meterAddr);
		valEvent.setTermType(termType);
		valEvent.setNewVal(newValue);
		valEvent.setpUrl(pUl);
		valEvent.setShortCode(shortCode);
		valEvent.setOrgId((String)storeDataMap.get(HisColEnum.oId.name()));
		eventService.stopValEventToPush(valEvent);
		
		WaterUnderVoltageEvent waterEvent = new WaterUnderVoltageEvent();
		waterEvent.setTermNo(meterAddr);
		waterEvent.setTermType(termType);
		waterEvent.setpUrl(pUl);
		waterEvent.setOrgId((String)storeDataMap.get(HisColEnum.oId.name()));
		waterEvent.setVoltageValue((String)storeDataMap.get("WB"));
		eventService.waterUnderVoltageEventToPush(waterEvent);
		
		ElecUnderVoltageEvent elecEvent = new ElecUnderVoltageEvent();
		elecEvent.setTermNo(meterAddr);
		elecEvent.setTermType(termType);
		elecEvent.setpUrl(pUl);
		elecEvent.setOrgId((String)storeDataMap.get(HisColEnum.oId.name()));
		elecEvent.setVoltageValue((String)storeDataMap.get("Ua"));
		eventService.elecOverAndUnderEventToPush(elecEvent, elecVoltageRating);
	}
	
	/**
	 * 通过表号|回路类型查询变记录
	 * @param key
	 * @return
	 */
	public Map<String, Object> queryLastByTermNo(String key) {
		return headLastDao.queryLastByKey(key, null);
	}
	
	/**
	 * 通过机构Id查询所有数据
	 * @param familyColName
	 * @param colValue
	 * @return
	 */
	public List<Map<String, Object>> queryLastByColValue(String familyColName, String colValue){
		return headLastDao.queryLastByColValue(familyColName, colValue);
	}
	
	/**
	 * 通过机构Id，查询下面所有主回路的数据
	 * @param familyColName
	 * @param colValue
	 * @return
	 */
	public List<Map<String, Object>> queryLastByColValueMainLoop(String familyColName, String colValue){
		return headLastDao.queryLastByColValueMainLoop(familyColName, colValue);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLastByPage(Page page) {
		
		Map<String, Object> params = (Map<String, Object>) page.getParamObj();
		
		String equipNumber = (String) params.get("equipNumber");
		String itemCode = (String) params.get("itemCode");
		
		String appId = (String) params.get("appId");
		//如果appId为空,则不允许操作
		if (StringUtils.isEmpty(appId)){
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "appId不能为空");
		}

		List<Map<String, Object>> lastList = null;
		//如果为设备编号没有传值，认为查询所有,如果设备编号有，查询一条
		if (StringUtils.isEmpty(equipNumber)) {
			
			//计算起始页
	        int firstPage = (page.getPage() - 1) * page.getRows();
	        int endPage = firstPage + page.getRows();
			
			Response<?> termResp = terminalService.selectTerminalEquipPage(page);
			List<Map<String, Object>> termData = (List<Map<String, Object>>) termResp.getData();
			
			if (termData == null) {
				return lastList;
			}
			
			List<byte[]> termNoList = new ArrayList<byte[]>();
			int count = 0; 
			for (Map<String, Object> termMap : termData) {
				String termNo = (String) termMap.get("equipNumber");
				if(StringUtils.isEmpty(termNo)) {
					continue;
				}
				if (count >= firstPage && count < endPage) {
					termNoList.add(Bytes.toBytes(MD5Utils.digest(termNo)));
				}
				
				count++;
			}
			
			page.setTotalRecord(count);
			lastList = headLastDao.queryLastByKeys(termNoList, itemCode);
			page.setData(lastList);
		}else {
			Map<String, Object> resultMap = headLastDao.queryLastByKey(equipNumber, itemCode);
			
			if (resultMap == null) {
				return lastList;
			}
			
			lastList = new ArrayList<Map<String, Object>>();
			lastList.add(resultMap);
			page.setTotalRecord(1);
		}
		
		return lastList;
	}
	
	public List<Map<String, Object>> queryLastByKeys(String equipNumbers, String itemCode) {
		List<byte[]> termNoList = new ArrayList<byte[]>();
		String[] enquipNumbers = equipNumbers.split(",");
		for(String equipNumber : enquipNumbers) {
			termNoList.add(Bytes.toBytes(new StringBuffer(MD5Utils.digest(equipNumber)).append("|0").toString()));
		}
		List<Map<String, Object>> lastList = headLastDao.queryLastByKeys(termNoList, itemCode);
		return lastList;
	}
	
	public void queryReadByPage(Page page) {
		List<Map<String, Object>> result = hReadHisDao.queryReadByPage(page);
		page.setData(result);
	}
	
	public void queryPushByPage(Page page) {
		List<Map<String, Object>> result = hReadHisDao.queryPushByPage(page);
		page.setData(result);
	}
	
	/**
	 * 查询历史表， 只含有止码的数据
	 * @param appId
	 * @param curDate
	 * @param meterAddr
	 * @param meterType
	 * @return
	 */
	public List<Map<String, Object>> queryListStopValueByRowKey(String appId, String curDate, String meterAddr, String meterType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", appId);
		params.put("startTime", curDate + " 00:00:00");
		params.put("endTime", curDate + " 23:59:59");
		params.put("meterAddr", meterAddr);
		
		String qualifierName = null;
		if ("110000".equals(meterType)) {
			qualifierName = "TAE";
		}else {
			qualifierName = "WF";
		}
		List<Map<String, Object>> result = hReadHisDao.queryDateListByRowKeyQualifier(params, null, qualifierName);
		return result;
	}
	
	/**
	 * 通过时间区间，列值，查询指定列名及数据
	 * 比如，查询当天，某个机构下面，某块仪表（列值），止码的数据（列名）
	 * @param appId
	 * @param startTime  "2017-10-25 00:00:00"
	 * @param endTime "2018-10-25 00:00:00"
	 * @param meterAddr
	 * @param meterType
	 * @return
	 */
	public List<Map<String, Object>> queryListColNameAndColValByRowKey(String appId, String startTime, String endTime, String meterAddr, String colNames, String qualifierNameIsNoNull) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", appId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("meterAddr", meterAddr);
		List<Map<String, Object>> result = hReadHisDao.queryDateListByRowKeyQualifier(params, colNames, qualifierNameIsNoNull);
		return result;
	}
	
	/**
	 * 通过时间区间查询历史表数据
	 * @param appId
	 * @param curDate
	 * @param meterAddr
	 * @param meterType
	 * @return
	 */
	public List<Map<String, Object>> queryListByRowKey(String appId, String startTime, String endTime, String meterAddr) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", appId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("meterAddr", meterAddr);
		List<Map<String, Object>> result = hReadHisDao.queryList(params);
		return result;
	}
	
	
	public void updatePushInfo(Map<String, Object> pushInfo){
		hReadHisDao.updatePushInfo(pushInfo);
	}
	
	public void save(Map<String, Object> storeDataMap) {
		
		/**
		 * 设置历史存储信息
		 */
		hReadHisDao.putRadHis(storeDataMap);
	}

}
