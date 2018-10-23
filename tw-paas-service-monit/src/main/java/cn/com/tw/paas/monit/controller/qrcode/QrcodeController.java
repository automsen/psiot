package cn.com.tw.paas.monit.controller.qrcode;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.common.utils.EquipQRCodeUtils;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.service.baseEquipModel.BaseEquipModelService;
import cn.com.tw.paas.monit.service.org.NetEquipService;
import cn.com.tw.paas.monit.service.org.OrgApplicationService;
import cn.com.tw.paas.monit.service.org.TerminalEquipService;
import cn.com.tw.paas.monit.service.read.ReadService;

@RestController
@RequestMapping("qrcode")
public class QrcodeController {
	
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
	
	/**
	 * 二维码扫描录入设备档案
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("equip")
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
