package cn.com.tw.saas.serv.controller.dict;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.dict.SysDict;
import cn.com.tw.saas.serv.service.dict.SysDictService;

@RestController
@RequestMapping("dict")
@Api(description = "数据字典接口")
public class SysDictController {

	@Autowired
	private SysDictService sysDictService;
	/**
	 * 数据字典列表
	 * @param page
	 * @return
	 */
	@ApiOperation(value="获数据字典列表", notes="")
	@GetMapping("page")
	@ResponseBody
	public Response<?> selectByPage(Page page){
		List<SysDict> list=null;
		try {
			list=sysDictService.selectByPage(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}
	
	@ApiOperation(value="数据字典下拉列表", notes="")
	@GetMapping("{dictType}")
	@ResponseBody
	public Response<?> selectByDictType(@PathVariable String dictType){
		List<SysDict> list=null;
		try {
			list=sysDictService.selectByDictType(dictType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(list);
	}
	
	@GetMapping("all")
	public Response<?> selectDict(SysDict sysDict){
		List<SysDict> sysDicts = sysDictService.selectAll(sysDict);
		return Response.ok(sysDicts);
	}
}
