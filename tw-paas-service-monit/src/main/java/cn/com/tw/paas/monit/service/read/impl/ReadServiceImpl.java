package cn.com.tw.paas.monit.service.read.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.core.jms.cons.MqCons;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.common.utils.cons.code.PushStatusEm;
import cn.com.tw.paas.monit.entity.business.org.OrgApplicationExpand;
import cn.com.tw.paas.monit.entity.business.read.ReadHistoryExtend;
import cn.com.tw.paas.monit.entity.business.read.ReadHistorySimple;
import cn.com.tw.paas.monit.entity.db.base.InsDataItem;
import cn.com.tw.paas.monit.entity.db.org.EquipNetStatus;
import cn.com.tw.paas.monit.entity.db.org.PushLog;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.entity.db.read.ReadHistory;
import cn.com.tw.paas.monit.entity.db.read.ReadLast;
import cn.com.tw.paas.monit.mapper.org.EquipNetStatusMapper;
import cn.com.tw.paas.monit.mapper.org.OrgApplicationMapper;
import cn.com.tw.paas.monit.mapper.org.PushLogMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.mapper.orgEquipment.OrgEquipmentMapper;
import cn.com.tw.paas.monit.mapper.read.ReadHistoryMapper;
import cn.com.tw.paas.monit.mapper.read.ReadLastMapper;
import cn.com.tw.paas.monit.service.read.ReadService;
import cn.com.tw.paas.monit.service.sys.CmdInsCacheService;

@Service
public class ReadServiceImpl implements ReadService {

	@Autowired
	private CmdInsCacheService cmdInsCacheService;

	@Autowired
	private ReadLastMapper readLastMapper;

	@Autowired
	private ReadHistoryMapper readHistoryMapper;

	@Autowired
	private OrgEquipmentMapper equipmentMapper;

	@Autowired
	private PushLogMapper pushLogMapper;

	@Autowired
	private OrgApplicationMapper applicationMapper;

	@Autowired
	private EquipNetStatusMapper equipNetStatusMapper;

	@Autowired
	private MqHandler mqHandler;
	@Autowired
	private TerminalEquipMapper terminalEquipMapper;

	/**
	 * 解析中间件返回的数据并保存
	 */
	@Override
	@Transactional
	public void saveRead(String uniqueId, Map<String, Object> dataMap) {
		String[] uniqueIds = uniqueId.split(":");
		// 仪表地址
		String meterAddr = uniqueIds[0];
		// 时间戳
		String readTimeStr = uniqueIds[1];
		Date readTime = new Date(Long.valueOf(readTimeStr));
		// 数据标识
		String dataMarker = (String) dataMap.get("dataMarker");
		// 数据
		String dataValueStr = (String) dataMap.get("dataValues");
		// 表号
		String commAddr = (String) dataMap.get("addr");
		if (StringUtils.isEmpty(dataMarker) || StringUtils.isEmpty(dataValueStr)) {
			return;
		}
		String[] dataValues = dataValueStr.split(",");
		// 根据数据标识查询相关数据项
		@SuppressWarnings("unchecked")
		List<InsDataItem> items = (List<InsDataItem>) cmdInsCacheService.selectInsByDataMarker(dataMarker);
		if (null == items) {
			return;
		}
		// 默认倍率
		BigDecimal pt = new BigDecimal("1");
		BigDecimal ct = new BigDecimal("1");
		// 查询仪表
		TerminalEquip terminalEquip = terminalEquipMapper.selectByEquipNumber(meterAddr);
		String orgId = "";
		String appId = "";
		String equipType = "";
		String elecMeterType = "";
		if (null != terminalEquip) {
			orgId = terminalEquip.getOrgId();
			appId = terminalEquip.getAppId();
			equipType = terminalEquip.getEquipTypeCode();
			//判断是否电表，如果是拿到电压变比和电流变比，是否三相电表还是单相电表
			if (equipType.equals("110000")) {
				elecMeterType = terminalEquip.getElecMeterTypeCode();
				pt = terminalEquip.getElecPt();
				ct = terminalEquip.getElecCt();
			}
			// 更新网络状态
			EquipNetStatus status = new EquipNetStatus();
			status.setCommAddr(meterAddr);
			status.setNetStatus((byte) 1);
			status.setOnlineTime(new Date());
			equipNetStatusMapper.replace(status);
		}
		String url = "";
		String appKey = "";
		OrgApplicationExpand app = new OrgApplicationExpand();
		app = applicationMapper.selectByPrimaryKey(appId);
		if (app != null && !StringUtils.isEmpty(app.getCallbackUrl())) {
			url = app.getCallbackUrl();
			appKey = app.getAppKey();
		}
		// 查询存储读取记录的目标表
		// String insId = items.get(0).getInsId();
		// InsFrequency insF = (InsFrequency) cache.get(CacheName.ins_frequency,
		// insId);
		// // 是否需要保存记录
		// boolean needSave = false;
		// if (null != insF) {
		// if (!StringUtils.isEmpty(insF.getSaveTable())) {
		// needSave = true;
		// }
		// }
		//
		List<ReadLast> readList = new ArrayList<ReadLast>();
		Map<String, Object> mapData = new TreeMap<String, Object>();
		// 采集记录
		// ReadHistory readRecord = new ReadHistory();
		for (InsDataItem item : items) {
			BigDecimal readValue;
			// 有对应数据则保存
			try {
				readValue = new BigDecimal(dataValues[item.getItemIndex() - 1]);
				if (item.getIsMultiplyCt().equals((byte) 1)) {
					readValue = readValue.multiply(ct);
				}
				if (item.getIsMultiplyPt().equals((byte) 1)) {
					readValue = readValue.multiply(pt);
				}
				mapData.put(item.getItemCode(), readValue.toString());
			} catch (Exception e) {
				continue;
			}
			ReadLast temp = new ReadLast();
			temp.setOrgId(orgId);
			temp.setAppId(appId);
			temp.setMeterType(equipType);
			temp.setElecMeterType(elecMeterType);
			temp.setMeterAddr(meterAddr);
			temp.setItemCode(item.getItemCode());
			temp.setItemName(item.getItemName());
			temp.setReadValue(readValue.toPlainString());
			temp.setIsPush((byte) 0);
			temp.setReadTime(readTime);
			readList.add(temp);
			// if (needSave) {
			// // 反射赋值
			// try {
			// Method setMethod = ReadHistory.class.getMethod("setValue" +
			// item.getSaveIndex(), BigDecimal.class);
			// setMethod.invoke(readRecord, readValue);
			// } catch (Exception e) {
			// continue;
			// }
			// }
			//
		}

		if (readList.size() > 0) {
			for (ReadLast readOne : readList) {
				// 更新最近读数
				readLastMapper.replace(readOne);
				// 插入历史读数
				readHistoryMapper.insert(readOne);
			}
		}
		try {
			// TODO 生成推送记录
			if (null != terminalEquip) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("equipNumber", commAddr);
				map.put("appKey", appKey);
				map.put("lastSaveTime", readTimeStr);
				map.put("data", mapData);
				map.put("dataType", "221");
				map.put("commType", terminalEquip.getNetTypeCode());
				String pushData = JsonUtils.objectToJson(map);

				PushLog log = new PushLog();
				log.setLogId(CommUtils.getUuid());
				log.setOrgId(orgId);
				log.setAppId(appId);
				log.setPushUrl(url);
				log.setBusinessType("221");//数据：221
				log.setPushData(pushData);
				log.setTryTimes(0);
				log.setAppName(app.getAppName());
				log.setOrgName(app.getOrgName());
				log.setStatusCode(PushStatusEm.PUSH_CREATE.getValue());
				log.setPushStatus(PushStatusEm.PUSH_CREATE.getValue());
				pushLogMapper.insert(log);

				String logJson = JsonUtils.objectToJson(log);
				// 往Mq里发送数据
				mqHandler.sendDelay(MqCons.QUEUE_PUSH_RETRY, logJson, 10);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询最近读数
	 */
	@Override
	public List<ReadLast> selectLast(ReadLast readLast) {
		List<ReadLast> readLasts = readLastMapper.selectLast(readLast);
		return readLasts;
	}

	/**
	 * 查询历史数据（分页）
	 */
	@Override
	public List<ReadHistory> selectHistoryPage(Page page) {
		List<ReadHistory> readHis = readHistoryMapper.selectPage(page);
		return readHis;
	}

	/**
	 * 查询历史数据
	 */
	@Override
	public List<ReadHistory> selectHistory(ReadHistoryExtend param) {
		List<ReadHistory> readHis = readHistoryMapper.selectList(param);
		return readHis;
	}

	/**
	 * 查询历史数据(API,只返回必要数据)
	 */
	@Override
	public List<ReadHistorySimple> selectHistoryForApi(Page param) {
		List<ReadHistorySimple> result = readHistoryMapper.selectListForApi(param);
		return result;
	}

	@Override
	public ReadLast selectByAddrAndItem(ReadLast record) {
		return readLastMapper.selectByAddrAndItem(record);
	}

	@Override
	public List<ReadLast> selectRealTimeTrace(String meterAddr) {
		return readLastMapper.selectRealTimeTrace(meterAddr);
	}

	@Override
	public List<ReadHistoryExtend> selectTrace(ReadHistoryExtend param) {
		return readHistoryMapper.selectTrace(param);
	}

	@Override
	public List<ReadHistory> selectHistoryShow(Page page) {
		int offset = (page.getPage() - 1) * page.getRows();
		Map<String, Object> params = new HashMap<String, Object> ();
		params.put("offset", offset);
		params.put("end", page.getRows());
		params.put("paramObj", page.getParamObj());
		List<ReadHistory> readHistList = readHistoryMapper.selectHistoryShow(params);
		page.setTotalRecord(1000000);
		page.setData(readHistList);
		return readHistList;
	}

	@Override
	public List<ReadLast> selectReadLast(Page pageData) {
		List<ReadLast> readLasts = readLastMapper.selectReadLastByPage(pageData);
		return readLasts;
	}

	@Override
	public List<ReadHistory> select24hours(ReadHistoryExtend readHistoryExtend) {
		if("110000".equals(readHistoryExtend.getMeterType())){
			readHistoryExtend.setItemCode("positiveActiveE");
		}else if("120000".equals(readHistoryExtend.getMeterType())){
			readHistoryExtend.setItemCode("waterFlow");
		}
		
		
		return readHistoryMapper.select24hours(readHistoryExtend);
	}
	
	@Override
	public List<ReadLast> selectByMeterAddr(String meterAddr){
		return readLastMapper.selectByMeterAddr(meterAddr);
	}
}
