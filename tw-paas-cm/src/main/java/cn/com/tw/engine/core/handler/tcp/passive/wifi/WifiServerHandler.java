package cn.com.tw.engine.core.handler.tcp.passive.wifi;

import io.netty.channel.Channel;
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
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.CmdResult;
import cn.com.tw.engine.core.entity.WarnInfo;
import cn.com.tw.engine.core.entity.eum.TermStatus;
import cn.com.tw.engine.core.handler.tcp.passive.container.ServerPassiveChannelManager;
import cn.com.tw.engine.core.utils.MsgAysncService;
import cn.com.tw.engine.core.utils.cons.Cons;

@Sharable
public class WifiServerHandler extends ChannelInboundHandlerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(WifiServerHandler.class);
	
	private MsgAysncService msgAysncService;
	
	private ChannelManaFactory channelManaFactory = ChannelManaFactory.getInstanc();
	
	public WifiServerHandler(){
		this.msgAysncService = (MsgAysncService) SpringContext.getBean("msgAysncService");
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
		
		Dlt645Data data645 = (Dlt645Data) ((CmdResponse) msg).getData();
		
		String dataField = data645.getDataField();
		
		if (StringUtils.isEmpty(dataField)){
			return;
		}
		
		//更新设备Id 和 信道的对应 关系
		updateChannelMap(data645.getAddr(), (SocketChannel) ctx.channel());
		
		//如果是返回的读的控制码，写入mq，同时，放入队列
		if ("91".equals(data645.getCotrolCode())){
			
			CmdResponse cmdResp = (CmdResponse) msg;
			
			if (!ResultCode.OPERATION_IS_SUCCESS.equals(cmdResp.getStatusCode())){
				logger.warn("msg data can not into mq!!!, statuCode is not 000000,  cmdresp = {}", msg);
				return;
			}
			
			msgAysncService.sendCollectRes((CmdResponse) msg);
			
		}else {
			
		}
		
		ChannelBridge channelBridge = channelManaFactory.getBridge(ctx.channel().id().asLongText());
		
		if (channelBridge == null) {
			return;
		}
		
		channelBridge.putResult(new CmdResult((CmdResponse) msg));
		
		
		/*BlockingQueue<CmdResponse> respQueue = ResponseMap.get(ctx.channel().id().asLongText());
		
		if (respQueue == null){
			return;
		}
		
		try {
			respQueue.put((CmdResponse) msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
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
				
				//移除
				/*String clientId = remove(ctx);
				
				logger.error("{} clientId = {} , disconnect server....no receive read msg(900s)", new Object[]{SysCons.LOG_ERROR, clientId} );
				
				asyncTask(clientId, TermStatus.close.getValue());
				
				//清除超时会话
				ctx.close();*/
				
			//设置10秒 服务器接受发送心跳，
			} else if (event.state().equals(IdleState.WRITER_IDLE)) {
				

			//设置900秒 15分钟
			} else {

				/*//移除
				String clientId = remove(ctx);

				logger.error("{} clientId = {} , disconnect server....no receive read and writer msg(900s)", new Object[]{SysCons.LOG_ERROR, clientId} );
				
				asyncTask(clientId, TermStatus.close.getValue());
				
				//清除超时会话
				ctx.close();*/
				
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
		logger.debug("***************** connecting  **************** There's a new client coming in the engine. channel active id = {}, name = {}", new Object[]{socketChannel.id().asLongText(), ctx.name()});
	}

	/**
	 * 客户端断开	
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String clientId = remove(ctx);
		
		logger.error("{} clientId = {} disconnect , happen in channelInactive method..", new Object[]{SysCons.LOG_ERROR, clientId} );
		
		asyncTask(clientId, TermStatus.close.getValue());
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	SocketChannel socketChannel = (SocketChannel)ctx.channel();
		String clientId = ((ServerPassiveChannelManager)channelManaFactory.build()).remove(socketChannel);
    	asyncTask(clientId, TermStatus.close.getValue());
    	
    	logger.error("{} clientId = {} exception disconnect , happen in exceptionCaught method.. e = {} ", new Object[]{SysCons.LOG_ERROR, clientId, cause.toString()} );
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
    
    /**
     * 判断是否更新channelMap
     * @param addr
     * @param socketChannel
     */
    private void updateChannelMap(String addr, SocketChannel socketChannel){
    	
    	ChannelBridge bridge = channelManaFactory.build().get(addr);
    	
    	Channel channel = bridge.getChannel();
    	
    	if (channel == null || !channel.isActive() || !channel.id().asLongText().equals(socketChannel.id().asLongText())){
    		
    		channelManaFactory.build().add(addr, new ChannelBridge(socketChannel));
    		
    	}
    	
    }
}