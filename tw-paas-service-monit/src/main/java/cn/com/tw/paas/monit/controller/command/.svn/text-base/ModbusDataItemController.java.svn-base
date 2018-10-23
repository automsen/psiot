package cn.com.tw.paas.monit.controller.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.db.base.ModbusDataItem;
import cn.com.tw.paas.monit.service.command.ModbusDataItemService;

@RestController
@RequestMapping("modbus")
public class ModbusDataItemController {

	@Autowired
	private ModbusDataItemService modbusDataItemService;

	@RequestMapping("items")
	public Response<?>  selectItems(String equipmentNumber){
		List<ModbusDataItem> result = modbusDataItemService.selectByEquip(equipmentNumber);
		return Response.ok(result);
	}

}
