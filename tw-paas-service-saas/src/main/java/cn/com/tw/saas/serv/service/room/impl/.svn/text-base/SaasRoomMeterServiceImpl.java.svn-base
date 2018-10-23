package cn.com.tw.saas.serv.service.room.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.DeductOrderCalculator;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.business.archives.RoomMeterChangeParam;
import cn.com.tw.saas.serv.entity.business.cust.DeductOrderParams;
import cn.com.tw.saas.serv.entity.db.cust.DeductOrder;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.entity.db.cust.SubAccountRecord;
import cn.com.tw.saas.serv.entity.db.read.ReadLast;
import cn.com.tw.saas.serv.entity.db.rule.EnergyPrice;
import cn.com.tw.saas.serv.entity.excel.RoomMeterExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.room.SaasRoomMeter;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.cust.DeductOrderMapper;
import cn.com.tw.saas.serv.mapper.cust.RoomAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.cust.SubAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.read.ReadLastMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.room.SaasRoomMeterMapper;
import cn.com.tw.saas.serv.mapper.rule.EnergyPriceMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterHisMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.room.SaasRoomMeterService;

@Service
public class SaasRoomMeterServiceImpl implements SaasRoomMeterService {

	@Autowired
	private SaasRoomMeterMapper saasRoomMeterMapper;
	@Autowired
	private MeterMapper meterMapper;
	@Autowired
	private MeterHisMapper meterHisMapper;
	@Autowired
	private RoomMapper roomMapper;
	@Autowired
	private ReadLastMapper readLastMapper;
	@Autowired
	private EnergyPriceMapper energyPriceMapper;
	@Autowired
	private DeductOrderMapper deductOrderMapper;
	@Autowired
	private SubAccountRecordMapper subAccountRecordMapper;
	@Autowired
	private RoomAccountRecordMapper roomAccountRecordMapper;

	@Override
	public int deleteById(String arg0) {
		return saasRoomMeterMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public SaasRoomMeter selectById(String arg0) {
		// TODO Auto-generated method stub
		return saasRoomMeterMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<SaasRoomMeter> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return saasRoomMeterMapper.selectByPage(arg0);
	}

	@Override
	public int updateSelect(SaasRoomMeter arg0) {
		// TODO Auto-generated method stub
		return saasRoomMeterMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	@Transactional
	public int appBind(SaasRoomMeter param, OrgUser user) {
		// 查询仪表信息
		Meter meter = new Meter();
		meter.setMeterAddr(param.getMeterAddr());
		List<Meter> meterList = meterMapper.selectByEntity(meter);
		if (null == meterList || meterList.size() <= 0) {
			throw new BusinessException(MonitResultCode.DATA_EXISTS_NULL, "当前仪表未注册");
		}
		// 按表号查询至多一条记录
		meter = meterList.get(0);
		if(meter.getSubAccountStatus() == 3){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "当前仪表已被废弃，不可使用");
		}
		if (!StringUtils.isEmpty(meter.getRoomId())) {
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "当前仪表已关联到其它房间");
		}
		if (param.getRoomId().equals(meter.getRoomId())) {
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该表已经与该房间绑定");
		}
		// 查房间信息
		Room room = roomMapper.selectByPrimaryKey(param.getRoomId());
		// 房间存在
		if (null != room && !StringUtils.isEmpty(room.getRoomId())) {
			// 绑定并处理账户相关逻辑
			return relate(meter, room);
		} else {
			return 0;
		}
		
	}

	// 仪表和房间绑定
	@Override
	@Transactional
	public String excelBind(RoomMeterExcel excelTemp, String regionId,String orgId) {
		String result1 = "";
		String result2 = "";
		String roomNumber, elecAddr = null, waterAddr = null;
		roomNumber = excelTemp.getRoomNumber();
		if (!StringUtils.isEmpty(excelTemp.getElecMeterAddr())) {
			elecAddr = excelTemp.getElecMeterAddr();
		}
		if (!StringUtils.isEmpty(excelTemp.getWaterMeterAddr())) {
			waterAddr = excelTemp.getWaterMeterAddr();
		}

		Room room = new Room();
		room.setOrgId(orgId);
		room.setRegionId(regionId);
		room.setRoomNumber(roomNumber);
		// 查询房间
		List<Room> roomList = roomMapper.selectByEntity(room);
		boolean roomFlag = false;
		// 房间是否存在
		if (roomList == null || roomList.size() <= 0) {
			return "无匹配房间";
		} else {
			room = roomList.get(0);
			roomFlag = true;
			/**
			 * 判断 房间下是否已有仪表存在
			 */
			Meter meter = new Meter();
			meter.setRoomId(room.getRoomId());
			List<Meter> meters = meterMapper.selectByEntity(meter);
			if (meters != null && meters.size() > 0) {
				for (Meter tempMeter : meters) {
					if (tempMeter.getMeterAddr().equals(elecAddr)) {
						return "重复关联仪表";
						// throw new
						// BusinessException(MonitResultCode.ALREADY_EXIST_ERROR,
						// "电表已存在该房间！");
					}
					if (tempMeter.getMeterAddr().equals(waterAddr)) {
						return "重复关联仪表";
						// throw new
						// BusinessException(MonitResultCode.ALREADY_EXIST_ERROR,
						// "水表已存在该房间！");
					}
				}
			}
		}

		// 有导入电表
		if (!StringUtils.isEmpty(elecAddr)) {
			Meter elecMeter = new Meter();
			elecMeter.setMeterAddr(elecAddr);
			List<Meter> meters = meterMapper.selectByEntity(elecMeter);
			if (meters == null || meters.size() == 0) {
				result1 = "无匹配电表";
			} else {
				elecMeter = meters.get(0);
				
				if(elecMeter.getSubAccountStatus() == 3){
					return "该表已废弃";
				}
				// 仪表已绑定任意房间
				if (!StringUtils.isEmpty(elecMeter.getRoomId())) {
					result1 = "电表已关联其它房间";
					// 仪表未绑定房间
				} else {
					// 房间存在
					if (roomFlag) {
						relate(elecMeter, room);
						result1 = "ok";
					}
				}
			}
		} else {
			result1 = "ok";
		}

		// 有导入水表
		if (!StringUtils.isEmpty(waterAddr)) {
			Meter waterMeter = new Meter();
			waterMeter.setMeterAddr(waterAddr);
			List<Meter> meters = meterMapper.selectByEntity(waterMeter);
			if (meters == null || meters.size() == 0) {
				result2 = "无匹配水表";
			} else {
				waterMeter = meters.get(0);
				
				if(waterMeter.getSubAccountStatus() == 3){
					return "该表已废弃";
				}
				// 仪表已绑定任意房间
				if (!StringUtils.isEmpty(waterMeter.getRoomId())) {
					result2 = "水表已关联其它房间";
					// 仪表未绑定房间
				} else {
					// 房间存在
					if (roomFlag) {
						relate(waterMeter, room);
						result2 = "ok";
					}
				}
			}
		} else {
			result2 = "ok";
		}
		if (result1.equals("ok") && result2.equals("ok")) {
			return "ok";
		} else if (result1.equals("ok")) {
			return result2;
		} else if (result2.equals("ok")) {
			return result1;
		} else {
			return result1 + "," + result2;
		}
	}

	/**
	 * 仪表和房间绑定
	 * 
	 * @param meter
	 * @param room
	 */
	private int relate(Meter meter, Room room) {
		// 绑定房间
		Meter param = new Meter();
		param.setMeterId(meter.getMeterId());
		param.setRoomId(room.getRoomId());
		// 暂时用房间名作为仪表名称
		if(!StringUtils.isEmpty(room.getRoomName())){
			param.setMeterName(room.getRoomName());
		}
		// 房间账户已激活
		if (!StringUtils.isEmpty(room.getAccountId())){
			// 预激活仪表子账户
			param.setAccountId(room.getAccountId());
			param.setSubAccountId(meter.generateSubAccountId());
			// 商铺
			if (room.getRoomUse().equals("1710")){
				param.setSubAccountStatus((byte) 0);
			}
			// 宿舍
			else if (room.getRoomUse().equals("1720")){
				param.setSubAccountStatus((byte) 0);
//				param.setSubAccountStatus((byte) 1);
				// 最近读数
//				ReadLast read = new ReadLast();
//				read.setOrgId(meter.getOrgId());
//				read.setMeterAddr(meter.getMeterAddr());
//				// 电表
//				if (meter.getEnergyType().equals("110000")) {
//					read.setItemCode("totalActiveE");
//					read.setMeterType("110000");
//				} 
//				// 水表
//				else if (meter.getEnergyType().equals("120000")) {
//					read.setItemCode("waterFlow");
//					read.setMeterType("120000");
//				}
//				ReadLast last = readLastMapper.selectByAddrAndItem(read);
//				if (null!=last){
//					param.setStartRead(last.getReadValue());
//					param.setStartTime(last.getReadTime());
//					param.setBalanceUpdateRead(last.getReadValue());
//					param.setBalanceUpdateTime(last.getReadTime());
//				}
//				else {
//					param.setStartRead(meter.getInstallRead());
//					param.setStartTime(new Date());
//					param.setBalanceUpdateRead(meter.getInstallRead());
//					param.setBalanceUpdateTime(new Date());
//				}
			} 
			
		}
		return meterMapper.updateByPrimaryKeySelective(param);
	}

	/**
	 * 取消关联
	 * 
	 * @param param
	 * @return
	 */
	@Override
	@Transactional
	public int cancel(RoomMeterChangeParam param) {
		int result = 0;
		// 查询仪表信息
		Meter oldMeter = new Meter();
		oldMeter.setOrgId(param.getOrgId());
		oldMeter.setEnergyType(param.getEnergyType());
		oldMeter.setMeterAddr(param.getOldMeterAddr());
		oldMeter.setRoomId(param.getRoomId());
		List<Meter> meterList = meterMapper.selectByEntity(oldMeter);
		if (null == meterList || meterList.size() <= 0) {
			throw new BusinessException(MonitResultCode.DATA_EXISTS_NULL, "无对应仪表");
		}
		// 按表号查询至多一条记录
		oldMeter = meterList.get(0);
		if (!oldMeter.getRoomId().equals(param.getRoomId())) {
			throw new BusinessException(MonitResultCode.DATA_CONSISTENCY_ERROR, "数据一致性异常");
		}
		// 仪表子账户已激活
		if (!StringUtils.isEmpty(oldMeter.getSubAccountId())){
			oldMeter = cancel(oldMeter,param.getOldMeterRead());
		}
		// 取消关联的仪表另存为历史数据
		oldMeter.setSubAccountStatus((byte) 3);
		meterHisMapper.insert(oldMeter);
		// 仪表本身取消房间关联
		oldMeter.setRoomId("");
		oldMeter.setAccountId("");
		oldMeter.setSubAccountStatus((byte) 0);//空置中
		oldMeter.setSubAccountId("");
		oldMeter.setBalance(BigDecimal.ZERO);
		oldMeter.setPriceModeCode("1304");
		result = meterMapper.updateByPrimaryKeySelective(oldMeter);
		return result;
	}

	/**
	 * 取消仪表子账户
	 * 
	 * @param subAcc
	 * @param readValue
	 * @return
	 */
	private Meter cancel(Meter oldMeter,BigDecimal readValue) {
		// 系统计费
		if (oldMeter.getIsCostControl() == 0) {
			// 先计算需扣除费用
			DeductOrder order = systemDeduct(oldMeter, readValue);
			oldMeter.setBalanceUpdateRead(readValue);
			oldMeter.setBalanceUpdateTime(new Date());
			Room room = roomMapper.selectByPrimaryKey(oldMeter.getRoomId());
			BigDecimal deductMoney = BigDecimal.ZERO;
			// 有需扣除费用
			if (null != order) {
				order.setIsPriceOver((byte) 1);
				order.setStatus((byte) 1);
				deductMoney = BigDecimal.ZERO.subtract(order.getDeductMoney());
				// 保存扣费订单
				deductOrderMapper.insert(order);
				// 扣费金额
				oldMeter.setBalance(deductMoney);
				// 扣除
				meterMapper.updateForBalance(oldMeter);
				// 查询最新余额
				oldMeter = meterMapper.selectByPrimaryKey(oldMeter.getMeterId());
				SubAccountRecord record1 = SubAccountRecord.generateRecord(oldMeter, room, null, null, deductMoney);
				record1.setRemark("系统计算扣费");
				record1.setOrderTypeCode("2");
				subAccountRecordMapper.insert(record1);
			}
			// 无需扣除费用
			else if (null == order) {
				// nothing to do
			}
			// 余额转移到房间账户
			// 无需转移
			if (oldMeter.getBalance().compareTo(BigDecimal.ZERO) == 0) {
				// nothing to do
			} 
			// 有余额需转移
			else if (oldMeter.getBalance().compareTo(BigDecimal.ZERO) != 0) {
				// 转出金额
				BigDecimal transferOutMoney = BigDecimal.ZERO.subtract(oldMeter.getBalance());
				// 转入金额
				BigDecimal transferInMoney = oldMeter.getBalance();
				// 子账户归零
				oldMeter.setBalance(transferOutMoney);
				// 转出
				meterMapper.updateForBalance(oldMeter);
				// 最新余额
				oldMeter = meterMapper.selectByPrimaryKey(oldMeter.getMeterId());
				SubAccountRecord record2 = SubAccountRecord.generateRecord(oldMeter, room, null, null, transferOutMoney);
				record2.setRemark("取消关联结算");
				record2.setOrderTypeCode("3");
				subAccountRecordMapper.insert(record2);
				
				// 对房间账户转账
				room.setBalance(transferInMoney);
				// 转入
				roomMapper.updateForBalance(room);
				// 最新余额
				room = roomMapper.selectByPrimaryKey(room.getRoomId());
				RoomAccountRecord record3 = RoomAccountRecord.generateRecord(room, null, null,
						transferInMoney);
				record3.setRemark("仪表子账户结算");
				record3.setOrderTypeCode("3");
				roomAccountRecordMapper.insert(record3);
			}
		}
		// 仪表计费
		else if (oldMeter.getIsCostControl() == 1) {
			// TODO
		}

		return oldMeter;
	}

	/**
	 * 生成扣费订单 系统计费
	 * 
	 * @param subAcc
	 * @param readValue
	 * @return
	 */
	private DeductOrder systemDeduct(Meter meter, BigDecimal readValue) {
		DeductOrder param = new DeductOrder();
		param.setSubAccountId(meter.getSubAccountId());
		param.setMeterAddr(meter.getMeterAddr());
		DeductOrderParams orderParam = deductOrderMapper.selectOneParams(param);
		DeductOrder order = null;
		if(orderParam != null){
			orderParam.setReadValue(readValue);
			orderParam.setDeductTime(new Date());
			EnergyPrice price = new EnergyPrice();
			price = energyPriceMapper.selectByPrimaryKey(orderParam.getPriceId());
			order = DeductOrderCalculator.generateOrder(orderParam, price);
		}
		return order;
	}

	@Override
	@Transactional
	public int replace(RoomMeterChangeParam param) {
		int result = 0;
		// 查询旧表信息
		Meter oldMeter = new Meter();
		oldMeter.setOrgId(param.getOrgId());
		oldMeter.setEnergyType(param.getEnergyType());
		oldMeter.setMeterAddr(param.getOldMeterAddr());
		oldMeter.setRoomId(param.getRoomId());
		List<Meter> meterList1 = meterMapper.selectByEntity(oldMeter);
		if (null == meterList1 || meterList1.size() <= 0) {
			throw new BusinessException(MonitResultCode.DATA_EXISTS_NULL, "无对应仪表");
		}
		// 按表号查询至多一条记录
		oldMeter = meterList1.get(0);
		if (!oldMeter.getRoomId().equals(param.getRoomId())) {
			throw new BusinessException(MonitResultCode.DATA_CONSISTENCY_ERROR, "数据一致性异常");
		}
		// 查询新表信息
		Meter newMeter = new Meter();
		newMeter.setOrgId(param.getOrgId());
		newMeter.setEnergyType(param.getEnergyType());
		newMeter.setMeterAddr(param.getNewMeterAddr());
		List<Meter> meterList2 = meterMapper.selectByEntity(newMeter);
		if (null == meterList2 || meterList2.size() <= 0) {
			throw new BusinessException(MonitResultCode.DATA_EXISTS_NULL, "无对应仪表");
		}
		// 按表号查询至多一条记录
		newMeter = meterList2.get(0);
		if (!StringUtils.isEmpty(newMeter.getRoomId())) {
			throw new BusinessException(MonitResultCode.DATA_CONSISTENCY_ERROR, "该仪表绑定其它房间");
		}
		// 查询房间信息
		Room room = roomMapper.selectByPrimaryKey(param.getRoomId());
		// 新表继承数据
		newMeter.setRoomId(oldMeter.getRoomId());
		newMeter.setSubAccountStatus(oldMeter.getSubAccountStatus());
		newMeter.setPriceModeCode(oldMeter.getPriceModeCode());
		newMeter.setPriceId(oldMeter.getPriceId());
		newMeter.setAlarmId(oldMeter.getAlarmId());	
		// 新表初始化数据
		newMeter.setStartRead(param.getNewMeterRead());
		newMeter.setStartTime(new Date());
		// 仪表子账户已激活
		if (!StringUtils.isEmpty(oldMeter.getSubAccountId())){
			// 新表继承账户数据
			newMeter.setAccountId(oldMeter.getAccountId());
			newMeter.setSubAccountId(oldMeter.getSubAccountId());	
			newMeter.setBalanceUpdateRead(param.getNewMeterRead());
			newMeter.setBalanceUpdateTime(new Date());
			// 余额转账
			Meter[] meters = replace(oldMeter,newMeter, param.getOldMeterRead(), param.getNewMeterRead(), room);
			oldMeter = meters[0];
			newMeter = meters[1];
		}
		// 更新新表数据
		meterMapper.updateByPrimaryKeySelective(newMeter);
		// 取消关联的仪表另存为历史数据
		oldMeter.setSubAccountStatus((byte) 3);
		meterHisMapper.insert(oldMeter);
		// 仪表本身取消房间关联
		oldMeter.setRoomId("");
		oldMeter.setAccountId("");
		oldMeter.setSubAccountStatus((byte) 0);
		oldMeter.setSubAccountId("");
		oldMeter.setBalance(BigDecimal.ZERO);
		oldMeter.setPriceModeCode("1304");
		result = meterMapper.updateByPrimaryKeySelective(oldMeter);
		return result;
	}

	/**
	 * 跟换仪表子账户
	 * 
	 * @param oldSubAcc
	 * @param oldRead
	 * @return
	 */
	private Meter[] replace(Meter oldMeter, Meter newMeter, BigDecimal oldRead, BigDecimal newRead, Room room) {
		// 系统计费
		if (oldMeter.getIsCostControl() == 0) {
			// 先计算需扣除费用
			DeductOrder order = systemDeduct(oldMeter, oldRead);
			oldMeter.setBalanceUpdateRead(oldRead);
			oldMeter.setBalanceUpdateTime(new Date());
			BigDecimal deductMoney = BigDecimal.ZERO;
			// 有需扣除费用
			if (null != order) {
				order.setIsPriceOver((byte) 1);
				order.setStatus((byte) 1);
				deductMoney = BigDecimal.ZERO.subtract(order.getDeductMoney());
				// 保存扣费订单
				deductOrderMapper.insert(order);
				// 扣费金额
				oldMeter.setBalance(deductMoney);
				// 扣除
				meterMapper.updateForBalance(oldMeter);
				// 查询最新余额
				oldMeter = meterMapper.selectByPrimaryKey(oldMeter.getMeterId());
				SubAccountRecord record1 = SubAccountRecord.generateRecord(oldMeter, room, null, null, deductMoney);
				record1.setRemark("系统计算扣费");
				record1.setOrderTypeCode("2");
				subAccountRecordMapper.insert(record1);
			}
			// 无需扣除费用
			else if (null == order) {
				// nothing to do
			}
			// 余额转移到新仪表子账户
			// 无需转移
			if (oldMeter.getBalance().compareTo(BigDecimal.ZERO) == 0) {
				newMeter.setBalanceUpdateRead(newRead);
				newMeter.setBalanceUpdateTime(new Date());
				newMeter.setBalance(BigDecimal.ZERO);
			} 
			// 有余额需转移
			else if (oldMeter.getBalance().compareTo(BigDecimal.ZERO) != 0) {
				// 转出金额
				BigDecimal transferOutMoney = BigDecimal.ZERO.subtract(oldMeter.getBalance());
				// 转入金额
				BigDecimal transferInMoney = oldMeter.getBalance();
				// 子账户归零
				oldMeter.setBalance(transferOutMoney);
				// 转出
				meterMapper.updateForBalance(oldMeter);
				// 最新余额
				oldMeter = meterMapper.selectByPrimaryKey(oldMeter.getMeterId());
				SubAccountRecord record2 = SubAccountRecord.generateRecord(oldMeter, room, null, null,
						transferOutMoney);
				record2.setRemark("仪表更换结算");
				record2.setOrderTypeCode("3");
				subAccountRecordMapper.insert(record2);

				newMeter.setBalanceUpdateRead(newRead);
				newMeter.setBalanceUpdateTime(new Date());
				newMeter.setBalance(transferInMoney);
//				// 转入新仪表
//				meterMapper.updateForBalance(newMeter);
//				// 最新余额
//				newMeter = meterMapper.selectByPrimaryKey(newMeter.getMeterId());
				SubAccountRecord record3 = SubAccountRecord.generateRecord(newMeter, room, null, null,
						transferInMoney);
				record3.setRemark("仪表更换继承自旧仪表");
				record3.setOrderTypeCode("4");
				subAccountRecordMapper.insert(record3);
			}
		}
		// 仪表计费
		else if (oldMeter.getIsCostControl() == 1) {
			// TODO
		}
		Meter[] meters = new Meter[2];
		meters[0] = oldMeter;
		meters[1] = newMeter;
		return meters;
	}

}
