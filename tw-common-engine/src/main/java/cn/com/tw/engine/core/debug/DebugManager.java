package cn.com.tw.engine.core.debug;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.utils.env.Env;

public class DebugManager {
	
	private int isDebuger = 0;
	
	private BlockingQueue<EleInfo> debugQueue = new ArrayBlockingQueue<EleInfo>(60);
	
	private ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	private Byte[] lock = new Byte[0];
	
	private DebugManager(){
		String debugger = Env.getVal("engine.debuger.switch");
		if (!StringUtils.isEmpty(debugger)) {
			try {
				this.isDebuger = Integer.parseInt(debugger);
			} catch (NumberFormatException e) {
			}
		}
	}
	
	public static class Single {
		
		private static DebugManager debugManager;
		
		static {
			debugManager = new DebugManager();
		}
		
		public static DebugManager getIns(){
			return debugManager;
		}
		
	}
	
	public static DebugManager build() {
		return DebugManager.Single.getIns();
	}
	
	public void put(String content) {
		
		if (!isDebugger()) {
			return;
		}
		
		synchronized (lock) {
			
			EleInfo eleInfo = new EleInfo(Thread.currentThread().getName(), content, new Date());
			try {
				debugQueue.put(eleInfo);
			} catch (Exception e) {
				debugQueue.poll();
				try {
					debugQueue.put(eleInfo);
				} catch (Exception e1) {
				}
			}
			
		}
		
	}
	
	public EleInfo take() {
		try {
			EleInfo eleInfo = debugQueue.take();
			return eleInfo;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void clear(){
		debugQueue.clear();
	}
	
	public boolean isDebugger(){
		if (isDebuger == 1) {
			return true;
		}
		return false;
	}
	
	public void addGroup(Channel channel){
		group.add(channel);
	}
	
	public void removeGroup(Channel channel) {
		group.remove(channel);
	}
	
	public void sendGroup(String jsonMsg){
		if (group.size() == 0) {
			return;
		}
		TextWebSocketFrame tws = new TextWebSocketFrame(jsonMsg);
		group.writeAndFlush(tws);
	}
	
	public void init(int port) {
		
		if (!isDebugger()) {
			return;
		}
		new Thread(new DebugWebSocketServer(port)).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					EleInfo eleInfo = take();
					sendGroup(JsonUtils.objectToJson(eleInfo));
				}
			}
		}).start();
		
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					sendGroup(JsonUtils.objectToJson(new EleInfo(Thread.currentThread().getName(), ">> FEFEFE123123124123123SDSDAS", new Date())));
				}
			}
		}).start();*/
	}

}
