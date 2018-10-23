/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.room;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.room.RoomHis;

import java.util.List;

public interface RoomHisMapper {

    int deleteByPrimaryKey(String id);

    int insert(RoomHis record);

    List<RoomHis> selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RoomHis record);
    
    List<RoomHis> selectByPage(Page page);
    
    List<RoomHis> selectByEntity(RoomHis record);

	void insert(Room room);

	RoomHis selectByEntity1(RoomHis roomHis1);

	RoomHis selectPriceByRoomId(String roomId);
}