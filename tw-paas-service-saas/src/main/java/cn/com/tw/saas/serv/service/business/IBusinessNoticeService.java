package cn.com.tw.saas.serv.service.business;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;

@FeignClient(name = "service-notify", url="${feign.client.url.monit:}")  
public interface IBusinessNoticeService {
	
	@RequestMapping(value="notify/list/page",method=RequestMethod.GET)
	Response<?> selectNotifyByPage(@RequestBody Page page);
}
