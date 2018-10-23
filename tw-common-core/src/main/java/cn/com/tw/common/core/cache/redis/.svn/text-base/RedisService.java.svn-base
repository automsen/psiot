package cn.com.tw.common.core.cache.redis;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import cn.com.tw.common.core.cache.RedisHandler;

/**
 * redis实现
 * @author admin
 *
 */
public class RedisService implements RedisHandler{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private ValueOperations<String, Object> getOptValue(){
    	return redisTemplate.opsForValue();
    } 

	@Override
	public Object get(String key) {
		return getOptValue().get(key);
	}

	@Override
	public long del(String... keys) {
		List<String> keyList = Arrays.asList(keys);
		redisTemplate.delete(keyList);
		return 0;
	}

	@Override
	public void set(String key, String value, int seconds) {
		getOptValue().set(key, value, seconds, TimeUnit.SECONDS);
	}

	@Override
	public void set(String key, String value) {
		getOptValue().set(key, value);
	}

	@Override
	public void set(String key, Object obj) {
		getOptValue().set(key, obj);
	}

	@Override
	public void set(String key, Object obj, int seconds) {
		getOptValue().set(key, obj, seconds, TimeUnit.SECONDS);
	}

	/** 
     * 指定缓存失效时间 
     * @param key 键 
     * @param time 时间(秒) 
     * @return 
     */  
    public boolean expire(String key,long time){  
        if(time>0){  
            redisTemplate.expire(key, time, TimeUnit.SECONDS);  
        }  
        return true;  
    }  

    /** 
     * 根据key 获取过期时间 
     * @param key 键 不能为null 
     * @return 时间(秒) 返回0代表为永久有效 
     */  
    public long getExpire(String key){  
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);  
    }  

    /** 
     * 判断key是否存在 
     * @param key 键 
     * @return true 存在 false不存在 
     */  
    public boolean hasKey(String key){  
         return redisTemplate.hasKey(key);  
    }  

    /** 
     * 普通缓存放入并设置时间 
     * @param key 键 
     * @param value 值 
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期 
     * @return true成功 false 失败 
     */  
    public boolean set(String key,Object value,long time){  
        if(time>0){  
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);  
        }else{  
            set(key, value);  
        }  
        return true;
    }  

    /** 
     * 递增 
     * @param key 键 
     * @param by 要增加几(大于0) 
     * @return 
     */  
    public long incr(String key, long delta){    
        if(delta<0){  
            throw new RuntimeException("递增因子必须大于0");  
        }  
        return redisTemplate.opsForValue().increment(key, delta);  
    }  

    /** 
     * 递减 
     * @param key 键 
     * @param by 要减少几(小于0) 
     * @return 
     */  
    public long decr(String key, long delta){    
        if(delta<0){  
            throw new RuntimeException("递减因子必须大于0");  
        }  
        return redisTemplate.opsForValue().increment(key, -delta);    
    }    

    //================================Map=================================  
    /** 
     * HashGet 
     * @param key 键 不能为null 
     * @param item 项 不能为null 
     * @return 值 
     */  
    public Object hGet(String key, String item){  
        return redisTemplate.opsForHash().get(key, item);  
    }  

    /** 
     * 获取hashKey对应的所有键值 
     * @param key 键 
     * @return 对应的多个键值 
     */  
    public Map<Object,Object> hGetMap(String key){  
        return redisTemplate.opsForHash().entries(key);  
    }  

    /** 
     * HashSet 
     * @param key 键 
     * @param map 对应多个键值 
     * @return true 成功 false 失败 
     */  
    public boolean hSetMap(String key, Map<String,Object> map){    
        try {  
            redisTemplate.opsForHash().putAll(key, map);  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  

    /** 
     * HashSet 并设置时间 
     * @param key 键 
     * @param map 对应多个键值 
     * @param time 时间(秒) 
     * @return true成功 false失败 
     */  
    public boolean hSetMap(String key, Map<String,Object> map, long time){    
        redisTemplate.opsForHash().putAll(key, map);  
        if(time>0){  
            expire(key, time);  
        }  
        return true;  
    }  

    /** 
     * 向一张hash表中放入数据,如果不存在将创建 
     * @param key 键 
     * @param item 项 
     * @param value 值 
     * @return true 成功 false失败 
     */  
    public boolean hSet(String key,String item,Object value) {  
        redisTemplate.opsForHash().put(key, item, value);  
        return true;  
    }  

    /** 
     * 向一张hash表中放入数据,如果不存在将创建 
     * @param key 键 
     * @param item 项 
     * @param value 值 
     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间 
     * @return true 成功 false失败 
     */  
    public boolean hSet(String key,String item, Object value,long time) {  
        redisTemplate.opsForHash().put(key, item, value);  
        if(time>0){  
            expire(key, time);  
        }  
        return true;  
    }  

    /** 
     * 删除hash表中的值 
     * @param key 键 不能为null 
     * @param item 项 可以使多个 不能为null 
     */  
    public void hDel(String key, Object... item){    
        redisTemplate.opsForHash().delete(key,item);  
    }   

    /** 
     * 判断hash表中是否有该项的值 
     * @param key 键 不能为null 
     * @param item 项 不能为null 
     * @return true 存在 false不存在 
     */  
    public boolean hHasKey(String key, String item){  
        return redisTemplate.opsForHash().hasKey(key, item);  
    }   

    /** 
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回 
     * @param key 键 
     * @param item 项 
     * @param by 要增加几(大于0) 
     * @return 
     */  
    public double hIncr(String key, String item,double by){    
        return redisTemplate.opsForHash().increment(key, item, by);  
    }  

    /** 
     * hash递减 
     * @param key 键 
     * @param item 项 
     * @param by 要减少记(小于0) 
     * @return 
     */  
    public double hDecr(String key, String item,double by){    
        return redisTemplate.opsForHash().increment(key, item,-by);    
    }    

    //============================set=============================  
    /** 
     * 根据key获取Set中的所有值 
     * @param key 键 
     * @return 
     */  
    public Set<Object> sMembers(String key){  
        try {  
            return redisTemplate.opsForSet().members(key);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  

    /** 
     * 根据value从一个set中查询,是否存在 
     * @param key 键 
     * @param value 值 
     * @return true 存在 false不存在 
     */  
    public boolean sIsMember(String key,Object value){  
        try {  
            return redisTemplate.opsForSet().isMember(key, value);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  

    /** 
     * 将数据放入set缓存 
     * @param key 键 
     * @param values 值 可以是多个 
     * @return 成功个数 
     */  
    public long sAdd(String key, Object...values) {  
       return redisTemplate.opsForSet().add(key, values);  
    }  

    /** 
     * 获取set缓存的长度 
     * @param key 键 
     * @return 
     */  
    public long sSize(String key){  
       return redisTemplate.opsForSet().size(key);  
    }  

    /** 
     * 移除值为value的 
     * @param key 键 
     * @param values 值 可以是多个 
     * @return 移除的个数 
     */  
    public long sRemove(String key, Object ...values) {  
        Long count = redisTemplate.opsForSet().remove(key, values);  
        return count;  
    }  
    //===============================list=================================  

    /** 
     * 获取list缓存的内容 
     * @param key 键 
     * @param start 开始 
     * @param end 结束  0 到 -1代表所有值 
     * @return 
     */  
    public List<Object> lRange(String key,long start, long end){  
        return redisTemplate.opsForList().range(key, start, end);  
    }
    
    /** 
     * 获取list缓存的长度 
     * @param key 键 
     * @return 
     */  
    public long lSize(String key){  
        return redisTemplate.opsForList().size(key);  
    }  

    /** 
     * 通过索引 获取list中的值 
     * @param key 键 
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推 
     * @return 
     */  
    public Object lGetByIndex(String key,long index){  
        return redisTemplate.opsForList().index(key, index);  
    }  

    /** 
     * 将list放入缓存 
     * @param key 键 
     * @param value 值 
     * @param time 时间(秒) 
     * @return 
     */  
    public boolean lRightPush(String key, Object value) {  
        redisTemplate.opsForList().rightPush(key, value);  
        return true;  
    }
    
    /** 
     * 将list放入缓存 
     * @param key 键 
     * @param value 值 
     * @param time 时间(秒) 
     * @return 
     */  
    public boolean lLeftPush(String key, Object value) {  
        redisTemplate.opsForList().leftPush(key, value);
        return true;  
    }  

    /** 
     * 将list放入缓存 
     * @param key 键 
     * @param value 值 
     * @param time 时间(秒) 
     * @return 
     */  
    public boolean lRightPush(String key, Object value, long time) {  
        redisTemplate.opsForList().rightPush(key, value);  
        if (time > 0) {
        	expire(key, time);  
        }
        return true;  
    }
    
    /** 
     * 将list放入缓存 
     * @param key 键 
     * @param value 值 
     * @param time 时间(秒) 
     * @return 
     */  
    public boolean lLeftPush(String key, Object value, long time) {  
        redisTemplate.opsForList().leftPush(key, value);  
        if (time > 0) {
        	expire(key, time);  
        }
        return true;  
    }

    /** 
     * 将list放入缓存 
     * @param key 键 
     * @param value 值 
     * @param time 时间(秒) 
     * @return 
     */  
    public boolean lRightPushAll(String key, List<Object> value) {  
        redisTemplate.opsForList().rightPushAll(key, value);  
        return true;  
    }
    
    /** 
     * 将list放入缓存 
     * @param key 键 
     * @param value 值 
     * @param time 时间(秒) 
     * @return 
     */  
    public boolean lLeftPushAll(String key, List<Object> value) {  
        redisTemplate.opsForList().leftPushAll(key, value);  
        return true;  
    }

    /** 
     * 将list放入缓存 
     * @param key 键 
     * @param value 值 
     * @param time 时间(秒) 
     * @return 
     */  
    public boolean lSet(String key, List<Object> value, long time) {  
        redisTemplate.opsForList().rightPushAll(key, value);  
        if (time > 0) {
        	expire(key, time);  
        }
        return true;  
    }  

    /** 
     * 根据索引修改list中的某条数据 
     * @param key 键 
     * @param index 索引 
     * @param value 值 
     * @return 
     */  
    public boolean lUpdateByIndex(String key, long index, Object value) {  
        redisTemplate.opsForList().set(key, index, value);  
        return true;  
    }   

    /** 
     * 移除N个值为value  
     * @param key 键 
     * @param count 移除多少个 
     * @param value 值 
     * @return 移除的个数 
     */  
    public long lRemove(String key,long count,Object value) {  
        Long remove = redisTemplate.opsForList().remove(key, count, value);  
        return remove;
    }
    
    /** 
     * 移除N个值为value  
     * @param key 键 
     * @param count 移除多少个 
     * @param value 值 
     * @return 移除的个数 
     */  
    public Object lRightPop(String key) {  
    	Object value = redisTemplate.opsForList().rightPop(key);
        return value;
    }
    
    public Object lRightPop(String key, long pLong) {  
    	Object value = redisTemplate.opsForList().rightPop(key, pLong, TimeUnit.SECONDS);
        return value;
    }
    
    public Object lRightPopAndLeftPush(String key1, String key2, long pLong) {  
    	Object value = redisTemplate.opsForList().rightPopAndLeftPush(key1, key2, pLong, TimeUnit.SECONDS);
        return value;
    }

	@Override
	public void execute(RedisCallback<?> callback) {
		redisTemplate.execute(callback);
	}
	
	public void convertAndSend(String channel, Object message) {
		redisTemplate.convertAndSend(channel, message);
	}

}