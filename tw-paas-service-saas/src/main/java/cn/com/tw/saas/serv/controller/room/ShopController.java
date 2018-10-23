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

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.service.room.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("shop")
@Api(description = "商铺管理接口")
public class ShopController {

	@Autowired
	private RoomService roomService;
	
	@ApiOperation(value = "分页查询关联客户", notes = "")
	@GetMapping("page/withCust")
	@ResponseBody
	public Response<?> selectWithCustByPage(Page page) {
		List<Room> list = null;
		try {
			list = roomService.selectWithCustByPage(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}
	
	@ApiOperation(value = "列表查询未签约商铺", notes = "")
	@GetMapping("list/noCust")
	@ResponseBody
	public Response<?> selectNoCust(String regionId) {
		List<Room> list = null;
		try {
			Room param = new Room();
			param.setRegionId(regionId);
			param.setRoomUse("1710");
			param.setAccountId("");
			param.setAccountStatus((byte) 4);//查出所有房间状态为空置的 房间
			list = roomService.selectByEntity(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(list);
	}
	
	
	@ApiOperation(value = "新户签约", notes = "")
	@PutMapping("sign")
	public Response<?> sign(@RequestBody Map<String,Object> paramMap) {
		try {
			roomService.submitAuditForSign(paramMap);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		
		return Response.ok();
	}
	
	@ApiOperation(value = "续约", notes = "")
	@PutMapping("renewal")
	public Response<?> renewal(@RequestBody Map<String,Object> paramMap) {
		try {
			roomService.submitAuditForRenewal(paramMap);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	} 
}
