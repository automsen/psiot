package cn.com.tw.paas.auth;

import javax.annotation.PostConstruct;

import org.jasypt.encryption.StringEncryptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import cn.com.tw.common.core.anno.EnableCusService;
import cn.com.tw.common.core.anno.MessgeTypeEum;
import cn.com.tw.common.security.EnableAuthClient;
import cn.com.tw.common.web.anno.cache.EnableEhCache;
import cn.com.tw.common.web.anno.cors.EnableCors;
import cn.com.tw.common.web.anno.excep.EnableGExcep;
import cn.com.tw.db.mybatis.anno.EnableDB;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableAuthClient
@EnableCusService(mq = MessgeTypeEum.KAFKA)
@EnableDB
@EnableCors
@EnableGExcep
@EnableEhCache
@MapperScan("cn.com.tw.paas.auth.mapper")
public class AuthApplication {

	@Autowired
    private StringEncryptor stringEncryptor;
	
	@PostConstruct
	public void init(){
		String result = stringEncryptor.encrypt("123456");
		System.out.println(result);
	}
	
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}