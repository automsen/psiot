package cn.com.tw.saas.serv.controller.audit;

import java.util.Map;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.entity.audit.AuditRoom;
import cn.com.tw.saas.serv.service.audit.AuditRoomService;

@RestController
@RequestMapping("auditShop")
@Api(description = "商铺审核")
public class AuditShopController {

	@Autowired
	private AuditRoomService auditRoomService;
	
	/**
	 * 详情查看
	 * @param auditRoom
	 * @return
	 */
	@GetMapping("detail")
	public Response<?> selectAuditShop(AuditRoom auditRoom){
		Map<String,Object> result = auditRoomService.selectAuditShop(auditRoom);
		return Response.ok(result);
	}
	
	/**
	 * 查找旧的商铺参数
	 * @param roomId
	 * @return
	 */
	@GetMapping("oldShop")
	public Response<?> selectOldShopParam(String roomId){
		Map<String,Object> result = auditRoomService.selectOldShopParam(roomId);
		return Response.ok(result);
	}
	
}
