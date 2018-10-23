package cn.com.tw.paas.job.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.google.common.base.Preconditions;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.job.commons.abs.AbstractSimpleDataJob;
import cn.com.tw.paas.job.service.MeterUseQuantumService;


/**
 *  单片处理
 *  主从关系  只有主服务挂掉子服务才启动
 * @author liming
 *
 */

//@Component
public class MeterUseDayAndMonthAndYearJob extends AbstractSimpleDataJob{
	
	@Autowired
	private MeterUseQuantumService meterQuantumService;
	

	
	/**
	 *  其余配置使用默认即可用
	 */
	@Override
	public void init() {
//		setCron("0 05 0/1 * * ?");  
		setCron("0/15 * * * * ?"); 
		setJobName("仪表日月年使用量统计");
		setShardingTotalCount(1);  // 单片执行  
		
		
	
	}

	@Override
	public void execute(ShardingContext shardingContext) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Response<?> response = meterQuantumService.meterUseDayAndMonthAndYearJob(sdf.format(new Date()));
		String statusCode = response.getStatusCode();
		// 如果返回错误码，直接向上抛异常
		Preconditions.checkArgument(statusCode.equals("000000"), response.getMessage());
	}

}
