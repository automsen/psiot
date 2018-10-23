package cn.com.tw.saas.serv.controller.sys;

import java.util.List;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.entity.sys.SysUser;
import cn.com.tw.saas.serv.service.sys.SysUserService;


@RestController
@RequestMapping("sadmin")
@Api(description = "管理用户")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;
	
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("name/{name}")
	public Response<?> selectByName(@PathVariable String name){
		SysUser user = sysUserService.selectByName(name);
		return Response.ok(user);
	}
	
	/**
	 * 根据条件查询
	 * @param sysUser
	 * @return
	 */
	@GetMapping()
	public Response<?> selectByEntity(SysUser sysUser){
		List<SysUser> sysUsers = sysUserService.selectByEntity(sysUser);
		return Response.ok(sysUsers);
	}
}
