package cn.com.tw.saas.serv.service.org;

import java.util.List;

import cn.com.tw.saas.serv.entity.org.OrgPayConfig;

public interface OrgPayConfigService {
	
	List<OrgPayConfig> selectByEntity(OrgPayConfig orgPayConfig);
}
