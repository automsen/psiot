package cn.com.tw.paas.monit.controller.directive;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.common.utils.cons.EnergyCategEum;
import cn.com.tw.paas.monit.common.utils.cons.code.MonitResultCode;
import cn.com.tw.paas.monit.controller.api.IssueValidateController;
import cn.com.tw.paas.monit.entity.business.base.BaseParam;
import cn.com.tw.paas.monit.entity.business.ins.AlarmRule;
import cn.com.tw.paas.monit.entity.business.ins.CommonParam;
import cn.com.tw.paas.monit.entity.business.ins.DirectDataItem;
import cn.com.tw.paas.monit.entity.business.ins.DirectHz;
import cn.com.tw.paas.monit.entity.business.ins.EquipBatch;
import cn.com.tw.paas.monit.entity.business.ins.LoopSwitch;
import cn.com.tw.paas.monit.entity.business.ins.NetMeter;
import cn.com.tw.paas.monit.entity.business.ins.NetMeterData;
import cn.com.tw.paas.monit.entity.business.ins.PriceRule;
import cn.com.tw.paas.monit.entity.business.ins.Recharge;
import cn.com.tw.paas.monit.entity.db.base.Ins;
import cn.com.tw.paas.monit.entity.db.baseEquipModel.BaseEquipModel;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.NetEquip;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.entity.db.read.ReadLast;
import cn.com.tw.paas.monit.service.baseEquipModel.BaseEquipModelService;
import cn.com.tw.paas.monit.service.inn.CmdHandleException;
import cn.com.tw.paas.monit.service.inn.CmdIssueService;
import cn.com.tw.paas.monit.service.inn.callback.CmdCallback;
import cn.com.tw.paas.monit.service.org.NetEquipService;
import cn.com.tw.paas.monit.service.read.ReadService;

/**
 * 指令下发
 * @author Administrator
 *
 */
@RestController
@RequestMapping("directive")
public class DirectiveController extends IssueValidateController{

	private Logger logger = LoggerFactory.getLogger(DirectiveController.class);
	
	@Resource(name="noPushCallback")
	private CmdCallback noPushCallback;
	
	@Resource(name="baseCallback")
	private CmdCallback baseCallback;
	
	@Autowired
	private ReadService readService;
	
	@Autowired
	private CmdIssueService cmdIssueService;
	
	@Autowired
	private NetEquipService netEquipService;
	
	@Autowired
	private BaseEquipModelService baseEquipModelService;
	
	/**
	 * 常规写指令
	 * @param param
	 * @param br
	 * @return
	 */
	@PostMapping("commonWrite")
	public Response<?> commonWrite(@RequestBody @Valid CommonParam param, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		Boolean isApplicationRequest = false;
		String cmdCode = param.getCmdCode();
		String equipNumber = param.getEquipNumber();
		
		// 验证仪表是否存在
		TerminalEquip terminalEquip = valiedateEquipAndOrgId(equipNumber, param.getOrgId());
		if(terminalEquip == null){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, terminalEquip.getModelId(), cmdCode);
		if(null==ins){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR);
		}
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), param.getBusinessNo(), param.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
		if(null==cmd){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR);
		}
		if (StringUtils.isEmpty(param.getParams())){
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
		}
		else{
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins, param.getParams());
		}
		
		try {
			if(!isApplicationRequest){
				cmdIssueService.cmdIssue(cmd, noPushCallback);
			}else{
				cmdIssueService.cmdIssue(cmd, baseCallback);
			}
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 实时数据立即读数
	 * @param equipId
	 * @return
	 */
	@ApiOperation(value="实时数据立即读数", notes="")
	@PostMapping("read/{equipNumber}")
	public Response<?> readMeter(@PathVariable String equipNumber, @RequestBody @Valid BaseParam params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		//CmdResult result = new CmdResult();
		String cmdCode = "read_common";
		
		// 验证仪表是否存在
		TerminalEquip terminalEquip = valiedateEquipAndOrgId(equipNumber, params.getOrgId());
		if(terminalEquip == null){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = null;
		if ("110000".equals(terminalEquip.getEquipTypeCode())) {
			ins = getCmdIns(terminalEquip.getElecMeterTypeCode(),terminalEquip.getProtocolType(), terminalEquip.getModelId(), cmdCode);
		}else {
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, terminalEquip.getModelId(), cmdCode);
		}
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), params.getBusinessNo(), params.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
		//CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
		// 组装指令结构 ，当前无参数
		loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
		
		try {
			cmdIssueService.cmdIssue(cmd, baseCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		//result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
		return Response.ok();
	}
	
	/**
	 * 通讯测试读止码
	 * @param equipId
	 * @return
	 */
	@ApiOperation(value="通讯测试读止码", notes="")
	@PostMapping("readTest/{equipNumber}")
	public Response<?> readTestMeter(@PathVariable String equipNumber, @RequestBody @Valid BaseParam params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		//CmdResult result = new CmdResult();
		String cmdCode = "read_test";
		
		// 验证仪表是否存在
		TerminalEquip terminalEquip = valiedateEquipAndOrgId(equipNumber, params.getOrgId());
		if(terminalEquip == null){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = null;

			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, terminalEquip.getModelId(), cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), params.getBusinessNo(), params.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
		//CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
		// 组装指令结构 ，当前无参数
		loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
		
		try {
			cmdIssueService.cmdIssue(cmd, baseCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		//result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
		return Response.ok();
	}
	
	
	
	/**
	 * 开闸
	 * @param equipId
	 * @return
	 */
	@PostMapping("open/{equipNumber}")
	public Response<?> openMeter(@PathVariable String equipNumber, @RequestBody @Valid BaseParam params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		// 645协议(默认)
		String cmdCode = "switch_on";
		// modbus协议
		if (equipNumber.length()==11){
			cmdCode = "switch_on_m";
		}
		//result.setMeterAddr(equipNumber);
		// 验证仪表是否存在
		TerminalEquip terminalEquip = valiedateEquipAndOrgId(equipNumber, params.getOrgId());
		if(terminalEquip == null){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, terminalEquip.getModelId(), cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), params.getBusinessNo(), params.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
		//CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
		//result.setCmdId(cmd.getId());
		//result.setCmdName(cmd.getCmdName());
		// 组装指令结构 ，当前无参数
		loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
		try {
			cmdIssueService.cmdIssue(cmd, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		//result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
		// 填写返回参数
		return Response.ok();
	}
	
	/**
	 * 开闸
	 * 多路控制
	 * @param params
	 * @param br
	 * @return
	 */
	@PostMapping("openLoop")
	public Response<?> openLoop(@RequestBody @Valid LoopSwitch params, BindingResult br){
		
		logger.debug("openLoop method : params = {}", params == null ? "" : params.toString());
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		//CmdResult result = new CmdResult();
		String cmdCode = "switch_on";
		
		//result.setMeterAddr(equipNumber);
		// 验证仪表是否存在
		TerminalEquip terminalEquip = valiedateEquipAndOrgId(params.getEquipNumber(), params.getOrgId());
		if(terminalEquip == null){
			logger.error("openLoop method : meterAddr not find!");
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 验证是否电表
		else if (!terminalEquip.getEquipTypeCode().equals(EnergyCategEum.ELEC.getValue())){
			logger.error("openLoop method : meterType not suppot!");
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterType not suppot!");
		}
		// 验证电表子类
		/*else if (!terminalEquip.getElecMeterTypeCode().equals("111002")
				&&!terminalEquip.getElecMeterTypeCode().equals("111003")){
			params.setLoop("00");
//			logger.error("openLoop method : meterType not suppot!");
//			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterType not suppot!");
		}*/
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, terminalEquip.getModelId(), cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), params.getBusinessNo(), params.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
		//CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
		//result.setCmdId(cmd.getId());
		//result.setCmdName(cmd.getCmdName());
		// 组装指令结构 ，当前无参数
		loadCmdIns(cmd, terminalEquip.getProtocolType(), ins,params.getLoop());
		try {
			cmdIssueService.cmdIssue(cmd, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		//result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
		// 填写返回参数
		return Response.ok();
	}
	
	/**
	 * 批量开闸
	 * @param equipId
	 * @return
	 */
	@PostMapping("open")
	public Response<?> openMeter(@RequestBody @Valid EquipBatch params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		//CmdResult result = new CmdResult();
		String cmdCode = "switch_on";
		
		List<String> equipNumbers = params.getEquipNumbers();
		//result.setMeterAddr(equipNumber);
		
		// 关键字段判空
		if(StringUtils.isEmpty(equipNumbers)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterNumbers can not be null!");
		}
		
		List<CmdExe> cmdExeList = new ArrayList<CmdExe>();
		for (String equipNumber : equipNumbers) {
			// 验证仪表是否存在
			TerminalEquip terminalEquip = valiedateEquipAndOrgId(equipNumber, params.getOrgId());
			if(terminalEquip == null){
				logger.error("openMeter(), {} and {} , no match ,no find!", new Object[]{equipNumber, params.getOrgId()});
				continue;
			}
			// 判断数据库中是否包含相关命令和指令
			// TODO: handle exception  modelId 暂时给空值
			List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(),null,terminalEquip.getModelId(), cmdCode);
			
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, equipNumber, terminalEquip.getAppId(), params.getBusinessNo(), params.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
			//CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			//result.setCmdId(cmd.getId());
			//result.setCmdName(cmd.getCmdName());
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
			
			cmdExeList.add(cmd);
		}
		try {
			cmdIssueService.cmdIssueBatch(cmdExeList, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		//result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
		// 填写返回参数
		return Response.ok();
	}
	
	
	/**
	 * 关闸
	 * @param equipNumber
	 * @return
	 */
	@PostMapping("close/{equipNumber}")
	public Response<?> closeMeter(@PathVariable String equipNumber, @RequestBody @Valid BaseParam params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}

		// 关键字段判空
		if(StringUtils.isEmpty(equipNumber)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr can not be null!");
		}
		// 645协议(默认)
		String cmdCode = "switch_off";
		// modbus协议
		if (equipNumber.length()==11){
			cmdCode = "switch_off_m";
		}
		
		// 验证仪表是否存在
		TerminalEquip terminalEquip = valiedateEquipAndOrgId(equipNumber, params.getOrgId());
		if(terminalEquip == null){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(),null,terminalEquip.getModelId(), cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), params.getBusinessNo(), params.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
		//CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
		//result.setCmdId(cmd.getId());
		//result.setCmdName(cmd.getCmdName());
		// 组装指令结构 ，当前无参数
		loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
		try {
			cmdIssueService.cmdIssue(cmd, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		//result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
		return Response.ok();
	}
	
	/**
	 * 批量关闸
	 * @param equipId
	 * @return
	 */
	@PostMapping("close")
	public Response<?> closeMeter(@RequestBody @Valid EquipBatch params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		//CmdResult result = new CmdResult();
		String cmdCode = "switch_off";
		
		List<String> equipNumbers = params.getEquipNumbers();
		//result.setMeterAddr(equipNumber);
		
		// 关键字段判空
		if(StringUtils.isEmpty(equipNumbers)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterNumbers can not be null!");
		}
		
		List<CmdExe> cmdExeList = new ArrayList<CmdExe>();
		for (String equipNumber : equipNumbers) {
			// 验证仪表是否存在
			TerminalEquip terminalEquip = valiedateEquipAndOrgId(equipNumber, params.getOrgId());
			if(terminalEquip == null){
				logger.error("closeMeter(), {} and {} , no match ,no find!", new Object[]{equipNumber, params.getOrgId()});
				continue;
			}
			// 判断数据库中是否包含相关命令和指令
			// TODO: handle exception  modelId 暂时给空值
			List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, terminalEquip.getModelId(), cmdCode);
			
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), params.getBusinessNo(), params.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
			//CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
			//result.setCmdId(cmd.getId());
			//result.setCmdName(cmd.getCmdName());
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
			
			cmdExeList.add(cmd);
		}
		try {
			cmdIssueService.cmdIssueBatch(cmdExeList, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		//result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
		// 填写返回参数
		return Response.ok();
	}
	
	/**
	 * 关闸
	 * 多路控制
	 * @param params
	 * @param br
	 * @return
	 */
	@PostMapping("closeLoop")
	public Response<?> closeLoop(@RequestBody @Valid LoopSwitch params, BindingResult br){
		logger.debug("closeLoop method : params = {}", params == null ? "" : params.toString());
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		//CmdResult result = new CmdResult();
		String cmdCode = "switch_off";
		
		//result.setMeterAddr(equipNumber);
		// 验证仪表是否存在
		TerminalEquip terminalEquip = valiedateEquipAndOrgId(params.getEquipNumber(), params.getOrgId());
		if(terminalEquip == null){
			logger.error("closeLoop method : meterAddr not find!");
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 验证是否电表
		else if (!terminalEquip.getEquipTypeCode().equals(EnergyCategEum.ELEC.getValue())){

			logger.error("closeLoop method : meterType not suppot!");
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterType not suppot!");
		}
		// 验证电表子类
//		else if (!terminalEquip.getElecMeterTypeCode().equals("111002")
//				&&!terminalEquip.getElecMeterTypeCode().equals("111003")){
//			params.setLoop("00");
////			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterType not suppot!");
//		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, terminalEquip.getModelId(), cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), params.getBusinessNo(), params.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
		//CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
		//result.setCmdId(cmd.getId());
		//result.setCmdName(cmd.getCmdName());
		// 组装指令结构 ，当前无参数
		loadCmdIns(cmd, terminalEquip.getProtocolType(), ins,params.getLoop());
		try {
			cmdIssueService.cmdIssue(cmd, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		//result.setSuccess(ResultCode.OPERATION_IS_SUCCESS);
		// 填写返回参数
		return Response.ok();
	}
	
	/**
	 * 下发网关连接仪表指令
	 * @param netEquipAddr
	 * @return
	 */
	@PostMapping("netMeter")
	public Response<?> connectGatewayMeter(@RequestBody @Valid NetMeter params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		if (StringUtils.isEmpty(params.getCommAddrs())) {
			throw new RequestParamValidException("commAddrs can not null");
		}
		
		//CmdResult result = new CmdResult();
		String cmdCode = "write_collect_equip";
		
		String netEquipAddr = params.getNetEquipAddr();
		
		//result.setMeterAddr(params.getNetEquipAddr());
        //TerminalEquip terminalEquip  = null;
		NetEquip queryOrg = new NetEquip();
		
		queryOrg.setOrgId(params.getOrgId());
		queryOrg.setEquipNumber(netEquipAddr);
		// 验证网关是否存在
		List<NetEquip> tempList = netEquipService.selectByEntity(queryOrg);
		if(tempList == null ){
			return Response.retn(MonitResultCode.PARAMETER_NULL_ERROR,"meterAddr not null!");
		}
		if(tempList.size() > 1 || tempList.size() == 0 ){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		/*OrgApplication application1 = orgApplicationService.selectByTerminalEquip(equipNumber);
		// 验证仪表是否存在
		terminalEquip = valiedateEquipment(equipNumber, application1.getAppKey());*/
		// 判断数据库中是否包含相关命令和指令
		List<Ins> ins = getCmdIns(tempList.get(0).getEquipTypeCode(), null, null, cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, tempList.get(0).getEquipNumber(), tempList.get(0).getAppId(), params.getBusinessNo(), params.getOrgId(), tempList.get(0).getEquipTypeCode(), tempList.get(0).getNetTypeCode(), tempList.get(0).getNetNumber(), tempList.get(0).getCommPwd());
		
		//CmdExe cmd = getNetCmdExe(cmdCode, tempList.get(0), application);
		//result.setCmdId(cmd.getId());
		//result.setCmdName(cmd.getCmdName());
		// 组装指令结构 ，当前无参数
		loadNetCmdIns(cmd, ins, params.getCommAddrs());
		try {
			cmdIssueService.cmdIssue(cmd, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 读取采集器下关联的仪表
	 * @param netEquipAddr
	 * @return
	 */
	@PostMapping("netMeter/read")
	public Response<?> gatewayMeterGet(@RequestBody @Valid NetMeter params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		String cmdCode = "read_collect_equip";
		
		NetEquip queryOrg = new NetEquip();
		queryOrg.setOrgId(params.getOrgId());
		queryOrg.setEquipNumber(params.getNetEquipAddr());
		// 验证网关是否存在
		List<NetEquip> tempList = netEquipService.selectByEntity(queryOrg);
		if(tempList == null ){
			return Response.retn(MonitResultCode.PARAMETER_NULL_ERROR,"meterAddr not null!");
		}
		if(tempList.size() > 1 || tempList.size() == 0 ){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(tempList.get(0).getEquipTypeCode(), null, null, cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, tempList.get(0).getEquipNumber(), tempList.get(0).getAppId(), params.getBusinessNo(), params.getOrgId(), tempList.get(0).getEquipTypeCode(), tempList.get(0).getNetTypeCode(), tempList.get(0).getNetNumber(), tempList.get(0).getCommPwd());
		//CmdExe cmd = getNetCmdExe(cmdCode,tempList.get(0),application);
		// 组装指令结构 ，当前无参数
		loadNetCmdIns(cmd, ins);
		
		try {
			cmdIssueService.cmdIssue(cmd, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		// 填写返回参数
		return Response.ok();
		
	}
	
	/**
	 * 数据项下发
	 * @param netEquipAddr
	 * @param powerdata
	 * @return
	 */
	@PostMapping("netDataItem")
	public Response<?> dataItem(@RequestBody @Valid NetMeterData params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		String cmdCode = "write_collect_ins";
		
		NetEquip queryOrg = new NetEquip();
		queryOrg.setOrgId(params.getOrgId());
		queryOrg.setEquipNumber(params.getNetEquipAddr());
		// 验证网关是否存在
		List<NetEquip> tempList = netEquipService.selectByEntity(queryOrg);
		if(tempList == null ){
			return Response.retn(MonitResultCode.PARAMETER_NULL_ERROR,"meterAddr not null!");
		}
		if(tempList.size() > 1 || tempList.size() == 0 ){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(tempList.get(0).getEquipTypeCode(),null, null, cmdCode);
		
		// 组装命令指令集
		//CmdExe cmd = getNetCmdExe(cmdCode,tempList.get(0),application);
		CmdExe cmd = getCmdExe(cmdCode, tempList.get(0).getEquipNumber(), tempList.get(0).getAppId(), "", params.getOrgId(), tempList.get(0).getEquipTypeCode(), tempList.get(0).getNetTypeCode(), tempList.get(0).getNetNumber(), tempList.get(0).getCommPwd());
		
		// 组装指令结构
		loadNetCmdIns(cmd, ins,params.getPowerData());
		try {
			cmdIssueService.cmdIssue(cmd, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		// 填写返回参数
		return Response.ok();
	}
	
	/**
	 * 读取设备指令
	 * @param netEquipAddr
	 * @return
	 */
	@PostMapping("netDataItem/read")
	public Response<?> getDataItem(@RequestBody @Valid NetMeter params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		String cmdCode = "read_collect_ins";
		
		NetEquip queryOrg = new NetEquip();
		queryOrg.setOrgId(params.getOrgId());
		queryOrg.setEquipNumber(params.getNetEquipAddr());
		// 验证网关是否存在
		List<NetEquip> tempList = netEquipService.selectByEntity(queryOrg);
		if(tempList == null ){
			return Response.retn(MonitResultCode.PARAMETER_NULL_ERROR,"meterAddr not null!");
		}
		if(tempList.size() > 1 || tempList.size() == 0 ){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(tempList.get(0).getEquipTypeCode(),null, null, cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, tempList.get(0).getEquipNumber(), tempList.get(0).getAppId(), "", params.getOrgId(), tempList.get(0).getEquipTypeCode(), tempList.get(0).getNetTypeCode(), tempList.get(0).getNetNumber(), tempList.get(0).getCommPwd());
		//CmdExe cmd = getNetCmdExe(cmdCode,tempList.get(0),application);
		// 组装指令结构 ，当前无参数
		loadNetCmdIns(cmd, ins);
		
		
		try {
			cmdIssueService.cmdIssue(cmd, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		// 填写返回参数
		return Response.ok();
	}
	
	/**
	 * 采集频率
	 * @param netEquipAddr
	 * @param time
	 * @return
	 */
	@PostMapping("netCollectHz")
	public Response<?> gatherHz(@RequestBody @Valid NetMeter params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		if (StringUtils.isEmpty(params.getTime())) {
			throw new RequestParamValidException("time can not null");
		}
		
		String cmdCode = "write_collect_frequency";
		
		NetEquip queryOrg = new NetEquip();
		queryOrg.setOrgId(params.getOrgId());
		queryOrg.setEquipNumber(params.getNetEquipAddr());
		// 验证网关是否存在
		List<NetEquip> tempList = netEquipService.selectByEntity(queryOrg);
		if(tempList == null ){
			return Response.retn(MonitResultCode.PARAMETER_NULL_ERROR,"meterAddr not null!");
		}
		if(tempList.size() > 1 || tempList.size() == 0 ){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(tempList.get(0).getEquipTypeCode(),null, null, cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, tempList.get(0).getEquipNumber(), tempList.get(0).getAppId(), "", params.getOrgId(), tempList.get(0).getEquipTypeCode(), tempList.get(0).getNetTypeCode(), tempList.get(0).getNetNumber(), tempList.get(0).getCommPwd());
		//CmdExe cmd = getNetCmdExe(cmdCode,tempList.get(0),application);
		// 组装指令结构 ，当前无参数
		loadNetCmdIns(cmd, ins, params.getTime());
		try {
			cmdIssueService.cmdIssue(cmd, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 读取采集频率
	 * @param netEquipAddr
	 * @return
	 */
	@GetMapping("netCollectHz/read")
	public Response<?> getCollectHz(@RequestBody @Valid NetMeter params, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		String cmdCode = "read_collect_frequency";

		NetEquip queryOrg = new NetEquip();
		queryOrg.setOrgId(params.getOrgId());
		queryOrg.setEquipNumber(params.getNetEquipAddr());
		// 验证网关是否存在
		List<NetEquip> tempList = netEquipService.selectByEntity(queryOrg);
		if(tempList == null ){
			return Response.retn(MonitResultCode.PARAMETER_NULL_ERROR,"meterAddr not null!");
		}
		if(tempList.size() > 1 || tempList.size() == 0 ){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(tempList.get(0).getEquipTypeCode(),null, null, cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, tempList.get(0).getEquipNumber(), tempList.get(0).getAppId(), "", params.getOrgId(), tempList.get(0).getEquipTypeCode(), tempList.get(0).getNetTypeCode(), tempList.get(0).getNetNumber(), tempList.get(0).getCommPwd());
		// 组装指令结构 ，当前无参数
		loadNetCmdIns(cmd, ins);
		try {
			cmdIssueService.cmdIssue(cmd, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		// 填写返回参数
		return Response.ok();
	}
	
	/**
	 * 仪表充值
	 * @param equipNumber
	 * @return
	 */
	@PostMapping("recharge")
	public Response<?> meterPay(@RequestBody @Valid Recharge recharge, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		String cmdCode = "recharge_cmd";
		
		// 验证仪表是否存在
		TerminalEquip terminalEquip = valiedateEquipAndOrgId(recharge.getEquipNumber(), recharge.getOrgId());
		if(terminalEquip == null){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, terminalEquip.getModelId(), cmdCode);
		
		// 获取充值次数
		ReadLast read = new ReadLast();
		read.setMeterAddr(recharge.getEquipNumber());
		read.setItemCode("payTime");
		read = readService.selectByAddrAndItem(read);
		Integer payNum = 0;
		
		if(read != null &&!StringUtils.isEmpty(read.getReadValue())){
			payNum = Integer.parseInt(read.getReadValue());
		}
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), recharge.getBusinessNo(), recharge.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
		loadCmdIns(cmd, terminalEquip.getProtocolType(), ins, recharge.getMoney() + "," + payNum);
		try {
			cmdIssueService.cmdIssue(cmd, noPushCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	
	/**
	 * 单一计价规则
	 * @param equipNumber
	 * @param price
	 * @return
	 */
	@PostMapping("price")
	public Response<?> meterPrice(@RequestBody @Valid PriceRule rule, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		String cmdCode = "write_price1";
		
		String equipNumber = rule.getEquipNumber();
		
		String price = rule.getPrice();
		
		BaseEquipModel baseEquipModel = baseEquipModelService.selectByEquipNumber(equipNumber);
		/**
		 * 判断是否费控
		 */
		if(baseEquipModel.getIsCostControl() == 0){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr model not costControl!");
		}
		// 验证仪表是否存在
		TerminalEquip terminalEquip = valiedateEquipAndOrgId(equipNumber, rule.getOrgId());
		if(terminalEquip == null){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, terminalEquip.getModelId(), cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), rule.getBusinessNo(), rule.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
		//CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
		String price1 = price + "," + price + "," + price + "," + price;
		loadCmdIns(cmd, terminalEquip.getProtocolType(), ins, price1);
		try {
			cmdIssueService.cmdIssue(cmd, baseCallback);
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 写电压电流变比报警透支限额数据块
	 * @param equipNumber表号
	 * @param alarmMoney1 一级预警金额
	 * @param alarmMoney2 二级预警金额
	 * @param overdraw投资金额
	 * @return
	 */
	@PostMapping("alarm")
	public Response<?> alarmOverBlock(@RequestBody @Valid AlarmRule rule, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		String cmdCode = "write_ct_pt_alarm_over_block";
		Boolean isApplicationRequest = false;
		
		String equipNumber = rule.getEquipNumber();
		String alarmMoney1 = rule.getAlarmMoney1();
		String alarmMoney2 = rule.getAlarmMoney2();
		String overdraft = rule.getOverdraft();
		
		BaseEquipModel baseEquipModel = baseEquipModelService.selectByEquipNumber(equipNumber);
		/**
		 * 判断是否费控
		 */
		if(baseEquipModel.getIsCostControl() == 0){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "meterAddr model not costControl!");
		}
		
		// 验证仪表是否存在
		TerminalEquip terminalEquip = valiedateEquipAndOrgId(equipNumber, rule.getOrgId());
		String pt = terminalEquip.getElecPt().toString();//电压变比
		String ct = terminalEquip.getElecCt().toString();//电流变比
		
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception  modelId 暂时给空值
		List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(),null, terminalEquip.getModelId(), cmdCode);
		
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), rule.getBusinessNo(), rule.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
		//CmdExe cmd = getCmdExe(cmdCode,terminalEquip,application);
		String params = ct + "," + pt + "," + alarmMoney1 + "," + alarmMoney2 + "," + overdraft;
		
		loadCmdIns(cmd, terminalEquip.getProtocolType(), ins, params);
	
		try {
			if(!isApplicationRequest){
				cmdIssueService.cmdIssue(cmd, noPushCallback);
			}else{
				cmdIssueService.cmdIssue(cmd, baseCallback);
			}
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	
	
	/**
	 * 下发直连指令
	 * @param netEquipAddr
	 * @return
	 */
	@PostMapping("directDataItem")
	public Response<?> directMeter(@RequestBody @Valid DirectDataItem dataItem, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		String cmdCode = "";
		String commAddrs = dataItem.getCommAddrs();
		String powerdata = dataItem.getPowerdata();
		
		String[] equips = commAddrs.split(",");
		for (String equip : equips) {
			
			TerminalEquip terminalEquip  = null;
			List<Ins> ins = null;
				
			// 验证仪表是否存在
			terminalEquip = valiedateEquipAndOrgId(equip, dataItem.getOrgId());
			// 判断数据库中是否包含相关命令和指令
			ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, terminalEquip.getModelId(), cmdCode);
			
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), dataItem.getBusinessNo(), dataItem.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins,powerdata);
			try {
				cmdIssueService.cmdIssue(cmd, noPushCallback);
			} catch (CmdHandleException e) {
				return Response.retn(e.getCode(),e.getMessage());
			}
		}
		
		return Response.ok();
	}
	
	/**
	 * 下发直连采集频率读取
	 * @param netEquipAddr
	 * @return
	 */
	@PostMapping("directHz")
	public Response<?> directHz(@RequestBody @Valid DirectHz directHz, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		String cmdCode = "";
		
		String commAddrs = directHz.getCommAddrs();
		String time = directHz.getTime();
		
		
		String[] equips = commAddrs.split(",");
		for (String equip : equips) {
			
			// 验证仪表是否存在
			TerminalEquip terminalEquip = valiedateEquipAndOrgId(equip, directHz.getOrgId());
			// 判断数据库中是否包含相关命令和指令
			List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, terminalEquip.getModelId(), cmdCode);
			
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), directHz.getBusinessNo(), directHz.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
			//CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins,time);
			try {
				cmdIssueService.cmdIssue(cmd, noPushCallback);
			} catch (CmdHandleException e) {
				return Response.retn(e.getCode(),e.getMessage());
			}
		}
		
		return Response.ok();
	}
	
	/**
	 * 读直连采集频率读取
	 * @param netEquipAddr
	 * @return
	 */
	@PostMapping("directHz/read")
	public Response<?> getDirectHz(@RequestBody @Valid DirectHz directHz, BindingResult br){
		
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		final String cmdCode = "";
		String commAddrs = directHz.getCommAddrs();
		
		String[] equips = commAddrs.split(",");
		for (String equip : equips) {
		
			// 验证仪表是否存在
			TerminalEquip terminalEquip = valiedateEquipAndOrgId(equip, directHz.getOrgId());
			// 判断数据库中是否包含相关命令和指令
			List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, terminalEquip.getModelId(), cmdCode);
			
			// 组装命令指令集
			CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(), terminalEquip.getAppId(), directHz.getBusinessNo(), directHz.getOrgId(), terminalEquip.getEquipTypeCode(), terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(), terminalEquip.getCommPwd());
			//CmdExe cmd = getCmdExe(cmdCode, terminalEquip, application);
			// 组装指令结构 ，当前无参数
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
			try {
				cmdIssueService.cmdIssue(cmd, noPushCallback);
			} catch (CmdHandleException e) {
				return Response.retn(e.getCode(),e.getMessage());
			}
		}
		
		return Response.ok();
	}
}
