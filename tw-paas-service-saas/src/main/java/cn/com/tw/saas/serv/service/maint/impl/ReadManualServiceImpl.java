package cn.com.tw.saas.serv.service.maint.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.DeductOrderCalculator;
import cn.com.tw.saas.serv.common.utils.cons.ApiTemplateCons;
import cn.com.tw.saas.serv.entity.business.command.PageCmdResult;
import cn.com.tw.saas.serv.entity.business.cust.DeductOrderParams;
import cn.com.tw.saas.serv.entity.db.cust.DeductOrder;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.entity.db.cust.SubAccountRecord;
import cn.com.tw.saas.serv.entity.db.read.ReadLast;
import cn.com.tw.saas.serv.entity.db.read.ReadManual;
import cn.com.tw.saas.serv.entity.db.rule.EnergyPrice;
import cn.com.tw.saas.serv.entity.room.AllowanceRecord;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.cust.DeductOrderMapper;
import cn.com.tw.saas.serv.mapper.cust.RoomAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.cust.SubAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.read.ReadLastMapper;
import cn.com.tw.saas.serv.mapper.read.ReadManualMapper;
import cn.com.tw.saas.serv.mapper.room.AllowanceRecordMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.rule.EnergyPriceMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.command.CmdRecordService;
import cn.com.tw.saas.serv.service.maint.ReadManualService;

@Service
public class ReadManualServiceImpl implements ReadManualService {

	@Autowired
	private ReadManualMapper readManualMapper;
	@Autowired
	private ReadLastMapper readLastMapper;
	@Autowired
	private MeterMapper meterMapper;
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
	private CmdRecordService cmdService;
	@Autowired
	private AllowanceRecordMapper allowanceRecordMapper;
	/**
	 * 查询人工抄表记录
	 */
	@Override
	public List<ReadManual> selectByPage(Page param) {
		return readManualMapper.selectByPage(param);
	}

	@Override
	public int insertList(List<ReadManual> recordList) {
		int i = 0;
		for (ReadManual temp : recordList) {
			i = i+insert(temp);			
		}
		return i;
	}

	@Override
	public int insertOne(ReadManual record) {
		return insert(record);
	}

	private int insert(ReadManual record) {
		// 查询对应仪表
		Meter meter = meterMapper.selectByMeterAddr(record.getMeterAddr());
		if (null == meter) {
			return 0;
//			record.setHaveMeter((byte) 0);
		} else {
			record.setHaveMeter((byte) 1);
			record.setOrgId(meter.getOrgId());
			// 最近读数
			ReadLast read = new ReadLast();
			read.setOrgId(meter.getOrgId());
			read.setMeterAddr(meter.getMeterAddr());
			// 电表
			if (meter.getEnergyType().equals("110000")) {
				record.setItemCode("totalActiveE");
				read.setItemCode("totalActiveE");
				read.setMeterType("110000");
			} 
			// 水表
			else if (meter.getEnergyType().equals("120000")) {
				record.setItemCode("waterFlow");
				read.setItemCode("waterFlow");
				read.setMeterType("120000");
			}
			ReadLast last = readLastMapper.selectByAddrAndItem(read);
			// 比较最近读数和人工录入数据，人工录入较大时更新最近读数
			if (last == null || last.getReadValue().compareTo(record.getReadValue())<0){
				read.setIsManual((byte) 1);
				read.setReadValue(record.getReadValue());
				// 人工只录入总回路数据
				read.setLoopType(0);
				read.setReadTime(record.getReadTime());
				readLastMapper.replace(read);
				// 使用中的系统预付费仪表计算费用
				if ((meter.getPriceModeCode().equals("1302") 
						||meter.getPriceModeCode().equals("1303")) 
						&& meter.getSubAccountStatus() == 1) {
					deduct(meter);
				}
			}
			// 更新重复日期的人工读数
			List<ReadManual> records = readManualMapper.selectByEntity(record);
			if(records.size()>0){
				ReadManual temp = records.get(0);
				if (temp.getReadValue().compareTo(record.getReadValue())<0){
					temp.setReadValue(record.getReadValue());
					temp.setReadStaff(record.getReadStaff());
					temp.setReadStaffId(record.getReadStaffId());
					readManualMapper.updateByPrimaryKeySelective(temp);
				}else{
					return 0;
				}
			}
			else {
				readManualMapper.insert(record);
			}
			return 1;
		}
	}
	
	@Override
	public List<ReadManual> selectAppReadManual(String regionId) {
		return readManualMapper.selectAppReadManual(regionId);
	}
	
	/**
	 * 费用扣除
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
		// 有需扣除费用
		meter.setBalanceUpdateRead(order.getReadValue());
		meter.setBalanceUpdateTime(new Date());
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
		if (!room.getElecAllowance().equals("2000") && meter.getEnergyType().equals("110000")
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
			AllowanceRecord record = new AllowanceRecord();
			record = record.generateRecord(room, null, deductAllowance);
			record.setStatus((byte) 1);
			record.setExecuteTime(new Date());
			record.setBalance(room.getElecAllowance());
			allowanceRecordMapper.insert(record);
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
			roomAccountRecordMapper.insert(record2);
			// 子账户
			meter.setBalance(transferInMoney);
			meterMapper.updateForBalance(meter);
			meter = meterMapper.selectByPrimaryKey(meter.getMeterId());
			SubAccountRecord record3 = SubAccountRecord.generateRecord(meter, room, null, null, transferInMoney);
			record3.setRemark("房间账户转移");
			record3.setOrderTypeCode("1");
			subAccountRecordMapper.insert(record3);
		}
		// 最终余额小于0，断开
		if (meter.getBalance().compareTo(BigDecimal.ZERO) < 0
				&& meter.getPriceModeCode().equals("1302") ) {
			Map<String, String> requestMap = new HashMap<String, String>();
			requestMap.put("equipNumber", meter.getMeterAddr());
			PageCmdResult result = cmdService.generateCmd(ApiTemplateCons.switchOff, meter.getMeterAddr(), requestMap);
		}
	}

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
}
