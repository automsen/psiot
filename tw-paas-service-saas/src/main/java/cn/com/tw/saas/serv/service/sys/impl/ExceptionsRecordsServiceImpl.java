package cn.com.tw.saas.serv.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.sys.ExceptionsRecords;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.sys.ExceptionsRecordsMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.sys.ExceptionsRecordsService;

@Service
public class ExceptionsRecordsServiceImpl implements ExceptionsRecordsService{

	@Autowired
	private ExceptionsRecordsMapper exceptionsRecordsMapper;
	@Autowired
	private RoomMapper roomMapper;
	@Autowired
	private MeterMapper meterMapper;

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(ExceptionsRecords arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(ExceptionsRecords arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ExceptionsRecords selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExceptionsRecords> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(ExceptionsRecords arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(ExceptionsRecords arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void insertExceptionsRecords(JSONObject jsonMap) {
		// 仪表地址
		String meterAddr =  jsonMap.getString("equipNumber");
		// 能源类型
		String equipType =  jsonMap.getString("equipType");
		
		String dataType = String.valueOf(jsonMap.get("dataType"));
		
		if(dataType != "1600" && dataType != "1601" && dataType != "1602"){
			Meter meter = meterMapper.selectInfoByMeterAddr(meterAddr);
			
			ExceptionsRecords exceptionsRecords = new ExceptionsRecords();
			exceptionsRecords.setOrgId(meter.getOrgId());
			exceptionsRecords.setAccountId(meter.getAccountId());
			exceptionsRecords.setMeterAddr(meterAddr);
			exceptionsRecords.setEnergyType(equipType);
			exceptionsRecords.setNotifyBusType(dataType);
			exceptionsRecords.setRoomId(meter.getRoomId());
			exceptionsRecords.setRegionId(meter.getRegionId());
			exceptionsRecords.setRegionNumber(meter.getRoomNumber());
			exceptionsRecords.setRegionFullName(meter.getFullName());
			exceptionsRecords.setRoomName(meter.getRoomName());
			exceptionsRecords.setRoomNumber(meter.getRoomNumber());
			exceptionsRecords.setRoomUse(meter.getRoomUse());
			exceptionsRecords.setCustomerId(meter.getCustomerId());
			exceptionsRecords.setCustomerMobile1(meter.getCustomerMobile1());
			exceptionsRecords.setCustomerRealname(meter.getCustomerRealname());
			exceptionsRecords.setCustomerType(meter.getCustomerType());
			exceptionsRecords.setCustomerNo(meter.getCustomerNo());
			exceptionsRecordsMapper.insert(exceptionsRecords);
		}
		
	}

}
