package cn.com.tw.saas.serv.controller.rule;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
import cn.com.tw.saas.serv.entity.db.rule.Surcharge;
import cn.com.tw.saas.serv.service.rule.SurchargeService;
/**
 * 附加费规则
 * @author Administrator
 *
 */
@RestController
@RequestMapping("surcharge")
@Api(description = " 附加费规则接口")
@Deprecated
public class SurchargeController {
	
	@Autowired
	private SurchargeService surchargeService;
	
	/**
	 * 附加费用页面
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	@ApiOperation(value="附加费用列表", notes="")
	public Response<?> surchargePage(Page page){
		List<Surcharge> surcharges = surchargeService.selectByPage(page);
		page.setData(surcharges);
		return Response.ok(page);
	}
	
	/**
	 * 附加费用下拉
	 * @param surcharge
	 * @return
	 */
	@GetMapping("")
	@ApiOperation(value="附加费用下拉列表", notes="")
	public Response<?> selectSurchargeAll(Surcharge surcharge){
		List<Surcharge> surcharges = surchargeService.selectSurchargeAll(surcharge);
		return Response.ok(surcharges);
	}
	
	/**
	 * 附加费用添加
	 * @param ruleAlarm
	 * @return
	 */
	@PostMapping("")
	@ApiOperation(value="附加费用添加", notes="")
	public Response<?> addSurcharge(@RequestBody @Valid Surcharge surcharge, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			surchargeService.insertSelect(surcharge);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 附加费用修改
	 * @param ruleAlarm
	 * @param br
	 * @return
	 */
	@PutMapping("")
	@ApiOperation(value="附加费用修改", notes="")
	public Response<?> updetaSurcharge(@RequestBody @Valid Surcharge surcharge, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			surchargeService.updateSelect(surcharge);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 附加费用规则
	 * @param alarmId
	 * @return
	 */
	@DeleteMapping("")
	@ApiOperation(value="删除附加费用", notes="")
	public Response<?> deleteSurcharge(@PathVariable String surchargeId){
		surchargeService.deleteById(surchargeId);
		return Response.ok();
	}

}
