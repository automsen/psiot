package cn.com.tw.engine.core.ws;

import java.util.List;

import org.quartz.CronExpression;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.engine.core.entity.DataItemGroup;
import cn.com.tw.engine.core.entity.Meter;
import cn.com.tw.engine.core.utils.DataUtils;

@RestController
@RequestMapping("cache")
public class CacheController {
	
	@GetMapping("clear/{dataItemNum}")
	public Response<?> clearCache(@PathVariable String dataItemNum){
		
		DataUtils.deleteData(dataItemNum);
		
		return Response.ok();
		
	}
	
	@GetMapping("{dataItemNum}")
	public Response<?> getCache(@PathVariable String dataItemNum){
		
		if ("all".equals(dataItemNum)){
			
			Object obj = DataUtils.readDataAll();
			
			if (obj == null){
				return Response.retn(ResultCode.OPERATION_IS_SUCCESS,"data is null", obj);
			}
			return Response.ok(obj);
		}
		
		Object obj = DataUtils.readDataByKey(dataItemNum);
		
		if (obj == null){
			return Response.retn(ResultCode.OPERATION_IS_SUCCESS,"data is null", obj);
		}
		return Response.ok(obj);
		
	}
	
	@PostMapping()
	public Response<?> addLocalCache(@RequestBody DataItemGroup dataGroup){
		List<Meter> meters = dataGroup.getMeters();
		if (StringUtils.isEmpty(dataGroup.getDataItemNum()) || StringUtils.isEmpty(dataGroup.getDataItemName()) 
				|| StringUtils.isEmpty(dataGroup.getCollectCron()) || StringUtils.isEmpty(meters) || meters.isEmpty()){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "param must be not null");
		}
		
		if (!CronExpression.isValidExpression(dataGroup.getCollectCron())){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "Field 'collectCron' must be crontab express!!!");
		}
		
		for (Meter meter : meters){
			
			if (StringUtils.isEmpty(meter.getMeterAddr()) || StringUtils.isEmpty(meter.getMeterType())
					|| StringUtils.isEmpty(meter.getProtocolType()) || StringUtils.isEmpty(meter.getGwId())){
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "Meter All Field must be not null!!!");
			}
			
		}
		
		try {
			
			DataUtils.saveDataConfig(dataGroup);
			
			return Response.ok();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.retn(ResultCode.UNKNOW_ERROR);
	}
}
