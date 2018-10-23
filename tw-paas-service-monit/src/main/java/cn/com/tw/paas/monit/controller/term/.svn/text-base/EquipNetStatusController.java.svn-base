package cn.com.tw.paas.monit.controller.term;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.EquipNetStatusExpand;
import cn.com.tw.paas.monit.service.org.EquipNetStatusService;

/**
 * 终端网关 状态查询
 * @author Administrator
 *
 */
@RestController
@RequestMapping("status")
public class EquipNetStatusController {

	@Autowired
	private EquipNetStatusService equipNetStatusService;
	
	/**
	 * 终端状态页面查询
	 * @param page
	 * @return
	 */
	@GetMapping("terminalPage")
	public Response<?> selectByPage(Page page){
		List<EquipNetStatusExpand> equipNetStatusExpands = equipNetStatusService.selectEquipNetStatusByPage(page);
		page.setData(equipNetStatusExpands);
		return Response.ok(page);
	}
	
	/**
	 * 网关页面查询
	 * @param page
	 * @return
	 */
	@GetMapping("netPage")
	public Response<?> selectNetByPage(Page page){
		List<EquipNetStatusExpand> equipNetStatusExpands = equipNetStatusService.selectNetByPage(page);
		page.setData(equipNetStatusExpands);
		return Response.ok(page);
	}
	
	@PutMapping("/{commAddr}/{status}")
	public Response<?> updateStatus(@PathVariable String commAddr, @PathVariable String status){
		equipNetStatusService.updateStatus(commAddr, status);
		return Response.ok();
	}
}
