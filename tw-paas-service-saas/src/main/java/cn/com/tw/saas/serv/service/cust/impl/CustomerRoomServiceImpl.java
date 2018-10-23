package cn.com.tw.saas.serv.service.cust.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.excel.CustomerRoomExcel;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.cust.CustomerMapper;
import cn.com.tw.saas.serv.mapper.cust.CustomerRoomMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.cust.CustomerRoomService;

@Service
public class CustomerRoomServiceImpl implements CustomerRoomService{
	@Autowired
	private CustomerRoomMapper customerRoomMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private RoomMapper roomMapper;
	@Autowired
	private MeterMapper meterMapper;
	@Override
	public List<CustRoomParam> selectByPage(Page page){
		return customerRoomMapper.selectByPage(page);
	}
	/**
	 * 保存导入数据
	 */
	@Override
	@Transactional
	public String importTempExcel(CustomerRoomExcel customerRoomExcel, String regionId, String orgId) {
		String roomNumber = customerRoomExcel.getRoomNumber();
		Customer cust = new Customer();
		cust.setOrgId(orgId);
		cust.setCustomerMobile1(customerRoomExcel.getCustomerPhone());
		// 查询是否有手机号匹配的客户
		List<Customer> custList = customerMapper.selectByEntity(cust);
		if (null == custList ||custList.size()<=0 ) {
			return "无匹配客户信息";
		} else {
			cust = custList.get(0);
		}
		Room room = new Room();
		room.setOrgId(orgId);
		room.setRegionId(regionId);
		room.setRoomNumber(roomNumber);
		// 房间是否存在
		List<Room> roomList = roomMapper.selectByEntity(room);
		if (roomList == null || roomList.size() <= 0) {
			return "无匹配房间信息";
		} else {
			room = roomList.get(0);
		}
		// 重复校验
		CustomerRoom custRoom = new CustomerRoom();
		custRoom.setCustomerId(cust.getCustomerId());
		custRoom.setRoomId(room.getRoomId());
		List<CustomerRoom> custRooms = customerRoomMapper.selectByEntity(custRoom);
		if (custRooms == null || custRooms.size() == 0) {
			// 房间和客户都存在时，关联客户——房间
			RelateCustomerRoom(cust, room);
		} else {
			return "该客户与房间关系重复";
		}
		return "ok";
	}
	
	/**
	 * 新增客户和房间的关联
	 * 页面接口
	 */
	@Override
	@Transactional
	public int RelateCustomerRoom(CustRoomParam param){
		// 查询客户
		Customer cust = new Customer();
		cust.setOrgId(param.getOrgId());
		cust.setCustomerMobile1(param.getCustomerMobile1());
		// 查询是否有手机号匹配的客户
		List<Customer> custList = customerMapper.selectByEntity(cust);
		if (null != custList && custList.size() > 0 ) {
			cust = custList.get(0);
		}else{
			return 3;
		}
		// 查询房间
		Room room = new Room();
		room = roomMapper.selectByPrimaryKey(param.getRoomId());
		CustomerRoom custRoom = new CustomerRoom();
		custRoom.setRoomAccountId(room.getAccountId());
		custRoom.setRoomId(param.getRoomId());
		List<CustomerRoom> custRoomList = customerRoomMapper.selectByEntity(custRoom);
		if (null==custRoomList||custRoomList.size()==0){
			return RelateCustomerRoom(cust, room);
		}
		else {// 重复数据
			return 0;
		}
		
	}
	
	/**
	 * 取消房间客户关联
	 * @param param
	 * @param user
	 * @return
	 */
	@Override
	@Transactional
	public int CancelRelateCustomerRoom(CustRoomParam param){
		// 查询客户房间关系
		CustomerRoom custRoom = new CustomerRoom();
		custRoom.setOrgId(param.getOrgId());
		custRoom.setRoomId(param.getRoomId());
		custRoom.setStatus((byte) 1);
		List<CustomerRoom> custRoomList = customerRoomMapper.selectByEntity(custRoom);
		// 无有效数据
		if (null==custRoomList||custRoomList.size()==0){
			return 0;
		}
		
		custRoom.setCustomerId(param.getCustomerId());
		custRoomList = customerRoomMapper.selectByEntity(custRoom);
		// 应只有一条记录
		custRoom = custRoomList.get(0);
		/**
		 * 取消关联的客户状态改为 空置中
		 */
		Customer customer = new Customer();
		customer.setCustomerId(param.getCustomerId());
		
		CustomerRoom customerRoom = new CustomerRoom();
		customerRoom.setCustomerId(custRoom.getCustomerId());
		List<CustomerRoom> custRooms = customerRoomMapper.selectRoomCustByCustomerId(customerRoom);
		//客户只关联一个房间的 情况下可以修改客户状态
		if(custRooms.size() <= 1){
			customer.setCustomerStatus((byte)1);
		}else{
			customer.setCustomerStatus((byte)0);
		}				
		customerMapper.updateByPrimaryKeySelective(customer);
		
		//取消关联的房间account_Id改为空字符串
		Room room=new Room();
		room.setRoomId(param.getRoomId());
		room.setAccountId("");
		roomMapper.updateByPrimaryKeySelective(room);
		
		return customerRoomMapper.deleteByPrimaryKey(custRoom.getId());
		
		// 房间下有多个客户，只取消客户房间关联
		/*else if (custRoomList.size()>1){
			// 取消客户房间关联
			custRoom.setCustomerId(param.getCustomerId());
			custRoomList = customerRoomMapper.selectByEntity(custRoom);
			// 应只有一条记录
			custRoom = custRoomList.get(0);
			return customerRoomMapper.deleteByPrimaryKey(custRoom.getId());
		}
		// 房间下只有一个客户，走结算流程
		// TODO
		else if(custRoomList.size() == 1){
			return 0;
		}
		else return 0;*/
	}

	/**
	 * 关联客户——房间
	 * 
	 * @param custId
	 * @param roomId
	 * @param user
	 */
	private int RelateCustomerRoom(Customer cust, Room room) {
		// 房间——客户关系
		CustomerRoom relation = new CustomerRoom();
		relation.setOrgId(room.getOrgId());
		relation.setCustomerId(cust.getCustomerId());
		relation.setRoomId(room.getRoomId());
		// 房间账户未激活
		if (StringUtils.isEmpty(room.getAccountId())){
			// 激活房间账户
			room.setAccountId(room.generateAccountId());
			Room roomParam = new Room();
			roomParam.setRoomId(room.getRoomId());
			roomParam.setAccountId(room.getAccountId());
			//roomParam.setAccountStatus((byte) 1);
			roomParam.setBalance(BigDecimal.ZERO);
			roomMapper.updateByPrimaryKeySelective(roomParam);
			// 查询房间已关联仪表
			Meter param = new Meter();
			param.setRoomId(room.getRoomId());
			param.setOrgId(room.getOrgId());
			List<Meter> meterList = meterMapper.selectByEntity(param);
			if (null != meterList && meterList.size() > 0) {
				for (Meter temp : meterList) {
					initMeterSubAccount(temp, room);
				}
			}
		}
		relation.setRoomAccountId(room.getAccountId());
		// 关系已激活
		relation.setStatus((byte) 1);
		/**
		 * 修改客户状态为 0使用中
		 */
		cust.setCustomerStatus((byte)0);
		customerMapper.updateByPrimaryKeySelective(cust);
		
		return customerRoomMapper.insert(relation);
	}

	/**
	 * 预激活仪表子账户
	 * 
	 * @param meter
	 * @param roomAccount
	 * @param user
	 */
	private void initMeterSubAccount(Meter meter, Room room) {
		Meter param = new Meter();
		param.setMeterId(meter.getMeterId());
		param.setAccountId(room.getAccountId());
		param.setSubAccountStatus((byte) 0);
		param.setSubAccountId(param.generateSubAccountId());
		param.setBalance(BigDecimal.ZERO);
		// 商户初始化
//		if (room.getRoomUse().equals("1710")){
//			param.setSubAccountStatus((byte) 0);
//		}
//		// 宿舍初始化
//		else if (room.getRoomUse().equals("1720")){
//			param.setSubAccountStatus((byte) 1);
//			param.setStartRead(meter.getInstallRead());
//			param.setStartTime(new Date());
//			param.setBalanceUpdateRead(meter.getInstallRead());
//			param.setBalanceUpdateTime(new Date());
//		}
		meterMapper.updateByPrimaryKeySelective(param);
	}
	
	@Override
	@Transactional
	public void updateCustomerRoom(CustRoomParam param) {
		
		/**
		 * 将修改钱的客户状态 改为停用中
		 */
		Customer customer = new Customer();
		customer.setCustomerId(param.getCustomerId());
		customer.setCustomerStatus((byte)1);
		customerMapper.updateByPrimaryKeySelective(customer);
		
		
		// 查询客户
		Customer cust = new Customer();
		cust.setOrgId(param.getOrgId());
		cust.setCustomerMobile1(param.getCustomerMobile1());
		// 查询是否有手机号匹配的客户
		List<Customer> custList = customerMapper.selectByEntity(cust);
		if (null != custList && custList.size() > 0 ) {
			cust = custList.get(0);
		}else{
			throw new BusinessException(MonitResultCode.DATA_EXISTS_NULL,"客户不存在"); 
		}
		
		/**
		 * 修改后的客户使用状态为 使用中
		 */
		cust.setCustomerStatus((byte)0);
		customerMapper.updateByPrimaryKeySelective(cust);
		
		CustomerRoom customerRoom = customerRoomMapper.selectByPrimaryKey(param.getRoomCustId());
		customerRoom.setCustomerId(cust.getCustomerId());
		customerRoom.setRoomId(param.getRoomId());
		customerRoomMapper.updateByPrimaryKeySelective(customerRoom);
	}
	
	
	@Override
	public List<CustomerRoom> selectByEntity(CustomerRoom param) {
		return customerRoomMapper.selectByEntity(param);
	}
	
	
}
