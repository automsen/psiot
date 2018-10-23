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

//@Component
public class MeterUseQuantumJob extends AbstractSimpleDataJob{
	
	@Autowired
	private MeterUseQuantumService meterUseQuantumService;
	/**
	 *  其余配置使用默认即可用
	 */
	@Override
	public void init() {
		setCron("0 0 0/1 * * ?");  
		setJobName("仪表时段使用量统计");
		setShardingTotalCount(1);  // 单片执行  
	}

	@Override
	public void execute(ShardingContext shardingContext) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Response<?> response = meterUseQuantumService.meterUseQuantumJob(sdf.format(new Date()));
		String statusCode = response.getStatusCode();
		// 如果返回错误码，直接向上抛异常
		Preconditions.checkArgument(statusCode.equals("000000"), response.getMessage());
	}

}

