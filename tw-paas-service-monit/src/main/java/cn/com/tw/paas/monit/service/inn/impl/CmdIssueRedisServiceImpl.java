package cn.com.tw.paas.monit.service.inn.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.core.cache.redis.RedisService;
import cn.com.tw.paas.monit.entity.db.base.Cmd;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.InsExe;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.mapper.org.CmdExeMapper;
import cn.com.tw.paas.monit.mapper.org.InsExeMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.service.inn.CmdHandleException;
import cn.com.tw.paas.monit.service.inn.CmdIssueService;
import cn.com.tw.paas.monit.service.inn.callback.CmdCallback;
import cn.com.tw.paas.monit.service.inn.idl.CmdHandleErrorCode;
import cn.com.tw.paas.monit.service.sys.CmdInsCacheService;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;


/**
 *  命令下发服务
 * @author liming
 * 2018年3月7日19:08:49
 */

@Service
public class CmdIssueRedisServiceImpl  implements CmdIssueService{
	
	
	private Logger logger = LoggerFactory.getLogger(CmdIssueRedisServiceImpl.class);
	
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private CmdExeMapper cmdExeMapper;
	@Autowired
	private InsExeMapper insExeMapper;
	@Autowired
	private CmdInsCacheService cmdInsCacheService;
	
	private static String CMD_LOG_START = "####################### CMD HANDLE";
	
	private static String CMD_LOG_END = "#######################";
	
	@Autowired
	private TerminalEquipMapper terminalEquipMapper;

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void cmdIssue(CmdExe cmd,CmdCallback callback) throws CmdHandleException  {
		logger.debug(CMD_LOG_START+" start"+CMD_LOG_END);
		List<InsExe> inns = cmd.getIns();
		// 相关指令信息不能为空
		if(inns == null){
			throw new CmdHandleException(CmdHandleErrorCode.innsBeNull);
		}
		cmd.setInnNum(inns.size());
		InsExe currInn = cmd.getCurrIns();
	    String requestStr = currInn.getParam();
		JSONObject obj = JSONObject.parseObject(requestStr);
		String gwId = obj.getString("gwId");
		/**
		 *  验证网关是否配置
		 */
		if(Strings.isNullOrEmpty(gwId)  ){
			throw new CmdHandleException(CmdHandleErrorCode.gateWayBeNull);
		}
		
		try {
			
			CmdExe queryCmd = new CmdExe();
			if(!StringUtils.isEmpty(cmd.getBusinessNo())){
				queryCmd.setBusinessNo(cmd.getBusinessNo());
				List<CmdExe> tempList= cmdExeMapper.selectByEntity(queryCmd);
				Cmd cmdObj = (Cmd) cmdInsCacheService.selectCmdById(cmd.getCmdCode());
				
				
				// 当前业务号已经存在,只做唤醒操作
				if(!tempList.isEmpty()){
					cmd = tempList.get(0);
					// 更新命令执行次数，防止重复提交执行
					cmd.setHandleTimes(0);
					cmd.setLimitHandleTimes(cmdObj.getCmdRetryNum());
					cmd.setStatus(new Byte("10"));  // 准备执行
					cmd.setUpdateTime(new Date());
					cmd.setCreateTime(new Date());  // 防止被定时任务重复扫到并拉起
					cmdExeMapper.updateByPrimaryKeySelective(cmd);
					sendCmdToRedis( cmd,  gwId);
					return;
				}
			}
			
			// 判断是否重复执行
			validateCmd(cmd);
			cmd.setHandleTimes(0); //执行次数 
			cmdExeMapper.insert(cmd);
			for (InsExe insExe : inns) {
				insExe.setHandleTimes(1); // 指令默认都是第一次下发
				insExeMapper.insert(insExe);
			}
			sendCmdToRedis( cmd,  gwId);
		} catch (DataAccessException e) {
			logger.debug(CMD_LOG_START+"cmd insert rollback,exception:{}",e);
			// 数据插入回滚
			throw new CmdHandleException(CmdHandleErrorCode.unknowError);
		} catch(CmdHandleException e1){
			logger.debug(CMD_LOG_START+"cmd runtime error; code:{} msg:{}",e1.getCode(),e1.getMessage());
			throw e1;
		}
	}
	

	private void validateCmd(CmdExe cmd ) throws CmdHandleException {
		String cmdCode = cmd.getCmdCode();
		String nextCode = cmdCode;
		// 相同命令是否执行
		if(hasRunCmd(cmdCode,cmd.getConnAddr())){
			throw new CmdHandleException(CmdHandleErrorCode.repeatCmdHandle);
		}
		// 开闸
		if("switch_on".equals(cmdCode) || "switch_on_m" .equals(cmdCode)){
			if (cmd.getConnAddr().length()==11){
				nextCode = "switch_off_m";
			}
			if(hasRunCmd(nextCode,cmd.getConnAddr())){
				throw new CmdHandleException(CmdHandleErrorCode.openError);
			}
		// 关闸	
		}else if("switch_off".equals(cmdCode) || "switch_off_m" .equals(cmdCode)){
			if (cmd.getConnAddr().length()==11){
				nextCode = "switch_on_m";
			}
			if(hasRunCmd(nextCode,cmd.getConnAddr())){
				throw new CmdHandleException(CmdHandleErrorCode.closeError);
			}
		}
	}
	
	private Boolean hasRunCmd(String cmdCode,String connAddr){
		
		List<CmdExe> tempList = null;
		CmdExe queryModel = new CmdExe();
		queryModel.setCmdCode(cmdCode);
		queryModel.setConnAddr(connAddr);
		queryModel.setStatusNotIn(new String[]{"1"});  // 过滤掉已经成功的
		tempList = cmdExeMapper.selectByEntity(queryModel);
		if(!tempList.isEmpty()){
			Integer handleTimes = 0;
			Integer limitHandleTimes = 0;
			for (CmdExe cmdExe : tempList) {
				handleTimes = cmdExe.getHandleTimes()== null ?0:cmdExe.getHandleTimes();
				limitHandleTimes = cmdExe.getLimitHandleTimes()== null ?5:cmdExe.getLimitHandleTimes();
				// // 判断是否还在redis队列中， 防止重复执行
				if(limitHandleTimes != 0  && handleTimes < limitHandleTimes){
					return true;
				}
			}
		}
		return false;
	}


	@Override
	@Transactional
	public void cmdIssueBatch(List<CmdExe> cmds, CmdCallback callback) {
		if (cmds == null) {
			return;
		}
		for (CmdExe cmd : cmds) {
			try {
				cmdIssue( cmd,null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendCmdToRedis(CmdExe cmd, String gwId) throws DataAccessException{
		
		if(StringUtils.isEmpty(gwId)){
			InsExe currInn = cmd.getCurrIns();
		    String requestStr = currInn.getParam();
			JSONObject obj = JSONObject.parseObject(requestStr);
			gwId = obj.getString("gwId");
		}
		Preconditions.checkArgument(!Strings.isNullOrEmpty(gwId),CMD_LOG_START+"gwId can not be empty"+CMD_LOG_END);
		
		TerminalEquip meter = terminalEquipMapper.selectByEquipNumber(cmd.getConnAddr());
		final String sendGwId = gwId;
		redisService.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					connection.multi();
					String gwId;
					if(sendGwId.indexOf(":") != -1){
						gwId = sendGwId.replace(":", "_");
					}else{
						gwId = sendGwId;
					}
					// 将命令指令信息发送到job  机构，网关扇区，仪表，命令code
					String redisKey = String.format("ps:ins:send:%s:%s%s",cmd.getOrgId(), gwId,StringUtils.isEmpty(meter.getSectors())|| meter.getSectors() ==0 ?"":meter.getSectors());
					cmd.setIns(null);
					redisService.lLeftPush(redisKey,cmd.getId());
					/**
					 *  键值缓存
					 */
					String taskKey = "ps:ins:send:key";
					redisService.lLeftPush(taskKey,redisKey);
					// 执行redis事务
					connection.exec();
				} catch (DataAccessException e) {
					logger.debug(CMD_LOG_START+"redis will be rollback,exception:{}"+CMD_LOG_END,e);
					// 事务回滚，丢弃事务中的所有keyset
					connection.discard();
					throw e;  // 放弃整个任务
				}
				return null;
			}
		});
	}
	
	
}
