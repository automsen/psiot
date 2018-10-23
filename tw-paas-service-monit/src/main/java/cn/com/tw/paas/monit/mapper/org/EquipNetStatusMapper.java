package cn.com.tw.paas.monit.mapper.org;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.EquipNetStatusExpand;
import cn.com.tw.paas.monit.entity.db.org.EquipNetStatus;

public interface EquipNetStatusMapper {
    int deleteByPrimaryKey(String id);
    
    int deleteByEquipAddr(String equipAddr);

    int insert(EquipNetStatus record);
    
    int replace(EquipNetStatus record);
    
    int insertSelective(EquipNetStatus record);

    EquipNetStatus selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(EquipNetStatus record);

    int updateByPrimaryKey(EquipNetStatus record);
 
	List<EquipNetStatusExpand> selectByPage(Page page);

	List<EquipNetStatusExpand> selectNetByPage(Page page);

	EquipNetStatus selectByCommAddr(String commAddr);
}