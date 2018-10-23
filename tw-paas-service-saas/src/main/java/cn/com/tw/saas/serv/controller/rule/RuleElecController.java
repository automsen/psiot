package cn.com.tw.saas.serv.controller.rule;

import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.Api;

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
import cn.com.tw.saas.serv.entity.db.rule.RuleElec;
import cn.com.tw.saas.serv.service.rule.RuleElecService;

/**
 * 限制用电规则
 * @author Administrator
 *
 */
@RestController
@RequestMapping("ruleelec")
@Api(description = "限制用电规则")
public class RuleElecController {
	
	@Autowired
	private RuleElecService ruleElecService;
	
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<RuleElec> ruleElecs = ruleElecService.selectByPage(page);
		page.setData(ruleElecs);
		return Response.ok(page);
	}
	
	/**
	 * 限制用电规则添加
	 * @param ruleElec
	 * @param br
	 * @return
	 */
	@PostMapping()
	public Response<?> add(@RequestBody @Valid RuleElec ruleElec, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			ruleElecService.insert(ruleElec);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 限制用电规则修改
	 * @param ruleElec
	 * @param br
	 * @return
	 */
	@PutMapping()
	public Response<?> update(@RequestBody @Valid RuleElec ruleElec, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			ruleElecService.updateSelect(ruleElec);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
	    return Response.ok();
	}
	
	/**
	 * 详情查询
	 * @param ruleId
	 * @return
	 */
	@GetMapping("{ruleId}")
	public Response<?> selectById(@PathVariable String ruleId){
		RuleElec ruleElec = ruleElecService.selectById(ruleId);
		return Response.ok(ruleElec);
	}

	/**
	 * 条件查询
	 * @param ruleElec
	 * @return
	 */
	@GetMapping()
	public Response<?> selectByEntity(RuleElec ruleElec){
		List<RuleElec> ruleElecs = ruleElecService.selectByEntity(ruleElec);
		return Response.ok(ruleElecs);
	}
	
	@DeleteMapping("{ruleId}")
	public Response<?> delete(@PathVariable String ruleId){
		ruleElecService.deleteById(ruleId);
		return Response.ok();
	}
	
}
