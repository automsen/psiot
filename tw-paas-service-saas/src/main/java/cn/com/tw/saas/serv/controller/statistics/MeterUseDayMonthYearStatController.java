package cn.com.tw.saas.serv.controller.statistics;

import java.util.List;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;
import cn.com.tw.saas.serv.service.statistics.MeterUseQuantumService;

@RestController
@RequestMapping("wxuse")
@Api(description = "微信日月年用量统计")
public class MeterUseDayMonthYearStatController {
	
	@Autowired
	private MeterUseQuantumService meterUseQuantumService;
	
	/**
	 * 读取电表今天24小时的读数（点读表）
	 * @param custNo
	 * @return
	 */
	@GetMapping("dayNodes")
	public Response<?> loadMeterDayNodes(@RequestBody MeterUseQuantum node){
		String freeTd = node.getFreezeTd();
		String meterAddr = node.getMeterAddr();
		String meterCateg = node.getMeterCateg();
		List<MeterUseQuantum> uses = null;
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
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR);
		}
	}
	
	
	/**
	 * 读取电表某月的数据
	 * @param custNo
	 * @return
	 */
	@GetMapping("monthNodes")
	public Response<?> loadMeterDaysByMonth(@RequestBody MeterUseQuantum node){
		String freeTd = node.getFreezeTd();
		String meterAddr = node.getMeterAddr();
		String meterCateg = node.getMeterCateg();
		List<MeterUseQuantum> uses = null;
		if(StringUtils.isEmpty(freeTd)|| StringUtils.isEmpty(meterAddr)||StringUtils.isEmpty(meterCateg)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR);
		}
		try {
			if(StringUtils.isEmpty(meterCateg) || "110000".equals(meterCateg)){
				node.setValueType("totalActiveE");
				
			}else if("120000".equals(meterCateg)){
				node.setValueType("waterFlow");
			}
			uses = meterUseQuantumService.selectConditionByMonth(node);
			return Response.ok(uses);
		} catch (Exception e) {
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR);
		}
	}
	
	
	
	/**
	 * 读取表某年的数据
	 * @param custNo
	 * @return
	 */
	@GetMapping("yearNodes")
	public Response<?> loadMeterMonthsByYear(@RequestBody MeterUseQuantum node){
		String freeTd = node.getFreezeTd();
		String meterAddr = node.getMeterAddr();
		String meterCateg = node.getMeterCateg();
		List<MeterUseQuantum> uses = null;
		if(StringUtils.isEmpty(freeTd)|| StringUtils.isEmpty(meterAddr)||StringUtils.isEmpty(meterCateg)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR);
		}
		try {
			if(StringUtils.isEmpty(meterCateg) || "110000".equals(meterCateg)){
				node.setValueType("totalActiveE");
				
			}else if("120000".equals(meterCateg)){
				node.setValueType("waterFlow");
			}
			uses = meterUseQuantumService.selectConditionByYear(node);
			return Response.ok(uses);
		} catch (Exception e) {
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR);
		}
	}

}
