package cn.com.tw.paas.job.cmd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import cn.com.tw.paas.job.cmd.receiver.DelayKeyReceiver;
import cn.com.tw.paas.job.service.TerminalService;


/**
 * 添加延迟key侦听
 * 
 * @author liming 
 * 2018年8月31日 09:35:39
 */
@Configuration
public class RedisConfiguration {
	
	@Value("${spring.redis.database}")
	private String dateIndex;
	
	
	
    /**
     * redis消息监听器容器
     * @param connectionFactory
     * @param paasPushAdapter 
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
    		TerminalService terminalService 
             ) {
    	
    	String topic = "__keyevent@*__:expired";
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(new DelayKeyReceiver(container,terminalService ), new PatternTopic(topic));
        return container;
    }
    
	
}
