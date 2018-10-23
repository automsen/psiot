package cn.com.tw.saas.serv.service.audit.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.ApiTemplateCons;
import cn.com.tw.saas.serv.entity.audit.AuditAccount;
import cn.com.tw.saas.serv.entity.audit.AuditRoom;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.db.rule.RuleElec;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.AllowancePlan;
import cn.com.tw.saas.serv.entity.room.AllowanceRecord;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.audit.AuditAccountMapper;
import cn.com.tw.saas.serv.mapper.audit.AuditRoomMapper;
import cn.com.tw.saas.serv.mapper.cust.CustomerMapper;
import cn.com.tw.saas.serv.mapper.cust.CustomerRoomMapper;
import cn.com.tw.saas.serv.mapper.room.AllowancePlanMapper;
import cn.com.tw.saas.serv.mapper.room.AllowanceRecordMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.rule.RuleElecMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.audit.AuditRoomService;
import cn.com.tw.saas.serv.service.command.CmdRecordService;

@Service
public class AuditRoomServiceImpl implements AuditRoomService{
	
	@Autowired
	private AuditRoomMapper AuditRoomMapper;
	@Autowired
	private RoomMapper roomMapper;
	@Autowired
	private AuditAccountMapper auditAccountMapper;
	@Autowired
	private MeterMapper meterMapper;
	@Autowired
	private CustomerRoomMapper customerRoomMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private RuleElecMapper ruleElecMapper;
	@Autowired
	private CmdRecordService cmdService;
	@Autowired
	private AllowanceRecordMapper allowanceRecordMapper;
	@Autowired
	private AllowancePlanMapper allowancePlanMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return AuditRoomMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public int insert(AuditRoom auditRoom) {
		// TODO Auto-generated method stub
		return AuditRoomMapper.insert(auditRoom);
	}

	@Override
	public int insertSelect(AuditRoom auditRoom) {
		// TODO Auto-generated method stub
		return AuditRoomMapper.insertSelective(auditRoom);
	}

	@Override
	public AuditRoom selectById(String arg0) {
		// TODO Auto-generated method stub
		return AuditRoomMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<AuditRoom> selectByPage(Page page) {
		// TODO Auto-generated method stub
		return AuditRoomMapper.selectByPage(page);
	}

	@Override
	public int update(AuditRoom auditRoom) {
		// TODO Auto-generated method stub
		return AuditRoomMapper.updateByPrimaryKey(auditRoom);
	}

	@Override
	public int updateSelect(AuditRoom auditRoom) {
		// TODO Auto-generated method stub
		return AuditRoomMapper.updateByPrimaryKeySelective(auditRoom);
	}

	
	/**
	 * 通过
	 */
	@Override
	@Transactional
	public void passAuditRoom(AuditRoom auditRoom) {
		
		// 审核人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())){
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		/**
		 * 1.根据主键ID 查出 计划用量
		 */
		AuditRoom auditRoom2 = AuditRoomMapper.selectByPrimaryKey(auditRoom.getId());
		/**
		 * 2.根据房间ID 修改房间的计划用量  修改房间审核状态为通过
		 */
		Room room = roomMapper.selectByPrimaryKey(auditRoom.getRoomId());
		
		//水电管理取消房间的计划用电规则并审核通过后，应该取消掉计划用量里该房间的待发放的计划用量。  暂时不考虑水的情况 后期要改
		if(auditRoom2.getElecAllowanceRuleId() == null || !auditRoom2.getElecAllowanceRuleId().equals(room.getElecAllowanceRuleId())){
			/**
			 * 清除房间的 计划用量预设
			 */
			List<AllowanceRecord> allowanceRecords = allowanceRecordMapper.selectByRoomIdAndStatus(auditRoom.getRoomId());
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
		}
		
		room.setAccountId(auditRoom2.getAccountId());
		room.setRoomId(auditRoom2.getRoomId());
		room.setElecAllowance(auditRoom2.getElecAllowance());
		room.setElecAllowanceType(auditRoom2.getElecAllowanceType());
		room.setElecAllowanceRuleId(auditRoom2.getElecAllowanceRuleId());
		room.setWaterAllowance(auditRoom2.getWaterAllowance());
		room.setWaterAllowanceType(auditRoom2.getWaterAllowanceType());
		room.setWaterAllowanceRuleId(auditRoom2.getWaterAllowanceRuleId());
		room.setPropertyId(auditRoom2.getPropertyId());
		room.setRentalId(auditRoom2.getRentalId());
		room.setAccountStatus((byte)1);//通过审核 房间账户状态改为使用中
		room.setStartTime(auditRoom.getStartTime());
		room.setEndTime(auditRoom.getEndTime());
		room.setIsUsed((byte)1);
		roomMapper.updateByPrimaryKeySelective(room);
		
		//修改房间客户关联状态
		CustomerRoom customerRoom = new CustomerRoom();
		Customer customer = new Customer();
		customerRoom.setRoomAccountId(auditRoom2.getAccountId());
		List<CustomerRoom> customerRooms = customerRoomMapper.selectByEntity(customerRoom);
		if(customerRooms != null && customerRooms.size() > 0){
			customerRoom = customerRooms.get(0);
			customerRoom.setStatus((byte)1);
		}
		customer.setCustomerId(customerRoom.getCustomerId());
		customer.setCustomerStatus((byte)0);
		customerMapper.updateByPrimaryKeySelective(customer);
		customerRoomMapper.updateByPrimaryKeySelective(customerRoom);
		
		auditRoom2.setAuditStatus((byte)1);//通过
		auditRoom2.setUpdateTime(new Date(System.currentTimeMillis()));
		auditRoom2.setAuditId(user.getUserId());
		auditRoom2.setAuditName(user.getUserName());
		auditRoom2.setAuditOpinion(auditRoom.getAuditOpinion());
		AuditRoomMapper.updateByPrimaryKeySelective(auditRoom2);
		
		/**
		 * 3.判断传入的是水电表是否为空  并查处仪表审核
		 */
		List<AuditAccount> auditAccounts = new ArrayList<AuditAccount>();
		List<AuditAccount> elecMeter = new ArrayList<AuditAccount>();
		List<AuditAccount> waterMeter = new ArrayList<AuditAccount>();
		AuditAccount auditAccount = new AuditAccount();
		auditAccount.setAuditStatus((byte)0);
		if(!StringUtils.isEmpty(auditRoom.getElecMeterAddr())){
			auditAccount.setMeterAddr(auditRoom.getElecMeterAddr());
			elecMeter = auditAccountMapper.selectByEntity(auditAccount);
		}
		if(!StringUtils.isEmpty(auditRoom.getWaterMeterAddr())){
			auditAccount.setMeterAddr(auditRoom.getWaterMeterAddr());
			waterMeter = auditAccountMapper.selectByEntity(auditAccount);
		}
		/**
		 * 4.修改仪表审核的状态 修改仪表规则
		 */
		if(elecMeter != null && elecMeter.size() > 0){
			auditAccounts.addAll(elecMeter);
		}
		if(waterMeter != null && waterMeter.size() > 0){
			auditAccounts.addAll(waterMeter);
		}
		/**
		 * 修改审核记录 和 仪表账户信息
		 */
		for (AuditAccount auditAccount2 : auditAccounts) {
			auditAccount2.setAuditStatus((byte)1);
			if(null!=user){
				auditAccount2.setAuditId(user.getUserId());
				auditAccount2.setAuditName(user.getUserName());
			}
			auditAccount2.setAccountId(room.getAccountId());
			auditAccount2.setAuditOpinion(auditAccount.getAuditOpinion());
			auditAccountMapper.updateByPrimaryKeySelective(auditAccount2);
			
			Meter meter = meterMapper.selectByMeterAddr(auditAccount2.getMeterAddr());
			
			meter.setAlarmId(auditAccount2.getAlarmId());
			meter.setPriceId(auditAccount2.getPriceId());
			meter.setPriceModeCode(auditAccount2.getPriceModeCode());
			meter.setCurrentTimeLimitId(auditAccount2.getCurrentTimeLimitId());
			// 恶性负载设置
			if (!StringUtils.isEmpty(auditAccount2.getPowerChangeLimitId())){
				meter.setPowerChangeLimitId(auditAccount2.getPowerChangeLimitId());
				writeMalignantLoad(meter);
			}
			
			meter.setPowerFluctuationLimitId(auditAccount2.getPowerFluctuationLimitId());
			meter.setPowerLadderLimitId(auditAccount2.getPowerLadderLimitId());
			meter.setPowerMaxLimitId(auditAccount2.getPowerMaxLimitId());
			// 开户审核
			if(auditAccount2.getAuditType()==0){
				meter.setSubAccountStatus((byte)1);//仪表子账户 状态改为使用中
				meter.setStartRead(auditAccount2.getStartRead());
				meter.setStartTime(auditAccount2.getStartTime());
				
				meter.setAccountId(auditAccount2.getAccountId());
				meter.setSubAccountId(auditAccount2.getSubAccountId());
				
				
				meter.setBalanceUpdateRead(auditAccount2.getStartRead());
				meter.setBalanceUpdateTime(auditAccount2.getStartTime());
				meter.setBalance(BigDecimal.ZERO);
			}
			// 续约审核
			else if(auditAccount2.getAuditType()==1){
				meter.setSubAccountStatus((byte)1);//仪表子账户 状态改为使用中
				// 未开始计量的续约
				if (null==meter.getStartTime()){
					meter.setStartRead(auditAccount2.getStartRead());
					meter.setStartTime(auditAccount2.getStartTime());
					meter.setBalanceUpdateRead(auditAccount2.getStartRead());
					meter.setBalanceUpdateTime(auditAccount2.getStartTime());
					meter.setBalance(BigDecimal.ZERO);
				}
			}
			meterMapper.updateByPrimaryKeySelective(meter);
		}
		
	}

	/**
	 * 恶性负载指令下发
	 */
	private void writeMalignantLoad(Meter meter){
		// 查询规则
		RuleElec rule = ruleElecMapper.selectByPrimaryKey(meter.getPowerChangeLimitId());
		if (StringUtils.isEmpty(rule.getSetJson())){
			return ;
		}
		JSONObject jsonMap = JSONObject.parseObject(rule.getSetJson());
		String malignance1Str =(String) jsonMap.get("malignance1");
		// 暂时只考虑回路一的处理
		JSONObject dataMap = JSONObject.parseObject(malignance1Str);
		try{
			String interval = dataMap.getString("interval");
			Map<String, String> requestParam1 = new HashMap<String, String>();
			requestParam1.put("equipNumber", meter.getMeterAddr());
			requestParam1.put("params", interval);
			cmdService.generateCmdUrlNoAddr(ApiTemplateCons.wMalignantLoadInterval, meter.getMeterAddr(), requestParam1);
			String value = dataMap.getString("value");
			Map<String, String> requestParam2 = new HashMap<String, String>();
			requestParam2.put("equipNumber", meter.getMeterAddr());
			requestParam2.put("params", value);
			cmdService.generateCmdUrlNoAddr(ApiTemplateCons.wMalignantLoadValue, meter.getMeterAddr(), requestParam2);
			String delay = dataMap.getString("delay");
			Map<String, String> requestParam3 = new HashMap<String, String>();
			requestParam3.put("equipNumber", meter.getMeterAddr());
			requestParam3.put("params", delay);
			cmdService.generateCmdUrlNoAddr(ApiTemplateCons.wMalignantLoadDelay, meter.getMeterAddr(), requestParam3);
			String offTime = dataMap.getString("offTime");
			Map<String, String> requestParam4 = new HashMap<String, String>();
			requestParam4.put("equipNumber", meter.getMeterAddr());
			requestParam4.put("params", offTime);
			cmdService.generateCmdUrlNoAddr(ApiTemplateCons.wMalignantLoadOffTime, meter.getMeterAddr(), requestParam4);
		}
		catch (Exception e){
			
		}
	}
	
	/**
	 * 驳回
	 */
	@Override
	@Transactional
	public void backAuditRoom(AuditRoom auditRoom) {
		
		// 审核人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())){
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		
		Room room = roomMapper.selectByPrimaryKey(auditRoom.getRoomId());
		
		//新户签约驳回   将房间客户关联关系改为取消状态  新增的客户状态改为已销户
		if(auditRoom.getAuditType() == 0){
			CustomerRoom customerRoom = new CustomerRoom();
			customerRoom.setRoomId(auditRoom.getRoomId());
			customerRoom.setStatus((byte)0);
			List<CustomerRoom> customerRooms = customerRoomMapper.selectByEntity(customerRoom);
			for (CustomerRoom customerRoom2 : customerRooms) {
				
				CustomerRoom customerRoom1 = new CustomerRoom();
				customerRoom1.setCustomerId(customerRoom2.getCustomerId());
				customerRoom1.setStatus((byte)0);
				List<CustomerRoom> customerRooms1 = customerRoomMapper.selectByEntity(customerRoom1);
				if(customerRooms1 == null || customerRooms1.size() <= 1){
					Customer customer = new Customer();
					customer.setCustomerId(customerRoom2.getCustomerId());
					customer.setCustomerStatus((byte)2);//已销户
					customerMapper.updateByPrimaryKeySelective(customer);
				}
				
				customerRoom2.setStatus((byte)2);//取消关联
				customerRoomMapper.updateByPrimaryKeySelective(customerRoom2);
			}
			room.setAccountId("");
		}
		//使用中的 房间水电变更审核
		if(auditRoom.getAuditType() == 3){
			/**
			 * 将驳回的房间账户状态改为 空置状态
			 */
			room.setAccountStatus((byte)1);
		}
		//空置中的房间 水电变更审核
		else{
			/**
			 * 将驳回的房间账户状态改为 空置状态
			 */
			room.setAccountStatus((byte)4);
		}
		
		roomMapper.updateByPrimaryKeySelective(room);
		
		
		
		/**
		 * 1.驳回房间审核 修改审核状态
		 */
		auditRoom.setAuditStatus((byte)2);//驳回
		auditRoom.setAuditId(user.getUserId());
		auditRoom.setAuditName(user.getUserName());
		auditRoom.setUpdateTime(new Date(System.currentTimeMillis()));
		AuditRoomMapper.updateByPrimaryKeySelective(auditRoom);
		
		/**
		 * 2.驳回仪表审核 修改仪表状态
		 */
		List<AuditAccount> auditAccounts = new ArrayList<AuditAccount>();
		List<AuditAccount> elecMeter = new ArrayList<AuditAccount>();
		List<AuditAccount> waterMeter = new ArrayList<AuditAccount>();
		AuditAccount auditAccount = new AuditAccount();
		auditAccount.setAuditStatus((byte)0);
		if(!StringUtils.isEmpty(auditRoom.getElecMeterAddr())){
			auditAccount.setMeterAddr(auditRoom.getElecMeterAddr());
			elecMeter = auditAccountMapper.selectByEntity(auditAccount);
		}
		if(!StringUtils.isEmpty(auditRoom.getWaterMeterAddr())){
			auditAccount.setMeterAddr(auditRoom.getWaterMeterAddr());
			waterMeter = auditAccountMapper.selectByEntity(auditAccount);
		}
		
		if(elecMeter != null && elecMeter.size() > 0){
			auditAccounts.addAll(elecMeter);
		}
		if(waterMeter != null && waterMeter.size() > 0){
			auditAccounts.addAll(waterMeter);
		}
		
		for (AuditAccount auditAccount2 : auditAccounts) {
			/**
			 * 将驳回的仪表子账户状态改为 空置
			 */
			Meter meter = meterMapper.selectByMeterAddr(auditAccount2.getMeterAddr());
			meter.setSubAccountStatus((byte)4);
			meterMapper.updateByPrimaryKeySelective(meter);
			
			
			auditAccount2.setAuditStatus((byte)2);
			if(null!=user){
				auditAccount2.setAuditId(user.getUserId());
				auditAccount2.setAuditName(user.getUserName());
			}
			auditAccount2.setAuditOpinion(auditAccount.getAuditOpinion());
			auditAccountMapper.updateByPrimaryKeySelective(auditAccount2);
		}
		
		
	}

	@Override
	public Map<String, Object> selectAuditShop(AuditRoom auditRoom) {
		
		AuditRoom auditRoom1 = AuditRoomMapper.selectAuditShop(auditRoom);
		Map<String, Object> result = new HashMap<String, Object>();
		String roomId = auditRoom.getRoomId();
		// 查询房间
		Room room = roomMapper.selectByPrimaryKey(roomId);
		room.setElecAllowance(auditRoom1.getElecAllowance());
		room.setElecAllowanceRuleId(auditRoom1.getElecAllowanceRuleId());
		room.setElecAllowanceType(auditRoom1.getElecAllowanceType());
		room.setWaterAllowance(auditRoom1.getWaterAllowance());
		room.setWaterAllowanceRuleId(auditRoom1.getWaterAllowanceRuleId());
		room.setWaterAllowanceType(auditRoom1.getWaterAllowanceType());
		room.setPropertyId(auditRoom1.getPropertyId());
		room.setRentalId(auditRoom1.getRentalId());
		room.setRegionFullName(auditRoom1.getRegionFullName());
		room.setRoomName(auditRoom1.getRoomName());
		room.setRoomNumber(auditRoom1.getRoomNumber());
		room.setStartTime(auditRoom1.getStartTime());
		room.setEndTime(auditRoom1.getEndTime());
		result.put("room", room);
		// 查询关联仪表
		AuditAccount auditAccount = new AuditAccount();
		auditAccount.setRoomId(roomId);
		auditAccount.setAuditVersion(auditRoom.getAuditVersion());
		List<AuditAccount> auditAccounts = auditAccountMapper.selectByEntity(auditAccount);
		result.put("meterList", auditAccounts);
		Customer customer = new Customer();
		customer.setCustomerMobile1(auditRoom1.getCustomerMobile1());
		customer.setCustomerNo(auditRoom1.getCustomerNo());
		customer.setCustomerRealname(auditRoom1.getCustomerRealname());
		result.put("customer", customer);
		
		result.put("auditOpinion", auditRoom1.getAuditOpinion());
		
		/**
		 * 
		 */
		Meter meterParam = new Meter();
		meterParam.setRoomId(roomId);
		List<Meter> meters = meterMapper.selectByEntity(meterParam);
		for (Meter meter : meters) {
			if("110000".equals(meter.getEnergyType())){
				meterParam.setIsElecLoad(meter.getIsElecLoad());
				meterParam.setIsElecPowerMaxLimit(meter.getIsElecPowerMaxLimit());
			}
		}
		result.put("meterParam", meterParam);
		
		return result;
	}

	@Override
	public Map<String, Object> selectOldShopParam(String roomId) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询房间
		Room room = roomMapper.selectRoomAndCust(roomId);
		result.put("room", room);
		Customer customer = new Customer();
		customer.setCustomerMobile1(room.getCustomerMobile1());
		customer.setCustomerNo(room.getCustomerNo());
		customer.setCustomerRealname(room.getCustomerRealname());
		result.put("customer", customer);
		
		/**
		 * 查询仪表信息
		 */
		Meter meterParam = new Meter();
		meterParam.setRoomId(roomId);
		List<Meter> meters = meterMapper.selectByEntity(meterParam);
		for (Meter meter : meters) {
			if("110000".equals(meter.getEnergyType())){
				meterParam.setIsElecLoad(meter.getIsElecLoad());
				meterParam.setIsElecPowerMaxLimit(meter.getIsElecPowerMaxLimit());
			}
		}
		result.put("meters", meters);
		result.put("meterParam", meterParam);
		
		return result;
	}

}
