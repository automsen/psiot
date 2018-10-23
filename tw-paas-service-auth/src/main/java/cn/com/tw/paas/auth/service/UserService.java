package cn.com.tw.paas.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.paas.auth.entity.User;
import cn.com.tw.paas.auth.mapper.UserMapper;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	public User queryUserByName(String userName){
		return userMapper.selectUserByName(userName);
	}

}
