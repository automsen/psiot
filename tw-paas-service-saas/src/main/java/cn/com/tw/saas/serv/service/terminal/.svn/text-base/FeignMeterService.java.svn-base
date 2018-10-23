package cn.com.tw.saas.serv.service.terminal;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.tw.common.web.entity.Response;
@Deprecated
@FeignClient(name = "service-monit", url="${feign.client.url.monit:}")  
public interface FeignMeterService {

	/**
	 * 仪表编号精确查询
	 * @param equipNumber
	 * @param appKey
	 * @param timestamp
	 * @param businessNo
	 * @param sign
	 * @return
	 */
	
	@RequestMapping(value="usepaas/terminalinfo",method=RequestMethod.GET)
	Response<?> selectAbnorDate(
			@RequestParam("equipNumber") String equipNumber
			,@RequestParam("appKey")String appKey,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("businessNo") String businessNo,
			@RequestParam("sign") String sign
			);
	
	/**
	 * 仪表编号模糊查询
	 * @param equipNumber
	 * @param pages
	 * @param appKey
	 * @param timestamp
	 * @param businessNo
	 * @param sign
	 * @return
	 */
	@RequestMapping(value="usepaas/likemeter",method=RequestMethod.GET)
	Response<?> selectLikeData(
			@RequestParam("equipNumber") String equipNumber,@RequestParam("pages") String pages
			,@RequestParam("appKey")String appKey,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("businessNo") String businessNo,
			@RequestParam("sign") String sign
			);
}
