package cn.com.tw.engine.core.handler.tcp.passive.etnet;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.engine.core.ChannelManaFactory;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.WarnInfo;
import cn.com.tw.engine.core.entity.eum.TermStatus;
import cn.com.tw.engine.core.handler.tcp.passive.container.ServerPassiveChannelManager;
import cn.com.tw.engine.core.utils.EngineUtils;
import cn.com.tw.engine.core.utils.MsgAysncService;
import cn.com.tw.engine.core.utils.cons.Cons;

@Sharable
public class EtnetPassiveRevServerHandler extends ChannelInboundHandlerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(EtnetPassiveRevServerHandler.class);
	
	private MsgAysncService msgAysncService;
	
	//private DeviceDataHandler deviceDataHandler;
	
	public EtnetPassiveRevServerHandler(){
		this.msgAysncService = (MsgAysncService) SpringContext.getBean("msgAysncService");
		//this.deviceDataHandler = (DeviceDataHandler) SpringContext.getBean("deviceDataHandler");
	}
	
	private ChannelManaFactory channelManaFactory = ChannelManaFactory.getInstanc();

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		
		if (msg == null){
			return;
		}
		
		if (!(msg instanceof CmdResponse)){
			return;
		}
		
		CmdResponse msgRes = (CmdResponse) msg;
		
		Dlt645Data data645 = (Dlt645Data) msgRes.getData();
		
		//判断异常情况,如果协议解析失败，这个时候拿不到仪表ID，无法放入对应的队列里面
		if (!msgRes.getStatusCode().equals(ResultCode.OPERATION_IS_SUCCESS) || data645 == null){
			
			logger.error("CmdResponse is null and not exits meterAddr, message can not put queue of channel!!!");
			
			return;
		}
		
		String dataField = data645.getDataField();
		
		String dataMaker = data645.getDataMarker();
		
		dataMaker = dataMaker == null ? "" : dataMaker;
		
		//如果是登录
		if ("09000090".equals(dataField)){
			
			logger.debug("--------------receive gprs login.");
			
			CmdRequest cmdReq = new CmdRequest();
			cmdReq.setContent(data645.getAddr() + ",91,#09000090");
			
			//添加网关Id和通道的关系
			if (channelManaFactory.getBridge(data645.getAddr()) == null) {
				channelManaFactory.addGwBridge(data645.getAddr(), (SocketChannel) ctx.channel());
				//ChannelManaFactory.build().add(data645.getAddr(), new ChannelBridge((SocketChannel) ctx.channel()));
			}
			
			//地址打开
			asyncTask(data645.getAddr(), TermStatus.open.getValue());
			
			ctx.writeAndFlush(cmdReq);
			
		} else if ("0A000090".equals(dataField.toUpperCase())){//如果是心跳，收到同时回复
			
			logger.debug("-----------> receive gprs heartbeat.");
			//放入心跳队列
			msgAysncService.sendCollectRes(msgRes);
			
			CmdRequest cmdReq = new CmdRequest();
			cmdReq.setContent(data645.getAddr() + ",91,#0A000090");
			//ChannelManaFactory.build().add(data645.getAddr(), new ChannelBridge((SocketChannel) ctx.channel()));
			ChannelBridge channelBridge = channelManaFactory.getBridge(data645.getAddr());
			if (channelBridge == null) {
				channelManaFactory.addGwBridge(data645.getAddr(), (SocketChannel) ctx.channel());
				//ChannelManaFactory.build().add(data645.getAddr(), new ChannelBridge((SocketChannel) ctx.channel()));
			}else{
				//更新信道
				channelManaFactory.updateGwBridge(data645.getAddr(), (SocketChannel) ctx.channel());
			}
			
			ctx.writeAndFlush(cmdReq);
		
		//判断是否网关返回的数据帧
		} else if ("04700102".equals(dataMaker) || "04700103".equals(dataMaker) || "04700201".equals(dataMaker)){
			
			logger.debug("----------> receive gprs gw return.");
			
			//通过仪表Id 查找网关Id， 在去查找网关对应的bridge
			ChannelBridge channelBridge = channelManaFactory.getBridge(data645.getAddr());
			
			if (channelBridge == null) {
				logger.error("channelBridge : {}, can not find ChannelBridge by gwId, gwId = {}", new Object[]{channelBridge, data645.getAddr()});
				return;
			}
			
			//更新信道
			channelManaFactory.updateGwBridge(data645.getAddr(), (SocketChannel) ctx.channel());
			
			//放入队列的信息， 包括主动上传的数据和 下发指令返回的数据，去重操作
			try {
				channelManaFactory.putResultGwBridgeRemDuplicate(data645.getAddr(), (CmdResponse) msg);
				//channelBridge.putResultRemDuplicate(new CmdResult((CmdResponse) msg));
			} catch (Exception e) {
				logger.error("channelBridge.putResultRemDuplicate, exception : e = {} ", e);
			}
		
		}  else {
			
			logger.debug("----------> receive gprs meter of gw return.");
			
			//将采集的数据放入mq
			msgAysncService.sendCollectRes((CmdResponse) msg);
			
			//如果是事件状态字，//特殊处理，关于事件状态字，如果返回了事件状态字，这个时候异步去下发复位指令，
			if ("04001501".equals(dataMaker)) {
				CmdRequest cmdR = new CmdRequest();
				String statusContent = data645.getDataValues().substring(0, 96);
				String content = data645.getAddr() + ",14,04001503,00000002," + EngineUtils.getOpposite(statusContent);
				cmdR.setContent(content);
				ctx.writeAndFlush(cmdR);
				return;
			}
			
			//通过仪表ID 获取网关ID
			String gwId = channelManaFactory.getBridgeGwIdByChannelId(ctx.channel().id().asLongText());
			//String gwId = deviceDataHandler.getGwId(data645.getAddr());
			
			if (StringUtils.isEmpty(gwId)){
				logger.error("channelId: {}, meterAddr : {}, can not find meterAddr's gwId, gwId = {}", new Object[]{ctx.channel().id().asLongText(), data645.getAddr(), gwId});
				return;
			}
			
			logger.debug("channelId: {}, meterAddr : {}, to find gwId : {}", new Object[]{ctx.channel().id().asLongText(), data645.getAddr(), gwId});
			
			//通过仪表Id 查找网关Id， 在去查找网关对应的bridge
			/*ChannelBridge channelBridge = channelManaFactory.getBridgeGwIdByChannelId(gwId);
			
			if (channelBridge == null) {
				logger.error("channelBridge : {}, can not find ChannelBridge by gwId, gwId = {}", new Object[]{channelBridge, gwId});
				return;
			}*/
			
			//更新信道
			channelManaFactory.updateGwBridge(gwId, (SocketChannel) ctx.channel());
			
			//channelBridge.updateChannel((SocketChannel) ctx.channel());
			
			//放入队列的信息， 包括主动上传的数据和 下发指令返回的数据，去重操作
			try {
				channelManaFactory.putResultGwBridgeRemDuplicate(gwId, (CmdResponse) msg);
				//channelBridge.putResultRemDuplicate(new CmdResult((CmdResponse) msg));
			} catch (Exception e) {
				logger.error("channelBridge.putResultRemDuplicate, exception : e = {} ", e);
			}
			
		}
		
	}
	
	/**
	 * 监听心跳并且处理
	 */
	@Override
	public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
		
		if(evt instanceof IdleStateEvent){
			IdleStateEvent event = (IdleStateEvent)evt;
			
			//如果900秒（默认），没有读操作，认为是超时，关闭资源
			if (event.state().equals(IdleState.READER_IDLE)) {
				//移除
				String clientId = remove(ctx);
				
				logger.error("{} clientId = {} , disconnect server....no receive read msg(900s)", new Object[]{SysCons.LOG_ERROR, clientId} );
				
				asyncTask(clientId, TermStatus.close.getValue());
				
				//清除超时会话
				ctx.close();
				
			//设置10秒（默认） 服务器接受发送心跳，
			} else if (event.state().equals(IdleState.WRITER_IDLE)) {
				

			//设置900秒 15分钟
			} else {

				//移除
				String clientId = remove(ctx);

				logger.error("{} clientId = {} , disconnect server....no receive read and writer msg(900s)", new Object[]{SysCons.LOG_ERROR, clientId} );
				
				asyncTask(clientId, TermStatus.close.getValue());
				
				//清除超时会话
				ctx.close();
				
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
		//SocketChannel socketChannel = (SocketChannel)ctx.channel();
		//logger.debug("** connecting  ** There's a new client coming in the engine. channel active id = {}, name = {}", new Object[]{socketChannel.id().asLongText(), ctx.name()});
	}

	/**
	 * 客户端断开	
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String clientId = remove(ctx);
		
		if (!StringUtils.isEmpty(clientId)) {
			logger.error("** disconnect **{} clientId = {}, happen in channelInactive method..", new Object[]{SysCons.LOG_ERROR, clientId} );
			asyncTask(clientId, TermStatus.close.getValue());
		}
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	SocketChannel socketChannel = (SocketChannel)ctx.channel();
		String clientId = ((ServerPassiveChannelManager)channelManaFactory.build()).remove(socketChannel);
    	
		if (!StringUtils.isEmpty(clientId)) {
			asyncTask(clientId, TermStatus.close.getValue());
			logger.error("e = {} ", cause);
		}
        cause.printStackTrace();
        ctx.close();
    }
    
    public String remove(ChannelHandlerContext ctx){
    	SocketChannel socketChannel = (SocketChannel)ctx.channel();
		String clientId = ((ServerPassiveChannelManager)channelManaFactory.build()).remove(socketChannel);
		return clientId;
    }
    
    private void asyncTask(String clientId, int status){
    	msgAysncService.sendWarnInfo(new WarnInfo(clientId, Cons.COLLECT_GW_3G4G, status));
    }
    
}