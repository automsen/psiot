package cn.com.tw.paas.monit.service.org;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.OrgExpand;
import cn.com.tw.paas.monit.entity.db.org.Org;

public interface OrgService extends IBaseSerivce<Org>{

	List<OrgExpand> selectOrgAll(OrgExpand orgExpand);

	void insertOrgAndOrgUser(OrgExpand orgExpand);

	void updateOrgAndOrgUser(OrgExpand orgExpand);

	List<OrgExpand> selectOrgExpandByPage(Page page);

	OrgExpand selectByOrgId(String orgId);

}
