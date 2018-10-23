package cn.com.tw.paas.monit.service.org;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.paas.monit.entity.db.org.EquipGroup;

public interface EquipGroupService extends IBaseSerivce<EquipGroup>{

	EquipGroup selectByCommAddr(String commAddr);

	List<EquipGroup> selectAll(EquipGroup equipGroup);

	int insertSelect(List<EquipGroup> equipGroupList);

	int deleteMeterAndGatewayRelationship(EquipGroup equipGroup);

}
