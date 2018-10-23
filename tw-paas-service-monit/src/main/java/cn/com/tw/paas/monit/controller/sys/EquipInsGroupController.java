package cn.com.tw.paas.monit.controller.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.db.org.EquipInsGroup;
import cn.com.tw.paas.monit.service.org.EquipInsGroupService;

@RestController
@RequestMapping("insGroup")
public class EquipInsGroupController {
	
	@Autowired
	private EquipInsGroupService equipInsGroupService;
	
	/**
	 * @return
	 */
	@GetMapping("all")
	public Response<?> selectEquipInsGroupAll(EquipInsGroup equipInsGroup){
		List<EquipInsGroup> equipInsGroups = equipInsGroupService.selectByAll(equipInsGroup);
		return Response.ok(equipInsGroups);
	}

}
