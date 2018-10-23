package cn.com.tw.saas.serv.mapper.sys;

import java.util.List;

import javax.websocket.server.PathParam;

import cn.com.tw.saas.serv.entity.sys.SysUser;
import feign.Param;

public interface SysUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

	SysUser selectNumber(String userId);

	SysUser selectByName(String name);

	List<SysUser> selectByEntity(SysUser sysUser);
}