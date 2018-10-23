package cn.com.tw.paas.service.notify;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.com.tw.common.core.anno.EnableCusService;
import cn.com.tw.common.core.anno.MessgeTypeEum;
import cn.com.tw.common.web.anno.excep.EnableGExcep;
import cn.com.tw.common.web.anno.module.EnableCustomScan;
import cn.com.tw.db.mybatis.anno.EnableDB;

@SpringBootApplication
@EnableEurekaClient
@ServletComponentScan
@EnableTransactionManagement
@EnableCustomScan
@EnableCusService(mq = MessgeTypeEum.KAFKA)
@EnableDB
@EnableGExcep
@MapperScan("cn.com.tw.paas.service.notify.mapper")
public class NotifyApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(NotifyApplication.class, args);
    }
 
}