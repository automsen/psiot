package cn.com.tw.paas.job.cmd.service;

import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.core.cache.redis.RedisService;
import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.core.jms.cons.PsMqCons;
import cn.com.tw.common.enm.notify.NotifyBusTypeEm;
import cn.com.tw.common.enm.notify.NotifyLvlEm;
import cn.com.tw.common.utils.notify.Notify;
import cn.com.tw.common.utils.notify.NotifyUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.job.cmd.http.AbstractHandelService;
import cn.com.tw.paas.job.cmd.pool.RedisCmdExcFactory;
import cn.com.tw.paas.job.cmd.service.inn.CmdHandle;
import cn.com.tw.paas.job.entity.CmdExe;
import cn.com.tw.paas.job.entity.InsExe;
import cn.com.tw.paas.job.entity.OrgApplication;
import cn.com.tw.paas.job.service.CmdExeService;
import cn.com.tw.paas.job.service.OrgApplicationService;


@Service
public class CmdExecute extends AbstractHandelService implements CmdHandle{
	
	private static Logger log = LoggerFactory.getLogger(CmdExecute.class);

	private static String CMD_HANDLE_START = "### CMD HANDLE";
	
	private static String CMD_HANDLE_END = "###";
	
	@Autowired
	private MqHandler mqHandler;
	@Autowired
	private CmdExeService cmdExeService;
	@Autowired
	private OrgApplicationService orgApplicationService;
	
	private final String TASK_KEY = "ps:ins:send:key";
	
	private final Byte HANDLE_BEGIN = new Byte("10");
	
	
	private final Byte HANDLE_SUCCESS = new Byte("1");
	
	private final Byte HANDLE_ERROR = new Byte("2");
	
	private final Byte HANDLE_TIME_OUT = new Byte("3");
	
	@Value("${mq.queue.meter.data}")
	private String queueDataName;
	
	
	@Autowired
	private RedisService redisService;

	
	private boolean  notInRetry(String statusCode){
		for (String notInKey : noRetryNum) {
			if(notInKey.equals(statusCode)){
				return true;
			}
		}
		return false;
	}

	public CmdExecute() {
	}

	/**
	 * 同步阻塞同一机构网关下的所有仪表下发指令
	 */

	@Override
	public void handle(String orgGwId) {
		// key格式  ps:ins:send:%s:%s%s 机构，网关，扇区
		while (true) {
				String cmdId =  null;
				try {
					// 执行后5秒连接阻塞，如果没值直接释放 .取值时可能会出现value序列化失败
					cmdId =String.valueOf(redisService.lRightPop(orgGwId, 5));
				} catch (Exception e) {
					RedisCmdExcFactory.tasks.remove(orgGwId);
					log.error(CMD_HANDLE_START+" ERROR redis right pop error.key:{} Exception:{}",orgGwId,e);
					break;
				}
				// 队列执行完毕
				if (StringUtils.isEmpty(cmdId)||"null".equalsIgnoreCase(cmdId)) {
					try {
						RedisCmdExcFactory.tasks.remove(orgGwId);
						redisService.lRemove(TASK_KEY,0, orgGwId);
					} catch (Exception e) {
						log.error(CMD_HANDLE_START+" ERROR redis clear key Exception{}",e);
					}
					break;
				} else {
					// 通过接口
					try {
						Response<?> response = cmdExeService.findCmdIns(cmdId);
						if(!"000000".equals(response.getStatusCode())){
							log.error(CMD_HANDLE_START+" ERROR.request(findCmdIns) statusCode:{},msg:{}"+CMD_HANDLE_END,response.getStatusCode(),response.getMessage());
						}
						String cmdStr = JSONObject.toJSONString(response.getData());
						CmdExe cmd = JSONObject.parseObject(cmdStr, CmdExe.class);
						// 验证不通过， 直接放弃
						if(!validateCmd(cmd)){
							continue;
						}
						// 第一次执行
						if(HANDLE_BEGIN.equals(cmd.getStatus())){
							if(cmd.getLimitHandleTimes()== null){
								cmd.setLimitHandleTimes(5);
							}
							cmd.setHandleTimes(1); // 第一次执行
						}else{
							Integer handleTimes = cmd.getHandleTimes();
							cmd.setHandleTimes(handleTimes+1); //命令重试次数+1
							//判断起始执行指令
							cmd.setCurrIns(getCurrIns(cmd.getCurrIns()));
						}
						cmd.setCreateTime(new Date()); // 更新创建时间。防止计算指令执行时间错误
						exceResult(cmd);
					} catch (Exception e) {
						redisService.lLeftPush(orgGwId, cmdId);
						log.error(CMD_HANDLE_START+" error. Exception{}", e);
					}
				}
		}
	}
	
	/**
	 * 递归获取当期执行的命令
	 * @param insExe
	 * @return
	 */
	private InsExe getCurrIns(InsExe insExe ){
		Byte currStatus = insExe.getStatus();
		if(!HANDLE_SUCCESS.equals(currStatus)){
			insExe.setHandleTimes(1); // 第一次执行 刷新重试
			insExe.setCreateTime(new Date()); // 刷新执行开始时间
			return insExe;
		}else{
			return getCurrIns(insExe.getNextIns());
		}
	}
	

	
	private boolean validateCmd(CmdExe cmd) {
		// 数据或通讯异常
		if(cmd == null){
			return false;
		}
		// 已经执行成功，重复KEY
		if(HANDLE_SUCCESS.equals(cmd.getStatus())){
			log.debug(CMD_HANDLE_START+" current cmd handle success,repetition request error,cmdId:{}, currStatus:{}",cmd.getId(),cmd.getStatus());
			return false;
		}
		Integer handleTimes = cmd.getHandleTimes();
		Integer limitHandleTimes = cmd.getLimitHandleTimes();
		if(handleTimes != 1 && handleTimes >= limitHandleTimes){
			log.debug(CMD_HANDLE_START+" handle validate error maxlimit:{},limit:{},currStatus:{}",limitHandleTimes,handleTimes,cmd.getStatus());
			return false;
		}
		return true;
	}

	@Override
	public void handleResponse(CmdExe commands, String result) {
		 if(StringUtils.isEmpty(result)){
			 handleError(commands,result,null);
			 return;
		 }
		 JSONObject resultObj = JSONObject.parseObject(result);
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
			log.error(CMD_HANDLE_START+" ({}) resultCode error  {}",result, resultCode);
			if(isRetryOver(commands)){
				//执行完毕，更新状态
				handleError(commands,result,null);
				return;
			}
			bindTask(commands);
			return;
		 }else{
			 
		 }
		 //success callback有特殊处理，不判断指令执行结果
		 handleSuccess(commands,result);
	}

	@Override
	public void handleSuccess(CmdExe commands, String result) {
		final Long startTime = commands.getCurrIns().getStartTime();
		final Long currTime   = System.currentTimeMillis();
		final Long createTime = commands.getCreateTime().getTime();
		InsExe nextInn = commands.getCurrIns().getNextIns();
		try {
			InsExe inn = commands.getCurrIns();
			inn.setReturnValue(result);
		
			//指令执行成功
			inn.setStatus(HANDLE_SUCCESS); //0创建；1成功；2失败；3超时；10正在处理
			inn.setUseTime(currTime.intValue()-startTime.intValue()
					);
			/**
			 * 指令访问成功，执行失败
			 */
			if(!insSuccessSendToMq(commands, result)){
				// 放弃执行下一步
				return;
			}
			//可能下一步会继续执行。重新指向
			nextInn = commands.getCurrIns().getNextIns();
			//下一步为空。更新命令执行完成时间和状态
			if(nextInn == null){
				//执行成功
				commands.setUpdateTime(new Date());
				commands.setReturnValue("SUCCESS");
				commands.setUseTime(currTime.intValue()-createTime.intValue());
				commands.setStatus(HANDLE_SUCCESS);
			}
			// 错误的更新状态不阻塞指令的继续下发
			try {
				cmdExeService.updateCmdStatus(commands);
			} catch (Exception e1) {
				log.error(CMD_HANDLE_START+" MONIT UPDATE ERROR ({}) ,callback exception{}",result!= null?result:"", e1 );
			}
			//下一步不为空。继续执行指令
			if(nextInn != null){
				commands.setCurrIns(nextInn);
				exceResult(commands);
			}
			log.debug(CMD_HANDLE_START+" SUCCESS return value({}),handle time{} seconds",result,inn.getUseTime());
		} catch (Exception e) {
			// 代码级别错误， 不做重试
			handleError(commands, result, e);
			log.error(CMD_HANDLE_START+" ERROR ({})  {}",result,e);
		}
	}
	
	public boolean  insSuccessSendToMq(CmdExe cmd, String result) throws Exception {
			InsExe currIns = cmd.getCurrIns();
			JSONObject obj = JSONObject.parseObject(result);
			obj.put("businessNo", cmd.getBusinessNo());
			JSONObject resultMap = obj.getJSONObject("data");
			if (resultMap.getBooleanValue("success")) {
				log.debug(CMD_HANDLE_START+" OVER SUCCESS meter:{},requestUrl:{},cmdCode:{}",cmd.getConnAddr(),currIns.getRequestUrl(),cmd.getCmdCode());
				// 推送消息到kafka
				try {
					mqHandler.send(queueDataName, obj.toJSONString());
				} catch (Exception e) {
					log.error( CMD_HANDLE_START+" SEND MSG ERROR  insSuccessSendToMq,cmdId:{} ,exception:{}",cmd.getId(),e);
				}
				// 继续执行
				return true;
			}
			// 执行失败的，无需重试直接更新数据库。
			else {
				cmd.setParam(currIns.getParam());
				cmd.setReturnValue(result);
				cmd.setHandleTimes(cmd.getLimitHandleTimes());
				cmd.setCmdBlock(new Byte("1")); // 防止非阻塞继续执行
				// 不重试指令，直接错误执行处理
				currIns.setHandleTimes(currIns.getLimitHandleTimes());
				handleError( cmd,  result,null);
				// 放弃后续执行操作
				return false;
			}
	}
	
	

	@Override
	public void handleError(CmdExe commands, String result, Exception e) {
		log.debug(CMD_HANDLE_START+" RETURN VALUE({}),EXCEPTION:{}",result!= null?result:"", e!= null ?e.toString():"");
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
		inn.setStatus(HANDLE_ERROR);  //执行失败
		// socket 超时
		if(e != null && ( e instanceof RuntimeException || e instanceof SocketTimeoutException)){
			inn.setStatus(HANDLE_TIME_OUT);  //执行超时
		}
		//任务超时
		if(!StringUtils.isEmpty(result)){
			 JSONObject resultObj = JSONObject.parseObject(result);
			 String resultCode = resultObj.getString("statusCode");
			 if("E00992".equals(resultCode)){
				 inn.setStatus(HANDLE_TIME_OUT);  //任务超时
			 }
		}
		inn.setReturnValue(result);
		inn.setUseTime(currTime.intValue()-inn.getStartTime().intValue()
				);
		
		//判断非阻塞并且包含下一条指令
		if(!hasNext(commands)){
			commands.setUpdateTime(new Date());
			commands.setReturnValue(result);
			commands.setParam(inn.getParam());
			//判断当前指令的执行状态是否超时
			commands.setStatus(inn.getStatus());
			//执行失败
			commands.setUseTime(currTime.intValue()-createTime.intValue());
		}
		// 错误的更新状态不阻塞指令的继续下发
		try {
			cmdExeService.updateCmdStatus(commands);
		} catch (Exception e1) {
			log.error(CMD_HANDLE_START+" ERROR ({}) ,callback exception{}",result!= null?result:"", e1);
		}
	}
	
	@Override
	public void handleException(CmdExe commands, Exception e ) {
		log.error(CMD_HANDLE_START+" ERROR ({}) notifying exception {}",commands.getConnAddr(), e.toString());
		super.handleException(commands, e);
	}

	@Override
	public void handleRuntimeException(CmdExe commands,SocketTimeoutException run ) {
		log.error(CMD_HANDLE_START+" RUNTIME EXCEPTION ({}) http connect timeout {}",commands.getConnAddr(), run.toString());
		super.handleRuntimeException(commands, run);
	}
	
	
	
	/**
	 * 推送 异常消息 到MQ
	 * 
	 * @param cmd
	 * @param dataMap
	 */
	public void publishErrorMessageToMq(CmdExe cmd, Boolean success) {
		/**
		 * 命令重试完毕。推送消息
		 */
		if( cmd.getLimitHandleTimes() > cmd.getHandleTimes()){
			return ;
		}
		try {
			Response<?> response = orgApplicationService.selectByOrgId(cmd.getAppId());
			InsExe currIns = cmd.getCurrIns();
			if("000000".equals(response.getStatusCode())){
				OrgApplication application =JSONObject.parseObject( JSONObject.toJSONString(response.getData()),OrgApplication.class) ;
				log.debug( CMD_HANDLE_START+" OVER ERROR :{},requestUrl:{},cmdCode:{}",cmd.getConnAddr(),currIns.getRequestUrl(),cmd.getCmdCode());
				Map<String, Object> requestParams = JSONObject.parseObject(currIns.getParam());
				
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
			}
		} catch (Exception e) {
			log.error( CMD_HANDLE_START+" SEND MSG ERROR  publishErrorMessageToMq,cmdId:{} ,exception:{}",cmd.getId(),e);
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




}
