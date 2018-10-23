package cn.com.tw.common.web.listener.pubc;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.common.web.utils.env.Env;

/**
 * 系统初始化监听器
 * @author Administrator
 *
 */
@WebListener
public class InitListener implements ServletContextListener {
	
	public static final Logger logger = LoggerFactory.getLogger(InitListener.class);

	@Override
	public void contextInitialized(ServletContextEvent context) {
		
		SpringContext.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(context.getServletContext()));
		
		Environment env = (Environment) SpringContext.getBean("environment");

		Env.setEnv(env);
		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
	
}
