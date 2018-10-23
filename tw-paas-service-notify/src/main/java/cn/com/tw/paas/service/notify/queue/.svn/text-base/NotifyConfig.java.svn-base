package cn.com.tw.paas.service.notify.queue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;


/**
 * 通知配置项
 * @author admin
 *
 */
@Component
public class NotifyConfig {
	
	public int maxNotifyTimes = 5;
	
	@SuppressWarnings("serial")
	public Map<Integer, Integer> params = new HashMap<Integer, Integer>(){
		{
			put(1, 0);
			put(2, 1);
			put(3, 2);
			put(4, 5);
			put(5, 10);
		}
	};
	
	public int getMaxNotifyTimes() {
		return maxNotifyTimes;
	}
	
	public int getParamsValue(int num){
		return params.get(num);
	}
		

}
