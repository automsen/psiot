package cn.com.tw.paas.job.commons.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.job.executor.handler.JobExceptionHandler;


/**
 * 自定义定时任务异常处理
 * @author liming
 * 2018年8月21日 17:46:28
 */
public class MeterUseDayAndMonthAndYearException implements JobExceptionHandler{
	
	private static Logger logger = LoggerFactory.getLogger(MeterUseDayAndMonthAndYearException.class);

	@Override
	public void handleException(String jobName, Throwable cause) {
		logger.error(String.format("Job '%s' exception occur in job processing", jobName), cause);
	}

}
