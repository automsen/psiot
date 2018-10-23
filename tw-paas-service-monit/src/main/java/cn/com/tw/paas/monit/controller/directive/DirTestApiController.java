package cn.com.tw.paas.monit.controller.directive;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.controller.api.IssueValidateController;
import cn.com.tw.paas.monit.entity.db.base.CmdResult;
import cn.com.tw.paas.monit.entity.db.base.Ins;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.service.inn.CmdHandleException;
import cn.com.tw.paas.monit.service.inn.CmdIssueService;
import cn.com.tw.paas.monit.service.inn.callback.CmdCallback;
import cn.com.tw.paas.monit.service.org.OrgApplicationService;

/**
 * 生产调试接口
 * 仅限lorawan
 * @author admin
 *
 */
@RestController
@RequestMapping("directive/test")
public class DirTestApiController extends IssueValidateController {

	private Logger logger = LoggerFactory.getLogger(DirTestApiController.class);
	
	@Resource(name = "testCallback")
	private CmdCallback testCallback;

	@Autowired
	private CmdIssueService cmdIssueService;
	
	@Autowired
	private OrgApplicationService orgApplicationService;
	
	/**
	 * 读设备地址
	 * 
	 * @param equipNumber
	 *            仪表地址
	 * @param netNumber
	 *            网络地址
	 * @param appId
	 *            应用ID
	 * @return
	 */
	@GetMapping("readAddr")
	public Response<?> readAddr(@RequestParam String appKey, @RequestParam String equipNumber, @RequestParam String netNumber, @RequestParam String businessNo) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>#readAddr start#>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Response<?> response = null;
		String cmdCode = "read_addr";

		CmdResult result = new CmdResult();
		OrgApplication application = orgApplicationService.selectByAppKey(appKey);
		//OrgApplication application = (OrgApplication) request.getAttribute("application");
		application.setBussinesNo(businessNo);
		application.setAppBussinesNo(CommUtils.getUuid());
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
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
			terminalEquip.setProtocolType("103");

			// 判断数据库中是否包含相关命令和指令
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), application.getAppId(), businessNo, application.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
			//CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
			try {
				cmdIssueService.cmdIssue(cmd, testCallback);
			} catch (CmdHandleException e) {
				return Response.retn(e.getCode(), e.getMessage());
			}
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response = Response.retn(ResultCode.PARAM_VALID_ERROR, e.getMessage());
		}
		return response;
	}

	/**
	 * 写设备地址
	 * 
	 * @param equipNumber
	 *            仪表地址
	 * @param newEquipNumber
	 *            新仪表地址
	 * @param netNumber
	 *            网络地址
	 * @return
	 */
	@PostMapping("writeAddr")
	public Response<?> writeAddr(@RequestBody Map<String, String> params) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>#writeAddr start#>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Response<?> response = null;
		String cmdCode = "write_addr";

		String equipNumber = params.get("equipNumber");
		String netNumber = params.get("netNumber");
		String newEquipNumber = params.get("newEquipNumber");
		String appKey = params.get("appKey");
		String businessNo = params.get("businessNo");
		
		if (StringUtils.isEmpty(businessNo) || StringUtils.isEmpty(equipNumber) || StringUtils.isEmpty(netNumber) || StringUtils.isEmpty(newEquipNumber) || StringUtils.isEmpty(appKey)) {
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数不能为空");
		}
		
		CmdResult result = new CmdResult();
		OrgApplication application = orgApplicationService.selectByAppKey(appKey);
		
		application.setBussinesNo(businessNo);
		application.setAppBussinesNo(CommUtils.getUuid());
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
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
			terminalEquip.setProtocolType("103");
			// 判断数据库中是否包含相关命令和指令
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), application.getAppId(), businessNo, application.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
			//CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins, newEquipNumber);
			
			try {
				cmdIssueService.cmdIssue(cmd, testCallback);
			} catch (CmdHandleException e) {
				return Response.retn(e.getCode(), e.getMessage());
			}
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response = Response.retn(ResultCode.PARAM_VALID_ERROR, e.getMessage());
		}
		return response;
	}
	
	/**
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
	 */
	@PostMapping("writeWaterBase")
	public Response<?> writeWaterBase(@RequestBody Map<String, String> params) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>#readAddr start#>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Response<?> response = null;
		String cmdCode = "write_water_base";

		//Map<String, String> params = (Map<String, String>) request.getAttribute("params");
		String equipNumber = params.get("equipNumber");
		String netNumber = params.get("netNumber");
		String waterPulse = params.get("waterPulse");
		String waterMultiply = params.get("waterMultiply");
		String businessNo = params.get("businessNo");
		String appKey = params.get("appKey");
		
		if (StringUtils.isEmpty(businessNo) || StringUtils.isEmpty(equipNumber) || StringUtils.isEmpty(netNumber) || StringUtils.isEmpty(waterPulse) || StringUtils.isEmpty(appKey) || StringUtils.isEmpty(waterMultiply)) {
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数不能为空");
		}
		
		CmdResult result = new CmdResult();
		OrgApplication application = orgApplicationService.selectByAppKey(appKey);
		application.setBussinesNo(businessNo);
		application.setAppBussinesNo(CommUtils.getUuid());
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
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
			terminalEquip.setProtocolType("103");
			// 判断数据库中是否包含相关命令和指令
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), application.getAppId(), businessNo, application.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
			//CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins,waterPulse+","+waterMultiply);
			try {
				cmdIssueService.cmdIssue(cmd, testCallback);
			} catch (CmdHandleException e) {
				return Response.retn(e.getCode(), e.getMessage());
			}
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response = Response.retn(ResultCode.PARAM_VALID_ERROR, e.getMessage());
		}
		return response;
	}
	
	/**
	 * 电表读数测试
	 * 
	 * @param equipNumber
	 *            仪表地址
	 * @param netNumber
	 *            网络地址
	 * @param appId
	 *            应用ID
	 * @return
	 */
	@PostMapping("readElec")
	public Response<?> readElec(@RequestBody Map<String, String> params) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>#readAddr start#>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Response<?> response = null;
		String cmdCode = "read_test";
		
		String equipNumber = params.get("equipNumber");
		String netNumber = params.get("netNumber");
		String businessNo = params.get("businessNo");
		String appKey = params.get("appKey");

		if (StringUtils.isEmpty(businessNo) || StringUtils.isEmpty(equipNumber) || StringUtils.isEmpty(netNumber) || StringUtils.isEmpty(appKey)) {
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数不能为空");
		}
		
		CmdResult result = new CmdResult();
		
		OrgApplication application = orgApplicationService.selectByAppKey(appKey);
		application.setBussinesNo(businessNo);
		application.setAppBussinesNo(CommUtils.getUuid());
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
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
			terminalEquip.setProtocolType("103");

			// 判断数据库中是否包含相关命令和指令
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, null, cmdCode);
			// 组装命令指令集
			//CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), application.getAppId(), businessNo, application.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
			try {
				cmdIssueService.cmdIssue(cmd, testCallback);
			} catch (CmdHandleException e) {
				return Response.retn(e.getCode(), e.getMessage());
			}
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response = Response.retn(ResultCode.PARAM_VALID_ERROR, e.getMessage());
		}
		return response;
	}
	
	/**
	 * 水表读数测试
	 * 
	 * @param equipNumber
	 *            仪表地址
	 * @param netNumber
	 *            网络地址
	 * @param appId
	 *            应用ID
	 * @return
	 */
	@PostMapping("readWater")
	public Response<?> readWater(@RequestBody Map<String, String> params) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>#readAddr start#>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Response<?> response = null;
		String cmdCode = "read_test";

		String equipNumber = params.get("equipNumber");
		String netNumber = params.get("netNumber");
		String businessNo = params.get("businessNo");
		String appKey = params.get("appKey");

		if (StringUtils.isEmpty(businessNo) || StringUtils.isEmpty(equipNumber) || StringUtils.isEmpty(netNumber) || StringUtils.isEmpty(appKey)) {
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数不能为空");
		}

		CmdResult result = new CmdResult();
		
		OrgApplication application = orgApplicationService.selectByAppKey(appKey);
		application.setBussinesNo(businessNo);
		application.setAppBussinesNo(CommUtils.getUuid());
		result.setOut_trance_no(application.getAppBussinesNo());
		result.setBusiness_no(application.getBussinesNo());
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
			terminalEquip.setProtocolType("103");
			// 判断数据库中是否包含相关命令和指令
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, null, cmdCode);
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), application.getAppId(), businessNo, application.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
			//CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			result.setCmdId(cmd.getId());
			result.setCmdName(cmd.getCmdName());
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
			try {
				cmdIssueService.cmdIssue(cmd, testCallback);
			} catch (CmdHandleException e) {
				return Response.retn(e.getCode(), e.getMessage());
			}
			result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
			response = Response.ok();
		} catch (RequestParamValidException e) {
			response = Response.retn(ResultCode.PARAM_VALID_ERROR, e.getMessage());
		}
		return response;
	}

}
