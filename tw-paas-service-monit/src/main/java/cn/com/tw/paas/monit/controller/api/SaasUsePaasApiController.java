package cn.com.tw.paas.monit.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.business.ins.ReadTest;
import cn.com.tw.paas.monit.entity.business.org.NetEquipExpand;
import cn.com.tw.paas.monit.entity.business.org.TerminalEquipExpand;
import cn.com.tw.paas.monit.entity.db.org.NetEquip;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.service.org.NetEquipService;
import cn.com.tw.paas.monit.service.org.TerminalEquipService;
/**
 * 提供SAAS查询接口
 * @author Administrator
 *
 */
@RestController
@RequestMapping("usepaas")
public class SaasUsePaasApiController {

	@Autowired
	private TerminalEquipService terminalEquipService;
	@Autowired
	private NetEquipService netEquipService;
	
	private static Logger logger = LoggerFactory.getLogger(SaasUsePaasApiController.class);
	/**
	 * 通过仪表地址查询相关参数
	 * @param equipNumber
	 * @return
	 */
	@GetMapping("terminalinfo")
	public Response<?> getTerminalInfo(@RequestParam("equipNumber") String equipNumber){
		TerminalEquipExpand terminalEquip=null;
		if(StringUtils.isEmpty(equipNumber)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "设备编号为空");
		}
		try {
			terminalEquip=terminalEquipService.selectSaasTerminalInfo(equipNumber);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getTerminalInfo {}", e);
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常");
		}
		return Response.ok(terminalEquip);
	}
	
	/**
	 * 通过仪表地址查询相关参数
	 * @param equipNumber
	 * @return
	 */
	@PostMapping("terminalinfo")
	public Response<?> getTerminalInfo(@RequestBody ReadTest params){
		TerminalEquipExpand TerminalEquipExpand=null;
		TerminalEquip te = new TerminalEquip();
		if(StringUtils.isEmpty(params.getEquipNumber())){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "设备编号为空");
		}
		if(StringUtils.isEmpty(params.getAppId())){
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "app_key为空");
		}
		try {
			te.setAppId(params.getAppId());
			te.setEquipNumber(params.getEquipNumber());
			TerminalEquipExpand = terminalEquipService.selectSaasTerminalInfo(te);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getTerminalInfo {}", e);
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常");
		}
		return Response.ok(TerminalEquipExpand);
	}
	/**
	 * 通过网关编号获取信息
	 * @param equipNumber
	 * @return
	 */
	@GetMapping("netinfo")
	public Response<?> getNetInfo(@RequestParam("equipNumber") String equipNumber ){
		NetEquipExpand netEquip=null;
		if(StringUtils.isEmpty(equipNumber)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		try {
			netEquip=netEquipService.selectByEquipNumber(equipNumber);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常");
		}
		return Response.ok(netEquip);
	}
	/**
	 * 通过网关编号模糊查询
	 * @param equipNumber
	 * @return
	 */
	@GetMapping("likenet")
	public Response<?> getLikeNetInfo(@RequestParam("equipNumber") String equipNumber ){
		List<NetEquip> list=null;
		if(StringUtils.isEmpty(equipNumber)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		try {
			list=netEquipService.selectLikeEquipNumber(equipNumber);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常");
		}
		return Response.ok(list);
	}
	/**
	 * 通过仪表编号模糊查询
	 * @param equipNumber
	 * @return
	 */
	@GetMapping("likemeter")
	public Response<?> getLikeMeterInfo(@RequestParam("equipNumber") String equipNumber ,@RequestParam("pages") String pages ){
		List<TerminalEquip> list=null;
		if(StringUtils.isEmpty(equipNumber)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("equipNumber", equipNumber);
			map.put("pages", Integer.parseInt(pages));
			list=terminalEquipService.selectLikeEquipNumber(map);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常");
		}
		return Response.ok(list);
	}
	
}
