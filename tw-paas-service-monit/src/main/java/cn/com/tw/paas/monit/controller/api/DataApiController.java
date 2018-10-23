/*package cn.com.tw.paas.monit.controller.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.common.utils.DateStrUtils;
import cn.com.tw.paas.monit.common.utils.EquipQRCodeUtils;
import cn.com.tw.paas.monit.entity.business.read.ReadHistoryExtend;
import cn.com.tw.paas.monit.entity.business.read.ReadHistorySimple;
import cn.com.tw.paas.monit.entity.db.baseEquipModel.BaseEquipModel;
import cn.com.tw.paas.monit.entity.db.org.NetEquip;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.entity.db.read.ReadLast;
import cn.com.tw.paas.monit.service.baseEquipModel.BaseEquipModelService;
import cn.com.tw.paas.monit.service.org.NetEquipService;
import cn.com.tw.paas.monit.service.org.OrgApplicationService;
import cn.com.tw.paas.monit.service.org.TerminalEquipService;
import cn.com.tw.paas.monit.service.read.ReadService;

*//**
 * 数据查询接口
 * 
 * @author Cheng Qi Peng
 *
 *//*
@RestController
@RequestMapping("data")
public class DataApiController extends IssueValidateController {

	private Logger logger = LoggerFactory.getLogger(DataApiController.class);
	@Autowired
	private ReadService readService;
	@Autowired
	private BaseEquipModelService modelService;
	@Autowired
	private TerminalEquipService equipService;

	@Autowired
	private NetEquipService netEquipService;
	
	@Autowired
	private OrgApplicationService orgApplicationService;
	
	
	@GetMapping("/read/list")
	public Response<?> sellectReadLast(String equipNumber, int page, int rows){
		try {
			// 收到的数据
			Map<String, String> params = (Map<String, String>) request.getAttribute("params");
			// 查询参数
			ReadHistoryExtend param = new ReadHistoryExtend();
			OrgApplication application = (OrgApplication) request.getAttribute("application");
			// 必填参数
			String appId = application.getAppId();
			String orgId = application.getOrgId();
			String page = (String) params.get("page");
			String rows = (String) params.get("rows");
			System.out.println(equipNumber);
			if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(appId)) {
				return Response.retn("000XXX", "必要参数缺失");
			}
			if (StringUtils.isEmpty(page)){
				page = "1";
			}
			if (StringUtils.isEmpty(rows)){
				rows = "50";
			}
			param.setOrgId(orgId);
			param.setAppId(appId);
			String startTimeStr = params.get("startTime");
			String endTimeStr = params.get("endTime");
			param.setStartTime(new Date(new Long(startTimeStr)));
			param.setStartTimeStr(DateStrUtils.dateToTd(param.getStartTime(), DateStrUtils.fullFormat));
			param.setEndTime(new Date(new Long(endTimeStr)));
			param.setEndTimeStr(DateStrUtils.dateToTd(param.getEndTime(), DateStrUtils.fullFormat));
			// 可选参数
			String equipNumber = params.get("equipNumber");
			if (!StringUtils.isEmpty(equipNumber)) {
				param.setMeterAddr(equipNumber);
			}
			String itemCode = params.get("itemCode");
			if (!StringUtils.isEmpty(itemCode)) {
				param.setItemCode(itemCode);
			}
			// 分页查询
			Page pageData = new Page();
			pageData.setParamObj(param);
			pageData.setRows(Integer.valueOf(rows));
			pageData.setPage(Integer.valueOf(page));

			List<ReadLast> result = readService.selectReadLast(pageData);
			pageData.setData(result);
			pageData.setParamObj(null);
			return Response.ok(pageData);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn("XXXXXX", "未知异常");
		}
	}
	

	*//**
	 * 查询历史读数
	 * 
	 * @param request
	 * @return
	 *//*
	@GetMapping("read/history")
	public Response<?> selectHistory(HttpServletRequest request) {
		try {
			// 收到的数据
			Map<String, String> params = (Map<String, String>) request.getAttribute("params");
			// 查询参数
			ReadHistoryExtend param = new ReadHistoryExtend();
			OrgApplication application = (OrgApplication) request.getAttribute("application");
			// 必填参数
			String appId = application.getAppId();
			String orgId = application.getOrgId();
			String startTimeStr = params.get("startTime");
			String endTimeStr = params.get("endTime");
			String page = params.get("page");
			String rows = params.get("rows");
			
			if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(startTimeStr)
					|| StringUtils.isEmpty(endTimeStr)) {
				return Response.retn("000XXX", "必要参数缺失");
			}
			if (StringUtils.isEmpty(page)){
				page = "1";
			}
			if (StringUtils.isEmpty(rows)){
				rows = "50";
			}
			param.setOrgId(orgId);
			param.setAppId(appId);
			param.setStartTime(new Date(new Long(startTimeStr)));
			param.setStartTimeStr(DateStrUtils.dateToTd(param.getStartTime(), DateStrUtils.fullFormat));
			param.setEndTime(new Date(new Long(endTimeStr)));
			param.setEndTimeStr(DateStrUtils.dateToTd(param.getEndTime(), DateStrUtils.fullFormat));
			// 可选参数
			String equipNumber = params.get("equipNumber");
			if (!StringUtils.isEmpty(equipNumber)) {
				param.setMeterAddr(equipNumber);
			}
			String meterType = params.get("meterType");
			if (!StringUtils.isEmpty(meterType)) {
				param.setMeterType(meterType);
			}
			String itemCode = params.get("itemCode");
			if (!StringUtils.isEmpty(itemCode)) {
				param.setItemCode(itemCode);
			}
			// 分页查询
			Page pageData = new Page();
			pageData.setParamObj(param);
			pageData.setRows(Integer.valueOf(rows));
			pageData.setPage(Integer.valueOf(page));

			List<ReadHistorySimple> result = readService.selectHistoryForApi(pageData);
			pageData.setData(result);
			pageData.setParamObj(null);
			return Response.ok(pageData);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn("XXXXXX", "未知异常");
		}
	}

	*//**
	 * 查询设备型号
	 * 
	 * @param request
	 * @return
	 *//*
	@GetMapping("model")
	public Response<?> selectModel(HttpServletRequest request) {
		try {
			// 收到的数据
			Map<String, String> params = (Map<String, String>) request.getAttribute("params");
			// 查询参数
			BaseEquipModel param = new BaseEquipModel();
			OrgApplication application = (OrgApplication) request.getAttribute("application");
			// 必填参数
			String appId = application.getAppId();
			String orgId = application.getOrgId();
			if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(appId)) {
				return Response.retn("000XXX", "必要参数缺失");
			}
			// 可选参数
			String equipType = params.get("equipType");
			if (!StringUtils.isEmpty(equipType)) {
				param.setEquipType(equipType);
			}
			String equipCateg = params.get("equipCateg");
			if (!StringUtils.isEmpty(equipCateg)) {
				param.setEquipCateg(equipCateg);
			}
			List<BaseEquipModel> modelList = modelService.selectBaseEquipModelAll(param);
			return Response.ok(modelList);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn("XXXXXX", "未知异常");
		}
	}

	*//**
	 * 查询终端设备
	 * 
	 * @param request
	 * @return
	 *//*
	@GetMapping("terminalEquip")
	public Response<?> selectTerminalEquip(HttpServletRequest request) {
		try {
			// 收到的数据
			Map<String, String> params = (Map<String, String>) request.getAttribute("params");
			// 查询参数
			TerminalEquip param = new TerminalEquip();
			OrgApplication application = (OrgApplication) request.getAttribute("application");
			// 必填参数
			String appId = application.getAppId();
			String orgId = application.getOrgId();
			if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(appId)) {
				return Response.retn("000XXX", "必要参数缺失");
			}
			param.setAppId(appId);
			param.setOrgId(orgId);
			// 可选参数
			// String equipType = params.get("equipType");
			// if (!StringUtils.isEmpty(equipType)) {
			// param.setEquipType(equipType);
			// }
			// String equipCateg = params.get("equipCateg");
			// if (!StringUtils.isEmpty(equipCateg)) {
			// param.setEquipCateg(equipCateg);
			// }
			List<TerminalEquip> result = equipService.selectTerminalForApi(param);
			return Response.ok(result);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn("XXXXXX", "未知异常");
		}
	}

	*//**
	 * 查询终端设备
	 * 
	 * @param request
	 * @return
	 *//*
	@GetMapping("netEquip")
	public Response<?> selectNetEquip(HttpServletRequest request) {
		try {
			// 收到的数据
			Map<String, String> params = (Map<String, String>) request.getAttribute("params");
			// 查询参数
			NetEquip param = new NetEquip();
			OrgApplication application = (OrgApplication) request.getAttribute("application");
			// 必填参数
			String appId = application.getAppId();
			String orgId = application.getOrgId();
			if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(appId)) {
				return Response.retn("000XXX", "必要参数缺失");
			}
			param.setAppId(appId);
			param.setOrgId(orgId);
			// 可选参数
			// String equipType = params.get("equipType");
			// if (!StringUtils.isEmpty(equipType)) {
			// param.setEquipType(equipType);
			// }
			// String equipCateg = params.get("equipCateg");
			// if (!StringUtils.isEmpty(equipCateg)) {
			// param.setEquipCateg(equipCateg);
			// }
			List<NetEquip> result = netEquipService.selectNetForApi(param);
			return Response.ok(result);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn("XXXXXX", "未知异常");
		}
	}

	*//**
	 * 二维码扫描录入设备档案
	 * 
	 * @param request
	 * @return
	 *//*
	@PostMapping("qrcode/equip")
	public Response<?> saveEquipForQRcode(@RequestBody Map<String, String> params) {
		try {
			// 收到的数据
			String equipCode = params.get("equipCode");
			String netCode = params.get("netCode");
			String appKey = params.get("appKey");
			
			OrgApplication application = orgApplicationService.selectByAppKey(appKey);
			// 必填参数
			String appId = application.getAppId();
			String orgId = application.getOrgId();
			if (StringUtils.isEmpty(equipCode) || StringUtils.isEmpty(netCode) || StringUtils.isEmpty(appKey) || StringUtils.isEmpty(orgId) || StringUtils.isEmpty(appId)) {
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "必要参数缺失");
			}
			Map<String, String> paramMap = new HashMap<String, String>();
			String equipCode = params.get("equipCode");
			String netCode = params.get("netCode");
			boolean checkFlag = false;
			if (!StringUtils.isEmpty(equipCode)) {
				paramMap = EquipQRCodeUtils.translate(equipCode);
				if (!StringUtils.isEmpty(paramMap.get("XH"))) {
					checkFlag = true;
				} else {
					checkFlag = false;
				}
				if (!StringUtils.isEmpty(paramMap.get("No"))) {
					checkFlag = true;
				} else {
					checkFlag = false;
				}
				if (!StringUtils.isEmpty(netCode)) {
					paramMap.put("netNumber", netCode);
				}
			}
			if (!checkFlag) {
				return Response.retn("000XXX", "必要参数缺失");
			}
			int result = equipService.saveEquipForQRcode(paramMap, application);
			if (result == 1) {
				return Response.ok();
			} else {
				return Response.retn("XXXXXX", "未知异常");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn("XXXXXX", "未知异常");
		}
	}
}
*/