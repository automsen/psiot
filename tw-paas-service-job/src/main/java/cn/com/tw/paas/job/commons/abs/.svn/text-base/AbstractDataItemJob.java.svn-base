package cn.com.tw.paas.job.commons.abs;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import cn.com.tw.paas.job.commons.ins.JobExcutedHandle;


/**
 * 分流执行
 * @author liming
 * 2018年8月17日 17:47:48
 */
public abstract class AbstractDataItemJob<T>  implements JobExcutedHandle,DataflowJob<T>{
	
	@Autowired
	private ZookeeperRegistryCenter regCenter;
	
	@Autowired
	private DataSource dataSource;
	
	/**
	 * 
	 *  分片总数
	 */
	private int shardingTotalCount = 1;

	
	/**
	 * 默认不开启
	 * 是否开启任务执行失效转移，
	 * 开启表示如果作业在一次任务执行中途宕机，
	 * 允许将该次未完成的任务在另一作业节点上补偿执行
	 */
	private boolean failover = false;
	
	/**
	 *  默认开启
	 *  是否开启错过任务重新执行
	 */
	private boolean misfire = true;
	
	/**
	 *  是否流式 执行
	 *  必须在fetchdata 函数返回空值时停止执行，否则会一直执行
	 *
	 */
	private boolean streamingProcess = false;
	
	/**
	 *  作业名称(必需)
	 */
	private String jobName ;
	
	/**
	 *  定时任务公式 （必需）
	 */
	private String cron;
	
	
	/**
	 * 分片序列号和参数用等号分隔，多个键值对用逗号分隔
	 * 分片序列号从0开始，不可大于或等于作业分片总数
	 *	如：
	 *	0=上海,1=北京,2=深圳
	 */
	private String shardingItemParameters;
	
	/**
	 *  作业自定义参数
		作业自定义参数，可通过传递该参数为作业调度的业务方法传参，用于实现带参数的作业
		例：每次获取的数据量、作业实例从数据库读取的主键等
	 */
	private String jobParameter;
	
	/**
	 *  任务描述
	 */
	private String description;
	
	
	/**
     * 设置监控作业执行时状态.
     *
     * <p>
     * 每次作业执行时间和间隔时间均非常短的情况, 建议不监控作业运行时状态以提升效率, 因为是瞬时状态, 所以无必要监控. 请用户自行增加数据堆积监控. 并且不能保证数据重复选取, 应在作业中实现幂等性. 也无法实现作业失效转移.
     * 每次作业执行时间和间隔时间均较长短的情况, 建议监控作业运行时状态, 可保证数据不会重复选取.
     * </p>
     *
     * @param monitorExecution 监控作业执行时状态
     *
     * @return 作业配置构建器
     */
	private boolean monitorExecution  = true;;
	
	  /**
     * 设置最大容忍的本机与注册中心的时间误差秒数.
     *
     * <p>
     * 如果时间误差超过配置秒数则作业启动时将抛异常.
     * 配置为-1表示不检查时间误差.
     * </p>
     *
     * @param maxTimeDiffSeconds 最大容忍的本机与注册中心的时间误差秒数
     *
     * @return 作业配置构建器
     */
	private int maxTimeDiffSeconds = -1;
	
	
	
	/**
	 *  自定义事件。用于记录和反馈扩展
	 */
	private ElasticJobListener[] events;
	




	/**
	 *  预留  更变配置，如无更变使用默认配置
	 */
	public abstract void  init();
	
	
	@Override
	public void handleTask( ) {
		init(); // 初始化配置

		// 配置有效性验证
		initParams();
		JobCoreConfiguration dataCoreConfig =  null;
		
		/**
		 *  日志录入
		 */
		JobEventConfiguration eventConfig = createDataBaseListner();
		/**
		 *  初始化基本参数配置
		 */
		JobCoreConfiguration.Builder builder = JobCoreConfiguration.newBuilder(jobName, cron,shardingTotalCount );
		if(StringUtils.isNotBlank(shardingItemParameters)){
			builder.shardingItemParameters(shardingItemParameters);
		}
		if(StringUtils.isNotBlank(jobParameter)){
			builder.jobParameter(jobParameter);
		}
		if(StringUtils.isNotBlank(description)){
			builder.description(description);
		}
		
		if(StringUtils.isNotBlank(description)){
			builder.description(description);
		}
		// 不是默认值
		if(failover){
			builder.failover(failover);
		}
		// 不是默认值
		if(!misfire){
			builder.misfire(misfire);
		}
		dataCoreConfig = builder.build();
		
		 // 定义DataJob类型配置
        DataflowJobConfiguration dataJobConfig = new DataflowJobConfiguration(dataCoreConfig,this.getClass().getCanonicalName(),streamingProcess);
       
        LiteJobConfiguration.Builder liteBuilder =  LiteJobConfiguration.newBuilder(dataJobConfig);
        if(!monitorExecution){
        	liteBuilder.monitorExecution(monitorExecution);
        }
        if(maxTimeDiffSeconds != -1){
        	liteBuilder.maxTimeDiffSeconds(maxTimeDiffSeconds);
        }
        LiteJobConfiguration datajobRootConfig = liteBuilder.build();
        
        SpringJobScheduler datajobcheduler = null;
        
        if(events != null && events.length>0){
        	datajobcheduler =  new SpringJobScheduler(this,regCenter, datajobRootConfig,eventConfig,events);
        }else{
        	datajobcheduler =  new SpringJobScheduler(this,regCenter, datajobRootConfig,eventConfig);
        }
        datajobcheduler.init();
	}
	
	private void initParams(){
		 Preconditions.checkArgument(!Strings.isNullOrEmpty(jobName), "jobName can not be empty.");
         Preconditions.checkArgument(!Strings.isNullOrEmpty(cron), "cron can not be empty.");
         Preconditions.checkArgument(shardingTotalCount > 0, "shardingTotalCount should larger than zero.");
	}
	
	/**
	 * 数据库日志监听，会自动写入数据到日志表
	 * @return
	 */
	private JobEventConfiguration  createDataBaseListner(){
		
		JobEventConfiguration jobEventRdbConfig = null;
		if(dataSource!= null){
			 jobEventRdbConfig = new JobEventRdbConfiguration(dataSource);
		}
		return jobEventRdbConfig;
	}
	
	

	
	/**
	 * 
	 *  分片总数
	 */
	public void setShardingTotalCount(int shardingTotalCount) {
		this.shardingTotalCount = shardingTotalCount;
	}


	/**
	 * 默认不开启
	 * 是否开启任务执行失效转移，
	 * 开启表示如果作业在一次任务执行中途宕机，
	 * 允许将该次未完成的任务在另一作业节点上补偿执行
	 */
	public void setFailover(boolean failover) {
		this.failover = failover;
	}


	public void setMisfire(boolean misfire) {
		this.misfire = misfire;
	}


	public void setJobName(String jobName) {
		this.jobName = jobName;
	}


	public void setCron(String cron) {
		this.cron = cron;
	}


	public void setShardingItemParameters(String shardingItemParameters) {
		this.shardingItemParameters = shardingItemParameters;
	}


	public void setJobParameter(String jobParameter) {
		this.jobParameter = jobParameter;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public void setMonitorExecution(boolean monitorExecution) {
		this.monitorExecution = monitorExecution;
	}


	public void setMaxTimeDiffSeconds(int maxTimeDiffSeconds) {
		this.maxTimeDiffSeconds = maxTimeDiffSeconds;
	}






	public void setStreamingProcess(boolean streamingProcess) {
		this.streamingProcess = streamingProcess;
	}
	
	
	
	
	public void setEvents(ElasticJobListener[] events) {
		this.events = events;
	}
	

}
