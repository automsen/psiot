package cn.com.tw.saas.serv.controller.org;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.com.tw.common.utils.tools.excel.ExcelControllerHelper;
import cn.com.tw.common.utils.tools.excel.ExportUtil;
import cn.com.tw.common.utils.tools.excel.ViewExcel;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.ExcelUtils;
import cn.com.tw.saas.serv.entity.org.OrgAccountRecord;
import cn.com.tw.saas.serv.service.org.OrgAccountRecordService;

/**
 * 机构账户记录
 * @author Administrator
 *
 */
@RestController
@RequestMapping("orgaccount")
public class OrgAccountRecordController {

	@Autowired
	private OrgAccountRecordService orgAccountRecordService;
	
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<OrgAccountRecord> orgAccountRecords = orgAccountRecordService.selectByPage(page);
		page.setData(orgAccountRecords);
		return Response.ok(page);
	}
	
	@GetMapping("/export")
	public ModelAndView customerExpert(OrgAccountRecord orgAccountRecord){
		ExportUtil exprot = null;
		try {
			String[] headers = {"日期","期初余额（元）","收入（元）","收入笔数","支出（元）","支出笔数","日终余额（元）"};
			String[] ENheaders = {"Date","Beginning balance","Income","Income No.s","Expense","Expense No.s","End-of-day balance"};
			String[] fields = {"dayTime","minMoney","dayMoney","revenueNumber", "disburseMoneyStr","disburseNumber","balance"};
			List<OrgAccountRecord> dataList = orgAccountRecordService.orgAccountRecordExpert(orgAccountRecord);
			if(dataList == null || dataList.size() <= 0){
				OrgAccountRecord accountRecord = new OrgAccountRecord();
				dataList.add(accountRecord);
			}
			OrgAccountRecord  record = (OrgAccountRecord) ExcelUtils.loadTotalColumns(dataList,"dayTime",new String[]{"dayMoney","revenueNumber", "disburseMoneyStr","disburseNumber"});
			if(!StringUtils.isEmpty(orgAccountRecord.getIsEnExcel())){
				record.setDayTime("Total");
			}
			
			dataList.add(record);
			
			if(StringUtils.isEmpty(orgAccountRecord.getIsEnExcel())){
				exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, orgAccountRecord.getOrgName()+"资金账单","资金账单");
			}else{
				exprot  = ExcelControllerHelper.simpleExportData(dataList, ENheaders, fields, orgAccountRecord.getOrgName()+"Bill","Bill");
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(new ViewExcel(), exprot.build());
	}
	
	
	
}
