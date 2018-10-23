package cn.com.tw.saas.serv.mapper.terminal;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.SaasMeter;

@Deprecated
public interface SaasMeterMapper {
    int deleteByPrimaryKey(String meterId);

    int insert(SaasMeter record);

    SaasMeter selectByPrimaryKey(String meterId);

    int updateByPrimaryKeySelective(SaasMeter record);

    int updateByPrimaryKey(SaasMeter record);
    
    List<SaasMeter> selectByPage(Page page);
    
    List<SaasMeter> selectByRoomId(String roomId);
    
    SaasMeter selectByMeterAddr(String meterAddr);

	List<SaasMeter> selectSaasMeter(SaasMeter saasMeter);

	List<SaasMeter> selectByEntity(SaasMeter saasMeter);

	/**
	 * 模糊查询
	 * @param saasMeter
	 * @return
	 */
	List<SaasMeter> selectByLikeEntity(SaasMeter saasMeter);

	List<SaasMeter> selectCommunicationTest(@Param("meterAddr") String meterAddr,@Param("page") int page);

	void updateByMeterAddr(SaasMeter saasMeter2);

	List<SaasMeter> selectLoadMonitorByPage(Page page);

	List<Meter> selectHbByPage(Page page);

}