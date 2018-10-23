package cn.com.tw.saas.serv.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.org.OrgUserRole;
import cn.com.tw.saas.serv.mapper.org.OrgUserRoleMapper;
import cn.com.tw.saas.serv.service.org.OrgUserRoleService;

@Service
public class OrgUserRoleServiceImpl implements OrgUserRoleService{

	@Autowired
	private OrgUserRoleMapper userRoleMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return userRoleMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public int insert(OrgUserRole arg0) {
		// TODO Auto-generated method stub
		return userRoleMapper.insert(arg0);
	}

	@Override
	public int insertSelect(OrgUserRole arg0) {
		// TODO Auto-generated method stub
		return userRoleMapper.insert(arg0);
	}

	@Override
	public OrgUserRole selectById(String arg0) {
		// TODO Auto-generated method stub
		return userRoleMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<OrgUserRole> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(OrgUserRole arg0) {
		// TODO Auto-generated method stub
		return userRoleMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public int updateSelect(OrgUserRole arg0) {
		// TODO Auto-generated method stub
		return userRoleMapper.updateByPrimaryKeySelective(arg0);
	}

}
