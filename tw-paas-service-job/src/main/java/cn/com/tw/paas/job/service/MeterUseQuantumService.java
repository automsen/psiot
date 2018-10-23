package cn.com.tw.paas.job.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.tw.common.web.entity.Response;


@FeignClient(name = "service-saas", url="${feign.client.url.saas:}")  
public interface MeterUseQuantumService {

	
	@RequestMapping(value="quantum/meterUse",method=RequestMethod.POST)
	public Response<?> meterUseQuantumJob(@RequestParam("format") String format);
	
	@RequestMapping(value="quantum/meterUseDayAndMonthAndYear",method=RequestMethod.POST)
	public Response<?> meterUseDayAndMonthAndYearJob(@RequestParam("format") String format);
}
