package cn.com.tw.paas.monit.controller.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.PushLogExpand;
import cn.com.tw.paas.monit.service.org.PushLogService;

@RestController
@RequestMapping("pushLog")
public class PushLogController {
	
	@Autowired
	private PushLogService pushLogService;
	
	/**
	 * 推送日志
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectPushLogPage(Page page){
		List<PushLogExpand> pushLogs = pushLogService.selectPushLogPage(page);
		page.setData(pushLogs);
		return Response.ok(page);
	}
	
	/**
	 * 推送日志
	 * @param page
	 * @return
	 */
	@GetMapping("show")
	public Response<?> selectPushLogShow(Page page){
		pushLogService.selectPushLogShow(page);
		return Response.ok(page);
	}

}
