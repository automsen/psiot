package cn.com.tw.saas.serv.controller.room;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.service.room.RoomService;

/**
 * 房态管理
 * @author Administrator
 *
 */
@RestController
@RequestMapping("roomStatus")
@Api(description = "房间状态管理接口")
public class RoomStatusManageController {

	@Autowired
	private RoomService roomService;
	
	
	@ApiOperation(value = "获取房间状态管理列表", notes = "")
	@GetMapping("page")
	@ResponseBody
	public Response<?> selectRoomStatusByPage(Page page) {
		List<Room> list = null;
		try {
			list = roomService.selectRoomStatusByPage(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}
	
	
	@ApiOperation(value = "修改房间状态", notes = "")
	@PutMapping()
	@ResponseBody
	public Response<?> update(@RequestBody Room newRoom){
		try {
			//电水表都没有的情况不允许 修改为空置
			if(StringUtils.isEmpty(newRoom.geteMeterAddr()) && StringUtils.isEmpty(newRoom.getwMeterAddr())){
				return Response.retn(MonitResultCode.DATA_EXISTS_NULL, "房间未关联仪表！！");
			}
			
			roomService.updateRoomStatus(newRoom);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		
		return Response.ok();
	}
	
}
