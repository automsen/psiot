package cn.com.tw.paas.monit.service.directive.impl;


import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;
import cn.com.tw.paas.monit.entity.db.command.BaseInnEXE;
import cn.com.tw.paas.monit.mapper.orgEquipment.OrgEquipmentMapper;
import cn.com.tw.paas.monit.mapper.read.ReadLastMapper;
import cn.com.tw.paas.monit.service.directive.MeterPayService;
import cn.com.tw.paas.monit.service.dlt645.CmdCallBack;
import cn.com.tw.paas.monit.service.dlt645.CmdHandleService;
import cn.com.tw.paas.monit.thread.entity.PageCmdResult;

import com.alibaba.fastjson.JSONObject;

/**
 * 充值功能
 * @author liming
 * 2017年9月15日10:59:27
 *
 */
@Service
public class MeterPayServiceImpl implements MeterPayService{
	
	private   Logger logger = LoggerFactory.getLogger(MeterPayServiceImpl.class);

	@Autowired
	private OrgEquipmentMapper orgEquipmentMapper;
	@Autowired
	private CmdHandleService cmdHandleService;
	@Autowired
	private ReadLastMapper readLastMapper;
	
	private static final int CMD_LEVEL = 20;
	
	private static final int retryNum = 0;
	/**
	 * 缴费
	 * 表计预付费
	 * @param customerNo
	 * @param money
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public PageCmdResult meterRecharge(String commAddr,CmdCallBack callback,BigDecimal money){
		return null;
		/*PageCmdResult result = new PageCmdResult();
		OrgEquipment orgEquipment1 = new OrgEquipment();
		orgEquipment1.setCommAddr(commAddr);
		OrgEquipmentExpand orgEquipment = orgEquipmentMapper.selectOrgEquipmentById(orgEquipment1);
		List<BaseInnEXE> inns = new ArrayList<BaseInnEXE>();
		BaseCmdEXE commandExe = new BaseCmdEXE();
		
		//查最近一次充值次数
		ReadLast readLast = new ReadLast();
		readLast.setMeterAddr(commAddr);
		List<ReadLast> readLasts = readLastMapper.selectLast(readLast);
		int payNum = 0;//充值次数
		for (ReadLast readLastExpand : readLasts) {
			if(!StringUtils.isEmpty(readLastExpand.getItemCode())){
				if("payTime".equals(readLastExpand.getItemCode())){
					payNum = Integer.valueOf(readLastExpand.getReadValue());
				}
			}
		}
		// 参数拼接
		// 将入口参数放入命令中
		commandExe.setCmdId(CommUtils.getUuid());
		commandExe.setRetryNum(retryNum);
		commandExe.setParamObj(recharge);
		commandExe.setCmdObjid(recharge.getOrderId());
		commandExe.setMeterId(orgEquipment.getCommAddr());
		commandExe.setMeterAddr(orgEquipment.getCommAddr());
		commandExe.setCmdLevel(CMD_LEVEL + "");
		commandExe.setCmdName("仪表充值");
		commandExe.setCreateTime(new Date(System.currentTimeMillis()));
		commandExe.setStatus(new Byte("10")); // 0创建；1成功；2失败；3超时；10正在处理
		commandExe.setInns(inns);
		commandExe.setIsBlock(false);   //非阻塞，直接执行下一条指令
		//当前指令不允许重试
		BaseInnEXE inn1 = new BaseInnEXE();
		//首冲
		if(payNum == 0){
			inn1  = CmdInnEXEUtil.rechargeInnExe(commandExe, PriceEnum.firstrecharge, orgEquipment, 1, new Byte("10"), money, 1);
		}else{
			inn1  = CmdInnEXEUtil.rechargeInnExe(commandExe, PriceEnum.normrecharge, orgEquipment, 1, new Byte("10"), money, payNum+1);
		}
		inns.add(inn1);
		commandExe.setInnEXE(inn1);
		
		result.setCmdId(commandExe.getCmdId());
		result.setExeTime(400l*inns.size());
		result.setIsRepeat(false);
		result.setMeterAddr(commandExe.getMeterAddr());
		result.setCmdName(commandExe.getCmdName());
		try {
			cmdHandleService.execute(commandExe, new rechargeCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;*/
	}
	
	class rechargeCallback implements CmdCallBack{
		
		@Override
		public void cmdSuccess(BaseCmdEXE cmd) throws Exception {
				Date currTime = new Date();
				BaseInnEXE currInn = cmd.getInnEXE();
				//OrderRecharge order = (OrderRecharge) cmd.getParamObj();
				JSONObject result = JSONObject.parseObject(currInn.getReturnValue());
				JSONObject resultData = result.getJSONObject("data");
				if (!resultData.getBooleanValue("success")) {
					// 向上抛异常 ，重新计数执行
					String returnStr = "return value error,curr retryNum:" + cmd.getReqNum() + ",currRetryControl:"
							+ currInn.getCommand().getIdentity();
						// 充值次数错误。直接跳过重试
						if("20".equals(resultData.getString("errCode"))||"02".equals(resultData.getString("errCode"))){
							cmd.setReqNum(cmd.getRetryNum());
						}
					throw new Exception(returnStr);
				}
				try{
					String resultStr = resultData.getString("dataValues");
					String[] resultArr = resultStr.split(",");
					//更新状态 0  当前剩余金额	1 当前透支金额	2 表内已购电次数	3 本次金额变动	4 表内累计电量
					BigDecimal currMoney = new BigDecimal(resultArr[0]);
					BigDecimal overMoney = new BigDecimal(resultArr[1]);
					BigDecimal payNum =    new BigDecimal(resultArr[2]);
					BigDecimal changeMoney = new BigDecimal(resultArr[3]);
					BigDecimal currPapval = new BigDecimal(resultArr[4]);
					//全部执行成功，更新充值状态，扣费，减去余额
//						order.setAftReadValue(currPapval);
//						order.setAftMoney(currMoney);
//						order.setRechargeNum(payNum.intValue());
//					//更新状态
//					updateOrderStatusSuccess(currTime, order, overMoney
//							, changeMoney);
				}catch(Exception e){
					e.printStackTrace();
				}
		}
		@Override
		public void cmdError(BaseCmdEXE cmd) throws Exception {
//			logger.error(" cmdError rechargeMoney error");
//			BaseInnEXE currInn = cmd.getInnEXE();
//			OrderRecharge order = (OrderRecharge) cmd.getParamObj();
//			updateMeterPayError( order, currInn);
		}
	}


//	@Transactional
//	private void updateOrderStatusSuccess(Date currTime, OrderRecharge order,
//		 BigDecimal overMoney,BigDecimal changeMoney) {
//		Customer customer = customerMapper.selectByCustomerNo(order.getCustomerNo());
//		//更新账户
//		CustMeterRelation custMeterRelation = custMeterRelationMapper.selectByCustmeterMeterId(order.getMeterAddr());
//
//		order.setAftReadTime(currTime);
//		order.setStatus(OrderRechargeCommons.Status.SUCCESS);
//		order.setUpdateTime(currTime);
//		order.setAftMoney(order.getAftMoney().subtract(overMoney));  //当前金额减去透支金额
//		//更新中间表信息
//		BaseMeter meter = new BaseMeter();
//		meter.setPayNum(order.getRechargeNum());
//		meter.setMeterId(order.getMeterAddr());
//		meter.setMeterAddr(order.getMeterAddr());
//		SysEvent event = new SysEvent();
//		event.setId(CommUtils.getUuid());
//		event.setEventType(OrderRechargeCommons.ORDER_SUCCESS_EVENT_TYPE);  //充值成功事件
//		event.setEventName(OrderRechargeCommons.ORDER_SUCCESS_EVENT_NAME);
//		event.setEventLv(OrderRechargeCommons.ORDER_SUCCESS_EVENT_LV);
//		event.setMeterAddr(order.getMeterAddr());
//		event.setMeterAlias(order.getMeterAlias());
//		event.setMeterCateg(order.getMeterCateg());
//		event.setPositName(order.getPositName());
//		event.setPositId(order.getPositId());
//		event.setCustomerName(order.getCustomerName());
//		event.setCustomerNo(order.getCustomerNo());
//		
//		order.setOrgId("");  //暂时设置为空值
//		
//		event.setCreateTime(currTime);
//		event.setStatus(OrderRechargeCommons.ORDER_EVENT_CREATE_STATUS);  //0未产生通知， 1通知成功，2通知失败
//		event.setDetail(OrderRechargeCommons.getSuccessStr(order.getMeterAddr(), order.getRechargeMoney()));
//		sysEventMapper.insert(event);
//		//更新充值次数
//		baseMeterMapper.updateByPrimaryKeySelective(meter);
//		custMeterRelation.setCloseTime(currTime);
//		custMeterRelation.setCloseData(order.getAftReadValue());
//		custMeterRelation.setRechargeTime(order.getCreateTime());
//		custMeterRelation.setMeterMoney(order.getAftMoney());
//		custMeterRelationMapper.updateByPrimaryKeySelective(custMeterRelation);
//		//更新订单
//		orderRechargeMapper.updateByPrimaryKeySelective(order);
//		// 发送短信
//		try {
//			mqHandler.send(MqCons.QUEUE_NOTIFY_NAME, NotifyUtils.sendSMSTemplate(
//					customer.getCustomerMobile1(),    // 用户手机号
//					NotifyNum.RECHARGE_WARN.getValue(),  // 微信平台 模板编号
//					OrderRechargeCommons.getMessageStr(order.getMeterAddr(), 
//							order.getRechargeMoney(), order.getAftMoney()),  // 微信平台模板参数  1,2,3 逗号隔开格式
//					NotifyLvlEm.LOW.getValue(), 
//					event.getId()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 更新仪表
	 * @param currTime
	 * @param order
	 * @param currMoney
	 * @param overMoney
	 * @param payNum
	 * @param changeMoney
	 */
//	public void updateMeterPayError(OrderRecharge order,BaseInnEXE innexe){
//		try {
//			if(!StringUtils.isEmpty(innexe.getReturnValue())){
//				JSONObject result = JSONObject.parseObject(innexe.getReturnValue());
//				JSONObject resultData = result.getJSONObject("data");
//				if(resultData != null){
//					if("20".equals(resultData.getString("errCode"))||"02".equals(resultData.getString("errCode"))){
//						String resultStr = resultData.getString("dataValues");
//						String[] resultArr = resultStr.split(",");
//						//更新状态 0  当前剩余金额	1 当前透支金额	2 表内已购电次数	3 本次金额变动	4 表内累计电量
//						BigDecimal payNum =    new BigDecimal(resultArr[2]);
//						//更新充值次数
//						BaseMeter meter = new BaseMeter();
//						meter.setMeterId(innexe.getMeterId());
//						meter.setMeterAddr(innexe.getMeterAddr());
//						meter.setPayNum(payNum.intValue());
//						baseMeterMapper.updateByPrimaryKeySelective(meter);
//					}
//				}
//			}
//			//充值订单失败  ，状态和指令的错误状态一致    2，失败  3， 超时
//			int status = innexe.getStatus().intValue();
//			if(status == 2 || status == 3){
//				order.setStatus(innexe.getStatus());
//			}else{
//				order.setStatus(new Byte("2")); // 默认设置为失败
//			}
//			orderRechargeMapper.updateByPrimaryKeySelective(order);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("充值错误修改状态失败 ,异常{}",e.getMessage());
//		}
//		
//	}
	
}
