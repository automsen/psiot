package cn.com.tw.paas.monit.service.directive.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;
import cn.com.tw.paas.monit.entity.db.command.BaseInnEXE;
import cn.com.tw.paas.monit.mapper.orgEquipment.OrgEquipmentMapper;
import cn.com.tw.paas.monit.service.directive.MeterBreakerCmdService;
import cn.com.tw.paas.monit.service.dlt645.CmdCallBack;
import cn.com.tw.paas.monit.service.dlt645.CmdHandleService;
import cn.com.tw.paas.monit.service.dlt645.idl.PriceEnum;
import cn.com.tw.paas.monit.service.equipment.OrgEquipmentService;
import cn.com.tw.paas.monit.thread.entity.PageCmdResult;

import com.alibaba.fastjson.JSONObject;


/**
 * 645协议基本操作  水电表 开合闸 （不加密，明文操作）
 * @author liming
 * 2017年9月4日10:05:43
 *
 */
@Service
public class MeterBreakerCmdServiceImpl implements MeterBreakerCmdService{
	
	
	private static Logger logger = LoggerFactory.getLogger(MeterBreakerCmdServiceImpl.class);
	
	
	
	@Autowired
	private CmdHandleService cmdHandleService;
	@Autowired
	private OrgEquipmentService orgEquipmentService;
	@Autowired
	private OrgEquipmentMapper orgEquipmentMapper;
	
	private static final int CMD_LEVEL = 20;
	
	private static final int retryNum = 3;
	
	
	/**
	 * 基本未加密参数
	 * 开闸
	 * {"cmdLvl":1,"gwIp":"192.168.2.254","gwPort":"26", "content":"000000200508,1C,00000002,1C" }
	 */
	@Override
	public PageCmdResult breakerOpenMeter(String commAddr,CmdCallBack callback) {
		return null;
		/*logger.info("ElecMeterBasicCmdServiceImpl read elec meter start!");
		PageCmdResult result = new PageCmdResult();
		OrgEquipment orgEquipment1 = new OrgEquipment();
		orgEquipment1.setCommAddr(commAddr);
		OrgEquipmentExpand orgEquipment = orgEquipmentMapper.selectOrgEquipmentById(orgEquipment1);
		if(orgEquipment == null){
			return null;
		}
		String meterC = "0201".equals(orgEquipment.getEquipType())?"电表":"水表";
		List<BaseInnEXE> inns = new ArrayList<BaseInnEXE>();
		BaseCmdEXE commandExe = new BaseCmdEXE();
		// 前端下指令模式2必须加入  当前指令的ID集合
		LinkedList<String> innList = new LinkedList<String>();
		//参数拼接
		//将入口参数放入命令中
		commandExe.setCmdId(CommUtils.getUuid());
		commandExe.setRetryNum(retryNum);
		
		commandExe.setParamObj(orgEquipment);
		
		commandExe.setMeterId(orgEquipment.getCommAddr());
		commandExe.setMeterAddr(orgEquipment.getCommAddr());
		commandExe.setCmdLevel(CMD_LEVEL+"");
		commandExe.setCmdName(meterC+"开闸");
		commandExe.setCreateTime(new Date(System.currentTimeMillis()));
		commandExe.setStatus(new Byte("10"));  //0创建；1成功；2失败；3超时；10正在处理
		commandExe.setMeterId(commAddr);
		//可重试
		commandExe.setCmdObjid(commAddr);
		commandExe.setCmdTable("meterBreakerCmdServiceImpl");
		
		commandExe.setInns(inns);
		BaseInnEXE inn = new BaseInnEXE();
		
		//电表指令
		if("0201".equals(orgEquipment.getEquipType())){
			orgEquipment.setEquipType("0301");
		  int isCost = orgEquipment.getIsCostControl().intValue();
			if(isCost == 1){
				inn = CmdInnEXEUtil.noEncryptContrlInnExe(commandExe, PriceEnum.NoEncryptElecOn2, orgEquipment, 1, new Byte("10"));
			}else{
				inn = CmdInnEXEUtil.noEncryptContrlInnExe(commandExe, PriceEnum.NoEncryptElecOn, orgEquipment, 1, new Byte("10"));
			}
		//水表指令
		}else if("0202".equals(orgEquipment.getEquipType())){
			orgEquipment.setEquipType("0302");
			inn = CmdInnEXEUtil.noEncryptContrlInnExe(commandExe, PriceEnum.NoEncryptWaterOn, orgEquipment, 1, new Byte("10"));
		}
		innList.add(inn.getInnId());
		inns.add(inn);
		commandExe.setInnEXE(inn);  //当前执行的指令
		try {
			result.setCmdId(commandExe.getCmdId());
			result.setExeTime(1400l*(inns.size()));
			result.setInnArr(innList);
			result.setIsRepeat(true);
			result.setMeterAddr(commandExe.getMeterAddr());
			result.setCmdName(commandExe.getCmdName());
			CmdCallBack tempCallback = null;
			if(callback == null){
				tempCallback = new HandleCallback();
			}else{
				tempCallback = callback;
			}
			cmdHandleService.execute(commandExe, tempCallback);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;*/
	}
	

	/**
	 * 基本未加密参数
	 * 合闸
	 * {"cmdLvl":1,"gwIp":"192.168.2.254","gwPort":"26", "content":"000000200508,1C,00000002,1A" }
	 */
	@Override
	public PageCmdResult breakerCloseMeter(String commAddr,CmdCallBack callback) {
		return null;
		/*PageCmdResult result = new PageCmdResult();
		// 设置重试次数
		logger.info("ElecMeterBasicCmdServiceImpl read elec meter start!");
		OrgEquipment orgEquipment1 = new OrgEquipment();
		orgEquipment1.setCommAddr(commAddr);
		OrgEquipmentExpand orgEquipment = orgEquipmentMapper.selectOrgEquipmentById(orgEquipment1);
		if(orgEquipment == null){
			return null;
		}
		// 前端下指令模式2必须加入  当前指令的ID集合
		LinkedList<String> innList = new LinkedList<String>();
		String meterC = "0201".equals(orgEquipment.getEquipType())?"电表":"水表";
		List<BaseInnEXE> inns = new ArrayList<BaseInnEXE>();
		BaseCmdEXE commandExe = new BaseCmdEXE();
		//参数拼接
		//将入口参数放入命令中
		commandExe.setCmdId(CommUtils.getUuid());
		commandExe.setRetryNum(retryNum);
		commandExe.setParamObj(orgEquipment);
		commandExe.setMeterId(orgEquipment.getCommAddr());
		commandExe.setMeterAddr(orgEquipment.getCommAddr());
		commandExe.setCmdLevel(CMD_LEVEL+"");
		commandExe.setCmdName(meterC+"闭闸");
		
		commandExe.setMeterId(commAddr);
		commandExe.setCmdTable("meterBreakerCmdServiceImpl");
		
		commandExe.setCreateTime(new Date(System.currentTimeMillis()));
		commandExe.setStatus(new Byte("10"));  //0创建；1成功；2失败；3超时；10正在处理
		commandExe.setInns(inns);
		BaseInnEXE inn = new BaseInnEXE();
	
		//电表指令
		if("0201".equals(orgEquipment.getEquipType())){
			orgEquipment.setEquipType("0301");
			inn = CmdInnEXEUtil.noEncryptContrlInnExe(commandExe, PriceEnum.NoEncryptElecOff, orgEquipment, 1, new Byte("10"));
			inns.add(inn);
		//水表指令
		}else {
			orgEquipment.setEquipType("0302");
			inn = CmdInnEXEUtil.noEncryptContrlInnExe(commandExe, PriceEnum.NoEncryptWaterOff, orgEquipment, 1, new Byte("10"));
			inns.add(inn);
		}
		innList.add(inn.getInnId());
		commandExe.setInnEXE(inn);
		try {
			result.setCmdId(commandExe.getCmdId());
			result.setExeTime(1400l*(inns.size()));
			result.setIsRepeat(true);
			result.setInnArr(innList);
			result.setMeterAddr(commandExe.getMeterAddr());
			result.setCmdName(commandExe.getCmdName());
			CmdCallBack tempCallback = null;
			if(callback == null){
				tempCallback = new HandleCallback();
			}else{
				tempCallback = callback;
			}
			cmdHandleService.execute(commandExe, tempCallback);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;*/
	}
	
	@Override
	public void execCmd(BaseCmdEXE cmd) {
		
		/*try {
			BaseMeter meter =baseMeterMapper.selectByMeterAddr(cmd.getCmdObjid());
			cmd.setParamObj(meter);
			cmd.setCmdLevel(CMD_LEVEL+"");
			cmd.setRetryNum(retryNum);
			cmdHandleService.executeNoInsert(cmd, new HandleCallback());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" handle error Exception:{}",e.toString());
		}*/
	}
	
	class HandleCallback implements CmdCallBack{
		@Override
		public void cmdSuccess(BaseCmdEXE cmd) throws Exception {
			BaseInnEXE currInn = cmd.getInnEXE();
			String vals = currInn.getReturnValue();
			String resultStr = currInn.getReturnValue();
			JSONObject resultObj = JSONObject.parseObject(resultStr);
			JSONObject resultData = resultObj.getJSONObject("data");
			Integer retry = cmd.getReqNum();
			//执行失败处理
			if(!resultData.getBooleanValue("success")){
				//向上抛异常 ，重新计数执行
			    String returnStr = "return value error,curr retryNum:"+retry+",currRetryControl:"+currInn.getCommand().getIdentity();
			    throw new Exception(returnStr);
			}
			try {
				if(PriceEnum.NoEncryptElecOff.equals(currInn.getCommand()) || PriceEnum.NoEncryptWaterOff.equals(currInn.getCommand())){
					//sysEventService.addOpenCloseEvent(currInn.getMeterAddr(), true);
				}
				if(PriceEnum.NoEncryptElecOn.equals(currInn.getCommand()) || PriceEnum.NoEncryptElecOn2.equals(currInn.getCommand())||
						PriceEnum.NoEncryptWaterOn.equals(currInn.getCommand())
						){
					//sysEventService.addOpenCloseEvent(currInn.getMeterAddr(), false);
				}
			} catch (Exception e) {
			}
			logger.debug("{} handle success result{}",cmd.getCmdName(),vals);
		}

		@Override
		public void cmdError(BaseCmdEXE cmd) throws Exception {
			BaseInnEXE currInn = cmd.getInnEXE();
			String vals = currInn.getReturnValue();
			logger.debug("{} handle cmdError result{}",cmd.getCmdName(),vals);			
		}
	}

	


}
