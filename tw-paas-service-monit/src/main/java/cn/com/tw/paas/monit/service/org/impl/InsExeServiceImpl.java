package cn.com.tw.paas.monit.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.paas.monit.entity.db.org.InsExe;
import cn.com.tw.paas.monit.mapper.org.InsExeMapper;
import cn.com.tw.paas.monit.service.org.InsExeService;

@Service
public class InsExeServiceImpl implements InsExeService{

	@Autowired
	private InsExeMapper insExeMapper;
	
	@Override
	public void updateIns(InsExe ins) {
		insExeMapper.updateByPrimaryKeySelective(ins);
	}

	@Override
	public List<InsExe> selectByEntity(InsExe ins) {
		return insExeMapper.selectByEntity(ins);
	}
}
