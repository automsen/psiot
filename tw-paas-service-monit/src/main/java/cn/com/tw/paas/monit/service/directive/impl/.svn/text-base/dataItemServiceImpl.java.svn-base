package cn.com.tw.paas.monit.service.directive.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;
import cn.com.tw.paas.monit.entity.db.command.BaseInnEXE;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.entity.db.orgEquipment.OrgEquipment;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.mapper.orgEquipment.OrgEquipmentMapper;
import cn.com.tw.paas.monit.service.directive.DataItemService;
import cn.com.tw.paas.monit.service.dlt645.CmdCallBack;
import cn.com.tw.paas.monit.service.dlt645.CmdHandleService;
import cn.com.tw.paas.monit.service.dlt645.idl.CmdInnEXEUtil;
import cn.com.tw.paas.monit.service.dlt645.idl.PriceEnum;
import cn.com.tw.paas.monit.thread.entity.PageCmdResult;

import com.alibaba.fastjson.JSONObject;

/**
 * 645协议基本操作
 * 
 * 基本读取功能命令
 * 
 * @author liming 2017年9月4日10:05:43
 *
 */
@Service
public class dataItemServiceImpl implements DataItemService {

	private static Logger logger = LoggerFactory.getLogger(dataItemServiceImpl.class);


	@Autowired
	private CmdHandleService cmdHandleService;
	@Autowired
	private OrgEquipmentMapper orgEquipmentMapper;
	@Autowired
	private TerminalEquipMapper terminalEquipMapper;

	private static final int CMD_LEVEL = 20;

	private static final int retryNum = 3;

	@Autowired
	private MqHandler mqHandler;

	/**
	 * 数据项下发
	 */
	@Override
	public PageCmdResult dataItem(String netEquipAddr,CmdCallBack callback,String powerdata) {
		TerminalEquip terminalEquip = terminalEquipMapper.selectByNetEquipNumber(netEquipAddr);
		/*OrgEquipment orgEquipment1 = new OrgEquipment();
		orgEquipment1.setCommAddr(netEquipAddr);*/
		String [] powerdatas = powerdata.split(",");
//		OrgEquipmentExpand orgEquipment = orgEquipmentMapper.selectOrgEquipmentById(orgEquipment1);
		
		if (terminalEquip == null) {
			logger.error("dataItem handle error netEquipAddr is undefiend!");
			return null;
		}
		// 前端下指令模式2必须加入  当前指令的ID集合
		LinkedList<String> innList = new LinkedList<String>();
		
		List<BaseInnEXE> inns = new ArrayList<BaseInnEXE>();
		BaseCmdEXE commandExe = new BaseCmdEXE();
		// 参数拼接
		// 将入口参数放入命令中
		commandExe.setCmdId(UUID.randomUUID().toString().replaceAll("-", ""));
		commandExe.setRetryNum(retryNum);
		commandExe.setParamObj(terminalEquip);
		/*commandExe.setCmdObjid(commAddr);
		commandExe.setCmdTable("meterReadCmdServiceImpl");*/
		commandExe.setMeterId(terminalEquip.getEquipNumber());
		commandExe.setMeterAddr(terminalEquip.getEquipNumber());
		commandExe.setCmdLevel(CMD_LEVEL + "");
		commandExe.setCmdName("数据项下发");
		commandExe.setCreateTime(new Date(System.currentTimeMillis()));
		commandExe.setStatus(new Byte("10")); // 0创建；1成功；2失败；3超时；10正在处理
		commandExe.setInns(inns);
		commandExe.setIsBlock(true);   //阻塞，根据成功与否执行下一条指令
		BaseInnEXE inn1 = new BaseInnEXE();

		inn1 = CmdInnEXEUtil.sendingDataSetInnExe(commandExe, PriceEnum.sendingDataSet, terminalEquip, 1,new Byte("10"),powerdatas);
		inns.add(inn1);
		innList.add(inn1.getInnId());
			
		commandExe.setInnEXE(inn1);
		PageCmdResult result = null;
		try {
			result = new PageCmdResult();
			result.setInnArr(innList);
			result.setCmdId(commandExe.getCmdId());
			result.setExeTime(15000+400l*(inns.size()));
			result.setIsRepeat(true);
			result.setCmdName(commandExe.getCmdName());
			result.setMeterAddr(commandExe.getMeterAddr());
			CmdCallBack callbackHandle = null;                                                                          
			if( callback != null){
				commandExe.setCmdCallback(callback.getClass().getName());
				callbackHandle = callback;
			}else{
				callbackHandle =  new HandleCallback();
			}
			cmdHandleService.execute(commandExe, callbackHandle);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 读表重试
	 */
	@Override
	public void execCmd(BaseCmdEXE cmd) {
		/*BaseMeter meter = baseMeterMapper.selectMeterIpAndPort(cmd.getCmdObjid());
		cmd.setParamObj(meter);
		cmd.setParamObj(meter);
		cmd.setCmdLevel(CMD_LEVEL+"");
		cmd.setRetryNum(retryNum);
		try {
			cmdHandleService.executeNoInsert(cmd,cmd.getCallback() != null?cmd.getCallback():new HandleCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	public class HandleCallback implements CmdCallBack {
		@Override
		public void cmdSuccess(BaseCmdEXE cmd) throws Exception {
			BaseInnEXE currInn = cmd.getInnEXE();
			OrgEquipment orgEquipment = (OrgEquipment) cmd.getParamObj();
			/*ReadLast last = meter.getReadLast();
			if(last == null){
				last = new ReadLast();
				meter.setReadLast(last);
			}*/
			
			String resultStr = currInn.getReturnValue();
			JSONObject resultObj = JSONObject.parseObject(resultStr);
			JSONObject resultData = resultObj.getJSONObject("data");
			Integer retry = cmd.getReqNum();
			// 执行失败处理
			if (!resultData.getBooleanValue("success")) {
				// 向上抛异常 ，重新计数执行
				String returnStr = "return value error,curr retryNum:" + retry + ",currRetryControl:"
						+ currInn.getCommand().getIdentity();
				throw new Exception(returnStr);
			}
			/*if (EnergyCateg.ELEC.getValue().equals(meter.getMeterCateg())) {
		
				// 读当前止码
				if (PriceEnum.readElecActual.equals(currInn.getCommand())) {
					String tempStr = resultData.getString("dataValues");
					String[] resultArr = tempStr.split(",");
					BigDecimal dataVal = new BigDecimal(resultArr[0]);
					last.setLastReadValue(dataVal);
					cmd.setReturnValue(String.valueOf(dataVal));
				}  else if (PriceEnum.readbalance.equals(currInn.getCommand())) {
					BigDecimal dataVal = new BigDecimal(resultData.getString("dataValues"));
					// 余额大于0，不用再读取透支
					*//**
					if(dataVal.compareTo(BigDecimal.ZERO)>0){
						currInn.setNextInn(null);
					}
					**//*
					cmd.setReturnValue(String.valueOf(last.getLastReadValue())+","+String.valueOf(dataVal));
				}  else if (PriceEnum.readoverdraft.equals(currInn.getCommand())) {
					BigDecimal dataVal = new BigDecimal(resultData.getString("dataValues"));
					// 0-透支金额
					cmd.setReturnValue(String.valueOf(last.getLastReadValue())+","+String.valueOf(BigDecimal.ZERO.subtract(dataVal)));
				} 
			// 水表
			} else{
				String[] tempArray = resultData.getString("dataValues").split(",");
				BigDecimal tempDecimal = new BigDecimal(tempArray[0]);
				cmd.setReturnValue(String.valueOf(tempDecimal));
			}*/
		}

		@Override
		public void cmdError(BaseCmdEXE cmd) throws Exception {
			BaseInnEXE currInn = cmd.getInnEXE();
			String vals = currInn.getReturnValue();
			logger.debug("{} handle cmdError result{}", cmd.getCmdName(), vals);
		}
	}

}
