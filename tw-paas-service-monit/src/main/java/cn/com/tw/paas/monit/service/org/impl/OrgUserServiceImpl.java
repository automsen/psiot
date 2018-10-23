package cn.com.tw.paas.monit.service.org.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.paas.monit.entity.db.org.OrgUser;
import cn.com.tw.paas.monit.mapper.org.OrgUserMapper;
import cn.com.tw.paas.monit.service.org.OrgUserService;

@Service
public class OrgUserServiceImpl implements OrgUserService{
	
	@Autowired
	private OrgUserMapper orgUserMapper;
	
	@Override
	public OrgUser selectById(String id) {
		return orgUserMapper.selectByPrimaryKey(id);
	}

}
