package cn.com.tw.paas.monit.controller.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ScanOptions.ScanOptionsBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.cache.ehcache.EHCache;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.common.utils.cons.CacheNameCons;
import cn.com.tw.paas.monit.service.base.DItemService;

@RestController
@RequestMapping("cache")
public class CacheController {
	
	@Autowired
	private DItemService dItemService;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@RequestMapping("ins/{code}")
	public Response<?> insCache(@PathVariable String code){
		Object obj = getCmdCache(CacheNameCons.ins_dataItem, code);
		return Response.ok(obj);
	}
	
	@RequestMapping("cmd/{code}")
	public Response<?> cmdCache(@PathVariable String code){
		Object obj = getCmdCache(CacheNameCons.cmd_dataItem, code);
		return Response.ok(obj);
	}
	
	@RequestMapping("itemAll")
	public Response<?> itemAll(){
		return Response.ok(dItemService.queryAll());
	}
	
	public Object getCmdCache(String cacheKey,String key){
		EHCache cache = EHCache.build();
		return  cache.get(cacheKey, key) ;
	}
	
	/**
	 * 通过线程池刷redis 启动项目
	 */
	private final String REDIS_SEND_KEY = "ps:ins:send";
	
	@RequestMapping("redisList")
	public Response<?> getRedisCacheList(){
		
		LinkedList<Map<String,Object>> resultList = new LinkedList<Map<String,Object>>();
		try {
			ListOperations<String, Object> listopt = redisTemplate.opsForList();
			/**
			 * 键值对 序列化方式。设置和取值必须保持一致否则报错
			 */
			// 设置key的解析方式
			ScanOptionsBuilder build = new ScanOptions.ScanOptionsBuilder();
			// 扫描区间 每次1000条，防止扫描阻塞。
			// 基本延迟忽略不计。加快速度可以将1000-》10000。最佳数字为预估值，并低于10000
			build.count(1000);
			// 每隔10秒 扫描一次所有集合的建值
			Set<String> keys = redisTemplate.execute(new RedisCallback<Set<String>>() {
				@Override
				public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
					Cursor<byte[]> cursor = null;
					Set<String> binaryKeys = new HashSet<>();
					try {
							// 根据机构ID分类扫描
							build.match(REDIS_SEND_KEY + "*");
							cursor = connection.scan(build.build());
							while (cursor.hasNext()) {
								String keys = new String(cursor.next());
								binaryKeys.add(keys);
							}
							
					} catch (Exception e) {
					}
					return binaryKeys;
				}
			});
			Iterator<String> keyIt = keys.iterator();
			Map<String,Object> obj = null;
			while(keyIt.hasNext()){
				String key = keyIt.next();
				// 查询key下的count
				Long size = listopt.size(key);
				obj = new HashMap<String,Object>();
				obj.put("key", key);
				obj.put("value", size);
				resultList.add(obj);
			}
		} catch (Exception e) {
			return Response.retn(ResultCode.UNKNOW_ERROR, String.format("获取key操作异常，exception{%s}", e));
		}
		return Response.ok(resultList);
	}
	
	
	@RequestMapping("removeRedisKey")
	public Response<?> removeKey(@RequestParam("key") String key){
		try {
			ListOperations<String, Object> listopt = redisTemplate.opsForList();
			listopt.trim(key, 1, 0);
		} catch (Exception e) {
			return Response.retn(ResultCode.UNKNOW_ERROR,String.format("清空失败！exception{%s}", e));
		}
		return Response.ok();
	}
	
}
