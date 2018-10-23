package cn.com.tw.engine.core.handler.tcp.passive;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;

import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.engine.core.handler.eum.ProtocolEum;
import cn.com.tw.engine.core.netty.decoder.Cl645ProtocolDecoder;
import cn.com.tw.engine.core.netty.decoder.MonBusProtocolDecoder;
import cn.com.tw.engine.core.netty.encoder.Cl645ProtocolEncoder;
import cn.com.tw.engine.core.netty.encoder.MonBusProtocolEncoder;

public class TcpServer {
	
	private int bossCount = 2;
	
	private int workerCount = 2;
	
	private int tcpPort = 2020;
	
	private boolean keepAlive = true;
	
	private ChannelHandler channelHandler;
	
	//engine.tcp.server.readTimeout
	private int readTimeout = 1800;
	
	//engine.tcp.server.allTimeout
	private int allTimeout = 1800;
	
	//engine.tcp.server.writeTimeout
	private int writeTimeout = 1800;
	
	private ProtocolEum protocolEum = ProtocolEum.DLT645V2007;
	
	public TcpServer(){
		
		init();
		
	}
	
	public TcpServer(ChannelHandler channelHandler){
		
		init();
		
		this.channelHandler = channelHandler;
		
	}
	
	public TcpServer setProtocol(ProtocolEum protocol){
		this.protocolEum = protocol;
		return this;
	}
	
	private ChannelHandler getDecoder() {
		switch (this.protocolEum) {
		case DLT645V2007:
			return new Cl645ProtocolDecoder();
		case MODBUS:
			return new MonBusProtocolDecoder();
		default:
			return new Cl645ProtocolDecoder();
		}
	}
	
	private ChannelHandler getEncoder() {
		switch (this.protocolEum) {
		case DLT645V2007:
			return new Cl645ProtocolEncoder();
		case MODBUS:
			return new MonBusProtocolEncoder();
		default:
			return new Cl645ProtocolEncoder();
		}
	}
	
	private void init(){
		
		try {
			bossCount = Integer.parseInt(Env.getVal("engine.tcp.bossThreadCount"));
			
			workerCount = Integer.parseInt(Env.getVal("engine.tcp.workerThreadCount"));
			
			tcpPort = Integer.parseInt(Env.getVal("engine.tcp.port"));
			
			readTimeout = Integer.parseInt(Env.getVal("engine.tcp.server.readTimeout"));
			
			writeTimeout = Integer.parseInt(Env.getVal("engine.tcp.server.writeTimeout"));
			
			allTimeout = Integer.parseInt(Env.getVal("engine.tcp.server.allTimeout"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start() throws InterruptedException{
		
		if (this.channelHandler == null) {
			
			throw new RuntimeException();
			
		}
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(new NioEventLoopGroup(bossCount), new NioEventLoopGroup(workerCount))
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                	ch.pipeline().addLast(new IdleStateHandler(readTimeout, writeTimeout, allTimeout));
                	ch.pipeline().addLast(getDecoder());
					ch.pipeline().addLast(getEncoder());
                    ch.pipeline().addLast(channelHandler);
                }
            })
			.option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, keepAlive);
		
		
		@SuppressWarnings("unused")
		Channel serverChannel = bootstrap.bind(new InetSocketAddress(tcpPort)).sync().channel().closeFuture().sync().channel();
		
	}

}
