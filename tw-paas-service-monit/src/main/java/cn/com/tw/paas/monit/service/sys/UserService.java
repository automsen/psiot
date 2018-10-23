package cn.com.tw.paas.monit.service.sys;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.paas.monit.entity.db.sys.User;

public interface UserService extends IBaseSerivce<User>{

	List<User> selectUserAll(User user);

	User selectByName(String name);

	User userLogin(User user);
}
