package cn.com.tw.common.web.utils.bean;

import org.springframework.context.ApplicationContext;

/**
 * 存放spring context 上下文
 * @author admin
 *
 */
public class SpringContext{

	/**
	 * Spring应用上下文环境  
	 */
    private static ApplicationContext applicationContext;  
  
    /** 
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境 
     *  
     * @param applicationContext 
     */  
    public static void setApplicationContext(ApplicationContext applicationContext) {  
    	SpringContext.applicationContext = applicationContext;  
    }  
  
    /** 
     * @return ApplicationContext 
     */  
    public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    }  
  
    /**
     * 
     * @param name
     * @return
     */
    public static Object getBean(String name){
    	if (applicationContext == null) {
    		return null;
    	}
        return applicationContext.getBean(name);  
    }  
}
