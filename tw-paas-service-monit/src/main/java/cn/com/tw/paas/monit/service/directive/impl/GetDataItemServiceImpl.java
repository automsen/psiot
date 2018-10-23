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
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;
import cn.com.tw.paas.monit.entity.db.command.BaseInnEXE;
import cn.com.tw.paas.monit.entity.db.org.EquipInsGroup;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.entity.db.orgEquipment.OrgEquipment;
import cn.com.tw.paas.monit.mapper.org.EquipInsGroupMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.mapper.orgEquipment.OrgEquipmentMapper;
import cn.com.tw.paas.monit.service.directive.GetDataItemService;
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
public class GetDataItemServiceImpl implements GetDataItemService {

	private static Logger logger = LoggerFactory.getLogger(GetDataItemServiceImpl.class);


	@Autowired
	private CmdHandleService cmdHandleService;
	@Autowired
	private OrgEquipmentMapper orgEquipmentMapper;
	@Autowired
	private EquipInsGroupMapper equipInsGroupMapper;
	@Autowired
	private TerminalEquipMapper terminalEquipMapper;

	private static final int CMD_LEVEL = 20;

	private static final int retryNum = 3;

	@Autowired
	private MqHandler mqHandler;

	/**
	 * 读取采集指令
	 */
	@Override
	public PageCmdResult getDataItem(String netEquipAddr,CmdCallBack callback) {
		/*OrgEquipment orgEquipment1 = new OrgEquipment();
		orgEquipment1.setCommAddr(netEquipAddr);
		OrgEquipmentExpand orgEquipment = orgEquipmentMapper.selectOrgEquipmentById(orgEquipment1);*/
		TerminalEquip terminalEquip = terminalEquipMapper.selectByNetEquipNumber(netEquipAddr);
		if (terminalEquip == null) {
			logger.error("getDataItem handle error netEquipAddr is undefiend!");
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
		commandExe.setCmdName("读取采集指令");
		commandExe.setCreateTime(new Date(System.currentTimeMillis()));
		commandExe.setStatus(new Byte("10")); // 0创建；1成功；2失败；3超时；10正在处理
		commandExe.setInns(inns);
		commandExe.setIsBlock(true);   //阻塞，根据成功与否执行下一条指令
		BaseInnEXE inn1 = new BaseInnEXE();

		inn1 = CmdInnEXEUtil.getDataItemInnExe(commandExe, PriceEnum.getDataItem, terminalEquip, 1,new Byte("10"));
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
			EquipInsGroup equipInsGroup = new EquipInsGroup();
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
			
			equipInsGroup.setCommAddr(orgEquipment.getCommAddr());
			/**
			 * 数据库查出来的数据
			 */
			List<EquipInsGroup> equipInsGroups = equipInsGroupMapper.selectByAll(equipInsGroup);
			String[] resultArr;
			/**
			 * 采集器读回来的
			 */
			List<String> insConfs = new ArrayList<String>();
			String statusCode = resultObj.getString("statusCode");
			if(ResultCode.OPERATION_IS_SUCCESS.equals(statusCode)){
				String dateValues = resultData.getString("dataValues");
				String commAddr = resultData.getString("addr");
				Boolean success = resultData.getBoolean("success");
				resultArr = dateValues.split(",");
				for(int i=0;i<resultArr.length;i++){
					insConfs.add(resultArr[i]);
				}
				
				List<EquipInsGroup> equipInsGroups2 = new ArrayList<EquipInsGroup>();
				if(equipInsGroups != null && equipInsGroups.size() > 0){
					for (String insConf : insConfs) {
						EquipInsGroup equipInsGroup2 = new EquipInsGroup();
						for (EquipInsGroup insGroup : equipInsGroups) {
							/**
							 * 与数据库表一致的 往list里面放 状态为1
							 */
							if(insConf.equals(insGroup.getDataMarker())){
								/**
								 * callback
								 */
								equipInsGroup2.setInsStatus((byte) 1);
								equipInsGroup2.setDataMarker(insConf);
								equipInsGroup2.setCommAddr(orgEquipment.getCommAddr());
								/**
								 * 数据库修改
								 */
								insGroup.setInsStatus((byte) 1);
								insGroup.setUpdateTime(new Date(System.currentTimeMillis()));
								equipInsGroupMapper.updateByPrimaryKeySelective(insGroup);
								equipInsGroups.remove(insGroup);
							}else{
								/**
								 * callback
								 */
								equipInsGroup2.setInsStatus((byte) 2);
								equipInsGroup2.setDataMarker(insConf);
								equipInsGroup2.setCommAddr(orgEquipment.getCommAddr());
								/**
								 * 数据库修改
								 */
								insGroup.setInsStatus((byte) 2);
								insGroup.setUpdateTime(new Date(System.currentTimeMillis()));
								equipInsGroupMapper.updateByPrimaryKeySelective(insGroup);
							}
						}
						equipInsGroups2.add(equipInsGroup2);
					}
					equipInsGroups2.addAll(equipInsGroups);
					String jsonString = JsonUtils.objectToJson(equipInsGroups2);
					cmd.setReturnValue(jsonString);
				}
				
			}
			
		}

		@Override
		public void cmdError(BaseCmdEXE cmd) throws Exception {
			BaseInnEXE currInn = cmd.getInnEXE();
			String vals = currInn.getReturnValue();
			logger.debug("{} handle cmdError result{}", cmd.getCmdName(), vals);
		}
	}

}
