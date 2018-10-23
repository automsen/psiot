package cn.com.tw.saas.serv.service.room.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.DateStrUtils;
import cn.com.tw.saas.serv.common.utils.cons.EnergyCategEum;
import cn.com.tw.saas.serv.entity.db.cust.SubAccountRecord;
import cn.com.tw.saas.serv.entity.db.rule.EnergyAllowance;
import cn.com.tw.saas.serv.entity.room.AllowancePlan;
import cn.com.tw.saas.serv.entity.room.AllowanceRecord;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.cust.SubAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.room.AllowancePlanMapper;
import cn.com.tw.saas.serv.mapper.room.AllowanceRecordMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.rule.EnergyAllowanceMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.room.AllowanceService;

@Service
public class AllowanceServiceImpl implements AllowanceService {

	@Autowired
	private RoomMapper roomMapper;
	@Autowired
	private AllowanceRecordMapper allowanceRecordMapper;
	@Autowired
	private AllowancePlanMapper allowancePlanMapper;
	@Autowired
	private EnergyAllowanceMapper allowanceMapper;
	@Autowired
	private MeterMapper meterMapper;
	@Autowired
	private SubAccountRecordMapper subAccountRecordMapper;

	/**
	 * 保存发放计划
	 * 
	 * @param plan
	 */
	@Override
	@Transactional
	public int savePlan(AllowancePlan plan) {
		// 指定月份
		String monthStr = DateStrUtils.dateToTd(plan.getExecuteTime(),DateStrUtils.monthFormat);
		plan.setMonth(monthStr);
		int result = 0;
		// 查询是否有当月的记录
		AllowancePlan planParam = new AllowancePlan();
		planParam.setOrgId(plan.getOrgId());
		planParam.setMonth(plan.getMonth());
		List<AllowancePlan> plans = allowancePlanMapper.selectByEntity(planParam);
		if (plans != null && plans.size() > 0) {
			// 本月已保存其它发放计划
			return -1;
		}
		// 只查询使用中的宿舍
		Room param = new Room();
		param.setOrgId(plan.getOrgId());
		param.setRoomUse("1720");
		param.setAccountStatus((byte) 1);
		List<Room> rooms = roomMapper.selectByEntity(param);
		plan.setStatus((byte) 0);
		// 计划批次号
		String batchNo = CommUtils.getUuid();
		plan.setId(CommUtils.getUuid());
		plan.setBatchNo(batchNo);
		// 缓存补助规则
		Map<String, EnergyAllowance> ruleMap = new HashMap<String, EnergyAllowance>();
		// 每个房间计算补助量
		List<AllowanceRecord> records = new ArrayList<AllowanceRecord>();
		for (Room room : rooms) {
			if (room.getElecAllowanceType().equals("2000")) {
				continue;
			}
			// 电费补助
			String ruleId = room.getElecAllowanceRuleId();
			// 补助规则未缓存
			if (ruleMap.get(ruleId) == null) {
				EnergyAllowance newRule = allowanceMapper.selectByPrimaryKey(ruleId);
				if (newRule != null) {
					ruleMap.put(ruleId, newRule);
				} else {
					continue;
				}
			}
			EnergyAllowance tempRule = ruleMap.get(ruleId);
			// 补助量
			BigDecimal volume = BigDecimal.ZERO;
			// 定额
			if (tempRule.getAllowanceType().equals("2001")) {
				volume = tempRule.getAllowanceQuota();
			}
			// 按人头
			else if (tempRule.getAllowanceType().equals("2002")) {
				volume = tempRule.getAllowanceQuota().multiply(new BigDecimal(room.getPeopleNumber()));
			}
			// 按比例发放
			volume = volume.multiply(new BigDecimal(plan.getPercent())).divide(new BigDecimal(100));
			if (volume.compareTo(BigDecimal.ZERO) > 0) {
				AllowanceRecord record = new AllowanceRecord();
				record = record.generateRecord(room, null, volume);
				record.setStatus((byte) 0);
				record.setOrderId(plan.getBatchNo());
				record.setExecuteTime(plan.getExecuteTime());
				record.setEnergyType("110000");
				result += allowanceRecordMapper.insert(record);
				records.add(record);
			}
		}
		plan.setRoomAmount(result);
		if(result>0){
			// 保存发放计划
			allowancePlanMapper.insert(plan);
			// 执行时间小于当前时间直接执行
			if(plan.getExecuteTime().before(new Date())){
				executePlan(records);
				plan.setStatus((byte) 1);
				allowancePlanMapper.updateByPrimaryKeySelective(plan);
			}
		}
		return result;
	}
	
	/**
	 * 执行预设
	 * @return
	 */
	@Override
	public int executePlan(){
		List<AllowanceRecord> records = allowanceRecordMapper.selectWaitExecuteToday();
		return executePlan(records);
	}
	
	/**
	 * 立即执行
	 * @param records
	 * @return
	 */
	private int executePlan(List<AllowanceRecord> records){
		int result = 0;
		for(AllowanceRecord record :records){
			// 查询房间
			Room room = new Room();
			room = roomMapper.selectByPrimaryKey(record.getRoomId());
			//计划用量
			BigDecimal volume = record.getVolume();
			
			room.setElecAllowance(volume);
			// 更新补助电量
			roomMapper.updateForAllowance(room);
			// 查询更新后数据
			room = roomMapper.selectByPrimaryKey(record.getRoomId());
			// 更新补助记录
			record.setBalance(room.getElecAllowance());
			record.setStatus((byte) 1);
			allowanceRecordMapper.updateByPrimaryKeySelective(record);
			
			/**
			 * 查出房间对应电表的余额  
			 */
			List<Meter> meters = meterMapper.selectPriceByRoomId(record.getRoomId());
			Meter meter = null;
			for (Meter meter1 : meters) {
				if(EnergyCategEum.ELEC.getValue().equals(meter1.getEnergyType())){
					meter = meter1;
				}
			}
			
			if(meter != null){
				//判断仪表余额是否小于零
				if(meter.getBalance().compareTo(BigDecimal.ZERO) == -1){
					//扣除的余额
					BigDecimal dBalance = meter.getBalance().setScale(2, BigDecimal.ROUND_HALF_UP);
					//扣除的计划用量
					BigDecimal dAllowance = meter.getBalance().divide(meter.getPrice1(),2, BigDecimal.ROUND_HALF_EVEN);
					//计划补助金额 
					BigDecimal moneyAllowance = volume.multiply(meter.getPrice1()).setScale(2, BigDecimal.ROUND_HALF_UP);
					//计划用电量扣除后的余额
					BigDecimal balance = moneyAllowance.add(meter.getBalance()).setScale(2, BigDecimal.ROUND_HALF_UP);
					/**
					 * 扣除后的余额小于0 则计划用量扣除完了 计划用量设为0
					 * 扣除后的余额大于0则计划用量为 扣除后的余额除以电价
					 */
					if(balance.compareTo(BigDecimal.ZERO) == -1){
						meter.setBalance(balance);
						volume = BigDecimal.ZERO;
					}else if(balance.compareTo(BigDecimal.ZERO) == 1){
						meter.setBalance(BigDecimal.ZERO);
						volume = balance.divide(meter.getPrice1(),2, BigDecimal.ROUND_HALF_EVEN);
					}else{
						meter.setBalance(BigDecimal.ZERO);
						volume = BigDecimal.ZERO;
					}
					meterMapper.updateByPrimaryKeySelective(meter);
					
					room.setElecAllowance(volume);
					// 更新补助电量
					roomMapper.updateByPrimaryKeySelective(room);
					// 查询更新后数据
					room = roomMapper.selectByPrimaryKey(record.getRoomId());
					// 更新补助记录
					record.setBalance(dBalance);
					record.setStatus((byte) 1);
					record.setVolume(dAllowance);
					record.setId(CommUtils.getUuid());
					allowanceRecordMapper.insert(record);
					
					/**
					 * 计划用量扣除 写入子账户流水9.27
					 */
					SubAccountRecord record1 = SubAccountRecord.generateRecord(meter, room, null, null, meter.getBalance());
					record1.setRemark("系统计算扣费");
					record1.setOrderTypeCode("2");
					subAccountRecordMapper.insert(record1);
				}
			}
			
		}
		return result;
	}

	@Override
	public List<AllowancePlan> selectByPage(Page param) {
		return allowancePlanMapper.selectByPage(param);
	}

	@Override
	public List<AllowanceRecord> selectByRoomId(String roomId) {
		return allowanceRecordMapper.selectByRoomId(roomId);
	}
	
	/**
	 * 取消发放计划
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public int cancelPlan(String id){
		// 查询预设计划
		AllowancePlan plan= allowancePlanMapper.selectByPrimaryKey(id);
		// 计划不存在
		if (null==plan){
			return 0;
		}
		// 查询未执行单条记录
		List<AllowanceRecord> records = allowanceRecordMapper.selectByOrderId(plan.getBatchNo());
		// 计划与单条预设不符合
		if (plan.getRoomAmount()!=records.size()){
			return 0;
		}
		int result = allowanceRecordMapper.deleteByOrderId(plan.getBatchNo());
		allowancePlanMapper.deleteByPrimaryKey(id);
		return result;
	}
}
