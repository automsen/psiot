package cn.com.tw.saas.serv.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.org.OrgRole;
import cn.com.tw.saas.serv.mapper.org.OrgRoleMapper;
import cn.com.tw.saas.serv.service.org.OrgRoleService;
@Service
public class OrgRoleServiceImpl implements OrgRoleService{

	@Autowired
	private OrgRoleMapper roleMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return roleMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public int insert(OrgRole arg0) {
		// TODO Auto-generated method stub
		return roleMapper.insert(arg0);
	}

	@Override
	public int insertSelect(OrgRole arg0) {
		// TODO Auto-generated method stub
		return roleMapper.insert(arg0);
	}

	@Override
	public OrgRole selectById(String arg0) {
		// TODO Auto-generated method stub
		return roleMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<OrgRole> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(OrgRole arg0) {
		// TODO Auto-generated method stub
		return roleMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public int updateSelect(OrgRole arg0) {
		// TODO Auto-generated method stub
		return roleMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public List<OrgRole> selectRoleAll() {
		return roleMapper.selectAll();
	}

}
