package cn.com.tw.saas.serv.controller.statistics;

import java.util.List;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.SaasMeter;
import cn.com.tw.saas.serv.service.terminal.SaasMeterService;

@RestController
@RequestMapping("monitor")
@Api(description = "负荷监测接口")
public class LoadMonitorController {

	@Autowired
	private SaasMeterService saasMeterService;
	
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<SaasMeter> saasMeters = saasMeterService.selectLoadMonitorByPage(page);
		page.setData(saasMeters);
		return Response.ok(page);
	}
	
	
	@GetMapping("hbPage")
	public Response<?> selectHbByPage(Page page){
		List<Meter> saasMeters = saasMeterService.selectHbByPage(page);
		page.setData(saasMeters);
		return Response.ok(page);
	}
}
