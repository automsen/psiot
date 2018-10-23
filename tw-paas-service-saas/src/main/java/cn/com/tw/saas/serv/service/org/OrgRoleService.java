package cn.com.tw.saas.serv.service.org;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.org.OrgRole;

public interface OrgRoleService extends IBaseSerivce<OrgRole>{

	List<OrgRole> selectRoleAll();

}
