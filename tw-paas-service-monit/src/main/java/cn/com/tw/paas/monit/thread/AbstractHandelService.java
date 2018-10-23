package cn.com.tw.paas.monit.thread;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.tools.http.HttpPostReq;
import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.paas.monit.common.utils.cons.EnergyCategEum;
import cn.com.tw.paas.monit.common.utils.cons.GatewayTypeEum;
import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;
import cn.com.tw.paas.monit.entity.db.command.BaseInnEXE;
import cn.com.tw.paas.monit.entity.db.orgEquipment.OrgEquipment;
import cn.com.tw.paas.monit.service.equipment.OrgEquipmentService;

import com.alibaba.fastjson.JSONObject;

/**
 * 单指令类型命令
 * @author liming
 * 2017年9月4日18:31:41
 *
 */
@Component
public abstract class AbstractHandelService implements HandleService{
	private   Logger logger = LoggerFactory.getLogger(AbstractHandelService.class);
	
	private String URL = Env.getVal("mediserver.accesspath");
	
	/*@Autowired
	private MeterGatewayService meterGatewayService;*/
	@Autowired
	private OrgEquipmentService orgEquipmentService;
	
	private int connectTime = 2000;
	/**
	 * 默认重试次数
	 */
	private int  reqNum = 0;
	
	private int defaultLevel = 1;

	
	
	public String exceResult(BaseCmdEXE commands) {
		try {
			OrgEquipment orgEquipment1 = new OrgEquipment();
			//当前执行指令
			BaseInnEXE currInn  = commands.getInnEXE();
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<http start request{} currTime:{}",currInn.getParam(),System.currentTimeMillis());
			
			//当前指令执行开始,没有开始计时
			if(currInn!= null && currInn.getStartTime() == null){
				currInn.setStartTime(System.currentTimeMillis());
			}
			// 内部类时为空值
			if(orgEquipmentService == null){
				orgEquipmentService = (OrgEquipmentService) SpringContext.getBean("OrgEquipmentServiceImpl");
			}
			orgEquipment1.setCommAddr(currInn.getMeterAddr());
//			String engineIp = orgEquipment.getNetEquipAddr();
			// 通过配置获取engine访问地址
//			URL = "http://"+engineIp+"/model1/contr";
			String reqDatas = currInn.getParam();
			StringEntity entity = new StringEntity(reqDatas,"utf-8");//解决中文乱码问题    
	        entity.setContentEncoding("UTF-8");    
	        entity.setContentType("application/json");
	        
	        JSONObject obj = JSONObject.parseObject(currInn.getParam());
	        String meterCateg = obj.getString("meterType");
	        String gatewayType= obj.getString("gatewayType");
	        int socketTimeout = getSocketTimeout(meterCateg,gatewayType);
	        String result = new HttpPostReq(URL, null, entity,socketTimeout,connectTime).excuteReturnStr();
	        handleResponse(commands,result);
	        logger.debug(">>>>>>>>>>>>>>>>>>>http response success currTime:{}",System.currentTimeMillis());
		} catch (SocketTimeoutException db){
			logger.debug(">>>>>>>>>>>>>>>>>>>http response timeout  currTime:{}",System.currentTimeMillis());
			// 连接超时，不再重试
			commands.setReqNum(commands.getRetryNum());
			handleRuntimeException(commands, db);
		} catch (IOException e){
			logger.debug(">>>>>>>>>>>>>>>>>>>http response IOException  currTime:{}",System.currentTimeMillis());
			// 连接超时，不再重试
			commands.setReqNum(commands.getRetryNum());
			handleException(commands, e);
		}catch (Exception  e) {
			logger.debug(">>>>>>>>>>>>>>>>>>>http response error  currTime:{}",System.currentTimeMillis());
			handleException(commands, e);
		}
		return null;
		
	}
	
	private int getSocketTimeout(String meterCateg, String gatewayType) {
		String timeout = "30000"	;
		// 485 超时时间一致
		if(GatewayTypeEum.RS485_ETHERNET1.getValue().equals(gatewayType) ||
				GatewayTypeEum.RS485_ETHERNET2.getValue().equals(gatewayType) ||
				GatewayTypeEum.RS485_GPRS1.getValue().equals(gatewayType)||	
				GatewayTypeEum.RS485_GPRS2.getValue().equals(gatewayType)
				){
			if(EnergyCategEum.ELEC.getValue().equals(meterCateg)){
				timeout = Env.getVal("engine.timeout.elec.rs485");
			}
			if(EnergyCategEum.WATER.getValue().equals(meterCateg)){
				timeout = Env.getVal("engine.timeout.water.rs485");
			}
		}
		else if(GatewayTypeEum.NB_IoT.getValue().equals(gatewayType) ||
				GatewayTypeEum.LORAWAN.getValue().equals(gatewayType)
				){
			if(EnergyCategEum.ELEC.getValue().equals(meterCateg)){
				timeout = Env.getVal("engine.timeout.elec.platform");
			}
			if(EnergyCategEum.WATER.getValue().equals(meterCateg)){
				timeout = Env.getVal("engine.timeout.water.platform");
			}
		}else{
			if(EnergyCategEum.ELEC.getValue().equals(meterCateg)){
				timeout = Env.getVal("engine.timeout.elec.directc");
			}
			if(EnergyCategEum.WATER.getValue().equals(meterCateg)){
				timeout = Env.getVal("engine.timeout.water.directc");
			}
		}
		return Integer.parseInt(timeout);
	}

	//重新绑定
	public void bindTask(BaseCmdEXE commands){
		if(commands.getReqNum()> 1){
			try {
				Thread.sleep(1000*commands.getReqNum());
			} catch (Exception e) {
			}
		}
		TaskBlockQueue.put(new TaskThread<HandleService>(commands, 
				this, 
				System.currentTimeMillis(),
				StringUtils.isEmpty(commands.getCmdLevel())?defaultLevel:
				Integer.parseInt(commands.getCmdLevel())));
	}
	
	public boolean isRetryOver(BaseCmdEXE commands){
		//起步为0 小于等于即可
		if(commands.getRetryNum()<=commands.getReqNum()){
			//执行完毕，更新状态
			return true;
		}
		commands.setReqNum(commands.getReqNum() + 1);
		return false;
	}
	
	public abstract void handleResponse( BaseCmdEXE commands,String result);
	
	//如果发生未知错误，或连接失败 操作
	public void handleException(BaseCmdEXE commands, Exception e) {
		//重试后继续
		if(isRetryOver(commands)){
			//查询数据失败
			handleError(commands, null, e);
			return;
		}
		bindTask(commands);
	}
	
	 // 读数超时后执行
	public void handleRuntimeException(BaseCmdEXE commands,
		 SocketTimeoutException	run) {
		//重试后继续
		if(isRetryOver(commands)){
			//查询数据失败
			handleError(commands, null, run);
			return;
		}
		bindTask(commands);
	}
	
	
	public abstract void handleSuccess( BaseCmdEXE commands,String result);
	
	public abstract void handleError(BaseCmdEXE commands,String result,Exception e);
	
	

	public int getReqNum() {
		return reqNum;
	}

	public void setReqNum(int reqNum) {
		this.reqNum = reqNum;
	}

}
