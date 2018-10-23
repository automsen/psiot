/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.cust;

import java.util.List;

import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccount;
import cn.com.tw.saas.serv.entity.view.serve.RoomExtend;

public interface RoomAccountMapper {
    int deleteByPrimaryKey(String accountId);

    int insert(RoomAccount record);

    RoomAccount selectByPrimaryKey(String accountId);

    int updateByPrimaryKeySelective(RoomAccount record);
    
    int updateOnlyBalance(RoomAccount record);
    
    RoomAccount selectByRoomId(String getpRoomId);
    
    List<RoomAccount> selectByEntity(RoomAccount record);
    /**
     * 查询房间客户账户扩展信息
     * @param param
     * @return
     */
    List<RoomExtend> selectForExtendList(CustRoomParam param);
    
    /**
     * 查询房间客户账户扩展信息
     * 包括无客户使用的房间
     * @param param
     * @return
     */
    List<RoomExtend> selectForAllExtendList(CustRoomParam param);
    
}