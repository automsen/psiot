package cn.com.tw.paas.service.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.com.tw.common.core.anno.CacheTypeEum;
import cn.com.tw.common.core.anno.EnableCusService;
import cn.com.tw.common.core.anno.MessgeTypeEum;
import cn.com.tw.common.hb.anno.EnableHB;
import cn.com.tw.common.security.EnableJwtFilter;
import cn.com.tw.common.web.anno.excep.EnableGExcep;
import cn.com.tw.common.web.anno.module.EnableCustomScan;
import cn.com.tw.paas.service.queue.service.ReadDataService;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@EnableCustomScan
@EnableCusService(mq = MessgeTypeEum.KAFKA, cache = CacheTypeEum.REDIS)
@EnableHB
@EnableGExcep
@EnableFeignClients
@EnableJwtFilter
public class QueApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(QueApplication.class, args);
    }
    
    @Component
    public static class InitClass implements CommandLineRunner {
    	
    	@Autowired
    	private ReadDataService readDataService;

		@Override
		public void run(String... arg0) throws Exception {
			readDataService.createHisTable();
			readDataService.createLastTable();
		}
    	
    }
 
}