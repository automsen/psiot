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

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.OrgApplicationExpand;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.service.org.OrgApplicationService;

@RestController
@RequestMapping("application")
public class OrgApplicationController {

	@Autowired
	private OrgApplicationService orgApplicationService;
	
	
	/**
	 * 机构应用页面
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectOrgApplicationPage(Page page){
		List<OrgApplicationExpand> orgApplications = orgApplicationService.selectOrgApplicationPage(page);
		page.setData(orgApplications);
		return Response.ok(page);
	}
	
	/**
	 * 机构应用查询
	 * @param orgApplication
	 * @return
	 */
	@GetMapping("all")
	public Response<?>  selectOrgApplicationAll(OrgApplication orgApplication){
		List<OrgApplication> orgApplications = orgApplicationService.selectOrgApplicationAll(orgApplication);
		return Response.ok(orgApplications);
	}
	
	/**
	 * 机构应用添加
	 * @param orgApplication
	 * @return
	 */
	@PostMapping()
	public Response<?> addOrg(@RequestBody @Valid OrgApplication orgApplication, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			orgApplicationService.insertSelect(orgApplication);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	
	/**
	 * 机构应用查询
	 * @param appId
	 * @return
	 */
	@GetMapping("{appId}")
	public Response<?> selectByOrgId(@PathVariable String appId){
		OrgApplicationExpand applicationExpand = orgApplicationService.selectByppId(appId);
		return Response.ok(applicationExpand);
	}
	
	
	/**
	 * 机构应用修改
	 * @param orgApplication
	 * @return
	 */
	@PutMapping()
	public Response<?> updateOrg(@RequestBody @Valid OrgApplication orgApplication, BindingResult br){
		if(br.hasErrors()){
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		orgApplicationService.updateSelect(orgApplication);
		return Response.ok();
	}
	
	/**
	 * 机构应用删除
	 * @param appId
	 * @return
	 */
	@DeleteMapping("{appId}")
	public Response<?> deleteOrg(@PathVariable String appId){
		orgApplicationService.deleteById(appId);
		return Response.ok();
	}
	
	@GetMapping("apk")
	public Response<?> queryAppKey(String appKey) {
		return Response.ok(orgApplicationService.selectByAppKey(appKey));
	}
	
	/**
	 * PAAS运维手机端查询
	 * @return
	 */
	@GetMapping("app")
	public Response<?> selectOrgAndApplicationByApp(){
		return Response.ok(orgApplicationService.selectOrgAndApplicationByApp());
	}
}
