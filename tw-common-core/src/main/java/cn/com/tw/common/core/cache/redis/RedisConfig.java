package cn.com.tw.common.core.cache.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
 
	@SuppressWarnings("rawtypes")
	@Primary
    @Bean
    public RedisCacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        return rcm;
    }
 
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
       RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
       redisTemplate.setKeySerializer(new StringRedisSerializer());
       redisTemplate.setConnectionFactory(factory);
       redisTemplate.afterPropertiesSet();
       return redisTemplate;
    }
}