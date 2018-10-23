package cn.com.tw.paas.service.queue.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.com.tw.common.utils.tools.excel.ExcelControllerHelper;
import cn.com.tw.common.utils.tools.excel.ExportUtil;
import cn.com.tw.common.utils.tools.excel.ViewExcel;
import cn.com.tw.common.utils.tools.security.MD5Utils;
import cn.com.tw.paas.service.queue.service.ReadDataService;

@Controller
@RequestMapping("export")
public class ExportController {
	
	@Autowired
	private ReadDataService readDataService;
	
	/**
	 * 分别导出 电表 水表的止码数据，水表显示
	 * @param termNo
	 * @return
	 */
	@RequestMapping("term/last")
	public ModelAndView queryPushByTerm(@RequestParam String orgId, String meterType, String meterAddr) {
		
		List<Map<String, Object>> dataList = null;
		if (!StringUtils.isEmpty(meterAddr)) {
			dataList = new ArrayList<Map<String, Object>>();
			StringBuffer sb = new StringBuffer(MD5Utils.digest(meterAddr)).append("|0");
			Map<String, Object> dataMap = readDataService.queryLastByTermNo(sb.toString());
			dataList.add(dataMap);
		}else {
			String familyColName = "d:oId";
			dataList = readDataService.queryLastByColValue(familyColName, orgId);
		}
		
		dataList = new CopyOnWriteArrayList<Map<String, Object>>(dataList);
		
		String fileName = "仪表实时数据";
		
		String sheetName = "仪表实时数据";
		
		if (StringUtils.isEmpty(meterType)) {
			for(Map<String, Object> dataMap : dataList) {
				String termType = (String) dataMap.get("tTy");
				if ("110000".equals(termType)) {
					dataMap.put("termTypeName", "电表");
					dataMap.put("lastValue", (String) dataMap.get("totalActiveE"));
				}else if ("120000".equals(termType)){
					dataMap.put("termTypeName", "水表");
					dataMap.put("lastValue", (String) dataMap.get("waterFlow"));
				}
			}
			
			String[] headers = {"仪表编号","所属机构","所属应用","仪表类型","本点止码","采集时间","入库时间"};
			String[] fields = {"tNo", "oNm", "aNm", "termTypeName", "lastValue", "rTm:time", "cTm:time"};
			
			ExportUtil exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, fileName, sheetName);
			
			return new ModelAndView(new ViewExcel(), exprot.build());
		} else {
			
			if ("110000".equals(meterType)) {
				
				for(Map<String, Object> dataMap : dataList) {
					String termType = (String) dataMap.get("tTy");
					if (meterType.equals(termType)) {
						dataMap.put("termTypeName", "电表");
						dataMap.put("lastValue", (String) dataMap.get("totalActiveE"));
						fileName = "电表实时数据";
						sheetName = "电表实时数据";
					}
					if (!meterType.equals(termType)) {
						dataList.remove(dataMap);
					}
				}
				
				String[] headers = {"仪表编号","所属机构","所属应用","仪表类型","本点止码","采集时间","入库时间"};
				String[] fields = {"tNo", "oNm", "aNm", "termTypeName", "lastValue", "rTm:time", "cTm:time"};
				
				ExportUtil exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, fileName, sheetName);
				
				return new ModelAndView(new ViewExcel(), exprot.build());
			} else if ("120000".equals(meterType)) {
				for(Map<String, Object> dataMap : dataList) {
					String termType = (String) dataMap.get("tTy");
					if (meterType.equals(termType)) {
						dataMap.put("termTypeName", "水表");
						dataMap.put("lastValue", (String) dataMap.get("waterFlow"));
						dataMap.put("waterBattery", (String) dataMap.get("waterBattery"));
						fileName = "水表实时数据";
						sheetName = "水表实时数据";
					}
					if (!meterType.equals(termType)) {
						dataList.remove(dataMap);
					}
				}
				
				String[] headers = {"仪表编号","所属机构","所属应用","仪表类型","本点止码","电池电量(mV)","采集时间","入库时间"};
				String[] fields = {"tNo", "oNm", "aNm", "termTypeName", "lastValue", "waterBattery", "rTm:time", "cTm:time"};
				
				ExportUtil exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, fileName, sheetName);
				
				return new ModelAndView(new ViewExcel(), exprot.build());
			}
			
			return null;
			
		}
		
		
		/*for(Map<String, Object> dataMap : dataList) {
			
			String termType = (String) dataMap.get("tTy");
			
			if (StringUtils.isEmpty(meterType)) {
				if ("110000".equals(termType)) {
					dataMap.put("termTypeName", "电表");
					dataMap.put("lastValue", (String) dataMap.get("totalActiveE"));
				}else if ("120000".equals(termType)){
					dataMap.put("termTypeName", "水表");
					dataMap.put("lastValue", (String) dataMap.get("waterFlow"));
				}
			} else {
				if (meterType.equals(termType) && "110000".equals(termType)) {
					dataMap.put("termTypeName", "电表");
					dataMap.put("lastValue", (String) dataMap.get("totalActiveE"));
					fileName = "电表实时数据";
					sheetName = "电表实时数据";
				}else if (meterType.equals(termType) && "120000".equals(termType)) {
					dataMap.put("termTypeName", "水表");
					dataMap.put("lastValue", (String) dataMap.get("waterFlow"));
					fileName = "水表实时数据";
					sheetName = "水表实时数据";
				}
				
				if (!meterType.equals(termType)) {
					dataList.remove(dataMap);
				}
			}
			
		}
		
		String[] headers = {"仪表编号","所属机构","所属应用","仪表类型","本点止码","采集时间","入库时间"};
		String[] fields = {"tNo", "oNm", "aNm", "termTypeName", "lastValue", "rTm:time", "cTm:time"};
		
		ExportUtil exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, fileName, sheetName);*/
		
		/*return new ModelAndView(new ViewExcel(), exprot.build());*/
	}

}
