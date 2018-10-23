package cn.com.tw.paas.monit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.business.org.TerminalEquipExpand;
import cn.com.tw.paas.monit.service.equipment.OrgEquipmentService;
import cn.com.tw.paas.monit.service.index.IndexService;
import cn.com.tw.paas.monit.service.org.TerminalEquipService;

@ApiIgnore
@RestController
@RequestMapping("")
public class IndexController {
	
	@Autowired
	private IndexService indexService;
	
	@Autowired
	private OrgEquipmentService orgEquipmentService;
	
	@Autowired
	private TerminalEquipService terminalEquipService;
	
	@RequestMapping("index")
	public String index() {
		return "paasIndex";
	}
	
	/**
	 * 查询总设备数量
	 * @return
	 */
	@GetMapping("selceTotalDevice")
	public Response<?> selceTotalDevice(){
		List<TerminalEquipExpand> equipNums = terminalEquipService.selectCountNum();
		return Response.ok(equipNums);
	}
	
}
