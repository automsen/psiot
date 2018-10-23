package cn.com.tw.paas.monit.service.inn.impl;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.core.jms.cons.PsMqCons;
import cn.com.tw.common.enm.notify.NotifyBusTypeEm;
import cn.com.tw.common.enm.notify.NotifyLvlEm;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.utils.notify.Notify;
import cn.com.tw.common.utils.notify.NotifyUtils;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.InsExe;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.mapper.org.CmdExeMapper;
import cn.com.tw.paas.monit.mapper.org.InsExeMapper;
import cn.com.tw.paas.monit.mapper.org.OrgApplicationMapper;
import cn.com.tw.paas.monit.service.inn.CmdIssueService;
import cn.com.tw.paas.monit.service.inn.callback.CmdCallback;
import cn.com.tw.paas.monit.service.inn.http.AbstractHandelService;
import cn.com.tw.paas.monit.service.inn.idl.HandleService;
import cn.com.tw.paas.monit.service.inn.idl.TaskBlockQueue;
import cn.com.tw.paas.monit.service.inn.idl.TaskThread;

import com.alibaba.fastjson.JSONObject;


/**
 *  命令下发服务
 * @author liming
 * 2018年3月7日19:08:49
 */

//@Service
public class CmdIssueServiceImpl extends AbstractHandelService implements CmdIssueService{
	
	
	private Logger logger = LoggerFactory.getLogger(CmdIssueServiceImpl.class);
	
	@Autowired
	private OrgApplicationMapper orgApplicationMapper;
	
	@Autowired
	private MqHandler mqHandler;
	
	@Autowired
	private CmdExeMapper cmdExeMapper;
	@Autowired
	private InsExeMapper insExeMapper;
	
	/*private static String[] noRetryNum = new String[]{
		"E00997","E00995","E00996","E00998","E00994","E00993","E00989","E00992"
	};*/
	
	private static String[] noRetryNum = new String[]{"E00995","E00996","E00998","E00994"};
	
	private boolean  notInRetry(String statusCode){
		for (String notInKey : noRetryNum) {
			if(notInKey.equals(statusCode)){
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional
	public void cmdIssue(CmdExe cmd,CmdCallback callback) {
		List<InsExe> inns = cmd.getIns();
		if(inns == null){
			throw new RequestParamValidException("ins error find!");
		}
		cmd.setInnNum(inns.size());
		
		cmdExeMapper.insert(cmd);
		for (InsExe insExe : inns) {
			insExe.setHandleTimes(1); // 指令默认都是第一次下发
			insExeMapper.insert(insExe);
		}
		cmd.setCallback(callback);
		// push result to quene
		TaskBlockQueue.put(new TaskThread<HandleService>(cmd, 
				this,
				System.currentTimeMillis(),
				cmd.getCmdLevel()));
		
	}
	
	@Override
	@Transactional
	public void cmdIssueBatch(List<CmdExe> cmds, CmdCallback callback) {
		if (cmds == null) {
			return;
		}
		for (CmdExe cmd : cmds) {
			List<InsExe> inns = cmd.getIns();
			if(inns == null){
				throw new RequestParamValidException("ins error find!");
			}
			cmd.setInnNum(inns.size());
			
			cmdExeMapper.insert(cmd);
			for (InsExe insExe : inns) {
				insExe.setHandleTimes(1); // 指令默认都是第一次下发
				insExeMapper.insert(insExe);
			}
			cmd.setCallback(callback);
			// push result to quene
			TaskBlockQueue.put(new TaskThread<HandleService>(cmd, 
					this,
					System.currentTimeMillis(),
					cmd.getCmdLevel()));
		}
	}
	
	@Override
	public void handleException(CmdExe commands, Exception e ) {
		logger.error("Meter Handle({}) notifying update exception {}",commands.getConnAddr(), e.toString());
		super.handleException(commands, e);
	}

	@Override
	public void handleRuntimeException(CmdExe commands,SocketTimeoutException run ) {
		logger.error("Meter Handle({}) http connect timeout {}",commands.getConnAddr(), run.toString());
		super.handleRuntimeException(commands, run);
	}

	@Override
	public void handleResponse(CmdExe commands, String result ) {
		 // 通讯engine 未知BUG
		 if(StringUtils.isEmpty(result)){
			 handleError(commands,result,null);
			 return;
		 }
		 JSONObject resultObj = JSONObject.parseObject(result);
		 logger.debug("Meter Handle({}) http connect success {}",commands.getConnAddr(), result);
		 String resultCode = resultObj.getString("statusCode");
		 // 只处理http执行情况
		 if(!"000000".equals(resultCode) ){
			 commands.setReturnValue(resultCode);
			 // 没有任何必要重试
			 if(notInRetry(resultCode)){
				 handleError(commands,result,null);
				 return;
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
		 //success callback有特殊处理，不判断指令执行结果
		 handleSuccess(commands,result);
	}

	@Override
	public void handleSuccess(CmdExe commands,String result ) throws RuntimeException {
		final Long startTime = commands.getCurrIns().getStartTime();
		final Long currTime   = System.currentTimeMillis();
		final Long createTime = commands.getCreateTime().getTime();
		InsExe nextInn = commands.getCurrIns().getNextIns();
		try {
			InsExe inn = commands.getCurrIns();
			inn.setReturnValue(result);
			commands.getCallback().insCallbackSuccess(commands, result);
			//指令执行成功
			inn.setStatus(new Byte("1")); //0创建；1成功；2失败；3超时；10正在处理
			inn.setUseTime(currTime.intValue()-startTime.intValue()
					);
			insExeMapper.updateByPrimaryKeySelective(inn);
			//可能下一步会继续执行。重新指向
			nextInn = commands.getCurrIns().getNextIns();
			//下一步为空。更新命令执行完成时间和状态
			if(nextInn == null){
				//执行成功指令回调
				commands.getCallback().cmdSuccess(commands);
				//执行成功
				commands.setUseTime(currTime.intValue()-createTime.intValue());
				commands.setStatus(new Byte("1"));
				cmdExeMapper.updateByPrimaryKeySelective(commands);
			}
			//下一步不为空。继续执行指令
			if(nextInn != null){
				commands.setCurrIns(nextInn);
				TaskBlockQueue.put(new TaskThread<HandleService>(commands, this, System.currentTimeMillis(), commands.getCmdLevel()));
			}
			logger.debug("Meter Handle({}) handle sccuess,having time  {}",result,inn.getUseTime());
		} catch (Exception e) {
			e.printStackTrace();
			// 代码级别错误， 不做重试
			handleError(commands, result, e);
			logger.debug("Meter Handle({}) handle error,database saving error  {}",result,e.toString());
		}
	}
	@Override
	public void handleError(CmdExe commands,String result,Exception e
			 ) {
		logger.debug("Meter Handle({}) handle error,task over  {}",result!= null?result:"", e!= null ?e.toString():"");
		// mq推送消息和主业务隔离开。防止事务回滚
		try {
			publishErrorMessageToMq(commands, false);
		} catch (Exception e2) {
			e.printStackTrace();
		}
		/**
		 *  命令指令相关数据收集处理
		 */
		//获取总的执行时间
		Long createTime = commands.getCreateTime().getTime();
		Long currTime   = System.currentTimeMillis();
		InsExe inn = commands.getCurrIns();
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
		insExeMapper.updateByPrimaryKeySelective(inn);
		
		/**
		 *  业务回调处理 。不影响整个日志流程
		 */
		// 指令异常 回调， 不影响整个流程
		try {
			commands.getCallback().insCallbackError(commands,e);
		} catch (Exception e2) {
		}
		try {
			//判断非阻塞并且包含下一条指令
			if(!hasNext(commands)){
				//判断当前指令的执行状态是否超时
				commands.setStatus(inn.getStatus());
				//执行失败
				commands.setUseTime(currTime.intValue()-createTime.intValue());
				cmdExeMapper.updateByPrimaryKeySelective(commands);
				commands.getCallback().cmdError(commands,e);
			}
		} catch (Exception e1) {
			e.printStackTrace();
			logger.debug("Meter Handle({}) handle error,callback exception{}",result!= null?result:"", e1.toString());
		}
		
		
		
	}
	
	
	/**
	 * 推送 异常消息 到MQ
	 * 
	 * @param cmd
	 * @param dataMap
	 */
	public void publishErrorMessageToMq(CmdExe cmd, Boolean success) {
		/**
		 * 通断异常 推送到客户
		 */
		try {
			OrgApplication application = orgApplicationMapper.selectByPrimaryKey(cmd.getAppId());
			Map<String, Object> requestParams = cmd.getRequestParams();
				// 发送回调不做任何数据操作
				String returnJson = NotifyUtils.sendTermEventMsg(
						cmd.getConnAddr(),
						requestParams.get("meterType").toString(),
						cmd.getBusinessNo(),
						NotifyBusTypeEm.termCmdError, cmd.getCmdCode(),
						System.currentTimeMillis() );
				String pushStr = Notify
						.createEvent(application.getOrgId(), application.getCallbackUrl(),
								returnJson, 5)
						.setPaasBusData(NotifyBusTypeEm.termCmdError, null, null, null, application.getOrgId(),
								NotifyLvlEm.HIGH)
						.toJsonStr();
				mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, pushStr);
		} catch (Exception e) {
			// 异常不做任何处理防止 命令指令结果保存回滚
			e.printStackTrace();
		}

	}
	
	/*防止重试时超过重试次数后，不继续执行
	 @param commands*/
	private boolean hasNext(CmdExe commands){
		Byte isBlock = commands.getCmdBlock();
		//非阻塞，继续读取下一条记录
		if(isBlock != null && new Byte("0").equals(isBlock)){
			InsExe currInn = commands.getCurrIns();
			//还有下一步
			if(currInn.getNextIns()!= null){
				InsExe nextInn = currInn.getNextIns();
				commands.setCurrIns(nextInn);
				bindTask(commands);
				return true;
			}
		}
		return false;
	}

	@Override
	public void sendCmdToRedis(CmdExe cmd, String gwId) {
		
	}
}
