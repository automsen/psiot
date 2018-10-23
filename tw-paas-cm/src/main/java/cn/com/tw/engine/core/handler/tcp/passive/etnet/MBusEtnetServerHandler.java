package cn.com.tw.engine.core.handler.tcp.passive.etnet;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.engine.core.ChannelManaFactory;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.WarnInfo;
import cn.com.tw.engine.core.entity.eum.TermStatus;
import cn.com.tw.engine.core.handler.tcp.passive.container.ServerPassiveChannelManager;
import cn.com.tw.engine.core.utils.MsgAysncService;
import cn.com.tw.engine.core.utils.cons.Cons;

@Sharable
public class MBusEtnetServerHandler extends ChannelInboundHandlerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(MBusEtnetServerHandler.class);
	
	private MsgAysncService msgAysncService;
	
    //private DeviceDataHandler deviceDataHandler;
    
    private ChannelManaFactory channelManaFactory = ChannelManaFactory.getInstanc();
	
	public MBusEtnetServerHandler(){
		this.msgAysncService = (MsgAysncService) SpringContext.getBean("msgAysncService");
		//this.deviceDataHandler = (DeviceDataHandler) SpringContext.getBean("deviceDataHandler");
	}

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		
		try {
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
			
			String addr = data645.getAddr();
			
			String code = data645.getCotrolCode();
			if ("0".equals(addr) && "FF".equals(code.toUpperCase())) {
				
				String gwAddr = data645.getDataValues();
				
				String gwAddrLong = String.format("%08d", Long.parseLong(gwAddr, 16));
				
				//添加网关Id和通道的关系
				channelManaFactory.addGwBridge(gwAddrLong, (SocketChannel) ctx.channel());
				
/*				if (ChannelManaFactory.getBridge(gwAddrLong) == null) {
					ChannelManaFactory.build().add(gwAddrLong, new ChannelBridge((SocketChannel) ctx.channel()));
				}
*/				
				logger.debug("-----------> receive gprs gw heartbeat.");
				
				return;
			}
			
			//通过仪表ID 获取网关ID
			String gwId = channelManaFactory.getBridgeGwIdByChannelId(ctx.channel().id().asLongText());
			//String gwId = deviceDataHandler.getGwId(data645.getAddr());
			
			if (StringUtils.isEmpty(gwId)){
				logger.error("meterAddr : {}, can not find meterAddr's gwId, gwId = {}", new Object[]{data645.getAddr(), gwId});
				return;
			}
			
			
			msgRes.setUniqueId(gwId + StringUtils.leftPad(data645.getAddr(), 3, "0") + ":" + new Date().getTime());
			
			//通过仪表Id 查找网关Id， 在去查找网关对应的bridge
			/*ChannelBridge channelBridge = channelManaFactory.getBridgeGwIdByChannelId(gwId);
			
			if (channelBridge == null) {
				logger.error("channelBridge : {}, can not find ChannelBridge by gwId, gwId = {}", new Object[]{channelBridge, gwId});
				return;
			}*/
			
			channelManaFactory.putResultGwBridge(gwId, msgRes);
			//channelBridge.putResult(new CmdResult(msgRes));
			
		} catch (Exception e) {
			logger.error("channelRead data error, e = {}", e);
		}
			
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
	}

	/**
	 * 客户端断开	
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
		SocketChannel socketChannel = (SocketChannel)ctx.channel();
		
		//移除从队列组中
		String clientId = ((ServerPassiveChannelManager)channelManaFactory.build()).remove(socketChannel);
		
		if (!StringUtils.isEmpty(clientId)) {
			logger.error("** disconnect **{} clientId = {}, happen in channelInactive method..", new Object[]{SysCons.LOG_ERROR, clientId} );
			asyncTask(clientId, TermStatus.close.getValue());
		}
		
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	SocketChannel socketChannel = (SocketChannel)ctx.channel();
    	
		//移除从队列组中
		String clientId = ((ServerPassiveChannelManager)channelManaFactory.build()).remove(socketChannel);
		
		if (!StringUtils.isEmpty(clientId)) {
			asyncTask(clientId, TermStatus.close.getValue());
			logger.error("e = {} ", cause);
		}
        cause.printStackTrace();
        ctx.close();
    }
    
    private void asyncTask(String clientId, int status){
    	msgAysncService.sendWarnInfo(new WarnInfo(clientId, Cons.COLLECT_GW_3G4G, status));
    }
    
}
