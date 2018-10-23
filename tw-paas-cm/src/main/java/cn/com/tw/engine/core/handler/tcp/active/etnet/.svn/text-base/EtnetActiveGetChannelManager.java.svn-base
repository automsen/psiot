package cn.com.tw.engine.core.handler.tcp.active.etnet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.entity.WarnInfo;
import cn.com.tw.engine.core.entity.eum.TermStatus;
import cn.com.tw.engine.core.exception.EngineException;
import cn.com.tw.engine.core.exception.code.EngineCode;
import cn.com.tw.engine.core.handler.ChannelManagerHandler;
import cn.com.tw.engine.core.netty.decoder.Cl645ProtocolDecoder;
import cn.com.tw.engine.core.netty.encoder.Cl645ProtocolEncoder;
import cn.com.tw.engine.core.utils.MsgAysncService;
import cn.com.tw.engine.core.utils.cons.Cons;

/**
 * 客户端连接服务端实现
 * @author admin
 *
 */
public class EtnetActiveGetChannelManager implements ChannelManagerHandler{
	
	//private static Logger logger = LoggerFactory.getLogger(EtnetActiveGetChannelManager.class);
	
	/**
	 * 会话
	 */
	private ConcurrentMap<String, ChannelBridge> channelMap = new ConcurrentHashMap<String, ChannelBridge>();
	
	/**
	 * 服务类
	 */
	private Bootstrap bootstrap = new Bootstrap();
	
	@Autowired
	private MsgAysncService msgAysncService;
	
	public EtnetActiveGetChannelManager(final ChannelHandler channelHandler){
		//worker
		EventLoopGroup worker = new NioEventLoopGroup();
		
		//设置线程池
		bootstrap.group(worker);
		
		//设置socket工厂、
		bootstrap.channel(NioSocketChannel.class);
		
		//设置管道
		bootstrap.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new Cl645ProtocolDecoder());
				ch.pipeline().addLast(new Cl645ProtocolEncoder());
				ch.pipeline().addLast(channelHandler);
			}
			
		});
		
		this.msgAysncService = (MsgAysncService) SpringContext.getBean("msgAysncService");
	}
	
	public ConcurrentMap<String, ChannelBridge> getChannelMap() {
		return channelMap;
	}

	/**
	 * 创建连接
	 * @param serverUrl
	 * @param port
	 */
	private ChannelBridge connect(String serverIP, int port){
		String serverId = serverIP + ":" + port;
		ChannelBridge bridge = channelMap.get(serverId);
		try {
			
			if (bridge != null) {
				Channel channel = bridge.getChannel();
				if(channel != null){
					if (!channel.isActive()){
						//重连
						ChannelBridge reconnBridge = reconnect(serverId, bridge);
						
						return reconnBridge;
					}
					return bridge;
				}
			}
			
			ChannelFuture future = bootstrap.connect(serverIP, port).sync();
			
			ChannelBridge channelBridge = new ChannelBridge(future.channel());
			
			channelMap.put(serverId, channelBridge);
			
			msgAysncService.sendWarnInfo(new WarnInfo(serverId, Cons.COLLECT_GW_TYPE, TermStatus.open.getValue()));
			return channelBridge;
		} catch (Exception e) {
			
			msgAysncService.sendWarnInfo(new WarnInfo(serverId, Cons.COLLECT_GW_TYPE, TermStatus.close.getValue()));
			
			return null;
			//throw new EngineException(EngineCode.CHANNEL_GW_TIMEOUT, "can't connect server, target connect fail! or device info non-existent!");
		}
		
	}
	
	/**
	 * 获取活动信道 独木桥
	 * @param serverId
	 * @return
	 * @throws InterruptedException 
	 * @throws NumberFormatException 
	 */
	public ChannelBridge getActiveChannelBridge(String serverId) throws NumberFormatException, InterruptedException{
		ChannelBridge bridge = channelMap.get(serverId);
		Channel channel = bridge.getChannel();
		
		if (!channel.isActive()){
			//重连
			return reconnect(serverId, bridge);
		}
		
		return bridge;
	}
	
	/**
	 * 重连
	 * @param serverId
	 * @param channel
	 * @return
	 * @throws InterruptedException 
	 * @throws NumberFormatException 
	 */
	public ChannelBridge reconnect(String serverId, ChannelBridge bridge) {
		String[] serverIds = serverId.split(":");
		try {
			synchronized (bridge) {
				
				if (bridge.getChannel().isActive()){
					return bridge;
				}
				Channel newChannel = bootstrap.connect(serverIds[0], Integer.parseInt(serverIds[1])).sync().channel();
				bridge.setChannel(newChannel);
				channelMap.put(serverId, bridge);
				
				msgAysncService.sendWarnInfo(new WarnInfo(serverId, Cons.COLLECT_GW_TYPE, TermStatus.open.getValue()));
				
				return bridge;
			}
		} catch (Exception e) {
			
			msgAysncService.sendWarnInfo(new WarnInfo(serverId, Cons.COLLECT_GW_TYPE, TermStatus.close.getValue()));
			
			return null;
			//throw new EngineException(EngineCode.CHANNEL_GW_TIMEOUT, "can't connect server, target connect fail! or device info non-existent!");
		}
	}

	@Override
	public ChannelBridge get(String targetId){
		ChannelBridge bridge = channelMap.get(targetId);
		
		if (bridge == null) {
			
			String[] targetIds = targetId.split(":");
			
			if (targetIds.length != 2){
				
				throw new EngineException(EngineCode.CHANNEL_SETTING_ERROR, "active get meter data, but targetId params do not match ' ip : prot', format error!!!!");
			}
			
			//重连
			return connect(targetIds[0], Integer.parseInt(targetIds[1]));
		}
		
		
		Channel channel = bridge.getChannel();
		
		if (!channel.isActive()){
			//重连
			return reconnect(targetId, bridge);
		}
		
		return bridge;
	}

	@Override
	public void add(String targetId, ChannelBridge bridge) {
		
		String[] targetIds = targetId.split(":");
		
		if (targetIds.length != 2){
			
			throw new EngineException(EngineCode.CHANNEL_SETTING_ERROR, "active get meter data, but targetId params do not match ' ip : prot', format error!!!!");
			
		}
		connect(targetIds[0], Integer.parseInt(targetIds[1]));
	}

	@Override
	public void remove(String targetId) {
		
		channelMap.remove(targetId);
		
	}
	
	public String remove(SocketChannel channel){
		for (Map.Entry<String, ChannelBridge> map : channelMap.entrySet()){
			String key = map.getKey();
			ChannelId channelId = map.getValue().getChannel().id();
			String channId = channelId.asLongText();
			if (channId.equals(channel.id().asLongText())){
				channelMap.remove(key);
				return key;
			}
		}
		
		return null;
		
	}

	@Override
	public Map<String, ChannelBridge> getAll() {
		// TODO Auto-generated method stub
		return channelMap;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return channelMap.size();
	}

	@Override
	public void add(String targetId, Channel channel) {
		String[] targetIds = targetId.split(":");
		
		if (targetIds.length != 2){
			
			throw new EngineException(EngineCode.CHANNEL_SETTING_ERROR, "active get meter data, but targetId params do not match ' ip : prot', format error!!!!");
			
		}
		connect(targetIds[0], Integer.parseInt(targetIds[1]));
	}

}
