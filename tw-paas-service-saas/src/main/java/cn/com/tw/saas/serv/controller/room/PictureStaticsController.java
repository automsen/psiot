package cn.com.tw.saas.serv.controller.room;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;
import cn.com.tw.saas.serv.service.statistics.MeterUseDayService;
import cn.com.tw.saas.serv.service.statistics.MeterUseHourService;
import cn.com.tw.saas.serv.service.statistics.MeterUseMonthService;
import cn.com.tw.saas.serv.service.statistics.MeterUseQuantumService;
import cn.com.tw.saas.serv.service.statistics.MeterUseYearService;

/**
 * 柱状图比对页面的控制层
 * @author Administrator
 *
 */

@RestController
@RequestMapping("roomUse1")
@Api(description = "柱状图比对页面的控制层接口")
public class PictureStaticsController {

	@Autowired
	private MeterUseDayService meterUseDayService;
	@Autowired
	private MeterUseMonthService meterUseMonthService;
	@Autowired
	private MeterUseYearService meterUseYearService;
	@Autowired
	private MeterUseQuantumService meterUseQuantumService;
	
	
	@GetMapping("")
public Response<?> loadRoomUseByDate1(MeterUseQuantum useQuantum){
		
		if("110000".equals(useQuantum.getMeterCateg())){
			useQuantum.setValueType("totalActiveE");
		}else{
			useQuantum.setValueType("waterFlow");
		}
		
		/**
		 * 根据 数据类型 1.日数据 2.月数据 3.年数据 查
		 */
		List<MeterUseQuantum> quantums = new ArrayList<MeterUseQuantum>();
		if("day".equals(useQuantum.getDateType())){
			quantums = meterUseDayService.selectByDate1(useQuantum);
		}
		if("month".equals(useQuantum.getDateType())){
			quantums = meterUseMonthService.selectByDate1(useQuantum);		
		}
		if("year".equals(useQuantum.getDateType())){
			quantums = meterUseYearService.selectByDate1(useQuantum);
		}
		if("hour".equals(useQuantum.getDateType())){
			quantums=meterUseQuantumService.selectByDate1(useQuantum);
		}
		
		return Response.ok(quantums);
	}
	
	
	
}
