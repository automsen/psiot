package cn.com.tw.paas.service.queue.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.tw.common.web.entity.Response;

@FeignClient(name = "service-monit", url="${feign.client.url.monit:}")  
public interface CacheService {
	
	@RequestMapping(value="cache/ins/{code}", method=RequestMethod.GET)
	public Response<?> insCache(@PathVariable("code") String code);
	
	@RequestMapping(value="cache/cmd/{code}", method=RequestMethod.GET)
	public Response<?> cmdCache(@PathVariable("cmd") String cmd);
	
	@RequestMapping(value="cache/itemAll", method=RequestMethod.GET)
	public Response<?> itemAll();
	
	@GetMapping("modbus/items")
	public Response<?> modbusItems(@RequestParam("equipmentNumber") String equipmentNumber);
}
