package cn.com.tw.engine.core.handler.tcp.active.lora;

import io.netty.channel.Channel;

import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import cn.com.tw.common.protocol.ParseEnum;
import cn.com.tw.common.protocol.ParseFactory;
import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.engine.core.ChannelManaFactory;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.debug.DebugManager;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.entity.CmdResult;
import cn.com.tw.engine.core.handler.ChannelManagerHandler;
import cn.com.tw.engine.core.utils.MsgAysncService;
import cn.com.tw.engine.core.utils.RegCacheService;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;


/**
 * 客户端连接服务端实现
 * @author admin
 *
 */
public class MqLoraActiveGetChannelManager implements ChannelManagerHandler{
	
	private static Logger logger = LoggerFactory.getLogger(MqLoraActiveGetChannelManager.class);
	
	/**
	 * 会话
	 */
	private ConcurrentMap<String, ChannelBridge> channelMap = new ConcurrentHashMap<String, ChannelBridge>();
	
	public MsgAysncService msgAysncService;
	
	private String accessKeyId;
	
	private String accessKeySecret;
	
	private String nsaddr;
	
	private String mqUpName;
	
	private String mqDownName;
	
	private Producer producer;
	
    private RegCacheService regCacheService;
    
    private String cId;
    
    private String pId;
	
	private MqLoraActiveGetChannelManager(){
		
		this.regCacheService = (RegCacheService) SpringContext.getBean("regCacheService");
		
		this.msgAysncService = (MsgAysncService) SpringContext.getBean("msgAysncService");
		
		this.accessKeyId = Env.getVal("engine.lorawan.aliyun.accessKeyId");
		
		this.accessKeySecret = Env.getVal("engine.lorawan.aliyun.accessKeySecret");
		
		this.nsaddr = Env.getVal("engine.lorawan.aliyun.nsaddr");
		
		this.setMqUpName(Env.getVal("engine.lorawan.aliyun.mqUpName"));
		
		this.setMqDownName(Env.getVal("engine.lorawan.aliyun.mqDownName"));
		
		this.cId = Env.getVal("engine.lorawan.aliyun.cId");
		
		this.pId = Env.getVal("engine.lorawan.aliyun.pId");
		
		initConsumer();
		
		initProduct();
	}
	
	private void initConsumer(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Properties properties = new Properties();
		        // 您在 MQ 控制台创建的 Consumer ID
		        properties.put(PropertyKeyConst.ConsumerId, cId);
		        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
		        properties.put(PropertyKeyConst.AccessKey, accessKeyId);
		        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
		        properties.put(PropertyKeyConst.SecretKey, accessKeySecret);
		        // 设置 TCP 接入域名（此处以公共云公网环境接入为例）
		        properties.put(PropertyKeyConst.ONSAddr, nsaddr);

		        Consumer consumer = ONSFactory.createConsumer(properties);
		        
		        consumer.subscribe(mqUpName, "*", new MessageListener() {
					
					@Override
					public Action consume(Message message, ConsumeContext paramConsumeContext) {
						try {
							
							String bodyMsg = new String(message.getBody());
							logger.debug("---------->> receive ali lorawan plat callback, params message = {}, body = {}", message, bodyMsg);
				            
							@SuppressWarnings("unchecked")
							Map<String, Object> msgMap = JsonUtils.jsonToPojo(bodyMsg, Map.class);
							
							String dataInfo = (String) msgMap.get("data");
							
							String devEui = (String) msgMap.get("devEui");
							
							int fport = msgMap.containsKey("fport") ? (int) msgMap.get("fport") : 0;
							
							int mtype = msgMap.containsKey("mtype") ? (int) msgMap.get("mtype") : 0;
							
							int rssi = msgMap.containsKey("rssi") ? (int) msgMap.get("rssi") : 0;
							
							double snr = msgMap.containsKey("snr") ? (double) msgMap.get("snr") : 0;
							
							//暂时加上这，用来调测
							DebugManager.build().put(">> " + dataInfo + "  --fport:" + fport + ", mtype:" + mtype + ", rssi:" + rssi + ", snr:" + snr + ", devEui:" + devEui);
							
							String mac = (String) msgMap.get("devEui");
							
							CmdResponse cmdRes = new CmdResponse();
						
							Dlt645Data data = (Dlt645Data) ParseFactory.build(ParseEnum.GEHUA).decode(cn.com.tw.common.protocol.utils.ByteUtils.toByteArray(dataInfo));
							
							if (data == null) {
								logger.error("Parse data data is null , {}", data == null ? null : data.toString());
								return Action.CommitMessage;
							}
							
							cmdRes.setStatusCode(ResultCode.OPERATION_IS_SUCCESS);
							cmdRes.setData(data);
							//String uniqueId = data.getAddr() + ":" + new Date().getTime();
							String uniqueId = mac + ":" + new Date().getTime();
							cmdRes.setUniqueId(uniqueId);
							cmdRes.setExt("mac", mac);
							
							//放入结果队列
							ChannelBridge bridge = ChannelManaFactory.getInstanc().build().get(mac);
							//ChannelBridge bridge = ChannelManaFactory.getBridge(bean.getMac());
							
							/** 该判断暂时加上， 后面可能去掉 **/
							if (bridge.getPriorityQueue().size() > 0) {
								bridge.putResultRemDuplicate(new CmdResult(cmdRes));
							}
							
							//将采集的数据放入mq
							logger.debug("put kafka mq, res = {}", cmdRes);
							msgAysncService.sendCollectRes(cmdRes);
							
							return Action.CommitMessage;
						} catch(Exception e){
							logger.error("Exception e = {}", e);
							return Action.CommitMessage;
						}
						
					}
				});
		        
		        consumer.start();
		        logger.debug("Consumer Started");
			}
		}).start();
	}
	
	private void initProduct(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Properties properties = new Properties();
		        // 您在 MQ 控制台创建的 Consumer ID
		        properties.put(PropertyKeyConst.ProducerId, pId);
		        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
		        properties.put(PropertyKeyConst.AccessKey, accessKeyId);
		        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
		        properties.put(PropertyKeyConst.SecretKey, accessKeySecret);
		        //设置发送超时时间，单位毫秒
		        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
		        // 设置 TCP 接入域名（此处以公共云公网环境接入为例）
		        properties.put(PropertyKeyConst.ONSAddr, nsaddr);

		        producer = ONSFactory.createProducer(properties);
		        
		        // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可。
		        producer.start();
			}
		}).start();
	}
	
	public void send(String msg){
		new ProduerSend().async(msg);
	}
	
	public class ProduerSend {
		
		public void async(String msg){
			// Message 所属的 Topic
			Message message = new Message(mqDownName, "*", msg.getBytes());
			
			 // 异步发送消息, 发送结果通过 callback 返回给客户端。
			producer.sendAsync(message, new SendCallback() {
	            @Override
	            public void onSuccess(final SendResult sendResult) {
	                // 消费发送成功
	                logger.debug("send message success. topic=" + sendResult.getTopic() + ", msgId=" + sendResult.getMessageId());
	            }
	            @Override
	            public void onException(OnExceptionContext context) {
	                // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
	            	logger.debug("send message failed. topic=" + context.getTopic() + ", msgId=" + context.getMessageId());
	            }
	        });
		}
	}
	
	public static class Single {
		private static MqLoraActiveGetChannelManager mqLoraActiveGetChannelManager;
		static {
			mqLoraActiveGetChannelManager = new MqLoraActiveGetChannelManager();
		}
		
	    public static MqLoraActiveGetChannelManager getInstance(){
	    	return mqLoraActiveGetChannelManager;
	    }
	}
	
	public static MqLoraActiveGetChannelManager build(){
		return Single.getInstance();
	}

	@Override
	public ChannelBridge get(String meterId){
		if (StringUtils.isEmpty(meterId)){
			return null;
		}
		
		reg(meterId.toLowerCase());
		
		ChannelBridge bridge = channelMap.get(meterId);
		if (bridge == null){
			bridge = new ChannelBridge(null);
			channelMap.put(meterId.toLowerCase(), bridge);
			return bridge;
		}
		
		return bridge;
	}

	@Override
	public void add(String targetId, ChannelBridge bridge) {
		if (StringUtils.isEmpty(targetId)){
			return;
		}
		
		reg(targetId.toLowerCase());
		
		channelMap.put(targetId.toLowerCase(), bridge);
	}

	@Override
	public void remove(String targetId) {
		channelMap.remove(targetId);
	}

	@Override
	public Map<String, ChannelBridge> getAll() {
		return channelMap;
	}

	@Override
	public int getSize() {
		return channelMap.size();
	}

	@Override
	public void add(String targetId, Channel channel) {
	}

	public String getMqUpName() {
		return mqUpName;
	}

	public void setMqUpName(String mqUpName) {
		this.mqUpName = mqUpName;
	}

	public String getMqDownName() {
		return mqDownName;
	}

	public void setMqDownName(String mqDownName) {
		this.mqDownName = mqDownName;
	}
	
	public void reg(String targetId){
		if (regCacheService == null) {
			return;
		}
		regCacheService.regisHostPort(targetId);
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

}
