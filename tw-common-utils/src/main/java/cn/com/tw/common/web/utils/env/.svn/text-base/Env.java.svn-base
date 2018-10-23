package cn.com.tw.common.web.utils.env;

import org.springframework.core.env.Environment;

/**
 * properties 类
 * @author admin
 *
 */
public abstract class Env {

	private static Environment env;

	/**
	 * 
	 * @return
	 */
	public static Environment getEnv() {
		return env;
	}
	
	public static void setEnv(Environment env){
		Env.env = env;
	}
	
	public static String getVal(String key){
		if (env == null) {
			return null;
		}
		return env.getProperty(key);
	}
	
}
