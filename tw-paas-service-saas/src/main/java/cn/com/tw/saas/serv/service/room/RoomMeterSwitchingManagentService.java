package cn.com.tw.saas.serv.service.room;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.RoomMeterSwitchingManagent;
/**
 * 
 * 电表通断管理接口
 *
 */
public interface RoomMeterSwitchingManagentService {
	/**
	 * 房屋分页查询
	 * @param arg0
	 * @return
	 */
	List<RoomMeterSwitchingManagent> selectByPage(Page arg0);

	 /**
	  * 根据登陆人和楼栋查询
	  * @param regionId
	  * @param orgId
	  * @return
	  */
	 List<RoomMeterSwitchingManagent> selectRoomSwitchingManagentByregionId(String regionId, String orgId);
	 
	 
	 /**
	  * 电表通断查询实体
	  * @param record
	  * @return
	  */
	 List<RoomMeterSwitchingManagent>  selectRoomMeterSwitchingManagentByEntity(RoomMeterSwitchingManagent roomMeterSwitchingManagent);
	 

	 /**
	  * 通电通断分页查询
	  * @param page
	  * @return
	  */
	 List<RoomMeterSwitchingManagent>  selectRoomMeterSwitchingManagentByPage (Page page);
}
