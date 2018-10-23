package cn.com.tw.paas.monit.controller.sys;

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

import springfox.documentation.annotations.ApiIgnore;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.sys.Dict;
import cn.com.tw.paas.monit.service.sys.DictService;

/**
 * 数据字典
 * @author Administrator
 *
 */
@ApiIgnore
@RestController
@RequestMapping("dict")
public class DictController {
	
	@Autowired
	private DictService dictService;
	
	
	/**
	 * 字典页面
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectDictPage(Page page){
		List<Dict> dicts = dictService.selectDictByPage(page);
		page.setData(dicts);
		return Response.ok(page);
	}
	/**
	 * 字典查询
	 * @param dict
	 * @return
	 */
	@GetMapping("all")
	public Response<?>  selectDictAll(Dict dict){
		List<Dict> dicts = dictService.selectDictAll(dict);
		return Response.ok(dicts);
	}
	
	/**
	 * 字典添加
	 * @param dict
	 * @return
	 */
	@PostMapping()
	public Response<?> addDict(@RequestBody @Valid Dict dict, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		dictService.insertSelect(dict);
		return Response.ok();
	}
	
	/**
	 * 详情查询
	 * @param equipId
	 * @return
	 */
	@GetMapping("{dictId}")
	public Response<?> selectByEquipId(@PathVariable("dictId") String dictId){
		Dict dict = dictService.selectById(dictId);
		return Response.ok(dict);
	}
	
	/**
	 * 字典修改
	 * @param dict
	 * @return
	 */
	@PutMapping()
	public Response<?> updateDict(@RequestBody @Valid Dict dict, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		dictService.updateSelect(dict);
		return Response.ok();
	}

}
