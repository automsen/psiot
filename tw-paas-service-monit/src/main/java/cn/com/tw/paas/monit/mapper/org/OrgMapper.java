package cn.com.tw.paas.monit.mapper.org;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.OrgExpand;
import cn.com.tw.paas.monit.entity.db.org.Org;

public interface OrgMapper {
	int deleteByPrimaryKey(String orgId);

    int insert(Org record);

    int insertSelective(Org record);

    Org selectByPrimaryKey(String orgId);

    int updateByPrimaryKeySelective(Org record);

    int updateByPrimaryKey(Org record);

	List<Org> selectByPage(Page page);

	List<OrgExpand> selectOrgAll(OrgExpand orgExpand);

	List<OrgExpand> selectOrgExpandByPage(Page page);

	OrgExpand selectByOrgId(String orgId);

	List<OrgExpand> selectByOrgName(@Param("orgName") String orgName);

	OrgExpand selectOrgCode(OrgExpand orgExpand);

	OrgExpand selectOrgNumber();

}