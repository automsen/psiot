package cn.com.tw.engine.core.handler.tcp.passive.container;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.handler.ChannelManagerHandler;
import cn.com.tw.engine.core.utils.RegCacheService;

public class ServerPassiveChannelManager implements ChannelManagerHandler{
	
	private Map<String, ChannelBridge> channelMap = new ConcurrentHashMap<String, ChannelBridge>();
	
	private RegCacheService regCacheService;
	
	public ServerPassiveChannelManager() {
		this.regCacheService = (RegCacheService) SpringContext.getBean("regCacheService");
	}
	
	public Map<String, ChannelBridge> getChannelMap(){
		return channelMap;
	}
	
	public ChannelBridge get(String key){
		return channelMap.get(key);
	}
	
	public int getSize(){
		return channelMap.size();
	}
	
	public void remove(String key){
    	channelMap.remove(key);
    }
	
	public String remove(SocketChannel channel){
		for (Map.Entry<String, ChannelBridge> map : channelMap.entrySet()){
			String key = map.getKey();
			ChannelId channelId = map.getValue().getChannel().id();
			String channId = channelId.asLongText();
			if (channId.equals(channel.id().asLongText())){
				//channelMap.remove(key);
				int index = key.indexOf("#");
				if (index > -1) {
					return key.substring(0, index);
				}
				return key;
			}
		}
		
		return null;
		
	}
	
	public Set<String> getKeys(){
    	return channelMap.keySet();
    }

	@Override
	public void add(String targetId, ChannelBridge bridge) {
		reg(targetId);
		channelMap.put(targetId, bridge);
	}

	@Override
	public Map<String, ChannelBridge> getAll() {
		return channelMap;
	}

	@Override
	public void add(String targetId, Channel channel) {
		channelMap.put(targetId, new ChannelBridge(channel));
	}
	
	public void reg(String targetId){
		
		if (regCacheService == null) {
			return;
		}
		
		regCacheService.regisHostPort(targetId);
		
	}

}
