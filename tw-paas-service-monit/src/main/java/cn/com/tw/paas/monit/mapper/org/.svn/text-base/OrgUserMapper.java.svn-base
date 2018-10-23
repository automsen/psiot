package cn.com.tw.paas.monit.mapper.org;

import cn.com.tw.paas.monit.entity.db.org.OrgUser;

public interface OrgUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(OrgUser record);

    int insertSelective(OrgUser record);

    OrgUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(OrgUser record);

    int updateByPrimaryKey(OrgUser record);

	void updateByOrgId(OrgUser orgUser);

	void deleteByOrgId(String orgId);
}