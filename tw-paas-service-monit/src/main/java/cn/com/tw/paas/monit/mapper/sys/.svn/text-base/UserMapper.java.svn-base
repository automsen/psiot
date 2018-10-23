package cn.com.tw.paas.monit.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.sys.UserExpend;
import cn.com.tw.paas.monit.entity.db.sys.User;

public interface UserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	List<User> selectByPage(Page arg0);

	List<User> selectUserAll(User user);

	User selectByUserName(@Param("userName") String userName);

	List<UserExpend> selectUserByPage(Page page);

	User userLogin(User user);
}