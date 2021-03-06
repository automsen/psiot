package cn.com.tw.saas.serv;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {
	
	 	@Bean
	    public Docket createRestApi() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(apiInfo())
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("cn.com.tw.saas.serv.controller"))
	                .paths(PathSelectors.any())
	                .build();
	    }

	 	
	    private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	                .title("武汉时波网络技术有限公司")
	                .description("")
	                .termsOfServiceUrl("")
	                .contact("武汉时波")
	                .version("1.0")
	                .build();
	    }

}
