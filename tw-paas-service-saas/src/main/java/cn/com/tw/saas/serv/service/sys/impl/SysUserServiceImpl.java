package cn.com.tw.saas.serv.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.sys.SysUser;
import cn.com.tw.saas.serv.mapper.sys.SysUserMapper;
import cn.com.tw.saas.serv.service.sys.SysUserService;

@Service
public class SysUserServiceImpl implements SysUserService{

	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(SysUser arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(SysUser arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SysUser selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysUser> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(SysUser arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(SysUser arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SysUser selectNumber(String userId) {
		SysUser sysUser = sysUserMapper.selectNumber(userId);
		return sysUser;
	}

	@Override
	public SysUser selectByName(String name) {
		return sysUserMapper.selectByName(name);
	}

	@Override
	public List<SysUser> selectByEntity(SysUser sysUser) {
		return sysUserMapper.selectByEntity(sysUser);
	}

}
