package cn.com.tw.engine.core.job;

import java.util.Date;
import java.util.List;

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
import cn.com.tw.engine.core.handler.tcp.passive.container.ServerPassiveChannelManager;
import cn.com.tw.engine.core.utils.DataUtils;
import cn.com.tw.engine.core.utils.cons.Cons;

/**
 * 按月采集
 * @author admin
 *
 */
public class MonthCollectJob implements Job{
	
	private static Logger logger = LoggerFactory.getLogger(DayCollectJob.class);
	
	private ChannelManaFactory channelManaFactory = ChannelManaFactory.getInstanc();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		logger.debug("-------------------------------- start collecting month data -------------------------------------------");
		
		/**
		 * 如果没有客户端联系过来，跳过
		 */
		if (channelManaFactory.build() instanceof ServerPassiveChannelManager){
			
			if (channelManaFactory.build().getSize() == 0){
				
				logger.error(SysCons.LOG_ERROR + " collecting day data, channelManager map size is 0, task can not complete!!!!");
				
				return;
			}
			
		}
		
		Date currdate = context.getScheduledFireTime();
		
		try {
			
			//缓存中获取数据项和仪表组的对应关系
			List<DataItemGroup> dataItemGroups = DataUtils.readDataAll();
			
			if (dataItemGroups == null){
				logger.error(SysCons.LOG_ERROR + " dataitem info is empty, task can not complete!!!!");
			}
			
			//遍历数据项
			for (DataItemGroup dataItemGroup : dataItemGroups){
				
				
				if (dataItemGroup.getIsMonthCollect()){
					
					//获取该数据项对应的仪表
					List<Meter> meters = dataItemGroup.getMeters();
					
					if (meters == null){
						continue;
					}
					
					//点次数
					int dotTimes = 0;
					long cornIntervMseconds = 0;
					//如果是后续数据项, 如果是 获取点数据
					if (dataItemGroup.getIsFollow()){
						
						cornIntervMseconds = CronExUtil.getIntervalTime(dataItemGroup.getCollectCron());
						int packIntervalTime= dataItemGroup.getPackIntervalTime();
						
						int seconds = (int) (cornIntervMseconds / 1000);
						
						//如果采集间隔时间小于 仪表打包时间 就不采集
						if ((seconds / 60) <  packIntervalTime){
							
							logger.error(SysCons.LOG_ERROR + " cronTime < packIntervalTime. can not execute!!");
							
							continue;
						}
						
						dotTimes = (seconds / 60) / packIntervalTime;
						
					}
					
					//遍历仪表，查找所有下面的仪表，再遍历每个仪表需要采集的数据项，拼成指令 放入队列中
					for (Meter meter : meters){
						
						try {
							ChannelBridge channelBridge = channelManaFactory.build().get(meter.getGwId());
							
							if (channelBridge == null){
								continue;
							}
							
							CmdRequest cmdReq = new CmdRequest();
							cmdReq.setCmdLvl(Cons.LVL_CMD_COLLECT);
							cmdReq.setGwId(meter.getGwId());
							cmdReq.setDate(currdate);
							cmdReq.setMeterAddr(meter.getMeterAddr());
							cmdReq.setMeterType(meter.getMeterType());
							
							if (Cons.P645_2007_ADIKE.equals(meter.getProtocolType())){
								if (Cons.ELEC_METER_TYPE.equals(meter.getMeterType()) || Cons.WATER_METER_TYPE.equals(meter.getMeterType())){
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
								}
							}else {
								
								logger.error("protocol type is not support!!!");
								
								break;
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
