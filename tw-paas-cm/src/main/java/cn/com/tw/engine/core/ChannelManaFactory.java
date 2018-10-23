package cn.com.tw.engine.core;

import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.utils.tools.quartz.JobScheManager;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.enm.ModeEnum;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.CmdResult;
import cn.com.tw.engine.core.entity.Task;
import cn.com.tw.engine.core.handler.ChannelManagerHandler;
import cn.com.tw.engine.core.handler.eum.ProtocolEum;
import cn.com.tw.engine.core.handler.tcp.active.etnet.EtnetActiveGetChannelManager;
import cn.com.tw.engine.core.handler.tcp.active.etnet.EtnetActiveGetClientHandler;
import cn.com.tw.engine.core.handler.tcp.active.lora.LoraActiveGetChannelManager;
import cn.com.tw.engine.core.handler.tcp.active.lora.MqLoraActiveGetChannelManager;
import cn.com.tw.engine.core.handler.tcp.active.lora.ZkLoraActiveGetChannelManager;
import cn.com.tw.engine.core.handler.tcp.passive.TcpServer;
import cn.com.tw.engine.core.handler.tcp.passive.container.ServerPassiveChannelManager;
import cn.com.tw.engine.core.handler.tcp.passive.etnet.EtnetChannelManager;
import cn.com.tw.engine.core.handler.tcp.passive.etnet.EtnetServerHandler;
import cn.com.tw.engine.core.handler.tcp.passive.etnet.MBusEtnetServerHandler;
import cn.com.tw.engine.core.handler.tcp.passive.gprs.GPRSPassiveGetServerHandler;
import cn.com.tw.engine.core.handler.tcp.passive.gprs.GPRSPassiveRevServerHandler;
import cn.com.tw.engine.core.handler.tcp.passive.wifi.WifiServerHandler;
import cn.com.tw.engine.core.job.CollectJob;
import cn.com.tw.engine.core.utils.TaskUtils;

/**
 * 实例工厂
 * @author admin
 *
 */
public class ChannelManaFactory {
	
	private static Logger logger = LoggerFactory.getLogger(ChannelManaFactory.class);
	
	private ChannelManagerHandler channelManager = null;
	
	private boolean isSection = false;
	
	private ChannelManaFactory() {
		this.isSection = Boolean.valueOf(Env.getVal("engine.gw.isSection"));
	}
	
	public static class Single {
		private static ChannelManaFactory channelManaFactory = null;
		static {
			channelManaFactory = new ChannelManaFactory();
		}
		
		public static ChannelManaFactory getSingle() {
			return channelManaFactory;
		}
	}
	
	public static ChannelManaFactory getInstanc() {
		return Single.getSingle();
	}
	
	public void init(ModeEnum type, ProtocolEum protoEum) throws ClassNotFoundException, SchedulerException {
		
		switch (type) {
		
			//以太网主动连接，主动采
			case etnet_active_get:
				
				initTask();
				
				//定义信道管理类
				channelManager = new EtnetActiveGetChannelManager(new EtnetActiveGetClientHandler());
				
				return;
				//以太网被动连接，主动采
			case etnet_passive_getByGroup:
				
				//启动Tcp服务
				initServer(new EtnetServerHandler(), protoEum);
				//定义信道管理类
				channelManager = new EtnetChannelManager();
				
				initTask();
				
				return;
			//以太网被动连接被动采集
			case etnet_passive_get:
				
				//启动Tcp服务
				if (protoEum.equals(ProtocolEum.DLT645V2007)) {
					//启动Tcp服务
					initServer(new GPRSPassiveRevServerHandler(), protoEum);
					//定义信道管理类
					channelManager = new ServerPassiveChannelManager();
				}else if (protoEum.equals(ProtocolEum.MODBUS)){
					initServer(new MBusEtnetServerHandler(), protoEum);
					channelManager = new ServerPassiveChannelManager();
				}
				
				initTask();
				
				return;	
			case etnet_passive_rev:
				
				//启动Tcp服务
				initServer(new GPRSPassiveRevServerHandler(), protoEum);
				
				//定义信道管理类
				channelManager = new ServerPassiveChannelManager();
				
				return;	
			case etnet_gprs_passive_rev:
				
				//启动Tcp服务
				initServer(new GPRSPassiveRevServerHandler(), protoEum);
				
				//定义信道管理类
				channelManager = new ServerPassiveChannelManager();
				
				return;	
			//lorawan 主动连接（http）, 主动采
			case lorawan_active_get:
				
				initTask();
				
				//定义信道管理类
				channelManager = new LoraActiveGetChannelManager();
				
				return;
			
				//lorawan ali平台通过mq, 主动采
			case ali_mq_lorawan_get_get:
				
				//initTask();
				
				//定义信道管理类
				channelManager = MqLoraActiveGetChannelManager.build();
				
				return;
				
			//gprs被动连接主动采
			case gprs_passive_get:
				
				//启动Tcp服务
				initServer(new GPRSPassiveGetServerHandler(), protoEum);
				
				//初始化定时任务
				initTask();
				
				//定义信道管理类
				channelManager = new ServerPassiveChannelManager();
				
				return;
				//gprs被动连接被动采集
			case gprs_passive_rev:
				
				//启动Tcp服务
				initServer(new GPRSPassiveRevServerHandler(), protoEum);
				
				//定义信道管理类
				channelManager = new ServerPassiveChannelManager();
				
				return;	
			case wifi_passive_rev:
				
				//启动Tcp服务
				initServer(new WifiServerHandler(), protoEum);
				
				//定义信道管理类
				channelManager = new ServerPassiveChannelManager();
			
				return;
			case zk_lorawan_active_get:
				
				initTask();
				
				//定义信道管理类
				channelManager = new ZkLoraActiveGetChannelManager();
				
				return;
			default:
				
				return;
			
		}
	}
	
	public void init(ModeEnum modeEnum) throws InterruptedException, ClassNotFoundException, SchedulerException{
		init(modeEnum, ProtocolEum.DLT645V2007);
	}
	
	private void initTask() throws ClassNotFoundException, SchedulerException{
		
		Task task = TaskUtils.readTaskFromFile();
		
		if (task == null){
			logger.error("task.object file read exception, task is null");
			
			task = new Task();
			
			task.setCronExp("0 0/1 * * * ?");
			//throw new ClassNotFoundException("task.object file read exception, task is null");
		}
		
		JobScheManager.getInstance().addJob(task.getJobName(), task.getGroupName(), CollectJob.class, "0 0/1 * * * ?").start();
		
		/**
		 * 明天的凌晨多5分钟执行
		 */
		//JobScheManager.getInstance().addJob("dayCollect", task.getGroupName(), DayCollectJob.class, "0 5 0 1/1 * ? *").start();
		
		/**
		 * 每月的最后一天 凌晨多5分钟执行
		 */
		//JobScheManager.getInstance().addJob("monthCollect", task.getGroupName(), MonthCollectJob.class, "0 5 0 L 1/1 ?").start();
		
	}
	
	public  ChannelManagerHandler build(){
		return channelManager;
	}
	
	/**
	 * 启动服务
	 * @param channelHandler
	 */
	private static void initServer(final ChannelHandler channelHandler, ProtocolEum proEum){
		//启动Tcp服务
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					new TcpServer(channelHandler).setProtocol(proEum).start();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		
	}
	
	/**
	 * 通过targetId获取ChannelBridge
	 * @param targetId 表示信道Id， 仪表Id， 网关ID
	 * @return
	 */
	public ChannelBridge getBridge(String targetId){
		
		targetId = (targetId == null ? "" : targetId.toLowerCase());
		
		if (channelManager == null) {
			return null;
		}
		
		Map<String, ChannelBridge> channelMap = channelManager.getAll();
		
		if (channelManager instanceof EtnetChannelManager){
			return channelMap.get("1");
		} else if (channelManager instanceof LoraActiveGetChannelManager || channelManager instanceof MqLoraActiveGetChannelManager){
			return channelMap.get(targetId);
		} else if (channelManager instanceof EtnetActiveGetChannelManager) {
			for (Map.Entry<String, ChannelBridge> map : channelMap.entrySet()){
				ChannelBridge bridge = map.getValue();
				String channId = bridge.getChannel().id().asLongText();
				channId = channId == null ? "" : channId;
				if (channId.toLowerCase().equals(targetId)){
					return bridge;
				}
			}
		}
		
		/*for (Map.Entry<String, ChannelBridge> map : channelMap.entrySet()){
			ChannelBridge bridge = map.getValue();
			String channId = bridge.getChannel().id().asLongText();
			if (channId.equals(targetId)){
				return bridge;
			}
		}*/
		
		ChannelBridge bridge = channelMap.get(targetId);
		
		if (bridge == null) {
			return null;
		}
		
		if (bridge.getChannel() == null || !bridge.getChannel().isActive()) {
			return null;
		}
		
		return bridge;
		
	}
	
	public String getGwIdByChannelId(String channelId){
		
		if (StringUtils.isEmpty(channelId)) {
			return null;
		}
		
		Map<String, ChannelBridge> channelMap = channelManager.getAll();
		
		for (Map.Entry<String, ChannelBridge> map : channelMap.entrySet()){
			ChannelBridge bridge = map.getValue();
			String channId = bridge.getChannel().id().asLongText();
			if (channelId.equals(channId)){
				return map.getKey();
			}
		}
		
		return null;
	}
	
	/**
	 * 通过targetId获取ChannelBridge
	 * @param targetId 表示信道Id， 仪表Id， 网关ID
	 * @return
	 */
	public ChannelBridge getBridgeByGwId(String targetId){
		
		targetId = (targetId == null ? "" : targetId.toLowerCase());
		
		if (channelManager == null) {
			return null;
		}
		
		Map<String, ChannelBridge> channelMap = channelManager.getAll();
		
		if (channelManager instanceof EtnetChannelManager){
			return channelMap.get("1");
		} else {
			return channelMap.get(targetId);
		}
	}
	
	public void addGwBridge(String addr, SocketChannel channel) {
    	
    	channelManager.add(addr, new ChannelBridge(channel));
    	
    	if (isSection) {
    		for (int i = 1; i <= 8; i++) {
    			channelManager.add(addr + "#" + i, new ChannelBridge(channel));
    		}
    	}
    	
    }
	
	public void updateGwBridge(String gwId, SocketChannel channel) {
		ChannelBridge channelBridge = getBridge(gwId);
		
		if (channelBridge != null) {
			channelBridge.updateChannel(channel);
		}else{
			logger.error("channelBridge : {}, can not find ChannelBridge by gwId, gwId = {}", new Object[]{channelBridge, gwId});
			return;
		}
		
		if (isSection) {
			for (int i = 1; i <= 8; i++) {
				ChannelBridge bridge = getBridge(gwId + "#" + i);
				if (bridge != null) {
					bridge.updateChannel(channel);
				}
			}
		}
		
	}
	
	public void putResultGwBridgeRemDuplicate(String gwId, CmdResponse resp) {
		ChannelBridge channelBridge = getBridge(gwId);
		if (channelBridge != null) {
			channelBridge.putResultRemDuplicate(new CmdResult(resp));
		}else {
			logger.error("channelBridge : {}, can not find ChannelBridge by gwId, gwId = {}", new Object[]{channelBridge, gwId});
			return;
		}
    	
    	if (isSection) {
    		for (int i = 1; i <= 8; i++) {
				ChannelBridge bridge = getBridge(gwId + "#" + i);
				if (bridge != null) {
					bridge.putResultRemDuplicate(new CmdResult(resp));
				}
			}
    	}
    	
    }
	
	public void putResultGwBridge(String gwId, CmdResponse resp) {
		ChannelBridge channelBridge = getBridge(gwId);
		if (channelBridge != null) {
			channelBridge.putResult(new CmdResult(resp));
		}else {
			logger.error("channelBridge : {}, can not find ChannelBridge by gwId, gwId = {}", new Object[]{channelBridge, gwId});
			return;
		}
    	
    	if (isSection) {
    		for (int i = 1; i <= 8; i++) {
				ChannelBridge bridge = getBridge(gwId + "#" + i);
				if (bridge != null) {
					bridge.putResult(new CmdResult(resp));
				}
			}
    	}
    	
    }
	
	public String getBridgeGwIdByChannelId(String channelId){
		
		if (StringUtils.isEmpty(channelId)) {
			return null;
		}
		
		Map<String, ChannelBridge> channelMap = channelManager.getAll();
		
		for (Map.Entry<String, ChannelBridge> map : channelMap.entrySet()){
			ChannelBridge bridge = map.getValue();
			if (bridge != null) {
				
				if (bridge.getChannel() == null) {
					continue;
				}
				
				String channId = bridge.getChannel().id().asLongText();
				if (channelId.equals(channId)){
					String gwId = map.getKey();
					int index = gwId.indexOf("#");
					if (index > -1) {
						return gwId.substring(0, index);
					}
					return gwId;
				}
			}
		}
		
		return null;
	}
	
	public boolean isSection() {
		return isSection;
	}

}
