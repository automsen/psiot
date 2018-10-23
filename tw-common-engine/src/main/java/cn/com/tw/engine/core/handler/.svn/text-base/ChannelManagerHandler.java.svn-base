package cn.com.tw.engine.core.handler;

import io.netty.channel.Channel;

import java.util.Map;

import cn.com.tw.engine.core.bridge.ChannelBridge;

/**
 * 容器管理接口
 * @author admin
 *
 */
public interface ChannelManagerHandler {
	
	ChannelBridge get(String targetId);
	
	void add(String targetId, ChannelBridge bridge);
	
	void add(String targetId, Channel channel);
	
	void remove(String targetId);
	
	Map<String, ChannelBridge> getAll();
	
	int getSize();

}
