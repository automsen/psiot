package cn.com.tw.saas.serv.controller.room;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.RoomAllowanceInfo;
import cn.com.tw.saas.serv.service.room.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("dorm")
@Api(description = "宿舍管理接口")
public class DormController {

	@Autowired
	private RoomService roomService;
	
	@ApiOperation(value = "水电管理", notes = "")
	@PutMapping("manage")
	public Response<?> update(@RequestBody Map<String,Object> paramMap) {
		int result = 0;
		roomService.submitAuditForUpdate(paramMap);
		return Response.ok(result);
	} 
	
	@ApiOperation(value = "分页查询房间和补助信息", notes = "")
	@GetMapping("page/allowance")
	public Response<?> selectByPage(Page page) {
		List<RoomAllowanceInfo> list = null;
		try {
			list = roomService.selectByPageForAllowance(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}
	
}
