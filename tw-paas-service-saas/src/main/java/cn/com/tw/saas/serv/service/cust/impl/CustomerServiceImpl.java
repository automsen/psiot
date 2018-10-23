package cn.com.tw.saas.serv.service.cust.impl;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.RegExp;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.excel.SaasCustomerExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.mapper.cust.CustomerMapper;
import cn.com.tw.saas.serv.mapper.cust.CustomerRoomMapper;
import cn.com.tw.saas.serv.mapper.org.OrgUserMapper;
import cn.com.tw.saas.serv.service.cust.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private CustomerRoomMapper customerRoomMapper;
	@Autowired
	private OrgUserMapper orgUserMapper;

	@Override
	public int deleteById(String arg0) {
		
		CustomerRoom customerRoom = new CustomerRoom();
		customerRoom.setCustomerId(arg0);
		List<CustomerRoom> customerRooms = customerRoomMapper.selectByEntity(customerRoom);
		/**
		 * 判断客户是否与房间绑定过  绑定过则将客户修改为销户 没有就直接删除
		 */
		if(customerRooms != null && customerRooms.size() > 0){
			for(CustomerRoom customerRoom1 : customerRooms){
				if(customerRoom1.getStatus()==(byte)1){
					throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "客户已与房间绑定");
				}
			}	
			Customer customer = new Customer();
			customer.setCustomerId(arg0);
			customer.setCustomerStatus((byte)2);//销户
			customerMapper.updateByPrimaryKeySelective(customer);
		}else{
			customerMapper.deleteByPrimaryKey(arg0);
		}
		return 0;
	}

	@Override
	public String insertOne(Customer cust) {
		return insert(cust);
	}

	@Override
	@Transactional
	public String importInsert(SaasCustomerExcel cust) {
		Customer customer = new Customer();
		customer.setCustomerEmail(cust.getCustomerEmail());
		customer.setCustomerMobile1(cust.getCustomerMobile1());
		customer.setCustomerQq(cust.getCustomerQq());
		if(!StringUtils.isEmpty(cust.getCustomerRealname())){
			customer.setCustomerRealname(cust.getCustomerRealname().trim());
		}
		/**
		 * 判断用户类型  0-商户 存1810 1-学生 存1820 2-教师 存1830
		 */
		String custType = "";
		if("0".equals(cust.getCustomerType())){
			custType = "1810";
		}else if("1".equals(cust.getCustomerType())){
			custType = "1820";
		}else if("2".equals(cust.getCustomerType())){
			custType = "1830";
		}
		customer.setCustomerType(custType);
		/**
		 * 性别为空默认为男
		 */
		if(StringUtils.isEmpty(cust.getCustomerSex())){
			customer.setCustomerSex((byte) 1);
		}
		if (cust.getCustomerSex().equals("1")){
			customer.setCustomerSex((byte) 1);
		}
		else if (cust.getCustomerSex().equals("0")){
			customer.setCustomerSex((byte) 0);
		}
		
		/**
		 * 身份标识为空默认为手机号
		 */
		if(StringUtils.isEmpty(cust.getCustomerNo())){
			customer.setCustomerNo(cust.getCustomerMobile1());
		}else{
			customer.setCustomerNo(cust.getCustomerNo());
		}
		
		return insert(customer);
	}

	private String insert(Customer cust) {
		// 操作员信息
		OrgUser orgUser = new OrgUser();
		try{
			JwtInfo info = JwtLocal.getJwt();
			orgUser = orgUserMapper.selectByPrimaryKey(info.getSubject());
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		int result = 0;
		cust.setOrgId(orgUser.getOrgId());
		
		Customer param = new Customer();
		param.setOrgId(orgUser.getOrgId());
		param.setCustomerNo(cust.getCustomerNo());
		List<Customer> custList1 = customerMapper.selectByEntity(param);
		if(null!=custList1&&custList1.size()>0){
			return "客户标识重复";
		}
		param = new Customer();
		param.setOrgId(orgUser.getOrgId());
		param.setCustomerMobile1(cust.getCustomerMobile1());
		//根据机构 和手机号判未销户客户的重复
		List<Customer> custList = customerMapper.selectRepeatCustomer(param);
		if(null!=custList&&custList.size()>0){
			return "手机号重复";
		}
		
		if(StringUtils.isEmpty(cust.getCustomerNo())){
			cust.setCustomerNo(cust.getCustomerMobile1());
		}else if(!cust.getCustomerNo().matches(RegExp.wordNumberUnderlineRungExp)){
			return "客户标识格式错误";
		}
		cust.setCustomerStatus((byte) 1);
		cust.setOperatorId(orgUser.getUserId());
		cust.setOperatorName(orgUser.getUserRealName());
		cust.setCustomerPassword(DigestUtils.md5Hex("123456".getBytes()));
		result = customerMapper.insert(cust);
		if (result == 1) {
			return "ok";
		}
		else {
			return "未知异常";
		}
	}

	@Override
	public Customer selectById(String arg0) {
		// TODO Auto-generated method stub
		return customerMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<Customer> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return customerMapper.selectByPage(arg0);
	}
	
	@Override
	public List<Customer> selectByRoom(CustomerRoom param) {
		// TODO Auto-generated method stub
		return customerMapper.selectByRoom(param);
	}

	@Override
	public int update(Customer arg0) {
		
		Customer customer = customerMapper.selectByPrimaryKey(arg0.getCustomerId());
		if(!arg0.getCustomerMobile1().equals(customer.getCustomerMobile1())){
			Customer param = new Customer();
			param.setCustomerMobile1(arg0.getCustomerMobile1());
			List<Customer> custList = customerMapper.selectByEntity(param);
			if(null!=custList&&custList.size()>0){
				throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "手机号已存在");
			}
		}
		if(StringUtils.isEmpty(arg0.getCustomerNo())){
			arg0.setCustomerNo(arg0.getCustomerMobile1());
		}else if(!arg0.getCustomerNo().matches(RegExp.wordNumberUnderlineRungExp)){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "客户标识格式错误");
		}
		
		//如果修改了客户标识
		if(!customer.getCustomerNo().equals(arg0.getCustomerNo())){
			Customer param = new Customer();
			param.setOrgId(arg0.getOrgId());
			param.setCustomerNo(arg0.getCustomerNo());
			List<Customer> custList = customerMapper.selectByEntity(param);
			if(custList!=null&&custList.size()>0){
				throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "客户标识已存在");
			}
		}
		
		
		return customerMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public int countByEntity(Customer cust) {
		return customerMapper.countByEntity(cust);
	}

	@Override
	public List<Customer> selectCustomerAccountBalance(Page page) {
		// TODO Auto-generated method stub
		return customerMapper.selectCustomerAccountBalance(page);
	}

	@Override
	public List<Customer> selectByEntity(Customer customer) {
		// TODO Auto-generated method stub
		return customerMapper.selectByEntity(customer);
	}
	
	@Override
	public List<Customer> selectByLikeEntity(Customer customer) {
		// TODO Auto-generated method stub
		return customerMapper.selectByLikeEntity(customer);
	}
	
	@Override
	public List<Customer> selectByCustRoomParam(CustRoomParam param){
		List<Customer> custList = customerMapper.selectByCustRoomParam(param);
		return custList;
	}
	
	/**
	 *  防止密码暴露。在前端MD5
	 */
	@Override
	public void updateCustPass(Customer customer) throws Exception{
		if(StringUtils.isEmpty(customer.getCustomerId())){
			throw new Exception("数据异常！");
		}
		customerMapper.updateByPrimaryKeySelective(customer);
	}

	@Override
	public Customer selectByPhone(Customer param) {
		
		return customerMapper.selectByPhone(param);
	}

	@Override
	public Customer selectByCustomerMobile1(String customerMobile1) {
		// TODO Auto-generated method stub
		return customerMapper.selectByCustomerMobile1(customerMobile1);
	}		

}
