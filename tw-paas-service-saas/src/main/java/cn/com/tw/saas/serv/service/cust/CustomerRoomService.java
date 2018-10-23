package cn.com.tw.saas.serv.service.cust;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.excel.CustomerRoomExcel;

public interface CustomerRoomService {

	String importTempExcel(CustomerRoomExcel customerRoomExcel, String regionId, String orgId);

	int RelateCustomerRoom(CustRoomParam param);

	int CancelRelateCustomerRoom(CustRoomParam param);

	List<CustRoomParam> selectByPage(Page page);
	
	List<CustomerRoom> selectByEntity(CustomerRoom param);
	void updateCustomerRoom(CustRoomParam param);

}
