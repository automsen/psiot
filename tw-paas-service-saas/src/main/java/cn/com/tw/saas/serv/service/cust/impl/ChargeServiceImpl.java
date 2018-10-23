package cn.com.tw.saas.serv.service.cust.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.core.jms.cons.PsMqCons;
import cn.com.tw.common.enm.notify.NotifyBusTypeEm;
import cn.com.tw.common.enm.notify.NotifyLvlEm;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.notify.Notify;
import cn.com.tw.saas.serv.common.utils.cons.ApiTemplateCons;
import cn.com.tw.saas.serv.common.utils.cons.SysCommons;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.db.cust.OrderRecharge;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.entity.db.cust.SubAccountRecord;
import cn.com.tw.saas.serv.entity.db.cust.ThirdOrder;
import cn.com.tw.saas.serv.entity.org.Org;
import cn.com.tw.saas.serv.entity.org.OrgAccountRecord;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.cust.CustomerMapper;
import cn.com.tw.saas.serv.mapper.cust.OrderRechargeMapper;
import cn.com.tw.saas.serv.mapper.cust.RoomAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.cust.SubAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.cust.ThirdOrderMapper;
import cn.com.tw.saas.serv.mapper.org.OrgAccountRecordMapper;
import cn.com.tw.saas.serv.mapper.org.OrgMapper;
import cn.com.tw.saas.serv.mapper.org.OrgPayConfigMapper;
import cn.com.tw.saas.serv.mapper.org.OrgUserMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.command.CmdRecordService;
import cn.com.tw.saas.serv.service.cust.ChargeService;

import com.alibaba.fastjson.JSONObject;

@Service
public class ChargeServiceImpl implements ChargeService {
	
	
	//private final static Logger logger = LoggerFactory.getLogger(ChargeServiceImpl.class);
	
	@Autowired
	private ThirdOrderMapper thirdOrderMapper;
	@Autowired
	private RoomAccountRecordMapper roomAccountRecordMapper;
	@Autowired
	private SubAccountRecordMapper subAccountRecordMapper;
	@Autowired
	private RoomMapper roomMapper;
	@Autowired
	private MeterMapper meterMapper;

	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private OrgAccountRecordMapper orgAccountRecordMapper;
	@Autowired
	private OrgMapper orgMapper;
	@Autowired
	private MqHandler mqHandler;
	@Autowired
	private CmdRecordService cmdService;

	
	@Autowired
	private OrgUserMapper orgUserMapper;
	
	@Autowired
	private OrderRechargeMapper orderRechargeMapper;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	/**
	 * 页面充值
	 */
	@Override
	@Transactional
	public Map<String, Object> charge(CustRoomParam param) {
		String roomId = param.getRoomId();
		BigDecimal roomChargeMoney = param.getRoomChargeMoney();
		// 查询房间
		Room room = roomMapper.selectByPrimaryKey(roomId);
		/**
		 * 银联充值的 添加银行备注信息
		 */
		if("1904".equals(param.getPayModeCode())){
			room.setRemarks(param.getRemarks());
		}
		// 有客户信息的充值
		if (!StringUtils.isEmpty(param.getCustomerId())) {
			String custId = param.getCustomerId();
			// 查询客户
			Customer cust = customerMapper.selectByPrimaryKey(custId);
			charge(cust, room, roomChargeMoney, null,param.getPayModeCode());
		} 
		// 无客户信息的充值
		else {
			charge(null, room, roomChargeMoney, null,param.getPayModeCode());
		}

		return null;
	}

	/**
	 * 创建微信订单
	 * 
	 * @param thirdOrder
	 */
	@Override
	@Transactional
	public ThirdOrder createWechatOrder(ThirdOrder thirdOrder) {
		// 充值金额，单位（元）
		BigDecimal money = thirdOrder.getMoney();
		String meterAddr = thirdOrder.getMeterAddr();
		String customerId = thirdOrder.getCustomerId();
		// 微信订单
		thirdOrder.setThirdType("wechat");
		// 查询客户信息
		Customer cust = customerMapper.selectByPrimaryKey(customerId);
		// 查询仪表信息
		Meter meter = meterMapper.selectByMeterAddr(meterAddr);
		// 查询房间信息
		Room room = roomMapper.selectByPrimaryKey(meter.getRoomId());
		thirdOrder.setOrderId(CommUtils.getUuid());
		thirdOrder.setOrgId(cust.getOrgId());
		thirdOrder.setCustomerId(customerId);
		thirdOrder.setCustomerMobile(cust.getCustomerMobile1());
		thirdOrder.setRoomId(room.getRoomId());
		thirdOrder.setMoney(money);
		// 初始状态
		thirdOrder.setStatus((byte) 0);
		// 自定义
		OrderRecharge orderRecharge = OrderRecharge.generateOrder(room, cust, null, money, meter);
		if(!StringUtils.isEmpty(cust.getOrgId())){
			Org org = orgMapper.selectByPrimaryKey(cust.getOrgId());
			orderRecharge.setOrgName(org.getOrgName());
		}
		orderRecharge.setPayModeCode("1902");
		orderRecharge.setExternalNo(thirdOrder.getOrderId());
		orderRecharge.setStatus(new Byte("0"));  // 初始状态
		orderRecharge.setCreateTime(new Date());
		orderRecharge.setOperatorId(cust.getCustomerId());
		orderRecharge.setOperatorName(cust.getOperatorName());
		orderRecharge.setOrderSource(SysCommons.orderSource.wechat_order);
		orderRecharge.setOperatorType(SysCommons.operatorType.customer_user);  // 系统操作员订单
		orderRechargeMapper.insert(orderRecharge);
		//系统订单ID。
		thirdOrder.setOutTradeNo(orderRecharge.getOrderId());
		thirdOrderMapper.insert(thirdOrder);
		return thirdOrder;
	}

	/**
	 * 微信充值成功确认
	 * 
	 * @param order
	 * @return
	 */
	@Override
	@Transactional
	public String wechatChargeSuccess(ThirdOrder order) {
		ThirdOrder oldOrder = thirdOrderMapper.selectByPrimaryKey(order.getOrderId());
		if (oldOrder == null) {
			return "订单不存在";
		}
		if (oldOrder.getStatus() != 0) {
			return "订单已完成";
		}
		// 微信下单金额为分
		BigDecimal money = order.getMoney().divide(new BigDecimal(100));
		String meterAddr = oldOrder.getMeterAddr();
		String customerId = oldOrder.getCustomerId();
		// 微信订单
		order.setThirdType("wechat");
		// 查询客户信息
		Customer cust = customerMapper.selectByPrimaryKey(customerId);
		// 查询仪表信息
		Meter meter = meterMapper.selectByMeterAddr(meterAddr);
		// 查询房间信息
		Room room = roomMapper.selectByPrimaryKey(meter.getRoomId());
		order.setMoney(money);
		// 初始状态
		order.setStatus((byte) 1);
		thirdOrderMapper.updateByPrimaryKeySelective(order);
		order.setOutTradeNo(oldOrder.getOutTradeNo());
		charge(cust, room, money, order,null);
		return "ok";
	}

	/**
	 * 微信充值失败确认
	 * 
	 * @param order
	 * @return
	 */
	@Override
	@Transactional
	public String wechatChargeFail(ThirdOrder order) {
		ThirdOrder oldOrder = thirdOrderMapper.selectByPrimaryKey(order.getOrderId());
		OrderRecharge orderRecharge = orderRechargeMapper.selectByPrimaryKey(oldOrder.getOutTradeNo());
		if (oldOrder == null || orderRecharge == null) {
			return "订单不存在";
		}
		orderRecharge = new OrderRecharge();
		orderRecharge.setOrderId(oldOrder.getOutTradeNo());
		orderRecharge.setStatus((byte) 2);
		orderRechargeMapper.updateByPrimaryKeySelective(orderRecharge);
		order.setStatus((byte) 2);
		thirdOrderMapper.updateByPrimaryKeySelective(order);
		return "ok";
	}

	private void charge(Customer cust, Room room, BigDecimal roomChargeMoney, ThirdOrder third,String payModel) {
		// 更新账户余额  
		room.setBalance(roomChargeMoney);
		roomMapper.updateForBalance(room);
		/**
		 * 银联备注
		 */
		String remarks = "";
		if(!StringUtils.isEmpty(payModel) ){
			if("1904".equals(payModel)){
				remarks = room.getRemarks();
			}
		}
		room = roomMapper.selectByPrimaryKey(room.getRoomId());
		// 操作员信息
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (info!= null && !StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
			/**
			 * 通过用户ID查出用户信息
			 */
			if(!StringUtils.isEmpty(user.getUserId())){
				user = orgUserMapper.selectByPrimaryKey(user.getUserId());
				if(!StringUtils.isEmpty(user.getOrgId())){
					Org org = orgMapper.selectByPrimaryKey(user.getOrgId());
					user.setOrgName(org.getOrgName());
				}
			}
		}
		// 保存账户记录
		RoomAccountRecord record = RoomAccountRecord.generateRecord(room, cust, user, roomChargeMoney);
		record.setRemark("房间充值");
		record.setOrderTypeCode("1");
		/**
		 *  第三方支付订单已经生成，只更新订单状态，和更新时间
		 */
		OrderRecharge order =  new OrderRecharge();
		if(third != null){
			// 查询订单
			order.setOrderId(third.getOutTradeNo()); // 第三方订单ID
			// 保证金额正确
			order.setMoney(roomChargeMoney);
			// 更新订单状态
			order.setOrderId(third.getOutTradeNo());
			order.setUpdateTime(new Date());
			order.setStatus(new Byte("1"));
			// 充值用户操作员指向客户
			record.setOrderId(third.getOutTradeNo());
			record.setPayModeCode("1902"); // 微信支付
			record.setOperatorId(cust.getCustomerId());
			record.setOperatorName(cust.getCustomerRealname());
			record.setOperatorType(SysCommons.operatorType.customer_user);// 客户订单
			orderRechargeMapper.updateByPrimaryKeySelective(order);
		}else{
			// 生成新的充值订单
			order = OrderRecharge.generateOrder(room, cust, user, roomChargeMoney,null);
			order.setOrderSource(SysCommons.orderSource.web_order);
			order.setStatus(new Byte("1"));
			order.setOperatorType(SysCommons.operatorType.system_user);  // 系统操作员订单
			order.setRemarks(remarks);
			if(!StringUtils.isEmpty(payModel) ){
				order.setPayModeCode(payModel );
			}else{
				order.setPayModeCode("1901");
			}
			record.setOperatorType(SysCommons.operatorType.system_user);// 系统操作员订单
			record.setPayModeCode(order.getPayModeCode() );
			record.setOrderId(order.getOrderId());
			orderRechargeMapper.insert(order);
		}
		roomAccountRecordMapper.insert(record);
		// 更新机构余额
		Org org = orgMapper.selectByPrimaryKey(room.getOrgId());
		if (org.getBalance() != null) {
			org.setBalance(org.getBalance().add(roomChargeMoney));
		} else {
			org.setBalance(roomChargeMoney);
		}
		orgMapper.updateByPrimaryKeySelective(org);
		/**
		 * 机构账户记录生成
		 */
		OrgAccountRecord accountRecord = OrgAccountRecord.createOrgAccountRecord(room, cust, null, roomChargeMoney,
				org.getBalance());
		accountRecord.setOrderTypeCode("1");
		orgAccountRecordMapper.insert(accountRecord);
		
		/**
		 *  清空key 
		 */
		try {
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.setValueSerializer(new StringRedisSerializer());
			redisTemplate.afterPropertiesSet();
			ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
			String cacheKey1 = SysCommons.AlarmCache.alarmHeader1+room.getRoomId();
			String cacheKey2 = SysCommons.AlarmCache.alarmHeader2+room.getRoomId();
			// 清除1级预警
			String alarm1 = valueOps.get(cacheKey1);
			if(StringUtils.isNotBlank(alarm1)){
				if(redisTemplate.hasKey(cacheKey1)){
					redisTemplate.delete(cacheKey1);
				}
			}
			// 清除2级预警
			String alarm2 = valueOps.get(cacheKey2);
			if(StringUtils.isNotBlank(alarm2)){
				if(redisTemplate.hasKey(cacheKey2)){
					redisTemplate.delete(cacheKey2);
				}
			}
		} catch (Exception e) {
		}
		// 房间余额大于0
		if (room.getBalance().compareTo(BigDecimal.ZERO) > 0) {
			// 查询仪表子账户(系统计费)
			Meter meterParam = new Meter();
			meterParam.setRoomId(room.getRoomId());
			meterParam.setSubAccountStatus((byte) 1);
			meterParam.setIsCostControl((byte) 0);
			List<Meter> meters = meterMapper.selectByEntity(meterParam);
			// 总余额
			BigDecimal totalBalance = BigDecimal.ZERO;
			for (Meter meter : meters) {
				// 子账户余额小于0
				if (meter.getBalance().compareTo(BigDecimal.ZERO) < 0) {
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
					RoomAccountRecord record2 = RoomAccountRecord.generateRecord(room, cust, null, transferOutMoney);
					record2.setRemark("仪表子账户扣除");
					record2.setOrderTypeCode("2");// 2、子账户扣除
					roomAccountRecordMapper.insert(record2);
					// 子账户
					meter.setBalance(transferInMoney);
					meterMapper.updateForBalance(meter);
					meter = meterMapper.selectByPrimaryKey(meter.getMeterId());
					SubAccountRecord record3 = SubAccountRecord.generateRecord(meter, room, cust, null,
							transferInMoney);
					record3.setRemark("房间账户转移");
					record3.setOrderTypeCode("1");
					subAccountRecordMapper.insert(record3);
					// 按每个仪表的余额单独控制通电的逻辑
					// 仪表余额大于等于0 接通
//					if (meter.getBalance().compareTo(BigDecimal.ZERO) >= 0) {
//						Map<String, String> requestMap = new HashMap<String, String>();
//						requestMap.put("equipNumber", meter.getMeterAddr());
//						PageCmdResult result = cmdService.generateCmd(ApiTemplateCons.switchOn, meter.getMeterAddr(),
//								requestMap);
//					}
				}
				// 按房间总余额一同控制通电的逻辑
				totalBalance = totalBalance.add(meter.getBalance());
			}
			room = roomMapper.selectByPrimaryKey(room.getRoomId());
			totalBalance = totalBalance.add(room.getBalance());
			if (totalBalance.compareTo(BigDecimal.ZERO)>0){
				for (Meter meter : meters){
					Map<String, String> requestMap = new HashMap<String, String>();
					requestMap.put("equipNumber", meter.getMeterAddr());
					cmdService.generateCmd(ApiTemplateCons.switchOn, meter.getMeterAddr(),
							requestMap);
				}
			}
		}

		/**
		 * 添加通知记录
		 */
		/*
		 * BusinessNotice businessNotice = new BusinessNotice();
		 * businessNotice.setCustomerId(cust.getCustomerId());// 客户ID
		 * businessNotice.setCustomerMobile1(cust.getCustomerMobile1());// 客户电话
		 * businessNotice.setCustomerRealname(cust.getCustomerRealname());//
		 * 客户姓名 businessNotice.setCustomerNo(cust.getCustomerNo());// 客户身份标识
		 * 
		 * businessNotice.setRoomId(room.getRoomId());// 房间ID
		 * businessNotice.setRoomName(room.getRoomName());// 房间名
		 * businessNotice.setRoomNumber(room.getRoomNumber());// 房间编号
		 * businessNotice.setOrgId(room.getOrgId());// 机构ID
		 * 
		 * businessNotice.setNoticeStatus((byte) 0);// 通知状态
		 * businessNotice.setBusinessName("房间充值");// 业务名称
		 * businessNotice.setBusinessType("1601");// 业务类型 1601充值
		 */
		if(null!=cust){
			
			/**
			 *  充值模版
			 *  尊敬的${name}，你在${time}为${roomname}成功缴纳${type}，金额为${change}元。
			 *  模版编号  SMS_143862974
			 */
			//塞入队列发短信
			String orgId = room.getOrgId();
			JSONObject contentJson = new JSONObject();
			String customerName = cust.getCustomerRealname();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
			if(StringUtils.isBlank(customerName)){
				customerName = cust.getCustomerMobile1();
			}
			contentJson.put("name", customerName);
			contentJson.put("roomname", room.getRoomName());
			contentJson.put("type", "水电费");
			contentJson.put("time", sdf.format(new Date()));
			contentJson.put("change",roomChargeMoney.toString());
			String contentStr = Notify.createAliSms( cust.getCustomerMobile1(), "SMS_143862974", 
					contentJson.toJSONString(), SysCommons.AlarmCache.signature).
					setSaasBusData(NotifyBusTypeEm.recharge, null, null, null, orgId, NotifyLvlEm.HIGH)
			.toJsonStr();
			mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, contentStr);
//			String content = Notify
//					.createSms(cust.getCustomerMobile1(), NotifyNum.RECHARGE_WARN.getValue(),
//							room.getRoomName() + "," + roomChargeMoney + "," + room.getBalance())
//					.setSaasBusData(NotifyBusTypeEm.recharge, "", cust.getCustomerId(), cust.getCustomerRealname(),
//							room.getOrgId(), NotifyLvlEm.HIGH)
//					.setSaasExt(room.getRoomId(), room.getRoomName()).toJsonStr();
//
//			mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, content);
		}
		// businessNoticeMapper.insert(businessNotice);
	}

	/**
	 *  充值撤销
	 */
	@Transactional
	@Override
	public void repeal(String recordId)throws Exception {
		if(StringUtils.isEmpty(recordId)){
			throw new Exception();
		}
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		RoomAccountRecord record = roomAccountRecordMapper.selectByPrimaryKey(recordId);
		// 扣除账户余额
		BigDecimal money = record.getMoney();
		// 转换为负值
		money = BigDecimal.ZERO.subtract(money);
		// 更新充值记录状态为撤销
		RoomAccountRecord tempRecord = new RoomAccountRecord();
		
		tempRecord.setId(record.getId());
		tempRecord.setStatus(new Byte("3")); 
		tempRecord.setUpdateTime(new Date());
		tempRecord.setRemark("充值已撤销！撤销时间："+sdf.format(new Date()));
		roomAccountRecordMapper.updateByPrimaryKeySelective(tempRecord);
		
		// 更新充值订单表
		if(!StringUtils.isEmpty(record.getOrderId())){
			OrderRecharge orderRecharge  = new OrderRecharge();
			orderRecharge.setStatus((byte)4);
			orderRecharge.setUpdateTime(new Date());
			orderRecharge.setOrderId(record.getOrderId());
			orderRechargeMapper.updateByPrimaryKeySelective(orderRecharge);
		}
		
		Room tempRoom = roomMapper.selectByPrimaryKey(record.getRoomId());
		// 新增一条负数的流水
		RoomAccountRecord kfRecord = new RoomAccountRecord();
		
		BeanUtils.copyProperties(record, kfRecord); // 复制基础数据
		//设置新的ID
		kfRecord.setId(CommUtils.getUuid());
		kfRecord.setOrderTypeCode("5"); // 撤销
		kfRecord.setCreateTime(new Date());
		kfRecord.setMoney(money);
		kfRecord.setBalance(tempRoom.getBalance().add(money));
		kfRecord.setRemark("充值已撤销！退费金额："+money.toString());
		roomAccountRecordMapper.insert(kfRecord);
		
		Room room = new Room();
		room.setRoomId(record.getRoomId());
		room.setBalance(money);
		// 更新账号余额
		roomMapper.updateForBalance(room);
		
		/**
		 * 撤销金额存入 机构帐号中
		 */
		CustomerRoom customerRoom = new CustomerRoom();
		customerRoom.setRoomId(record.getRoomId());
		customerRoom.setOrgId(tempRoom.getOrgId());
		customerRoom.setStatus((byte)1);
		Customer cust = new Customer();
		List<Customer> customers = customerMapper.selectByRoom(customerRoom);
		cust = customers.get(0);
		
		OrgAccountRecord accountRecord = OrgAccountRecord.createOrgAccountRecord(tempRoom, cust, null, money,
				tempRoom.getBalance().add(money));
		accountRecord.setOrderTypeCode("3");
		orgAccountRecordMapper.insert(accountRecord);
		
		// 更新机构余额
		Org org = orgMapper.selectByPrimaryKey(tempRoom.getOrgId());
		if (org.getBalance() != null) {
			org.setBalance(org.getBalance().add(money));
		} else {
			org.setBalance(money);
		}
		orgMapper.updateByPrimaryKeySelective(org);
	}

	
	

}