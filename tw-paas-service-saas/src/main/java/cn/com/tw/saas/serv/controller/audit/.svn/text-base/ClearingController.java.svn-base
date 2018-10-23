package cn.com.tw.saas.serv.controller.audit;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.excel.ExcelControllerHelper;
import cn.com.tw.common.utils.tools.excel.ExportUtil;
import cn.com.tw.common.utils.tools.excel.ViewExcel;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.RegExp;
import cn.com.tw.saas.serv.entity.audit.Clearing;
import cn.com.tw.saas.serv.entity.db.cust.DeductOrder;
import cn.com.tw.saas.serv.entity.db.rule.EnergyPrice;
import cn.com.tw.saas.serv.entity.room.RoomHis;
import cn.com.tw.saas.serv.entity.terminal.MeterHis;
import cn.com.tw.saas.serv.service.audit.ClearingService;
import cn.com.tw.saas.serv.service.audit.DeductOrderService;
import cn.com.tw.saas.serv.service.room.RoomHisService;
import cn.com.tw.saas.serv.service.room.RoomService;
import cn.com.tw.saas.serv.service.terminal.MeterHisService;

@RestController
@RequestMapping("clearing")
@Api(description = "结算")
public class ClearingController {

	@Autowired
	private ClearingService clearingSrevice;
	
	@Autowired
	private RoomService roomService;
	@Autowired
	private RoomHisService  roomHisService;
	@Autowired
	private MeterHisService  meterHisService;
	@Autowired
	private DeductOrderService  deductOrderService;
	
	
	
	/**
	 * 结算记录生成  老的基板的
	 * @return
	 */
	@PostMapping("")
	public Response<?> roomClearing(@RequestBody List<Clearing> clearings){
		try {
			clearingSrevice.roomClearingAdd(clearings);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 结算记录生成
	 * @return
	 */
	@PostMapping("/one")
	public Response<?> roomClearing(@RequestBody Clearing clearing){
		
		if(!StringUtils.isEmpty(clearing.getPayeePhone())){
			if (!clearing.getPayeePhone().matches(RegExp.phoneExp)){
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "保存失败！手机号格式错误");
			}
		}
		
		try {
			clearingSrevice.roomClearingAdd(clearing);
			
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 结算审核页面
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<Clearing> clearings = clearingSrevice.selectByPage(page);
		page.setData(clearings);
		return Response.ok(page);
	}
	
	@PostMapping("count")
	@ApiOperation(value = "统计", notes = "")
	public Response<?> count(@RequestBody Map<String,Object> param) {
		Map<String,Object> result = clearingSrevice.count(param);
		return Response.ok(result);
	}
	
	
	@GetMapping("")
	public Response<?> selectByEntity(Clearing clearing){
		List<Clearing> clearings = clearingSrevice.selectByEntity(clearing);
		return Response.ok(clearings);
	}
	
	/**
	 * 审核查询
	 * @param clearing
	 * @return
	 */
	@GetMapping("audit")
	public Response<?> selectClearingAudit(Clearing clearing){
		List<Clearing> clearings = clearingSrevice.selectClearingAudit(clearing);
		return Response.ok(clearings);
	}
	
	/**
	 * 结算通过操作
	 * @return
	 */
	@PutMapping("")
	public Response<?> passClearing(@RequestBody List<Clearing> clearings){
		try {
			clearingSrevice.passClearing(clearings);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 结算驳回操作
	 * @return
	 */
	@PutMapping("back")
	public Response<?> rejectClearing(@RequestBody List<Clearing> clearings){
		try {
			clearingSrevice.rejectClearing(clearings);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	@GetMapping("detail")
	public Response<?> selectClearing(Clearing clearing){
		Map<String,Object> result = clearingSrevice.selectClearing(clearing);
		return Response.ok(result);
	}
	
	/**
	 * 查历史房间账户记录
	 * @param clearing
	 * @return
	 */
	@GetMapping("hisDetail")
	public Response<?> selectHisClearing(Clearing clearing){
		Map<String,Object> result = clearingSrevice.selectHisClearing(clearing);
		return Response.ok(result);
	}
	
	/**
	 * 导出页面
	 * @param clearing
	 * @return
	 */
	@GetMapping("/export")
	public ModelAndView clearExpert(Clearing clearing){
		ExportUtil exprot = null;
		try {
			String[] headers = {"提交时间","楼栋","房间","房间类型","状态","客户信息","提交人"};
			String[] ENheaders = {"Submission time","Build","Room","Room type","Status","User info","Submitter"};
			String[] fields = {"createTimeStr","regionFullName","roomName","roomUse","clearingState", "custInfo","submitName"};
			List<Clearing> dataList = clearingSrevice.clearingExpert(clearing);
			if(StringUtils.isEmpty(clearing.getIsEnExcel())){
				exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, "房间结算","房间结算");
			}else{
				exprot  = ExcelControllerHelper.simpleExportData(dataList, ENheaders, fields, "Room Settlement","Room Settlement");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(new ViewExcel(), exprot.build());
	}
	
	
	@GetMapping("{id}")
	public Response<?> selectById(@PathVariable String id){
		Clearing clearing=clearingSrevice.selectById(id);
		
		
		if(clearing.getClearingState()!=null){
			if("0".equals(clearing.getClearingState())){
				clearing.setClearingState("初始状态");
			}else if("1".equals(clearing.getClearingState())){
				clearing.setClearingState("通过");
			}else if("2".equals(clearing.getClearingState())){
				clearing.setClearingState("退回");
			}else if("3".equals(clearing.getClearingState())){
				clearing.setClearingState("取消");
			}
		}
		
		if(clearing.getClearingMoney()!=null){
			clearing.setClearingMoney(clearing.getClearingMoney());
		}else{
			BigDecimal a=new BigDecimal(0);
			clearing.setClearingMoney(a);
		}
		
		
		if(clearing.getRoomBalance()!=null){
			clearing.setRoomBalance(clearing.getRoomBalance());
		}else{
			BigDecimal a=new BigDecimal(0);
			clearing.setRoomBalance(a);
		}
		
		
		//根据账户Id获取获取开户日期和结算日期
		RoomHis roomHis1 = new RoomHis();
		roomHis1.setAccountId(clearing.getAccountId());
		roomHis1.setOrgId(clearing.getOrgId());
		RoomHis roomHis = roomHisService.selectByEntity1(roomHis1); 
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		/**
		 * 往前台塞入开户时间和结束时间
		 */ 
		if(!StringUtils.isEmpty(roomHis.getStartTime())){
			clearing.setsTimeStr(sdf.format(roomHis.getStartTime()));
		}else{
			clearing.setsTimeStr(sdf.format(roomHis.getCreateTime()));	
		}
		
		if(!StringUtils.isEmpty(roomHis.getEndTime())){
			clearing.seteTimeStr(sdf.format(roomHis.getEndTime()));
		}else{
			clearing.seteTimeStr(sdf.format(roomHis.getUpdateTime()));
		}
			
		//根据id获取开户止码和结束停码
		MeterHis meterHis1=new MeterHis();
		meterHis1.setOrgId(clearing.getOrgId());
		meterHis1.setAccountId(clearing.getAccountId());
		meterHis1.setMeterAddr(clearing.getMeterAddr());
		MeterHis meterHis=meterHisService.selectByEntity1(meterHis1);
		BigDecimal a=new BigDecimal("0");
		if(meterHis!=null){
			if(meterHis.getStartRead()!=null){
				clearing.setStartRead(meterHis.getStartRead());
			}else{
				clearing.setStartRead(a);
			}
			
			if(meterHis.getBalanceUpdateRead()!=null){
				clearing.setBalanceUpdateRead(meterHis.getBalanceUpdateRead());
			}else{
				clearing.setBalanceUpdateRead(a);
			}
		}else{
			clearing.setStartRead(a);
			clearing.setBalanceUpdateRead(a);
		}
		
			//根据id获取单价,对应t_saas_deduct_order
			DeductOrder deductOrder1=new DeductOrder();
			//获取机构id
			deductOrder1.setOrgId(clearing.getOrgId());
			//创建规则表单价对象
			EnergyPrice energyPrice=new EnergyPrice();
			//将规则表t_saas_deduct_order中的id传给t_saas_rule_energy_price中
			energyPrice.setPriceId(deductOrder1.getPriceId());
			//将历史房间表t_saas_room_history中的账号id传入到deductOrder1中的房间Id中
			deductOrder1.setRoomAccountId(roomHis1.getAccountId());
			//将历史电表t_saas_meter_history中的账户id subAccountId  传入到deductOrder1中的子账户电表id  subAccountId中
			deductOrder1.setSubAccountId(meterHis1.getSubAccountId());
			//将关联表id传入到deductOrder之中通过sql语句进行查询
			List<DeductOrder> deductOrder=deductOrderService.selectByEntity1(deductOrder1);
			if(deductOrder!=null && deductOrder.size() > 0){
			   for (DeductOrder deductOrder2 : deductOrder) {
				if(deductOrder2!=null){
					clearing.setPrice(deductOrder2.getPrice());
				}else{
					
					clearing.setPrice("");
				}
			   }
			}
			else{
				MeterHis meterHis2=new MeterHis();
				//获取结算页面的机构id
				meterHis2.setOrgId(clearing.getOrgId());
				//获取结算页面的账户id
				meterHis2.setAccountId(clearing.getAccountId());
				//将历史电表中的账户id accountId传递给房间之中
				//roomHis1.setAccountId(meterHis2.getAccountId());
				//创建规则表单价对象
				//EnergyPrice energyPrice2=new EnergyPrice();
				//将历史电表t_saas_meter_history中的priceId传递给规则表t_saas_rule_energy_price之中
				//energyPrice2.setPriceId(meterHis2.getPriceId());
				//
				List<MeterHis> meterHis3=meterHisService.selectByEntity2(meterHis2);
				for (MeterHis meterHis4 : meterHis3) {
					if(meterHis4!=null&&meterHis3.size()>0){
						clearing.setPrice(meterHis4.getPrice());
					}else{
						clearing.setPrice("");
					}
				}
			}
		return Response.ok(clearing);
	}
	
	
}
