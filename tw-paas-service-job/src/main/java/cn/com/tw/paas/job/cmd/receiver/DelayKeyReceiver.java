package cn.com.tw.paas.job.cmd.receiver;

import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import cn.com.tw.common.core.cache.redis.RedisService;
import cn.com.tw.paas.job.service.TerminalService;


/**
 * 根据数据对key的侦听做相对处理
 * @author liming
 * 2018年9月4日 15:22:28
 *
 */
public class DelayKeyReceiver extends KeyExpirationEventMessageListener{
	
private static String METER_DELAY_START = "### METER DELAY";
	

	
	private TerminalService terminalService;
	
	
	private RedisService redisService;
	
	public DelayKeyReceiver(RedisMessageListenerContainer listenerContainer,TerminalService terminalService ) {
		super(listenerContainer); 
		this.terminalService = terminalService;
	}

	private static final Logger log = LoggerFactory.getLogger(DelayKeyReceiver.class);

    private String METER_EXPIRE_KEY = "EX:COMM:METER";
    
    @Override
    protected void doHandleMessage(Message message) {
    	String key = new String(message.getBody());
    	log.debug("##################delay key({})",key);
    	
    	if(key.indexOf(METER_EXPIRE_KEY) != -1){
    		meterDelayError(key);
    	}
    }


    /**
     * 仪表失联
     * @param key
     */
	private void meterDelayError(String key) {
		String[] keyArr = key.split(":");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String meterId = keyArr[keyArr.length-1];
		String nextStatus= "2";
		log.debug(METER_DELAY_START+" delay key({}) EquipNum({}) currStatus:({})",key,meterId,nextStatus);

		/**
		 * 网络状态
		 *	0.未通讯 
		 *	1.通讯正常 
		 *	2.通讯警告 
		 *	3.通讯失联
		 */
	    // 更新设备状态
	    try {
	    	// 更新状态
		    terminalService.updateStatus(meterId, nextStatus);		
		} catch (Exception e) {
			log.error( METER_DELAY_START+" Update terninal Error. Key({})",key);
		}
	   
	}
}