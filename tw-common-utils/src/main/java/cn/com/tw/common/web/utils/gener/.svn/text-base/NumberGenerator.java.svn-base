package cn.com.tw.common.web.utils.gener;

import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.tools.snowf.SnowflakeIdWorker;
import cn.com.tw.common.web.utils.env.Env;

public class NumberGenerator {
	
	private SnowflakeIdWorker snowFlake;
	
	public NumberGenerator(){
		String workerId = Env.getVal("spring.snowf.workerId");
		String dataId = Env.getVal("spring.snowf.dataId");
		long workerIdL = StringUtils.isEmpty(workerId) ? 0 : Long.parseLong(workerId);
		long dataIdL = StringUtils.isEmpty(dataId) ? 0 : Long.parseLong(dataId);
		this.snowFlake = new SnowflakeIdWorker(workerIdL, dataIdL);
	}
	
	public String getIdByPrefix(String prefix) {
		return prefix + snowFlake.nextId();
	}
	
	public String getId(){
		return "" + snowFlake.nextId();
	}
	
	public String getHexStrId(){
		return Long.toHexString(snowFlake.nextId());
	}
	
	public String getHexStrIdByByPrefix(String prefix){
		return prefix + Long.toHexString(snowFlake.nextId());
	}

}
