package cn.com.tw.paas.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import cn.com.tw.common.core.anno.CacheTypeEum;
import cn.com.tw.common.core.anno.EnableCusService;
import cn.com.tw.common.core.anno.MessgeTypeEum;
import cn.com.tw.common.web.anno.module.EnableCustomScan;
import cn.com.tw.db.mybatis.anno.EnableDB;

@SpringBootApplication
@ServletComponentScan
@EnableEurekaClient
@EnableCustomScan
@EnableCusService(mq = MessgeTypeEum.KAFKA,cache=CacheTypeEum.REDIS)
@EnableFeignClients
@EnableDB
public class JobApplication{
 
    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }
    
    
}