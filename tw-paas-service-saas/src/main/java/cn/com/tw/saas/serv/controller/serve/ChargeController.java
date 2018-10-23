package cn.com.tw.saas.serv.controller.serve;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.service.cust.ChargeService;
import cn.com.tw.saas.serv.service.cust.RoomAccountRecordService;

@RestController
@RequestMapping("charge")
@Api(description = "充值接口")
public class ChargeController {
	
	@Autowired
	private ChargeService chargeService;
	@Autowired
	private RoomAccountRecordService roomAccountRecordService;
	
	@ApiOperation(value = "房间账户充值", notes = "")
	@PostMapping("")
	public Response<?> charge(@RequestBody CustRoomParam param){
		if(BigDecimal.ZERO.compareTo(param.getRoomChargeMoney())==1){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "充值金额不能为负数");
		}
		Map<String,Object> result = chargeService.charge(param);
		return Response.ok();
	}
	
	@ApiOperation(value = "房间账户充值撤销", notes = "")
	@PostMapping("repeal/{id}")
	public Response<?> repeal(@PathVariable("id") String id){
		if(StringUtils.isEmpty(id)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"系统异常！");
		}
		RoomAccountRecord record = null;
		try {
			record = roomAccountRecordService.selectById(id);
			// 不是充值订单，不能撤销
			if(!record.getOrderTypeCode().equals("1")){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"系统异常！");
			}
			// 判断流水是否是今天最新的流水
			RoomAccountRecord queryCord = new RoomAccountRecord();
			queryCord.setOrderTypeCode("1"); // 充值类型
			queryCord.setOrgId(record.getOrgId());  
			queryCord = roomAccountRecordService.selectOneRecordByCondition(queryCord);
			/**
			 *  不是当前房间充值记录的最后一条
			 */
			
			Calendar todayStart=Calendar.getInstance();
			todayStart.setTime(new Date());
			todayStart.set(Calendar.HOUR_OF_DAY,0);
			todayStart.set(Calendar.MINUTE, 0);
			todayStart.set(Calendar.SECOND, 0);
			todayStart.set(Calendar.MILLISECOND, 0);
			
			Calendar todayEnd=Calendar.getInstance();
			todayEnd.setTime(new Date());
			todayEnd.set(Calendar.HOUR_OF_DAY,23);
			todayEnd.set(Calendar.MINUTE, 59);
			todayEnd.set(Calendar.SECOND, 59);
			todayEnd.set(Calendar.MILLISECOND, 999);
			
			if(!id.equals(queryCord.getId())){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"系统异常！刷新后重试");
			}else if(queryCord.getCreateTime().getTime()<todayStart.getTimeInMillis()
					||queryCord.getCreateTime().getTime()>todayEnd.getTimeInMillis()){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"系统异常！刷新后重试");
			}
			/**
			 *  只有状态为正常的流水才能撤销
			 */
			if(record == null || !new Byte("1").equals(record.getStatus())){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"撤销异常！刷新后重试");
			}
			chargeService.repeal(id);
			return Response.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR,"系统异常！");
		}
	}

}
