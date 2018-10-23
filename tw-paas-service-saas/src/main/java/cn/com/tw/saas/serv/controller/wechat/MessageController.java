package cn.com.tw.saas.serv.controller.wechat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.business.BusinessNotice;
import cn.com.tw.saas.serv.service.business.BusinessNoticeService;

/**
 * 事件记录查询
 * @author admin
 *
 */
@RestController
@RequestMapping("wechat/message")
public class MessageController {
	@Autowired
	private BusinessNoticeService sysEventService;
	
	@PostMapping("loadEventList")
	public Response<?> loadEventList(@RequestBody Page page){
		List<BusinessNotice> list = null;
		try {
			list = sysEventService.selectByPage(page);
			page.setData(list);
			
		} catch (Exception e) {
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统错误！");
		}
		return Response.ok(page);
	}
}
