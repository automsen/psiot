package cn.com.tw.saas.serv.service.cust;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.excel.SaasCustomerExcel;

public interface CustomerService{

	String importInsert(SaasCustomerExcel cust);
	
	void updateCustPass(Customer customer) throws Exception;

	List<Customer> selectCustomerAccountBalance(Page page);

	List<Customer> selectByEntity(Customer customer);

	int deleteById(String arg0);

	Customer selectById(String arg0);

	int update(Customer arg0);

	String insertOne(Customer cust);

	List<Customer> selectByPage(Page arg0);

	/**
	 * 客户号与手机号判重
	 * @param cust
	 * @return
	 */
	int countByEntity(Customer cust);

	List<Customer> selectByCustRoomParam(CustRoomParam param);

	List<Customer> selectByLikeEntity(Customer customer);

	List<Customer> selectByRoom(CustomerRoom param);

	Customer selectByPhone(Customer param);
	/**
	 * 根据手机号来查询客户的信息
	 * @param customerMobile1
	 * @return
	 */
	Customer selectByCustomerMobile1(String customerMobile1);
}
