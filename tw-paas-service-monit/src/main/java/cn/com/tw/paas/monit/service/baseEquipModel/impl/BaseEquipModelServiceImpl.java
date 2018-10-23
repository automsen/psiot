package cn.com.tw.paas.monit.service.baseEquipModel.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.common.utils.cons.code.MonitResultCode;
import cn.com.tw.paas.monit.entity.db.baseEquipModel.BaseEquipModel;
import cn.com.tw.paas.monit.entity.db.org.NetEquip;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.mapper.baseEquipModel.BaseEquipModelMapper;
import cn.com.tw.paas.monit.mapper.org.NetEquipMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.service.baseEquipModel.BaseEquipModelService;

@Service
public class BaseEquipModelServiceImpl implements BaseEquipModelService{
	
	@Autowired
	private BaseEquipModelMapper baseEquipModelMapper;
	@Autowired
	private TerminalEquipMapper terminalEquipMapper;
	@Autowired
	private NetEquipMapper netEquipMapper;

	
	@Override
	public int insert(BaseEquipModel paramT) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(BaseEquipModel paramT) {
		paramT.setModelId(CommUtils.getUuid());
		paramT.setCreateTime(new Date(System.currentTimeMillis()));
		/**
		 * 型号名称判重
		 */
		BaseEquipModel baseEquipModel = baseEquipModelMapper.selectByModelName(paramT.getModelName());
		if(!StringUtils.isEmpty(baseEquipModel)){
			throw new BusinessException(MonitResultCode.PARAM_EXIST_ERROR, "型号已存在");
		}
		
		return baseEquipModelMapper.insertSelective(paramT);
	}

	@Override
	public int deleteById(String modelId) {
		TerminalEquip terminalEquip = new TerminalEquip();
		terminalEquip.setModelId(modelId);
		List<TerminalEquip> terminalEquips = terminalEquipMapper.selectTerminalAll(terminalEquip);
		/**
		 * 校验型号是否被终端使用
		 */
		if(terminalEquips != null && terminalEquips.size() > 0){
			throw new BusinessException(MonitResultCode.CAN_NOT_DELETE_ERROR,"该型号已有终端使用");
		}
		NetEquip netEquip = new NetEquip();
		netEquip.setModelId(modelId);
		List<NetEquip> netEquips = netEquipMapper.selectByEntity(netEquip);
		/**
		 * 校验型号是否被网关使用
		 */
		if(netEquips != null && netEquips.size() > 0){
			throw new BusinessException(MonitResultCode.CAN_NOT_DELETE_ERROR,"该型号已有网关使用");
		}
		
		baseEquipModelMapper.deleteByPrimaryKey(modelId);
		return 0;
	}

	@Override
	public BaseEquipModel selectById(String modelId) {
		return baseEquipModelMapper.selectByPrimaryKey(modelId);
	}

	@Override
	public int updateSelect(BaseEquipModel paramT) {
		/**
		 * 型号名判重
		 */
		BaseEquipModel baseEquipModel = baseEquipModelMapper.selectByPrimaryKey(paramT.getModelId());
		if(!baseEquipModel.getModelName().equals(paramT.getModelName())){
			BaseEquipModel baseEquipModel1 = baseEquipModelMapper.selectByModelName(paramT.getModelName());
			if(!StringUtils.isEmpty(baseEquipModel1)){
				throw new BusinessException(MonitResultCode.PARAM_EXIST_ERROR, "型号已存在");
			}
		}
		
		
		return baseEquipModelMapper.updateByPrimaryKeySelective(paramT);
	}

	@Override
	public int update(BaseEquipModel paramT) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<BaseEquipModel> selectByPage(Page paramPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseEquipModel> selectBaseEquipModelPage(Page page) {
		List<BaseEquipModel> baseEquipModels = baseEquipModelMapper.selectBaseEquipModelPage(page);
		return baseEquipModels;
	}

	@Override
	public List<BaseEquipModel> selectBaseEquipModelAll(BaseEquipModel baseEquipModel) {
		List<BaseEquipModel> baseEquipModels = baseEquipModelMapper.selectBaseEquipModelAll(baseEquipModel);
		return baseEquipModels;
	}

	@Override
	public BaseEquipModel selectByEquipNumber(String equipNumber) {
		
		return baseEquipModelMapper.selectByEquipNumber(equipNumber);
	}

	@Override
	public BaseEquipModel selectByModelName(String modelName) {
		return baseEquipModelMapper.selectByModelName(modelName);
	}

}
