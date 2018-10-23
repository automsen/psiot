package cn.com.tw.paas.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import cn.com.tw.common.web.core.IBaseMapper;
import cn.com.tw.paas.auth.entity.User;

@Mapper
public interface UserMapper extends IBaseMapper<User>{
	
	
	@Select(value="select user_id,user_name,`password`,phone,email,qq,wechat,real_name,users.org_id, org.org_name,org.org_level,org.org_website,org.org_logo from t_org_user users"+
				" LEFT JOIN t_org org on users.org_id = org.org_id"+
					" where user_name = #{userName}")
	@Results({
		@Result(column="user_name",property="userName"),
		@Result(column="user_id",property="userId"),
		@Result(column="password",property="password"),
		@Result(column="phone",property="phone"),
		@Result(column="email",property="email"),
		@Result(column="qq",property="qq"),
		@Result(column="wechat",property="wechat"),
		@Result(column="real_name",property="realName"),
		@Result(column="is_usable",property="isUsable"),
		@Result(column="org_name",property="orgName"),
		@Result(column="create_time",property="createTime"),
		@Result(column="org_level",property="orgLevel"),
		@Result(column="org_name",property="orgName"),
		@Result(column="org_id",property="orgId"),
		@Result(column="org_website",property="orgWebsite"),
		@Result(column="org_logo",property="orgLogo")
	})
	User selectUserByName(@Param(value = "userName")String userName);

}
