package cn.com.tw.paas.monit.controller.sys;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.service.org.CmdExeService;

/**
 * 命令
 * @author Administrator
 *
 */
@RestController
@RequestMapping("cmdExe")
public class CmdExeController {

	@Autowired
	private CmdExeService cmdExeService;
	
	@GetMapping("equip/{id}")
	public Response<?>  selectEquipGroup(@PathVariable String id){
		return cmdExeService.selectEquipGroup(id);
	}
	
	@GetMapping("{id}")
	public Response<?>  selectAll(@PathVariable String id){
		CmdExe cmdExe = cmdExeService.selectById(id);
		return Response.ok(cmdExe);
	}
	
	@GetMapping("ins/{id}")
	public Response<?>  selectEquipInsGroup(@PathVariable String id){
		return cmdExeService.selectEquipInsGroup(id);
	}
	
}
