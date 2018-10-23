package cn.com.tw.paas.service.queue.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;

@FeignClient(name = "service-monit", url="${feign.client.url.monit:}")  
public interface TerminalService {
	
	@GetMapping("/terminal/number/{equipNum}")
	public Response<?> selectByEquipNum(@PathVariable("equipNum") String equipNum);
	
	@GetMapping("terminal/page")
	public Response<?> selectTerminalEquipPage(Page page);
	
	@PutMapping("status/{commAddr}/{status}")
	public Response<?> updateStatus(@PathVariable("commAddr") String commAddr, 
			@PathVariable("status") String status);

}
