package cn.com.tw.saas.serv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.com.tw.common.core.anno.CacheTypeEum;
import cn.com.tw.common.core.anno.EnableCusService;
import cn.com.tw.common.core.anno.MessgeTypeEum;
import cn.com.tw.common.hb.anno.EnableHB;
import cn.com.tw.common.security.EnableJwtFilter;
import cn.com.tw.common.web.anno.cache.EnableEhCache;
import cn.com.tw.common.web.anno.excep.EnableGExcep;
import cn.com.tw.common.web.anno.module.EnableCustomScan;
import cn.com.tw.db.mybatis.anno.EnableDB;
import cn.com.tw.saas.serv.service.hb.TerminalHisDataService;
import cn.com.tw.saas.serv.service.hb.TerminalLastDataService;

@SpringBootApplication
@EnableEurekaClient
@ServletComponentScan
@EnableTransactionManagement
@EnableCustomScan
@EnableCusService(mq = MessgeTypeEum.KAFKA,cache=CacheTypeEum.REDIS)
@EnableDB
@EnableFeignClients
@EnableGExcep
@EnableHB
@EnableEhCache
@EnableJwtFilter
@MapperScan("cn.com.tw.saas.serv.mapper")
public class SaasApplication{
 
    public static void main(String[] args) {
        SpringApplication.run(SaasApplication.class, args);
    }
    
    @Component
    public static class InitClass implements CommandLineRunner {
    	
    	@Autowired
    	private TerminalHisDataService terminalHisDataService;
    	
    	@Autowired
    	private TerminalLastDataService terminalLastDataService;

		@Override
		public void run(String... arg0) throws Exception {
			terminalHisDataService.createHisTable();
			terminalLastDataService.createLastTable();
		}
    	
    }

 
}