package cn.com.tw.saas.serv.controller.terminal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.http.HttpGetReq;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.saas.serv.common.utils.MapUtil;
import cn.com.tw.saas.serv.common.utils.ReadExcleUtils;
import cn.com.tw.saas.serv.common.utils.SignatureUtil;
import cn.com.tw.saas.serv.common.utils.cons.RegExp;
import cn.com.tw.saas.serv.entity.excel.MeterExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.terminal.SaasMeter;
import cn.com.tw.saas.serv.service.terminal.SaasMeterService;

@RestController
@RequestMapping("meterOld")
@Api(description = "仪表管理接口")
public class SaasMeterController {
	
	private String URL = Env.getVal("paas.url");

	@Autowired
	private SaasMeterService saasMeterService;

	private static String APPKEY = Env.getVal("paas.appKey");

	private static String SECRET = Env.getVal("paas.secret");

	/**
	 * 仪表列表
	 * 
	 * @param page
	 * @return
	 */
	@ApiOperation(value = "获取仪表列表", notes = "")
	@GetMapping("page")
	@ResponseBody
	public Response<?> selectBypage(Page page) {
		try {
			List<SaasMeter> list = saasMeterService.selectByPage(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}

	/**
	 * 新增仪表
	 * 
	 * @param meter
	 * @param br
	 * @return
	 */
	@ApiOperation(value = "新增仪表", notes = "")
	@PostMapping("")
	public Response<?> addMeter(@RequestBody SaasMeter meter, BindingResult br) {
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		// 仪表未录入
		if (saasMeterService.selectByMeterAddr(meter.getMeterAddr()) == null) {
			// 访问paas获取数据
			Map<String, String> requestMap = new HashMap<String, String>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			requestMap.put("equipNumber", meter.getMeterAddr());
			requestMap.put("appKey", Env.getVal("paas.appKey"));
			requestMap.put("timestamp", sdf.format(new Date()));
			String businessNo = CommUtils.getUuid();
			requestMap.put("businessNo", businessNo);
			String sign = SignatureUtil.generateSign(requestMap, SECRET);
			requestMap.put("sign", sign);
			/*Response<?> data = feignMeterService.selectAbnorDate(meter.getMeterAddr(), APPKEY,
					requestMap.get("timestamp"), businessNo, sign);*/
			String uri = "/usepaas/terminalinfo/";
	        HttpGetReq req = new HttpGetReq(URL+"/"+uri+"?"+MapUtil.mapJoin(requestMap, false, false));
	        String result;
			try {
				 result = req.excuteReturnStr();
				 Response response = JsonUtils.jsonToPojo(result, Response.class);
					// paas查到结果
					if (null != response.getData()) {
						Map<String, Object> map = (Map<String, Object>) response.getData();
						meter = SaasMeter.splicing(meter, map);
						saasMeterService.insert(meter);
					}
					else{
						return Response.retn(ResultCode.PARAM_VALID_ERROR, "无匹配的仪表编号");
					}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "仪表编号已存在");
		}
		return Response.ok();
	}

	/**
	 * 删除仪表
	 * 
	 * @param meterId
	 * @return
	 */
	@ApiOperation(value = "删除仪表", notes = "")
	@DeleteMapping("{meterId}")
	public Response<?> delete(@PathVariable String meterId) {
		if (StringUtils.isEmpty(meterId)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		try {
			saasMeterService.deleteById(meterId);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}

	@ApiOperation(value = "根据条件房间仪表关联", notes = "")
	@GetMapping("")
	public Response<?> selectSaasMeter(SaasMeter saasMeter) {
		List<SaasMeter> meters = saasMeterService.selectSaasMeter(saasMeter);
		return Response.ok(meters);
	}

	/**
	 * 仪表详情页
	 * 
	 * @param meterId
	 * @return
	 */
	@ApiOperation(value = "仪表详情", notes = "")
	@GetMapping("{meterId}")
	public Response<?> details(@PathVariable String meterId) {
		if (StringUtils.isEmpty(meterId)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		return Response.ok(saasMeterService.selectById(meterId));
	}

	/**
	 * 修改仪表信息
	 * 
	 * @param saasMeter
	 * @return
	 */
	@ApiOperation(value = "修改仪表信息", notes = "")
	@PutMapping()
	public Response<?> update(@RequestBody SaasMeter saasMeter) {
		if (StringUtils.isEmpty(saasMeter.getMeterId())) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		saasMeterService.updateSelect(saasMeter);
		return Response.ok();
	}

	@ApiOperation(value = "根据roomId和meterAddr校验", notes = "")
	@GetMapping("verify")
	public Response<?> selectByRoomIdAndMeterAddr(SaasMeter saasMeter) {
		SaasMeter meter = new SaasMeter();
		try {
			meter = saasMeterService.selectByRoomIdAndMeterAddr(saasMeter);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok(meter);
	}

	/**
	 * 查询所有
	 * 
	 * @param saasMeter
	 * @return
	 */
	@ApiOperation(value = "条件模糊查询", notes = "")
	@GetMapping("all")
	public Response<?> selectByLikeEntity(SaasMeter saasMeter) {
		List<SaasMeter> saasMeters = saasMeterService.selectByLikeEntity(saasMeter);
		return Response.ok(saasMeters);
	}

	@ApiOperation(value = "根据表号查询", notes = "")
	@GetMapping("No/{meterAddr}")
	public Response<?> selectByMeterAddr(@PathVariable String meterAddr) {
		SaasMeter saasMeter = saasMeterService.selectByMeterAddr(meterAddr);
		return Response.ok(saasMeter);
	}

	@ApiOperation(value = "通讯测试查询", notes = "")
	@GetMapping("communication/{meterAddr}/{page}")
	public Response<?> selectCommunicationTest(@PathVariable("meterAddr") String meterAddr,
			@PathVariable("page") String page) {
		List<SaasMeter> saasMeters = saasMeterService.selectCommunicationTest(meterAddr, page);
		return Response.ok(saasMeters);
	}

	/**
	 * 仪表导入
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "仪表导入", notes = "")
	@PostMapping("/import")
	@ResponseBody
	public Response<?> importExcel(@RequestParam MultipartFile file, HttpServletRequest request) {
		final int rowStart = 3;
		final int cellStrart = 0;
		Map<String, Object> res;
		// 文件名称
		String fileName = file.getOriginalFilename();
		if (org.springframework.util.StringUtils.isEmpty(fileName)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "上传失败！文件名为空");
		}
		if (!fileName.substring(fileName.indexOf(".") + 1, fileName.length()).equals("xls")
				&& !fileName.substring(fileName.indexOf(".") + 1, fileName.length()).equals("xlsx")) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "上传失败！文件格式不支持");
		}

		String tempPath = request.getSession().getServletContext().getRealPath("/") + "upload";
		File dir = new File(tempPath);
		if (!tempPath.endsWith(File.separator)) {
			tempPath = tempPath + File.separator;
		}
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String newFile = tempPath + fileName;
		File files = new File(newFile);
		try {
			FileCopyUtils.copy(file.getBytes(), files);
			// 解析excel文件
			List<Object> dataList = ReadExcleUtils.readExcel(newFile, new MeterExcel(), rowStart, cellStrart);
			List<MeterExcel> list = new ArrayList<MeterExcel>();
			if (dataList.size() > 0 && dataList != null) {
				for (int i = 0; i < dataList.size(); i++) {
					MeterExcel meterExcel = (MeterExcel) dataList.get(i);
					list.add(meterExcel);
				}
			} else {
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "解析失败！无有效数据");
			}
			res = importResearch(list);
			return Response.retn(ResultCode.OPERATION_IS_SUCCESS, "解析成功！导入结果如下", res);
		} catch (IOException e) {
			e.printStackTrace();
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "解析失败！报表信息异常");
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "解析失败！未知异常");
		} finally {
			try {
				FileUtils.forceDelete(files);
			} catch (IOException e) {
				e.printStackTrace();
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "上传失败！未知异常");
			}
		}
	}

	/**
	 * 导入判断
	 * 
	 * @param dataList
	 * @return
	 */
	private Map<String, Object> importResearch(List<MeterExcel> dataList) {
		// TODO 模拟已登录用户
		OrgUser user = new OrgUser();
		user.setOrgId("1");
		user.setUserId("111");
		user.setUserName("模拟用户");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 导入成功条数
		int successNum = 0;
		// 导入失败条数
		int errorNum = 0;
		// 异常记录
		List<MeterExcel> errorRecord = new ArrayList<MeterExcel>();
		
		for (int i = 0; i < dataList.size(); i++) {
			MeterExcel meterTemp = dataList.get(i);
			// 验证仪表编号
			if (StringUtils.isEmpty(meterTemp.getMeterAddr())) {
				errorNum++;
				meterTemp.setMessage("仪表编号为空");
				errorRecord.add(meterTemp);
				continue;
			}
			if (meterTemp.getMeterAddr().length() > 12) {
				errorNum++;
				meterTemp.setMessage("仪表编号过长");
				errorRecord.add(meterTemp);
				continue;
			}
			if (meterTemp.getMeterAddr().length() < 12) {
				if (!meterTemp.getMeterAddr().matches(RegExp.integerExp)) {
					errorNum++;
					meterTemp.setMessage("仪表编号格式错误");
					errorRecord.add(meterTemp);
					continue;
				}
				// 不足12位补0
				meterTemp.setMeterAddr(String.format("%0" + 12 + "d", Long.parseLong(meterTemp.getMeterAddr())));
			}
			// 验证仪表变比
			if (StringUtils.isEmpty(meterTemp.getElecCt()) || StringUtils.isEmpty(meterTemp.getElecPt())) {
				errorNum++;
				meterTemp.setMessage("仪表变比为空");
				errorRecord.add(meterTemp);
				continue;
			}
			if (!meterTemp.getElecCt().matches(RegExp.positiveIntegerExp) || !meterTemp.getElecPt().matches(RegExp.positiveIntegerExp)) {
				errorNum++;
				meterTemp.setMessage("仪表变比格式错误");
				errorRecord.add(meterTemp);
				continue;
			}
			// 一条条执行导入
			try {
				String returnCode = saasMeterService.importInsert(meterTemp);
				if (returnCode.equals("ok")) {
					successNum++;
				} else {
					errorNum++;
					meterTemp.setMessage(returnCode);
					errorRecord.add(meterTemp);
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorNum++;
				meterTemp.setMessage("未知异常");
				errorRecord.add(meterTemp);
			}
		}
		resultMap.put("successNum", successNum);
		resultMap.put("errorNum", errorNum);
		resultMap.put("errorRecord", errorRecord);
		return resultMap;
	}

	/**
	 * 房间号查询仪表
	 * 
	 * @param roomId
	 * @return
	 */
	@ApiOperation(value = "通过房间号查询仪表", notes = "")
	@GetMapping("selectByRoomid/{roomId}")
	@ResponseBody
	public Response<?> selectByRoomId(@PathVariable String roomId) {
		if (StringUtils.isEmpty(roomId)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		List<SaasMeter> list = null;
		try {
			list = saasMeterService.selectByRoomId(roomId);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常");
		}
		return Response.ok(list);
	}

	/**
	 * 获取PAAS平台仪表信息
	 * 
	 * @param meterId
	 * @return
	 */
	@ApiOperation(value = "获取PAAS平台仪表信息", notes = "")
	@GetMapping("/paasMeterInfo/{meterId}")
	@ResponseBody
	public Response<?> selectByPaasMeterInfo(@PathVariable String meterId) {
		if (StringUtils.isEmpty(meterId)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		Map<String, String> requestMap = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			requestMap.put("equipNumber", meterId);
			requestMap.put("appKey", APPKEY);
			requestMap.put("timestamp", sdf.format(new Date()));
			requestMap.put("businessNo", "4445");
			String sign = SignatureUtil.generateSign(requestMap, SECRET);
			requestMap.put("sign", sign);
			//Response<?> data=feignMeterService.selectAbnorDate(meterId, APPKEY, requestMap.get("timestamp"), "4445", sign);
			
	        String uri = "/usepaas/terminalinfo/";
	        HttpGetReq req = new HttpGetReq(URL+"/"+uri+"?"+MapUtil.mapJoin(requestMap, false, false));
	        String result = req.excuteReturnStr();
	        Response response = JsonUtils.jsonToPojo(result, Response.class);
			
			/*if(StringUtils.isEmpty(data.getData())){

				return Response.retn(MonitResultCode.DATA_EXISTS_NULL, "仪表不存在");
			} else {
				return data;
			}*/
	        return response;
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
	}

	/**
	 * 通过仪表编号模糊查询PAAS平台网关信息
	 * 
	 * @return
	 */
	@ApiOperation(value = " 通过仪表编号模糊查询PAAS平台网关信息", notes = "")
	@ResponseBody
	@GetMapping("like/{equipNumber}/{pages}")
	public Response<?> likeMeterNumber(@PathVariable String equipNumber, @PathVariable String pages) {
		if (StringUtils.isEmpty(equipNumber)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		Map<String, String> requestMap = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			requestMap.put("equipNumber", equipNumber);
			requestMap.put("pages", pages);
			requestMap.put("appKey", APPKEY);
			requestMap.put("timestamp", sdf.format(new Date()));
			requestMap.put("businessNo", "4445");
			String sign = SignatureUtil.generateSign(requestMap, SECRET);
			requestMap.put("sign", sign);
			/*Response<?> data = feignMeterService.selectLikeData(equipNumber, pages, APPKEY, requestMap.get("timestamp"),"4445", sign);
			if (StringUtils.isEmpty(data)) {
				return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常");
			} else {
				return data;
			}*/
			String uri = "/usepaas/likemeter/";
	        HttpGetReq req = new HttpGetReq(URL+"/"+uri+"?"+MapUtil.mapJoin(requestMap, false, false));
	        String result = req.excuteReturnStr();
	        Response response = JsonUtils.jsonToPojo(result, Response.class);
	        return response;
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "系统异常");
		}
	}
}
