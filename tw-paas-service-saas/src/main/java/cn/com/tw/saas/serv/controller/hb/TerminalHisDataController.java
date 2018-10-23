package cn.com.tw.saas.serv.controller.hb;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.service.hb.TerminalHisDataService;

@RestController
@RequestMapping("his")
@Api(description = "设备历史数据")
public class TerminalHisDataController {

	@Autowired
	private TerminalHisDataService terminalHisDataService;
	
	@GetMapping("page")
	public Response<?> selectTerminalHisDataPage(Page page){
		return Response.ok(terminalHisDataService.selectTerminalHisDataPage(page));
	}
	
	
	
}
