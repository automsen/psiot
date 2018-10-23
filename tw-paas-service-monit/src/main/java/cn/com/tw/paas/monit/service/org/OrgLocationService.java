package cn.com.tw.paas.monit.service.org;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.paas.monit.entity.db.org.OrgLocation;
import cn.com.tw.paas.monit.entity.db.org.OrgResident;

public interface OrgLocationService extends IBaseSerivce<OrgLocation> {

	List<OrgResident> selectHouseNumByAroeId(String areaId);

	List<Map<String, String>> selectHouseListByLikeForApp(
			OrgLocation orgLocation);

}
