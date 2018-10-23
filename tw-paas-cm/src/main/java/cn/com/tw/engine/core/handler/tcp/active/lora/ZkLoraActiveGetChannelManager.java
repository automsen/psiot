package cn.com.tw.engine.core.handler.tcp.active.lora;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.util.StringUtils;

import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.handler.ChannelManagerHandler;

/**
 * 客户端连接服务端实现
 * @author admin
 *
 */
public class ZkLoraActiveGetChannelManager implements ChannelManagerHandler{
	
	/**
	 * 会话
	 */
	private ConcurrentMap<String, ChannelBridge> channelMap = new ConcurrentHashMap<String, ChannelBridge>();
	
	public ZkLoraActiveGetChannelManager(){
	}

	@Override
	public ChannelBridge get(String meterId){
		if (StringUtils.isEmpty(meterId)){
			return null;
		}
		
		ChannelBridge bridge = channelMap.get(meterId);
		if (bridge == null){
			bridge = new ChannelBridge(null);
			channelMap.put(meterId.toLowerCase(), bridge);
			return bridge;
		}
		
		return bridge;
	}

	@Override
	public void add(String targetId, ChannelBridge bridge) {
		if (StringUtils.isEmpty(targetId)){
			return;
		}
		
		channelMap.put(targetId.toLowerCase(), bridge);
	}

	@Override
	public void remove(String targetId) {
		channelMap.remove(targetId);
	}

	@Override
	public Map<String, ChannelBridge> getAll() {
		return channelMap;
	}

	@Override
	public int getSize() {
		return channelMap.size();
	}

	@Override
	public void add(String targetId, Channel channel) {
	}

}
