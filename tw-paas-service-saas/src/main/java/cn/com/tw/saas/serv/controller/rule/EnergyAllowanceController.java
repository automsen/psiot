package cn.com.tw.saas.serv.controller.rule;

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
import cn.com.tw.saas.serv.entity.db.rule.EnergyAllowance;
import cn.com.tw.saas.serv.service.rule.EnergyAllowanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("allowance")
@Api(description = "计划用量规则接口")
public class EnergyAllowanceController {
	@Autowired
	private EnergyAllowanceService allowanceService;
	
	@GetMapping("page")
	@ApiOperation(value = "分页查询", notes = "")
	public Response<?> EnergyAllowancePage(Page page) {
		List<EnergyAllowance> allowanceRules = allowanceService.selectByPage(page);
		page.setData(allowanceRules);
		return Response.ok(page);
	}

	@GetMapping("")
	@ApiOperation(value = "列表查询", notes = "")
	public Response<?> selectByEntity(EnergyAllowance param) {
		List<EnergyAllowance> allowanceRules = allowanceService.selectByEntity(param);
		return Response.ok(allowanceRules);
	}

	@PostMapping("")
	@ApiOperation(value = "添加", notes = "")
	public Response<?> addLadderPrice(@RequestBody @Valid EnergyAllowance allowanceRule, BindingResult br) {
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			allowanceService.insert(allowanceRule);
		} catch (BusinessException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		
		return Response.ok();
	}

	
	@GetMapping("{ruleId}")
	@ApiOperation(value = "主键查询", notes = "")
	public Response<?> selectByPriceId(@PathVariable String ruleId) {
		EnergyAllowance allowanceRule = allowanceService.selectById(ruleId);
		return Response.ok(allowanceRule);
	}

	/**
	 * 计价修改
	 * 
	 * @param EnergyAllowance
	 * @param br
	 * @return
	 */
	@PutMapping("")
	@ApiOperation(value = "修改", notes = "")
	public Response<?> updetaLadderPrice(@RequestBody @Valid EnergyAllowance allowanceRule, BindingResult br) {
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			allowanceService.update(allowanceRule);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		
		return Response.ok();
	}

	@DeleteMapping("{ruleId}")
	@ApiOperation(value = "删除", notes = "")
	public Response<?> deleteLadderPrice(@PathVariable String ruleId) {
		try {
			allowanceService.deleteById(ruleId);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
}
