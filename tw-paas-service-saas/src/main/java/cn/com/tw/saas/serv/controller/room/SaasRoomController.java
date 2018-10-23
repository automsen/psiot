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
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.ReadExcleUtils;
import cn.com.tw.saas.serv.entity.excel.RoomExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.SaasRoom;
import cn.com.tw.saas.serv.service.room.SaasRoomService;

@Deprecated
@RestController
@RequestMapping("roomOld")
@Api(description = "房间管理接口")
public class SaasRoomController {

	@Autowired
	private SaasRoomService saasRoomService;

	/**
	 * 通过楼栋ID查询房间
	 * 
	 * @param pRoomId
	 * @return
	 */
	@ApiOperation(value = "通过楼栋ID查询房间", notes = "")
	@GetMapping("getregion/{regionId}")
	@ResponseBody
	public Response<?> selectByProomId(@PathVariable String regionId) {
		if (StringUtils.isEmpty(regionId)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		List<SaasRoom> list = null;
		SaasRoom param = new SaasRoom();
		param.setOrgId("1");
		param.setRegionId(regionId);
		try {
			list = saasRoomService.selectByEntity(param);
			if (list == null) {
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "该楼栋下面无房间");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		return Response.ok(list);
	}

	@ApiOperation(value = "获取房间列表", notes = "")
	@GetMapping("page")
	@ResponseBody
	public Response<?> selectByPage(Page page) {
		List<SaasRoom> list = null;
		try {
			list = saasRoomService.selectByPage(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}

	/**
	 * 删除房间
	 * 
	 * @param roomId
	 * @return
	 */
	@ApiOperation(value = "删除房间", notes = "")
	@DeleteMapping("{Id}")
	@ResponseBody
	public Response<?> delete(@PathVariable String Id) {
		if (StringUtils.isEmpty(Id)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		try {
			saasRoomService.deleteById(Id);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}

	/**
	 * 修改房间信息
	 * 
	 * @param newRoom
	 * @return
	 */
	@ApiOperation(value = "修改房间信息", notes = "")
	@PutMapping()
	@ResponseBody
	public Response<?> update(@RequestBody SaasRoom newRoom) {
		int result = 0;
		if (StringUtils.isEmpty(newRoom.getRegionId())) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		try {
			result = saasRoomService.update(newRoom);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok(result);
	}

	/**
	 * 编辑
	 * 
	 * @param roomId
	 * @return
	 */
	@ApiOperation(value = "编辑回显", notes = "")
	@GetMapping("edit/{id}")
	@ResponseBody
	public Response<?> getedit(@PathVariable String id) {
		SaasRoom saasRoom = null;
		try {
			saasRoom = saasRoomService.selectById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(saasRoom);
	}

	@GetMapping("")
	public Response<?> selectSaasMeter(SaasRoom saasRoom) {
		List<SaasRoom> saasRooms = saasRoomService.selectByEntity(saasRoom);
		return Response.ok(saasRooms);
	}

	/**
	 * app 根据楼栋Id查询
	 * 
	 * @param regionId
	 * @return
	 */
	@GetMapping("meter")
	public Response<?> selectRoomMeter(String regionId) {
		List<SaasRoom> saasRooms = saasRoomService.selectRoomMeterByregionId(regionId);
		return Response.ok(saasRooms);
	}

	/**
	 * 添加房间
	 * 
	 * @param saasRoom
	 * @return
	 */
	@ApiOperation(value = "添加房间", notes = "")
	@PostMapping()
	public Response<?> addRoom(@RequestBody SaasRoom saasRoom) {
		try {
			saasRoomService.insertSelect(saasRoom);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}

	/**
	 * 房间导入
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "房间导入", notes = "")
	@PostMapping("/import")
	@ResponseBody
	public Response<?> importExcel(@RequestParam MultipartFile file, HttpServletRequest request,
			@RequestParam(name = "regionId") String regionId) {
		final int rowStart = 4;
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
			List<Object> dataList = ReadExcleUtils.readExcel(newFile, new RoomExcel(), rowStart, cellStrart);
			List<RoomExcel> list = new ArrayList<RoomExcel>();
			// 解析后有有效数据
			if (dataList.size() > 0 && dataList != null) {
				for (int i = 0; i < dataList.size(); i++) {
					RoomExcel roomExcel = (RoomExcel) dataList.get(i);
					list.add(roomExcel);
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
	private Map<String, Object> importResearch(List<RoomExcel> dataList, String regionId) {
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
		List<RoomExcel> errorRecord = new ArrayList<RoomExcel>();
		for (int i = 0; i < dataList.size(); i++) {
			RoomExcel roomTemp = dataList.get(i);
			// 验证房间号
			if (StringUtils.isEmpty(roomTemp.getRoomNumber())) {
				errorNum++;
				roomTemp.setMessage("房间号为空");
				errorRecord.add(roomTemp);
				continue;
			}
			// 一条条执行导入
			try {
				String returnCode = saasRoomService.insertRoom(roomTemp, regionId, user);
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