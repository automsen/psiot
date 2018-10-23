package cn.com.tw.saas.serv.service.terminal;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.excel.MeterExcel;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.MeterExtend;

public interface MeterService{

	List<Meter> selectByRoomId(String roomId);
	
	List<Meter> selectAllInfoByRoomId(String roomId);
	
	List<Meter> selectByEntity(Meter param);

	List<Meter> selectSaasMeter(Meter saasMeter);

	Meter selectByRoomIdAndMeterAddr(Meter saasMeter);

	/**
	 * 模糊查询
	 * @param saasMeter
	 * @return
	 */
//	List<Meter> selectByLikeEntity(Meter saasMeter);
//
//	List<Meter> selectCommunicationTest(String meterAddr, String page);
//
//	List<Meter> selectLoadMonitorByPage(Page page);
//
//	List<Meter> selectHbByPage(Page page);

	int deleteById(String arg0);

	String insert(Meter arg0);

	Meter selectById(String arg0);

	List<Meter> selectByPage(Page arg0);

	int updateSelect(Meter arg0);

	Meter selectOldMeter(String newMeter, String elecMeter, String waterMeter);

	Meter selectByMeterAddr(String meterAddr);

	/**
	 * 查询客户使用中的仪表
	 * @param param
	 * @return
	 */
	List<Meter> selectByCustomer(Customer param);

	List<MeterExtend> selectByCustomerForWechat(Customer param);

	MeterExtend selectByAddrForWechat(Meter param);

	List<Meter> selectCommunicationTest(String meterAddr, String page);

	List<Meter> selectTerminalNumber(String userId);

	List<Meter> selectTerminalNetType(String userId);
	
	List<Meter> selectPriceByRoomId(String roomId);
	
	Meter selectRoomMeterByMeterAddr(Meter saasMeter);

	Meter selectInfoByMeterAddr(String meterAddr);

}
