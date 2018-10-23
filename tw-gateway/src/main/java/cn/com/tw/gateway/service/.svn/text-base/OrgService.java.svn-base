package cn.com.tw.gateway.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.tw.common.web.entity.Response;

@FeignClient(name = "service-monit", url="${feign.client.url.monit:}")
public interface OrgService {
	
	/**
	 * 
	 * @param equipNumber
	 * @return
	 */
	@RequestMapping(value = "application/apk", method = RequestMethod.GET)
	Response<?> selectByApk(@RequestParam("appKey") String appKey);
	
}