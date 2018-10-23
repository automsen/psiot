package cn.com.tw.paas.monit.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.org.OrgResident;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquipChildren;
import cn.com.tw.paas.monit.mapper.org.OrgResidentMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipChildrenMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.service.org.OrgResidentService;

@Service
public class OrgResidentServiceImpl implements OrgResidentService {

	@Autowired
	private OrgResidentMapper orgResidentMapper;
	@Autowired
	private TerminalEquipMapper terminalEquipMapper;
	@Autowired
	private TerminalEquipChildrenMapper terminalEquipChildrenMapper;
	
	@Override
	public int deleteById(String arg0) {
		return orgResidentMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public int insert(OrgResident arg0) {
		return orgResidentMapper.insert(arg0);
	}

	@Override
	public int insertSelect(OrgResident arg0) {
		return orgResidentMapper.insertSelective(arg0);
	}

	@Override
	public OrgResident selectById(String arg0) {
		return orgResidentMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<OrgResident> selectByPage(Page arg0) {
		return null;
	}

	@Override
	public int update(OrgResident arg0) {
		return orgResidentMapper.updateByPrimaryKey(arg0);
	}

	@Override
	public int updateSelect(OrgResident arg0) {
		return orgResidentMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public List<OrgResident> selectResidentByEntity(OrgResident resident) {
		return orgResidentMapper.selectResidentByEntity(resident);
	}

	@Override
	public OrgResident selectResidentByLocatioId(TerminalEquip terminalEquip) {
		// TODO Auto-generated method stub
		if(null != terminalEquip.getEquipId() && !"".equals(terminalEquip.getEquipId())){
			TerminalEquip ter = terminalEquipMapper.selectByPrimaryKey(terminalEquip.getEquipId());
			//如果名字没有设置过就查询居民基础信息表
			OrgResident orgResident = new OrgResident();
			if(null != ter.getfUserName() && !"".equals(ter.getfUserName())){//保存过
				TerminalEquipChildren terminalEquipChildren = new TerminalEquipChildren();
				terminalEquipChildren.setDtuId(terminalEquip.getEquipId());
				List<TerminalEquipChildren> list = terminalEquipChildrenMapper.selectByEntity(terminalEquipChildren);
				orgResident.setResidentName(ter.getfUserName());
				orgResident.setResidentPhone(ter.getfPhone());
				if(null != list && list.size() > 0){
					orgResident.setManufacturer(list.get(0).getManufacturer());
					orgResident.setEquipTypes(list.get(0).getEquipModel());
				}
				return orgResident;
			}else{//未保存过，传基础参数
				orgResident.setLocationId(terminalEquip.getLocationId());
				List<OrgResident> residentList = orgResidentMapper.selectResidentByEntity(orgResident);
				if(null != residentList && residentList.size() > 0){
					orgResident = residentList.get(0);
					orgResident.setEquipTypes(null);//传空方便前端判断
					return orgResident;
				}
			}
		}
		return null;
	}

}
