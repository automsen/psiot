package cn.com.tw.saas.serv.controller.room;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.AllowancePlan;
import cn.com.tw.saas.serv.service.room.AllowanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping("allowancePlan")
@Api(description = "补助计划接口")
public class AllowanceController {
	@Autowired
	private AllowanceService allowanceService;
	
	@ApiOperation(value = "保存计划", notes = "")
	@PostMapping("save")
	public Response<?> savePlan(@RequestBody AllowancePlan plan) {
		int result = 0;
		result = allowanceService.savePlan(plan);
		return Response.ok(result);
	}
	
	@ApiOperation(value = "取消计划", notes = "")
	@DeleteMapping("cancel/{id}")
	public Response<?> cancelPlan(@PathVariable String id) {
		int result = 0;
		result = allowanceService.cancelPlan(id);
		return Response.ok(result);
	}
	
	@ApiOperation(value = "分页查询", notes = "")
	@GetMapping("page")
	@ResponseBody
	public Response<?> selectByPage(Page page) {
		List<AllowancePlan> list = null;
		try {
			list = allowanceService.selectByPage(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}
}
