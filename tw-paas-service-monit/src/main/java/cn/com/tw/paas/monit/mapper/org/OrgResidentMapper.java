package cn.com.tw.paas.monit.mapper.org;

import java.util.List;
import java.util.Map;

import cn.com.tw.paas.monit.entity.db.org.OrgResident;

public interface OrgResidentMapper {
    int deleteByPrimaryKey(String residentId);

    int insert(OrgResident record);

    int insertSelective(OrgResident record);

    OrgResident selectByPrimaryKey(String residentId);

    int updateByPrimaryKeySelective(OrgResident record);

    int updateByPrimaryKey(OrgResident record);

	List<OrgResident> selectResidentByHouseNum(OrgResident resident);

	List<OrgResident> selectResidentByEntity(OrgResident resident);

	List<OrgResident> selectHouseListByLike(OrgResident resident);

	List<Map<String, String>> selectHouseListByLikeForApp(OrgResident resident);
}