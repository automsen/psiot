package cn.com.tw.saas.serv.controller.org;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.entity.org.OrgRole;
import cn.com.tw.saas.serv.service.org.OrgRoleService;

/**
 * 机构角色
 * @author Administrator
 *
 */
@RestController
@RequestMapping("role")
public class OrgRoleController {

	@Autowired
	private OrgRoleService orgRoleService;
	
	/**
	 * 机构角色下拉选择
	 * @return
	 */
	@GetMapping("")
	public Response<?> selectRoleAll(){
		List<OrgRole> orgRoles = orgRoleService.selectRoleAll();
		return Response.ok(orgRoles);
	}
}
