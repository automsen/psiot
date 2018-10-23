package cn.com.tw.saas.serv.service.terminal;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.excel.MeterExcel;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.SaasMeter;

public interface SaasMeterService extends IBaseSerivce<SaasMeter>{

	List<SaasMeter> selectByRoomId(String roomId);
	
	String importInsert(MeterExcel saasMeterExcel);
	
	 SaasMeter selectByMeterAddr(String meterAddr);

	List<SaasMeter> selectSaasMeter(SaasMeter saasMeter);

	SaasMeter selectByRoomIdAndMeterAddr(SaasMeter saasMeter);

	/**
	 * 模糊查询
	 * @param saasMeter
	 * @return
	 */
	List<SaasMeter> selectByLikeEntity(SaasMeter saasMeter);

	List<SaasMeter> selectCommunicationTest(String meterAddr, String page);

	List<SaasMeter> selectLoadMonitorByPage(Page page);

	List<Meter> selectHbByPage(Page page);

}
