package cn.com.tw.eureka;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 服务注册中心
 * @author admin
 *
 */
@EnableEurekaServer
@SpringBootApplication
public class PSServerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(PSServerApplication.class).web(true).run(args);
	}

}
