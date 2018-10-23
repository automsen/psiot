package cn.com.tw.paas.monit.service.equipment.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.common.utils.cons.code.MonitResultCode;
import cn.com.tw.paas.monit.entity.business.orgEquipment.OrgEquipmentExpand;
import cn.com.tw.paas.monit.entity.db.org.EquipGroup;
import cn.com.tw.paas.monit.entity.db.org.EquipNetStatus;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.orgEquipment.OrgEquipment;
import cn.com.tw.paas.monit.mapper.org.EquipGroupMapper;
import cn.com.tw.paas.monit.mapper.org.EquipNetStatusMapper;
import cn.com.tw.paas.monit.mapper.org.OrgApplicationMapper;
import cn.com.tw.paas.monit.mapper.orgEquipment.OrgEquipmentMapper;
import cn.com.tw.paas.monit.service.equipment.OrgEquipmentService;

@Service
public class OrgEquipmentServiceImpl implements OrgEquipmentService{
	
	@Autowired
	private OrgEquipmentMapper orgEquipmentMapper;
	@Autowired
	private EquipGroupMapper equipGroupMapper;
	@Autowired
	private EquipNetStatusMapper equipNetStatusMapper;
	@Autowired
	private OrgApplicationMapper orgApplicationMapper;

	@Override
	public List<OrgEquipmentExpand> selceTotalDevice(OrgEquipment orgEquipment) {
		List<OrgEquipmentExpand> orgEquipments = orgEquipmentMapper.selceTotalDevice(orgEquipment);
		return orgEquipments;
	}

	@Override
	public List<OrgEquipmentExpand> selectOrgEquipmentPage(Page page) {
		List<OrgEquipmentExpand> orgEquipments = orgEquipmentMapper.selectOrgEquipmentPage(page);
		return orgEquipments;
	}

	@Override
	public int deleteById(String paramString) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(OrgEquipment paramT) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public int insertOrgEquipmentExpand(OrgEquipmentExpand paramT) {
		
		/**
		 * 设备添加
		 */
		paramT.setEquipId(CommUtils.getUuid());
		paramT.setCreateTime(new Date(System.currentTimeMillis()));
		paramT.setOrgId(paramT.getOrgId());
		
		/**
		 * 根据机构ID查询该机构下的应用的第一条
		 */
		OrgApplication applications =  orgApplicationMapper.selectOrgApplicationByOrgId(paramT.getOrgId());
		paramT.setAppId(applications.getAppId());
		/**
		 * 通讯地址唯一校验
		 */
		OrgEquipment orgEquipment = orgEquipmentMapper.selectByCommAddr(paramT.getCommAddr());
		if(!StringUtils.isEmpty(orgEquipment)){
			throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR, "通信地址已存在");
		}
		orgEquipmentMapper.insertSelective(paramT);
		
		
		if(!"0102".equals(paramT.getEquipCateg())){
			/**
			 * 
			 */
			EquipGroup equipGroup = new EquipGroup();
			equipGroup.setId(CommUtils.getUuid());
			equipGroup.setOrgId(paramT.getOrgId());
			equipGroup.setCreateTime(new Date(System.currentTimeMillis()));
			equipGroup.setChildStatus((byte)0);
			/**
			 * RS485的通信设备地址网络设备地址 下联设备地址为终端地址  否 则都是终端地址
			 */
			if("0901".equals(paramT.getNetType())){
				equipGroup.setChildCommAddr(paramT.getCommAddr());
				equipGroup.setCommAddr(paramT.getNetEquipAddr());
			}else{
				equipGroup.setChildCommAddr(paramT.getCommAddr());
				equipGroup.setCommAddr(paramT.getCommAddr());
			}
			equipGroupMapper.insertSelective(equipGroup);
		}
		
		/**
		 * 设备状态添加 初始状态为2
		 */
		EquipNetStatus equipNetStatus = new EquipNetStatus();
		equipNetStatus.setId(CommUtils.getUuid());
		equipNetStatus.setCommAddr(paramT.getCommAddr());
		equipNetStatus.setNetStatus((byte)2);
		equipNetStatusMapper.insertSelective(equipNetStatus);
		
		
		return 0;
	}

	@Override
	public OrgEquipment selectById(String equipId) {
		return orgEquipmentMapper.selectByPrimaryKey(equipId);
	}

	@Override
	public List<OrgEquipment> selectByPage(Page paramPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(OrgEquipment paramT) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(OrgEquipment paramT) {
		paramT.setUpdateTime(new Date(System.currentTimeMillis()));
		
		/**
		 * 通讯地址唯一校验
		 */
		OrgEquipment orgEquipment1 = orgEquipmentMapper.selectByPrimaryKey(paramT.getEquipId());
		if(!orgEquipment1.getCommAddr().equals(paramT.getCommAddr())){
			OrgEquipment orgEquipment = orgEquipmentMapper.selectByCommAddr(paramT.getCommAddr());
			if(!StringUtils.isEmpty(orgEquipment)){
				throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR, "通信地址已存在");
			}
		}
		
		return orgEquipmentMapper.updateByPrimaryKeySelective(paramT);
	}

	@Override
	public List<OrgEquipmentExpand> selectOrgEquipmentAll(OrgEquipment orgEquipment) {
		List<OrgEquipmentExpand> orgEquipments = orgEquipmentMapper.selectOrgEquipmentAll(orgEquipment);
		return orgEquipments;
	}

	@Override
	public OrgEquipment selectOrgEquipmentById(OrgEquipment orgEquipment1) {
		// TODO Auto-generated method stub
		return orgEquipmentMapper.selectOrgEquipmentById(orgEquipment1);
	}

	@Override
	public OrgEquipment selectByCommAddr(String commAddr) {
		return orgEquipmentMapper.selectByCommAddr(commAddr);
	}

	@Override
	public List<OrgEquipment> selectByNetEquipAddr(String netEquipAddr) {
		return orgEquipmentMapper.selectByNetEquipAddr(netEquipAddr);
	}

	@Override
	public int insertSelect(OrgEquipment arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public List<OrgEquipment> selectDeviceByAppKey(OrgEquipment orgEquipment){
		return orgEquipmentMapper.selectDeviceByAppKey(orgEquipment);
	}
	
	@Override
	public OrgEquipmentExpand selectByEquipId(String equipId) {
		return orgEquipmentMapper.selectByEquipId(equipId);
	}
}
