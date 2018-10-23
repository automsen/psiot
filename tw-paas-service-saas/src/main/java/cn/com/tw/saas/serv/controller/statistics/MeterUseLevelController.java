package cn.com.tw.saas.serv.controller.statistics;

import java.util.List;

import io.swagger.annotations.Api;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.support.logging.Log;

import cn.com.tw.common.utils.tools.excel.ExcelControllerHelper;
import cn.com.tw.common.utils.tools.excel.ExportUtil;
import cn.com.tw.common.utils.tools.excel.ViewExcel;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.ExcelUtils;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;
import cn.com.tw.saas.serv.service.statistics.MeterUseQuantumService;

/**
 * 仪表用量网页
 * @author Administrator
 *
 */
@RestController
@RequestMapping("use")
@Api(description = "仪表用量接口")
public class MeterUseLevelController {
	
	@Autowired
	private MeterUseQuantumService meterUseQuantumService;
	
	private static Logger logger = LoggerFactory.getLogger(MeterUseLevelController.class);
	
	@GetMapping("page")
	public Response<?> selectMeterUseLevel(MeterUseQuantum useQuantum){
		Page page = new Page();
		List<MeterUseQuantum> meterUseQuantums = meterUseQuantumService.selectMeterUseLevel(useQuantum);
		page.setData(meterUseQuantums);
		return Response.ok(page);
	}
	@GetMapping("hourpage")
	public Response<?> selectMeterUseHour(Page page){
		List<MeterUseQuantum> meterUseHour=meterUseQuantumService.selectMeterUseHour(page);
		page.setData(meterUseHour);
		return Response.ok(page);
	}
	
	/**
	 * 房间用电的天查询
	 * @param page
	 * @return
	 */
	@GetMapping("dayElcPage")
	public Response<?> selectDayElc(Page page){
		List<MeterUseQuantum> dayElc=meterUseQuantumService.selectDayElc(page);
		page.setData(dayElc);
		return Response.ok(page);
	}
	/**
	 * 房间用电的月查询
	 * @param page
	 * @return
	 */
	@GetMapping("monthElcPage")
	public Response<?> selectMonthElc(Page page){
		List<MeterUseQuantum>  monthElc=meterUseQuantumService.selectMonthElc(page);
		page.setData(monthElc);
		return Response.ok(page);
	}
	/**
	 * 房间用电的年查询
	 * @param page
	 * @return
	 */
	@GetMapping("yearElcPage")
	public Response<?> selectYearElc(Page page){
		List<MeterUseQuantum> yearElc =meterUseQuantumService.selectYearElc(page);
		page.setData(yearElc);
		return Response.ok(page);
	}
	
	@GetMapping("/export/houseUse")
	public ModelAndView houseUseExpert(MeterUseQuantum useQuantum){
		ExportUtil exprot = null;
		try {
			String[] headers = {"日期","楼栋号","房间号","房间名称","房间类型","电表号","总费用","总用电量","主回路用电量","副回路用电量"};
			String[] ENheaders = {"Date","Build No.","Room No.","Room Name","Room type","elec No.","All-in cost","Total","Major","Minor"};
			String[] fields = {"freezeTd","regionFullName","roomNumber","roomName", "roomUse","meterAddr","moneyNow","totalUseValue","zUseValue","viceUseValue"};
 			List<MeterUseQuantum> dataList = meterUseQuantumService.selectHouseUseExpert(useQuantum);
 			Boolean isEnExcel=!StringUtils.isEmpty(useQuantum.getIsEnExcel());
 			String a="";
			if(("day").equals(useQuantum.getDateType())){
				a="房间用电量日数据统计";
				if(isEnExcel){
					a="Room Daily Consumption Statistics";
				}
			}else if(("month").equals(useQuantum.getDateType())){
				a="房间用电量月数据统计";
				if(isEnExcel){
					a="Room Monthly Consumption Statistics";
				}
			}else if(("year").equals(useQuantum.getDateType())){
				a="房间用电量年数据统计";
				if(isEnExcel){
					a="Room Yearly Consumption Statistics";
				}
			}
			
			MeterUseQuantum q = (MeterUseQuantum) ExcelUtils.loadTotalColumns(dataList,fields[0],"moneyNow","totalUseValue");
			if(isEnExcel){
				q.setFreezeTd("Total");
			}
			dataList.add(q);
			if(isEnExcel){
				exprot  = ExcelControllerHelper.simpleExportData(dataList, ENheaders, fields, a,"Room Consumption Statistics");
			}else{
				exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, a,"房间用电量统计");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(new ViewExcel(), exprot.build());
	}
	
	
	
	/**
	 * 根据小时时标来查询出对应的用电量
	 * @param freezeTd
	 * @param meterAddr
	 * @return
	 */
	/*@GetMapping("selectDayLineDate")
	public Response<?>  selectDayLineDate(String  freezeTd,String meterAddr){
		List<MeterUseQuantum> dayLineElc=meterUseQuantumService.selectDayLineDate(freezeTd,meterAddr);
		return Response.ok(dayLineElc);
	}*/
	
	/**
	 * 根据小时来查询出房屋天用电量
	 * @param freezeTd
	 * @param meterAddr
	 * @return
	 */
	@GetMapping("selectDayLineDate1")
	public Response<?>  selectDayLineDate1(String  freezeTd,String meterAddr){
		List<MeterUseQuantum> dayLineElc=meterUseQuantumService.selectDayLineDate1(freezeTd,meterAddr);
		return Response.ok(dayLineElc);
	}
	/**
	 * 根据天来查询出房屋的月用电量
	 * @param freezeTd
	 * @param meterAddr
	 * @return
	 */
	@GetMapping("selectMonthLineDate1")
	public Response<?> selectMonthLineDate1(String  freezeTd,String meterAddr){
		List<MeterUseQuantum> monthLineElc=meterUseQuantumService.selectMonthLineDate1(freezeTd,meterAddr);
		return Response.ok(monthLineElc);
	}
	/**
	 * 根据月来查询出房屋的年用电量
	 * @param freezeTd
	 * @param meterAddr
	 * @return
	 */
	@GetMapping("selectYearLineDate1")
	public Response<?> selectYearLineDate1(String  freezeTd,String meterAddr){
		List<MeterUseQuantum> yearLineElc=meterUseQuantumService.selectYearLineDate1(freezeTd,meterAddr);
		return Response.ok(yearLineElc);
	} 
	
	
	/**
	 * 根据天时标来查询出月的用电量
	 * @param freezeTd
	 * @param meterAddr
	 * @return
	 */
	/*@GetMapping("selectMonthLineDate")
	public Response<?> selectMonthLineDate(String  freezeTd,String meterAddr){
		List<MeterUseQuantum> monthLineElc=meterUseQuantumService.selectMonthLineDate(freezeTd,meterAddr);
		return Response.ok(monthLineElc);
	}*/
	/**
	 * 根据月时标来查询出年的用电量
	 * @param freezeTd
	 * @param meterAddr
	 * @return
	 */
	/*@GetMapping("selectYearLineDate")
	public Response<?> selectYearLineDate(String  freezeTd,String meterAddr){
		List<MeterUseQuantum> monthLineElc=meterUseQuantumService.selectYearLineDate(freezeTd,meterAddr);
		return Response.ok(monthLineElc);
	}*/
	
	
	@GetMapping("daypage")
	public Response<?> selectMeterUseday(Page page){
		List<MeterUseQuantum> meterUseDays = meterUseQuantumService.selectMeterUseday(page);
		page.setData(meterUseDays);
		return Response.ok(page);
	}
	
	@GetMapping("monthpage")
	public Response<?> selectMeterUseMonth(Page page){
		List<MeterUseQuantum> meterUseMonths = meterUseQuantumService.selectMeterUseMonth(page);
		page.setData(meterUseMonths);
		return Response.ok(page);
	}
	
	@GetMapping("yearpage")
	public Response<?> selectMeterUseYear(Page page){
		List<MeterUseQuantum> meterUseYears = meterUseQuantumService.selectMeterUseYear(page);
		page.setData(meterUseYears);
		return Response.ok(page);
	}
	
	@GetMapping("/export")
	public ModelAndView historyExpert(MeterUseQuantum useQuantum){
		ExportUtil exprot = null;
		try {
			String[] headers = {"房间号","房间名称","客户姓名","能源类型","表号","时标","本次止码","上次止码","倍率","回路类型","当前用量","付费类型","金额（元）"};
			String[] ENheaders = {"Room No.","Room Name","Username","Energy type","Meter No.","Time Mark","Current reading","Last reading","multiplying power","Loop type","current consumption","Pay type","Amount"};
			String[] fields = {"roomNumber","roomName","customerRealName","meterCateg","meterAddr", "freezeTd","readValue","preReadValue","multiple","loopTypeStr","useValue","controlType","moneyNowStr"};
 			List<MeterUseQuantum> dataList = meterUseQuantumService.historyExpert(useQuantum);
 			Boolean isEnExcel=!StringUtils.isEmpty(useQuantum.getIsEnExcel());
 			String a="";
			if(("day").equals(useQuantum.getDateType())){
				a="历史日数据统计用量";
				if(isEnExcel){
					a="History Daily Data Consumption  Statistics";
				}
			}else if(("month").equals(useQuantum.getDateType())){
				a="历史月数据统计用量";
				if(isEnExcel){
					a="History Monthly Data Consumption  Statistics";
				}
			}else if(("year").equals(useQuantum.getDateType())){
				a="历史年数据统计用量";
				if(isEnExcel){
					a="History Yearly Data Consumption  Statistics";
				}
			}else if(("hour").equals(useQuantum.getDateType())){
				a="历史小时数据统计用量";
				if(isEnExcel){
					a="History Hourly Data Consumption  Statistics";
				}
			}
			
			for(MeterUseQuantum muq:dataList){
				if("1302".equals(muq.getControlType())){
					muq.setControlType("系统预付费");
					if(isEnExcel){
						muq.setControlType("Prepaid by system");
					}
				}
				if("1303".equals(muq.getControlType())){
					muq.setControlType("系统后付费");
					if(isEnExcel){
						muq.setControlType("Postpaid by system");
					}
				}
				if("1304".equals(muq.getControlType())){
					muq.setControlType("不计费");
					if(isEnExcel){
						muq.setControlType("No charge");
					}
				}
			}
			
			if(dataList == null || dataList.size() <= 0){
				MeterUseQuantum meterUseQuantum = new MeterUseQuantum();
				dataList.add(meterUseQuantum);
			}
			
			MeterUseQuantum q = (MeterUseQuantum) ExcelUtils.loadTotalColumns(dataList,fields[0],"useValue","moneyNowStr");
			if(isEnExcel){
				q.setRoomNumber("Total");
			}
			dataList.add(q);
			if(isEnExcel){
				exprot  = ExcelControllerHelper.simpleExportData(dataList, ENheaders, fields, a,"Historical consumption statistics");
			}else{
				exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, a,"历史用量统计");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("MeterUseLevel export:"+e.getMessage());
		}
		return new ModelAndView(new ViewExcel(), exprot.build());
	}
	
	
	

	
}
