package cn.com.tw.saas.serv.mapper.org;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.org.OrgUser;

public interface OrgUserMapper {
    int deleteByPrimaryKey(String orgUserId);

    int insert(OrgUser record);

    int insertSelective(OrgUser record);

    OrgUser selectByPrimaryKey(String orgUserId);

    int updateByPrimaryKeySelective(OrgUser record);

    int updateByPrimaryKey(OrgUser record);
    
    List<OrgUser> selectByPage(Page page); 
    
    OrgUser selectByName(String userName);

	List<OrgUser> selectAll();

	List<OrgUser> selectByEntity(OrgUser orgUser);

	List<OrgUser> selectByOrgId(String orgId);

	OrgUser selectByUserName(String userName);
}