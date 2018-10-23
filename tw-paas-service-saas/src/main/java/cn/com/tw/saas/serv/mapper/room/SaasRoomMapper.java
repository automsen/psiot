package cn.com.tw.saas.serv.mapper.room;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.SaasRoom;
import cn.com.tw.saas.serv.entity.room.SaasRoomMeter;

@Deprecated
public interface SaasRoomMapper {
    int deleteByPrimaryKey(String id);

    int insert(SaasRoom record);
    
    SaasRoom selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SaasRoom record);
    
    List<SaasRoom> selectByPage(Page arg0);

	List<SaasRoom> selectByEntity(SaasRoom saasRoom);

	List<SaasRoom> selectRoomMeterByregionId(String regionId);

	void updateByPrimaryKeySelective(SaasRoomMeter param);
}