package cn.com.tw.saas.serv.service.org.impl;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.org.OrgUserRole;
import cn.com.tw.saas.serv.mapper.org.OrgUserMapper;
import cn.com.tw.saas.serv.mapper.org.OrgUserRoleMapper;
import cn.com.tw.saas.serv.service.org.OrgUserService;

@Service
public class OrgUserServiceImpl implements OrgUserService {

	@Autowired
	private OrgUserMapper saasOrgUserMapper;

	@Autowired
	private OrgUserRoleMapper orgUserRoleMapper;

	@Override
	@Transactional
	public int deleteById(String arg0) {
		/**
		 * admin 机构不能删除
		 */
		OrgUser orgUser = saasOrgUserMapper.selectByPrimaryKey(arg0);
		if("admin".equals(orgUser.getUserRole())){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该用户不能删除！");
		}
		
		return saasOrgUserMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public int insert(OrgUser arg0) {
		return saasOrgUserMapper.insert(arg0);
	}

	@Override
	@Transactional
	public int insertSelect(OrgUser arg0) {
		// TODO Auto-generated method stub

		/**
		 * 用户名判重
		 */
		OrgUser orgUser = new OrgUser();
		orgUser.setOrgId(arg0.getOrgId());
		orgUser.setUserName(arg0.getUserName());
		List<OrgUser> orgUsers = saasOrgUserMapper.selectByEntity(orgUser);
		if (orgUsers != null && orgUsers.size() > 0) {
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "用户名已存在");
		}
		
		List<OrgUser> users = saasOrgUserMapper.selectByOrgId(arg0.getOrgId());
		int y = 0;
		String userIda = "";
		for (OrgUser orgUser2 : users) {
			String userId = orgUser2.getUserId().substring(5,7);
			userIda = orgUser2.getUserId().substring(0,5);
			int i = Integer.parseInt(userId);
			if(i > y){
				y = i;
			}
		}
		
		String userId = "";
		if(y+1 > 9){
			userId = userIda+(y+1);
		}
		if(y+1 <= 9){
			userId = userIda+"0"+(y+1);
		}

		OrgUserRole orgUserRole = new OrgUserRole();
		orgUserRole.setUserId(userId);
		orgUserRole.setOrgId(arg0.getOrgId());
		/**
		 * 服务
		 */
		if (arg0.getServe() != null && arg0.getServe() != "") {
			orgUserRole.setRoleId(arg0.getServe());
			orgUserRole.setRoleCode("serve");
			orgUserRoleMapper.insert(orgUserRole);
		}
		/**
		 * 运维
		 */
		if (arg0.getMaint() != null && arg0.getMaint() != "") {
			orgUserRole.setRoleId(arg0.getMaint());
			orgUserRole.setRoleCode("maint");
			orgUserRoleMapper.insert(orgUserRole);
		}
		/**
		 * 管理
		 */
		if (arg0.getManage() != null && arg0.getManage() != "") {
			orgUserRole.setRoleId(arg0.getManage());
			orgUserRole.setRoleCode("manage");
			orgUserRoleMapper.insert(orgUserRole);
		}

		arg0.setUserId(userId);
		arg0.setPassword(DigestUtils.md5Hex("123456".getBytes()));
		return saasOrgUserMapper.insertSelective(arg0);
	}

	@Override
	public OrgUser selectById(String arg0) {
		// 将密码解密
		OrgUser orgUser = saasOrgUserMapper.selectByPrimaryKey(arg0);
		String password=orgUser.getPassword();
		/*char[]a=password.toCharArray();
		for(int i=0;i<a.length;i++){
			a[i]=(char)(a[i]^'t');
		}
		String k=new String(a);
		orgUser.setPassword(k);*/
		orgUser.setPassword(null);
		return  orgUser;
	}

	@Override
	public List<OrgUser> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return saasOrgUserMapper.selectByPage(arg0);
	}

	@Override
	public int update(OrgUser arg0) {
		// TODO Auto-generated method stub
		return saasOrgUserMapper.updateByPrimaryKey(arg0);
	}

	@Override
	@Transactional
	public int updateSelect(OrgUser arg0) {
		
		
		arg0.setPassword(DigestUtils.md5Hex(arg0.getPassword().getBytes()));
		
		/**
		 * admin用户不能修改
		 */
		OrgUser orgUser1 = saasOrgUserMapper.selectByPrimaryKey(arg0.getUserId());
		if("admin".equals(orgUser1.getUserRole())){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该用户不能修改！");
		}
		

		/**
		 * 用户名判重
		 */
		OrgUser orgUser = new OrgUser();
		orgUser.setOrgId(arg0.getOrgId());
		orgUser.setUserName(arg0.getUserName());
		/**
		 * 用户名发生变化 判断新用户名是否存在
		 */
		OrgUser orgUser2 = saasOrgUserMapper.selectByPrimaryKey(arg0.getUserId());
		if (!orgUser2.getUserName().equals(arg0.getUserName())) {
			List<OrgUser> orgUsers = saasOrgUserMapper.selectByEntity(orgUser);
			if (orgUsers != null && orgUsers.size() > 0) {
				throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "用户名已存在");
			}
		}

		OrgUserRole orgUserRole = new OrgUserRole();
		orgUserRole.setOrgId(arg0.getOrgId());
		orgUserRole.setUserId(arg0.getUserId());
		orgUserRoleMapper.deleteByUserId(arg0.getUserId());
		/**
		 * 服务
		 */
		if (arg0.getServe() != null && arg0.getServe() != "") {
			orgUserRole.setRoleId(arg0.getServe());
			orgUserRole.setRoleCode("serve");
			orgUserRoleMapper.insert(orgUserRole);
		}
		/**
		 * 运维
		 */
		if (arg0.getMaint() != null && arg0.getMaint() != "") {
			orgUserRole.setRoleId(arg0.getMaint());
			orgUserRole.setRoleCode("maint");
			orgUserRoleMapper.insert(orgUserRole);
		}
		/**
		 * 管理
		 */
		if (arg0.getManage() != null && arg0.getManage() != "") {
			orgUserRole.setRoleId(arg0.getManage());
			orgUserRole.setRoleCode("manage");
			orgUserRoleMapper.insert(orgUserRole);
		}

		return saasOrgUserMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public OrgUser queryUserByName(String name) {
		return saasOrgUserMapper.selectByName(name);
	}

	@Override
	public void updatePassword(String userId) {
		OrgUser orgUser = new OrgUser();
		orgUser.setUserId(userId);
		orgUser.setPassword(DigestUtils.md5Hex("123456".getBytes()));
		saasOrgUserMapper.updateByPrimaryKeySelective(orgUser);
	}

	/**
	 *  页面已经MD5 防止明文密码传递
	 */
	@Override
	public void changePassword(OrgUser user)throws Exception {
		if(StringUtils.isEmpty(user.getUserId())){
			throw new Exception();
		}
		// 只更变密码
		saasOrgUserMapper.updateByPrimaryKeySelective(user);
	}
	
	

}
