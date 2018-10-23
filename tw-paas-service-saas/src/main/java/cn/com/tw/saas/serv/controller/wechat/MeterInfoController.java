package cn.com.tw.saas.serv.controller.wechat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.common.utils.cons.ApiTemplateCons;
import cn.com.tw.saas.serv.controller.wechat.vo.RequestParamsVo;
import cn.com.tw.saas.serv.entity.business.command.PageCmdResult;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.read.ReadRecord;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.MeterExtend;
import cn.com.tw.saas.serv.service.command.CmdRecordService;
import cn.com.tw.saas.serv.service.read.ReadService;
import cn.com.tw.saas.serv.service.terminal.MeterService;

/**
 * 仪表查询相关
 * 
 * @author admin
 *
 */
@RestController
@RequestMapping("wechat/meter")
public class MeterInfoController {

	@Autowired
	private CmdRecordService cmdService;
	@Autowired
	private MeterService meterService;
	@Autowired
	private ReadService readService;

	private static Logger logger = LoggerFactory.getLogger(MeterInfoController.class);

	/**
	 * 查询客户所有仪表
	 * 
	 * @param vo
	 * @return
	 */
	@PostMapping("list")
	public Response<?> loadMetersByCustNo(@RequestBody RequestParamsVo vo) {
		Customer param = new Customer();
		if (!StringUtils.isEmpty(vo.getCustomerId())) {
			param.setCustomerId(vo.getCustomerId());
		}
		List<MeterExtend> meters = null;
		try {
			meters = meterService.selectByCustomerForWechat(param);
//			if(meters!= null && meters.size()>0){
//				BigDecimal meterMoney = null;
//				BigDecimal totalMoney = null;
//				for (MeterExtend meterExtend : meters) {
//					if(meterExtend.getBalance()!= null){
//						meterMoney = meterExtend.getBalance();
//					}else{
//						meterMoney = BigDecimal.ZERO;
//					}
//					totalMoney  = meterExtend.getRoomBalance().add(meterMoney);
//					totalMoney = totalMoney.setScale(2); // 2位小数
//					meterExtend.setRoomBalance(totalMoney);
//				}
//			}
			
		} catch (Exception e) {
			return Response.retn(ResultCode.UNKNOW_ERROR, "系统错误！");
		}
		return Response.ok(meters);
	}

	/**
	 * 查询单块仪表
	 * 
	 * @param meterAddr
	 * @return
	 */
	@GetMapping("one/{meterAddr}")
	@ResponseBody
	public Response<?> loadCustMeter(@PathVariable("meterAddr") String meterAddr) {
		if (StringUtils.isEmpty(meterAddr)) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "系统异常");
		}
		Meter param = new Meter();
		param.setMeterAddr(meterAddr);
		try {
			MeterExtend meter = meterService.selectByAddrForWechat(param);
			if (meter == null) {
				return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "仪表编号不匹配");
			}
			return Response.ok(meter);
		} catch (Exception e) {
			logger.error("meterAddr:{} database is undefiend Exception:{}", meterAddr, e.getMessage());
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常");
		}
	}

	/**
	 * 通断控制
	 * 
	 * @param meterAddr
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "switch/{meterAddr}/{status}", method = RequestMethod.POST)
	public Response<?> closeMeter(@PathVariable("meterAddr") String meterAddr, @PathVariable("status") Boolean status) {
		if (StringUtils.isEmpty(meterAddr)) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR);
		}
		try {
			// 断开
			if (status) {
				Map<String, String> requestMap = new HashMap<String, String>();
				requestMap.put("equipNumber", meterAddr);
				PageCmdResult result = cmdService.generateCmd(ApiTemplateCons.switchOff, meterAddr, requestMap);

			}
			// 接通
			else {
				Map<String, String> requestMap = new HashMap<String, String>();
				requestMap.put("equipNumber", meterAddr);
				PageCmdResult result = cmdService.generateCmd(ApiTemplateCons.switchOn, meterAddr, requestMap);
			}
			return Response.ok();
		} catch (Exception e) {
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR);
		}
	}
	
	/**
	 * 历史读数
	 * @param vo
	 * @return
	 */
	@PostMapping("readHis")
	public Response<?> selectReadHis(@RequestBody RequestParamsVo vo){
		String freezeTd = vo.getFreezeTd();
		String meterAddr = vo.getMeterAddr();
		String dataItem = vo.getDataItem();
		List<ReadRecord> records = null;
		if(StringUtils.isEmpty(freezeTd)|| StringUtils.isEmpty(meterAddr)||StringUtils.isEmpty(dataItem)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR);
		}
		try {
			records = readService.selectByAddrAndTd(meterAddr,freezeTd,dataItem,"0");
			return Response.ok(records);
		} catch (Exception e) {
			return Response.retn(ResultCode.UNKNOW_ERROR, "系统错误！");
		}
	}

}
