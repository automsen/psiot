package cn.com.tw.paas.service.queue.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.core.jms.cons.PsMqCons;
import cn.com.tw.common.enm.notify.NotifyBusTypeEm;
import cn.com.tw.common.enm.notify.NotifyLvlEm;
import cn.com.tw.common.utils.notify.Notify;
import cn.com.tw.common.utils.notify.NotifyUtils;
import cn.com.tw.common.utils.tools.security.MD5Utils;
import cn.com.tw.paas.service.queue.dao.read.HReadLastDao;
import cn.com.tw.paas.service.queue.entity.ElecUnderVoltageEvent;
import cn.com.tw.paas.service.queue.entity.StopValEvent;
import cn.com.tw.paas.service.queue.entity.WaterUnderVoltageEvent;
import cn.com.tw.paas.service.queue.eum.HisColEnum;

@Service
public class EventService {
	
	@Autowired
	private MqHandler mqHandler;
	
	@Autowired
	private HReadLastDao headLastDao;
	
	private static Logger logger = LoggerFactory.getLogger(EventService.class);
	
	/**
	 * //分表判断电表和水表 的止码， 如果采集的止码比最后一次的止码小的话，触发数据异常事件
	 * @param termAddr 仪表地址
	 * @param termType 仪表类型
	 * @param shortCode 如果是电表，止码为TAE，如果是水表，止码为WF
	 * @param newValue
	 */
	public void stopValEventToPush(StopValEvent valEvent) {
		
		String newValue = valEvent.getNewVal();
		
		String shortCode = valEvent.getShortCode();
		
		if (StringUtils.isEmpty(valEvent.getShortCode())) {
			logger.warn("stopValEvent method : shortCode is null!");
			return;
		}
		
		if (StringUtils.isEmpty(newValue)) {
			logger.warn("stopValEvent method : newValue is null!");
			return;
		}
		
		StringBuffer sb = new StringBuffer(MD5Utils.digest(valEvent.getTermNo())).append("|").append(0);
		
		Map<String, Object> lastData = headLastDao.queryLastByKey(sb.toString(), shortCode + ",oId");
		String lastValue = (String) lastData.get(shortCode);
		String orgId = (String) lastData.get(HisColEnum.oId.name());
		lastValue = StringUtils.isEmpty(lastValue) ? "0" : lastValue;
		if (new BigDecimal(newValue).compareTo(new BigDecimal(lastValue)) < 0) {
			//止码异常数据，后者比前者小 触发
			String pushMsg = NotifyUtils.sendTermEventMsg(valEvent.getTermNo(), valEvent.getTermType(), NotifyBusTypeEm.termValError, null, new Date().getTime());
			String eventMsg = Notify.createEvent(orgId, valEvent.getpUrl(), pushMsg, 2).setPaasBusData(NotifyBusTypeEm.termValError, null, null, null, orgId, NotifyLvlEm.HIGH).toJsonStr();
			mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, eventMsg);
		}
	}
    
    /**
     * 水表欠压
     * @param event 水表欠压事件
     */
	public void waterUnderVoltageEventToPush(WaterUnderVoltageEvent event){
		Map<String, Object> data = new HashMap<String, Object>();
		
		String volValue = event.getVoltageValue();
		
		if (StringUtils.isEmpty(volValue)) {
			logger.warn("waterUnderVoltageEventToPush method : VoltageValue is null!");
			return;
		}
		
		if (!"120000".equals(event.getTermType())){
			logger.warn("waterUnderVoltageEventToPush method : termType is not water");
			return;
		}
		
		if (new BigDecimal(volValue).compareTo(new BigDecimal(3300)) < 0) {
			logger.warn("waterUnderVoltageEventToPush method : volValue is {} vs {}", volValue, 3300);
			data.put("waterBattery", event.getVoltageValue());
			String pushMsg = NotifyUtils.sendTermEventMsg(event.getTermNo(), event.getTermType(), NotifyBusTypeEm.termBatterFlowError, data, new Date().getTime());
			String eventMsg = Notify.createEvent(event.getOrgId(), event.getpUrl(), pushMsg, 2).setPaasBusData(NotifyBusTypeEm.termBatterFlowError, null, null, null, event.getOrgId(), NotifyLvlEm.HIGH).toJsonStr();
			mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, eventMsg);
		}
	}
	
	/**
     * 电表电压异常
     * @param event
     */
	public void elecOverAndUnderEventToPush(ElecUnderVoltageEvent event, String elecVoltageRating){
		
		if (StringUtils.isEmpty(elecVoltageRating)) {
			return;
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		String volValue = event.getVoltageValue();
		
		if (StringUtils.isEmpty(volValue)) {
			logger.warn("elecOverAndUnderEventToPush method : VoltageValue is null!");
			return;
		}
		
		if (!"110000".equals(event.getTermType())){
			logger.warn("elecOverAndUnderEventToPush method : termType is not elec");
			return;
		}
		//电压报警修改：
		//220V规格的表 ＜176.00V  或者 ＞250.00V 就报警
		/*if(new BigDecimal(volValue).compareTo(new BigDecimal(220)) == 0){
			if (new BigDecimal(volValue).compareTo(new BigDecimal(176)) < 0 || new BigDecimal(volValue).compareTo(new BigDecimal(250)) > 0) {
				logger.warn("elecOverAndUnderEventToPush method : volValue is {} vs {} | {}", volValue, 176, 250.00);
				data.put("Ua", event.getVoltageValue());
				String pushMsg = NotifyUtils.sendTermEventMsg(event.getTermNo(), event.getTermType(), NotifyBusTypeEm.termElecUnderOrOver, data, new Date().getTime());
				String eventMsg = Notify.createEvent(event.getOrgId(), event.getpUrl(), pushMsg, 2).setPaasBusData(NotifyBusTypeEm.termElecUnderOrOver, null, null, null, event.getOrgId(), NotifyLvlEm.HIGH).toJsonStr();
				mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, eventMsg);
			}
		}
		
		//380V规格的表 ＜304.00V  或者 ＞418.00V 就报警
		if(new BigDecimal(volValue).compareTo(new BigDecimal(380)) == 0){
			if (new BigDecimal(volValue).compareTo(new BigDecimal(304)) < 0 || new BigDecimal(volValue).compareTo(new BigDecimal(418)) > 0) {
				logger.warn("elecOverAndUnderEventToPush method : volValue is {} vs {} | {}", volValue, 304.0, 418.0);
				data.put("Ua", event.getVoltageValue());
				String pushMsg = NotifyUtils.sendTermEventMsg(event.getTermNo(), event.getTermType(), NotifyBusTypeEm.termElecUnderOrOver, data, new Date().getTime());
				String eventMsg = Notify.createEvent(event.getOrgId(), event.getpUrl(), pushMsg, 2).setPaasBusData(NotifyBusTypeEm.termElecUnderOrOver, null, null, null, event.getOrgId(), NotifyLvlEm.HIGH).toJsonStr();
				mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, eventMsg);
			}
		}*/
		int minValue = 0;
		int maxValue = 0;
		
		if(new BigDecimal(elecVoltageRating).compareTo(new BigDecimal(220)) == 0){
			minValue = 176;
			maxValue = 250;
		}else if(new BigDecimal(elecVoltageRating).compareTo(new BigDecimal(380)) == 0){
			minValue = 304;
			maxValue = 418;
		}else{
			return;
		}
		
		if (new BigDecimal(volValue).compareTo(new BigDecimal(minValue)) < 0 || new BigDecimal(volValue).compareTo(new BigDecimal(maxValue)) > 0) {
			logger.warn("elecOverAndUnderEventToPush method : volValue is {} vs {} | {}", volValue, minValue, maxValue);
			data.put("Ua", event.getVoltageValue());
			String pushMsg = NotifyUtils.sendTermEventMsg(event.getTermNo(), event.getTermType(), NotifyBusTypeEm.termElecUnderOrOver, data, new Date().getTime());
			String eventMsg = Notify.createEvent(event.getOrgId(), event.getpUrl(), pushMsg, 2).setPaasBusData(NotifyBusTypeEm.termElecUnderOrOver, null, null, null, event.getOrgId(), NotifyLvlEm.HIGH).toJsonStr();
			mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, eventMsg);
		}
	}
}
