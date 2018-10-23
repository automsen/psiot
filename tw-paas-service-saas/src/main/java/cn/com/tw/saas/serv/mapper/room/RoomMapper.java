/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.room;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.room.RoomAllowanceInfo;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import feign.Param;

public interface RoomMapper {
    int deleteByPrimaryKey(String roomId);

    int insert(Room record);

    Room selectByPrimaryKey(String roomId);

    int updateByPrimaryKeySelective(Room record);
    
    List<Room> selectByPage(Page page);

    List<RoomAllowanceInfo> selectByPageForAllowance(Page page);
    
    List<Room> selectByEntity(Room record);
    
    int updateForBalance(Room record);
    
    int updateForAllowance(Room record);

	List<Room> selectShopRoomBalanceByPage(Page page);
	
	List<Room> selectDormRoomBalanceByPage(Page page);
	
	List<Room> selectWithCustByPage(Page page);

	List<Room> selectRoomMeterByregionId(String regionId, String orgId);

	void cleanRoomAccount(Room room);
	
	Room selectByRoomId(String roomId);

	List<Room> shopbalanceExport(Room record);

	Room selectRoomAndCust(String roomId);

	List<Room> selectRoomStatusByPage(Page page);

	List<Room> selectIsNotAbandon(Room temp);

	List<Room> selectRoomCustByRegionId(String regionId);

	List<Room> selectRoomMeterByRegionId(String regionId);
	
	List<Room> selectRoomWithThreePhaseByRegionId(Room room);
}