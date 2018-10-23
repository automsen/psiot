package cn.com.tw.paas.monit.service.org;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.OrgApplicationExpand;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;

public interface OrgApplicationService extends IBaseSerivce<OrgApplication>{

	List<OrgApplicationExpand> selectOrgApplicationPage(Page page);

	List<OrgApplication> selectOrgApplicationAll(OrgApplication orgApplication);

	OrgApplicationExpand selectByppId(String appId);

	OrgApplication selectByAppKey(String appKey);

	OrgApplication selectByTerminalEquip(String equipNumber);

	OrgApplication selectByNetEquip(String equipNumber);

	List<Map<String, Object>> selectOrgAndApplicationByApp();

	List<OrgApplication> selectAll(OrgApplication orgApplication);

}
