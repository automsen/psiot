package cn.com.tw.saas.serv.service.room.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.EnergyCategEum;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.audit.AuditAccount;
import cn.com.tw.saas.serv.entity.audit.AuditRoom;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.entity.db.rule.EnergyAllowance;
import cn.com.tw.saas.serv.entity.db.rule.EnergyPrice;
import cn.com.tw.saas.serv.entity.db.rule.RuleAlarm;
import cn.com.tw.saas.serv.entity.excel.RoomExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.AllowanceRecord;
import cn.com.tw.saas.serv.entity.room.ClearAllowanceRecord;
import cn.com.tw.saas.serv.entity.room.RoomHis;
import cn.com.tw.saas.serv.entity.room.SaasRegion;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.room.RoomAllowanceInfo;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.MeterHis;
import cn.com.tw.saas.serv.mapper.audit.AuditAccountMapper;
import cn.com.tw.saas.serv.mapper.audit.AuditRoomMapper;
import cn.com.tw.saas.serv.mapper.cust.CustomerMapper;
import cn.com.tw.saas.serv.mapper.cust.CustomerRoomMapper;
import cn.com.tw.saas.serv.mapper.cust.RoomAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.org.OrgUserMapper;
import cn.com.tw.saas.serv.mapper.room.AllowanceRecordMapper;
import cn.com.tw.saas.serv.mapper.room.ClearAllowanceRecordMapper;
import cn.com.tw.saas.serv.mapper.room.RoomHisMapper;
import cn.com.tw.saas.serv.mapper.room.SaasRegionMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.rule.EnergyAllowanceMapper;
import cn.com.tw.saas.serv.mapper.rule.EnergyPriceMapper;
import cn.com.tw.saas.serv.mapper.rule.RuleAlarmMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterHisMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.room.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomMapper roomMapper;
	@Autowired
	private SaasRegionMapper saasRegionMapper;
	@Autowired
	private CustomerRoomMapper customerRoomMapper;
	@Autowired
	private MeterMapper meterMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private AuditRoomMapper auditRoomMapper;
	@Autowired
	private AuditAccountMapper auditAccountMapper;
	@Autowired
	private RuleAlarmMapper ruleAlarmMapper;
	@Autowired
	private EnergyPriceMapper energyPriceMapper;
	@Autowired
	private EnergyAllowanceMapper allowanceMapper;
	@Autowired
	private OrgUserMapper orgUserMapper;
	
	@Autowired
	private MeterHisMapper meterHisMapper;
	@Autowired
	private RoomHisMapper roomHisMapper;
	
	@Autowired
	private AllowanceRecordMapper allowanceRecordMapper;
	@Autowired
	private RoomAccountRecordMapper roomAccountRecordMapper;
	@Autowired
	private ClearAllowanceRecordMapper clearAllowanceRecordMapper;
	
	
	@Override
	public void discardById(String id) {
		// TODO Auto-generated method stub
		/**
		 * 判断房间是否和客户绑定
		 */
		CustomerRoom customerRoom = new CustomerRoom();
		customerRoom.setRoomId(id);
		customerRoom.setStatus((byte)1); //正在使用的关系
		List<CustomerRoom> customerRooms = customerRoomMapper.selectByEntity(customerRoom);
		if (customerRooms != null && customerRooms.size() > 0) {
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "房间已与客户绑定");
		}
		/**
		 * 判断房间是否和仪表绑定
		 */
		Meter saasMeter = new Meter();
		saasMeter.setRoomId(id);
		List<Meter> saasMeters = meterMapper.selectByEntity(saasMeter);
		if (saasMeters != null && saasMeters.size() > 0) {
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "房间已与仪表绑定");
		}
		
		Room room = new Room();
		room.setRoomId(id);
		room.setAccountStatus((byte)3);
		roomMapper.updateByPrimaryKeySelective(room);
		
	}
	
	@Override
	public int deleteById(String id) {

		/**
		 * 判断房间是否和客户绑定
		 */
		CustomerRoom customerRoom = new CustomerRoom();
		customerRoom.setRoomId(id);
		customerRoom.setStatus((byte)1); //正在使用的关系
		List<CustomerRoom> customerRooms = customerRoomMapper.selectByEntity(customerRoom);
		if (customerRooms != null && customerRooms.size() > 0) {
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "房间已与客户绑定");
		}
		/**
		 * 判断房间是否和仪表绑定
		 */
		Meter saasMeter = new Meter();
		saasMeter.setRoomId(id);
		List<Meter> saasMeters = meterMapper.selectByEntity(saasMeter);
		if (saasMeters != null && saasMeters.size() > 0) {
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "房间已与仪表绑定");
		}
		
		/*RoomHis roomHis = new RoomHis();
		roomHis.setRoomId(id);
		List<RoomHis> his = roomHisMapper.selectByEntity(roomHis);
		if(his != null && his.size() > 0){
			Room room = new Room();
			room.setRoomId(id);
			room.setAccountStatus((byte)3);
			roomMapper.updateByPrimaryKeySelective(room);
		}else{
			
		}*/
		
		// TODO Auto-generated method stub
		return roomMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional
	public int insertSelect(Room room) {
		
		/**
		 *判断房间类型是否为空
		 */
		if(StringUtils.isEmpty(room.getRoomUse())){
			throw new BusinessException(MonitResultCode.PARAMETER_NULL_ERROR, "房间类型不能为空");
		}
		
		/**
		 * 房间名未填写 用房间号代替房间名
		 */
		if(StringUtils.isEmpty(room.getRoomName())){
			room.setRoomName(room.getRoomNumber());
		}
		//房间名称首尾去空
		room.setRoomName(room.getRoomName().trim());
		
		//入住人数没有则默认为4
		if(StringUtils.isEmpty(room.getPeopleLimit())){
			room.setPeopleLimit(4);
		}
		
		Room temp = new Room();
		temp.setOrgId(room.getOrgId());
		temp.setRegionId(room.getRegionId());
		temp.setRoomNumber(room.getRoomNumber());
		List<Room> tempList = roomMapper.selectIsNotAbandon(temp);
		if (tempList != null && tempList.size() > 0) {
			throw new BusinessException(MonitResultCode.PARAMETER_NULL_ERROR, "该楼栋已存在房间编号");
		}
		SaasRegion region = saasRegionMapper.selectByPrimaryKey(room.getRegionId());
		if (region.getIsDelete() == 1) {
			region.setIsDelete((byte) 0);
			saasRegionMapper.updateByPrimaryKeySelective(region);
		}
		room.setRegionFullName(region.getRegionFullName());
		room.setRegionNo(region.getRegionNo());
		// 商铺
		if ("1710".equals(room.getRoomUse())){
			//房间状态为运维维护中 即房间初始状态
			room.setAccountStatus((byte) 0);
			room.setAccountId("");
			room.setElecAllowanceType("2000");
			room.setWaterAllowanceType("2000");
			room.setElecAllowance(BigDecimal.ZERO);
			room.setWaterAllowance(BigDecimal.ZERO);
		}
		// 宿舍
		else if ("1720".equals(room.getRoomUse())){
			room = initDorm(room);
		} 
		room.setBalance(BigDecimal.ZERO);
		return roomMapper.insert(room);
	}

	@Override
	public Room selectById(String arg0) {
		// TODO Auto-generated method stub
		return roomMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<Room> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		List<Room> rooms = roomMapper.selectByPage(arg0);
		
		/**
		 * 查出房间是否使用过   
		 */
		/*for (Room room : rooms) {
			RoomHis roomHis = new RoomHis();
			roomHis.setRoomId(room.getRoomId());
			*//**
			 * 历史表不存在的房间可以删除
			 *//*
			List<RoomHis> roomHiss = roomHisMapper.selectByEntity(roomHis);
			if(roomHiss == null || roomHiss.size() == 0){
				room.setIsDelete("1");
			}
		}*/
		
		return rooms;
	}
	
	/**
	 * 分页查询房间和补助信息
	 */
	@Override
	public List<RoomAllowanceInfo> selectByPageForAllowance(Page arg0) {
		// TODO Auto-generated method stub
		return roomMapper.selectByPageForAllowance(arg0);
	}
	
	@Override
	public int update(Room newRoom) {
		int result = 0;
		// 查询旧数据
		Room oldRoom = roomMapper.selectByPrimaryKey(newRoom.getRoomId());
		if (!StringUtils.isEmpty(oldRoom)) {
			// 房间号不变
			if (oldRoom.getRoomNumber().equals(newRoom.getRoomNumber())
					|| StringUtils.isEmpty(newRoom.getRoomNumber())) {
				/**
				 * 宿舍
				 */
				if("1720".equals(newRoom.getRoomUse())){
					newRoom = initDorm(newRoom);
					newRoom.setAccountStatus(null);
				}else{
					newRoom.setAccountId("");
				}
				result = roomMapper.updateByPrimaryKeySelective(newRoom);
			} else {
				// 房间号变化时要判断不重复
				Room param = new Room();
				param.setOrgId(oldRoom.getOrgId());
				param.setRegionId(oldRoom.getRegionId());
				param.setRoomNumber(newRoom.getRoomNumber());
				List<Room> roomList = roomMapper.selectByEntity(param);
				/**
				 * 宿舍
				 */
				if("1720".equals(newRoom.getRoomUse())){
					newRoom = initDorm(newRoom);
					newRoom.setAccountStatus(null);
				}else{
					newRoom.setAccountId("");
				}
				
				if (roomList == null || roomList.size() <= 0) {
					roomMapper.updateByPrimaryKeySelective(newRoom);
				} else {
					throw new BusinessException(MonitResultCode.PARAMETER_NULL_ERROR, "该楼栋已存在房间编号");
				}
			}
		}
		return result;
	}

	@Override
	@Transactional
	public String insertRoom(RoomExcel roomExcel, String regionId, String orgId) {
		Room room = new Room();
		room.setOrgId(orgId);
		room.setRegionId(regionId);
		room.setRoomNumber(roomExcel.getRoomNumber());
		List<Room> roomList = roomMapper.selectByEntity(room);
		// 房间号不重复可以插入
		if (null == roomList || roomList.size() == 0) {
			// 楼栋信息
			SaasRegion region = saasRegionMapper.selectByPrimaryKey(regionId);
			if (region.getIsDelete() == 1) {
				region.setIsDelete((byte) 0);
				saasRegionMapper.updateByPrimaryKeySelective(region);
			}
			if (region.getIsLeaf() == 0) {
				return "请选择楼栋!";
			}
			room.setRegionFullName(region.getRegionFullName());
			room.setRegionNo(region.getRegionNo());
			room.setRegionNumber(region.getRegionNumber());
			// 房间名
			if (!StringUtils.isEmpty(roomExcel.getRoomName())) {
				room.setRoomName(roomExcel.getRoomName());
			}
			// 可入住人数
			if (!StringUtils.isEmpty(roomExcel.getPeopleLimit())) {
				room.setPeopleLimit(Integer.valueOf(roomExcel.getPeopleLimit()));
			}
			// 面积
			if (!StringUtils.isEmpty(roomExcel.getArea())) {
				room.setArea(new BigDecimal(roomExcel.getArea()));
			}
			room.setBalance(BigDecimal.ZERO);
			// 房间类型
			if (roomExcel.getRoomUse().equals("0")) {
				room.setRoomUse("1710");
			} else if (roomExcel.getRoomUse().equals("1")) {
				room.setRoomUse("1720");
			}
			// 商铺
			if (room.getRoomUse().equals("1710")){
				//房间状态为运维维护中 即房间初始状态
				room.setAccountStatus((byte) 0);
				room.setAccountId("");
				room.setElecAllowanceType("2000");
				room.setWaterAllowanceType("2000");
				room.setElecAllowance(BigDecimal.ZERO);
				room.setWaterAllowance(BigDecimal.ZERO);
			}
			// 宿舍
			else if (room.getRoomUse().equals("1720")){
				room = initDorm(room);
			}

			int result = roomMapper.insert(room);
			// 插入成功
			if (result == 1) {
				return "ok";
			}
			// 插入失败
			else {
				return "未知异常";
			}
		}
		// 房间号重复
		else {
			return "房间号重复";
		}
	}

	@Override
	public List<Room> selectRoomMeterByregionId(String regionId, String orgId) {

		return roomMapper.selectRoomMeterByregionId(regionId, orgId);
	}

	@Override
	public List<Room> selectByEntity(Room saasRoom) {
		return roomMapper.selectByEntity(saasRoom);
	}

	/**
	 * 商铺房间余额查询
	 */
	@Override
	public List<Room> selectShopRoomBalanceByPage(Page page) {
		// TODO Auto-generated method stub
		return roomMapper.selectShopRoomBalanceByPage(page);
	}

	/**
	 * 宿舍房间余额查询
	 */
	@Override
	public List<Room> selectDormRoomBalanceByPage(Page page) {
		// TODO Auto-generated method stub
		return roomMapper.selectDormRoomBalanceByPage(page);
	}

	/**
	 * 房间关联客户查询
	 */
	@Override
	public List<Room> selectWithCustByPage(Page page) {
		return roomMapper.selectWithCustByPage(page);
	}

	@Override
	public Map<String, Object> selectAllInfo(CustRoomParam param) {
		Map<String, Object> result = new HashMap<String, Object>();
		String roomId = param.getRoomId();
		// 查询房间
		Room room = roomMapper.selectByPrimaryKey(roomId);
		result.put("room", room);
		// 查询关联仪表
		Meter meterParam = new Meter();
		meterParam.setRoomId(roomId);
		List<Meter> meters = meterMapper.selectByEntity(meterParam);
		result.put("meterList", meters);
		// 账户存在
		if (!StringUtils.isEmpty(room.getAccountId())) {
			// 查询客户
			List<Customer> custList = customerMapper.selectByCustRoomParam(param);
			result.put("custList", custList);
		}

		return result;
	}

	/**
	 * 提交修改待审核
	 */
	@Override
	@Transactional
	public void submitAuditForUpdate(Map<String, Object> paramMap) {
		String orgId = (String) paramMap.get("orgId");
		Room room = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramMap.get("room")), Room.class);
		Meter elec = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramMap.get("elec")), Meter.class);
		Meter water = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramMap.get("water")), Meter.class);

		// 提交人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user = orgUserMapper.selectByPrimaryKey(info.getSubject());
		}
		String auditVersion = CommUtils.getUuid();

		/**
		 * 1.根据房间号查出房间信息
		 */
		int accountStatus = 0 ;
		Room room2 = roomMapper.selectByPrimaryKey(room.getRoomId());
		accountStatus = room2.getAccountStatus();
		room2.setAccountStatus((byte)2);//宿舍房间帐号状态改为审核中
		roomMapper.updateByPrimaryKeySelective(room2);
		
		/**
		 * 2.将房间信息和 前端传的计划用电规则存入房间审核表
		 */
		// 判断是会否有旧的待审核房间审核记录
		/*AuditRoom auditRoom1 = new AuditRoom();
		auditRoom1.setAuditStatus((byte) 0);
		auditRoom1.setAccountId(room2.getAccountId());
		List<AuditRoom> auditRooms = auditRoomMapper.selectByEntity(auditRoom1);
		// 房间账户存在则将旧记录修改为 取消
		if (auditRooms != null && auditRooms.size() > 0) {
			auditRoom1.setId(auditRooms.get(0).getId());
			auditRoom1.setAuditStatus((byte) 3);
			auditRoomMapper.updateByPrimaryKeySelective(auditRoom1);
		}*/

		AuditRoom auditRoom = new AuditRoom();
		auditRoom.setElecAllowance(room.getElecAllowance());
		auditRoom.setElecAllowanceType(room.getElecAllowanceType());
		auditRoom.setElecAllowanceRuleId(room.getElecAllowanceRuleId());
		auditRoom.setWaterAllowance(room.getWaterAllowance());
		auditRoom.setWaterAllowanceRuleId(room.getWaterAllowanceRuleId());
		auditRoom.setWaterAllowanceType(room.getWaterAllowanceType());
		auditRoom.setRoomId(room.getRoomId());
		auditRoom.setRegionFullName(room2.getRegionFullName());
		auditRoom.setRoomName(room2.getRoomName());
		auditRoom.setRoomNumber(room2.getRoomNumber());
		auditRoom.setAccountId(room2.getAccountId());
		auditRoom.setRoomUse(room2.getRoomUse());
		auditRoom.setOrgId(orgId);
		auditRoom.setSubmitId(user.getUserId());
		auditRoom.setSubmitName(user.getUserRealName());
		auditRoom.setAuditVersion(auditVersion);
		if(accountStatus == 1){
			auditRoom.setAuditType((byte) 3);//使用中的宿舍变更
		}else{
			auditRoom.setAuditType((byte) 2);//宿舍变更
		}
		auditRoom.setAuditStatus((byte)0);
		auditRoomMapper.insert(auditRoom);
		/**
		 * 3.根据仪表号查出仪表信息
		 */
		List<Meter> params = new ArrayList<Meter>();
		params.add(water);
		params.add(elec);
		/**
		 * 4.将仪表信息和 前端传的计价预警规则存入仪表审核表
		 */
		for (Meter temp : params) {
			if (!StringUtils.isEmpty(temp)) {
				if (!StringUtils.isEmpty(temp.getMeterId())) {
					Meter meter = meterMapper.selectByPrimaryKey(temp.getMeterId());
					meter.setSubAccountStatus((byte)2);//子账户状态 改为审核中的状态
					meterMapper.updateByPrimaryKeySelective(meter);

					/*AuditAccount auditAccount1 = new AuditAccount();
					auditAccount1.setAuditStatus((byte) 0);
					auditAccount1.setSubAccountId(meter.getSubAccountId());
					auditAccount1.setMeterAddr(meter.getMeterAddr());
					List<AuditAccount> auditAccounts = auditAccountMapper.selectByEntity(auditAccount1);
					*//**
					 * 判断是否拥有旧的待审核的记录
					 *//*
					if (auditAccounts != null && auditAccounts.size() > 0) {
						auditAccount1.setAuditStatus((byte) 3);
						auditAccount1.setId(auditAccounts.get(0).getId());
						auditAccountMapper.updateByPrimaryKeySelective(auditAccount1);
					}*/

					RuleAlarm ruleAlarm = ruleAlarmMapper.selectByPrimaryKey(temp.getAlarmId());
					EnergyPrice energyPrice = energyPriceMapper.selectByPrimaryKey(temp.getPriceId());
					// temp.setSubAccountStatus((byte)1);
					AuditAccount auditAccount = new AuditAccount();
					auditAccount.setAuditType((byte) 1);
					auditAccount.setSubAccountId(meter.getSubAccountId());
					auditAccount.setAlarmId(temp.getAlarmId());
					if (ruleAlarm != null) {
						auditAccount.setRuleName(ruleAlarm.getRuleName());
					} else {
						auditAccount.setRuleName("");
					}
					auditAccount.setPriceId(temp.getPriceId());
					if (energyPrice != null) {
						auditAccount.setPriceName(energyPrice.getPriceName());
					} else {
						auditAccount.setPriceName("");
					}
					auditAccount.setOrgId(meter.getOrgId());
					if (null != user) {
						auditAccount.setSubmitId(user.getUserId());
						auditAccount.setSubmitName(user.getUserRealName());
						auditAccount.setAuditName("");
					}
					auditAccount.setAuditStatus((byte) 0);
					auditAccount.setAuditVersion(auditVersion);
					auditAccount.setEnergyType(meter.getEnergyType());
					auditAccount.setMeterAddr(meter.getMeterAddr());
					auditAccount.setStartRead(temp.getStartRead());
					auditAccount.setPriceModeCode(temp.getPriceModeCode());
					auditAccount.setRegionFullName(room2.getRegionFullName());
					auditAccount.setRoomNumber(room2.getRoomNumber());
					auditAccount.setRoomId(meter.getRoomId());
					auditAccount.setStartTime(new Date(System.currentTimeMillis()));
                     
					auditAccount.setCurrentTimeLimitId(temp.getCurrentTimeLimitId());
					auditAccount.setPowerLadderLimitId(temp.getPowerLadderLimitId());
					auditAccount.setPowerFluctuationLimitId(temp.getPowerFluctuationLimitId());
					auditAccount.setPowerChangeLimitId(temp.getPowerChangeLimitId());
					auditAccount.setPowerMaxLimitId(temp.getPowerMaxLimitId());
					auditAccountMapper.insert(auditAccount);
				}
			}
		}
	}
	/**
	 * 提交开户待审核
	 */
	@Override
	@Transactional
	public void submitAuditForSign(Map<String, Object> paramMap) {
		String orgId = (String) paramMap.get("orgId");
		Customer cust = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramMap.get("cust")), Customer.class);
		Room room = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramMap.get("room")), Room.class);
		Meter elec = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramMap.get("elec")), Meter.class);
		Meter water = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramMap.get("water")), Meter.class);
		
		/**
		 * 判断是否有仪表
		 */
		/*if(StringUtils.isEmpty(elec) && StringUtils.isEmpty(water)){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR,"无可用仪表！");
		}*/
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(room.getStartTime());
		// 将时分秒,毫秒域清零
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		room.setStartTime(cal1.getTime());
		
		cal1.setTime(room.getEndTime());
		// 将时分秒,毫秒域清零
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		room.setEndTime(cal1.getTime());
		
		
		// 提交人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user = orgUserMapper.selectByPrimaryKey(info.getSubject());
		}
		String auditVersion = CommUtils.getUuid();
		/**
		 * 1.添加客户
		 */
		Customer customer = new Customer();
		customer.setOrgId(orgId);
		List<Customer> customers = customerMapper.selectByEntity(customer);
		//是否 存在 0 不存在   1存在
		int isExist = 0;
		for (Customer customer2 : customers) {
			//用手机号对 客户做校验
			if(!StringUtils.isEmpty(customer2.getCustomerStatus())){
				//if(customer2.getCustomerStatus() != 2){新户签约时，如果该新户在数据库中存在，不管是否销户，都应该沿用这个已存在的数据
					if(customer2.getCustomerMobile1().equals(cust.getCustomerMobile1())){
						isExist = 1;
						customer = customer2;
					}
				//}
			}
			
			/**
			 * 如果身份标识为空，那么默认填入手机号
			 */
			if(StringUtils.isEmpty(customer2.getCustomerNo())){
				cust.setCustomerNo(customer2.getCustomerMobile1());
			}
			
			
			if(StringUtils.isEmpty(customer2.getCustomerSex())){
				cust.setCustomerSex((byte) 1);
			}
		}
		
		
		/**
		 * 已存在的客户  不做客户添加
		 */
		String customerId = "";
		if(isExist == 1){
			customerId = customer.getCustomerId();
			customer.setCustomerStatus((byte)0);
			customerMapper.updateByPrimaryKeySelective(customer);
		}else{
			customerId = CommUtils.getUuid();
			cust.setCustomerId(customerId);
			cust.setOrgId(orgId);
			cust.setOperatorName(user.getUserRealName());
			cust.setOperatorId(user.getUserId());
			cust.setCustomerStatus((byte)0);
			/**
			 * 如果身份标识为空，那么默认填入手机号，不为空则判重
			 */
			if(StringUtils.isEmpty(cust.getCustomerNo())){
				cust.setCustomerNo(cust.getCustomerMobile1());
			}else{
				Customer param= new Customer();
				param.setOrgId(orgId);
				param.setCustomerNo(cust.getCustomerNo());
				List<Customer> custlist=customerMapper.selectByEntity(param);
				if(custlist!=null&&custlist.size()>0){
					throw new BusinessException(ResultCode.PARAM_VALID_ERROR,"客户标识已存在");
				}
			}
			/**
			 * 如果性别为空，那么默认为1
			 */
			if(StringUtils.isEmpty(cust.getCustomerSex())){
				cust.setCustomerSex((byte)1);
			}
			cust.setCustomerType("1810");
			cust.setCustomerPassword(DigestUtils.md5Hex("123456".getBytes()));
			customerMapper.shopInsert(cust);
		}
		
		
		/**
		 * 2.客户房间关联
		 */
		//账户ID
		String accountId = CommUtils.getUuid();
		CustomerRoom customerRoom = new CustomerRoom();
		customerRoom.setCustomerId(customerId);
		customerRoom.setOrgId(orgId);
		customerRoom.setRoomId(room.getRoomId());
		customerRoom.setStatus((byte)0);
		customerRoom.setRoomAccountId(accountId);
		customerRoomMapper.insert(customerRoom);
		
		/**
		 * 3.存入房间的审核表
		 */
		Room room2 = roomMapper.selectByPrimaryKey(room.getRoomId());
		/**
		 * 添加房间账户ID
		 */
		room2.setAccountId(accountId);
		room2.setRoomName(room.getRoomName());
		room2.setAccountStatus((byte)2);//房间账户状态 修改为审核中
		roomMapper.updateByPrimaryKeySelective(room2);
		
		// 判断是会否有旧的待审核房间审核记录
		/*AuditRoom auditRoom1 = new AuditRoom();
		auditRoom1.setAuditStatus((byte) 0);
		auditRoom1.setAccountId(room2.getAccountId());
		List<AuditRoom> auditRooms = auditRoomMapper.selectByEntity(auditRoom1);
		// 存在则将旧记录修改为 取消
		if (auditRooms != null && auditRooms.size() > 0) {
			auditRoom1.setId(auditRooms.get(0).getId());
			auditRoom1.setAuditStatus((byte) 3);
			auditRoomMapper.updateByPrimaryKeySelective(auditRoom1);
		}*/

		AuditRoom auditRoom = new AuditRoom();
		auditRoom.setElecAllowance(room.getElecAllowance());
		auditRoom.setElecAllowanceType(room.getElecAllowanceType());
		auditRoom.setElecAllowanceRuleId(room.getElecAllowanceRuleId());
		auditRoom.setWaterAllowance(room.getWaterAllowance());
		auditRoom.setWaterAllowanceRuleId(room.getWaterAllowanceRuleId());
		auditRoom.setWaterAllowanceType(room.getWaterAllowanceType());
		auditRoom.setRoomId(room.getRoomId());
		auditRoom.setStartTime(room.getStartTime());
		auditRoom.setEndTime(room.getEndTime());
		auditRoom.setRegionFullName(room2.getRegionFullName());
		auditRoom.setRoomName(room.getRoomName());
		auditRoom.setRoomNumber(room2.getRoomNumber());
		auditRoom.setAccountId(accountId);
		auditRoom.setRoomUse(room2.getRoomUse());
		auditRoom.setOrgId(orgId);
		auditRoom.setSubmitId(user.getUserId());
		auditRoom.setSubmitName(user.getUserRealName());
		auditRoom.setAuditVersion(auditVersion);
		auditRoom.setAuditType((byte) 0);//签约审核
		auditRoom.setAuditStatus((byte)0);
		auditRoom.setPropertyId(room.getPropertyId());
		auditRoom.setRentalId(room.getRentalId());
		auditRoomMapper.insert(auditRoom);
		/**
		 * 4.存入仪表的审核表
		 */
		List<Meter> params = new ArrayList<Meter>();
		params.add(water);
		params.add(elec);
		for (Meter temp : params) {
			if (!StringUtils.isEmpty(temp)) {
				String subAccountId = CommUtils.getUuid();
				if (!StringUtils.isEmpty(temp.getMeterId())) {
					Meter meter = meterMapper.selectByPrimaryKey(temp.getMeterId());
					//将仪表账户状态 设置为审核中
					meter.setSubAccountStatus((byte)2);
					meterMapper.updateByPrimaryKeySelective(meter);
					
					/*AuditAccount auditAccount1 = new AuditAccount();
					auditAccount1.setAuditStatus((byte) 0);
					auditAccount1.setSubAccountId(meter.getSubAccountId());
					auditAccount1.setMeterAddr(meter.getMeterAddr());
					List<AuditAccount> auditAccounts = auditAccountMapper.selectByEntity(auditAccount1);
					*//**
					 * 判断是否拥有旧的待审核的记录
					 *//*
					if (auditAccounts != null && auditAccounts.size() > 0) {
						auditAccount1.setAuditStatus((byte) 3);
						auditAccount1.setId(auditAccounts.get(0).getId());
						auditAccountMapper.updateByPrimaryKeySelective(auditAccount1);
					}*/

					RuleAlarm ruleAlarm = ruleAlarmMapper.selectByPrimaryKey(temp.getAlarmId());
					EnergyPrice energyPrice = energyPriceMapper.selectByPrimaryKey(temp.getPriceId());
					// temp.setSubAccountStatus((byte)1);
					AuditAccount auditAccount = new AuditAccount();
					auditAccount.setAuditType((byte) 0);
					//auditAccount.setSubAccountId(meter.getSubAccountId());
					auditAccount.setAlarmId(temp.getAlarmId());
					if (ruleAlarm != null) {
						auditAccount.setRuleName(ruleAlarm.getRuleName());
					} else {
						auditAccount.setRuleName("");
					}
					auditAccount.setPriceId(temp.getPriceId());
					if (energyPrice != null) {
						auditAccount.setPriceName(energyPrice.getPriceName());
					} else {
						auditAccount.setPriceName("");
					}
					auditAccount.setOrgId(meter.getOrgId());
					if (null != user) {
						auditAccount.setSubmitId(user.getUserId());
						auditAccount.setSubmitName(user.getUserRealName());
						auditAccount.setAuditName("");
					}
					auditAccount.setAuditStatus((byte) 0);
					auditAccount.setAuditVersion(auditVersion);
					auditAccount.setEnergyType(meter.getEnergyType());
					auditAccount.setMeterAddr(meter.getMeterAddr());
					auditAccount.setStartRead(temp.getStartRead());
					auditAccount.setAccountId(accountId);
					auditAccount.setSubAccountId(subAccountId);
					auditAccount.setPriceModeCode(temp.getPriceModeCode());
					auditAccount.setRegionFullName(room2.getRegionFullName());
					auditAccount.setRoomNumber(room2.getRoomNumber());
					auditAccount.setRoomId(meter.getRoomId());
					auditAccount.setStartTime(new Date(System.currentTimeMillis()));
                     
					auditAccount.setCurrentTimeLimitId(temp.getCurrentTimeLimitId());
					auditAccount.setPowerLadderLimitId(temp.getPowerLadderLimitId());
					auditAccount.setPowerFluctuationLimitId(temp.getPowerFluctuationLimitId());
					auditAccount.setPowerChangeLimitId(temp.getPowerChangeLimitId());
					auditAccount.setPowerMaxLimitId(temp.getPowerMaxLimitId());
					auditAccountMapper.insert(auditAccount);
				}
			}
		}
	}
	
	
	/**
	 * 提交续约待审核
	 */
	@Override
	@Transactional
	public void submitAuditForRenewal(Map<String, Object> paramMap) {
		String orgId = (String) paramMap.get("orgId");
		Room room = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramMap.get("room")), Room.class);
		Meter elec = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramMap.get("elec")), Meter.class);
		Meter water = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramMap.get("water")), Meter.class);
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(room.getStartTime());
		// 将时分秒,毫秒域清零
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		room.setStartTime(cal1.getTime());
		
		cal1.setTime(room.getEndTime());
		// 将时分秒,毫秒域清零
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		room.setEndTime(cal1.getTime());
		
		
		
		// 提交人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user = orgUserMapper.selectByPrimaryKey(info.getSubject());
		}
		String auditVersion = CommUtils.getUuid();
		
		/**
		 * 存入房间的审核表
		 */
		Room room2 = roomMapper.selectByPrimaryKey(room.getRoomId());
		/**
		 * 添加房间账户ID
		 */
		room2.setAccountStatus((byte)2);//房间账户状态 修改为审核中
		roomMapper.updateByPrimaryKeySelective(room2);
		
		
		/*// 判断是会否有旧的待审核房间审核记录
		AuditRoom auditRoom1 = new AuditRoom();
		auditRoom1.setAuditStatus((byte) 0);
		auditRoom1.setAccountId(room2.getAccountId());
		List<AuditRoom> auditRooms = auditRoomMapper.selectByEntity(auditRoom1);
		// 存在则将旧记录修改为 取消
		if (auditRooms != null && auditRooms.size() > 0) {
			auditRoom1.setId(auditRooms.get(0).getId());
			auditRoom1.setAuditStatus((byte) 3);
			auditRoomMapper.updateByPrimaryKeySelective(auditRoom1);
		}*/

		AuditRoom auditRoom = new AuditRoom();
		auditRoom.setElecAllowance(room.getElecAllowance());
		auditRoom.setElecAllowanceType(room.getElecAllowanceType());
		auditRoom.setElecAllowanceRuleId(room.getElecAllowanceRuleId());
		auditRoom.setWaterAllowance(room.getWaterAllowance());
		auditRoom.setWaterAllowanceRuleId(room.getWaterAllowanceRuleId());
		auditRoom.setWaterAllowanceType(room.getWaterAllowanceType());
		auditRoom.setRoomId(room.getRoomId());
		auditRoom.setStartTime(room.getStartTime());
		auditRoom.setEndTime(room.getEndTime());
		auditRoom.setRegionFullName(room2.getRegionFullName());
		auditRoom.setRoomName(room2.getRoomName());
		auditRoom.setRoomNumber(room2.getRoomNumber());
		auditRoom.setAccountId(room2.getAccountId());
		auditRoom.setRoomUse(room2.getRoomUse());
		auditRoom.setOrgId(orgId);
		auditRoom.setSubmitId(user.getUserId());
		auditRoom.setSubmitName(user.getUserRealName());
		auditRoom.setAuditVersion(auditVersion);
		auditRoom.setAuditType((byte) 1);//续约审核
		auditRoom.setAuditStatus((byte)0);
		auditRoom.setPropertyId(room.getPropertyId());
		auditRoom.setRentalId(room.getRentalId());
		auditRoomMapper.insert(auditRoom);
		/**
		 * 存入仪表的审核表
		 */
		List<Meter> params = new ArrayList<Meter>();
		params.add(water);
		params.add(elec);
		for (Meter temp : params) {
			if (!StringUtils.isEmpty(temp)) {
				if (!StringUtils.isEmpty(temp.getMeterId())) {
					Meter meter = meterMapper.selectByPrimaryKey(temp.getMeterId());
					//将仪表账户状态 设置为审核中
					meter.setSubAccountStatus((byte)2);
					meterMapper.updateByPrimaryKeySelective(meter);
					
                    meter.setAccountId(room2.getAccountId());
					/*AuditAccount auditAccount1 = new AuditAccount();
					auditAccount1.setAuditStatus((byte) 0);
					auditAccount1.setAccountId(meter.getAccountId());
					auditAccount1.setSubAccountId(meter.getSubAccountId());
					List<AuditAccount> auditAccounts = auditAccountMapper.selectByEntity(auditAccount1);
					*//**
					 * 判断是否拥有旧的待审核的记录
					 *//*
					if (auditAccounts != null && auditAccounts.size() > 0) {
						auditAccount1.setAuditStatus((byte) 3);
						auditAccount1.setId(auditAccounts.get(0).getId());
						auditAccountMapper.updateByPrimaryKeySelective(auditAccount1);
					}*/

					RuleAlarm ruleAlarm = ruleAlarmMapper.selectByPrimaryKey(temp.getAlarmId());
					EnergyPrice energyPrice = energyPriceMapper.selectByPrimaryKey(temp.getPriceId());
					// temp.setSubAccountStatus((byte)1);
					AuditAccount auditAccount = new AuditAccount();
					auditAccount.setAuditType((byte) 1);
					auditAccount.setSubAccountId(meter.getSubAccountId());
					auditAccount.setAlarmId(temp.getAlarmId());
					if (ruleAlarm != null) {
						auditAccount.setRuleName(ruleAlarm.getRuleName());
					} else {
						auditAccount.setRuleName("");
					}
					auditAccount.setPriceId(temp.getPriceId());
					if (energyPrice != null) {
						auditAccount.setPriceName(energyPrice.getPriceName());
					} else {
						auditAccount.setPriceName("");
					}
					auditAccount.setOrgId(meter.getOrgId());
					if (null != user) {
						auditAccount.setSubmitId(user.getUserId());
						auditAccount.setSubmitName(user.getUserRealName());
						auditAccount.setAuditName("");
					}
					auditAccount.setAuditStatus((byte) 0);
					auditAccount.setAuditVersion(auditVersion);
					auditAccount.setEnergyType(meter.getEnergyType());
					auditAccount.setMeterAddr(meter.getMeterAddr());
					auditAccount.setStartRead(temp.getStartRead());
					auditAccount.setPriceModeCode(temp.getPriceModeCode());
					auditAccount.setRegionFullName(room2.getRegionFullName());
					auditAccount.setRoomNumber(room2.getRoomNumber());
					auditAccount.setRoomId(meter.getRoomId());
					auditAccount.setStartTime(new Date(System.currentTimeMillis()));
					auditAccount.setCurrentTimeLimitId(temp.getCurrentTimeLimitId());
					auditAccount.setPowerLadderLimitId(temp.getPowerLadderLimitId());
					auditAccount.setPowerFluctuationLimitId(temp.getPowerFluctuationLimitId());
					auditAccount.setPowerChangeLimitId(temp.getPowerChangeLimitId());
					auditAccount.setPowerMaxLimitId(temp.getPowerMaxLimitId());
					auditAccountMapper.insert(auditAccount);
				}
			}
		}

	}
	
	/**
	 * 宿舍初始化
	 * @param room
	 * @return
	 */
	private Room initDorm(Room room){
		//房间状态为运维维护中 即房间初始状态  
		room.setAccountStatus((byte) 0);
		room.setAccountId(room.generateAccountId());
		// TODO 暂时默认已入住人数=可入住人数
		room.setPeopleNumber(room.getPeopleLimit());
		EnergyAllowance ruleParam = new EnergyAllowance();
		// 查询补助默认规则
		ruleParam.setOrgId(room.getOrgId());
		ruleParam.setIsDefault((byte) 1);
		// 电量补助默认规则
		ruleParam.setEnergyType("110000");
		List<EnergyAllowance> rules = allowanceMapper.selectByEntity(ruleParam);
		if(rules != null && rules.size() > 0){
			room.setElecAllowanceRuleId(rules.get(0).getRuleId());
			room.setElecAllowanceType(rules.get(0).getAllowanceType());
		}
		// 不补助
		else {
			room.setElecAllowanceRuleId("");
			room.setElecAllowanceType("2000");
		}
		// 水量补助默认规则
		ruleParam.setEnergyType("120000");
		rules = allowanceMapper.selectByEntity(ruleParam);
		if(rules != null && rules.size() > 0){
			room.setWaterAllowanceRuleId(rules.get(0).getRuleId());
			room.setWaterAllowanceType(rules.get(0).getAllowanceType());
		}
		// 不补助
		else {
			room.setWaterAllowanceRuleId("");
			room.setWaterAllowanceType("2000");
		}
		room.setElecAllowance(BigDecimal.ZERO);
		room.setWaterAllowance(BigDecimal.ZERO);
		return room;
	}

	/**
	 * 商铺余额导出的Serviceimpl
	 */
	@Override
	public List<Room> shopbalanceExport(Room room) {
		List<Room> rooms=roomMapper.shopbalanceExport(room);
		/*for (Room room2 : rooms) {
			if(room2.getSubWaterBalance().toString()==null){
				room2.setSubWaterBalanceStr("0");
			}else if(room2.getSubWaterBalance().toString()!=null){
				room2.setSubWaterBalanceStr(room2.getSubWaterBalance().toString());
			}
			if(room2.getSubElecBalance().toString()==null){
				room2.setSubElecBalanceStr("0");
			}else if(room2.getSubElecBalance().toString()!=null){
				room2.setSubWaterBalanceStr(room2.getSubElecBalance().toString());
			}
		}*/
		for (Room room2 : rooms) {
			
			if(!StringUtils.isEmpty(room2.getSubWaterBalance())){
				room2.setSubWaterBalanceStr(room2.getSubWaterBalance().toString());
			}else{
				room2.setSubWaterBalanceStr("0.00");
			}
			if(!StringUtils.isEmpty(room2.getSubElecBalance())){
				if(("0.00").equals(room2.getSubElecBalance().toString())){
					room2.setSubElecBalanceStr("0.00");
				}else{
					room2.setSubElecBalanceStr(room2.getSubElecBalance().toString());
				}
			}else{
				room2.setSubElecBalanceStr("0.00");
			}
			
		}
		return rooms;
	}

	@Override
	public List<Room> selectRoomStatusByPage(Page page) {
		return roomMapper.selectRoomStatusByPage(page);
	}


	@Override
	public List<Room> selectRoomWithThreePhaseByRegionId(Room room) {
		return roomMapper.selectRoomWithThreePhaseByRegionId(room);
	}
	
	/**
	 * 房态管理 修改使用状态
	 */
	@Override
	@Transactional
	public void updateRoomStatus(Room newRoom) {
		Meter meter = new Meter();
		/**
		 * 判断是废弃维护还是空置状态变更
		 */
		//由空置改为废弃
		if(newRoom.getAccountStatus() == 3){
			//解除仪表房间的关系  清空meter表房间ID 仪表状态改为初始状态即空置
			meter.setRoomId("");
			meter.setMeterAddr(newRoom.geteMeterAddr());
			meter.setSubAccountStatus((byte)0);
			meterMapper.updateToAlarmOrPrice(meter);
			
			//解除客户房间关系 修改客户状态
			CustomerRoom customerRoom = new CustomerRoom();
			customerRoom.setRoomId(newRoom.getRoomId());
			customerRoom.setRoomAccountId(newRoom.getAccountId());
			List<CustomerRoom> customerRooms = customerRoomMapper.selectByEntity(customerRoom);
			for (CustomerRoom customerRoom2 : customerRooms) {
				customerRoom2.setStatus((byte)2);
				customerRoomMapper.updateByPrimaryKeySelective(customerRoom2);
				
				Customer customer = new Customer();
				customer.setCustomerId(customerRoom2.getCustomerId());
				customer.setCustomerStatus((byte)1);
				customerMapper.updateByPrimaryKeySelective(customer);
			}
			
		}
		
		//由维护改为空置
		if(newRoom.getAccountStatus() == 4){
			//电表默认规则
			if(!StringUtils.isEmpty(newRoom.geteMeterAddr())){
				/**
				 * 1.判断房间绑定的仪表是否与预警规则绑定   没有绑定则给仪表绑定默认的 预警规则
				 */
				if(StringUtils.isEmpty(newRoom.geteAlarmId())){
					RuleAlarm ruleAlarm = ruleAlarmMapper.selectIsDefaultRule(newRoom.getOrgId(),"110000");
					if(StringUtils.isEmpty(ruleAlarm)){
						throw new BusinessException(MonitResultCode.DATA_EXISTS_NULL, "电表没有默认的预警规则");
					}
					meter.setAlarmId(ruleAlarm.getAlarmId());
				}
				
				/**
				 * 2.判断房间绑定的仪表是否与计价规则绑定   没有绑定则给仪表绑定默认的计价规则
				 */
				if(StringUtils.isEmpty(newRoom.getePriceId())){
					EnergyPrice energyPrice = energyPriceMapper.selectIsDefaultPrice(newRoom.getOrgId(),"110000");
					if(StringUtils.isEmpty(energyPrice)){
						throw new BusinessException(MonitResultCode.DATA_EXISTS_NULL, "电表没有默认的计价规则");
					}
					meter.setPriceId(energyPrice.getPriceId());
				}
			}
			//水表默认规则
			if(!StringUtils.isEmpty(newRoom.getwMeterAddr())){
				/**
				 * 1.判断房间绑定的仪表是否与预警规则绑定   没有绑定则给仪表绑定默认的 预警规则
				 */
				if(StringUtils.isEmpty(newRoom.getwAlarmId())){
					RuleAlarm ruleAlarm = ruleAlarmMapper.selectIsDefaultRule(newRoom.getOrgId(),"120000");
					if(StringUtils.isEmpty(ruleAlarm)){
						throw new BusinessException(MonitResultCode.DATA_EXISTS_NULL, "水表没有默认的预警规则");
					}
					meter.setAlarmId(ruleAlarm.getAlarmId());
				}
				
				/**
				 * 2.判断房间绑定的仪表是否与计价规则绑定   没有绑定则给仪表绑定默认的计价规则
				 */
				if(StringUtils.isEmpty(newRoom.getePriceId())){
					EnergyPrice energyPrice = energyPriceMapper.selectIsDefaultPrice(newRoom.getOrgId(),"120000");
					if(StringUtils.isEmpty(energyPrice)){
						throw new BusinessException(MonitResultCode.DATA_EXISTS_NULL, "水表没有默认的计价规则");
					}
					meter.setPriceId(energyPrice.getPriceId());
				}
				
			}
			
			
			meter.setSubAccountStatus((byte)1);
			meter.setMeterAddr(newRoom.geteMeterAddr());
			
			meterMapper.updateToAlarmOrPrice(meter);
		}
		
		roomMapper.updateByPrimaryKeySelective(newRoom);
	}

	@Override
	public List<Room> selectRoomCustByRegionId(String regionId) {
		return roomMapper.selectRoomCustByRegionId(regionId);
	}

	@Override
	public List<Room> selectRoomMeterByRegionId(String regionId) {
		return roomMapper.selectRoomMeterByRegionId(regionId);
	}

	@Override
	@Transactional
	public void roomBalabceReset(List<Room> rooms) {
		// 提交人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user = orgUserMapper.selectByPrimaryKey(info.getSubject());
		}
		
		for (Room room : rooms) {
			
			Room room2 = roomMapper.selectByPrimaryKey(room.getRoomId());
			
			ClearAllowanceRecord clearAllowanceRecord = new ClearAllowanceRecord();
			
			/**
			 * 清处计划用量流水保存 （电）
			 */
			if(!StringUtils.isEmpty(room2.getElecAllowance()) && !StringUtils.isEmpty(room2.getElecAllowanceRuleId())){
				AllowanceRecord record = AllowanceRecord.generateRecord(room, null, room2.getElecAllowance());
				record.setStatus((byte) 1);
				record.setExecuteTime(new Date());
				record.setVolume(BigDecimal.ZERO.subtract(room2.getElecAllowance()));
				record.setBalance(BigDecimal.ZERO);
				record.setRemark("宿舍计划用量清零操作");
				record.setEnergyType(EnergyCategEum.ELEC.getValue());
				allowanceRecordMapper.insert(record);
				room.setElecAllowance(BigDecimal.ZERO);
				
				clearAllowanceRecord.setElecAllowanceRuleId(room2.getElecAllowanceRuleId());
				clearAllowanceRecord.setElecAllowanceType(room2.getElecAllowanceType());
				clearAllowanceRecord.setElecAllowanceBalance(BigDecimal.ZERO.subtract(room2.getElecAllowance()));
			}
			
			/**
			 * 清处计划用量流水保存 （水）
			 */
			if(!StringUtils.isEmpty(room2.getWaterAllowance()) && !StringUtils.isEmpty(room2.getWaterAllowanceRuleId())){
				AllowanceRecord record = AllowanceRecord.generateRecord(room, null, room2.getWaterAllowance());
				record.setStatus((byte) 1);
				record.setExecuteTime(new Date());
				record.setVolume(BigDecimal.ZERO.subtract(room2.getElecAllowance()));
				record.setBalance(BigDecimal.ZERO);
				record.setRemark("宿舍计划用量清零操作");
				record.setEnergyType(EnergyCategEum.WATER.getValue());
				allowanceRecordMapper.insert(record);
				room.setWaterAllowance(BigDecimal.ZERO);
				
				clearAllowanceRecord.setWaterAllowanceRuleId(room2.getWaterAllowanceRuleId());
				clearAllowanceRecord.setWaterAllowanceType(room2.getWaterAllowanceType());
				clearAllowanceRecord.setWaterAllowanceBalance(BigDecimal.ZERO.subtract(room2.getElecAllowance()));
			}
			
			/**
			 * 房间账户流水插入
			 */
			RoomAccountRecord record2 = RoomAccountRecord.generateRecord(room2, null, null, room2.getBalance());
			record2.setRemark("宿舍计划用量清零同时清除余额");
			record2.setOrderTypeCode("5");// 2、房间清零
			record2.setMoney(BigDecimal.ZERO.subtract(room2.getBalance()));
			record2.setBalance(BigDecimal.ZERO);
			roomAccountRecordMapper.insert(record2);
			
			room.setBalance(BigDecimal.ZERO);
			roomMapper.updateByPrimaryKeySelective(room);
			
			
			clearAllowanceRecord.setRoomBalance(BigDecimal.ZERO.subtract(room2.getBalance()));
			clearAllowanceRecord.setRoomId(room2.getRoomId());
			clearAllowanceRecord.setRoomFullName(room2.getRegionFullName());
			clearAllowanceRecord.setRoomAccountId(room2.getAccountId());
			clearAllowanceRecord.setRoomName(room2.getRoomName());
			clearAllowanceRecord.setRoomNumber(room2.getRoomNumber());
			clearAllowanceRecord.setOperatorId(user.getUserId());
			clearAllowanceRecord.setOperatorName(user.getUserRealName());
			clearAllowanceRecord.setOrgId(room2.getOrgId());
			//清除计划用量记录 插入
			clearAllowanceRecordMapper.insert(clearAllowanceRecord);
		}
		
	}

	@Override
	public List<Room> dormRoomBalanceExpert(Room room) {
		Page page = new Page();
		page.setParamObj(room);
		return roomMapper.selectDormRoomBalanceByPage(page);
	}

}
