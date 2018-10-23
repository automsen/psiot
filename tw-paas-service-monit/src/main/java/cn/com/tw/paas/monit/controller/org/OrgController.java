package cn.com.tw.paas.monit.controller.org;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.common.utils.cons.code.MonitResultCode;
import cn.com.tw.paas.monit.entity.business.org.OrgExpand;
import cn.com.tw.paas.monit.entity.db.org.OrgUser;
import cn.com.tw.paas.monit.service.org.OrgService;
import cn.com.tw.paas.monit.service.org.OrgUserService;

@ApiIgnore
@RestController
@RequestMapping("org")
public class OrgController {

	@Autowired
	private OrgService orgService;
	
	@Autowired
	private OrgUserService orgUserService;
	
	/**
	 * 机构页面
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectOrgPage(Page page){
		List<OrgExpand> orgExpands = orgService.selectOrgExpandByPage(page);
		page.setData(orgExpands);
		return Response.ok(page);
	}
	
	/**
	 * 机构查询
	 * @param dict
	 * @return
	 */
	@GetMapping("all")
	public Response<?>  selectDictAll(OrgExpand orgExpand){
		List<OrgExpand> orgExpands = orgService.selectOrgAll(orgExpand);
		return Response.ok(orgExpands);
	}
	/**
	 * 机构用户   -------机构查询
	 * @param dict
	 * @return
	 */
	@GetMapping("orgAll")
	public Response<?>  selectDictAllByOrg(OrgExpand orgExpand){
		
		JwtInfo jwt = JwtLocal.getJwt();
		
		OrgUser user = orgUserService.selectById(jwt.getSubject());
		
		orgExpand.setOrgId(user.getOrgId());
		
		List<OrgExpand> orgExpands = orgService.selectOrgAll(orgExpand);
		return Response.ok(orgExpands);
	}
	
	
	/**
	 * 机构添加
	 * @param dict
	 * @return
	 */
	@PostMapping()
	public Response<?> addOrg(@RequestBody @Valid OrgExpand orgExpand, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			orgService.insertOrgAndOrgUser(orgExpand);
		} catch (Exception e) {
			return Response.retn(MonitResultCode.DATA_EXISTS_ERROR, e.getMessage());
		}
		return Response.ok();
	}
	
	
	/**
	 * 详情查询
	 * @param equipId
	 * @return
	 */
	@GetMapping("{orgId}")
	public Response<?> selectByOrgId(@PathVariable String orgId){
		OrgExpand orgExpand = orgService.selectByOrgId(orgId);
		return Response.ok(orgExpand);
	}
	
	
	/**
	 * 机构修改
	 * @param dict
	 * @return
	 */
	@PutMapping()
	public Response<?> updateOrg(@RequestBody @Valid OrgExpand orgExpand, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			orgService.updateOrgAndOrgUser(orgExpand);
		} catch (Exception e) {
			return Response.retn(MonitResultCode.DATA_EXISTS_ERROR, e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 机构删除
	 * @param orgId
	 * @return
	 */
	@DeleteMapping("{orgId}")
	public Response<?> deleteOrg(@PathVariable String orgId){
		orgService.deleteById(orgId);
		return Response.ok();
	}

	
}
