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

import jp.sourceforge.reedsolomon.RsEncode;

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
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.ReadExcleUtils;
import cn.com.tw.saas.serv.common.utils.cons.RegExp;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.excel.RoomExcel;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.service.room.RoomService;

@RestController
@RequestMapping("room")
@Api(description = "房间管理接口")
public class RoomController {

	@Autowired
	private RoomService roomService;
	
	@ApiOperation(value = "查询房间账户", notes = "")
	@GetMapping("AllInfo")
	public Response<?> selectAllInfo(CustRoomParam param){
		Map<String,Object> result = roomService.selectAllInfo(param);
		return Response.ok(result);
	}
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
		List<Room> list = null;
		Room param = new Room();
		param.setRegionId(regionId);
		try {
			list = roomService.selectByEntity(param);
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
		List<Room> list = null;
		try {
			list = roomService.selectByPage(page);
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
			roomService.deleteById(Id);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	
	/**
	 * 废弃房间
	 * 
	 * @param roomId
	 * @return
	 */
	@ApiOperation(value = "废弃房间", notes = "")
	@DeleteMapping("discard/{Id}")
	@ResponseBody
	public Response<?> discard(@PathVariable String Id) {
		if (StringUtils.isEmpty(Id)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		try {
			roomService.discardById(Id);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	
	@ApiOperation(value = "楼栋下所有没有绑定客户的房间", notes = "")
	@GetMapping("roomCust/{regionId}")
	@ResponseBody
	public Response<?> selectRoomCustByRegionId(@PathVariable String regionId){
		if (StringUtils.isEmpty(regionId)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		List<Room> list = roomService.selectRoomCustByRegionId(regionId);
		
		return Response.ok(list);
	}
	
	
	@ApiOperation(value = "楼栋下所有没有绑定仪表的房间", notes = "")
	@GetMapping("meterCust/{regionId}")
	@ResponseBody
	public Response<?> selectRoomMeterByRegionId(@PathVariable String regionId){
		if (StringUtils.isEmpty(regionId)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		List<Room> list = roomService.selectRoomMeterByRegionId(regionId);
		
		return Response.ok(list);
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
	public Response<?> update(@RequestBody Room newRoom) {
		int result = 0;
		if(!newRoom.getRoomNumber().matches(RegExp.roomNumberExp)){		
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "修改失败！房间号格式错误");
		}
		try {
			result = roomService.update(newRoom);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok(result);
	}
	
	/**
	 * 服务端修改 已入住人数
	 * @param newRoom
	 * @return
	 */
	@ApiOperation(value = "修改房间已入住人数", notes = "")
	@PutMapping("people")
	@ResponseBody
	public Response<?> updatePeopleNumber(@RequestBody Room newRoom) {
		int result = 0;
		try {
			result = roomService.update(newRoom);
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
		Room room = null;
		try {
			room = roomService.selectById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(room);
	}

	@GetMapping("")
	public Response<?> selectSaasMeter(Room room) {
		List<Room> rooms = roomService.selectByEntity(room);
		return Response.ok(rooms);
	}

	/**
	 * app 根据机构和楼栋Id查询
	 * 
	 * @param regionId
	 * @return
	 */
	@GetMapping("meter")
	public Response<?> selectRoomMeter(Room room) {
		List<Room> saasRooms = roomService.selectRoomMeterByregionId(room.getRegionId(),room.getOrgId());
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
	public Response<?> addRoom(@RequestBody Room saasRoom) {
		if(!saasRoom.getRoomNumber().matches(RegExp.roomNumberExp)){
			
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "添加失败！房间号格式错误");
		
		}
		if(saasRoom.getPeopleLimit()!=null && saasRoom.getPeopleLimit()<1)
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "添加失败！可入住人数至少为1");
		try {
			roomService.insertSelect(saasRoom);
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
		if(StringUtils.isEmpty(regionId)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "请选择楼栋");
		}
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
					//如果房间名称为空，则默认房间号为房间名称
					if(StringUtils.isEmpty(roomExcel.getRoomName())){
						roomExcel.setRoomName(roomExcel.getRoomNumber());
					}else{
						//如果房间名称不为空,每个汉字前后都要去空
						roomExcel.setRoomName(roomExcel.getRoomName().replaceAll(" ", ""));
					}
					//如果房间面积为空，那么默认0
					if(StringUtils.isEmpty(roomExcel.getArea())){
						roomExcel.setArea("0");
					}//如果不匹配正数
//					else if(!(roomExcel.getArea()).matches( RegExp.mathExp)){
//  						roomExcel.setArea("0");
//					}
					else{
						roomExcel.setArea(roomExcel.getArea());
					}
					//如果可住人数为空，那么默认为4
					if(StringUtils.isEmpty(roomExcel.getPeopleLimit())){
						roomExcel.setPeopleLimit("4");
					}else{
						roomExcel.setPeopleLimit(roomExcel.getPeopleLimit());
					}
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
		List<RoomExcel> errorRecord = new ArrayList<RoomExcel>();

		for (int i = 0; i < dataList.size(); i++) {
			RoomExcel roomTemp = dataList.get(i);
			// 验证房间号
			if (StringUtils.isEmpty(roomTemp.getRoomNumber())) {
				errorNum++;
				roomTemp.setMessage("房间号不能为空");
				errorRecord.add(roomTemp);
				continue;
			}
			/**
			 * 判断房间的输入类型,不为空的情况下房间号只可为字母、数字、“-”、“_”、“~”
			 */
			
			else if(!(roomTemp.getRoomNumber()).matches(RegExp.roomNumberExp)
					||roomTemp.getRoomNumber().length()>10){
				errorNum++;
				roomTemp.setMessage("房间号命名不符合规则");
				errorRecord.add(roomTemp);
				continue;
			}
			
			
			/*if(roomTemp.getRoomNumber()!=null){
				String b= roomTemp.getRoomNumber();
				
				for(int j=0;i<b.length();j++){
					char a = b.charAt(j);
					if("-".equals(b.charAt(0))){
						errorNum++;
						roomTemp.setMessage("-不可以为房间号第一位");
						errorRecord.add(roomTemp);
						continue;
					}else if("-".equals(b.charAt(b.length()-1))){
						errorNum++;
						roomTemp.setMessage("-不可以为房间号最后一位");
						errorRecord.add(roomTemp);
						continue;
					}else {
						if(a>=48 && a<=57){
							System.out.println("输入了一个数字");
						}else if((a>=65 && a<=90)){
							System.out.println("输入了一个大写字母");
						}else{
							errorNum++;
							roomTemp.setMessage("房间号只能为数字和大写字母");
							errorRecord.add(roomTemp);
							continue;
						}
					}
				}		
				
			}*/
			// 
			if (StringUtils.isEmpty(roomTemp.getRoomUse())) {
				errorNum++;
				roomTemp.setMessage("房间类型为空");
				errorRecord.add(roomTemp);
				continue;
			}
			if (!roomTemp.getRoomUse().equals("1") && !roomTemp.getRoomUse().equals("0")) {
				errorNum++;
				roomTemp.setMessage("房间类型格式错误");
				errorRecord.add(roomTemp);
				continue;
			}
			if(!StringUtils.isEmpty(roomTemp.getArea())){
				if(!roomTemp.getArea().matches(RegExp.numberExp)
						||roomTemp.getArea().length()>8){
					errorNum++;
					roomTemp.setMessage("面积格式错误");
					errorRecord.add(roomTemp);
					continue;
				}
			}else{
				errorNum++;
				roomTemp.setMessage("面积不能为空");
				errorRecord.add(roomTemp);
				continue;
			}
			/*if (!roomTemp.getArea().matches(RegExp.mathExp)){
				errorNum++;
				roomTemp.setMessage("面积格式错误");
				errorRecord.add(roomTemp);
				continue;
			}*/		
			if (roomTemp.getRoomUse().equals("0")){
				//商铺
				if ((!StringUtils.isEmpty(roomTemp.getPeopleLimit()) && !roomTemp.getPeopleLimit().matches(RegExp.positiveIntegerExp))
						|| roomTemp.getPeopleLimit().length()>11){
					errorNum++;
					roomTemp.setMessage("可入住人数格式错误");
					errorRecord.add(roomTemp);
					continue;
				}
			}else{
				//宿舍
				if (StringUtils.isEmpty(roomTemp.getPeopleLimit())) {
					errorNum++;
					roomTemp.setMessage("宿舍可入住人数必填");
					errorRecord.add(roomTemp);
					continue;
				} else if(!roomTemp.getPeopleLimit().matches(RegExp.positiveIntegerExp)
						|| roomTemp.getPeopleLimit().length()>11){
					errorNum++;
					roomTemp.setMessage("可入住人数格式错误");
					errorRecord.add(roomTemp);
					continue;
				}
			}
			
			
			// 一条条执行导入
			try {
				String returnCode = roomService.insertRoom(roomTemp, regionId, orgId);
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