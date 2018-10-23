package cn.com.tw.saas.serv.controller.supplementary;


import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.service.statistics.MeterUseQuantumService;

/**
 * 修补 用量缺失数据
 * @author Administrator
 *
 */
@RestController
@RequestMapping("sUse")
public class SupplementaryUseDataContronller {

	@Autowired
	private MeterUseQuantumService meterUseQuantumService;
	
	@PostMapping()
	public Response<?> supplementaryDayUseData(HttpServletRequest request){
		String jsonStr = null;
		try {
			jsonStr = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
			JSONObject resultJSON = JSONObject.parseObject(jsonStr);
			String freeTd = resultJSON.getString("freeTd");
			if(freeTd.length() != 8){
				return Response.retn("111111","时标错误");
			}
			meterUseQuantumService.meterUseDayAndMonthAndYearJob(freeTd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.ok();
	}
	
	
}
