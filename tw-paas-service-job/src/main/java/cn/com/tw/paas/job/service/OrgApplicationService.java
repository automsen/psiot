package cn.com.tw.paas.job.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.tw.common.web.entity.Response;

@FeignClient(name = "service-monit", url="${feign.client.url.paas:}")  
public interface OrgApplicationService {

	@RequestMapping(value="application/{appId}",method=RequestMethod.GET)
	public Response<?> selectByOrgId(@PathVariable("appId") String appId);
}
