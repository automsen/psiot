package cn.com.tw.saas.serv.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.saas.serv.entity.org.OrgPayConfig;
import cn.com.tw.saas.serv.mapper.org.OrgPayConfigMapper;
import cn.com.tw.saas.serv.service.org.OrgPayConfigService;

/**
 *  
 * @author liming
 * 2018年8月1日 10:41:41
 */
@Service
public class OrgPayConfigServiceImpl implements OrgPayConfigService{
	
	@Autowired
	private OrgPayConfigMapper orgPayConfigMapper;

	@Override
	public List<OrgPayConfig> selectByEntity(OrgPayConfig orgPayConfig) {
		return orgPayConfigMapper.selectByEntity(orgPayConfig);
	}
}
