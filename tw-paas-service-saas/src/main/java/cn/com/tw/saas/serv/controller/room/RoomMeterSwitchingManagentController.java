package cn.com.tw.saas.serv.controller.room;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.RoomMeterSwitchingManagent;
import cn.com.tw.saas.serv.service.room.RoomMeterSwitchingManagentService;

@RestController
@RequestMapping("roomMeterSM")
@Api(description ="电表通断管理")
public class RoomMeterSwitchingManagentController {

	@Autowired
	private RoomMeterSwitchingManagentService  roomMeterSwitchingManagentService;

	@ApiOperation(value = "获取电表通断管理列表", notes = "")
	@GetMapping("page")
	@ResponseBody
	public Response<?> selectRoomMeterSwitchingManagentByPage(Page page) {
		List<RoomMeterSwitchingManagent> list = null;
		try {
			list = roomMeterSwitchingManagentService.selectRoomMeterSwitchingManagentByPage(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}
	
	
	
	@ApiOperation(value = "通过楼栋ID查询电表通断状态", notes = "")
	@GetMapping("getregion/{regionId}")
	public Response<?> selectByProomId(@PathVariable String regionId) {
		if (StringUtils.isEmpty(regionId)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		List<RoomMeterSwitchingManagent> list = null;
		RoomMeterSwitchingManagent param = new RoomMeterSwitchingManagent();
		param.setRegionId(regionId);
		try {
			list = roomMeterSwitchingManagentService.selectRoomMeterSwitchingManagentByEntity(param);
			if (list == null) {
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "该楼栋下面无房间");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		return Response.ok(list);
	}
	
	
	@GetMapping("")
	public Response<?> selectSaasMeter1(RoomMeterSwitchingManagent roomMeterSwitchingManagent) {
		List<RoomMeterSwitchingManagent> rooms = roomMeterSwitchingManagentService.selectRoomMeterSwitchingManagentByEntity(roomMeterSwitchingManagent);
		return Response.ok(rooms);
	}
}
