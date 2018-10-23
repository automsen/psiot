package cn.com.tw.saas.serv.controller.statistics;

import java.util.List;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.com.tw.common.utils.tools.excel.ExcelControllerHelper;
import cn.com.tw.common.utils.tools.excel.ExportUtil;
import cn.com.tw.common.utils.tools.excel.ViewExcel;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;
import cn.com.tw.saas.serv.service.statistics.MeterUseQuantumService;

/**
 * 小时总回路路用量页面
 * @author Administrator
 *
 */
@RestController
@RequestMapping("use1")
@Api(description = "小时总回路路用量页面")
public class HoursStaticsController {

	@Autowired
	private MeterUseQuantumService meterUseQuantumService;
	
	/**
	 * 分页
	 * @param hoursStaticsService
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectHoursStatics(Page page){
		List<MeterUseQuantum> hoursStatics1 = meterUseQuantumService.selectHoursStatics(page);
		page.setData(hoursStatics1);
		return Response.ok(page);
	}
	
	
	
	/**
	 * 预览导出
	 * @param hoursStatics
	 * @return
	 */
	@GetMapping("/export")
	public ModelAndView hoursStaticsExport(MeterUseQuantum useQuantum){
		ExportUtil exprot = null;
		try {
			String[] headers = {"房间名称","客户姓名","能源类型","表号","时标","本次止码","上次止码","倍率","回路类型","当前用量","付费类型","金额（元）"};
			String[] fields = {"roomName","customerName","meterCateg","meterAddr", "freezeTd","readValue","preReadValue","multiple","loopTypeStr","useValue","controlType","moneyNowStr"};
			List<MeterUseQuantum> dataList = meterUseQuantumService.hoursStaticsExport(useQuantum);
			exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, "小时用量统计","小时用量统计");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(new ViewExcel(), exprot.build());
	}
	
}
