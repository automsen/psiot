package cn.com.tw.paas.job.cmd.http;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.utils.tools.http.HttpPostReq;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.paas.job.cmd.cons.EnergyCategEum;
import cn.com.tw.paas.job.entity.CmdExe;
import cn.com.tw.paas.job.entity.InsExe;

/**
 * 单指令类型命令
 * @author liming
 * 2017年9月4日18:31:41
 *
 */
@Component
public abstract class AbstractHandelService  {
	private   Logger logger = LoggerFactory.getLogger(AbstractHandelService.class);
	
	private String ethernetURL = Env.getVal("mediserver.ethernet");
	
	private String gprsURL = Env.getVal("mediserver.gprs");
	
	private String loraURL = Env.getVal("mediserver.LoRaWan");
	
	private String wfURL = Env.getVal("mediserver.Wi-Fi");
	
	private String nbURL = Env.getVal("mediserver.NB-IoT");
	
	private String mURL = Env.getVal("mediserver.modbus");
	
	private String dtuUrl = Env.getVal("mediserver.dtu");
	
	private int connectTime = 2000;
	
	
	
	
	public String exceResult(CmdExe commands) {
		try {
			//当前执行指令
			InsExe currInn  = commands.getCurrIns();
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<http start request{} currTime:{}",currInn.getParam(),System.currentTimeMillis());
			
			//当前指令执行开始,没有开始计时
			if(currInn!= null && currInn.getStartTime() == null){
				currInn.setStartTime(System.currentTimeMillis());
			}
			String reqDatas = currInn.getParam();
			StringEntity entity = new StringEntity(reqDatas,"utf-8");//解决中文乱码问题    
	        entity.setContentEncoding("UTF-8");    
	        entity.setContentType("application/json");
	        
	        JSONObject reqMap = JSONObject.parseObject(currInn.getParam());
	        String netTypeCode =reqMap.getString("gatewayType");
	        String meterType   =reqMap.getString("meterType");
	        String meterAddr   =reqMap.getString("meterAddr");
	        int socketTimeout = 30000;
	        String URL = "";
	        
	        if("150000".equals(meterType)){
	        	URL = dtuUrl;
	        	socketTimeout = getScoketTimeout(meterType, "engine.timeout.elec.rolawan", "engine.timeout.water.rolawan", "engine.timeout.hot.rolawan");
	        }else{
	        	// modbus仪表特别处理
		        if (meterAddr.length()==11){
		        	URL = mURL;
		        }
		        /**
		         * 301：GQRS 302:LoRaWan 303:Wi-Fi 304:NB-IoT 305:以太网
		         */
		        else if("301".equals(netTypeCode)){
		        	URL = gprsURL;
		        	socketTimeout = getScoketTimeout(meterType, "engine.timeout.elec.rs485", "engine.timeout.water.rs485", null);
		        }else if("302".equals(netTypeCode)){
		        	URL = loraURL;
		        	socketTimeout = getScoketTimeout(meterType, "engine.timeout.elec.platform", "engine.timeout.water.platform", null);
		        }else if("303".equals(netTypeCode)){
		        	URL = wfURL;
		        	socketTimeout = getScoketTimeout(meterType, "engine.timeout.elec.directc", "engine.timeout.water.directc", null);
		        }else if("304".equals(netTypeCode)){
		        	URL = nbURL;
		        }else if("305".equals(netTypeCode)){
		        	URL = ethernetURL;
		        	socketTimeout = getScoketTimeout(meterType, "engine.timeout.elec.directc", "engine.timeout.water.directc", null);
		        }
	        }
	        
	        currInn.setRequestUrl(URL);
	        String result = new HttpPostReq(URL, null, entity,socketTimeout,connectTime).excuteReturnStr();
	        handleResponse(commands,result);
//	        logger.debug(">>>>>>>>>>>>>>>>>>>http response success currTime:{}",System.currentTimeMillis());
		} catch (SocketTimeoutException db){
			logger.error(">>>>>>>>>>>>>>>>>>>http response timeout  currTime:{}",System.currentTimeMillis());
			handleRuntimeException(commands, db);
		} catch (IOException e){
			logger.error(">>>>>>>>>>>>>>>>>>>http response IOException  currTime:{}",System.currentTimeMillis());
			handleException(commands, e);
		}catch (Exception  e) {
			logger.error(">>>>>>>>>>>>>>>>>>>http response error  currTime:{}",System.currentTimeMillis());
			handleException(commands, e);
		}
		return null;
		
	}
	
	private int getScoketTimeout(String meterType, String envElecKey, String envWaterKey, String envHotKey) {
		try {
			if(EnergyCategEum.ELEC.getValue().equals(meterType)){
				return Integer.parseInt(Env.getVal(envElecKey));
			}
			if(EnergyCategEum.WATER.getValue().equals(meterType)){
				return Integer.parseInt(Env.getVal(envWaterKey));
			}
			if(EnergyCategEum.HOTWATER.getValue().equals(meterType)){
				return Integer.parseInt(Env.getVal(envHotKey));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 30000;
	}
	
	

	//重新绑定
	public void bindTask(CmdExe commands){
		InsExe currInn = commands.getCurrIns();
		int requestNum = currInn.getHandleTimes();
		if(requestNum >1 ){
			try {
				Thread.sleep(1000*currInn.getHandleTimes());
			} catch (Exception e) {
			}
		}
		exceResult(commands);
	}
	
	public boolean isRetryOver(CmdExe commands){
		InsExe currInn = commands.getCurrIns();
		int requestNum = currInn.getHandleTimes();
		int maxRequestNum = currInn.getLimitHandleTimes();
		//起步为0 小于等于即可
		if( maxRequestNum <= requestNum){
			//执行完毕，更新状态
			return true;
		}
		currInn.setHandleTimes(requestNum + 1);
		return false;
	}
	
	public abstract void handleResponse( CmdExe commands,String result);
	
	//如果发生未知错误，或连接失败 操作
	public void handleException(CmdExe commands, Exception e) {
		//重试后继续
		if(isRetryOver(commands)){
			//查询数据失败
			handleError(commands, null, e);
			return;
		}
		bindTask(commands);
	}
	
	 // 读数超时后执行
	public void handleRuntimeException(CmdExe commands,
		 SocketTimeoutException	run) {
		//重试后继续
		if(isRetryOver(commands)){
			//查询数据失败
			handleError(commands, null, run);
			return;
		}
		bindTask(commands);
	}
	
	
	public abstract void handleSuccess( CmdExe commands,String result);
	
	public abstract void handleError(CmdExe commands,String result,Exception e);
	
	
}
