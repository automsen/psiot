package cn.com.tw.paas.monit.service.command;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.base.ModbusDataItem;

public interface ModbusDataItemService {

	List<ModbusDataItem> selectByEquip(String equipmentNumber);

}
