package cn.com.tw.saas.serv.mapper.org;

import java.util.List;

import cn.com.tw.saas.serv.entity.org.OrgSmsConfig;

public interface OrgSmsConfigMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrgSmsConfig record);

    int insertSelective(OrgSmsConfig record);

    OrgSmsConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrgSmsConfig record);

    int updateByPrimaryKey(OrgSmsConfig record);
    
    List<OrgSmsConfig> selectByEntity(OrgSmsConfig record);
}