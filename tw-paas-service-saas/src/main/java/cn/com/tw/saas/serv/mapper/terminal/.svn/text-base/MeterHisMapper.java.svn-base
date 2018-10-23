/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.terminal;

import java.util.List;

import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.MeterHis;
import cn.com.tw.saas.serv.service.terminal.MeterHisService;

public interface MeterHisMapper {
    int deleteByPrimaryKey(String id);

    int insert(Meter record);

    MeterHis selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MeterHis record);

	List<Meter> selectByEntity(Meter meterParam);

	MeterHis selectByEntity1(MeterHis meterHis1);

	List<MeterHis> selectByEntity2(MeterHis meterHis2);

	List<MeterHis> selectPriceByRoomId(String roomId);

	
}