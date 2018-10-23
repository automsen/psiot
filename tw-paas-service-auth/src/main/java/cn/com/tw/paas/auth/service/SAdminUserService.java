package cn.com.tw.paas.auth.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.tw.common.web.entity.Response;

@FeignClient(name = "service-saas", url="${feign.client.url.monit:}")  
public interface SAdminUserService {
	
	@RequestMapping(value="sadmin/name/{name}",method=RequestMethod.GET)
	public Response<?> queryUserByName(@PathVariable("name") String name);

}
