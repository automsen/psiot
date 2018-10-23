package cn.com.tw.saas.serv.service.loadShedding.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.loadShedding.RuleElecOnTimeUsual;
import cn.com.tw.saas.serv.mapper.loadShedding.RuleElecOnTimeUsualMapper;
import cn.com.tw.saas.serv.service.loadShedding.RuleElecOnTimeUsualService;

@Service
public class RuleElecOnTimeUsualServiceImpl implements
		RuleElecOnTimeUsualService {
	
	@Autowired
	private RuleElecOnTimeUsualMapper ruleElecOnTimeUsualMapper;

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(RuleElecOnTimeUsual arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(RuleElecOnTimeUsual arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RuleElecOnTimeUsual selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RuleElecOnTimeUsual> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(RuleElecOnTimeUsual arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(RuleElecOnTimeUsual arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int replaceRuleElecOnTimeUsual(List<RuleElecOnTimeUsual> infos) {
		try {
			for (RuleElecOnTimeUsual ruleElecOnTimeUsual : infos) {
				ruleElecOnTimeUsual.setCreateTime(new Date());
				ruleElecOnTimeUsualMapper.replace(ruleElecOnTimeUsual);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

	@Override
	public List<RuleElecOnTimeUsual> selectByAll(RuleElecOnTimeUsual retu) {
		// TODO Auto-generated method stub
		return ruleElecOnTimeUsualMapper.selectByAll(retu);
	}

}
