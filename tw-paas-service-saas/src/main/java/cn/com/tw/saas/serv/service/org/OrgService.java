package cn.com.tw.saas.serv.service.org;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.org.Org;

public interface OrgService extends IBaseSerivce<Org>{

	Org selectByUserId(String userId);

}
