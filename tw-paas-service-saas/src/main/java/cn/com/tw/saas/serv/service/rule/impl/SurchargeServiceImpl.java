package cn.com.tw.saas.serv.service.rule.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.rule.Surcharge;
import cn.com.tw.saas.serv.mapper.rule.SurchargeMapper;
import cn.com.tw.saas.serv.service.rule.SurchargeService;

@Service
public class SurchargeServiceImpl implements SurchargeService{

	@Autowired
	private SurchargeMapper surchargeMapper;
	
	@Override
	public int deleteById(String surchargeId) {
		// TODO Auto-generated method stub
		surchargeMapper.deleteByPrimaryKey(surchargeId);
		return 0;
	}

	@Override
	public int insert(Surcharge arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(Surcharge surcharge) {
		surchargeMapper.insertSelective(surcharge);
		return 0;
	}

	@Override
	public Surcharge selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Surcharge> selectByPage(Page page) {
		// TODO Auto-generated method stub
		return surchargeMapper.selectByPage(page);
	}

	@Override
	public int update(Surcharge arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(Surcharge surcharge) {
		// TODO Auto-generated method stub
		surchargeMapper.updateByPrimaryKeySelective(surcharge);
		return 0;
	}

	@Override
	public List<Surcharge> selectSurchargeAll(Surcharge surcharge) {
		// TODO Auto-generated method stub
		return surchargeMapper.selectSurchargeAll(surcharge);
	}

}
