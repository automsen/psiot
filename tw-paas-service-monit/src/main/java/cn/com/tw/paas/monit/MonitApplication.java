package cn.com.tw.paas.monit;

import javax.annotation.PostConstruct;

import org.jasypt.encryption.StringEncryptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.com.tw.common.core.anno.CacheTypeEum;
import cn.com.tw.common.core.anno.EnableCusService;
import cn.com.tw.common.core.anno.MessgeTypeEum;
import cn.com.tw.common.security.EnableJwtFilter;
import cn.com.tw.common.web.anno.cache.EnableEhCache;
import cn.com.tw.common.web.anno.excep.EnableGExcep;
import cn.com.tw.common.web.anno.module.EnableCustomScan;
import cn.com.tw.db.mybatis.anno.EnableDB;

@SpringBootApplication
@EnableEurekaClient
@ServletComponentScan
@EnableTransactionManagement
@EnableCustomScan
@EnableCusService(mq = MessgeTypeEum.KAFKA,cache=CacheTypeEum.REDIS)
@EnableDB
@EnableFeignClients
@EnableGExcep
@EnableEhCache
@EnableJwtFilter
@MapperScan("cn.com.tw.paas.monit.mapper")
public class MonitApplication {
 
	@Autowired
    private StringEncryptor stringEncryptor;
	
	@PostConstruct
	public void init(){
		String result = stringEncryptor.encrypt("123456");
		System.out.println(result);
	}
	
    public static void main(String[] args) {
        SpringApplication.run(MonitApplication.class, args);
    }
 
}