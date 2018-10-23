/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.cust;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.SubAccount;
import cn.com.tw.saas.serv.entity.room.Room;

public interface SubAccountMapper {
    int deleteByPrimaryKey(String subAccountId);

    int insert(SubAccount record);
    
    SubAccount selectByPrimaryKey(String subAccountId);

    int updateForBalance(SubAccount record);
    
    int updateByPrimaryKeySelective(SubAccount record);

	List<SubAccount> selectByPage(Page page);
    
    List<SubAccount> selectByEntity(SubAccount record);

	List<Room> selectCustomerByPage(Page page);

}