package cn.com.tw.saas.serv.service.rule.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.db.rule.RuleAlarm;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.rule.RuleAlarmMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.rule.RuleAlarmService;

@Service
public class RuleAlarmServiceImpl implements RuleAlarmService{

	@Autowired
	private RuleAlarmMapper ruleAlarmMapper;
	@Autowired
	private MeterMapper meterMapper;
	
	@Override
	public int deleteById(String alarmId) {
		
		Meter meter = new Meter();
		meter.setAlarmId(alarmId);
		List<Meter> meters = meterMapper.selectByEntity(meter);
		if(meters != null && meters.size() > 0){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该规则已被使用");
		}
		ruleAlarmMapper.deleteByPrimaryKey(alarmId);
		return 0;
	}

	@Override
	public int insert(RuleAlarm arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public int insertSelect(RuleAlarm ruleAlarm) {
		ruleAlarm.setAlarmId(CommUtils.getUuid());
		ruleAlarm.setCreateTime(new Date(System.currentTimeMillis()));
		
		//判断规则名是否重复
		List<RuleAlarm> listAlarms=ruleAlarmMapper.selectByRuleName(ruleAlarm.getRuleName());
		if(listAlarms!=null&&listAlarms.size()>0){
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "规则名已存在");
		}
		
		/**
		 *判断是否为默认规则 
		 */
		if(ruleAlarm.getIsDefault() == 1){
			RuleAlarm ruleAlarm2 = new RuleAlarm();
			/**
			 * 查出机构下的默认规则
			 */
			ruleAlarm2.setOrgId(ruleAlarm.getOrgId());
			ruleAlarm2.setIsDefault(ruleAlarm.getIsDefault());
			ruleAlarm2.setEquipType(ruleAlarm.getEquipType());
			List<RuleAlarm> ruleAlarms = ruleAlarmMapper.selectAlarmAll(ruleAlarm2);
			/**
			 * 判断是否为第一条插进来的 默认规则
			 */
			if(ruleAlarms != null && ruleAlarms.size() > 0){
				ruleAlarm2 = ruleAlarms.get(0);
				ruleAlarm2.setIsDefault((byte)0);
				ruleAlarmMapper.updateByPrimaryKeySelective(ruleAlarm2);
			}
		}
		
		ruleAlarmMapper.insertSelective(ruleAlarm);
		
		return 0;
	}

	@Override
	public RuleAlarm selectById(String alarmId) {
		// TODO Auto-generated method stub
		return ruleAlarmMapper.selectByPrimaryKey(alarmId);
	}

	@Override
	public List<RuleAlarm> selectByPage(Page arg0) {
		return ruleAlarmMapper.selectByPage(arg0);
	}

	@Override
	public int update(RuleAlarm arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(RuleAlarm ruleAlarm) {
		// TODO Auto-generated method stub
		//规则名判重
		RuleAlarm alarm=ruleAlarmMapper.selectByPrimaryKey(ruleAlarm.getAlarmId());
		if(!alarm.getRuleName().equals(ruleAlarm.getRuleName())){
			List<RuleAlarm> alarmlist=ruleAlarmMapper.selectByRuleName(ruleAlarm.getRuleName());
			if(alarmlist!=null&&alarmlist.size()>0){
				throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR,"规则名已存在");
			}
		}
		
		
		/**
		 *判断是否为默认规则 
		 */
		if(ruleAlarm.getIsDefault() == 1){
			RuleAlarm ruleAlarm2 = new RuleAlarm();
			/**
			 * 查出机构下的默认规则
			 */
			ruleAlarm2.setOrgId(ruleAlarm.getOrgId());
			ruleAlarm2.setIsDefault(ruleAlarm.getIsDefault());
			ruleAlarm2.setEquipType(ruleAlarm.getEquipType());
			List<RuleAlarm> ruleAlarms = ruleAlarmMapper.selectAlarmAll(ruleAlarm2);
			/**
			 * 判断是否为第一条插进来的 默认规则
			 */
			if(ruleAlarms != null && ruleAlarms.size() > 0){
				ruleAlarm2 = ruleAlarms.get(0);
				ruleAlarm2.setIsDefault((byte)0);
				ruleAlarmMapper.updateByPrimaryKeySelective(ruleAlarm2);
			}
		}
		
		ruleAlarmMapper.updateByPrimaryKeySelective(ruleAlarm);
		return 0;
	}

	@Override
	public List<RuleAlarm> selectAlarmAll(RuleAlarm ruleAlarm) {
		
		return ruleAlarmMapper.selectAlarmAll(ruleAlarm);
	}

}
