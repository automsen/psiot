package cn.com.tw.paas.job.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.job.entity.CmdExe;
import cn.com.tw.paas.job.entity.InsExe;

/**
 * 命令指令服务
 * 
 * @author liming
 * 2018年8月24日 15:35:16
 *
 */
@FeignClient(name = "service-monit", url="${feign.client.url.paas:}")  
public interface CmdExeService {

	@RequestMapping(value="cmd/updateCmdStatus",method=RequestMethod.POST)
	public Response<?> updateCmdStatus(@RequestBody CmdExe cmd);
	
	@RequestMapping(value="cmd/findCmdIns",method=RequestMethod.POST)
	public Response<?> findCmdIns(@RequestParam("cmdId") String cmdId);
	
}
