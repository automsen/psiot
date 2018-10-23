package cn.com.tw.saas.serv.service.room.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.RoomMeterSwitchingManagent;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMeterSwitchingManagentMapper;
import cn.com.tw.saas.serv.service.room.RoomMeterSwitchingManagentService;

/**
 * 
 * 电表通断管理接口实现
 *
 */
@Service
public class RoomMeterSwitchingManagentServiceImpl implements
		RoomMeterSwitchingManagentService {

	@Autowired
	private RoomMeterSwitchingManagentMapper roomMeterSwitchingManagentMapper;
	
	
	/**
	 * 电表通断管理分页查询实现方法
	 */
	@Override
	public List<RoomMeterSwitchingManagent> selectByPage(Page arg0) {
		return roomMeterSwitchingManagentMapper.selectByPage(arg0);
	}

	/**
	 * 电表通断管理通过楼栋号和登陆人查询的实现方法
	 */
	@Override
	public List<RoomMeterSwitchingManagent> selectRoomSwitchingManagentByregionId(
			String regionId, String orgId) {
		return roomMeterSwitchingManagentMapper.selectRoomSwitchingManagentByregionId(regionId, orgId);
	}

	/**
	 * 电表通断查询实体的实现方法
	 */
	@Override
	public List<RoomMeterSwitchingManagent> selectRoomMeterSwitchingManagentByEntity(
			RoomMeterSwitchingManagent roomMeterSwitchingManagent) {
		return roomMeterSwitchingManagentMapper.selectRoomMeterSwitchingManagentByEntity(roomMeterSwitchingManagent);
	}
	/**
	 * 电表通断的分页查询方法
	 */
	@Override
	public List<RoomMeterSwitchingManagent> selectRoomMeterSwitchingManagentByPage(
			Page page) {
		return roomMeterSwitchingManagentMapper.selectRoomMeterSwitchingManagentByPage(page);
	}
	
	
	
}
