package cn.com.tw.paas.monit.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.org.EquipInsGroup;
import cn.com.tw.paas.monit.mapper.org.EquipInsGroupMapper;
import cn.com.tw.paas.monit.service.org.EquipInsGroupService;

@Service
public class EquipInsGroupServiceImpl implements EquipInsGroupService{
	
	@Autowired
	private EquipInsGroupMapper equipInsGroupMapper;

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(EquipInsGroup arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(EquipInsGroup arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EquipInsGroup selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EquipInsGroup> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(EquipInsGroup arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(EquipInsGroup arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<EquipInsGroup> selectByAll(EquipInsGroup equipInsGroup) {
		List<EquipInsGroup> equipInsGroups = equipInsGroupMapper.selectByAll(equipInsGroup);
		return equipInsGroups;
	}

}
