package cn.com.tw.paas.monit.service.sys.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.common.utils.cons.code.MonitResultCode;
import cn.com.tw.paas.monit.entity.db.sys.User;
import cn.com.tw.paas.monit.mapper.sys.UserMapper;
import cn.com.tw.paas.monit.service.sys.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public int deleteById(String userId) {
		userMapper.deleteByPrimaryKey(userId);
		return 0;
	}

	@Override
	public int insert(User arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(User user) {
		
		/**
		 * 用户名校验重复
		 */
		User user1 = userMapper.selectByUserName(user.getUserName());
		if(!StringUtils.isEmpty(user1)){
			throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR, "");
		}
		user.setPassword(DigestUtils.md5Hex(user.getPassword().getBytes()));
		user.setUserId(CommUtils.getUuid());
		user.setCreateTime(new Date(System.currentTimeMillis()));
		userMapper.insertSelective(user);
		return 0;
	}

	@Override
	public User selectById(String arg0) {
		return userMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<User> selectByPage(Page arg0) {
		List<User> users = userMapper.selectByPage(arg0);
		return users;
	}

	@Override
	public int update(User arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(User arg0) {
		
		/**
		 * 用户名校验重复
		 */
		User user = userMapper.selectByPrimaryKey(arg0.getUserId());
		if(!user.getUserName().equals(arg0.getUserName())){
			User user1 = userMapper.selectByUserName(arg0.getUserName());
			if(!StringUtils.isEmpty(user1)){
				throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR, "");
			}
		}
		arg0.setPassword(DigestUtils.md5Hex(arg0.getPassword().getBytes()));
		
		userMapper.updateByPrimaryKeySelective(arg0);
		return 0;
	}

	@Override
	public List<User> selectUserAll(User user) {
		List<User> users = userMapper.selectUserAll(user);
		return users;
	}

	@Override
	public User selectByName(String name) {
		return userMapper.selectByUserName(name);
	}

	@Override
	public User userLogin(User user) {
		user.setPassword(DigestUtils.md5Hex(user.getPassword().getBytes()));
		return userMapper.userLogin(user);
	}

}
