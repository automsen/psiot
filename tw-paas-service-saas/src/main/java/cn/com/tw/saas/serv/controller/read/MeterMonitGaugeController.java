package cn.com.tw.saas.serv.controller.read;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.entity.db.read.VReadLastElec;
import cn.com.tw.saas.serv.service.read.ReadService;

@RestController
@RequestMapping("meterMonitGauge")
public class MeterMonitGaugeController {

	private static Logger logger = LoggerFactory.getLogger(MeterMonitGaugeController.class);

	@Autowired
	private ReadService readService;

//	@RequestMapping(value = "/view/{meterAddr}", method = RequestMethod.GET)
//	@ResponseBody
//	public Response<?> view(@PathVariable("meterAddr") String meterAddr, ModelMap model) {
//		Map<String, Object> result = new HashMap<String, Object>();
//		result = elecRealServiece.selectMeterExtendAndReadData(meterAddr);
//		return Response.ok(result);
//	}

	@GetMapping("data/load")
	@ResponseBody
	public Response<?> dataLoad(VReadLastElec vReadLastElec) {
		VReadLastElec data = new VReadLastElec();
		try {
			// meterMonitCmdService.readMeterValue(meterId,null);
			data = readService.selectReadLastElecView(vReadLastElec);
		} catch (Exception e) {
			e.printStackTrace();
			Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "查询失败！");
		}
		return Response.ok(data);
	}

	@GetMapping("selectLineData")
	@ResponseBody
	public Response<?> selectByAddrAndTd(String meterAddr, String freezeTd, String dataItem, String loopType) {
		try {
			return Response.ok(readService.selectByAddrAndTd(meterAddr, freezeTd, dataItem, loopType));
		} catch (Exception e) {
			logger.error("selectByAddrAndTd error", e.toString());
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "查询失败！");
		}
	}

}
