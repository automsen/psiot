package cn.com.tw.paas.monit.controller.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.ApiLogExpand;
import cn.com.tw.paas.monit.service.org.ApiLogService;

/**
 * api日志
 * @author Administrator
 *
 */
@RestController
@RequestMapping("apiLog")
public class ApiLogController {
	
	@Autowired
	private ApiLogService apiLogService;
	
	/**
	 * api日志页面
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectApiLogPage(Page page){
		List<ApiLogExpand> apiLogs = apiLogService.selectApiLogExpandByPage(page);
		page.setData(apiLogs);
		return Response.ok(page);
	}
	
	@GetMapping("show")
	public Response<?> selectApiLogShow(Page page){
		apiLogService.selectApiLogExpandByShow(page);
		return Response.ok(page);
	}

}
