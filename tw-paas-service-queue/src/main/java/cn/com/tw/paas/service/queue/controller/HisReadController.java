package cn.com.tw.paas.service.queue.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.security.MD5Utils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.queue.entity.ReadHistory;
import cn.com.tw.paas.service.queue.service.ReadDataService;

@RestController
@RequestMapping("read")
public class HisReadController {

	@Autowired
	private ReadDataService readDataService;
	
	@RequestMapping("his")
	public Response<?> queryHisReadByPage(Page page){
		readDataService.queryReadByPage(page);
		return Response.ok(page);
	}
	
	@RequestMapping("history")
	public Response<?> queryHisReadByPage(@RequestParam String appId, @RequestParam String startTime, @RequestParam String endTime, @RequestParam(required = false) String equipNumber, @RequestParam(required = false) String itemCode, @RequestParam int page, @RequestParam int rows){
		Page pageObj = new Page();
		pageObj.setPage(page);
		pageObj.setRows(rows);
		@SuppressWarnings("unchecked")
		Map<String, String> pageMap = (Map<String, String>) pageObj.getParamObj();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		pageMap.put("startTime", formatter.format(new Date(Long.parseLong(startTime))));
		pageMap.put("endTime", formatter.format(new Date(Long.parseLong(endTime))));
		pageMap.put("meterAddr", equipNumber);
		pageMap.put("appId", appId);
		pageMap.put("itemCode", itemCode);
		readDataService.queryReadByPage(pageObj);
		return Response.ok(pageObj);
	}
	
	/**
	 * 查询指定数据，通过时间，仪表
	 * @param appId
	 * @param startTime
	 * @param endTime
	 * @param equipNumber
	 * @param itemCode
	 * @param retnItemCode
	 * @return
	 */
	@RequestMapping("data")
	public Response<?> queryListColNameAndColValByRowKey(@RequestParam String appId, @RequestParam String startTime, @RequestParam String endTime, String equipNumber, String itemCode, @RequestParam(required = false) String retnItemCode){
		List<Map<String, Object>> result = readDataService.queryListColNameAndColValByRowKey(appId, startTime, endTime, equipNumber, retnItemCode, itemCode);
		return Response.ok(result);
	}
	
	@RequestMapping("push")
	public Response<?> queryPushByPage(Page page) {
		readDataService.queryPushByPage(page);
		return Response.ok(page);
	}
	
	/**
	 * 通过某天时间段的止码数据
	 * @param appId
	 * @param curDate
	 * @param meterAddr
	 * @param meterType
	 * @return
	 */
	@RequestMapping("list")
	public Response<?> queryListsRowKey(String appId, String curDate, String meterAddr, String meterType){
		return Response.ok(readDataService.queryListStopValueByRowKey(appId, curDate, meterAddr, meterType));
	}
	
	/**
	 * 通过时间区间获取止码数据
	 * @param appId
	 * @param startTime
	 * @param endTime
	 * @param meterAddr
	 * @return
	 */
	@RequestMapping("listbytime")
	public Response<?> queryListsByTimeRowKey(String appId, String startTime, String endTime, String meterAddr){
		return Response.ok(readDataService.queryListByRowKey(appId, startTime, endTime, meterAddr));
	}
	
	/**
	 * 通过表号默认查询主回路/单回路数据
	 * @param termNo
	 * @return
	 */
	@RequestMapping("last/{termNo}")
	public Response<?> queryPushByTerm(@PathVariable String termNo) {
		StringBuffer sb = new StringBuffer(MD5Utils.digest(termNo)).append("|0");
		return Response.ok(readDataService.queryLastByTermNo(sb.toString()));
	}
	
	/**
	 * 通过表号和回路号 查询主回路或者子回路的数据
	 * @param termNo
	 * @return
	 */
	@RequestMapping("last/{termNo}/{lType}")
	public Response<?> queryPushByTerm(@PathVariable String termNo, @PathVariable String lType) {
		StringBuffer sb = new StringBuffer(MD5Utils.digest(termNo)).append("|").append(lType);
		return Response.ok(readDataService.queryLastByTermNo(sb.toString()));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("last")
	public Response<?> queryLast(@RequestParam(required = false) String equipNumber, @RequestParam(required = false) String itemCode, @RequestParam(required = false) int page, @RequestParam(required = false) int rows) {
		Page pageObj = new Page();
		Map<String, String> pageMap = (Map<String, String>) pageObj.getParamObj();
		pageMap.put("equipNumber", equipNumber);
		pageMap.put("itemCode", itemCode);
		
		JwtInfo info = JwtLocal.getJwt();
		String appId = (String) info.getExt("appId");
		
		if (StringUtils.isEmpty(appId)) {
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "orgId can not be null");
		}
		
		pageMap.put("appId", appId);
		
		if (page != 0) {
			pageObj.setPage(page);
		}
		if (rows != 0) {
			pageObj.setRows(rows);
		}
		return Response.ok(readDataService.queryLastByPage(pageObj));
	}
	
	/**
	 * 通过多仪表查询
	 * @param equipNumber
	 * @param itemCode
	 * @return
	 */
	@RequestMapping("last/multi")
	public Response<?> queryLast(@RequestBody ReadHistory readHistory) {
		
		if (StringUtils.isEmpty(readHistory.getEquipNumbers()) || StringUtils.isEmpty(readHistory.getItemCode())) {
			throw new RequestParamValidException("param can not be null");
		}
		
		List<Map<String, Object>> result = readDataService.queryLastByKeys(readHistory.getEquipNumbers(), readHistory.getItemCode());
		return Response.ok(result);
	}
	
}
