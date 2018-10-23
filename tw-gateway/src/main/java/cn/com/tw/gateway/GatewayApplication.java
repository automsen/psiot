package cn.com.tw.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import cn.com.tw.common.security.EnableAuthClient;
import cn.com.tw.common.web.anno.cors.EnableCors;

/**
 * 网关服务
 * @author admin
 *
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableAuthClient
@EnableCors
@EnableFeignClients
public class GatewayApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(GatewayApplication.class).web(true).run(args);
	}
}
