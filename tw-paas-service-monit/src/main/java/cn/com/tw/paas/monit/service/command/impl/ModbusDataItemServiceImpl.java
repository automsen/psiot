package cn.com.tw.paas.monit.service.command.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.paas.monit.entity.db.base.ModbusDataItem;
import cn.com.tw.paas.monit.entity.db.baseEquipModel.BaseEquipModel;
import cn.com.tw.paas.monit.mapper.base.ModbusDataItemMapper;
import cn.com.tw.paas.monit.mapper.baseEquipModel.BaseEquipModelMapper;
import cn.com.tw.paas.monit.service.command.ModbusDataItemService;
@Service
public class ModbusDataItemServiceImpl implements ModbusDataItemService{
	@Autowired
	private ModbusDataItemMapper modbusDataItemMapper;
	@Autowired
	private BaseEquipModelMapper modelMapper;
	
	@Override
	public List<ModbusDataItem> selectByEquip(String equipmentNumber) {
		// 查询厂家
		BaseEquipModel model = modelMapper.selectByEquipNumber(equipmentNumber);
		List<ModbusDataItem> dataItemList = new ArrayList<ModbusDataItem>();
		if (null==model){
			return dataItemList;
		}
		ModbusDataItem param = new ModbusDataItem();
		param.setManufacturerCode(model.getFactory());
		dataItemList = modbusDataItemMapper.selectByEntity(param);
		return dataItemList;
	}
}
