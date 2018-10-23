package cn.com.tw.saas.serv.mapper.org;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.org.Org;

public interface OrgMapper {
    int deleteByPrimaryKey(String orgId);

    int insert(Org record);

    int insertSelective(Org record);

    Org selectByPrimaryKey(String orgId);

    int updateByPrimaryKeySelective(Org record);

    int updateByPrimaryKey(Org record);
    
    List<Org> selectByAll();

	List<Org> selectByPage(Page page);

	Org selectMaxOrgCode();

	Org selectByUserId(String userId);
}