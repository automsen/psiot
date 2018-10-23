package cn.com.tw.engine.core;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.com.tw.common.utils.tools.quartz.JobScheManager;
import cn.com.tw.engine.core.debug.DebugManager;
import cn.com.tw.engine.core.enm.ModeEnum;
import cn.com.tw.engine.core.handler.eum.ProtocolEum;

@Component
public class TaskInit{
	
	@Value("${engine.mode}")
	private String modeType;
	
	@Value("${engine.protocol}")
	private String protocolType;
	
	@Value("${engine.debuger.tcp.port}")
	private int engineDebugerPort;
	
	@PostConstruct
	public void init() throws ClassNotFoundException, SchedulerException, InterruptedException{
		ModeEnum modeEnum = Enum.valueOf(ModeEnum.class, modeType);
		ProtocolEum proEum = Enum.valueOf(ProtocolEum.class, protocolType);
		ChannelManaFactory.getInstanc().init(modeEnum, proEum);
		//启动调测模式
		DebugManager.build().init(engineDebugerPort);
	}
	
	@PreDestroy
	public void destory(){
		try {
			JobScheManager.getInstance().shutdown();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
