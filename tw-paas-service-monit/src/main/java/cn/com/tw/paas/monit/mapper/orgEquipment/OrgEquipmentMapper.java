package cn.com.tw.paas.monit.mapper.orgEquipment;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.orgEquipment.OrgEquipmentExpand;
import cn.com.tw.paas.monit.entity.db.orgEquipment.OrgEquipment;

public interface OrgEquipmentMapper {
    int deleteByPrimaryKey(String equipId);

    int insert(OrgEquipment record);

    int insertSelective(OrgEquipment record);

    OrgEquipment selectByPrimaryKey(String equipId);

    int updateByPrimaryKeySelective(OrgEquipment record);

    int updateByPrimaryKey(OrgEquipment record);

	List<OrgEquipmentExpand> selceTotalDevice(OrgEquipment orgEquipment);

	List<OrgEquipmentExpand> selectOrgEquipmentPage(Page page);

	List<OrgEquipmentExpand> selectOrgEquipmentAll(OrgEquipment orgEquipment);

	OrgEquipmentExpand selectOrgEquipmentById(OrgEquipment orgEquipment);

	OrgEquipment selectByCommAddr(String commAddr);

	List<OrgEquipment> selectByNetEquipAddr(String netEquipAddr);
	
	List<OrgEquipment> selectDeviceByAppKey(OrgEquipment orgEquipment);
	
	List<OrgEquipment> selectForApi(OrgEquipment orgEquipment);
	
	OrgEquipmentExpand selectByEquipId(String equipId);

}