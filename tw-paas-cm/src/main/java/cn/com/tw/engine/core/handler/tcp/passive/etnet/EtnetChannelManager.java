package cn.com.tw.engine.core.handler.tcp.passive.etnet;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;

import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.handler.ChannelManagerHandler;

public class EtnetChannelManager implements ChannelManagerHandler{
 
	private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	private ChannelBridge channelBridge = new ChannelBridge(null);
	
	@Override
	public ChannelBridge get(String targetId) {
		return channelBridge;
	}

	@Override
	public void add(String targetId, ChannelBridge bridge) {
		
		return;
		
	}

	@Override
	public void remove(String targetId) {
		
	}
	
	public void remove(Channel channel) {
		channelGroup.remove(channel);
	}

	@Override
	public Map<String, ChannelBridge> getAll() {
		Map<String, ChannelBridge> result = new HashMap<String, ChannelBridge>();
		result.put("1", channelBridge);
		return result;
	}
	
	public ChannelGroup getChannelGroup() {
		return channelGroup;
	}

	@Override
	public int getSize() {
		return channelGroup.size();
	}

	@Override
	public void add(String targetId, Channel channel) {
		channelGroup.add(channel);
	}
	
}