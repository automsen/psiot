package cn.com.tw.paas.monit.service.inn.callback;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.InsExe;
import cn.com.tw.paas.monit.mapper.org.PushLogMapper;
import cn.com.tw.paas.monit.mapper.read.ReadHistoryMapper;
import cn.com.tw.paas.monit.mapper.read.ReadLastMapper;

import com.alibaba.fastjson.JSONObject;

/**
 *  充值回调
 * @author liming
 * 2018年3月7日18:57:37
 */
@Service(value="rechargeCallback")
@Scope("prototype") // 多例 
public class RechargeCallback extends AbstractCmdCallback{

	
	private Logger logger = LoggerFactory.getLogger(RechargeCallback.class);
	
	@Autowired
	private PushLogMapper pushLogMapper;
	@Autowired
	private ReadLastMapper readLastMapper;
	@Autowired
	private ReadHistoryMapper readHistoryMapper;
	@Autowired
	private MqHandler mqHandler;
	
	@Value("${mq.queue.meter.data}")
	private String queueDataName;
	
	@Override
	public void insCallbackSuccess(CmdExe cmd, String result) throws Exception{
		JSONObject object = JSONObject.parseObject(result);
		JSONObject data = object.getJSONObject("data");
		// 未成功！
		if(!data.getBooleanValue("success")){
			InsExe ins = cmd.getCurrIns();
			// 不重试，直接执行失败
			ins.setHandleTimes(ins.getLimitHandleTimes());
			throw new Exception();
		}
		//此处不更新数据，由事务保证插入和推送日志的一致性
		//super.insCallbackSuccess(cmd, result);
	}
	
	@Override
	public void cmdSuccess(CmdExe cmd) throws Exception{
		// 保证充值次数和推送日志的插入的一致性
		InsExe ins = cmd.getCurrIns();
		saveRecharge(cmd,ins.getReturnValue());
	}
	
	/**
	 *  充值异常捕获
	 * @param cmd
	 * @param e
	 */
	@Override
	public void cmdError(CmdExe cmd,Exception e) {
		InsExe ins = cmd.getCurrIns();
		saveRechargeError( cmd, ins.getReturnValue(), e);
		logger.info(String.format(">>>>>>>>>>>>>>>>>>>>>命令%s执行失败！",
				cmd.getCmdName()
				));
	}
	@Transactional(rollbackFor=Exception.class)
	public void saveRechargeError(CmdExe cmd,String result,Exception e){
		if(!StringUtils.isEmpty(result)){
			 JSONObject obj = JSONObject .parseObject(result);
			 JSONObject dataMap = obj.getJSONObject("data");
			 // 包含返回的数据，可能会更新充值次数
			 if(dataMap!= null ){
				 //如果不是通讯异常， 保存充值次数
				 mqHandler.send(queueDataName, result);
				 //savePayNum(cmd, obj, dataMap);
			 }
		}
		 // 保存错误推送日志
		 //savePushLog(cmd,e);
	}
	
	
	
	@Transactional(rollbackFor=Exception.class)
	public void saveRecharge(CmdExe cmd,String result){
		 JSONObject obj = JSONObject .parseObject(result);
		 JSONObject dataMap = obj.getJSONObject("data");
		 if(dataMap!= null &&dataMap.getBooleanValue("success")){
			 // 保存充值次数
			 mqHandler.send(queueDataName, result);
			 //savePayNum(cmd, obj, dataMap);
		 }
		 // 保存推送日志
		 //savePushLog(cmd,null);
	}
	

	/*private void savePayNum(CmdExe cmd, JSONObject obj, JSONObject dataMap) {
		
		String uniqueId = obj.getString("uniqueId");
		// 1. 更新本地readlast,history 的充值次数参数
		String[] uniqueIds = uniqueId.split(":");
		// 仪表地址
		String meterAddr = uniqueIds[0];
		// 时间戳
		String readTimeStr = uniqueIds[1];
		Date readTime = new Date(Long.valueOf(readTimeStr));
		// 数据标识
		String dataMarker = (String) dataMap.get("dataMarker");
		// 数据
		String dataValueStr = (String) dataMap.get("dataValues");
		if (StringUtils.isEmpty(dataMarker) || StringUtils.isEmpty(dataValueStr)) {
			return;
		}
		String[] dataValues = dataValueStr.split(",");
		// 根据数据标识查询相关数据项
		@SuppressWarnings("unchecked")
		List<InsDataItem> items = (List<InsDataItem>) cache.get(CacheNameCons.ins_dataItem, dataMarker);
		if (null == items) {
			return;
		}
		// 默认倍率
		BigDecimal pt = new BigDecimal("1");
		BigDecimal ct = new BigDecimal("1");
		// 查询仪表
		TerminalEquip meter = cmd.getTerminalEquip();
		String orgId = "";
		String appId = "";
		String equipType = "";
		String elecMeterType = "";
		if (null != meter) {
			orgId = meter.getOrgId();
			appId = meter.getAppId();
			equipType = meter.getEquipTypeCode();
			if(equipType.equals("0201")){
				elecMeterType = meter.getElecMeterTypeCode();
				pt = meter.getElecPt();
				ct = meter.getElecCt();
			}
		}
		List<ReadLast> readList = new ArrayList<ReadLast>();
		Map<String,Object> pushData = cmd.getPushData();
		
		if(pushData == null){
			pushData = new HashMap<String,Object>();
			cmd.setPushData(pushData);
		}
		for (InsDataItem item : items) {
			BigDecimal readValue;
			// 有对应数据则保存
			try {
				readValue = new BigDecimal(dataValues[item.getItemIndex() - 1]);
				if (item.getIsMultiplyCt().equals((byte)1)){
					readValue = readValue.multiply(ct);
				}if (item.getIsMultiplyPt().equals((byte)1)){
					readValue = readValue.multiply(pt);
				}
				pushData.put(item.getItemCode(), readValue);
			} catch (Exception e) {
				continue;
			}
			ReadLast temp = new ReadLast();
			temp.setOrgId(orgId);
			temp.setAppId(appId);
			temp.setMeterType(equipType);
			temp.setElecMeterType(elecMeterType);
			temp.setMeterAddr(meterAddr);
			temp.setItemCode(item.getItemCode());
			temp.setItemName(item.getItemName());
			temp.setReadValue(readValue.toPlainString());
			temp.setIsPush((byte) 0);
			temp.setReadTime(readTime);
			readList.add(temp);
		}
		
		// TODO 更新最近读数
		if (readList.size() > 0) {
			for (ReadLast readOne : readList) {
				readLastMapper.replace(readOne);
				readHistoryMapper.insert(readOne);
			}
		}
	}*/
	
	@Override
	public void insCallbackError(CmdExe cmd,Exception e) {
		InsExe inn = cmd.getCurrIns();
		logger.info(String.format(">>>>>>>>>>>>>>>>>>>>>订单号%s,指令%s,执行失败！",
				cmd.getBusinessNo(),inn.getDataMarker()
				));
	}


	
	
	
	
	
	
	
	

}
