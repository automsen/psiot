/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.room;

import java.util.List;

import cn.com.tw.saas.serv.entity.room.AllowanceRecord;

public interface AllowanceRecordMapper {
    int deleteByPrimaryKey(String id);

    int deleteByOrderId(String orderId);
    
    int insert(AllowanceRecord record);

    AllowanceRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AllowanceRecord record);
    
    List<AllowanceRecord> selectByRoomId(String roomId);

    List<AllowanceRecord> selectByOrderId(String orderId);

	List<AllowanceRecord> selectByRoomIdAndStatus(String roomId);
	
	List<AllowanceRecord> selectWaitExecuteToday();
}