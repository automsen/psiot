package cn.com.tw.paas.monit.service.org;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.db.read.ReadHistory;

@FeignClient(name="service-data",url="${feign.client.url.queue:}")
public interface AbnormalMeterPushService {
	
	/**
	 * 通过QUEUE取回仪表参数
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "read/last/multi", method = RequestMethod.POST)
	public Response<?> selectMeterDataToHB(@RequestBody ReadHistory rh);
}
