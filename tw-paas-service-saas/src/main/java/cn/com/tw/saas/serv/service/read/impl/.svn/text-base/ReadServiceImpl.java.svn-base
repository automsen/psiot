package cn.com.tw.saas.serv.service.read.impl;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;





import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.core.cache.redis.RedisService;
import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.core.jms.cons.PsMqCons;
import cn.com.tw.common.enm.notify.NotifyBusTypeEm;
import cn.com.tw.common.enm.notify.NotifyLvlEm;
import cn.com.tw.common.utils.notify.Notify;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.saas.serv.common.utils.DateStrUtils;
import cn.com.tw.saas.serv.common.utils.DeductOrderCalculator;
import cn.com.tw.saas.serv.common.utils.cache.InsCacheMap;
import cn.com.tw.saas.serv.common.utils.cons.ApiTemplateCons;
import cn.com.tw.saas.serv.common.utils.cons.SysCommons;
import cn.com.tw.saas.serv.entity.business.cust.DeductOrderParams;
import cn.com.tw.saas.serv.entity.db.base.InsDataItem;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.db.cust.DeductOrder;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.entity.db.cust.SubAccountRecord;
import cn.com.tw.saas.serv.entity.db.read.ReadLast;
import cn.com.tw.saas.serv.entity.db.read.ReadRecord;
import cn.com.tw.saas.serv.entity.db.read.VReadLastElec;
import cn.com.tw.saas.serv.entity.db.rule.EnergyPrice;
import cn.com.tw.saas.serv.entity.db.rule.RuleAlarm;
import cn.com.tw.saas.serv.entity.org.OrgSmsConfig;
import cn.com.tw.saas.serv.entity.room.AllowanceRecord;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.room.RoomMeterSwitchingManagent;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.cust.CustomerMapper;
import cn.com.tw.saas.serv.mapper.cust.CustomerRoomMapper;
import cn.com.tw.saas.serv.mapper.cust.DeductOrderMapper;
import cn.com.tw.saas.serv.mapper.cust.RoomAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.cust.SubAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.org.OrgSmsConfigMapper;
import cn.com.tw.saas.serv.mapper.read.ReadLastMapper;
import cn.com.tw.saas.serv.mapper.read.ReadRecordMapper;
import cn.com.tw.saas.serv.mapper.read.VReadLastElecMapper;
import cn.com.tw.saas.serv.mapper.room.AllowanceRecordMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.rule.EnergyPriceMapper;
import cn.com.tw.saas.serv.mapper.rule.RuleAlarmMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterHisMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.command.CmdRecordService;
import cn.com.tw.saas.serv.service.read.ReadService;
import cn.com.tw.saas.serv.service.sys.ExceptionsRecordsService;

@Service
public class ReadServiceImpl implements ReadService {

	private Logger logger = LoggerFactory.getLogger(ReadServiceImpl.class);
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private CustomerRoomMapper customerRoomMapper;
	@Autowired
	private ReadLastMapper readLastMapper;
	@Autowired
	private ReadRecordMapper readRecordMapper;
	@Autowired
	private MeterMapper meterMapper;
	@Autowired
	private MeterHisMapper meterHisMapper;
	@Autowired
	private MqHandler mqHandler;
	@Autowired
	private RoomMapper roomMapper;
	@Autowired
	private EnergyPriceMapper energyPriceMapper;
	@Autowired
	private DeductOrderMapper deductOrderMapper;
	@Autowired
	private SubAccountRecordMapper subAccountRecordMapper;
	@Autowired
	private RoomAccountRecordMapper roomAccountRecordMapper;
	@Autowired
	private AllowanceRecordMapper allowanceRecordMapper;
	@Autowired
	private VReadLastElecMapper elecMapper;
	@Autowired
	private CmdRecordService cmdService;
	@Autowired
	private RuleAlarmMapper ruleAlarmMapper;
	@Autowired
	private OrgSmsConfigMapper orgSmsConfigMapper;
	@Autowired
	private RedisService redisService;
	@Autowired
	private ExceptionsRecordsService exceptionsRecordsService;

	@Override
	public VReadLastElec selectReadLastElecView(VReadLastElec vReadLastElec) {
		VReadLastElec vLastElecs = elecMapper.selectElecAll(vReadLastElec);
		return vLastElecs;
	}

	/**
	 * 查询指定时间至今的指定仪表的真实读数记录
	 */
	@Override
	public List<ReadRecord> selectByAddrAndTd(String meterAddr, String freezeTd, String dataItem, String loopType) {
		ReadRecord param = new ReadRecord();
		switch (dataItem) {
		case "voltage":// 查询电压
			param.setSaveTable("t_read_elec_voltage");
			break;
		case "current":// 查询电流
			param.setSaveTable("t_read_elec_current");
			break;
		case "activePower":// 查询有功功率
			param.setSaveTable("t_read_elec_power_active");
			break;
		case "reactivePower":// 查询无功功率
			param.setSaveTable("t_read_elec_power_reactive");
			break;
		case "powerFactor":// 查询功率因数
			param.setSaveTable("t_read_elec_power_factor");
			break;
		case "elec":// 查询组合有功电能
			param.setSaveTable("t_read_elec_use_total_active");
			break;
		case "water":// 查询水流量
			param.setSaveTable("t_read_water_flow");
			break;
		default:
			return null;
		}
		param.setMeterAddr(meterAddr);
		param.setFreezeTd(freezeTd);
		param.setLoopType(Integer.parseInt(loopType));
		return readRecordMapper.selectByAddrAndTd(param);
	}

	/**
	 * paas回调的数据处理
	 */
	@Override
	@Transactional
	public void saveRead(String jsonStr) {
		// 接收到的msg转为map格式
		JSONObject jsonMap = JSONObject.parseObject(jsonStr);
		// 仪表地址
		String meterAddr =  jsonMap.getString("equipNumber");
		// 采集失败的msg
		if (StringUtils.isEmpty(meterAddr)) {
			return;
		}
		
		// 采集时间搓
		String readTimeStr = String.valueOf(jsonMap.get("lastSaveTime"));
		if (StringUtils.isEmpty(readTimeStr)) {
			return;
		}
		
		/**
		 * 异常事件 插入
		 */
		String dataType = String.valueOf(jsonMap.get("dataType"));
		if(!StringUtils.isEmpty(dataType) && !dataType.equals("221")){
			logger.debug(">>>>>>>>>>>>>>>> ExceptionsRecords inser start >>>>>>>>>>>>>>>");
			exceptionsRecordsService.insertExceptionsRecords(jsonMap);
			return;
		}
		
		Date readTime = new Date(Long.valueOf(readTimeStr));
		// 回路数
		String loopStr;
		if(null==jsonMap.get("loopType")){
			loopStr = "0";
		}
		else {
			loopStr = (String)jsonMap.get("loopType");
		}
		// msg中有价值的数据
		Map<String, String> dataMap = (Map<String, String>) jsonMap.get("data");

		if (null != dataMap && dataMap.size() > 0) {
			// 排序
			List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(dataMap.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
				// 升序排序
				public int compare(Entry<String, String> o1, Entry<String, String> o2) {
					return o1.getKey().compareTo(o2.getKey());
				}

			});
			// 默认倍率
			BigDecimal pt = new BigDecimal("1");
			BigDecimal ct = new BigDecimal("1");
			String meterCateg = "";
			String meterType = "";
			// 查询仪表
			Meter meter = meterMapper.selectByMeterAddr(meterAddr);
			String orgId = "";
			boolean haveMeter = false;
			// 是否需要扣费计算
			boolean needDeduct = false;
			if (null != meter) {
				haveMeter = true;
				orgId = meter.getOrgId();
				pt = new BigDecimal(meter.getElecPt());
				ct = new BigDecimal(meter.getElecCt());
				meterCateg = meter.getEnergyType();
				meterType = meter.getMeterType();
			}
			// 最近读数
			List<ReadLast> readList = new ArrayList<ReadLast>();
			// 历史记录
			Map<String,ReadRecord> recordMap = new HashMap<String,ReadRecord>();

			String saveTable = "";
			ReadRecord tempRecord = new ReadRecord();
			// 是否需要保存tempRecord
			boolean needSave = false;
			for (Map.Entry<String, String> entry : list) {
				if (entry.getKey().equals("success") || entry.getKey().equals("code") || entry.getKey().equals("errMsg")) {
					continue;
				}
				// 止码数据需要扣费
				if (entry.getKey().equals("totalActiveE") || entry.getKey().equals("waterFlow")) {
					needDeduct = true;
				}
				String tempCode = entry.getKey();
				BigDecimal readValue = new BigDecimal(entry.getValue());

				InsDataItem item = InsCacheMap.itemSaveTable.get(tempCode);
				if (null != item) {
					if (item.getIsMultiplyCt() == 1) {
						readValue = readValue.multiply(ct);
					}
					if (item.getIsMultiplyPt() == 1) {
						readValue = readValue.multiply(pt);
					}
					// 需要保存
					if (!StringUtils.isEmpty(item.getSaveTable())) {
						// 与上次保存目标数据表不同
						if (!saveTable.equals(item.getSaveTable())) {
							// 上次保存目标数据表存在，暂存
							if (!StringUtils.isEmpty(saveTable)) {
								recordMap.put(saveTable, tempRecord);
								needSave = false;
							}
							// 本次保存目标数据表
							saveTable = item.getSaveTable();
							// 无暂存数据新增条目
							if (null==recordMap.get(saveTable)){
								tempRecord = new ReadRecord();
								needSave = true;
								tempRecord.setSaveTable(saveTable);
								tempRecord.setOrgId(orgId);
								tempRecord.setMeterAddr(meterAddr);
								tempRecord.setLoopType(Integer.valueOf(loopStr));
								tempRecord.setIsManual((byte) 0);
								tempRecord.setReadTimeStr(readTimeStr);
								tempRecord.setFreezeTd(DateStrUtils.dateToTd(readTime, DateStrUtils.minFormat));
							}
							// 有暂存数据取已有条目继续更新
							else {
								needSave = true;
								tempRecord = recordMap.get(saveTable);
							}
						}
						try {
							Method setMethod = ReadRecord.class.getMethod("setValue" + item.getSaveIndex(),
									BigDecimal.class);
							setMethod.invoke(tempRecord, readValue);
						} catch (Exception e) {
							logger.error(e.getMessage());
							continue;
						}
					}

				}
				// 最近读数
				ReadLast temp = new ReadLast();
				temp.setOrgId(orgId);
				temp.setMeterType(meterCateg);
				temp.setMeterAddr(meterAddr);
				temp.setElecMeterType(meterType);
				temp.setItemCode(tempCode);
				temp.setLoopType(Integer.valueOf(loopStr));
				temp.setReadValue(readValue);
				temp.setIsManual((byte) 0);
				temp.setReadTime(readTime);
				readList.add(temp);
			}
			// 最后一条未保存的历史记录
			if (needSave) {
				recordMap.put(tempRecord.getSaveTable(), tempRecord);
			}
			// 插入历史记录
			if (recordMap.size() > 0) {
				for (String key : recordMap.keySet()) {
					readRecordMapper.insert(recordMap.get(key));
				}
			}
			// 更新最近读数
			if (readList.size() > 0) {
				for (ReadLast readOne : readList) {
					// 如果是止码类数据，需与上次数据比较
					if (readOne.getItemCode().equals("totalActiveE") || readOne.getItemCode().equals("waterFlow")) {
						ReadLast last = readLastMapper.selectByAddrAndItem(readOne);
						/**
						 * 判断是否为第一次
						 */
						if(last == null){
							readLastMapper.replace(readOne);
						}else{
							// 上次数据较小时保存，否则不保存
							if (last.getReadValue().compareTo(readOne.getReadValue()) <= 0) {
								readLastMapper.replace(readOne);
							}else{
								logger.debug(">>>>>>>>>>>>>>>> 新数值 过小未保存  数值为："+readOne.getReadValue());
							}
						}
					} else {
						readLastMapper.replace(readOne);
					}
				}
			}
			// 使用中的系统预付费仪表计算
			if (needDeduct && haveMeter && loopStr.equals("0")) {
				if ((meter.getPriceModeCode().equals("1302") 
						||meter.getPriceModeCode().equals("1303")) 
						&& (meter.getSubAccountStatus() == 1 || meter.getSubAccountStatus() == 6)) {
					try{
						deduct(meter);
					}
					catch(Exception e){
						logger.error(e.getMessage());
					}
					
				}
			}
			// 表计预付费更新 ||entry.getKey().equals("meterOverdraft")
			// if ((entry.getKey().equals("meterMoney"))
			// &&haveMeter) {
			// CustMeterRelation relation = new CustMeterRelation();
			// relation.setMeterAddr(meterAddr);
			// relation =
			// custMeterRelationMapper.selectBindByMeterAddr(meterAddr);
			//
			// BigDecimal money = new BigDecimal(entry.getValue());
			// // 因表内余额与透支金额分开存储
			// // 所以当系统内金额小于等于0，即欠费，同时读取的表内余额亦等于0时，无法知晓表内透支金额
			// // 依然欠费，且不应更新系统内金额
			// if (null != relation &&
			// relation.getMeterMoney().compareTo(BigDecimal.ZERO) <= 0
			// && money.compareTo(BigDecimal.ZERO) == 0) {
			// // do nothing
			// } else {
			// if (relation != null) {
			// relation.setMeterMoney(money);
			// relation.setCloseTime(new Date());
			// custMeterRelationMapper.updateMoneyOnly(relation);
			// triggerAlarm(meterAddr, relation);
			// }
			// }
			// }

		}
	}

	/**
	 * 费用扣除
	 * 
	 * @param meter
	 */
	private void deduct(Meter meter) {
		// 先计算需扣除费用
		DeductOrder order = systemDeduct(meter);
		// 无需扣除费用
		if (null == order) {
			// nothing to do
			return;
		}
		
		//扣费是的单价和止码
		Map<String, String> map = new HashMap<String, String>();
		map.put("paice", order.getPrice());
		map.put("readValue", order.getReadValue().toString());
		String details = JsonUtils.objectToJson(map);
		
		// 有需扣除费用
		meter.setBalanceUpdateRead(order.getReadValue());
		meter.setBalanceUpdateTime(new Date());
		meter.setBalanceUpdateNo(order.getDeductNo());
		order.setIsPriceOver((byte) 0);
		order.setStatus((byte) 1);
		// 保存扣费订单
		deductOrderMapper.insert(order);

		Room room = roomMapper.selectByPrimaryKey(meter.getRoomId());
		// 实扣费用
		BigDecimal deductMoney = BigDecimal.ZERO;
		// 未扣电量(正数)
		BigDecimal deductUse = order.getUseValue();
		// 先扣除房间剩余补助
		if (!room.getElecAllowanceType().equals("2000") && meter.getEnergyType().equals("110000")
				&& room.getElecAllowance().compareTo(BigDecimal.ZERO) > 0) {
			// 实扣电量(负数)
			BigDecimal deductAllowance = order.getUseValue();
			// 未扣电量小于等于房间剩余补助
			if (deductUse.compareTo(room.getElecAllowance()) <= 0) {
				// 未扣电量归零
				deductAllowance = BigDecimal.ZERO.subtract(deductUse);
				deductUse = BigDecimal.ZERO;
			} else {
				// 房间剩余补助电量归零，未扣电量减少
				deductAllowance = BigDecimal.ZERO.subtract(room.getElecAllowance());
				deductUse = deductUse.subtract(room.getElecAllowance());
			}
			// 更新补助电量
			room.setElecAllowance(deductAllowance);
			roomMapper.updateForAllowance(room);
			// 查询更新后数据
			room = roomMapper.selectByPrimaryKey(room.getRoomId());
			AllowanceRecord record = AllowanceRecord.generateRecord(room, null, deductAllowance);
			record.setStatus((byte) 1);
			record.setExecuteTime(new Date());
			record.setBalance(room.getElecAllowance());
			allowanceRecordMapper.insert(record);
			
			/**
			 * 计划用量扣除是 插入仪表子账户流水
			 */
			SubAccountRecord record1 = SubAccountRecord.generateRecord(meter, room, null, null, deductMoney);
			record1.setRemark("系统计算扣费");
			record1.setOrderTypeCode("2");
			record1.setDetails(details);
			subAccountRecordMapper.insert(record1);
		}
		// 无需计算费用
		if (deductUse.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		} else {
			// 仅限单一计价时
			// 最终扣费 = 应扣费用*(未扣电量/总电量)
			order.setDeductMoney(order.getDeductMoney().multiply(deductUse).divide(order.getUseValue()));
		}
		deductMoney = BigDecimal.ZERO.subtract(order.getDeductMoney());
		// 扣费金额
		meter.setBalance(deductMoney);
		// 扣除
		meterMapper.updateForBalance(meter);
		// 查询最新余额
		meter = meterMapper.selectByPrimaryKey(meter.getMeterId());
		SubAccountRecord record1 = SubAccountRecord.generateRecord(meter, room, null, null, deductMoney);
		record1.setRemark("系统计算扣费");
		record1.setOrderTypeCode("2");
		record1.setDetails(details);
		subAccountRecordMapper.insert(record1);
		// 余额转移到房间账户
		// 无需转移
		if (meter.getBalance().compareTo(BigDecimal.ZERO) == 0) {
			// nothing to do
		}
		// 仪表有余额需转移、且房间有余额可转移
		// 子账户余额小于0
		else if (meter.getBalance().compareTo(BigDecimal.ZERO) < 0
				&& room.getBalance().compareTo(BigDecimal.ZERO) > 0) {
			// 房间账户转出金额（负数）
			BigDecimal transferOutMoney = BigDecimal.ZERO;
			// 转入仪表子账户金额（正数）
			BigDecimal transferInMoney = BigDecimal.ZERO;
			// 子账户欠费余额大于房间账户余额
			if ((BigDecimal.ZERO.subtract(meter.getBalance())).compareTo(room.getBalance()) > 0) {
				transferOutMoney = BigDecimal.ZERO.subtract(room.getBalance());
				transferInMoney = room.getBalance();
			}
			// 子账户欠费余额小于等于房间账户余额
			else if ((BigDecimal.ZERO.subtract(meter.getBalance())).compareTo(room.getBalance()) <= 0) {
				transferOutMoney = meter.getBalance();
				transferInMoney = BigDecimal.ZERO.subtract(meter.getBalance());
			}
			// 查询房间账户
			// 转账
			room.setBalance(transferOutMoney);
			roomMapper.updateForBalance(room);
			room = roomMapper.selectByPrimaryKey(room.getRoomId());
			RoomAccountRecord record2 = RoomAccountRecord.generateRecord(room, null, null, transferOutMoney);
			record2.setRemark("仪表子账户扣除");
			record2.setOrderTypeCode("2");// 2、子账户扣除
			record2.setDetails(details);
			roomAccountRecordMapper.insert(record2);
			// 子账户
			meter.setBalance(transferInMoney);
			meterMapper.updateForBalance(meter);
			meter = meterMapper.selectByPrimaryKey(meter.getMeterId());
			SubAccountRecord record3 = SubAccountRecord.generateRecord(meter, room, null, null, transferInMoney);
			record3.setRemark("房间账户转移");
			record3.setOrderTypeCode("1");
			record3.setDetails(details);
			subAccountRecordMapper.insert(record3);
		}
		// 最终余额小于0，断开
		if (meter.getBalance().add(room.getBalance()).compareTo(BigDecimal.ZERO) < 0
				&& meter.getPriceModeCode().equals("1302") ) {
			Map<String, String> requestMap = new HashMap<String, String>();
			requestMap.put("equipNumber", meter.getMeterAddr());
			cmdService.generateCmd(ApiTemplateCons.switchOff, meter.getMeterAddr(), requestMap);
		}else{
			try {
				triggerAlarm(meter,room);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void triggerAlarm(Meter meter, Room room) {
		
		// 有补助，并且补助未使用完的
		if(!room.getElecAllowanceType().equals("2000") &&
				room.getElecAllowance().compareTo(BigDecimal.ZERO) > 0){
			return ;
		}
		// 获取预警信息
		RuleAlarm ruleAlarm = ruleAlarmMapper.selectByPrimaryKey(meter.getAlarmId());
		if(ruleAlarm == null){
			return ;
		}
		BigDecimal alertMoney1 = ruleAlarm.getAlarm1Value();
		BigDecimal alertMoney2 = ruleAlarm.getAlarm2Value();
		BigDecimal totalMoney = meter.getBalance().add(room.getBalance());
		//余额在预警1 区间
		if(totalMoney.compareTo(alertMoney1) < 0  &&
				totalMoney.compareTo(alertMoney2) > 0 
				){
			String key1 = SysCommons.AlarmCache.alarmHeader1+room.getRoomId();
			// 判断是否已经发送
			String meterId = String.valueOf(redisService.get(key1));
			// 如果当前仪表已经发送了预警
			if(StringUtils.isNotBlank(meterId) &&meterId.equals(meter.getMeterAddr()) ){
				// 不做任何处理，当前预警已发送
			}else{
				/**
				 *  预警1 执行断电一分钟
				 */
				if(SysCommons.AlarmCache.CLOSE_METER_ALARM.equals(ruleAlarm.getAlarm1Type())){
					//暂时只对电表通断做预警
					if(meter.getEnergyType().equals("110000")){
						closeAndDelayOpen(meter);
						redisService.set(key1, meter.getMeterAddr(),6*60*60);
					}
				}else if(SysCommons.AlarmCache.SMS_ALARM.equals(ruleAlarm.getAlarm1Type())){
					sendSMStoCustomer(room,meter,totalMoney);
					redisService.set(key1, meter.getMeterAddr(),6*60*60);
				}
			}
		}
		//余额在预警2 区间
		if(totalMoney.compareTo(alertMoney2) < 0  &&
				totalMoney.compareTo(BigDecimal.ZERO) > 0 
				){
			String key2 = SysCommons.AlarmCache.alarmHeader2+room.getRoomId();
			// 判断是否已经发送
			String meterId = String.valueOf(redisService.get(key2));
			// 如果当前仪表已经发送了预警
			if(StringUtils.isNotBlank(meterId) &&meterId.equals(meter.getMeterAddr()) ){
				// 不做任何处理，当前预警已发送
			}else{
				/**
				 *  预警1 执行断电一分钟
				 */
				if(SysCommons.AlarmCache.CLOSE_METER_ALARM.equals(ruleAlarm.getAlarm2Type())){
					//暂时只对电表通断做预警
					if(meter.getEnergyType().equals("110000")){
						closeAndDelayOpen(meter);
						redisService.set(key2, meter.getMeterAddr(),6*60*60);
					}
				}else if(SysCommons.AlarmCache.SMS_ALARM.equals(ruleAlarm.getAlarm2Type())){
					sendSMStoCustomer(room,meter,totalMoney);
					redisService.set(key2, meter.getMeterAddr(),6*60*60);
				}
			}
		}
	}
	
	
	
	/**
	 *  预警发送通知
	 * @param room
	 * @param meter
	 */
	private void sendSMStoCustomer(Room room, Meter meter,BigDecimal totalMoney) {
		CustomerRoom queryRoom = new CustomerRoom();
		String phone = "";
		queryRoom.setRoomId(room.getRoomId());
		queryRoom.setStatus(new Byte("1")); // 正常使用中的
		List<CustomerRoom> custRooms = customerRoomMapper.selectByEntity(queryRoom);
		if(custRooms.isEmpty() ){
			logger.debug("################房间{}内不包含用户", room.getRoomName());
			return ;
		}
		CustomerRoom customerRoom = custRooms.get(0);
		
		Customer customer = customerMapper.selectByPrimaryKey(customerRoom.getCustomerId());
		if(StringUtils.isEmpty(customer.getCustomerMobile1())){
			logger.debug("################房间{},用户{}没有设置手机号码", room.getRoomName(),customer.getCustomerRealname());
			return ;
		}
		phone = customer.getCustomerMobile1();
		
		// 验证当前消息推送是否在禁止发送时间段内
		if(!validateSmsSend(room.getOrgId(),SysCommons.AlarmCache.templateNo)){
			logger.debug("################房间{},用户{}根据配置取消发送预警", room.getRoomName(),customer.getCustomerRealname());
			return;
		}
		//塞入队列发短信
		String orgId = room.getOrgId();
		JSONObject contentJson = new JSONObject();
		/**
		 *  模版
		 *  尊敬的${name}您好，房间：${roomname}，类型$ {type} 到 ${time}，剩余金额：${change}元，请尽快充值，防止影响您的使用！
		 */
		String customerName = customer.getCustomerRealname();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
		if(StringUtils.isBlank(customerName)){
			customerName = customer.getCustomerMobile1();
		}
		contentJson.put("name", customerName);
		contentJson.put("roomname", room.getRoomName());
		contentJson.put("type", "水电费");
		contentJson.put("time", sdf.format(new Date()));
		contentJson.put("change",totalMoney.toString());
		String contentStr = Notify.createAliSms(phone, SysCommons.AlarmCache.templateNo, 
				contentJson.toJSONString(), SysCommons.AlarmCache.signature).
				setPaasBusData(NotifyBusTypeEm.termNoContactError, null, null, null, orgId, NotifyLvlEm.HIGH)
		.toJsonStr();
		mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, contentStr);
	}

	/**
	 *  验证当前时间根据配置是否能发送短信
	 * @param orgId  机构ID
	 * @param templateno 模版NO
	 * @return
	 */
	private boolean validateSmsSend(String orgId, String templateno) {
		JSONArray configJson = null;
		
		OrgSmsConfig config = new OrgSmsConfig();
		config.setStatus(new Byte("0")); // 启动的配置
		config.setOrgId(orgId);
		config.setTemplateNo(templateno);
		
		List<OrgSmsConfig> valiDatas = orgSmsConfigMapper.selectByEntity(config);
		if(valiDatas.isEmpty()){
			return true;
		}
		SimpleDateFormat currDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat compoartTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date currDateTime = new Date();
		String currDate = currDateFormat.format(currDateTime);
		String startTime = null;
		String endTime = null;
		Date startDate = null;
		Date endDate = null;
		Calendar date = Calendar.getInstance();
		date.setTime(currDateTime);
		for (OrgSmsConfig orgSmsConfig : valiDatas) {
			if(StringUtils.isEmpty(orgSmsConfig.getConfigJson())){
				continue;
			}
			try {
				configJson = JSONArray.parseArray(orgSmsConfig.getConfigJson());
				for(int i = 0,len =configJson.size();i<len;i++ ){
					JSONObject currConfig = configJson.getJSONObject(i);
					startTime = currDate+" "+currConfig.getString("start");
					String isSpanning = currConfig.getString("isSpanning");
					// 判断是否垮天
					if("0".equals(isSpanning)){
						date.add(Calendar.DAY_OF_MONTH, 1); // 往前加一天
						currDate = currDateFormat.format(date.getTime());
					}
					endTime = currDate+" "+ currConfig.getString("end");
					// 判断当前时间是否在不允许发送的时间
					startDate = compoartTimeFormat.parse(startTime);
					endDate   = compoartTimeFormat.parse(endTime);
					
					if(currDateTime.compareTo(startDate) == 1 &&
							currDateTime.compareTo(endDate) == -1
							){
						return false;
					}
				}
			} catch (Exception e) {
				logger.debug("#######配置文件格式错误,id:({}),json:{},exception:{}#############",
						orgSmsConfig.getId(),
						orgSmsConfig.getConfigJson(),
						e);
			}
		}
		return true;
	}

	/**
	 *  断电后1分钟通电
	 * @param meter
	 */
	private void closeAndDelayOpen(Meter meter) {
		// 先断电
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("equipNumber", meter.getMeterAddr());
		
		cmdService.generateCmd(ApiTemplateCons.switchOff, meter.getMeterAddr(), requestMap);
		scheduler.schedule(new Runnable() {
			       public void run() { 
			    	   cmdService.generateCmd(ApiTemplateCons.switchOn, meter.getMeterAddr(), requestMap);
			       }
	  	}, 1, TimeUnit.MINUTES);
	}

	/**
	 * 判断余额是否预警或欠费
	 * 
	 * @param meterAddr
	 */
	// private void triggerAlarm(String meterAddr, CustMeterRelation relation) {
	//
	// String eventType = "";
	// String eventName = "";
	// byte eventLv = (byte) 0;
	// byte alarmLv = (byte) 0;
	// // 欠费
	// if (relation.getMeterMoney().compareTo(BigDecimal.ZERO) <= 0) {
	// eventType = "1604";
	// eventName = "仪表欠费";
	// eventLv = (byte) 3;
	// alarmLv = (byte) 3;
	// } else {
	// RuleAlarm rule = ruleAlarmMapper.selectByMeter(meterAddr);
	// if (null != rule && null != rule.getAlarm1Value()) {
	// // 二级预警
	// if (relation.getMeterMoney().compareTo(rule.getAlarm2Value()) <= 0) {
	// eventType = "1603";
	// eventName = "余额二级预警";
	// eventLv = (byte) 2;
	// alarmLv = (byte) 2;
	// }
	// // 一级预警
	// else if (relation.getMeterMoney().compareTo(rule.getAlarm1Value()) <= 0)
	// {
	// eventType = "1602";
	// eventName = "余额一级预警";
	// eventLv = (byte) 2;
	// alarmLv = (byte) 1;
	// }
	// }
	// }
	//
	// if (eventLv != 0) {
	// try {
	// // 取缓存中仪表报警状态
	// Object alarmStatus = cache.get(alarmSms, meterAddr);
	// // 状态不变时不再发送短信
	// if ((byte) alarmStatus == alarmLv) {
	// return;
	// }
	// } catch (Exception e) {
	//
	// }
	// cache.put(alarmSms, meterAddr, alarmLv);
	// Customer customer =
	// customerMapper.selectByCustomerNo(relation.getCustmeterCustNo());
	// SysEvent event = new SysEvent();
	// event.setId(CommUtils.getUuid());
	// event.setEventType(eventType);
	// event.setEventName(eventName);
	// event.setEventLv(eventLv);
	// event.setMeterAddr(relation.getMeterAddr());
	// event.setMeterAlias(relation.getMeterAlias());
	// event.setMeterCateg(relation.getMeterCateg());
	// event.setPositName(relation.getPositName());
	// event.setPositId(relation.getCustmeterPositionId());
	// event.setCustomerName(relation.getCustomerRealname());
	// event.setCustomerNo(relation.getCustmeterCustNo());
	// event.setCreateTime(new Date());
	// event.setStatus((byte) 0);
	// event.setDetail("仪表：" + relation.getMeterAlias() + eventName + ",房间余额：" +
	// relation.getMeterMoney());
	// sysEventMapper.insert(event);
	// String msgDetail = DateStrUtils.dateToTd(new Date(),
	// DateStrUtils.zhFormat) + "," + relation.getMeterAlias()
	// + "," + relation.getMeterMoney();
	// mqHandler.send(MqCons.QUEUE_NOTIFY_NAME,
	// NotifyUtils.sendSMSTemplate(customer.getCustomerMobile1(),
	// NotifyNum.MONEY_WARN.getValue(), msgDetail, NotifyLvlEm.LOW.getValue(),
	// event.getId()));
	// }
	// }
	/**
	 * 生成扣费订单 系统计费
	 * 
	 * @param subAcc
	 * @param readValue
	 * @return
	 */
	private DeductOrder systemDeduct(Meter meter) {
		DeductOrder param = new DeductOrder();
		param.setSubAccountId(meter.getSubAccountId());
		param.setMeterAddr(meter.getMeterAddr());
		DeductOrderParams orderParam = deductOrderMapper.selectOneParams(param);
		DeductOrder order = null;
		if (orderParam != null) {
			// orderParam.setReadValue(readValue);
			// orderParam.setDeductTime(new Date());
			// 无最近读数
			if (null == orderParam.getReadValue()) {
				return null;
			}
			EnergyPrice price = new EnergyPrice();
			price = energyPriceMapper.selectByPrimaryKey(orderParam.getPriceId());
			order = DeductOrderCalculator.generateOrder(orderParam, price);
		}
		return order;
	}

	@Override
	public ReadLast readMeterStatus(String meterAddr) {
		return readLastMapper.selectLastOne(meterAddr);
	}

	@Override
	public void replaceThreeControlMeterData(List<RoomMeterSwitchingManagent> list, String openOrClose) {
		for (RoomMeterSwitchingManagent rsm : list) {
			ReadLast temp = new ReadLast();
			temp.setOrgId(rsm.getOrgId());
			temp.setMeterType("110000");
			temp.setElecMeterType(rsm.getMeterType());
			temp.setMeterAddr(rsm.getMeterAddr());
			temp.setItemCode("isOff");
			temp.setReadValue(new BigDecimal(openOrClose));
			temp.setIsManual((byte) 1);
			temp.setReadTime(new Date());
			readLastMapper.replace(temp);
		}
	}
	/**
	 * 查询指定时间的仪表的真实数据的Service的实现类
	 */
	@Override
	public List<ReadRecord> selectByAddrAndTd1(String meterAddr,
			String freezeTd,String freezeTd1, String dataItem) {
		ReadRecord param = new ReadRecord();
		switch (dataItem) {
		case "voltage":// 查询电压
			param.setSaveTable("t_read_elec_voltage");
			break;
		case "current":// 查询电流
			param.setSaveTable("t_read_elec_current");
			break;
		case "activePower":// 查询有功功率
			param.setSaveTable("t_read_elec_power_active");
			break;
		case "reactivePower":// 查询无功功率
			param.setSaveTable("t_read_elec_power_reactive");
			break;
		case "powerFactor":// 查询功率因数
			param.setSaveTable("t_read_elec_power_factor");
			break;
		case "elec":// 查询组合有功电能
			param.setSaveTable("t_read_elec_use_total_active");
			break;
		case "water":// 查询水流量
			param.setSaveTable("t_read_water_flow");
			break;
		default:
			return null;
		}
		param.setMeterAddr(meterAddr);
		param.setFreezeTd(freezeTd);
		param.setFreezeTd1(freezeTd1);
		return readRecordMapper.selectByAddrAndTd1(param);
	}

	@Override
	public VReadLastElec selectReadLastElecView1(String meterAddr,String readTime) {
		VReadLastElec vLastElecs = elecMapper.selectElecAll1(meterAddr,readTime);
		return vLastElecs;
	}

	@Override
	public ReadLast selectByAddrAndItem(String meterAddr, String Item_code) {
		ReadLast param=new ReadLast();
		param.setMeterAddr(meterAddr);
		param.setItemCode(Item_code);
		ReadLast readLast=readLastMapper.selectByAddrAndItem(param);
		return readLast;
	}

	@Override
	public List<ReadRecord> selectDemandByMeterAddr(Map<String, Object> paramMap) {	
		return readRecordMapper.selectDemandByMeterAddr(paramMap);
	}


}
