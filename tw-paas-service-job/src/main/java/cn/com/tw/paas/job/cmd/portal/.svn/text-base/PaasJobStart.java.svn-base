package cn.com.tw.paas.job.cmd.portal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.core.cache.redis.RedisService;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.job.cmd.local.cache.OrgListCache;
import cn.com.tw.paas.job.cmd.pool.RedisCmdExcFactory;
import cn.com.tw.paas.job.service.OrgService;

/**
 * 启动一个单例线程池扫描已上线的机构网关
 * 
 * @author liming 2018年8月23日 15:24:21
 *
 */
@Component
@Order(2)
public class PaasJobStart implements ApplicationRunner {

	public final static ExecutorService simpleExecutor = Executors.newSingleThreadExecutor();
	

	private static Logger logger = LoggerFactory.getLogger(PaasJobStart.class);

	private static String JOB_LOG_START = "####################### JOB HANDLE";
	
	private static String JOB_LOG_END = "#######################";
	

	@Autowired
	private RedisCmdExcFactory redisCmdExcPool;

	@Autowired
	private OrgService orgService;
	@Autowired
	private RedisService redisService;
	
	@Value("${service.redisscan.count}")
	private Long count;

	@Value("${service.redisscan.sleep}")
	private Long sleep;
	
	private final String TASK_KEY = "ps:ins:send:key";

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		initOrgList();
		// 子线程启动扫描已上线的机构网关
		simpleExecutor.execute(new RedisScanRunable());
	}

	/**
	 * 从paas拉机构信息
	 * 25秒重试一次
	 * @return
	 */
	private void initOrgList() {
		JSONArray orgsList = null;
		
		while (true) {
			try {
				Response<?> result = orgService.all();
				if("000000".equals(result.getStatusCode())){
					Object orgs =  result.getData();
					String dataJson = JSONObject.toJSONString(orgs);
					orgsList = JSONArray.parseArray(dataJson);
					if(orgsList != null && orgsList.size()>0){
						for (int i = 0,len = orgsList.size(); i < len; i++) {
							JSONObject org = orgsList.getJSONObject(i);
							OrgListCache.put( org.getString("orgId"), org);
						}
						return;
					}
				}
			} catch (Exception e) {
				logger.error(JOB_LOG_START+" read org list error，exception:{}"+JOB_LOG_END,e);
			}
			try {
				Thread.sleep(25000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



	/**
	 * 停止服务
	 */
	public void shutdown() {
		if (!simpleExecutor.isShutdown()) {
			simpleExecutor.shutdown();
		}
	}

	class RedisScanRunable implements Runnable {
		@Override
		public void run() {
			/**
			 * 键值对 序列化方式。设置和取值必须保持一致否则报错
			 */
			while (true) {
				if(OrgListCache.isEmpty()){
					initOrgList();
				}
				try {
					 Object taskVal = redisService.lRightPopAndLeftPush(TASK_KEY, TASK_KEY,2*60*60);
					 if(taskVal == null){
						try {
							Thread.sleep(sleep * 1000);
							continue;
						} catch (Exception e) {
						}
					 }
					 /**
					  * jobId
					  * queueId
					  */
					 String taskValStr = String.valueOf(taskVal);
					 String orgId = getOrgIdByCache(taskValStr);
					 // 判断是否包含在机构列表中
					 if(OrgListCache.contrainKey(orgId)){
						 redisCmdExcPool.put(taskValStr);
					 }
				} catch (Exception e) {
					logger.error(JOB_LOG_START+" redis connect exception:{}"+JOB_LOG_END, e);
				}
				try {
					//线程扫描等待间隔1秒
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
		}

		
		private  String getOrgIdByCache(String taskValStr) {
			String orgId =  null;
			try {
				String[] strArr = taskValStr.split(":");
				orgId  = strArr[3];
			} catch (Exception e) {
				logger.error(JOB_LOG_START+"redis key{} valid error exception:{}"+JOB_LOG_END,taskValStr, e);
			}
			return orgId;
		}
		
	}
	
	
	
	

}
