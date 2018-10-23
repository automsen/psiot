package cn.com.tw.paas.monit.service.org.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.tw.common.core.cache.redis.RedisService;
import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.core.jms.cons.PsMqCons;
import cn.com.tw.common.enm.notify.NotifyBusTypeEm;
import cn.com.tw.common.enm.notify.NotifyLvlEm;
import cn.com.tw.common.utils.notify.Notify;
import cn.com.tw.common.utils.notify.NotifyUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.EquipNetStatusExpand;
import cn.com.tw.paas.monit.entity.db.org.EquipNetStatus;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.mapper.org.EquipNetStatusMapper;
import cn.com.tw.paas.monit.mapper.org.OrgApplicationMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.service.org.EquipNetStatusService;

@Service
public class EquipNetStatusServiceImpl implements EquipNetStatusService{
	
	@Autowired
	private EquipNetStatusMapper equipNetStatusMapper;

	@Autowired
	private TerminalEquipMapper terminalEquipMapper;
	
	@Autowired
	private OrgApplicationMapper orgApplicationMapper;
	
	@Autowired
	private MqHandler mqHandler;
	@Autowired
	private RedisService redisService;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(EquipNetStatus arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(EquipNetStatus arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EquipNetStatus selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EquipNetStatus> selectByPage(Page arg0) {
		return null;
	}

	@Override
	public int update(EquipNetStatus arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(EquipNetStatus arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * 更新网络状态
	 *  1.更新状态 为 2 时做异常处理
	 *    如果旧状态为失联预警 ，修改状态为失联 ，记录失联时间，更新状态，推送异常
	 *    如果旧状态为 正常，未连接 ，修改状态为失联预警，继续往redis写delayKey监听 
	 *  2. 更新状态为1 时
	 *    更新连接时间，更新连接状态 
	 * @param commAddr 设备编号
	 * @param status   即将更新的状态
	 * @return
	 */
	@Override
	public void updateStatus(String commAddr,String status){
		// 未录入档案的设备不处理
		TerminalEquip meterInfo = terminalEquipMapper.selectByEquipNumber(commAddr);
		if (null != meterInfo) {
			EquipNetStatus oldStatus = equipNetStatusMapper.selectByCommAddr(commAddr);
			EquipNetStatus newStatus = new EquipNetStatus();
			newStatus.setCommAddr(commAddr);
			//判断最大预警次数
			int max = 0;
			// 中间件返回的状态含义与数据库设计相反
			//  0.未通讯 1.通讯正常 2.通讯告警 3.通讯失联
			if("0".equals(status)){
				newStatus.setNetStatus((byte)0);
			}else if("1".equals(status)){
				newStatus.setNetStatus((byte)1);
				newStatus.setOnlineTime(new Date());
				newStatus.setOfflineTime(null);
				newStatus.setLossOfCommNum(max);
			}else if("2".equals(status)){
				if(oldStatus == null){
					oldStatus = new EquipNetStatus();
					oldStatus.setLossOfCommNum(0);
					oldStatus.setNetStatus(new Byte("0"));
				}else{
					newStatus.setId(oldStatus.getId());
				}
				newStatus.setNetStatus(new Byte(status));
				newStatus.setLossOfCommNum(oldStatus.getLossOfCommNum()+1);
					// 上一个状态是警告
				if(new Byte("2").equals(oldStatus.getNetStatus())){
					newStatus.setOfflineTime(new Date());
					newStatus.setNetStatus(new Byte("3")); // 失联
			    	/**
			    	 *  异常推送，状态更新不考虑原子性
			    	 */
			    	try {
		    			OrgApplication applications = orgApplicationMapper.selectOrgApplicationByOrgId(meterInfo.getOrgId());
	    				// 发送回调不做任何数据操作
	    				String returnJson = NotifyUtils.sendTermEventMsg(
	    						meterInfo.getEquipNumber(),
	    						meterInfo.getEquipTypeCode(),
	    						null,
	    						NotifyBusTypeEm.termNoContactError, null,
	    						System.currentTimeMillis() );
	    				String pushStr = Notify
	    						.createEvent(applications.getOrgId(), applications.getCallbackUrl(),
	    								returnJson, 5)
	    						.setPaasBusData(NotifyBusTypeEm.termCmdError, null, null, null, applications.getOrgId(),
	    								NotifyLvlEm.HIGH)
	    						.toJsonStr();
	    				mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, pushStr);
			    	}catch (Exception e) {
					}
					
				}else if(new Byte("0").equals(oldStatus.getNetStatus()) || new Byte("1").equals(oldStatus.getNetStatus())){
					newStatus.setNetStatus(new Byte("2"));
					 // 重新插入delayKey
				    try {
					    String gatherHzStr = meterInfo.getGatherHz();
					    if(StringUtils.isEmpty(gatherHzStr)){
					    	gatherHzStr = "3600";
					    }
					    redisService.set("EX:COMM:METER:"+meterInfo.getEquipNumber(), meterInfo.getOrgId(), Integer.valueOf(gatherHzStr));
					} catch (Exception e) {
					}
				}
			}
			equipNetStatusMapper.replace(newStatus);
		}
	}

	@Override
	public List<EquipNetStatusExpand> selectEquipNetStatusByPage(Page page) {
		List<EquipNetStatusExpand> equipNetStatusExpands = equipNetStatusMapper.selectByPage(page);
		return equipNetStatusExpands;
	}

	@Override
	public List<EquipNetStatusExpand> selectNetByPage(Page page) {
		List<EquipNetStatusExpand> equipNetStatusExpands = equipNetStatusMapper.selectNetByPage(page);
		return equipNetStatusExpands;
	}

}
