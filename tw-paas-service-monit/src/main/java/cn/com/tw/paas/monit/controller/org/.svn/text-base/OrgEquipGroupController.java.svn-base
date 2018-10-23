package cn.com.tw.paas.monit.controller.org;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.db.org.EquipGroup;
import cn.com.tw.paas.monit.service.org.EquipGroupService;

@RestController
@RequestMapping("equipGroup")
public class OrgEquipGroupController {

	@Autowired
	private EquipGroupService equipGroupService;
	
	/**
	 * 根据终端地址查询
	 * @param commAddr
	 * @return
	 */
	@GetMapping("{commAddr}")
	public Response<?> selectEquipGroupByCommAddr(@PathVariable String commAddr){
		
		EquipGroup equipGroup = equipGroupService.selectByCommAddr(commAddr);
		return Response.ok(equipGroup);
	}
	
	/**
	 * @return
	 */
	@GetMapping("all")
	public Response<?> selectEquipGroupAll(EquipGroup equipGroup){
		List<EquipGroup> equipGroups = equipGroupService.selectAll(equipGroup);
		return Response.ok(equipGroups);
	}
	
	/**
	 * 新增仪表和网关额关联关系
	 */
	@PostMapping()
	public Response<?> insertMeterBindingGateway(@RequestBody List<EquipGroup> equipGroupList){
		int resNum = 0;
		try {
			resNum = equipGroupService.insertSelect(equipGroupList);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.UNKNOW_ERROR, "未知错误");
		}
		if(resNum == 1){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "无参数");
		}
		return Response.ok();
	}
	
	/**
	 * 删除仪表和网关的关联关系
	 */
	@PostMapping("delete")
	public Response<?> deleteMeterAndGatewayRelationship(@RequestBody EquipGroup equipGroup){
		int resNum = 0;
		try {
			resNum = equipGroupService.deleteMeterAndGatewayRelationship(equipGroup);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(resNum == 1){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "删除失败");
		}
		return Response.ok();
	}
}
