/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.terminal;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.MeterExtend;
import cn.com.tw.saas.serv.entity.terminal.SaasMeter;

public interface MeterMapper {
    int deleteByPrimaryKey(String meterId);

    int insert(Meter record);

    Meter selectByPrimaryKey(String meterId);

    int updateByPrimaryKeySelective(Meter record);
    
    int updateForBalance(Meter record);
    
    List<Meter> selectByPage(Page page);
    
    List<Meter> selectByEntity(Meter record);

	List<Meter> selectByRoomId(String roomId);
	
	List<Meter> selectAllInfoByRoomId(String roomId);
	
	List<Meter> selectSaasMeter(Meter saasMeter);

	Meter selectByMeterAddr(String meterAddr);

	List<Meter> selectHbByPage(Page page);

	List<Meter> selectByCustomer(Customer param);
	
	List<MeterExtend> selectByCustomerForWechat(Customer param);
	
	MeterExtend selectByAddrForWechat(Meter record);
	
	List<Meter> selectLoadMonitorByPage(Page page);
	
	void updateByMeterAddr(Meter meter);
	
	List<Meter> selectCommunicationTest(@Param("meterAddr") String meterAddr,@Param("page") int page);
	
	List<Meter> selectByLikeEntity(Meter meter);

	void clearingUpdate(Meter meter);

	List<Meter> selectTerminalNumber(String userId);

	List<Meter> selectTerminalNetType(@Param("userId") String userId);

	List<Meter> selectPriceByRoomId(String roomId);

	void updateToAlarmOrPrice(Meter meter);

	Meter selectRoomMeterByMeterAddr(Meter saasMeter);

	List<Meter> selectMeterByOrg(Page page);

	Meter selectInfoByMeterAddr(String meterAddr);
	
}