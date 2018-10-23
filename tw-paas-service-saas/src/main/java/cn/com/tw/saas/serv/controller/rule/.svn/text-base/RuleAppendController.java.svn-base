package cn.com.tw.saas.serv.controller.rule;

import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
import cn.com.tw.saas.serv.entity.db.rule.RuleAppend;
import cn.com.tw.saas.serv.service.rule.RuleAppendService;

/**
 * 附加费用
 * @author Administrator
 *
 */
@RestController
@RequestMapping("append")
@Api(description = "附加规则接口")
public class RuleAppendController {

	@Autowired
	private RuleAppendService ruleAppendService;
	
	@GetMapping("page")
	@ApiOperation(value="附加费列表", notes="")
	public Response<?> selectByPage(Page page){
		List<RuleAppend> ruleAppends = ruleAppendService.selectByPage(page);
		page.setData(ruleAppends);
		return Response.ok(page);
	}
	
	@GetMapping("")
	@ApiOperation(value="附加费下拉选择", notes="")
	public Response<?> selectByEntity(RuleAppend ruleAppend){
		List<RuleAppend> ruleAppends = ruleAppendService.selectByEntity(ruleAppend);
		return Response.ok(ruleAppends);
	}
	
	@GetMapping("{appendId}")
	@ApiOperation(value="附加费详情页面", notes="")
	public Response<?> selectByAppendId(@PathVariable String appendId){
		RuleAppend ruleAppend = ruleAppendService.selectById(appendId);
		return Response.ok(ruleAppend);
	}
	
	@PostMapping("")
	@ApiOperation(value="附加费添加", notes="")
	public Response<?> add(@RequestBody @Valid RuleAppend ruleAppend, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			ruleAppendService.insert(ruleAppend);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	@PutMapping()
	@ApiOperation(value="附加费修改", notes="")
	public Response<?> update(@RequestBody @Valid RuleAppend ruleAppend, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			ruleAppendService.updateSelect(ruleAppend);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	@DeleteMapping("{appendId}")
	@ApiOperation(value="附加费删除", notes="")
	public Response<?> delete(@PathVariable String appendId){
		try {
			ruleAppendService.deleteById(appendId);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
}
