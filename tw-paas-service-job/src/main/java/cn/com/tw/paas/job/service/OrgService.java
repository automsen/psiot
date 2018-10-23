package cn.com.tw.paas.job.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.tw.common.web.entity.Response;

@FeignClient(name = "service-monit", url="${feign.client.url.paas:}")  
public interface OrgService {

	@RequestMapping(value="org/all",method=RequestMethod.GET)
	public Response<?> all();
}
