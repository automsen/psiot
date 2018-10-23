package cn.com.tw.paas.monit.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquipParamDtu;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipParamDtuMapper;
import cn.com.tw.paas.monit.service.org.TerminalEquipParamDtuService;

@Service
public class TerminalEquipParamDtuServiceImpl implements
		TerminalEquipParamDtuService {

	@Autowired
	private TerminalEquipParamDtuMapper terminalEquipParamDtuMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return terminalEquipParamDtuMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public int insert(TerminalEquipParamDtu arg0) {
		// TODO Auto-generated method stub
		return terminalEquipParamDtuMapper.insert(arg0);
	}

	@Override
	public int insertSelect(TerminalEquipParamDtu arg0) {
		// TODO Auto-generated method stub
		return terminalEquipParamDtuMapper.insertSelective(arg0);
	}

	@Override
	public TerminalEquipParamDtu selectById(String arg0) {
		// TODO Auto-generated method stub
		return terminalEquipParamDtuMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<TerminalEquipParamDtu> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(TerminalEquipParamDtu arg0) {
		// TODO Auto-generated method stub
		return terminalEquipParamDtuMapper.updateByPrimaryKey(arg0);
	}

	@Override
	public int updateSelect(TerminalEquipParamDtu arg0) {
		// TODO Auto-generated method stub
		return terminalEquipParamDtuMapper.updateByPrimaryKeySelective(arg0);
	}

}
