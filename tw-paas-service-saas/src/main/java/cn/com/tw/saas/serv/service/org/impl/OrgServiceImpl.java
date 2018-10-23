package cn.com.tw.saas.serv.service.org.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.org.Org;
import cn.com.tw.saas.serv.entity.org.OrgRole;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.org.OrgUserRole;
import cn.com.tw.saas.serv.mapper.org.OrgMapper;
import cn.com.tw.saas.serv.mapper.org.OrgRoleMapper;
import cn.com.tw.saas.serv.mapper.org.OrgUserMapper;
import cn.com.tw.saas.serv.mapper.org.OrgUserRoleMapper;
import cn.com.tw.saas.serv.service.org.OrgService;

@Service
public class OrgServiceImpl implements OrgService{
	
	@Autowired
	private OrgMapper orgMapper;
	@Autowired
	private OrgUserMapper orgUserMapper;
	@Autowired
	private OrgRoleMapper orgRoleMapper;
	@Autowired
	private OrgUserRoleMapper orgUserRoleMapper;

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public int insert(Org org) {
		
		//登录用户名校验
		OrgUser orgUser1 = orgUserMapper.selectByUserName(org.getUserName());
		if(!StringUtils.isEmpty(orgUser1)){
			throw new BusinessException("111111","用户名已存在!!");
		}
		
		//查出机构code最大的机构
		Org org1 = orgMapper.selectMaxOrgCode();
		String orgCode = org1.getOrgCode();
		/**
		 * 1.用的代理商id生成机构id  机构code  代理商ID：101  机构Id：10110001  机构code：10001
		 * 2.将传入的机构插入
		 */
		//生成新的机构code
		int newCode = Integer.parseInt(orgCode) + 1;
		//新的机构ID
		String orgId = org.getManaUser() + newCode;
		Org newOrg = new Org();
		newOrg.setOrgId(orgId);
		newOrg.setOrgName(org.getOrgName());
		newOrg.setOrgCode(String.valueOf(newCode));
		newOrg.setBalance(BigDecimal.ZERO);
		orgMapper.insert(newOrg);
		
		/**
		 * 3.同时插入一条 机构管理用户
		 */
		OrgUser orgUser = new OrgUser();
		String userId = newCode+"01";
		orgUser.setOrgId(orgId);
		orgUser.setUserName(org.getUserName());
		orgUser.setUserId(userId);
		orgUser.setUserRole("admin");
		orgUser.setPassword(DigestUtils.md5Hex(org.getPassword().getBytes()));
		orgUser.setMobile(org.getMobile());
		orgUser.setEmail(org.getEmail());
		orgUser.setUserRealName(org.getUserRealName());
		orgUser.setIsVerification((byte)0);
		orgUserMapper.insertSelective(orgUser);
		
		/**
		 * 4.插入用户角色 为admin
		 */
		List<OrgRole> orgRoles = orgRoleMapper.selectAll();
		for (OrgRole orgRole : orgRoles) {
			OrgUserRole orgUserRole = new OrgUserRole();
			orgUserRole.setRoleId(orgRole.getRoleId());
			orgUserRole.setRoleCode(orgRole.getRoleCode());
			orgUserRole.setOrgId(orgId);
			orgUserRole.setUserId(userId);
			orgUserRoleMapper.insert(orgUserRole);
		}
		
		OrgUserRole orgUserRole = new OrgUserRole();
		orgUserRole.setRoleId("");
		orgUserRole.setRoleCode("admin");
		orgUserRole.setOrgId(orgId);
		orgUserRole.setUserId(userId);
		orgUserRoleMapper.insert(orgUserRole);
		
		return 0;
	}

	@Override
	public int insertSelect(Org arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Org selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Org> selectByPage(Page page) {
		
		return orgMapper.selectByPage(page);
	}

	@Override
	@Transactional
	public int update(Org org) {
		
		Org org1 = new Org();
		org1.setOrgId(org.getOrgId());
		org1.setOrgName(org.getOrgName());
		orgMapper.updateByPrimaryKeySelective(org1);
		
		OrgUser orgUser = new OrgUser();
		orgUser.setUserId(org.getUserId());
		orgUser.setUserName(org.getUserName());
		orgUser.setUserRealName(org.getUserRealName());
		orgUser.setEmail(org.getEmail());
		orgUser.setMobile(org.getMobile());
		if(!StringUtils.isEmpty(org.getPassword())){
			orgUser.setPassword(DigestUtils.md5Hex(org.getPassword().getBytes()));
		}
		orgUserMapper.updateByPrimaryKeySelective(orgUser);
		
		return 0;
	}

	@Override
	public int updateSelect(Org arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Org selectByUserId(String userId) {
		
		return orgMapper.selectByUserId(userId);
	}

}
