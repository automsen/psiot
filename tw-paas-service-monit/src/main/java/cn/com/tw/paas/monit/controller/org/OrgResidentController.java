package cn.com.tw.paas.monit.controller.org;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.db.org.OrgResident;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.service.org.OrgResidentService;

@RestController
@RequestMapping("resident")
public class OrgResidentController {

	@Autowired
	private OrgResidentService orgResidentService;
	
	/**
	 * 根据实体属性查询集合
	 * @param resident
	 * @return
	 */
	@PostMapping("all")
	public Response<?> selectResidentByEntity(@RequestBody OrgResident resident){
		List<OrgResident> residentList = orgResidentService.selectResidentByEntity(resident);
		return Response.ok(residentList);
	}
	
	@PostMapping("/location")
	public Response<?> selectResidentByLocatioId(@RequestBody TerminalEquip terminalEquip){
		OrgResident resident = orgResidentService.selectResidentByLocatioId(terminalEquip);
		return Response.ok(resident);
	}
}
