package cn.com.tw.saas.serv.service.terminal;

import java.util.List;

import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.MeterHis;


public interface MeterHisService {
	
	MeterHis selectByEntity1(MeterHis meterHis1);

	List<MeterHis> selectByEntity2(MeterHis meterHis2);

	List<MeterHis> selectPriceByRoomId(String roomId);
	
}
