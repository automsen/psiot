package cn.com.tw.saas.serv.controller.read;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.entity.db.read.VReadLastElec;
import cn.com.tw.saas.serv.service.read.ReadService;

@RestController
@RequestMapping("lineLoadStatics")
public class LineLoadStaticsController {

	private static Logger logger = LoggerFactory.getLogger(MeterMonitGaugeController.class);
	
	@Autowired
	private ReadService readService;
	
	@GetMapping("selectLineLoadStaticsData")
	@ResponseBody
	public Response<?> selectByAddrAndTd(String meterAddr, String freezeTd,String freezeTd1, String dataItem) {
		try {
			return Response.ok(readService.selectByAddrAndTd1(meterAddr, freezeTd,freezeTd1, dataItem));
		} catch (Exception e) {
			logger.error("selectByAddrAndTd1 error", e.toString());
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "查询失败！");
		}
	}
	
	@GetMapping("data/load1")
	@ResponseBody
	public Response<?> dataLoad1(String meterAddr,String readTime) {
		VReadLastElec data = new VReadLastElec();
		try {
			// meterMonitCmdService.readMeterValue(meterId,null);
			data = readService.selectReadLastElecView1(meterAddr,readTime);
		} catch (Exception e) {
			e.printStackTrace();
			Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "查询失败！");
		}
		return Response.ok(data);
	}
	
	
}
