package cn.com.tw.paas.job.commons.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.com.tw.paas.job.task.MeterUseDayAndMonthAndYearJob;
import cn.com.tw.paas.job.task.MeterUseQuantumJob;



@Component
@Order(1)
public class JobStart implements  ApplicationRunner{


//	@Autowired
//	private MeterUseDayAndMonthAndYearJob meterUseDayAndMonthAndYearJob;
//	@Autowired
//	private MeterUseQuantumJob meterUseQuantumJob;

	
	public void run(ApplicationArguments arg0) throws Exception {
		
		try {
//			meterUseDayAndMonthAndYearJob.setJob_exception_handler("cn.com.tw.paas.job.exception.MeterUseDayAndMonthAndYearException");
//			meterUseDayAndMonthAndYearJob.handleTask();
//			meterUseQuantumJob.handleTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	
}
