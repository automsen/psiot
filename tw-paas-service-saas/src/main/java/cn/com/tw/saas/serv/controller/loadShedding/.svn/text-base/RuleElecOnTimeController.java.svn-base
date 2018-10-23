package cn.com.tw.saas.serv.controller.loadShedding;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.entity.loadShedding.RuleElecOnTimeUnusual;
import cn.com.tw.saas.serv.entity.loadShedding.RuleElecOnTimeUsual;
import cn.com.tw.saas.serv.service.loadShedding.RuleElecOnTimeUnusualService;
import cn.com.tw.saas.serv.service.loadShedding.RuleElecOnTimeUsualService;


@RestController
@RequestMapping("ruleElecOnTime")
@Api(description = "非全天性用电")
public class RuleElecOnTimeController {
	
	@Autowired
	private RuleElecOnTimeUsualService ruleElecOnTimeUsualService;
	
	@Autowired
	private RuleElecOnTimeUnusualService ruleElecOnTimeUnusualService;
	
	/**
	 * 新增或修改限电周1--天模版
	 * @param List<RuleElecOnTimeUsual>
	 * @return
	 */
	@ApiOperation(value = "新增或修改限电周1--天模版", notes = "")
	@PostMapping("replace")
	public Response<?> replaceRuleElecOnTimeUsual(@RequestBody List<RuleElecOnTimeUsual> infos){
		int a = ruleElecOnTimeUsualService.replaceRuleElecOnTimeUsual(infos);
		if(a == 1){
			return Response.retn(ResultCode.UNKNOW_ERROR, "未知错误");
		}
		return Response.ok();
	}
	
	/**
	 * 查询模版列表
	 * @param RuleElecOnTimeUsual
	 * @return
	 */
	@ApiOperation(value = "查询模版列表 ", notes = "")
	@GetMapping("list")
	public Response<?> selectByList(RuleElecOnTimeUsual retu){
		List<RuleElecOnTimeUsual> list = ruleElecOnTimeUsualService.selectByAll(retu);
		return Response.ok(list);
	}
	
	/**
	 * 查询日历数据
	 * @param RuleElecOnTimeUnusual
	 * @return
	 */
	@ApiOperation(value = "查询日历数据", notes = "")
	@PostMapping("fullCalendarData")
	public Response<?> selectByStartAndEndTime(@RequestBody RuleElecOnTimeUnusual reotu){
		List<RuleElecOnTimeUnusual> list = ruleElecOnTimeUnusualService.selectByStartAndEndTime(reotu);
		return Response.ok(list);
	}
	
	/**
	 * 新增或修改特殊日期
	 * @param List<RuleElecOnTimeUsual>
	 * @return
	 */
	@ApiOperation(value = "新增或修改特殊日期", notes = "")
	@PostMapping("unusual/replace")
	public Response<?> replaceRuleElecOnTimeUnusual(@RequestBody RuleElecOnTimeUnusual info){
		ruleElecOnTimeUnusualService.replaceRuleElecOnTimeUnusual(info);
		return Response.ok();
	}
}
