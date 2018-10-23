package cn.com.tw.saas.serv.controller.wechat;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.controller.wechat.vo.RequestParamsVo;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;
import cn.com.tw.saas.serv.service.statistics.MeterUseQuantumService;


/**
 * 仪表历史用量查询
 * @author liming
 * 2017年10月25日16:09:25
 *
 */
@RestController
@RequestMapping("wechat/statistics")
public class StatisticsController {
	@Autowired
	private MeterUseQuantumService meterUseQuantumService;
	/**
	 * 选择日查询小时数据
	 * @param vo
	 * @return
	 */
	@PostMapping("hourByDay")
	public Response<?> selectHourByDay(@RequestBody RequestParamsVo vo){
		String freeTd = vo.getFreezeTd();
		String meterAddr = vo.getMeterAddr();
		String meterCateg = vo.getEnergyType();
		List<MeterUseQuantum> uses = null;
		MeterUseQuantum node = new MeterUseQuantum();
		node.setMeterAddr(meterAddr);
		node.setFreezeTd(freeTd);
		if(StringUtils.isEmpty(freeTd)|| StringUtils.isEmpty(meterAddr)||StringUtils.isEmpty(meterCateg)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR);
		}
		try {
			if(StringUtils.isEmpty(node.getValueType())){
				if(StringUtils.isEmpty(meterCateg)||"110000".equals(meterCateg)){
					node.setValueType("totalActiveE");
					
				}else if("120000".equals(meterCateg)){
					node.setValueType("waterFlow");
				}
			}
			uses = meterUseQuantumService.selectConditionByDay(node);
			return Response.ok(uses);
		} catch (Exception e) {
			return Response.retn(ResultCode.UNKNOW_ERROR, "系统错误！");
		}
	}
	
	/**
	 * 选择月查询日数据
	 * @param vo
	 * @return
	 */
	@PostMapping("dayByMonth")
	public Response<?> selectDayByMonth(@RequestBody RequestParamsVo vo){
		String freeTd = vo.getFreezeTd();
		String meterAddr = vo.getMeterAddr();
		String meterCateg = vo.getEnergyType();
		List<MeterUseQuantum> uses = null;
		MeterUseQuantum node = new MeterUseQuantum();
		node.setMeterAddr(meterAddr);
		node.setFreezeTd(freeTd);
		if(StringUtils.isEmpty(freeTd)|| StringUtils.isEmpty(meterAddr)||StringUtils.isEmpty(meterCateg)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR);
		}
		try {
			if(StringUtils.isEmpty(node.getValueType())){
				if(StringUtils.isEmpty(meterCateg)||"110000".equals(meterCateg)){
					node.setValueType("totalActiveE");
					
				}else if("120000".equals(meterCateg)){
					node.setValueType("waterFlow");
				}
			}
			uses = meterUseQuantumService.selectConditionByMonth(node);
			return Response.ok(uses);
		} catch (Exception e) {
			return Response.retn(ResultCode.UNKNOW_ERROR, "系统错误！");
		}
	}
	
	/**
	 * 选择年查询月数据
	 * @param vo
	 * @return
	 */
	@PostMapping("monthByYear")
	public Response<?> selectMonthByYear(@RequestBody RequestParamsVo vo){
		String freeTd = vo.getFreezeTd();
		String meterAddr = vo.getMeterAddr();
		String meterCateg = vo.getEnergyType();
		List<MeterUseQuantum> uses = null;
		MeterUseQuantum node = new MeterUseQuantum();
		node.setMeterAddr(meterAddr);
		node.setFreezeTd(freeTd);
		if(StringUtils.isEmpty(freeTd)|| StringUtils.isEmpty(meterAddr)||StringUtils.isEmpty(meterCateg)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR);
		}
		try {
			if(StringUtils.isEmpty(node.getValueType())){
				if(StringUtils.isEmpty(meterCateg)||"110000".equals(meterCateg)){
					node.setValueType("totalActiveE");
					
				}else if("120000".equals(meterCateg)){
					node.setValueType("waterFlow");
				}
			}
			uses = meterUseQuantumService.selectConditionByYear(node);
			return Response.ok(uses);
		} catch (Exception e) {
			return Response.retn(ResultCode.UNKNOW_ERROR, "系统错误！");
		}
	}
	
}
