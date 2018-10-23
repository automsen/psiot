package cn.com.tw.saas.serv.controller.org;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.org.Org;
import cn.com.tw.saas.serv.service.org.OrgService;

/**
 * 机构
 * @author Administrator
 *
 */
@RestController
@RequestMapping("org")
public class OrgController {

	@Autowired
	private OrgService orgService;
	
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<Org> orgs = orgService.selectByPage(page);
		page.setData(orgs);
		return Response.ok(page);
	}
	
	@PostMapping()
	public Response<?> addOrg(@RequestBody @Valid Org org , BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		try {
			orgService.insert(org);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	@GetMapping("{userId}")
	public Response<?> selectByUserId(@PathVariable String userId){
		Org org = orgService.selectByUserId(userId);
		return Response.ok(org);
	}
	
	@PutMapping()
	public Response<?> updateOrg(@RequestBody @Valid Org org, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		try {
			orgService.update(org);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	
	
}
