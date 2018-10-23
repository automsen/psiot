package cn.com.tw.paas.monit.common.utils;

import java.util.HashMap;
import java.util.Map;

public class CacheMap {

	
	private static Map<String,Object> cacheMap = new HashMap<String,Object>();
	
	
	public static Object getCache(String key){
		return cacheMap.get(key);
	}
	public static void setCache(String key,String value){
		cacheMap.put(key, value);
	}
}
