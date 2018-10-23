package cn.com.tw.saas.serv.service.sys;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.sys.SysUser;

public interface SysUserService extends IBaseSerivce<SysUser>{

	SysUser selectNumber(String userId);

	SysUser selectByName(String name);

	List<SysUser> selectByEntity(SysUser sysUser);

}
