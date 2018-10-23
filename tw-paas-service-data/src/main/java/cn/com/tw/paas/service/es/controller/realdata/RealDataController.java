package cn.com.tw.paas.service.es.controller.realdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.es.service.realdata.RealDataService;

@RestController
@RequestMapping("realdata")
public class RealDataController {
	
	@Autowired
	private RealDataService realDataService;
	
	@GetMapping("page")
	public Response<?> queryRealHistoryDataPage(Page page){
		return Response.ok(realDataService.queryRealHistroyPage(page));
	}
	
	@GetMapping("test")
	public Response<?> test(){
		return Response.ok();
	}

}
