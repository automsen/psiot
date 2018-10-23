package cn.com.tw.paas.monit.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.paas.monit.entity.db.base.CmdIns;
import cn.com.tw.paas.monit.entity.db.base.Ins;
import cn.com.tw.paas.monit.mapper.base.InsMapper;
import cn.com.tw.paas.monit.service.base.InsService;

@Service
public class InsServiceImpl implements InsService {
	@Autowired
	private InsMapper insMapper;
	
	@Override
	public List<Ins> selectInsByCmdIns(CmdIns cmdins) {
		return insMapper.selectInsByCmdIns(cmdins);
	}

}
