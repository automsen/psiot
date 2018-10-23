package cn.com.tw.paas.monit.service.org.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.org.EquipGroup;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.mapper.org.EquipGroupMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.service.org.EquipGroupService;

@Service
public class EquipGroupServiceImpl implements EquipGroupService{

	@Autowired
	private EquipGroupMapper equipGroupMapper;
	
	@Autowired
	private TerminalEquipMapper terminalEquipMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(EquipGroup equipGroup) {
		// TODO Auto-generated method stub
		equipGroupMapper.insert(equipGroup);
		return 0;
	}

	@Override
	public int insertSelect(EquipGroup equipGroup) {
		// TODO Auto-generated method stub
		return equipGroupMapper.insertSelective(equipGroup);
	}

	@Override
	public EquipGroup selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EquipGroup> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(EquipGroup arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(EquipGroup arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EquipGroup selectByCommAddr(String commAddr) {
		return equipGroupMapper.selectByCommAddr(commAddr);
	}

	@Override
	public List<EquipGroup> selectAll(EquipGroup equipGroup) {
		List<EquipGroup> equipGroups = equipGroupMapper.selectAll(equipGroup);
		return equipGroups;
	}

	@Override
	@Transactional
	public int insertSelect(List<EquipGroup> equipGroupList) {
		if(equipGroupList != null && equipGroupList.size() > 0){
			for (EquipGroup equipGroup : equipGroupList) {
				insertEquipGroup(equipGroup);
				updateTerminalEquip(equipGroup);
			}
		}else{
			return 1;
		}
		return 0;
	}
	
	private void insertEquipGroup(EquipGroup equipGroup){
		equipGroup.setId(CommUtils.getUuid());
		equipGroup.setCreateTime(new Date());
		equipGroupMapper.insertSelective(equipGroup);
	}
	
	private void updateTerminalEquip(EquipGroup equipGroup){
		TerminalEquip terminalEquip = new TerminalEquip();
		terminalEquip.setEquipNumber(equipGroup.getChildCommAddr());
		terminalEquip.setNetEquipNumber(equipGroup.getCommAddr());
		terminalEquip.setNetTypeCode(equipGroup.getNetTypeCode());
		terminalEquip.setNetNumber(equipGroup.getNetNumber());
		terminalEquip.setSectors(equipGroup.getSectors());
		terminalEquipMapper.updateByEquipNumber(terminalEquip);
	}

	@Override
	@Transactional
	public int deleteMeterAndGatewayRelationship(EquipGroup equipGroup) {
		if(equipGroup == null){
			return 1;
		}
		TerminalEquip terminalEquip = new TerminalEquip();
		terminalEquip.setEquipNumber(equipGroup.getChildCommAddr());
		equipGroupMapper.deleteByPrimaryKey(equipGroup);
		terminalEquipMapper.updateByEquipNumber(terminalEquip);
		return 0;
	}

}
