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
import cn.com.tw.saas.serv.entity.db.rule.EnergyPrice;
import cn.com.tw.saas.serv.service.rule.EnergyPriceService;
/**
 * 计价规则
 * @author Administrator
 *
 */
@RestController
@RequestMapping("price")
@Api(description = "计价规则接口")
public class EnergyPriceController {
	
	@Autowired
	private EnergyPriceService energyPriceService;

	/**
	 * 计价规则页面
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	@ApiOperation(value="计价规则列表", notes="")
	public Response<?> energyPricePage(Page page){
		List<EnergyPrice> energyPrices = energyPriceService.selectByPage(page);
		page.setData(energyPrices);
		return Response.ok(page);
	}
	
	/**
	 * 计价下拉
	 * @param energyPrice
	 * @return
	 */
	@GetMapping("")
	@ApiOperation(value="计价下拉列表", notes="")
	public Response<?> selectByBean(EnergyPrice energyPrice){
		List<EnergyPrice> energyPrices = energyPriceService.selectByBean(energyPrice);
		return Response.ok(energyPrices);
	}
	
	/**
	 * 计价添加
	 * @param energyPrice
	 * @return
	 */
	@PostMapping("")
	@ApiOperation(value="计价添加", notes="")
	public Response<?> addLadderPrice(@RequestBody @Valid EnergyPrice energyPrice, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			energyPriceService.insert(energyPrice);
		} catch (BusinessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	@ApiOperation(value="根据Id查计价", notes="")
	@GetMapping("{priceId}")
	public Response<?> selectByPriceId(@PathVariable String priceId){
		EnergyPrice energyPrice = energyPriceService.selectById(priceId);
		return Response.ok(energyPrice);
	}
	
	/**
	 * 计价修改
	 * @param energyPrice
	 * @param br
	 * @return
	 */
	@PutMapping("")
	@ApiOperation(value="计价修改", notes="")
	public Response<?> updetaLadderPrice(@RequestBody @Valid EnergyPrice energyPrice, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			energyPriceService.updateSelect(energyPrice);
		} catch (BusinessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 计价删除
	 * @param priceId
	 * @return
	 */
	@DeleteMapping("{priceId}")
	@ApiOperation(value="计价删除", notes="")
	public Response<?> deleteLadderPrice(@PathVariable String priceId){
		try {
			energyPriceService.deleteById(priceId);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(),e.getMessage());
		}
		return Response.ok();
	}
}
