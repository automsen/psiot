package cn.com.tw.paas.monit.service.dlt645.impl;

import java.net.SocketTimeoutException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;
import cn.com.tw.paas.monit.entity.db.command.BaseInnEXE;
import cn.com.tw.paas.monit.entity.db.orgEquipment.OrgEquipment;
import cn.com.tw.paas.monit.mapper.command.BaseCmdEXEMapper;
import cn.com.tw.paas.monit.mapper.command.BaseInnEXEMapper;
import cn.com.tw.paas.monit.mapper.orgEquipment.OrgEquipmentMapper;
import cn.com.tw.paas.monit.service.dlt645.CmdCallBack;
import cn.com.tw.paas.monit.service.dlt645.CmdHandleService;
import cn.com.tw.paas.monit.service.read.ReadService;
import cn.com.tw.paas.monit.thread.AbstractHandelService;
import cn.com.tw.paas.monit.thread.HandleService;
import cn.com.tw.paas.monit.thread.TaskBlockQueue;
import cn.com.tw.paas.monit.thread.TaskThread;

import com.alibaba.fastjson.JSONObject;
/**
 * 当前service 只关注与命令指令的状态。不做任何业务操作
 * @author liming
 * 2017年9月13日09:34:17
 *
 */
@Service
@Deprecated
public class CmdHandleServiceImpl extends AbstractHandelService implements CmdHandleService{

	private static Logger logger = LoggerFactory.getLogger(CmdHandleServiceImpl.class);
	@Autowired
	private OrgEquipmentMapper orgEquipmentMapper;
	@Autowired
	private BaseCmdEXEMapper baseCmdEXEMapper;
	@Autowired
	private BaseInnEXEMapper baseInnEXEMapper;
	@Autowired
	private ReadService readService;
	
	private static String[] noRetryNum = new String[]{
		"E00997","E00995","E00996","E00998","E00994","E00993","E00989","E00992"
	};
	
	private boolean  notInRetry(String statusCode){
		
		for (String notInKey : noRetryNum) {
			if(notInKey.equals(statusCode)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void execute(BaseCmdEXE cmd, CmdCallBack callback) throws Exception{
		OrgEquipment orgEquipment1 = new OrgEquipment();
		orgEquipment1.setCommAddr(cmd.getMeterAddr());
		// 判断仪表状态  已拆除停用, 未安装到线路 不用下发指令  
		/*if(MeterStatus.Uninstalled.getValue().equals(meter.getStatus()) || MeterStatus.BackUp.getValue().equals(meter.getStatus())){
			throw new Exception("仪表状态异常！");
		}*/
		cmd.setCallback(callback);
		List<BaseInnEXE> inns = cmd.getInns();
		//当前执行指令为空或 指令集为空。直接返回错误
		if(inns == null || cmd.getInnEXE() == null){
			logger.error("command start error next innexe is null ");
			throw new Exception("command start error next innexe is null ");
		}
		baseCmdEXEMapper.insert(cmd);
		for (BaseInnEXE baseInnEXE : inns) {
			baseInnEXEMapper.insert(baseInnEXE);
		}
		TaskBlockQueue.put(new TaskThread<HandleService>(cmd, this, System.currentTimeMillis(), Integer.parseInt(cmd.getCmdLevel())));
	}

	/**
	 * 重试时使用，无需插入相关
	 */
	/*@Override
	public void executeNoInsert(BaseCmdEXE cmd, CmdCallBack callback)
			throws Exception {
		cmd.setCallback(callback);
		TaskBlockQueue.put(new TaskThread<HandleService>(cmd, this, System.currentTimeMillis(), Integer.parseInt(cmd.getCmdLevel())));
	}
*/
	
	@Override
	public void handleException(BaseCmdEXE commands, Exception e ) {
		logger.error("Meter Handle({}) notifying update exception {}",commands.getMeterAddr(), e.toString());
		super.handleException(commands, e);
		
	}

	@Override
	public void handleRuntimeException(BaseCmdEXE commands,SocketTimeoutException run ) {
		logger.error("Meter Handle({}) http connect timeout {}",commands.getMeterAddr(), run.toString());
		super.handleRuntimeException(commands, run);
	}

	@Override
	public void handleResponse(BaseCmdEXE commands, String result ) {
		 // 通讯engine 未知BUG
		 if(StringUtils.isEmpty(result)){
			 commands.setReqNum(commands.getRetryNum());  // 取消重试
			 handleError(commands,result,null);
			 return;
		 }
		 JSONObject resultObj = JSONObject.parseObject(result);
		 logger.debug("Meter Handle({}) http connect success {}",commands.getMeterAddr(), result);
		 String resultCode = resultObj.getString("statusCode");
		 // 只处理http执行情况
		 if(!"000000".equals(resultCode)){
			 commands.setReason(resultCode);
			 // 没有任何必要重试
			 if(notInRetry(resultCode)){
				 commands.setReqNum(commands.getRetryNum());  // 取消重试
			 }
			 //执行返回错误码
			logger.debug("Meter Handle({}) resultCode error  {}",result, resultCode);
			if(isRetryOver(commands)){
				//执行完毕，更新状态
				handleError(commands,result,null);
				return;
			}
			bindTask(commands);
			return;
		 }
		 try {
			 JSONObject resultMap = resultObj.getJSONObject("data");
			 if(resultMap!= null &&resultMap.getBooleanValue("success")){
				 readService.saveRead(resultObj.getString("uniqueId"), resultMap);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 //success callback有特殊处理，不判断指令执行结果
		 handleSuccess(commands,result);
	}

	@Override
	public void handleSuccess(BaseCmdEXE commands,String result ) {
		final Long startTime = commands.getInnEXE().getStartTime();
		final Long currTime   = System.currentTimeMillis();
		final Long createTime = commands.getCreateTime().getTime();
		BaseInnEXE nextInn = commands.getInnEXE().getNextInn();
		try {
			BaseInnEXE inn = commands.getInnEXE();
			inn.setReturnValue(result);
			//执行成功指令回调
			commands.getCallback().cmdSuccess(commands);
			//指令执行成功
			inn.setStatus(new Byte("1")); //0创建；1成功；2失败；3超时；10正在处理
			inn.setUseTime(currTime.intValue()-startTime.intValue()
					);
			baseInnEXEMapper.updateByPrimaryKeySelective(inn);
			//可能下一步会继续执行。重新指向
			nextInn = commands.getInnEXE().getNextInn();
			//下一步为空。更新命令执行完成时间和状态
			if(nextInn == null){
				//执行成功
				commands.setUseTime(currTime.intValue()-createTime.intValue());
				commands.setStatus(new Byte("1"));
				baseCmdEXEMapper.updateByPrimaryKeySelective(commands);
			}
			//下一步不为空。继续执行指令
			if(nextInn != null){
				commands.setInnEXE(nextInn);
				TaskBlockQueue.put(new TaskThread<HandleService>(commands, this, System.currentTimeMillis(), Integer.parseInt(commands.getCmdLevel())));
			}
			logger.debug("Meter Handle({}) handle sccuess,having time  {}",result,inn.getUseTime());
		} catch (Exception e) {
			e.printStackTrace();
			if(isRetryOver(commands)){
				//执行完毕，更新状态
				handleError(commands,result,e);
				return;
			}
			bindTask(commands);
			logger.debug("Meter Handle({}) handle error,database saving error  {}",result,e.toString());
		}
	}
	@Override
	public void handleError(BaseCmdEXE commands,String result,Exception e
			 ) {
		logger.debug("Meter Handle({}) handle error,task over  {}",result!= null?result:"", e!= null ?e.toString():"");
		//获取总的执行时间
		Long createTime = commands.getCreateTime().getTime();
		Long currTime   = System.currentTimeMillis();
		BaseInnEXE inn = commands.getInnEXE();
		inn.setStatus(new Byte("2"));  //执行失败
		// socket 超时
		if(e != null && ( e instanceof RuntimeException || e instanceof SocketTimeoutException)){
			inn.setStatus(new Byte("3"));  //执行超时
		}
		//任务超时
		if(!StringUtils.isEmpty(result)){
			 JSONObject resultObj = JSONObject.parseObject(result);
			 String resultCode = resultObj.getString("statusCode");
			 if("E00992".equals(resultCode)){
				 inn.setStatus(new Byte("3"));  //任务超时
			 }
		}
		inn.setReturnValue(result);
		inn.setUseTime(currTime.intValue()-inn.getStartTime().intValue()
				);
		baseInnEXEMapper.updateByPrimaryKeySelective(inn);
		try {
			//判断非阻塞并且包含下一条指令
			if(!hasNext(commands)){
				//判断当前指令的执行状态是否超时
				commands.setStatus(inn.getStatus());
				//执行失败
				commands.setUseTime(currTime.intValue()-createTime.intValue());
				baseCmdEXEMapper.updateByPrimaryKeySelective(commands);
				commands.getCallback().cmdError(commands);
			}
		} catch (Exception e1) {
			logger.debug("Meter Handle({}) handle error,callback exception{}",result!= null?result:"", e1.toString());
		}
	}
	
	/*防止重试时超过重试次数后，不继续执行
	 @param commands*/
	private boolean hasNext(BaseCmdEXE commands){
		Boolean isBlock = commands.getIsBlock();
		//非阻塞，继续读取下一条记录
		if(isBlock != null && isBlock == false){
			BaseInnEXE currInn = commands.getInnEXE();
			//还有下一步
			if(currInn.getNextInn()!= null){
				BaseInnEXE nextInn = currInn.getNextInn();
				commands.setInnEXE(nextInn);
				commands.setReqNum(0);
				bindTask(commands);
				return true;
			}
		}
		return false;
	}

	/**
	 * 重试时使用，无需插入相关
	 */
	@Override
	public void executeNoInsert(BaseCmdEXE cmd, CmdCallBack callback)
			throws Exception {
		cmd.setCallback(callback);
		TaskBlockQueue.put(new TaskThread<HandleService>(cmd, this, System.currentTimeMillis(), Integer.parseInt(cmd.getCmdLevel())));
	}

}
