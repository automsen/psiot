package cn.com.tw.engine.core.debug;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class DebugWebSocketServer implements Runnable{

	private int port;
	
    public DebugWebSocketServer(int port) {
    	this.port = port;
    }

	@Override
	public void run() {
		//创建线程组
        EventLoopGroup bossgroup = new NioEventLoopGroup();
        EventLoopGroup workergroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossgroup, workergroup).
                    channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //将请求和应答消息编码或解码为HTTP消息
                            pipeline.addLast("http-codec", new HttpServerCodec());
                            //将HTTP消息的多个部分组合成一条完整的HTTP消息
                            pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            //用于支持浏览器和服务端进行websocket通信z
                            pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                            //增加websocket服务端handler
                            pipeline.addLast("handler", new WebSocketServerHandler());
                        }
                    });
            System.out.println("服务端开启等待客户端连接 ... ...");
            Channel ch = b.bind(port).sync().channel();
            System.out.println("端口：" + port + ".");
            System.out.println("连接地址：http://localhost:" + port + "/");
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //退出，是否线程组资源
            bossgroup.shutdownGracefully();
            workergroup.shutdownGracefully();
        }
		
	}
}
