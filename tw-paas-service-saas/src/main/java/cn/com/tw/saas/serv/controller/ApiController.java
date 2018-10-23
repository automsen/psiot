package cn.com.tw.saas.serv.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.common.utils.cons.ApiTemplateCons;
import cn.com.tw.saas.serv.entity.business.command.PageCmdResult;
import cn.com.tw.saas.serv.entity.room.RoomMeterSwitchingManagent;
import cn.com.tw.saas.serv.service.command.CmdRecordService;
import cn.com.tw.saas.serv.service.read.ReadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 调用paas命令接口
 * @author admin
 *
 */
@RestController
@RequestMapping("api")
@Api(description = "调用paas命令接口")
public class ApiController {

	@Autowired
	private CmdRecordService cmdService;
	@Autowired
	private ReadService readService;
	
	/**
	 * 常规读数
	 * @param meterAddr
	 * @return
	 */
	@GetMapping("readCommon")
	@ApiOperation(value="常规读数", notes="")
	public Response<?> readCommon(String meterAddr) {
		if (StringUtils.isEmpty(meterAddr)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("equipNumber", meterAddr);
		PageCmdResult result = cmdService.generateCmd(ApiTemplateCons.readCommon, meterAddr, requestMap);
		return Response.ok(result);
	}
	
	/**
	 * 通讯读止码
	 * @param meterAddr
	 * @return
	 */
	@GetMapping("readTest")
	@ApiOperation(value="通讯读止码", notes="")
	public Response<?> readTest(String meterAddr) {
		if (StringUtils.isEmpty(meterAddr)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("equipNumber", meterAddr);
		PageCmdResult result = cmdService.generateCmd(ApiTemplateCons.readTest, meterAddr, requestMap);
		return Response.ok(result);
	}
	
	
	/**
	 * 仪表开闸
	 * @param meterAddr
	 * @return
	 */
	@GetMapping("switchOn")
	@ApiOperation(value="仪表开闸", notes="")
	public Response<?> switchOn(String meterAddr) {
		if (StringUtils.isEmpty(meterAddr)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("equipNumber", meterAddr);
		PageCmdResult result = cmdService.generateCmd(ApiTemplateCons.switchOn, meterAddr, requestMap);
		return Response.ok(result);
	}
	
	
	/**
	 * 仪表关闸
	 * @param meterAddr
	 * @return
	 */
	@GetMapping("switchOff")
	@ApiOperation(value="仪表关闸", notes="")
	public Response<?> switchOff(String meterAddr) {
		if (StringUtils.isEmpty(meterAddr)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("equipNumber", meterAddr);
		PageCmdResult result = cmdService.generateCmd(ApiTemplateCons.switchOff, meterAddr, requestMap);
		return Response.ok(result);
	}
	
	/**
	 * 回路开闸
	 * @param meterAddr
	 * @return
	 */
	@PostMapping("loopOn")
	@ApiOperation(value="回路开闸", notes="")
	public Response<?> loopOn(@RequestBody List<RoomMeterSwitchingManagent> meterInfo) {
		/*if (StringUtils.isEmpty(meterAddr)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常"); 
		}*/
		List<RoomMeterSwitchingManagent> list = null;
		List<PageCmdResult> cmdResults = new ArrayList<PageCmdResult>();
		if(meterInfo.isEmpty()){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "未选择仪表");
		}
		for (RoomMeterSwitchingManagent roomMeterSwitchingManagent : meterInfo) {
			String meterAddr = roomMeterSwitchingManagent.getMeterAddr();
			String loopTypeStr = roomMeterSwitchingManagent.getLoopTypeStr();
			String loopTypeStr2 = roomMeterSwitchingManagent.getLoopTypeStr2();
			String loopTypeStr3 = roomMeterSwitchingManagent.getLoopTypeStr3();
			try {
				if(loopTypeStr != null && !loopTypeStr.isEmpty() && loopTypeStr != ""){
					Map<String, String> requestMap = new HashMap<String, String>();
					requestMap.put("equipNumber", meterAddr);
					requestMap.put("loop", loopTypeStr);
					PageCmdResult result = cmdService.generateCmdUrlNoAddr(ApiTemplateCons.loopOn, meterAddr, requestMap);
					cmdResults.add(result);
				}
				if(loopTypeStr2 != null && !loopTypeStr2.isEmpty() && loopTypeStr2 != ""){
					Map<String, String> requestMap = new HashMap<String, String>();
					requestMap.put("equipNumber", meterAddr);
					requestMap.put("loop", loopTypeStr2);
					PageCmdResult result = cmdService.generateCmdUrlNoAddr(ApiTemplateCons.loopOn, meterAddr, requestMap);
					cmdResults.add(result);
				}
				if(loopTypeStr3 != null && !loopTypeStr3.isEmpty() && loopTypeStr3 != ""){
					Map<String, String> requestMap = new HashMap<String, String>();
					requestMap.put("equipNumber", meterAddr);
					requestMap.put("loop", loopTypeStr3);
					PageCmdResult result = cmdService.generateCmdUrlNoAddr(ApiTemplateCons.loopOn, meterAddr, requestMap);
					if(result != null && result.getSuccess()){
						list.add(roomMeterSwitchingManagent);
					}
					cmdResults.add(result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(list != null){
			replaceThreeControlMeterData(list, "0.0000");
		}
		return Response.ok(cmdResults);
	}
	
	
	/**
	 * 回路关闸
	 * @param meterAddr
	 * @return
	 */
	@PostMapping("loopOff")
	@ApiOperation(value="回路关闸", notes="")
	public Response<?> loopOff(@RequestBody List<RoomMeterSwitchingManagent> meterInfo) {
		List<PageCmdResult> cmdResults = new ArrayList<PageCmdResult>();
		List<RoomMeterSwitchingManagent> list = null;
		if(meterInfo.isEmpty()){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "未选择仪表");
		}
		for (RoomMeterSwitchingManagent roomMeterSwitchingManagent : meterInfo) {
			String meterAddr = roomMeterSwitchingManagent.getMeterAddr();
			String loopTypeStr = roomMeterSwitchingManagent.getLoopTypeStr();
			String loopTypeStr2 = roomMeterSwitchingManagent.getLoopTypeStr2();
			String loopTypeStr3 = roomMeterSwitchingManagent.getLoopTypeStr3();
			try {
				if(loopTypeStr != null && !loopTypeStr.isEmpty() && loopTypeStr != ""){
					Map<String, String> requestMap = new HashMap<String, String>();
					requestMap.put("equipNumber", meterAddr);
					requestMap.put("loop", loopTypeStr);
					PageCmdResult result = cmdService.generateCmdUrlNoAddr(ApiTemplateCons.loopOff, meterAddr, requestMap);
					cmdResults.add(result);
				}
				if(loopTypeStr2 != null && !loopTypeStr2.isEmpty() && loopTypeStr2 != ""){
					Map<String, String> requestMap = new HashMap<String, String>();
					requestMap.put("equipNumber", meterAddr);
					requestMap.put("loop", loopTypeStr2);
					PageCmdResult result = cmdService.generateCmdUrlNoAddr(ApiTemplateCons.loopOff, meterAddr, requestMap);
					cmdResults.add(result);
				}
				if(loopTypeStr3 != null && !loopTypeStr3.isEmpty() && loopTypeStr3 != ""){
					Map<String, String> requestMap = new HashMap<String, String>();
					requestMap.put("equipNumber", meterAddr);
					requestMap.put("loop", loopTypeStr3);
					PageCmdResult result = cmdService.generateCmdUrlNoAddr(ApiTemplateCons.loopOff, meterAddr, requestMap);
					if(result != null && result.getSuccess()){
						list.add(roomMeterSwitchingManagent);
					}
					cmdResults.add(result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(list != null){
			replaceThreeControlMeterData(list, "1.0000");
		}
		return Response.ok(cmdResults);
	}
	
	public void replaceThreeControlMeterData(List<RoomMeterSwitchingManagent> list, String openOrClose){
		readService.replaceThreeControlMeterData(list, openOrClose);
	}
}
