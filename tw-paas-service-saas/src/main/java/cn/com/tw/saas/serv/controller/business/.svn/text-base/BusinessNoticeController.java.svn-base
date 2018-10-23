package cn.com.tw.saas.serv.controller.business;

import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.enm.notify.NotifyPlatTypeEm;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.service.business.IBusinessNoticeService;

@RestController
@RequestMapping("business")
@Api(description = "业务通知")
public class BusinessNoticeController {
	
	/*@Autowired
	private BusinessNoticeService businessNoticeService;*/
	
	@Autowired
	private IBusinessNoticeService businessNoticeService;
	
	@GetMapping("page")
	@ApiOperation(value = "业务通知列表", notes = "")
	public Response<?> selectByPage(Page page) {
		Map<String, Object> params = (Map<String, Object>) page.getParamObj();
		params.put("platType", NotifyPlatTypeEm.saas.getValue());
		Response<?> result = businessNoticeService.selectNotifyByPage(page);
		return result;
	}

}
