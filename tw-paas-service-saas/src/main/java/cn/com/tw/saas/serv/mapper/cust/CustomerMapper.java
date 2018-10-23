package cn.com.tw.saas.serv.mapper.cust;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;

public interface CustomerMapper {
	
    int deleteByPrimaryKey(String customerId);

    int insert(Customer record);

    Customer selectByPrimaryKey(String customerId);

    int updateByPrimaryKeySelective(Customer record);

	List<Customer> selectByPage(Page arg0);
	
	List<Customer> selectByLikeEntity(Customer customer);
	
	List<Customer> selectByEntity(Customer customer);
	
	List<Customer> selectByRoom(CustomerRoom customer);
	
	List<Customer> selectByCustRoomParam(CustRoomParam param);
	
	int countByEntity(Customer customer);

	// TODO 需要重写
	List<Customer> selectCustomerAccountBalance(Page page);

	void shopInsert(Customer cust);

	List<Customer> selectRepeatCustomer(Customer param);

	Customer selectByPhone(Customer param);

	Customer selectByCustomerMobile1(String customerMobile1);
	
}