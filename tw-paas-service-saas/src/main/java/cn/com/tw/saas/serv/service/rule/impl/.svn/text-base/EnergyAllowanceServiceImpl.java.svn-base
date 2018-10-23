package cn.com.tw.saas.serv.service.rule.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.db.rule.EnergyAllowance;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.rule.EnergyAllowanceMapper;
import cn.com.tw.saas.serv.service.rule.EnergyAllowanceService;

@Service
public class EnergyAllowanceServiceImpl implements EnergyAllowanceService {

	@Autowired
	private EnergyAllowanceMapper allowanceMapper;
	@Autowired
	private RoomMapper roomMapper;
	
	@Override
	public List<EnergyAllowance> selectByPage(Page page) {
		return allowanceMapper.selectByPage(page);
	}
	
	@Override
	public List<EnergyAllowance> selectByEntity(EnergyAllowance param) {
		return allowanceMapper.selectByEntity(param);
	}
	
	@Override
	public int deleteById(String ruleId) {
		EnergyAllowance rule = new EnergyAllowance();
		rule.setRuleId(ruleId);
		rule = allowanceMapper.selectByPrimaryKey(ruleId);
		
		Room room = new Room();
		if(rule.getEnergyType().equals("110000")){
			room.setElecAllowanceRuleId(ruleId);
		}
		else if (rule.getEnergyType().equals("120000")){
			room.setWaterAllowanceRuleId(ruleId);
		}
		List<Room> rooms = roomMapper.selectByEntity(room);
		if(rooms != null && rooms.size() > 0){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该规则已被使用");
		}
		return allowanceMapper.deleteByPrimaryKey(ruleId);
	}

	@Override
	@Transactional
	public int insert(EnergyAllowance rule) {
		//规则名判重
		EnergyAllowance param=new EnergyAllowance();
		param.setRuleName(rule.getRuleName());
		List<EnergyAllowance> dataList=allowanceMapper.selectByEntity(param);
		if(dataList!=null&&dataList.size()>0){
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR,"规则名已存在");
		}
		// 插入新的默认规则
		if(rule.getIsDefault() == 1){
			EnergyAllowance allowanceRule = new EnergyAllowance();
			// 查询机构当前默认规则
			allowanceRule.setOrgId(rule.getOrgId());
			allowanceRule.setIsDefault((byte) 1);
			allowanceRule.setEnergyType(rule.getEnergyType());
			List<EnergyAllowance> allowanceRules = allowanceMapper.selectByEntity(allowanceRule);
			// 原默认规则改为非默认
			if(allowanceRules != null && allowanceRules.size() > 0){
				EnergyAllowance oldDefault = allowanceRules.get(0);
				oldDefault.setIsDefault((byte)0);
				allowanceMapper.updateByPrimaryKeySelective(oldDefault);
			}
		}
		return allowanceMapper.insert(rule);
	}

	@Override
	public EnergyAllowance selectById(String ruleId) {
		return allowanceMapper.selectByPrimaryKey(ruleId);
	}

	@Override
	@Transactional
	public int update(EnergyAllowance rule) {
		//规则名判重
		EnergyAllowance allowance=allowanceMapper.selectByPrimaryKey(rule.getRuleId());
		if(!allowance.getRuleName().equals(rule.getRuleName())){
			EnergyAllowance param=new EnergyAllowance();
			param.setOrgId(rule.getOrgId());
			param.setRuleName(rule.getRuleName());
			List<EnergyAllowance> allowanceslist=allowanceMapper.selectByEntity(param);
			if(allowanceslist!=null&&allowanceslist.size()>0){
				throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR,"规则名已存在");
			}
		}
		
		
		
		// 更新新的默认规则
		if(rule.getIsDefault() == 1){
			EnergyAllowance allowanceRule = new EnergyAllowance();
			// 查询机构当前默认规则
			allowanceRule.setOrgId(rule.getOrgId());
			allowanceRule.setIsDefault((byte) 1);
			allowanceRule.setEnergyType(rule.getEnergyType());
			List<EnergyAllowance> allowanceRules = allowanceMapper.selectByEntity(allowanceRule);
			// 原默认规则改为非默认
			if(allowanceRules != null && allowanceRules.size() > 0){
				EnergyAllowance oldDefault = allowanceRules.get(0);
				// 原默认规则与新默认规则不是同一条规则
				if(!rule.getRuleId().equals(oldDefault.getRuleId())){
					oldDefault.setIsDefault((byte)0);
					allowanceMapper.updateByPrimaryKeySelective(oldDefault);
				}
			}
		}
		return allowanceMapper.updateByPrimaryKeySelective(rule);
	}
	
	
}
