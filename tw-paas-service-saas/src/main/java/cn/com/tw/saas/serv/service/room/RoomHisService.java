package cn.com.tw.saas.serv.service.room;

import cn.com.tw.saas.serv.entity.room.RoomHis;

public interface RoomHisService {

	RoomHis selectByEntity1(RoomHis roomHis1);

	RoomHis selectPriceByRoomId(String roomId);
	
}
