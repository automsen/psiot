package cn.com.tw.paas.monit.service.equipment;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.orgEquipment.OrgEquipmentExpand;
import cn.com.tw.paas.monit.entity.db.orgEquipment.OrgEquipment;

public interface OrgEquipmentService extends IBaseSerivce<OrgEquipment>{

	List<OrgEquipmentExpand> selceTotalDevice(OrgEquipment orgEquipment);

	List<OrgEquipmentExpand> selectOrgEquipmentPage(Page page);

	List<OrgEquipmentExpand> selectOrgEquipmentAll(OrgEquipment orgEquipment);

	OrgEquipment selectOrgEquipmentById(OrgEquipment orgEquipment1);

	OrgEquipment selectByCommAddr(String commAddr);

	List<OrgEquipment> selectByNetEquipAddr(String netEquipAddr);

	int insertOrgEquipmentExpand(OrgEquipmentExpand paramT);
	
	List<OrgEquipment> selectDeviceByAppKey(OrgEquipment orgEquipment);

	OrgEquipmentExpand selectByEquipId(String equipId);

}
