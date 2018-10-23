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
import cn.com.tw.saas.serv.entity.db.rule.RuleAlarm;
import cn.com.tw.saas.serv.service.rule.RuleAlarmService;
/**
 * 预警规则
 * @author Administrator
 *
 */
@RestController
@RequestMapping("alarm")
@Api(description = "预警规则接口")
public class RuleAlarmController {
	
	@Autowired
	private RuleAlarmService ruleAlarmService;
	
	/**
	 * 预警列表页面
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	@ApiOperation(value="预警列表", notes="")
	public Response<?> ruleAlarmPage(Page page){
		List<RuleAlarm> ruleAlarms = ruleAlarmService.selectByPage(page);
		page.setData(ruleAlarms);
		return Response.ok(page);
	}
	
	/**
	 * 预警下拉
	 * @param ruleAlarm
	 * @return
	 */
	@GetMapping("")
	@ApiOperation(value="预警下拉列表", notes="")
	public Response<?> selectAlarmAll(RuleAlarm ruleAlarm){
		List<RuleAlarm> ruleAlarms = ruleAlarmService.selectAlarmAll(ruleAlarm);
		return Response.ok(ruleAlarms);
	}
	
	/**
	 * 预警添加
	 * @param ruleAlarm
	 * @return
	 */
	@PostMapping("")
	@ApiOperation(value="预警添加", notes="")
	public Response<?> addRuleAlarm(@RequestBody @Valid RuleAlarm ruleAlarm, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}		
		try {
			ruleAlarmService.insertSelect(ruleAlarm);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(),e.getMessage());
		}
		return Response.ok();
	}
	
	@ApiOperation(value="预警透支详情查询", notes="")
	@GetMapping("{alarmId}")
	public Response<?> selectRuleAlarmById(@PathVariable String alarmId){
		RuleAlarm ruleAlarm = ruleAlarmService.selectById(alarmId);
		return Response.ok(ruleAlarm);
	}
	
	/**
	 * 预警修改
	 * @param ruleAlarm
	 * @param br
	 * @return
	 */
	@PutMapping("")
	@ApiOperation(value="预警修改", notes="")
	public Response<?> updetaRuleAlarm(@RequestBody @Valid RuleAlarm ruleAlarm, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			ruleAlarmService.updateSelect(ruleAlarm);
		} catch (BusinessException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		
		return Response.ok();
	}
	
	/**
	 * 预警删除
	 * @param alarmId
	 * @return
	 */
	@DeleteMapping("{alarmId}")
	@ApiOperation(value="预警删除", notes="")
	public Response<?> deleteRuleAlarm(@PathVariable String alarmId){
		try {
			ruleAlarmService.deleteById(alarmId);
		} catch (BusinessException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}

}
