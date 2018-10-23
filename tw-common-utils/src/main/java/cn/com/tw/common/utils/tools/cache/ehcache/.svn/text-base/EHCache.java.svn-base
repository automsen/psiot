package cn.com.tw.common.utils.tools.cache.ehcache;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


public class EHCache {
	
	private CacheManager manager = null;
	
	private EHCache(){
		manager = CacheManager.create();
	}
	
	private static class Singlon{
		
		private static EHCache ehCache = null;
		
		static {
			ehCache = new EHCache();
		}
		
		public static EHCache getInstance(){
			return ehCache;
		}
		
	}
	
	public static EHCache build(){
		return Singlon.getInstance();
	}
	

	public Object get(String cacheName, Object key) {
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			Element element = cache.get(key);
			if (element != null) {
				return element.getObjectValue();
			}
			
		}
		return null;
	}
	
	/**
	 * 获取所有
	 * @param cacheName
	 * @return
	 */
	public List<?> getAll(String cacheName){
		Cache cache = manager.getCache(cacheName);
		List<Object> results = new ArrayList<Object>();
		if (cache != null){
			@SuppressWarnings("unchecked")
			List<String> keys = cache.getKeys();
			
			for(String key : keys){
				Element element = cache.get(key);
				if (element != null) {
					results.add(element.getObjectValue());
				}
			}
		}
		
		return results;
	}

	public void put(String cacheName, Object key, Object value) {
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			cache.put(new Element(key, value));
		}
	}
	
	/**
	 * 最大存活时间 不管有没有访问
	 * @param cacheName
	 * @param key
	 * @param value
	 * @param liveSeconds
	 */
	public void putLive(String cacheName, Object key, Object value, int liveSeconds) {
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			Element ele = new Element(key, value);
			ele.setTimeToIdle(0);
			ele.setTimeToLive(liveSeconds);
			cache.put(ele);
		}
	}
	
	/**
	 * 最大存活时间 只有访问就不会过期
	 * @param cacheName
	 * @param key
	 * @param value
	 * @param liveSeconds
	 */
	public void putIdle(String cacheName, Object key, Object value, int idleSeconds) {
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			Element ele = new Element(key, value);
			ele.setTimeToLive(0);
			ele.setTimeToIdle(idleSeconds);
			cache.put(ele);
		}
	}

	public boolean remove(String cacheName, Object key) {
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			return cache.remove(key);
		}
		return false;
	}
}  