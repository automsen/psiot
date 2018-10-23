package cn.com.tw.paas.monit.mapper.org;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.OrgApplicationExpand;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;

public interface OrgApplicationMapper {
    int deleteByPrimaryKey(String appId);

    int insert(OrgApplication record);

    int insertSelective(OrgApplication record);

    OrgApplicationExpand selectByPrimaryKey(String appId);

    int updateByPrimaryKeySelective(OrgApplication record);

    int updateByPrimaryKey(OrgApplication record);

	List<OrgApplicationExpand> selectOrgApplicationByPage(Page page);

	List<OrgApplication> selectOrgApplicationAll(OrgApplication orgApplication);
	
	OrgApplication selectByAppKey(String appKey);

	OrgApplication selectOrgApplicationByOrgId(String orgId);

	OrgApplication selectByTerminalEquip(@Param("equipNumber") String equipNumber);

	OrgApplication selectByNetEquip(@Param("equipNumber") String equipNumber);
	
	int selectAppMaxIdByOrgId(String orgId);

	List<OrgApplication> selectAll(OrgApplication orgApplication);

}