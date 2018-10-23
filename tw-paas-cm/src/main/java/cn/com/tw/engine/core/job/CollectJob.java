package cn.com.tw.engine.core.job;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.common.utils.tools.quartz.utils.CronExUtil;
import cn.com.tw.common.utils.tools.quartz.utils.DateFormatUtil;
import cn.com.tw.engine.core.ChannelManaFactory;
import cn.com.tw.engine.core.bridge.ChannelBridge;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.entity.DataItemGroup;
import cn.com.tw.engine.core.entity.Meter;
import cn.com.tw.engine.core.exception.EngineException;
import cn.com.tw.engine.core.handler.eum.ProtocolEum;
import cn.com.tw.engine.core.handler.tcp.passive.container.ServerPassiveChannelManager;
import cn.com.tw.engine.core.thread.CollectThread;
import cn.com.tw.engine.core.thread.CusThreadPoolFactory;
import cn.com.tw.engine.core.utils.DataUtils;
import cn.com.tw.engine.core.utils.cons.Cons;

/**
 * 定时采集任务,获取需求采集的 仪表和网关的信息
 * @author admin
 *
 */
//@DisallowConcurrentExecution
public class CollectJob implements Job{
	
	private static Logger logger = LoggerFactory.getLogger(CollectJob.class);
	
	private static ChannelManaFactory channelManaFactory = ChannelManaFactory.getInstanc();
	
	public CollectJob(){
		
	}
	
	static {
		
		logger.debug("===init collect excute thread ");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					boolean flag = excuteTask();
					
					if (!flag) {
						try {
							Thread.sleep(20000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
		
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		/**
		 * 如果没有客户端联系过来，跳过
		 */
		if (channelManaFactory.build() instanceof ServerPassiveChannelManager){
			
			if (channelManaFactory.build().getSize() == 0){
				
				logger.warn("monitoring real data, channel number is 0, There is no client connection!!");
				
				return;
			}
			
		}
		
		//判断队列数据 如果存在则 不执行
		if (isExistQueData()) {
			logger.warn("collect queue data is not null, no start!!!!");
			return;
		}
		
		Date currdate = context.getScheduledFireTime();
		
		try {
			
			//缓存中获取数据项和仪表组的对应关系
			List<DataItemGroup> dataItemGroups = DataUtils.readDataAll();
			
			if (dataItemGroups == null){
				
				logger.error("*********************************dataitem info is empty, task can not complete!!!!");
				
				return;
			}
			
			//遍历数据项
			for (DataItemGroup dataItemGroup : dataItemGroups){
				
				if (!CronExpression.isValidExpression(dataItemGroup.getCollectCron())){
					
					logger.error("*********************************dataitem CollectCron format error!!!!");
					
					continue;
				}
				
				long cornIntervMseconds = CronExUtil.getIntervalTime(dataItemGroup.getCollectCron());
				
				CronExpression exp = new CronExpression(dataItemGroup.getCollectCron());
				//判断时间和规则上是否匹配
				if (exp.isSatisfiedBy(currdate)){
					
					//获取该数据项对应的仪表
					List<Meter> meters = dataItemGroup.getMeters();
					
					//点次数
					int dotTimes = 0;
					try {
						dotTimes = getDotTimes(dataItemGroup, cornIntervMseconds);
					} catch (Exception e1) {
						continue;
					}
					
					//遍历仪表，查找所有下面的仪表，再遍历每个仪表需要采集的数据项，拼成指令 放入队列中
					for (Meter meter : meters){
						
						try {
							ChannelBridge channelBridge = connect(meter.getGwId());
							
							if (channelBridge == null){
								logger.error("connect gw by gwId {} , channelBridge = null", meter.getGwId());
								continue;
							}
							
							CmdRequest cmdReq = null;
							try {
								cmdReq = packObj(dataItemGroup, meter, currdate, dotTimes, cornIntervMseconds);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								continue;
							}
							
							channelBridge.putRemDuplicate(cmdReq);
						} catch (EngineException e) {
							// TODO Auto-generated catch block
							logger.error(SysCons.LOG_ERROR + " collecting data exception, e = {} " ,e);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error(SysCons.LOG_ERROR + " collecting data exception, e = {} ", e);
						}
						
					}
					
				}
				
			}
			
			/*if(isSatisfied){
				excuteTask();
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isExistQueData(){
		//获取所有的通信“桥”
		Map<String, ChannelBridge> channelMap = channelManaFactory.build().getAll();
		
		boolean isExitsData = false;
		
		for (Map.Entry<String, ChannelBridge> entry : channelMap.entrySet()){
			
			ChannelBridge bridge = entry.getValue();
			
			if (bridge.getQueueSize() == 0){
				continue;
			}
			
			isExitsData = true;
		}
		
		return isExitsData;
	}
	
	private static boolean excuteTask(){
		
		logger.debug("----------excute task (collecting data) -----------");
		
		try {
			
			//获取所有的通信“桥”
			Map<String, ChannelBridge> channelMap = channelManaFactory.build().getAll();
			
			int channelMapSize = channelMap.size();
			
			if (channelMapSize == 0){
				logger.warn("channelMapSize is 0");
				return false;
			}
			
			CountDownLatch countDownLatch = new CountDownLatch(channelMapSize);
			
			boolean isExitsData = false;
			
			for (Map.Entry<String, ChannelBridge> entry : channelMap.entrySet()){
				
				ChannelBridge bridge = entry.getValue();
				
				if (bridge.getQueueSize() == 0){
					countDownLatch.countDown();
					continue;
				}
				
				isExitsData = true;
				
				CusThreadPoolFactory.build().excuteCollectTask(new CollectThread(bridge, countDownLatch));
				
			}
		
			//等待线程池中的线程全部执行完毕
			countDownLatch.await();
			
			if (isExitsData){
				logger.debug("all ChannelBridge thread excute data complete!!!, channelMap size = " + channelMapSize);
				return true;
			}else{
				//如果没有任何数据
				return false;
			}
			
		} catch (Exception e) {
			logger.error(SysCons.LOG_ERROR + " collecting data found exception, e = {}", e);
		}
		
		return false;
		
	}
	
	private int getDotTimes(DataItemGroup dataItemGroup, long cornIntervMseconds){
		//点次数
		int dotTimes = 0;
		//如果是后续数据项, 如果是 获取点数据
		if (dataItemGroup.getIsFollow()){
			
			int packIntervalTime= dataItemGroup.getPackIntervalTime();
			
			int seconds = (int) (cornIntervMseconds / 1000);
			
			//如果采集间隔时间小于 仪表打包时间 就不采集
			if ((seconds / 60) <  packIntervalTime){
				
				logger.error(SysCons.LOG_ERROR + " cronTime < packIntervalTime. can not execute!!");
				
				throw new RuntimeException();
			}
			
			dotTimes = (seconds / 60) / packIntervalTime;
			
		}
		
		return dotTimes;
	}
	
	private ChannelBridge connect(String gwId){
		
		//如果是lorawan方式，定时采集时，将所有的指令放在一个队列中，由单线程去间隔 1.5秒处理
		/*if (ChannelManaFactory.build() instanceof LoraActiveGetChannelManager){
			return ChannelManaFactory.build().get("lorakeys");
		}*/
		
		ChannelBridge channelBridge = channelManaFactory.build().get(gwId);
		return channelBridge;
	}
	
	private CmdRequest packObj(DataItemGroup dataItemGroup, Meter meter, Date currdate, int dotTimes, long cornIntervMseconds){
		CmdRequest cmdReq = new CmdRequest();
		cmdReq.setCmdLvl(Cons.LVL_CMD_COLLECT);
		cmdReq.setGwId(meter.getGwId());
		cmdReq.setDate(currdate);
		cmdReq.setCollecDate(currdate);
		cmdReq.setIsFollow(dataItemGroup.getIsFollow());
		cmdReq.setMeterAddr(meter.getMeterAddr());
		cmdReq.setMeterType(meter.getMeterType());
		
		/*if (Cons.P645_2007_ADIKE.equals(meter.getProtocolType())){
			if (dotTimes == 0) {
				String content = meter.getMeterAddr() + ",11," + dataItemGroup.getDataItemNum();
				cmdReq.setContent(content);
			}else if (dotTimes > 0){
				
				//获取cornIntervMseconds 之前的时间点
				Date backDate = new Date(currdate.getTime() - cornIntervMseconds);
				
				String backDateStr = DateFormatUtil.format("yyMMddHHmm", backDate);
				
				String content = meter.getMeterAddr() + ",11," + dataItemGroup.getDataItemNum() + "," + dotTimes + "," + backDateStr;
				cmdReq.setContent(content);
			}
		}else {
			
			logger.error("protocol type is not support!!!");
			
			throw new RuntimeException();
		}*/
		
		if (dotTimes == 0) {
			if (ProtocolEum.MODBUS.name().equals(meter.getProtocolType())) {
				String meterAddr = meter.getMeterAddr();
				String content = meterAddr.substring(meterAddr.length() - 3, meterAddr.length()) + ",03," + dataItemGroup.getDataItemNum() + ",2";
				cmdReq.setContent(content);
			}else {
				String content = meter.getMeterAddr() + ",11," + dataItemGroup.getDataItemNum();
				cmdReq.setContent(content);
			}
		}else if (dotTimes > 0){
			
			//获取cornIntervMseconds 之前的时间点
			Date backDate = new Date(currdate.getTime() - cornIntervMseconds);
			
			String backDateStr = DateFormatUtil.format("yyMMddHHmm", backDate);
			
			String content = meter.getMeterAddr() + ",11," + dataItemGroup.getDataItemNum() + "," + dotTimes + "," + backDateStr;
			cmdReq.setContent(content);
		}
		return cmdReq;
	}

}
