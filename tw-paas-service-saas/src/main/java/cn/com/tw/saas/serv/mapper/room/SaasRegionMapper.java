package cn.com.tw.saas.serv.mapper.room;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.SaasRegion;
public interface SaasRegionMapper {
    int deleteByPrimaryKey(String roomId);

    int insert(SaasRegion record);

    SaasRegion selectByPrimaryKey(String roomId);

    int updateByPrimaryKeySelective(SaasRegion record);
    
    List<SaasRegion> selectByPage(Page page);
    
    List<SaasRegion> selectByMap(Map<String, Object> map);
    
    String selectMaxregionNo(Map<String,Object> queryMap);

	List<SaasRegion> selectSaasRegion(SaasRegion saasRegion);

	List<SaasRegion> selectByEntity(SaasRegion param);

	List<SaasRegion> selectSubRegion(SaasRegion saasRegion);
	
}