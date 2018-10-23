package cn.com.tw.engine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.com.tw.engine.core.handler.DeviceDataHandler;
import cn.com.tw.engine.core.handler.store.device.FileDeviceDataHandler;

@Configuration
/*@Import(ExpireInterceptor.class)*/
public class InterceptorConfigurer extends WebMvcConfigurerAdapter {
	
	@Bean
	public DeviceDataHandler deviceDataHandler(){
		return new FileDeviceDataHandler();
	}
	
	/*@Autowired
	private ExpireInterceptor expireInterceptor;*/

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/*registry.addInterceptor(expireInterceptor)
		.addPathPatterns("/model1/contr").addPathPatterns("/cmd/send");*/
		/*.excludePathPatterns("/getSysManageLoginCode")
		.excludePathPatterns("/checkimagecode")
		.excludePathPatterns("/meter/recharge")
		.excludePathPatterns("/GetCode");*/
		
	}
}
