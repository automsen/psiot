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

*//**
 * 生产调试接口
 * 仅限lorawan
 * @author admin
 *
 *//*
@SuppressWarnings("unchecked")
@RestController
@RequestMapping("testapi")
public class TestApiController extends IssueValidateController {

	private Logger logger = LoggerFactory.getLogger(TestApiController.class);
	
	@Resource(name = "testCallback")
	private CmdCallback testCallback;

	@Autowired
	private CmdIssueService cmdIssueService;

	*//**
	 * 读设备地址
	 * 
	 * @param equipNumber
	 *            仪表地址
	 * @param netNumber
	 *            网络地址
	 * @param appId
	 *            应用ID
	 * @return
	 *//*
	@PostMapping("readAddr")
	public Response<?> readAddr(HttpServletRequest request) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>#readAddr start#>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Response<?> response = null;
		String cmdCode = "read_addr";

		Map<String, String> params = (Map<String, String>) request.getAttribute("params");
		String equipNumber = params.get("equipNumber");
		String netNumber = params.get("netNumber");

		CmdResult result = new CmdResult();
		OrgApplication application = (OrgApplication) request.getAttribute("application");
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(equipNumber);
		// 关键字段判空
		if (StringUtils.isEmpty(equipNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "equipNumber can not be null!");
		}
		if (StringUtils.isEmpty(netNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "netNumber can not be null!");
		}
		TerminalEquip terminalEquip = new TerminalEquip();
		List<Ins> ins = null;
		try {
			terminalEquip.setEquipTypeCode("100000");
			terminalEquip.setEquipNumber(equipNumber);
			terminalEquip.setNetTypeCode("302");
			terminalEquip.setNetNumber(netNumber);
			terminalEquip.setNetEquipNumber(netNumber);
			terminalEquip.setOrgId(application.getOrgId());
			terminalEquip.setAppId(application.getAppId());

			// 判断数据库中是否包含相关命令和指令
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			loadCmdIns(cmd, ins);
			cmdIssueService.cmdIssue(cmd, testCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response = Response.retn(ResultCode.PARAM_VALID_ERROR, e.getMessage());
		}
		return response;
	}

	*//**
	 * 写设备地址
	 * 
	 * @param equipNumber
	 *            仪表地址
	 * @param newEquipNumber
	 *            新仪表地址
	 * @param netNumber
	 *            网络地址
	 * @return
	 *//*
	@PostMapping("writeAddr")
	public Response<?> writeAddr(HttpServletRequest request) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>#writeAddr start#>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Response<?> response = null;
		String cmdCode = "write_addr";

		Map<String, String> params = (Map<String, String>) request.getAttribute("params");
		String equipNumber = params.get("equipNumber");
		String netNumber = params.get("netNumber");
		String newEquipNumber = params.get("newEquipNumber");
		CmdResult result = new CmdResult();
		OrgApplication application = (OrgApplication) request.getAttribute("application");
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(equipNumber);
		// 关键字段判空
		if (StringUtils.isEmpty(equipNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "equipNumber can not be null!");
		}
		if (StringUtils.isEmpty(netNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "netNumber can not be null!");
		}
		if (StringUtils.isEmpty(newEquipNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "newEquipNumber can not be null!");
		}
		TerminalEquip terminalEquip = new TerminalEquip();
		List<Ins> ins = null;
		try {
			terminalEquip.setEquipTypeCode("100000");
			terminalEquip.setEquipNumber(equipNumber);
			terminalEquip.setNetTypeCode("302");
			terminalEquip.setNetNumber(netNumber);
			terminalEquip.setNetEquipNumber(netNumber);
			terminalEquip.setOrgId(application.getOrgId());
			terminalEquip.setAppId(application.getAppId());
			terminalEquip.setCommPwd("00000002");
			// 判断数据库中是否包含相关命令和指令
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			loadCmdIns(cmd, ins,newEquipNumber);
			cmdIssueService.cmdIssue(cmd, testCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response = Response.retn(ResultCode.PARAM_VALID_ERROR, e.getMessage());
		}
		return response;
	}
	
	*//**
	 * 写水表底数
	 * 
	 * @param equipNumber
	 *            仪表地址
	 * @param netNumber
	 *            网络地址
	 * @param waterPulse
	 *            脉冲数          
	 * @param waterMultiply
	 *            倍率      
	 * @param appId
	 *            应用ID
	 * @return
	 *//*
	@PostMapping("writeWaterBase")
	public Response<?> writeWaterBase(HttpServletRequest request) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>#readAddr start#>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Response<?> response = null;
		String cmdCode = "write_water_base";

		Map<String, String> params = (Map<String, String>) request.getAttribute("params");
		String equipNumber = params.get("equipNumber");
		String netNumber = params.get("netNumber");
		String waterPulse = params.get("waterPulse");
		String waterMultiply = params.get("waterMultiply");

		CmdResult result = new CmdResult();
		OrgApplication application = (OrgApplication) request.getAttribute("application");
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(equipNumber);
		// 关键字段判空
		if (StringUtils.isEmpty(equipNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "equipNumber can not be null!");
		}
		if (StringUtils.isEmpty(netNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "netNumber can not be null!");
		}
		if (StringUtils.isEmpty(waterPulse)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "waterPulse can not be null!");
		}
		if (StringUtils.isEmpty(waterMultiply)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "waterMultiply can not be null!");
		}
		TerminalEquip terminalEquip = new TerminalEquip();
		List<Ins> ins = null;
		try {
			terminalEquip.setEquipTypeCode("120000");
			terminalEquip.setEquipNumber(equipNumber);
			terminalEquip.setNetTypeCode("302");
			terminalEquip.setNetNumber(netNumber);
			terminalEquip.setNetEquipNumber(netNumber);
			terminalEquip.setOrgId(application.getOrgId());
			terminalEquip.setAppId(application.getAppId());
			terminalEquip.setCommPwd("00000002");
			// 判断数据库中是否包含相关命令和指令
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			loadCmdIns(cmd, ins,waterPulse+","+waterMultiply);
			cmdIssueService.cmdIssue(cmd, testCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response = Response.retn(ResultCode.PARAM_VALID_ERROR, e.getMessage());
		}
		return response;
	}
	
	*//**
	 * 电表读数测试
	 * 
	 * @param equipNumber
	 *            仪表地址
	 * @param netNumber
	 *            网络地址
	 * @param appId
	 *            应用ID
	 * @return
	 *//*
	@PostMapping("readElec")
	public Response<?> readElec(HttpServletRequest request) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>#readAddr start#>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Response<?> response = null;
		String cmdCode = "read_test";

		Map<String, String> params = (Map<String, String>) request.getAttribute("params");
		String equipNumber = params.get("equipNumber");
		String netNumber = params.get("netNumber");

		CmdResult result = new CmdResult();
		OrgApplication application = (OrgApplication) request.getAttribute("application");
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(equipNumber);
		// 关键字段判空
		if (StringUtils.isEmpty(equipNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "equipNumber can not be null!");
		}
		if (StringUtils.isEmpty(netNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "netNumber can not be null!");
		}
		TerminalEquip terminalEquip = new TerminalEquip();
		List<Ins> ins = null;
		try {
			terminalEquip.setEquipTypeCode("110000");
			terminalEquip.setEquipNumber(equipNumber);
			terminalEquip.setNetTypeCode("302");
			terminalEquip.setNetNumber(netNumber);
			terminalEquip.setNetEquipNumber(netNumber);
			terminalEquip.setOrgId(application.getOrgId());
			terminalEquip.setAppId(application.getAppId());

			// 判断数据库中是否包含相关命令和指令
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			loadCmdIns(cmd, ins);
			cmdIssueService.cmdIssue(cmd, testCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response = Response.retn(ResultCode.PARAM_VALID_ERROR, e.getMessage());
		}
		return response;
	}
	*//**
	 * 水表读数测试
	 * 
	 * @param equipNumber
	 *            仪表地址
	 * @param netNumber
	 *            网络地址
	 * @param appId
	 *            应用ID
	 * @return
	 *//*
	@PostMapping("readWater")
	public Response<?> readWater(HttpServletRequest request) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>#readAddr start#>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Response<?> response = null;
		String cmdCode = "read_test";

		Map<String, String> params = (Map<String, String>) request.getAttribute("params");
		String equipNumber = params.get("equipNumber");
		String netNumber = params.get("netNumber");

		CmdResult result = new CmdResult();
		OrgApplication application = (OrgApplication) request.getAttribute("application");
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
		result.setMeterAddr(equipNumber);
		// 关键字段判空
		if (StringUtils.isEmpty(equipNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "equipNumber can not be null!");
		}
		if (StringUtils.isEmpty(netNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "netNumber can not be null!");
		}
		TerminalEquip terminalEquip = new TerminalEquip();
		List<Ins> ins = null;
		try {
			terminalEquip.setEquipTypeCode("120000");
			terminalEquip.setEquipNumber(equipNumber);
			terminalEquip.setNetTypeCode("302");
			terminalEquip.setNetNumber(netNumber);
			terminalEquip.setNetEquipNumber(netNumber);
			terminalEquip.setOrgId(application.getOrgId());
			terminalEquip.setAppId(application.getAppId());

			// 判断数据库中是否包含相关命令和指令
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			loadCmdIns(cmd, ins);
			cmdIssueService.cmdIssue(cmd, testCallback);
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response = Response.retn(ResultCode.PARAM_VALID_ERROR, e.getMessage());
		}
		return response;
	}

}
*/