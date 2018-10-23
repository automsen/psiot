package cn.com.tw.paas.service.notify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.notify.service.NotifyService;

@RestController
@RequestMapping("notify")
public class NotifyController {
	
	@Autowired
	private NotifyService notifyService;
	
	/**
	 * @param page
	 * @return
	 */
	@RequestMapping("/list/page")
	public Response<?> querymeterList(@RequestBody Page page) {
		try {
			notifyService.selectByPage(page);
			return Response.ok(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.retn(ResultCode.UNKNOW_ERROR);
	}
	
}
