package cn.com.tw.saas.serv.controller.cust;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.com.tw.common.utils.tools.excel.ExcelControllerHelper;
import cn.com.tw.common.utils.tools.excel.ExportUtil;
import cn.com.tw.common.utils.tools.excel.ViewExcel;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.ExcelUtils;
import cn.com.tw.saas.serv.entity.db.cust.OrderRecharge;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.entity.terminal.MeterHis;
import cn.com.tw.saas.serv.service.cust.RoomAccountRecordService;
import cn.com.tw.saas.serv.service.room.RoomHisService;
import cn.com.tw.saas.serv.service.terminal.MeterHisService;
import cn.com.tw.saas.serv.service.terminal.MeterService;

@RestController

@RequestMapping("roomAccountRecord")
@Api(description = "房间账户记录接口")
public class RoomAccountRecordController {

	@Autowired
	private RoomAccountRecordService roomAccountRecordService;
	@Autowired
	private MeterService meterService;
	@Autowired
	private MeterHisService meterHisService;
	@Autowired
	private RoomHisService roomHisService;
	
	@GetMapping("page")
	@ApiOperation(value = "分页查询", notes = "")
	public Response<?> selectByPage(Page page) {
		List<RoomAccountRecord> scList = roomAccountRecordService.selectByPage(page);
		for (RoomAccountRecord roomAccountRecord : scList) {
			if(roomAccountRecord.getBalance().toString()!=null){
				BigDecimal  a=roomAccountRecord.getBalance();
				BigDecimal b=a.setScale(2, BigDecimal.ROUND_HALF_UP);
				roomAccountRecord.setBalance(b);
			}
			if(roomAccountRecord.getMoney().toString()!=null){
				BigDecimal d=roomAccountRecord.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
				roomAccountRecord.setMoney(d);
			}
			if(!StringUtils.isEmpty(roomAccountRecord.getDetails())){
				Map<String, String> map = JsonUtils.jsonToPojo(roomAccountRecord.getDetails(), Map.class);
				if(!StringUtils.isEmpty(map)){
					if(!StringUtils.isEmpty(map.get("paice"))){
						roomAccountRecord.setPrice(map.get("paice"));
					}
					if(!StringUtils.isEmpty(map.get("readValue"))){
						roomAccountRecord.setReadValue(map.get("readValue"));
					}
				}
			}
		}
		page.setData(scList);
		return Response.ok(page);
	}
	
	
	//用于充值缴费 账户记录
	@GetMapping("page2")
	@ApiOperation(value = "分页查询", notes = "")
	public Response<?> selectByPage2(Page page) {
		List<RoomAccountRecord> scList = roomAccountRecordService.selectByPage2(page);
		for (RoomAccountRecord roomAccountRecord : scList) {
			if(roomAccountRecord.getBalance().toString()!=null){
				BigDecimal  a=roomAccountRecord.getBalance();
				BigDecimal b=a.setScale(2, BigDecimal.ROUND_HALF_UP);
				roomAccountRecord.setBalance(b);
			}
			if(roomAccountRecord.getMoney().toString()!=null){
				BigDecimal d=roomAccountRecord.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
				roomAccountRecord.setMoney(d);
			}
		}
		page.setData(scList);
		return Response.ok(page);
	}
	
	
	@GetMapping("chargePage")
	@ApiOperation(value = "分页查询", notes = "")
	public Response<?> selectOrderRechargeByPage(Page page){
		List<OrderRecharge> orderRecharges = roomAccountRecordService.selectOrderRechargeByPage(page);
		page.setData(orderRecharges);
		return Response.ok(page);
	}
	
	@PostMapping("count")
	@ApiOperation(value = "统计", notes = "")
	public Response<?> count(@RequestBody Map<String,Object> param) {
		Map<String,Object> result = roomAccountRecordService.count(param);
		return Response.ok(result);
	}
	
	@GetMapping("{id}")
	public Response<?> selectById(@PathVariable String id){
		RoomAccountRecord roomAccountRecord = roomAccountRecordService.selectById(id);
		/**
		 * 查询购买用电量
		 */
		//Meter meter = meterService.selectPriceByRoomId(roomAccountRecord.getRoomId());
		List<MeterHis> meterhis = meterHisService.selectPriceByRoomId(roomAccountRecord.getRoomId());
		for (MeterHis meterhis2 : meterhis) {
			/**
			 * 1.取出roomAccountRecord充值记录里的充值金额
			 */
			BigDecimal money = roomAccountRecord.getMoney();
			
			/**
			 * 3.算出用电量 并塞到返回的对象里面
			 */
			if(StringUtils.isEmpty(meterhis2)){
				roomAccountRecord.setMeterMuchStr("");
			}else{
				/**
				 * 2.取出仪表关联的单价
				 */
				//BigDecimal price1 = meter.getPrice1();
				if(!StringUtils.isEmpty(meterhis2.getPrice1())&&!("0").equals(meterhis2.getPrice1().toString())){
					BigDecimal price1 = meterhis2.getPrice1();
					roomAccountRecord.setMeterMuchStr(money.divide(price1, 2, RoundingMode.HALF_UP).toString());
				}else{
					roomAccountRecord.setMeterMuchStr("");
				}
				
			}
			
		}	
		return Response.ok(roomAccountRecord);
	}
	
	
	
	
	/**
	 * 房间充值明细的导出数据
	 * @param roomAccountRecord
	 * @return
	 */
	@GetMapping("/export")
	public ModelAndView roomAccountRecordExpert(RoomAccountRecord roomAccountRecord){
		ExportUtil exprot = null;
		try {
			String[] headers = {"楼栋","房间号","房间名称","房间类型","充值时间","充值金额（元）","支付方式","充值人姓名","充值人手机号","操作员"};
			String[] ENheaders = {"Building","Room No.","Room","Room type","Recharge time","Recharge amount","Pay Type","Payer","Payer Phone","Operator"};
			String[] fields = {"roomFullName","roomNumber","roomName","roomUse","createTimeStr","money","payModeCode","customerRealname","customerPhone","operatorName"};
			List<RoomAccountRecord> dataList = roomAccountRecordService.roomAccountRecordExpert(roomAccountRecord);
			if(dataList == null || dataList.size() == 0){
				RoomAccountRecord accountRecord = new RoomAccountRecord();
				dataList.add(accountRecord);
			}
			RoomAccountRecord  record = (RoomAccountRecord) ExcelUtils.loadTotalColumns(dataList,"roomFullName",new String[]{"money"});
			if(!StringUtils.isEmpty(roomAccountRecord.getIsEnExcel())){
				record.setRoomFullName("Total");
			}
			dataList.add(record);
			if(StringUtils.isEmpty(roomAccountRecord.getIsEnExcel())){
				exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, "房间充值明细","房间充值明细");
			}else{
				exprot  = ExcelControllerHelper.simpleExportData(dataList, ENheaders, fields, "Recharge details","Recharge details");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(new ViewExcel(), exprot.build());
	}
	
	
	/**
	 * 资金来源分析详情导出
	 * @param roomAccountRecord
	 * @return
	 */
	@GetMapping("/exportFund")
	public ModelAndView sourceOfFundExpert(OrderRecharge orderRecharge){
		ExportUtil exprot = null;
		try {
			String[] headers = {"时间","楼栋","房间编号","房间名称","房间类型","支付方式","支付金额","备注"};
			String[] ENheaders = {"Time","Building","Room No.","Room Name","Room Type","Payment Type","Money","Remark"};
			String[] fields = {"createTimeStr","roomFullName","roomNumber","roomName","roomUse","payModeCode","payMoney","remarks"};
			List<OrderRecharge> dataList = roomAccountRecordService.sourceOfFundExpert(orderRecharge);
			if(dataList == null || dataList.size() == 0){
				OrderRecharge orderRecharge1 = new OrderRecharge();
				dataList.add(orderRecharge1);
			}
			OrderRecharge  record = (OrderRecharge) ExcelUtils.loadTotalColumns(dataList,"createTimeStr",new String[]{"payMoney"});
			if(!StringUtils.isEmpty(orderRecharge.getIsEnExcel())){
				record.setCreateTimeStr("Total");
			}
			dataList.add(record);
			if(StringUtils.isEmpty(orderRecharge.getIsEnExcel())){
				exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, "资金来源分析","资金来源分析");
			}else{
				exprot  = ExcelControllerHelper.simpleExportData(dataList, ENheaders, fields, "FundSourceAnalysis","FundSourceAnalysis");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(new ViewExcel(), exprot.build());
	}
	
	
}
