package cn.com.tw.saas.serv.service.audit.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.ApiTemplateCons;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.audit.Clearing;
import cn.com.tw.saas.serv.entity.business.command.PageCmdResult;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.db.cust.OrderRecharge;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.entity.order.OrderClearing;
import cn.com.tw.saas.serv.entity.org.Org;
import cn.com.tw.saas.serv.entity.org.OrgAccountRecord;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.AllowancePlan;
import cn.com.tw.saas.serv.entity.room.AllowanceRecord;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.room.RoomHis;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.audit.ClearingMapper;
import cn.com.tw.saas.serv.mapper.cust.CustomerMapper;
import cn.com.tw.saas.serv.mapper.cust.CustomerRoomMapper;
import cn.com.tw.saas.serv.mapper.cust.RoomAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.order.OrderClearingMapper;
import cn.com.tw.saas.serv.mapper.org.OrgAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.org.OrgMapper;
import cn.com.tw.saas.serv.mapper.org.OrgUserMapper;
import cn.com.tw.saas.serv.mapper.room.AllowancePlanMapper;
import cn.com.tw.saas.serv.mapper.room.AllowanceRecordMapper;
import cn.com.tw.saas.serv.mapper.room.RoomHisMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterHisMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.audit.ClearingService;
import cn.com.tw.saas.serv.service.command.CmdRecordService;

@Service
public class ClearingServiceImpl implements ClearingService {

	@Autowired
	private ClearingMapper clearingMapper;
	@Autowired
	private RoomMapper roomMapper;
	@Autowired
	private MeterMapper meterMapper;
	@Autowired
	private OrgAccountRecordMapper orgAccountRecordMapper;
	@Autowired
	private OrgMapper orgMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private CustomerRoomMapper customerRoomMapper;
	@Autowired
	private RoomHisMapper roomHisMapper;
	@Autowired
	private MeterHisMapper meterHisMapper;
	@Autowired
	private CmdRecordService cmdService;
	@Autowired
	private RoomAccountRecordMapper roomAccountRecordMapper;
	@Autowired
	private OrderClearingMapper orderClearingMapper;
	@Autowired
	private OrgUserMapper orgUserMapper;
	@Autowired
	private AllowanceRecordMapper allowanceRecordMapper;
	@Autowired
	private AllowancePlanMapper allowancePlanMapper;
	

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Clearing arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(Clearing arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Clearing selectById(String id) {
		return clearingMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Clearing> selectByPage(Page page) {

		return clearingMapper.selectByPage(page);
	}

	@Override
	public int update(Clearing arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(Clearing arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void roomClearingAdd(List<Clearing> clearings) {

		/**
		 * 结算版本
		 */
		// 提交人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		String clearingVersion = CommUtils.getUuid();
		for (Clearing clearing : clearings) {
			/**
			 * 1.根据表号查出结算的仪表信息
			 */
			Meter meter = meterMapper.selectByMeterAddr(clearing.getMeterAddr());
			/**
			 * 2.根据房间id查出结算的房间信息
			 */
			Room room = roomMapper.selectByPrimaryKey(clearing.getRoomId());
			/**
			 * 3.判断房间余额与仪表余额的和是否大于等于0
			 */
			int res = BigDecimal.ZERO.compareTo(meter.getBalance().add(room.getBalance()));
			// res==1前面的值比较大 及为房间余额与仪表余额的和小于0
			if (res == 1) {
				throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR, "余额不足请先充值");
			}
			/**
			 * 4.根据仪表编号和审核状态判断 是否拥有待审核的记录
			 */
			Clearing clearing1 = new Clearing();
			clearing1.setClearingState("0");
			clearing1.setMeterAddr(meter.getMeterAddr());
			List<Clearing> list = clearingMapper.selectByEntity(clearing1);
			if (list != null && list.size() > 0) {
				clearing1.setClearingState("3");
				clearing1.setId(list.get(0).getId());
				clearing1.setMeterAddr(null);
				clearingMapper.updateByPrimaryKeySelective(clearing1);
			}

			/**
			 * 5.将房间和仪表相应的信息 插入结算表 结算状态给初始状态0
			 */
			Clearing clearing2 = new Clearing();
			clearing2.setRoomId(room.getRoomId());
			clearing2.setRoomName(room.getRoomName());
			clearing2.setRoomNumber(room.getRoomNumber());
			clearing2.setRegionFullName(room.getRegionFullName());
			clearing2.setAccountId(room.getAccountId());
			clearing2.setAccountStatus(room.getAccountStatus());
			clearing2.setMeterAddr(meter.getMeterAddr());
			clearing2.setMeterType(meter.getMeterType());
			clearing2.setEnergyType(meter.getEnergyType());
			clearing2.setClearingVersion(clearingVersion);
			clearing2.setClearingState("0");
			clearing2.setOrgId(clearing.getOrgId());
			if (null != user) {
				clearing2.setSubmitId(user.getUserId());
				clearing2.setSubmitName(user.getUserName());
				clearing2.setAuditName("");
			}
			clearingMapper.insert(clearing2);
		}
	}

	/**
	 * 结算版本
	 */

	@Override
	@Transactional
	public void roomClearingAdd(Clearing clearing) {
		// 提交人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user.setOrgId((String) info.getExt("orgId"));
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		/**
		 * 2.根据房间id查出结算的房间信息
		 */
		Room room = roomMapper.selectByPrimaryKey(clearing.getRoomId());
		//修改 结算房间的 房间账户状态 为审核中
		room.setAccountStatus((byte)6);
		roomMapper.updateByPrimaryKeySelective(room);
		
		Meter param = new Meter();
		param.setOrgId(user.getOrgId());
		param.setRoomId(clearing.getRoomId());
		List<Meter> meters = meterMapper.selectByEntity(param);
		//根据提交审核将提交申请插入到房间信息之中
		
		String clearingVersion = CommUtils.getUuid();
		if(meters==null)
			throw new BusinessException(ResultCode.UNKNOW_ERROR,"系统异常");
		if(meters!=null && meters.size()>0){
			for (Meter meter : meters) {
				/**
				 * 3.判断房间余额与仪表余额的和是否大于等于0
				 */
				int res = BigDecimal.ZERO.compareTo(meter.getBalance().add(room.getBalance()));
				// res==1前面的值比较大 及为房间余额与仪表余额的和小于0
				if (res == 1) {
					throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR, "余额不足请先充值");
				}
				/**
				 * 4.根据仪表编号和审核状态判断 是否拥有待审核的记录
				 */
				/*Clearing clearing1 = new Clearing();
				clearing1.setClearingState("0");
				clearing1.setMeterAddr(meter.getMeterAddr());
				List<Clearing> list = clearingMapper.selectByEntity(clearing1);
				if (list != null && list.size() > 0) {
					clearing1.setClearingState("3");
					clearing1.setId(list.get(0).getId());
					clearing1.setMeterAddr(null);
					clearingMapper.updateByPrimaryKeySelective(clearing1);
				}*/
				/**
				 * 5.将房间和仪表相应的信息 插入结算表 结算状态给初始状态0
				 */
				Clearing clearing2 = new Clearing();
				clearing2.setRoomId(room.getRoomId());
				clearing2.setRoomName(room.getRoomName());
				clearing2.setRoomNumber(room.getRoomNumber());
				clearing2.setAccountId(room.getAccountId());
				clearing2.setAccountStatus(room.getAccountStatus());
				clearing2.setRegionFullName(room.getRegionFullName());
				clearing2.setRoomUse(room.getRoomUse());
				clearing2.setMeterAddr(meter.getMeterAddr());
				clearing2.setMeterType(meter.getMeterType());
				clearing2.setEnergyType(meter.getEnergyType());
				clearing2.setClearingVersion(clearingVersion);
				clearing2.setClearingState("0");
				clearing2.setOrgId(user.getOrgId());
				clearing2.setPayeeName(clearing.getPayeeName());
				clearing2.setPayeePhone(clearing.getPayeePhone());
				if (null != user) {
					clearing2.setSubmitId(user.getUserId());
					clearing2.setSubmitName(user.getUserName());
					clearing2.setAuditName("");
				}
				clearingMapper.insert(clearing2);
				
				
				
				//将仪表账户状态改为 审核中
				meter.setSubAccountStatus((byte)2);
				meterMapper.updateByPrimaryKeySelective(meter);
			}
		}
			/**
			 * 3.判断房间余额是否大于等于0
			 */
			int res = BigDecimal.ZERO.compareTo(room.getBalance());
			// res==1前面的值比较大 及为房间余额小于0
			if (res == 1) {
				throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR, "余额不足请先充值");
			}
			/**
			 * 4.根据房间编号和审核状态判断 是否拥有待审核的记录
			 */
			/*Clearing clearing1 = new Clearing();
			clearing1.setClearingState("0");
			clearing1.setRoomId(room.getRoomId());
			List<Clearing> list = clearingMapper.selectByEntity(clearing1);
			if (list != null && list.size() > 0) {
				for (Clearing clearing3 : list) {
					if(StringUtils.isEmpty(clearing3.getMeterAddr())){
						clearing3.setClearingState("3");
						clearingMapper.updateByPrimaryKeySelective(clearing3);
					}
				}
			}*/
			/**
			 * 5.将房间相应的信息 插入结算表 结算状态给初始状态0
			 */
			Clearing clearing2 = new Clearing();
			clearing2.setRoomId(room.getRoomId());
			clearing2.setRoomName(room.getRoomName());
			clearing2.setRoomNumber(room.getRoomNumber());
			clearing2.setAccountId(room.getAccountId());
			clearing2.setAccountStatus(room.getAccountStatus());
			clearing2.setRegionFullName(room.getRegionFullName());
			clearing2.setRoomUse(room.getRoomUse());
			clearing2.setClearingVersion(clearingVersion);
			clearing2.setClearingState("0");
			clearing2.setOrgId(user.getOrgId());
			clearing2.setPayeeName(clearing.getPayeeName());
			clearing2.setPayeePhone(clearing.getPayeePhone());
			if (null != user) {
				clearing2.setSubmitId(user.getUserId());
				clearing2.setSubmitName(user.getUserName());
				clearing2.setAuditName("");
			}
			clearingMapper.insert(clearing2);
		}
	

	@Override
	public List<Clearing> selectByEntity(Clearing clearing) {
		return clearingMapper.selectByEntity(clearing);
	}

	@Override
	public List<Clearing> selectClearingAudit(Clearing clearing) {
		return clearingMapper.selectClearingAudit(clearing);
	}

	
	
	/**
	 * 房间 结算驳回
	 */
	@Override
	@Transactional
	public void rejectClearing(List<Clearing> clearings) {
		// 审核人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		
		List<Meter> meters1 = new ArrayList<Meter>();
		Room room1 = new Room();
		String clearingVersion = "";//结算审核版本
		for (Clearing clearing : clearings) {
			if(clearing.getMeterAddr()!=null){
				Meter meter = new Meter();
				meter.setMeterAddr(clearing.getMeterAddr());
				meter.setOrgId(clearing.getOrgId());
				meters1.add(meter);
			}else{
				room1.setRoomId(clearing.getRoomId());
				room1.setOrgId(clearing.getOrgId());
				clearingVersion = clearing.getClearingVersion();
			}
		}
		
		
		for (Meter meter : meters1) {
			/**
			 * 将驳回的仪表账户状态改为 使用中
			 */
			meter.setSubAccountStatus((byte)1);
			meterMapper.updateToAlarmOrPrice(meter);
			
			Clearing clearing1 = new Clearing();
			clearing1.setClearingState("0");
			clearing1.setClearingVersion(clearingVersion);
			clearing1.setMeterAddr(meter.getMeterAddr());
			List<Clearing> list = clearingMapper.selectByEntity(clearing1);
			
			clearing1.setClearingState("2");
			clearing1.setId(list.get(0).getId());
			clearing1.setAuditId(user.getUserId());
			clearing1.setAuditName(user.getUserName());
			clearing1.setClearingVersion(clearingVersion);
			clearing1.setMeterAddr(null);
			clearingMapper.updateByPrimaryKeySelective(clearing1);
		}
		
		
		/**
		 * 将驳回的房间账户状态 改为使用中
		 */
		room1.setAccountStatus((byte)1);
		roomMapper.updateByPrimaryKeySelective(room1);
		
		//房间没有表的情况下
		Clearing clearing2 = new Clearing();
		clearing2.setClearingState("0");
		clearing2.setClearingVersion(clearingVersion);
		clearing2.setRoomId(room1.getRoomId());
		List<Clearing> list = clearingMapper.selectByEntity(clearing2);
		
		clearing2.setClearingState("2");
		clearing2.setId(list.get(0).getId());
		clearing2.setRoomId(room1.getRoomId());
		clearing2.setAuditId(user.getUserId());
		clearing2.setClearingVersion(clearingVersion);
		clearing2.setAuditName(user.getUserName());
		clearingMapper.updateByPrimaryKeySelective(clearing2);
		
		

	}

	@Override
	@Transactional
	public void passClearing(List<Clearing> clearings) {
		List<Meter> meters1 = new ArrayList<Meter>();
		Room room1 = new Room();
		for (Clearing clearing : clearings) {
			if(clearing.getMeterAddr()!=null){
				Meter meter = new Meter();
				meter.setMeterAddr(clearing.getMeterAddr());
				meter.setOrgId(clearing.getOrgId());
				meters1.add(meter);
			}else{
				room1.setRoomId(clearing.getRoomId());
				room1.setOrgId(clearing.getOrgId());
			}
		}
		// 审核人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = orgUserMapper.selectByPrimaryKey(info.getSubject());
		}
		

		String orgId = "";
		String roomId = "";
		String roomUse = "";
		String accountId = CommUtils.getUuid();//房间账户ID  宿舍账户重新赋值用
		String subAccountId = CommUtils.getUuid();//仪表子账户ID  宿舍账户重新赋值用
		
		BigDecimal clearingMoney = BigDecimal.ZERO;// 计算金额
		BigDecimal roomBalance = BigDecimal.ZERO;// 房间余额
		BigDecimal meterBalance = BigDecimal.ZERO;// 房间余额
		
		/**
		 * 如果房间不为空，先去结算仪表的流程，再去结算房间的流程，否则直接去结算房间的流程
		 */
		if(meters1!=null){
			for (Meter meter2 : meters1) {
				//1计算出仪表的金额
				orgId = meter2.getOrgId();
				/**
				 * 1.计算出对应的 结算金额
				 */
				Clearing clearing1 = new Clearing();
				clearing1.setClearingState("0");
				clearing1.setMeterAddr(meter2.getMeterAddr());
				List<Clearing> list = clearingMapper.selectClearingAudit(clearing1);

				/**
				 * 2.跟新结算记录
				 */
				if(list != null && list.size() > 0){
					for (Clearing clearing2 : list) {
						roomId = clearing2.getRoomId();
						roomUse = clearing2.getRoomUse();
						meterBalance = clearing2.getMeterBalance();
						roomBalance = clearing2.getRoomBalance();
						// 每块表与房间的结算金额
						clearingMoney = roomBalance.add(meterBalance);
						clearing2.setClearingMoney(clearingMoney);
						clearing2.setClearingState("1");
						clearing2.setAuditId(user.getUserId());
						clearing2.setAuditName(user.getUserRealName());
						clearingMapper.updateByPrimaryKeySelective(clearing2);
					}
				}
				// 生成仪表历史记录 一.将账户状态改为4空置状态 ,清空余额 存入历史表 二.做meter表的跟新
				Meter meter = meterMapper.selectByMeterAddr(meter2.getMeterAddr());
				meterBalance = meter.getBalance();//计算通过时的余额

				meter.setSubAccountStatus((byte) 5);//以历史状态存入 历史表
				meter.setBalance(BigDecimal.ZERO);
				meterHisMapper.insert(meter);

				meter.setAlarmStatus((byte) 0);
				/**
				 * 商铺房间帐号ID和子账户ID清空  宿舍则给新的账户ID
				 */
				if("1720".equals(roomUse)){
					meter.setAccountId(accountId);
					meter.setSubAccountId(subAccountId);
				}
				if("1710".equals(roomUse)){
					meter.setAccountId("");
					meter.setSubAccountId("");
				}
				meter.setSubAccountStatus((byte) 4);//以空置状态 重新存入
				meter.setStartTime(null);
				meter.setStartRead(null);
				meter.setBalanceUpdateTime(null);
				meter.setBalanceUpdateNo(null);
				meter.setBalanceUpdateRead(null);
				meterMapper.clearingUpdate(meter);

			}

			// 结算后的仪表断开
			for (Meter clearing : meters1) {
				Map<String, String> requestMap = new HashMap<String, String>();
				requestMap.put("equipNumber", clearing.getMeterAddr());
				PageCmdResult result = cmdService.generateCmd(ApiTemplateCons.switchOff, clearing.getMeterAddr(),
						requestMap);
			}
		}
		
		// 房间结算金额
		clearingMoney = BigDecimal.ZERO.subtract(roomBalance.add(meterBalance));
		
		
		//计算出房间对应的金额,根据房间id查询出房间的对应金额
		Clearing clearing1 = new Clearing();
		clearing1.setRoomId(room1.getRoomId());
		List<Clearing> clearings2 = clearingMapper.selectByRoomId(clearing1);
		clearing1 = clearings2.get(0);
		 
		 
		/**
		 * 2.跟新结算记录
		 */
		if(clearing1!=null){
				roomId = clearing1.getRoomId();
				roomUse = clearing1.getRoomUse();
				// 房间的结算金额
				clearing1.setClearingMoney(clearingMoney);
				clearing1.setClearingState("1");
				clearing1.setAuditId(user.getUserId());
				clearing1.setAuditName(user.getUserRealName());
				clearingMapper.updateByPrimaryKeySelective(clearing1);
				/**
				 * 3.生成机构账户记录
				 */
				// 更新机构余额
				Org org = orgMapper.selectByPrimaryKey(clearing1.getOrgId());
				if(org!=null){
					if (org.getBalance() != null) {
						org.setBalance(org.getBalance().add(clearingMoney));
					} else {
						org.setBalance(clearingMoney);
					}
				}
				// 房间信息 查询
				Room room = roomMapper.selectByPrimaryKey(roomId);
				CustomerRoom customerRoom = new CustomerRoom();
				customerRoom.setRoomAccountId(room.getAccountId());
				List<CustomerRoom> customerRooms = customerRoomMapper.selectByEntity(customerRoom);
				// 客户信息查询
				CustomerRoom customerRoom1 = new CustomerRoom();
				customerRoom1.setRoomId(room.getRoomId());
				customerRoom1.setStatus((byte)1);
				List<Customer> customers = customerMapper.selectByRoom(customerRoom1);
				Customer customer = new Customer();
				if(customers != null && customers.size() > 0 ){
					customer = customers.get(0);
					/**
					 * 修改客户状态 为停用状态
					 */
					CustomerRoom custRoom = new CustomerRoom();
					custRoom.setCustomerId(customer.getCustomerId());
					List<CustomerRoom> custRooms = customerRoomMapper.selectRoomCustByCustomerId(custRoom);
					//客户只关联一个房间的 情况下可以修改客户状态
					if(custRooms.size() <= 1){
						customer.setCustomerStatus((byte)1);
					}else{
						customer.setCustomerStatus((byte)0);
					}
					customerMapper.updateByPrimaryKeySelective(customer);
				}
				

				orgMapper.updateByPrimaryKeySelective(org);
				OrgAccountRecord accountRecord = OrgAccountRecord.createOrgAccountRecord(room, customer, null, clearingMoney,
						org.getBalance());
				accountRecord.setOrderTypeCode("2");
				orgAccountRecordMapper.insert(accountRecord);
				
				/**
				 * 4.插入房间历史表 把余额设置为0 状态改为 取消关联
				 */
				room.setAccountStatus((byte) 5);//历史状态
				room.setBalance(BigDecimal.ZERO);
				roomHisMapper.insert(room);

				/**
				 * 5.客户与房间账户解除绑定 房间账户状态改为4空置状态
				 */
				room.setAccountStatus((byte) 4);
				
				/**
				 * 商铺房间帐号ID和子账户ID清空  宿舍则给新的账户ID
				 */
				if("1710".equals(roomUse)){
					room.setAccountId("");
				}
				if("1720".equals(roomUse)){
					room.setAccountId(accountId);
				}
				room.setStartTime(null);
				room.setEndTime(null);
				room.setElecAllowanceType("2000");
				room.setElecAllowanceRuleId("");
				room.setElecAllowance(BigDecimal.ZERO);
				room.setWaterAllowance(BigDecimal.ZERO);
				room.setWaterAllowanceRuleId("");
				room.setWaterAllowanceType("2000");
				roomMapper.cleanRoomAccount(room);
				
				
				//向结算账单里插入数据
				OrderClearing orderClearing = OrderClearing.generateOrder(room, customer, user, clearingMoney,null);
				orderClearing.setOrgName(org.getOrgName());
				orderClearingMapper.insert(orderClearing);
				//向房间账户流水表里 插入记录
				RoomAccountRecord record = RoomAccountRecord.generateRecord(room, customer, user, clearingMoney);
				record.setRemark("房间结算");
				record.setOrderTypeCode("4");//销户结算
				record.setOrderId(orderClearing.getOrderId());
				record.setPayModeCode(orderClearing.getClearingModeCode());
				record.setOperatorType(orderClearing.getOperatorType());
				record.setOperatorName(user.getUserRealName());
				roomAccountRecordMapper.insert(record);
				
				/**
				 * 清除房间的 计划用量预设
				 */
				List<AllowanceRecord> allowanceRecords = allowanceRecordMapper.selectByRoomIdAndStatus(roomId);
				for (AllowanceRecord allowanceRecord : allowanceRecords) {
					allowanceRecordMapper.deleteByPrimaryKey(allowanceRecord.getId());
				}
				/**
				 * 没有执行的 计划用量数量减1
				 */
				AllowancePlan allowancePlan = new AllowancePlan();
				allowancePlan.setStatus((byte)0);
				List<AllowancePlan> plans = allowancePlanMapper.selectByEntity(allowancePlan);
				for (AllowancePlan allowancePlan2 : plans) {
					int roomAmount = allowancePlan2.getRoomAmount()- 1;
					/**
					 * 房间数量小于等于0则删除
					 */
					if(roomAmount <= 0){
						allowancePlanMapper.deleteByPrimaryKey(allowancePlan2.getId());
					}else{
						allowancePlan2.setRoomAmount(allowancePlan2.getRoomAmount() - 1);
						allowancePlanMapper.updateRoomAmount(allowancePlan2);
					}
				}
				

				for (CustomerRoom customerRoom2 : customerRooms) {
					customerRoom2.setStatus((byte) 2);
					customerRoomMapper.updateByPrimaryKeySelective(customerRoom2);
				}
		}
	}
		

		

	@Override
	public Map<String, Object> selectClearing(Clearing clearing) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<Clearing> clearing2 = clearingMapper.selectClearingAudit(clearing);
		String roomId = clearing.getRoomId();
		// 查询房间
		Room room = roomMapper.selectByPrimaryKey(roomId);
		result.put("room", room);
		// 查询关联仪表
		Meter meterParam = new Meter();
		meterParam.setRoomId(roomId);
		List<Meter> meters = meterMapper.selectByEntity(meterParam);
		if(meters!=null){
			result.put("meterList", meters);
		}else{
			result.put("meterList", 0);
		}
		Customer customer = new Customer();
		customer.setCustomerMobile1(clearing2.get(0).getCustomerMobile1());
		customer.setCustomerNo(clearing2.get(0).getCustomerNo());
		customer.setCustomerRealname(clearing2.get(0).getCustomerRealname());
		result.put("customer", customer);
		
		
		Clearing clearing3 = new Clearing();
		BigDecimal meterBalance = BigDecimal.ZERO;
		clearing3.setRoomBalance(room.getBalance());
		if(meters!=null){
		for (Meter meter : meters) {
			if("110000".equals(meter.getEnergyType())){
				meterBalance.add(meter.getBalance());
			}
			if("120000".equals(meter.getEnergyType())){
				meterBalance.add(meter.getBalance());
			}
			clearing3.setClearingMoney(meterBalance.add(room.getBalance()));
		}
		}else{
			clearing3.setClearingMoney(room.getBalance());
		}
		
		clearing3.setPayeeName(clearing2.get(0).getPayeeName());
		clearing3.setPayeePhone(clearing2.get(0).getPayeePhone());
		result.put("clearing", clearing3);
		
		return result;
	}

	@Override
	public Map<String, Object> selectHisClearing(Clearing clearing) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<Clearing> clearing2 = clearingMapper.selectClearingAudit(clearing);
		String roomId = clearing.getRoomId();
		// 查询房间
		RoomHis roomHis = new RoomHis();
		roomHis.setRoomId(roomId);
		roomHis.setAccountId(clearing2.get(0).getAccountId());
		List<RoomHis> rooms = roomHisMapper.selectByEntity(roomHis);
		result.put("room", rooms.get(0));
		// 查询关联仪表
		Meter meterParam = new Meter();
		meterParam.setRoomId(roomId);
		List<Meter> meters = meterHisMapper.selectByEntity(meterParam);
		result.put("meterList", meters);
		Customer customer = new Customer();
		customer.setCustomerMobile1(clearing2.get(0).getCustomerMobile1());
		customer.setCustomerNo(clearing2.get(0).getCustomerNo());
		customer.setCustomerRealname(clearing2.get(0).getCustomerRealname());
		result.put("customer", customer);
		
		
		Clearing clearing3 = new Clearing();
		BigDecimal meterBalance = BigDecimal.ZERO;
		clearing3.setRoomBalance(rooms.get(0).getBalance());
		for (Meter meter : meters) {
			if("110000".equals(meter.getEnergyType())){
				meterBalance.add(meter.getBalance());
			}
			if("120000".equals(meter.getEnergyType())){
				meterBalance.add(meter.getBalance());
			}
		}
		if("0".equals(clearing2.get(0).getClearingState())){
			clearing3.setClearingMoney(meterBalance.add(rooms.get(0).getBalance()));
		}else{			
			//这里取房间结算记录，记录为负数取其绝对值
			clearing3.setClearingMoney(clearing2.get(0).getClearingMoney().abs());
		}
		

		clearing3.setPayeeName(clearing2.get(0).getPayeeName());
		clearing3.setPayeePhone(clearing2.get(0).getPayeePhone());
		result.put("clearing", clearing3);
		
		return result;
	}

	@Override
	public List<Clearing> clearingExpert(Clearing clearing) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<Clearing> clearings=clearingMapper.clearingExport(clearing);
		for (Clearing clearing2 : clearings) {
			String dateString = formatter.format(clearing2.getCreateTime());
			clearing2.setCreateTimeStr(dateString);
			
			if("1710".equals(clearing2.getRoomUse())){
				if("0".equals(clearing2.getClearingState())){
					clearing2.setClearingState("待审核");
					clearing2.setRoomUse("商铺");
				}else if("1".equals(clearing2.getClearingState())){
					clearing2.setClearingState("通过");
					clearing2.setRoomUse("商铺");
				}else if("2".equals(clearing2.getClearingState())){
					clearing2.setClearingState("退回");
					clearing2.setRoomUse("商铺");
				}
			}else if("1720".equals(clearing2.getRoomUse())){
				if("0".equals(clearing2.getClearingState())){
					clearing2.setClearingState("待审核");
					clearing2.setRoomUse("宿舍");
				}else if("1".equals(clearing2.getClearingState())){
					clearing2.setClearingState("通过");
					clearing2.setRoomUse("宿舍");
				}else if("2".equals(clearing2.getClearingState())){
					clearing2.setClearingState("退回");
					clearing2.setRoomUse("宿舍");
				}
			}
		}
		return clearings;
	}

	@Override
	public Map<String, Object> count(Map<String, Object> param) {
		// 
		return clearingMapper.count(param);
	}

}
