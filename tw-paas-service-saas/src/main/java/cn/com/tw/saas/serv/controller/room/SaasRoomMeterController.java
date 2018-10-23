package cn.com.tw.saas.serv.controller.room;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.ReadExcleUtils;
import cn.com.tw.saas.serv.entity.business.archives.RoomMeterChangeParam;
import cn.com.tw.saas.serv.entity.excel.RoomMeterExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.SaasRoomMeter;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.service.org.OrgUserService;
import cn.com.tw.saas.serv.service.room.SaasRoomMeterService;
import cn.com.tw.saas.serv.service.terminal.MeterService;

@RestController
@RequestMapping("roomMeter")
@Api(description = "房间仪表关联")
public class SaasRoomMeterController {

//	@Autowired
//	private SaasMeterService meterService;
	@Autowired
	private SaasRoomMeterService roomMeterService;
	@Autowired
	private MeterService meterService;
	@Autowired
	private OrgUserService orgUserService;

	/**
	 * 房间仪表关联 列表
	 * 
	 * @param page
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "")
	@GetMapping("page")
	public Response<?> selectByPage(Page page) {
		try {
			List<SaasRoomMeter> list = roomMeterService.selectByPage(page);
			for (SaasRoomMeter saasRoomMeter : list) {
				if(!StringUtils.isEmpty(saasRoomMeter.getElecAddr())){
					saasRoomMeter.setHaveMeter((byte)1);
				}
				if(!StringUtils.isEmpty(saasRoomMeter.getWaterAddr())){
					saasRoomMeter.setHaveMeter((byte)1);
				}
			}
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}

	@ApiOperation(value = "单个房间查询", notes = "")
	@GetMapping("{roomId}")
	public Response<?> selectByRoomId(@PathVariable String roomId) {
		List<Meter> list = new ArrayList<Meter>();
		try {
			Meter meter = new Meter();
			meter.setRoomId(roomId);
			list = meterService.selectByEntity(meter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(list);
	}

	@ApiOperation(value = "app新增", notes = "")
	@PostMapping("")
	public Response<?> appBind(@RequestBody SaasRoomMeter saasRoomMeter) {
		// 操作员
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = new OrgUser();
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = orgUserService.selectById(info.getSubject());
		}else{
			user.setOrgId(saasRoomMeter.getOrgId());
			user.setUserId("111");
			user.setUserName("test");
		}
		
		try {
			roomMeterService.appBind(saasRoomMeter,user);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	@ApiOperation(value = "取消或更换", notes = "")
	@PutMapping("change")
	public Response<?> change(@RequestBody RoomMeterChangeParam param) {
		int result = 0;
		try {
			/**
			 *  1、取消旧表 
		 	 *  2、更换新表
			 */
			if (param.getOperation().equals("1")){
				result = roomMeterService.cancel(param);
			}
			else if(param.getOperation().equals("2")){
				result = roomMeterService.replace(param);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(result);
	}
	
	/**
	 * 房间仪表关联导入
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "批量导入", notes = "")
	@PostMapping("/import")
	public Response<?> importExcel(@RequestParam MultipartFile file, HttpServletRequest request,
			@RequestParam(name = "regionId") String regionId) {
		final int rowStart = 6;
		final int cellStrart = 0;
		Map<String, Object> res;
		// 文件名称
		String fileName = file.getOriginalFilename();
		if (org.springframework.util.StringUtils.isEmpty(fileName)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "selectModelInfo uuid is null!");
		}
		fileName.substring(fileName.indexOf("."), fileName.length());

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
			List<Object> dataList = ReadExcleUtils.readExcel(newFile, new RoomMeterExcel(), rowStart, cellStrart);
			List<RoomMeterExcel> list = new ArrayList<RoomMeterExcel>();
			if (dataList.size() > 0 && dataList != null) {
				for (int i = 0; i < dataList.size(); i++) {
					RoomMeterExcel roomMeterExcel = (RoomMeterExcel) dataList.get(i);
					list.add(roomMeterExcel);
					}
				} else {
					return Response.retn(ResultCode.PARAM_VALID_ERROR, "解析失败！无有效数据");
				}
				res = importResearch(list, regionId);
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
	private Map<String, Object> importResearch(List<RoomMeterExcel> dataList, String regionId) {
		String orgId = "1";
		try {
			JwtInfo info = JwtLocal.getJwt();
			if(info != null){
				orgId = (String) info.getExt("orgId") == null ? orgId : (String) info.getExt("orgId");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 导入成功条数
		int successNum = 0;
		// 导入失败条数
		int errorNum = 0;
		// 异常记录
		List<RoomMeterExcel> errorRecord = new ArrayList<RoomMeterExcel>();
		for (int i = 0; i < dataList.size(); i++) {
			RoomMeterExcel roomTemp = dataList.get(i);
			// 验证房间号
			if (StringUtils.isEmpty(roomTemp.getRoomNumber())) {
				errorNum++;
				roomTemp.setMessage("房间号为空");
				errorRecord.add(roomTemp);
				continue;
			}
			if (StringUtils.isEmpty(roomTemp.getElecMeterAddr())
					&& StringUtils.isEmpty(roomTemp.getWaterMeterAddr())) {
				errorNum++;
				roomTemp.setMessage("仪表编号为空");
				errorRecord.add(roomTemp);
				continue;
			}
			// 一条条执行导入
			try {
				String returnCode = roomMeterService.excelBind(roomTemp, regionId, orgId);
				if (returnCode.equals("ok")) {
					successNum++;
				} else {
					errorNum++;
					roomTemp.setMessage(returnCode);
					errorRecord.add(roomTemp);
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorNum++;
				roomTemp.setMessage("未知异常");
				errorRecord.add(roomTemp);
			}
		}
		resultMap.put("successNum", successNum);
		resultMap.put("errorNum", errorNum);
		resultMap.put("errorRecord", errorRecord);
		return resultMap;
	}
}
