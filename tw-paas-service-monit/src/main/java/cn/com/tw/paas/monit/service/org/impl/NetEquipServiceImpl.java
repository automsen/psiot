package cn.com.tw.paas.monit.service.org.impl;

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
import cn.com.tw.paas.monit.entity.business.excel.NetEquipExcel;
import cn.com.tw.paas.monit.entity.business.org.NetEquipExpand;
import cn.com.tw.paas.monit.entity.db.baseEquipModel.BaseEquipModel;
import cn.com.tw.paas.monit.entity.db.org.EquipNetStatus;
import cn.com.tw.paas.monit.entity.db.org.NetEquip;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.mapper.baseEquipModel.BaseEquipModelMapper;
import cn.com.tw.paas.monit.mapper.org.EquipNetStatusMapper;
import cn.com.tw.paas.monit.mapper.org.NetEquipMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.service.org.NetEquipService;

@Service
public class NetEquipServiceImpl implements NetEquipService{

	@Autowired
	private NetEquipMapper netEquipMapper;
	@Autowired
	private EquipNetStatusMapper equipNetStatusMapper;
	@Autowired
	private TerminalEquipMapper terminalEquipMapper;
	@Autowired
	private BaseEquipModelMapper baseEquipModelMapper;
	

	public int deleteById(String equipId) {
		NetEquip netEquip = netEquipMapper.selectByPrimaryKey(equipId);
		TerminalEquip terminalEquip = new TerminalEquip();
		terminalEquip.setEquipNumber(netEquip.getEquipNumber());
		List<TerminalEquip> terminalEquips = terminalEquipMapper.selectTerminalAll(terminalEquip);
		/**
		 * 检验网关是否被终端使用
		 */
		if(terminalEquips != null && terminalEquips.size() > 0){
			throw new BusinessException(MonitResultCode.CAN_NOT_DELETE_ERROR,"该网关已有终端使用");
		}
				
		netEquipMapper.deleteByPrimaryKey(equipId);
		return 0;
	}

	public int insert(NetEquip arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int insertSelect(NetEquip arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public NetEquip selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<NetEquip> selectByPage(Page page) {
		List<NetEquip> netEquips = netEquipMapper.selectByPage(page);
		return netEquips;
	}

	public int update(NetEquip arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int updateSelect(NetEquip netEquip) {
		/**
		 * 通讯地址唯一校验
		 */
		NetEquip netEquip1 = netEquipMapper.selectByPrimaryKey(netEquip.getEquipId());
		if(!netEquip.getEquipNumber().equals(netEquip1.getEquipNumber())){
			NetEquip netEquip2 = netEquipMapper.selectByEquipNumber(netEquip.getEquipNumber());
			if(!StringUtils.isEmpty(netEquip2)){
				throw new BusinessException(MonitResultCode.PARAM_EXIST_ERROR, "设备编号重复！");
			}
		}
		
		BaseEquipModel baseEquipModel = baseEquipModelMapper.selectByPrimaryKey(netEquip.getModelId());
		if(StringUtils.isEmpty(baseEquipModel)){
			throw new BusinessException(MonitResultCode.PARAMETER_NULL_ERROR, "型号不存在！");
		}
		netEquip.setNetTypeCode(baseEquipModel.getNetType());
		
		netEquipMapper.updateByPrimaryKeySelective(netEquip);
		return 0;
	}

	@Override
	public List<NetEquipExpand> selectEquipByPage(Page page) {
		List<NetEquipExpand> equipExpands = netEquipMapper.selectEquipByPage(page);
		return equipExpands;
	}

	@Override
	public List<NetEquipExpand> selectNetEquipExpandAll(NetEquipExpand equipExpand) {
		List<NetEquipExpand> equipExpands = netEquipMapper.selectNetEquipExpandAll(equipExpand);
		return equipExpands;
	}

	@Override
	@Transactional
	public void insertNetEquipExpand(NetEquipExpand equipExpand) {
		
		
		/**
		 * 通讯地址唯一校验
		 */
		NetEquipExpand equipExpand1 = netEquipMapper.selectByEquipNumber(equipExpand.getEquipNumber());
		if(!StringUtils.isEmpty(equipExpand1)){
			throw new BusinessException(MonitResultCode.PARAM_EXIST_ERROR, "设备编号重复！");
		}
		
		BaseEquipModel baseEquipModel = baseEquipModelMapper.selectByPrimaryKey(equipExpand.getModelId());
		if(StringUtils.isEmpty(baseEquipModel)){
			throw new BusinessException(MonitResultCode.PARAMETER_NULL_ERROR, "型号不存在！");
		}
		equipExpand.setNetTypeCode(baseEquipModel.getNetType());
		/**
		 * 集抄设备写入
		 */
		equipExpand.setEquipId(CommUtils.getUuid());
		equipExpand.setCreateTime(new Date(System.currentTimeMillis()));
		netEquipMapper.insertSelective(equipExpand);
		/**
		 * 设备状态添加 初始状态为2
		 */
		EquipNetStatus equipNetStatus = new EquipNetStatus();
		equipNetStatus.setId(CommUtils.getUuid());
		equipNetStatus.setCommAddr(equipExpand.getEquipNumber());
		equipNetStatus.setNetStatus((byte)2);
		equipNetStatusMapper.insertSelective(equipNetStatus);
	}

	@Override
	public NetEquipExpand selectByEquipId(String equipId) {
		return netEquipMapper.selectByEquipId(equipId);
	}

	@Override
	public List<NetEquip> selectByEntity(NetEquip queryOrg) {
		List<NetEquip> netEquips = netEquipMapper.selectByEntity(queryOrg);
		return netEquips;
	}

	@Override
	public List<NetEquip> selectNetForApi(NetEquip param) {
		return netEquipMapper.selectNetForApi(param);
	}

	@Override
	@Transactional
	public void insertNetEquipExcel(NetEquipExcel orgGatewayExcel) {
		NetEquip netEquip = new NetEquip();
		netEquip.setEquipId(CommUtils.getUuid());//主键ID
		netEquip.setEquipCategCode(orgGatewayExcel.getEquipCategCode());//设备一级分类
		netEquip.setEquipTypeCode(orgGatewayExcel.getEquipTypeCode());//二级分类
		netEquip.setNetTypeCode(orgGatewayExcel.getNetTypeCode());//上行网络类型
		netEquip.setEquipNumber(orgGatewayExcel.getEquipNumber());// 设备编号
		netEquip.setModelId(orgGatewayExcel.getModelId());//型号ID
		// 通信密码
		if(StringUtils.isEmpty(orgGatewayExcel.getCommPwd())){
			netEquip.setCommPwd("");
		}else{
			netEquip.setCommPwd(orgGatewayExcel.getCommPwd());
		}
		//设备名称
		if(StringUtils.isEmpty(orgGatewayExcel.getEquipName())){
			netEquip.setEquipName("");
		}else{
			netEquip.setEquipName(orgGatewayExcel.getEquipName());
		}
		//集抄设备下联设备分类
		if(StringUtils.isEmpty(orgGatewayExcel.getChildEquipTypeCode())){
			netEquip.setChildEquipTypeCode("");
		}else{
			netEquip.setChildEquipTypeCode(orgGatewayExcel.getChildEquipTypeCode());
		}
		//网络编号
		if(StringUtils.isEmpty(orgGatewayExcel.getNetNumber())){
			netEquip.setNetNumber("");
		}else{
			netEquip.setNetNumber(orgGatewayExcel.getNetNumber());
		}
		
		/**
		 * 设备状态添加 初始状态为2
		 */
		EquipNetStatus equipNetStatus = new EquipNetStatus();
		equipNetStatus.setId(CommUtils.getUuid());
		equipNetStatus.setCommAddr(orgGatewayExcel.getEquipNumber());
		equipNetStatus.setNetStatus((byte)2);
		equipNetStatusMapper.insertSelective(equipNetStatus);
		
		netEquipMapper.insertSelective(netEquip);
	}

	@Override
	public NetEquipExpand selectByEquipNumber(String equipNumber) {
		return netEquipMapper.selectByEquipNumber(equipNumber);
	}

	@Override
	public List<NetEquip> selectLikeEquipNumber(String equipNumber) {
		// TODO Auto-generated method stub
		return netEquipMapper.selectLikeEquipNumber(equipNumber);
	}
	

}
