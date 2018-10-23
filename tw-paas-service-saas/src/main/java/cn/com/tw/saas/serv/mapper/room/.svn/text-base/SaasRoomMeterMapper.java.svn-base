package cn.com.tw.saas.serv.mapper.room;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.SaasRoomMeter;

public interface SaasRoomMeterMapper {
    int deleteByPrimaryKey(String id);

    int insert(SaasRoomMeter record);

    SaasRoomMeter selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SaasRoomMeter record);

    int updateByPrimaryKey(SaasRoomMeter record);
    
    List<SaasRoomMeter> selectByPage(Page page);
    
    List<SaasRoomMeter> selectByEntity(SaasRoomMeter param);

}