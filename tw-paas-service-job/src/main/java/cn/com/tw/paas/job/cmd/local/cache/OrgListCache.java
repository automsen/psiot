package cn.com.tw.paas.job.cmd.local.cache;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class OrgListCache {

	private final static ConcurrentHashMap<String,Object> orgMaps = new ConcurrentHashMap<String,Object>();

	
	public static void clearMap(){
		if(!orgMaps.isEmpty()){
			orgMaps.clear();
		}
	}
	
	public static void put(String key,Object value){
		orgMaps.put(key, value);
	}
	
	public static boolean contrainKey(String key){
		if(orgMaps.containsKey(key)){
			return true;
		}
		return false;
		
	}
	
	public static Enumeration<String> getKeys(){
		if(orgMaps.isEmpty()){
			return null;
		}
		return orgMaps.keys();
	}
	
	public static boolean isEmpty(){
		if(orgMaps.isEmpty()){
			return true;
		}
		return false;
	}
}
