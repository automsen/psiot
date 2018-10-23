package cn.com.tw.saas.serv.service.room.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.saas.serv.entity.room.RoomHis;
import cn.com.tw.saas.serv.mapper.room.RoomHisMapper;
import cn.com.tw.saas.serv.service.room.RoomHisService;

@Service
public class RoomHisServiceImpl  implements RoomHisService{

	@Autowired
	private RoomHisMapper roomHisMapper;

	@Override
	public RoomHis selectByEntity1(RoomHis roomHis1) {
		// TODO Auto-generated method stub
		return roomHisMapper.selectByEntity1(roomHis1);
	}

	@Override
	public RoomHis selectPriceByRoomId(String roomId) {
		// TODO Auto-generated method stub
		return roomHisMapper.selectPriceByRoomId(roomId);
	}

	
	
	
	


	

}
