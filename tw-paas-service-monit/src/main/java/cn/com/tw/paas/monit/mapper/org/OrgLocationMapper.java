package cn.com.tw.paas.monit.mapper.org;

import java.util.List;
import java.util.Map;

import cn.com.tw.paas.monit.entity.db.org.OrgLocation;
import cn.com.tw.paas.monit.entity.db.org.OrgResident;

public interface OrgLocationMapper {
    int deleteByPrimaryKey(String locationId);

    int insert(OrgLocation record);

    int insertSelective(OrgLocation record);

    OrgLocation selectByPrimaryKey(String locationId);

    int updateByPrimaryKeySelective(OrgLocation record);

    int updateByPrimaryKey(OrgLocation record);

	List<OrgResident> selectByEntity(String areaId);

	List<OrgResident> selectHouseNumByAroeId(String areaId);

	List<Map<String, String>> selectHouseListByLikeForApp(OrgLocation orgLocation);
}