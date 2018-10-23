/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.cust;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;

public interface RoomAccountRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(RoomAccountRecord record);

    RoomAccountRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RoomAccountRecord record);

	List<RoomAccountRecord> selectByPage(Page page);
	
	List<RoomAccountRecord> selectByPage2(Page page);
	
	List<RoomAccountRecord> selectByEntity(RoomAccountRecord record);
	
	Map<String, Object> count(Map<String, Object> param);

	List<RoomAccountRecord> selectAccountRecord(String userId);
	
	List<RoomAccountRecord> selectNewAccountRecord(RoomAccountRecord roomAccountRecord); 
	
	List<RoomAccountRecord> roomAccountRecordExpert(RoomAccountRecord record);
	
	List<RoomAccountRecord> selectBySourceAnalysis(Page page);
	
	List<RoomAccountRecord> selectBySourceExport(RoomAccountRecord roomAccountRecord);
	
	RoomAccountRecord selectOneRecordByCondition(RoomAccountRecord roomAccountRecord);
	
	List<RoomAccountRecord> selectRecordsByRoomId(String roomId);

}