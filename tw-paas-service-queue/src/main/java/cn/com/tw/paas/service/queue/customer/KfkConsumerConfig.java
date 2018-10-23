package cn.com.tw.paas.service.queue.customer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.com.tw.common.core.cache.redis.RedisService;
import cn.com.tw.common.core.jms.kafka.KafkaGroupConsumer;
import cn.com.tw.common.core.jms.kafka.KafkaGroupConsumerHandler;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.service.queue.service.TerminalService;

@Component
public class KfkConsumerConfig implements CommandLineRunner{
	
	public static final String group_1 = "group-1";
	
	public static final String group_2 = "group-2";
	
	@Autowired
	private KafkaProperties kafkaProperties;
	
	@Value("${mq.queue.meter.data}")
	private String topicName;
	
	@Autowired
	private CollectDataKafkaGroup1Consumer collectDataKafkaGroup1Consumer;
	
	@Autowired
	private TerminalService terminalService;
	
	@Autowired
	private RedisService redisService;
	
	private static Logger logger = LoggerFactory.getLogger(KfkConsumerConfig.class);

	@Override
	public void run(String... arg0) throws Exception {
		new KafkaGroupConsumer(group_1, kafkaProperties.buildConsumerProperties()).subscribe(new String[]{topicName}, collectDataKafkaGroup1Consumer);
		new KafkaGroupConsumer(group_2, kafkaProperties.buildConsumerProperties()).subscribe(new String[]{topicName}, new CollectDataKafkaGroup2Consumer());
	}
	
	public class CollectDataKafkaGroup2Consumer implements KafkaGroupConsumerHandler {

		private AtomicLong count = new AtomicLong(0);
		
		@SuppressWarnings("unchecked")
		@Override
		public void listen(ConsumerRecord<?, ?> cr) {
			long reqCount = count.incrementAndGet();
			logger.info("queue-statis-data - {} - {} : {}, {}", cr.topic(), cr.key(), cr.value(), reqCount);
			
			String msg = (String) cr.value();

			// 接收到的msg转为map格式
			Map<String, Object> msgMap = JsonUtils.jsonToPojo(msg, Map.class);
			
			Map<String, Object> dataMap = null;
			if (msgMap != null) {
				// msg中有价值的数据
				dataMap = (Map<String, Object>) msgMap.get("data");
			}else{
				return;
			}
			
			//判断仪表类型
			String protocolType = (String) dataMap.get("protocolType");
			String meterAddr = (String) dataMap.get("addr");
			if ("GEHUA".equals(protocolType)) {
				// 表号 仪表地址,如果是歌华协议，热力泵DTU,终端没有编号，用MAC代替
				Object extObj = msgMap.get("ext");
				if (extObj == null) {
					logger.warn("ext is null");
					return;
				}
				Map<String, Object> extMap = (Map<String, Object>) extObj;
				meterAddr = extMap.containsKey("mac") ? (String)extMap.get("mac") : null;
			}
			
			//ValueOperations<String, String> valueops = redisTemplate.opsForValue();
			
			// 请求获取 查询仪表信息
			Response<?> equipInfo = terminalService.selectByEquipNum(meterAddr);
			Map<String, Object> terminalEquip = (Map<String, Object>) equipInfo.getData();
			if (terminalEquip != null) {
				String orgId = (String) terminalEquip.get("orgId");
				String gatherHzStr = (String) terminalEquip.get("gatherHz");
				//更新paas设备状态为正常
				//0.未通讯 1.通讯正常 2.通讯警告 3.通讯失联
				terminalService.updateStatus(meterAddr, "1");
				
				if(!StringUtils.isEmpty(gatherHzStr)){//塞入redis ,通过redis数据状态判断仪表是否正常通讯
					//仪表号，仪表所有参数 ，超时时间(秒)
					redisService.set("EX:COMM:METER:"+meterAddr, orgId, Integer.valueOf(gatherHzStr));
				}else{
					logger.error("gatherHz is null !!");
				}
			}
		}
		
	}
	
}
