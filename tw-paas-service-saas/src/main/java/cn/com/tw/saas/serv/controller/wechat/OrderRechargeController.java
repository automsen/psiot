package cn.com.tw.saas.serv.controller.wechat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.controller.wechat.vo.RequestParamsVo;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.service.cust.CustomerService;
import cn.com.tw.saas.serv.service.cust.RoomAccountRecordService;
import cn.com.tw.saas.serv.service.terminal.MeterService;

@RestController
@RequestMapping("wechat/rechargeRecord")
public class OrderRechargeController {
	
	@Autowired
	private RoomAccountRecordService roomAccountRecordService;
	@Autowired
	private MeterService meterService;
	@Autowired
	private CustomerService customerService;
	/**
	 * 查询充值记录
	 * @param page
	 * @return
	 */
	@PostMapping("list")
	public Response<?> loadRechargeList(@RequestBody RequestParamsVo vo){
		List<RoomAccountRecord> results =null;
		try {
			if (!StringUtils.isEmpty(vo.getMeterAddr())){
				Meter meter = meterService.selectByMeterAddr(vo.getMeterAddr());
				RoomAccountRecord param = new RoomAccountRecord();
				param.setOrgId(meter.getOrgId());
				param.setRoomAccountId(meter.getAccountId());
				param.setOrderTypeCode("1");  // 充值订单
				param.setStatus(new Byte("1")); // 充值成功的
				results = roomAccountRecordService.selectByEntity(param);
			}
			else if (!StringUtils.isEmpty(vo.getCustomerId())){
				Customer cust = customerService.selectById(vo.getCustomerId());
				RoomAccountRecord param = new RoomAccountRecord();
				param.setOrgId(cust.getOrgId());
				param.setCustomerId(vo.getCustomerId());
				param.setOrderTypeCode("1");  // 充值订单
				param.setStatus(new Byte("1")); // 充值成功的
				results = roomAccountRecordService.selectByEntity(param);
			}
			else {
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "查询条件不足");
			}
		} catch (Exception e) {
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统错误！");
		}
		return Response.ok(results);
	}
	
}
