/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.cust;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;

public interface CustomerRoomMapper {
    int deleteByPrimaryKey(String id);

    int insert(CustomerRoom record);

    CustomerRoom selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CustomerRoom record);
    
    List<CustomerRoom> selectByEntity(CustomerRoom param);

    List<CustRoomParam> selectByPage(Page page);
    
    List<RoomAccountRecord> roomAccountRecordExpert(RoomAccountRecord record);

	List<CustomerRoom> selectRoomCustByCustomerId(CustomerRoom custRoom);
}