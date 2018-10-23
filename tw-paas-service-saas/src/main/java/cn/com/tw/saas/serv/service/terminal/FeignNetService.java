package cn.com.tw.saas.serv.service.terminal;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.tw.common.web.entity.Response;
@Deprecated
@FeignClient(name = "service-monit", url="${feign.client.url.monit:}")  
public interface FeignNetService {
	
	@RequestMapping(value="usepaas/netinfo",method=RequestMethod.GET)
	Response<?> selectAbnorDate(
			@RequestParam("equipNumber") String equipNumber
			,@RequestParam("appKey")String appKey,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("businessNo") String businessNo,
			@RequestParam("sign") String sign
			);
	@RequestMapping(value="usepaas/likenet",method=RequestMethod.GET)
	Response<?> selectLikeDate(
			@RequestParam("equipNumber") String equipNumber
			,@RequestParam("appKey")String appKey,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("businessNo") String businessNo,
			@RequestParam("sign") String sign
			);
}
