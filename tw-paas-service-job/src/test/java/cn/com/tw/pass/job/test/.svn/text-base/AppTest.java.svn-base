package cn.com.tw.pass.job.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ScanOptions.ScanOptionsBuilder;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.tw.paas.job.JobApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApplication.class) //1.4版本之前用的是//@SpringApplicationConfiguration(classes = Application.class)
public class AppTest {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	
    /**
     * 2017-06-16 14:08:09.884  INFO 13803 --- [           main] com.alibaba.druid.pool.DruidDataSource   : {dataSource-1} inited
     size:5
     -----测试完毕-------
     2017-06-16 14:08:09.955  INFO 13803 --- [       Thread-4] ationConfigEmbeddedWebApplicationContext : Closing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@fd07cbb: startup date [Fri Jun 16 14:08:04 CST 2017]; root of context hierarchy
     */
    @Test
    public void test(){
    	String keyTop = "ps:test";
    	redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();
    	ListOperations<String, Object> lists = redisTemplate.opsForList();
//    	// 插入10000个key的list
//    	for (int i = 0; i < 10000; i++) {
//    		lists.leftPush(keyTop+":"+i, i+"");
//		}
    	// 开始搜索
    	ScanOptionsBuilder build = new ScanOptions.ScanOptionsBuilder();
	    build.count(10);
	    
//	    for (int i = 0; i < orgIdArray.length; i++) {
	    build.match("ps:test*");
//		}
		
			// 每隔20秒 扫描一次所有集合的建值    
			redisTemplate.execute(new RedisCallback<Set<Object>>() {
			    @Override
			    public Set<Object> doInRedis(RedisConnection connection) throws DataAccessException {
			    	Cursor<byte[]> cursor = null;
			    	 Set<Object> binaryKeys = new HashSet<>();
			        try {
			        	/**
			        	 *  一次遍历1000条key
			        	 */
		        		cursor = connection.scan( build.build());
		        		while (cursor.hasNext()) {
					        	String keys = new String(cursor.next());
					        	System.out.println(keys);
					        	binaryKeys.add(keys);
					     }
					} catch (Exception e) {
					}
			        return binaryKeys;
			    }
			});
			
			
    }
    	
    	
    	
    
    
}