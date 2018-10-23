package cn.com.tw.saas.serv.service.terminal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.MeterHis;
import cn.com.tw.saas.serv.mapper.terminal.MeterHisMapper;
import cn.com.tw.saas.serv.service.terminal.MeterHisService;

@Service
public class MeterHisServiceImpl implements MeterHisService{

	@Autowired
	private MeterHisMapper meterHisMapper;

	@Override
	public MeterHis selectByEntity1(MeterHis meterHis1) {
		return meterHisMapper.selectByEntity1(meterHis1);
	}

	@Override
	public List<MeterHis> selectByEntity2(MeterHis meterHis2) {
		return meterHisMapper.selectByEntity2(meterHis2);
	}

	@Override
	public List<MeterHis> selectPriceByRoomId(String roomId) {
		return meterHisMapper.selectPriceByRoomId(roomId);
	}

	

	
	
	

	

}
