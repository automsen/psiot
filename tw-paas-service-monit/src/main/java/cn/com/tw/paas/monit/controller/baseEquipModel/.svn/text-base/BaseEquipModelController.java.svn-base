package cn.com.tw.paas.monit.controller.baseEquipModel;

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

import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.baseEquipModel.BaseEquipModel;
import cn.com.tw.paas.monit.service.baseEquipModel.BaseEquipModelService;

/**
 * 设备型号
 * @author Administrator
 *
 */
@RestController
@RequestMapping("model")
public class BaseEquipModelController {
	
	@Autowired
	private BaseEquipModelService baseEquipModelService;
	
	/**
	 * 设备型号页面
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectBaseEquipModelPage(Page page){
		List<BaseEquipModel> baseEquipModels = baseEquipModelService.selectBaseEquipModelPage(page);
		page.setData(baseEquipModels);
		return Response.ok(page);
	}
	
	/**
	 * 设备型号下拉选
	 * @return
	 */
	@GetMapping("all")
	public Response<?> selectBaseEquipModel(BaseEquipModel baseEquipModel){
		List<BaseEquipModel> baseEquipModels = baseEquipModelService.selectBaseEquipModelAll(baseEquipModel);
		return Response.ok(baseEquipModels);
	}
	
	/**
	 * 设备型号添加
	 * @param orgEquipment
	 * @return
	 */
	@PostMapping()
	public Response<?> addOrgEquipment(@RequestBody @Valid BaseEquipModel baseEquipModel, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		baseEquipModelService.insertSelect(baseEquipModel);
		return Response.ok();
	}
	
	/**
	 * 设备型号详情查询
	 * @param modelId
	 * @return
	 */
	@GetMapping("{modelId}")
	public Response<?> detail(@PathVariable String modelId){
		BaseEquipModel baseEquipModel = baseEquipModelService.selectById(modelId);
		return Response.ok(baseEquipModel);
	}
	
	/**
	 * 设备型号修改
	 * @param orgEquipment
	 * @return
	 */
	@PutMapping()
	public Response<?> updateBaseEquipModel(@RequestBody @Valid BaseEquipModel baseEquipModel, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		baseEquipModelService.updateSelect(baseEquipModel);
		return Response.ok();
	}
	
	/**
	 * 信号删除
	 * @param modelId
	 * @return
	 */
	@DeleteMapping("{modelId}")
	public Response<?> deleteOrg(@PathVariable String modelId){
		baseEquipModelService.deleteById(modelId);
		return Response.ok();
	}
	

}
