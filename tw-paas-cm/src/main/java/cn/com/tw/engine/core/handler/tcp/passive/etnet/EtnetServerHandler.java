package cn.com.tw.engine.core.handler.tcp.passive.etnet;

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
import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.engine.core.ChannelManaFactory;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.CmdResult;
import cn.com.tw.engine.core.entity.WarnInfo;
import cn.com.tw.engine.core.entity.eum.TermStatus;
import cn.com.tw.engine.core.utils.EngineUtils;
import cn.com.tw.engine.core.utils.MsgAysncService;
import cn.com.tw.engine.core.utils.cons.Cons;

@Sharable
public class EtnetServerHandler extends ChannelInboundHandlerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(EtnetServerHandler.class);
	
	private MsgAysncService msgAysncService;
	
	//private DeviceDataHandler deviceDataHandler;
	
	private ChannelManaFactory channelManaFactory = ChannelManaFactory.getInstanc();
	
	public EtnetServerHandler(){
		this.msgAysncService = (MsgAysncService) SpringContext.getBean("msgAysncService");
		//this.deviceDataHandler = (DeviceDataHandler) SpringContext.getBean("deviceDataHandler");
	}

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		
		logger.debug("channelRead -------" + msg);
		
		if (msg == null){
			return;
		}
		
		if (!(msg instanceof CmdResponse)){
			return;
		}
		
		CmdResponse msgRes = (CmdResponse) msg;
		
		Dlt645Data data645 = (Dlt645Data) msgRes.getData();
		
		/*CmdResponse msgRes = (CmdResponse) msg;
		
		DLT645Data data645 = (DLT645Data) msgRes.getData();
		
		//通过仪表ID 获取网关ID
		logger.warn("mode type is auto collecting , Find gwID first through the frequency configuration, if not, find the device configuration");
		String gwId = DataUtils.getGwId(data645.getAddr());
		
		if(StringUtils.isEmpty(gwId)) {
			gwId = deviceDataHandler.getGwId(data645.getAddr());
		}
		
		if (StringUtils.isEmpty(gwId)){
			logger.error("meterAddr : {}, can not find meterAddr's gwId, gwId = {}", new Object[]{data645.getAddr(), gwId});
			return;
		}*/
		
		//以太网被动连接，群发！！！！通过仪表Id 查找网关Id， 在去查找网关对应的bridge
		ChannelBridge channelBridge = channelManaFactory.getBridge(null);
		
		if (channelBridge == null) {
			logger.error("channelBridge : {}, can not find ChannelBridge by gwId, gwId = {}", new Object[]{channelBridge, null});
			return;
		}
		
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
		
		logger.debug("channelRead ------- complete !!");
	}
	
	/**
	 * 监听心跳并且处理
	 */
	@Override
	public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
		
		if(evt instanceof IdleStateEvent){
			IdleStateEvent event = (IdleStateEvent)evt;
			
			//如果900秒，没有读操作，认为是超时，关闭资源
			if (event.state().equals(IdleState.READER_IDLE)) {
				
				
			//设置10秒 服务器接受发送心跳，
			} else if (event.state().equals(IdleState.WRITER_IDLE)) {
				

			//设置900秒 15分钟
			} else {

				
			}
		}else{
			super.userEventTriggered(ctx, evt);
		}
	}
	
	/**
	 * 新客户端接入
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		SocketChannel socketChannel = (SocketChannel)ctx.channel();
		//添加到队列组中
		channelManaFactory.build().add(null, ctx.channel());
		logger.debug("***************** connecting  **************** There's a new client coming in the engine. channel active id = {}, name = {}", new Object[]{socketChannel.id().asLongText(), ctx.name()});
	}

	/**
	 * 客户端断开	
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
		//移除从队列组中
		((EtnetChannelManager)channelManaFactory.build()).remove(ctx.channel());
		
		logger.error("{} clientId = {} disconnect , happen in channelInactive method..", new Object[]{SysCons.LOG_ERROR, ""} );
		
		asyncTask("", TermStatus.close.getValue());
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		
		//移除从队列组中
		((EtnetChannelManager)channelManaFactory.build()).remove(ctx.channel());
		
    	asyncTask("", TermStatus.close.getValue());
    	
    	logger.error("{} clientId = {} exception disconnect , happen in exceptionCaught method.. e = {} ", new Object[]{SysCons.LOG_ERROR, "", cause.toString()} );
        cause.printStackTrace();
        ctx.close();
    }
    
    private void asyncTask(String clientId, int status){
    	msgAysncService.sendWarnInfo(new WarnInfo(clientId, Cons.COLLECT_GW_3G4G, status));
    }
    
}
