package cn.com.tw.freemarker;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.TemplateDirectiveModel;

@Configuration
public class FreemarkerConfig {
	
	@Autowired
	private FreeMarkerProperties properties;
	
	@Qualifier("permissDirect")
	@Autowired(required = false)
	private TemplateDirectiveModel permissDirect;
	
	@Qualifier("roleDirect")
	@Autowired(required = false)
	private TemplateDirectiveModel roleDirect;
	
	/**
	 * 重新配置FreeMarker，支持自定义标签
	 * @return
	 */
	@Bean
	public FreeMarkerConfigurer freeMarkConfig(){
		FreeMarkerConfigurer freeMarkerConfig = new FreeMarkerConfigurer();
		Map<String, Object> freeMarkVariables = new HashMap<String, Object>();
		freeMarkVariables.put("permission", permissDirect);
		if(roleDirect != null){
			freeMarkVariables.put("role", roleDirect);
		}
		//<prop key="classic_compatible">true</prop>
		freeMarkerConfig.setFreemarkerVariables(freeMarkVariables);
		freeMarkerConfig.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
		freeMarkerConfig.setPreferFileSystemAccess(this.properties.isPreferFileSystemAccess());
		freeMarkerConfig.setDefaultEncoding(this.properties.getCharsetName());
		Properties settings = new Properties();
		settings.putAll(this.properties.getSettings());
		freeMarkerConfig.setFreemarkerSettings(settings);
		return freeMarkerConfig;
	}

}
