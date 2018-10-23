package cn.com.tw.engine.core.handler.tcp.active.etnet;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.engine.core.ChannelManaFactory;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.CmdResult;
import cn.com.tw.engine.core.utils.EngineUtils;

@Sharable
public class EtnetActiveGetClientHandler extends ChannelInboundHandlerAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(EtnetActiveGetClientHandler.class);
	
	//private DeviceDataHandler deviceDataHandler;
	
	public EtnetActiveGetClientHandler(){
		//this.deviceDataHandler = (DeviceDataHandler) SpringContext.getBean("deviceDataHandler");
	}
	
	private ChannelManaFactory channelManaFactory = ChannelManaFactory.getInstanc();
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		
		if (msg == null){
			return;
		}
		
		CmdResponse msgRes = (CmdResponse) msg;
		
		Dlt645Data data645 = (Dlt645Data) msgRes.getData();
		
		//通过仪表ID 获取网关ID
		/*logger.warn("mode type is auto collecting , Find gwID first through the frequency configuration, if not, find the device configuration");
		String gwId = DataUtils.getGwId(data645.getAddr());
		
		if(StringUtils.isEmpty(gwId)) {
			gwId = deviceDataHandler.getGwId(data645.getAddr());
		}
		
		if (StringUtils.isEmpty(gwId)){
			logger.error("meterAddr : {}, can not find meterAddr's gwId, gwId = {}", new Object[]{data645.getAddr(), gwId});
			return;
		}
		
		ChannelBridge channelBridge = ChannelManaFactory.getBridge(gwId);
		
		if (channelBridge == null) {
			logger.error("channelBridge : {}, can not find ChannelBridge by gwId, gwId = {}", new Object[]{channelBridge, gwId});
			return;
		}
		*/
		
		//通过仪表Id 查找网关Id， 在去查找网关对应的bridge
		ChannelBridge channelBridge = channelManaFactory.getBridge(ctx.channel().id().asLongText());
		
		channelBridge.putResult(new CmdResult((CmdResponse) msg));
		
		
		//如果是事件状态字，//特殊处理，关于事件状态字，如果返回了事件状态字，这个时候异步去下发复位指令，
		if ("04001501".equals(data645.getDataMarker())) {
			CmdRequest cmdR = new CmdRequest();
			String statusContent = data645.getDataValues().substring(0, 96);
			String content = data645.getAddr() + ",14,04001503," + EngineUtils.getOpposite(statusContent);
			cmdR.setContent(content);
			ctx.writeAndFlush(cmdR);
			return;
		}
		
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		String clientId = remove(ctx);
		logger.error("{} clientId = {} exception disconnect , happen in exceptionCaught method.. e = {} ", new Object[]{SysCons.LOG_ERROR, clientId, cause.toString()} );
        ctx.close();
    }
	
	@Override
	public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
		
		if(evt instanceof IdleStateEvent){
			IdleStateEvent event = (IdleStateEvent)evt;
			logger.warn("操作类型:" + event.state());
			
			//如果25秒，没有读操作，认为是超时，关闭资源
			if (event.state().equals(IdleState.READER_IDLE)) {
				//清除超时会话
				ctx.close();
				
			//设置10秒 向服务器发送心跳，
			} else if (event.state().equals(IdleState.WRITER_IDLE)) {
				ctx.channel().writeAndFlush("ping");
			//设置5秒钟
			} else {
				//ctx.channel().writeAndFlush("ping");
			}
		}else{
			logger.warn("event info :" + evt.toString());
			super.userEventTriggered(ctx, evt);
		}
	}
	
	/**
	 * 客户端断开	
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String clientId = remove(ctx);
		logger.error("***************** disconnect  ****************{} clientId = {}, happen in channelInactive method..", new Object[]{SysCons.LOG_ERROR, clientId} );
		ctx.close();
	}
	
	public String remove(ChannelHandlerContext ctx){
    	SocketChannel socketChannel = (SocketChannel)ctx.channel();
		String clientId = ((EtnetActiveGetChannelManager)channelManaFactory.build()).remove(socketChannel);
		return clientId;
	}

}
