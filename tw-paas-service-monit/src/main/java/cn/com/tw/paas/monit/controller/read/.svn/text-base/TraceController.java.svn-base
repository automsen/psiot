package cn.com.tw.paas.monit.controller.read;

import java.util.List;

import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.tools.http.HttpPostReq;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.common.utils.cons.code.MonitResultCode;
import cn.com.tw.paas.monit.entity.business.read.ReadHistoryExtend;
import cn.com.tw.paas.monit.entity.db.read.ReadLast;
import cn.com.tw.paas.monit.service.read.ReadService;

/**
 * 智能手环定位
 * @author Administrator
 *
 */
@RestController
@RequestMapping("trace")
public class TraceController {
	
	@Autowired
	private ReadService readService;
	
	@Value("${http.proxy.hostname:}")
	private String hostName;
	
	@Value("${http.proxy.port:0}")
	private int port;
	
	/**
	 * 实时位置
	 * @return
	 */
	@GetMapping("{meterAddr}")
	public Response<?> realTime(@PathVariable String meterAddr){
		if(StringUtils.isEmpty(meterAddr)){
			Response.retn(MonitResultCode.PARAMETER_NULL_ERROR, "");
		}
		List<ReadLast> readLasts = readService.selectRealTimeTrace(meterAddr);
		return Response.ok(readLasts);
	}
	
	/**
	 * 轨迹
	 * @param meterAddr
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@GetMapping("{meterAddr}/{startTime}/{endTime}")
	public Response<?> trace(@PathVariable String meterAddr,@PathVariable String startTime,@PathVariable String endTime){
		ReadHistoryExtend readHistoryExtend = new ReadHistoryExtend();
		if(!StringUtils.isEmpty(startTime)){
			readHistoryExtend.setStartTimeStr(startTime);
		}
		if(!StringUtils.isEmpty(endTime)){
			readHistoryExtend.setEndTimeStr(endTime);
		}
		readHistoryExtend.setMeterAddr(meterAddr);
		List<ReadHistoryExtend> historyExtends = readService.selectTrace(readHistoryExtend);
		return Response.ok(historyExtends);
	}
	
	/**
	 * 获取百度地图经纬度
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@GetMapping("getXY")
	public Response<?> exceResult(String latitude,String longitude) {
		String result = "";
		try {
			StringEntity entity = new StringEntity("","utf-8");//解决中文乱码问题    
	        entity.setContentEncoding("UTF-8");    
	        entity.setContentType("application/json");
	        result = new HttpPostReq("http://api.map.baidu.com/geoconv/v1/?coords="+longitude+","+latitude+"&from=1&to=5&ak=q4fsWkGdKzVhSQbTI4GSXtz3dS83HpMB", null, entity).setProxy(hostName, port).excuteReturnStr();
		}catch (Exception  e) {
			e.printStackTrace();
		}
		return Response.ok(result);
		
	}

}
