package cn.com.tw.saas.serv.controller.finance;

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
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.service.cust.RoomAccountRecordService;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("fundsSource")
@Api(description = "资金来源统计")
public class FundsSourceAnalysisController {

	@Autowired 
	private RoomAccountRecordService roomAccountRecordService;
	
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<RoomAccountRecord> saasRooms = 
					roomAccountRecordService.roomOrderSourceAnalysis(page) ;
		page.setData(saasRooms);
		return Response.ok(page);
	}
	
	@GetMapping("export")
	public ModelAndView exportExcel(RoomAccountRecord record){
		ExportUtil exprot = null;
		try {
			String[] headers = {"时间","楼栋","房间编号","房间名称","房间类型","现金支付(元)","微信支付(元)","支付宝支付(元)","银行转账(元)","POS转账(元)","合计(元)"};
			String[] ENheaders = {"Time","Build","Room No.","Room Name","Room type","Cash","Wechat","Alipay","Bank transfer","POS","Count"};
			String[] fields = {"createTimeStr","roomFullName","roomNumber","roomName", "roomUse","cashPay","wechatPay","aliPay","unionPay","posPay","money"};
			List<RoomAccountRecord> dataList = roomAccountRecordService.roomOrderSourceExport(record);
			if(dataList == null || dataList.size() <= 0){
				RoomAccountRecord accountRecord = new RoomAccountRecord();
				dataList.add(accountRecord);
			}
			RoomAccountRecord  totalRow = (RoomAccountRecord) ExcelUtils.loadTotalColumns(dataList,"createTimeStr",new String[]{"cashPay","wechatPay", "aliPay","unionPay","posPay","money"});
			if(!StringUtils.isEmpty(record.getIsEnExcel())){
				totalRow.setCreateTimeStr("Total");
			}
			
			dataList.add(totalRow);
			if(StringUtils.isEmpty(record.getIsEnExcel())){
				exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, record.getOrgName()+"资金来源账单","资金来源账单");
			}else{
				exprot  = ExcelControllerHelper.simpleExportData(dataList, ENheaders, fields, record.getOrgName()+"FundSourceBill","FundSourceBill");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(new ViewExcel(), exprot.build());
	}
	
}
