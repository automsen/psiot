/*package cn.com.tw.paas.monit.controller.api;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.db.base.CmdResult;
import cn.com.tw.paas.monit.entity.db.base.Ins;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.service.inn.CmdIssueService;
import cn.com.tw.paas.monit.service.inn.callback.CmdCallback;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("api")
public class InstructionIssuedController extends IssueValidateController{

	private Logger logger = LoggerFactory.getLogger(InstructionIssuedController.class);
	
	@Resource(name="baseCallback")
	private CmdCallback baseCallback;
	
	@Resource(name="rechargeCallback")
	private CmdCallback rechargeCallback;
	
	@Autowired
	private CmdIssueService cmdIssueService;

	*//**
	 * 仪表 充值
	 * @param equipNumber 仪表地址
	 * @param rechargeMoney 充值金额  单位 ：元
	 * @param payNumber  充值次数
	 * @param appId  应用ID
	 * @return
	 *//*
	@PostMapping("recharge")
	public Response<?> issued(HttpServletRequest request){
		
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>#recharge start#>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		CmdResult result = new CmdResult();
		Response<?> response = null;
		String cmdCode = "recharge_cmd";
		
		Map<String,String> params = (Map<String, String>) request.getAttribute("params");
		String meterAddr = params.get("equipNumber");
		String rechargeMoney = params.get("rechargeMoney");
		String payNumber = params.get("payNumber");
		OrgApplication application =  (OrgApplication) request.getAttribute("application");
		
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(meterAddr);
		// 关键字段判空
		if(StringUtils.isEmpty(meterAddr)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber can not be null!");
		}
		if(StringUtils.isEmpty(rechargeMoney)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"rechargeMoney can not be null!");
		}
		if(StringUtils.isEmpty(payNumber)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"payNumber can not be null!");
		}
		// 不是数字
		if(!isNumeric(rechargeMoney)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"rechargeMoney must be Numeric!");
		}
		// 不是整数
		if(!isInteger(payNumber)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"payNumber must be Integer!");
		}
		
		TerminalEquip terminalEquip  = null;
		List<Ins> ins = null;
		try {
			// 验证仪表是否存在
			terminalEquip = valiedateEquipment( meterAddr, application.getAppKey());
			if(terminalEquip == null){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber not find!");
			}
			*//**
			 *  TODO: handle exception
			 *//*
			// 判断数据库中是否包含相关命令和指令 
			ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, cmdCode);
			//首次充值
			if("1".equals(payNumber)){
				cmdCode = "first_recharge_cmd";
			}
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			loadCmdIns(cmd, ins, rechargeMoney +","+payNumber);
			cmdIssueService.cmdIssue(cmd, rechargeCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response = Response.retn(ResultCode.PARAM_VALID_ERROR,e.getMessage());
		}
		return response;
	}
	
	

	*//**
	 * 取消充值
	 * @param equipNumber 仪表地址
	 * @param payNumber 充值次数
	 * @return
	 *//*
	@PostMapping("undoReload")
	public Response<?> undoReload(HttpServletRequest request){
		logger.info("undoReload START.........................");
		Response<?> response = null;
		CmdResult result = new CmdResult();
		final String cmdCode = "undo_reload";
		Map<String,String> params = (Map<String, String>) request.getAttribute("params");
		String meterAddr = params.get("equipNumber");
		String payNumber = params.get("payNumber");
		String money = params.get("money");
		if(StringUtils.isEmpty(money)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"money can not be null!");
		}
		OrgApplication application =  (OrgApplication) request.getAttribute("application");
		
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(meterAddr);
		// 关键字段判空
		if(StringUtils.isEmpty(meterAddr)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber can not be null!");
		}
		if(StringUtils.isEmpty(payNumber)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"payNumber can not be null!");
		}
		TerminalEquip terminalEquip  = null;
		List<Ins> ins = null;
		try {
			// 验证仪表是否存在
			terminalEquip = valiedateEquipment( meterAddr, application.getAppKey());
			if(terminalEquip == null){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber not find!");
			}
			*//**
			 *  TODO: handle exception
			 *//*
			// 判断数据库中是否包含相关命令和指令 
			ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, cmdCode);
			
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd,ins,money+","+payNumber);
			cmdIssueService.cmdIssue(cmd, baseCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response =  Response.retn(ResultCode.PARAM_VALID_ERROR,e.getMessage());
		}
		return response;
	}
	
	
	
	
	*//**
	 * 清0操作
	 * @param equipNumber 仪表地址
	 * @param payNumber 充值次数
	 * @return
	 *//*
	@PostMapping("clearElecPayNum")
	public Response<?> clearElecPayNum(HttpServletRequest request){
		logger.info("clearElecPayNum START.........................");
		Response<?> response = null;
		CmdResult result = new CmdResult();
		final String cmdCode = "clear_elec_pay_num";
		Map<String,String> params = (Map<String, String>) request.getAttribute("params");
		String meterAddr = params.get("equipNumber");
		String payNumber = params.get("payNumber");
		OrgApplication application =  (OrgApplication) request.getAttribute("application");
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(meterAddr);
		// 关键字段判空
		if(StringUtils.isEmpty(meterAddr)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber can not be null!");
		}
		if(StringUtils.isEmpty(payNumber)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"payNumber can not be null!");
		}
		TerminalEquip terminalEquip  = null;
		List<Ins> ins = null;
		try {
			// 验证仪表是否存在
			terminalEquip = valiedateEquipment( meterAddr, application.getAppKey());
			if(terminalEquip == null){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber not find!");
			}
			*//**
			 *  TODO: handle exception
			 *//*
			// 判断数据库中是否包含相关命令和指令 
			ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd,ins,"0,"+payNumber);
			cmdIssueService.cmdIssue(cmd, baseCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response =  Response.retn(ResultCode.PARAM_VALID_ERROR,e.getMessage());
		}
		return response;
	}
	
	

	
	*//**
	 * 常用数据读取
	 * @param equipNumber 仪表地址
	 * @return
	 *//*
	@PostMapping("readCommon")
	public Response<?> readCommon(HttpServletRequest request){
		logger.info("readCommon START.........................");
		Response<?> response = null;
		CmdResult result = new CmdResult();
		final String cmdCode = "read_common";
		Map<String,String> params = (Map<String, String>) request.getAttribute("params");
		String meterAddr = params.get("equipNumber");
		OrgApplication application =  (OrgApplication) request.getAttribute("application");
		
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(meterAddr);
		// 关键字段判空
		if(StringUtils.isEmpty(meterAddr)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber can not be null!");
		}
		TerminalEquip terminalEquip  = null;
		List<Ins> ins = null;
		try {
			// 验证仪表是否存在
			terminalEquip = valiedateEquipment( meterAddr, application.getAppKey());
			
			
			if(terminalEquip == null){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber not find!");
			}
			*//**
			 *  TODO: handle exception
			 *//*
			// 判断数据库中是否包含相关命令和指令 
			ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, cmdCode);
			
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd,ins);
			cmdIssueService.cmdIssue(cmd, baseCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response =  Response.retn(ResultCode.PARAM_VALID_ERROR,e.getMessage());
		}
		return response;
	}
	
	
	
	*//**
	 * 仪表开闸
	 * @param equipNumber 仪表地址
	 * @return
	 *//*
	@PostMapping("switchOn")
	public Response<?> switchOn(HttpServletRequest request){
		logger.info("switchOn START.........................");
		CmdResult result = new CmdResult();
		Response<?> response = null;
		final String cmdCode = "switch_on";
		Map<String,String> params = (Map<String, String>) request.getAttribute("params");
		String meterAddr = params.get("equipNumber");
		OrgApplication application =  (OrgApplication) request.getAttribute("application");
		
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(meterAddr);
		// 关键字段判空
		if(StringUtils.isEmpty(meterAddr)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber can not be null!");
		}
		TerminalEquip terminalEquip  = null;
		List<Ins> ins = null;
		try {
			// 验证仪表是否存在
			terminalEquip = valiedateEquipment( meterAddr, application.getAppKey());
			
			
			if(terminalEquip == null){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber not find!");
			}
			*//**
			 *  TODO: handle exception
			 *//*
			// 判断数据库中是否包含相关命令和指令 
			ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, cmdCode);
			
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd,ins);
			cmdIssueService.cmdIssue(cmd, baseCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response =  Response.retn(ResultCode.PARAM_VALID_ERROR,e.getMessage());
		}
		return response;
	}
	
	
	
	*//**
	 * 仪表关闸
	 * @param equipNumber 仪表地址
	 * @return
	 *//*
	@PostMapping("switchOff")
	public Response<?> switchOff(HttpServletRequest request){
		logger.info("switch_off START.........................");
		CmdResult result = new CmdResult();
		Response<?>  response = null;
		final String cmdCode = "switch_off";
		Map<String,String> params = (Map<String, String>) request.getAttribute("params");
		String meterAddr = params.get("equipNumber");
		OrgApplication application =  (OrgApplication) request.getAttribute("application");
		
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(meterAddr);
		// 关键字段判空
		if(StringUtils.isEmpty(meterAddr)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber can not be null!");
		}
		TerminalEquip terminalEquip  = null;
		List<Ins> ins = null;
		try {
			// 验证仪表是否存在
			terminalEquip = valiedateEquipment( meterAddr, application.getAppKey());
			
			
			if(terminalEquip == null){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber not find!");
			}
			*//**
			 *  TODO: handle exception
			 *//*
			// 判断数据库中是否包含相关命令和指令 
			ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, cmdCode);
			
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd,ins);
			cmdIssueService.cmdIssue(cmd, baseCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response =  Response.retn(ResultCode.PARAM_VALID_ERROR,e.getMessage());
		}
		return response;
	}
		
	*//**
	 * 电表保电
	 * @param equipNumber 仪表地址
	 * @return
	 *//*
	@PostMapping("switchKeepOn")
	public Response<?> switchKeepOn(HttpServletRequest request){
		logger.info("switch_keep_on START.........................");
		CmdResult result = new CmdResult();
		Response<?>  response = null;
		final String cmdCode = "switch_keep_on";
		Map<String,String> params = (Map<String, String>) request.getAttribute("params");
		String meterAddr = params.get("equipNumber");
		OrgApplication application =  (OrgApplication) request.getAttribute("application");
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(meterAddr);
		// 关键字段判空
		if(StringUtils.isEmpty(meterAddr)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber can not be null!");
		}
		TerminalEquip terminalEquip  = null;
		List<Ins> ins = null;
		try {
			// 验证仪表是否存在
			terminalEquip = valiedateEquipment( meterAddr, application.getAppKey());
			if(terminalEquip == null){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber not find!");
			}
			// 只支持电表
			if(!terminalEquip.getEquipTypeCode().equals("110000")){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"only suppot elec!");
			}
			// 判断数据库中是否包含相关命令和指令 
			ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd,ins);
			cmdIssueService.cmdIssue(cmd, baseCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response =  Response.retn(ResultCode.PARAM_VALID_ERROR,e.getMessage());
		}
		return response;
	}

	*//**
	 * 电表解除保电
	 * @param equipNumber 仪表地址
	 * @return
	 *//*
	@PostMapping("switchKeepRelieve")
	public Response<?> switchKeepRelieve(HttpServletRequest request){
		logger.info("switch_keep_relieve START.........................");
		CmdResult result = new CmdResult();
		Response<?>  response = null;
		final String cmdCode = "switch_keep_relieve";
		Map<String,String> params = (Map<String, String>) request.getAttribute("params");
		String meterAddr = params.get("equipNumber");
		OrgApplication application =  (OrgApplication) request.getAttribute("application");
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(meterAddr);
		// 关键字段判空
		if(StringUtils.isEmpty(meterAddr)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber can not be null!");
		}
		TerminalEquip terminalEquip  = null;
		List<Ins> ins = null;
		try {
			// 验证仪表是否存在
			terminalEquip = valiedateEquipment( meterAddr, application.getAppKey());
			if(terminalEquip == null){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber not find!");
			}
			// 只支持电表
			if(!terminalEquip.getEquipTypeCode().equals("110000")){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"only suppot elec!");
			}
			// 判断数据库中是否包含相关命令和指令 
			ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd,ins);
			cmdIssueService.cmdIssue(cmd, baseCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response =  Response.retn(ResultCode.PARAM_VALID_ERROR,e.getMessage());
		}
		return response;
	}
	
	*//**
	 * 电表写过载事件有功功率触发下限
	 * @param equipNumber 仪表地址
	 * @param overloadActiveP
	 * @return
	 *//*		
	@PostMapping("writeOverloadActiveP")
	public Response<?> writeOverloadActiveP(HttpServletRequest request){
		logger.info("write_overload_activeP START.........................");
		CmdResult result = new CmdResult();
		Response<?>  response = null;
		final String cmdCode = "write_overload_activeP";
		Map<String,String> params = (Map<String, String>) request.getAttribute("params");
		String meterAddr = params.get("equipNumber");
		String overloadActiveP = params.get("overloadActiveP");
		OrgApplication application =  (OrgApplication) request.getAttribute("application");
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(meterAddr);
		// 关键字段判空
		if(StringUtils.isEmpty(meterAddr)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber can not be null!");
		}
		if(StringUtils.isEmpty(overloadActiveP)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"overloadActiveP can not be null!");
		}
		// 不是数字
		if(!isNumeric(overloadActiveP)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"overloadActiveP must be Numeric!");
		}
		TerminalEquip terminalEquip  = null;
		List<Ins> ins = null;
		try {
			// 验证仪表是否存在
			terminalEquip = valiedateEquipment( meterAddr, application.getAppKey());
			if(terminalEquip == null){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber not find!");
			}
			// 只支持电表
			if(!terminalEquip.getEquipTypeCode().equals("110000")){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"only suppot elec!");
			}
			// 判断数据库中是否包含相关命令和指令 
			ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			// 组装指令结构 ，1个参数
			loadCmdIns(cmd,ins,new String[]{overloadActiveP});
			cmdIssueService.cmdIssue(cmd, baseCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response =  Response.retn(ResultCode.PARAM_VALID_ERROR,e.getMessage());
		}
		return response;
	}
	
	*//**
	 * 读取余额
	 * @param equipNumber 仪表地址
	 * @return
	 *//*
	@PostMapping("readMoney")
	public Response<?> readMoney(HttpServletRequest request){
		logger.info("read_money START.........................");
		CmdResult result = new CmdResult();
		Response<?>  response = null;
		final String cmdCode = "read_money";
		Map<String,String> params = (Map<String, String>) request.getAttribute("params");
		String meterAddr = params.get("equipNumber");
		OrgApplication application =  (OrgApplication) request.getAttribute("application");
		
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(meterAddr);
		// 关键字段判空
		if(StringUtils.isEmpty(meterAddr)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber can not be null!");
		}
		TerminalEquip terminalEquip  = null;
		List<Ins> ins = null;
		try {
			// 验证仪表是否存在
			terminalEquip = valiedateEquipment( meterAddr, application.getAppKey());
			
			
			if(terminalEquip == null){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"equipNumber not find!");
			}
			*//**
			 *  TODO: handle exception
			 *//*
			// 判断数据库中是否包含相关命令和指令 
			ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, cmdCode);
			
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd,ins);
			cmdIssueService.cmdIssue(cmd, baseCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response =  Response.retn(ResultCode.PARAM_VALID_ERROR,e.getMessage());
		}
		return response;
	}
	
}
*/