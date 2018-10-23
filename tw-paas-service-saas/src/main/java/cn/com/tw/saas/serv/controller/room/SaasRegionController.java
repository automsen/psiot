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
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.common.utils.ReadExcleUtils;
import cn.com.tw.saas.serv.common.utils.cons.RegExp;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.excel.RegionExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.room.RoomTree;
import cn.com.tw.saas.serv.entity.room.SaasRegion;
import cn.com.tw.saas.serv.service.room.RoomService;
import cn.com.tw.saas.serv.service.room.SaasRegionService;

@RestController
@RequestMapping("region")
@Api(description = "区域楼栋管理接口")
public class SaasRegionController {

	@Autowired
	private  SaasRegionService saasRegionService;
	@Autowired
	private RoomService roomService;
	
	
	/**
	 * WEB树加载
	 * @param searchName
	 * @return
	 */
	@ApiOperation(value="区域楼栋树加载", notes="")
	@GetMapping("tree")
	@ResponseBody
	public Response<?> initRoomTree(String searchName){
		List<RoomTree> tree=null;
		try {
			tree=saasRegionService.loadRoomNodeTree(null, searchName);
		} catch (Exception e) {
			e.printStackTrace();
			Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR,"系统异常");
		}
		return Response.ok(tree);
	}
	/**
	 * Wap树加载
	 * @param searchName
	 * @return
	 */
	@ApiOperation(value="Wap树加载加载", notes="")
	@GetMapping("waptree")
	@ResponseBody
	public String intwapTree(){
		List<RoomTree>  menuList =null;
		List<RoomTree> nodeList = new ArrayList<RoomTree>(); 
		String json=null;
		try {
			 menuList =saasRegionService.loadRoomNodeTree(null, null);
			for(RoomTree node1 :  menuList ){  
			    boolean mark = false;  
			    for(RoomTree node2 :  menuList ){  
			        if(node1.getpId()!=null && node1.getpId().equals(node2.getId())){  
			            mark = true;  
			            if(node2.getChildren() == null)  
			                node2.setChildren(new ArrayList<RoomTree>());  
			            node2.getChildren().add(node1);   
			            break;  
			        }  
			    }  
			    if(!mark){  
			        nodeList.add(node1); 
			        
			    }  
			}  
			json=JsonUtils.objectToJson(nodeList);
			//转为json格式        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	/**
	 * 返回顶级房间
	 * @return
	 */
	@ApiOperation(value="返回楼栋集合", notes="")
	@GetMapping("toproom")
	@ResponseBody
	public Response<?> selectTopRoom(){
		List<SaasRegion> list=null;
		try {
			SaasRegion region = new SaasRegion();
			region.setOrgId("1");
			region.setIsTop((byte) 1);
			list = saasRegionService.selectByEntity(region);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(list);
	}
	/**
	 * 删除区域
	 * @param roomId
	 * @return
	 */
	@ApiOperation(value="删除楼栋", notes="")
	@DeleteMapping("{id}")
	@ResponseBody
	public Response<?> delete(@PathVariable String id){
		if(StringUtils.isEmpty(id)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		try {
			SaasRegion saasRegion = saasRegionService.selectById(id);
			Room saasRoom = new Room();
			saasRoom.setOrgId(saasRegion.getOrgId());
			saasRoom.setRegionNo(saasRegion.getRegionNo());
			List<Room> saasRooms = roomService.selectByEntity(saasRoom);
			if(saasRooms != null && saasRooms.size() > 0){
				return Response.retn(MonitResultCode.CANNOT_DELETE, "楼栋下存在房间不能删除！！");
			}
			saasRegionService.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常");
		}
		return Response.ok();
	}
	/**
	 * 修改房间信息
	 * @param saasRoom
	 * @return
	 */
	@ApiOperation(value="修改楼栋信息", notes="")
	@PutMapping()
	@ResponseBody
	public Response<?> update(@RequestBody SaasRegion saasRoom){
		if(StringUtils.isEmpty(saasRoom.getRegionNo())){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "");
		}
		try {
			saasRegionService.updateSelect(saasRoom);
		}catch(BusinessException e){
			return Response.retn(e.getCode(),e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常");
		}
		return Response.ok();
	}
	/**
	 * 添加楼栋
	 * @param saasRoom
	 * @return
	 */
	@ApiOperation(value="添加楼栋", notes="")
	@PostMapping()
	public Response<?> addRoom(@RequestBody SaasRegion saasRegion){
		try {
			if(saasRegion.getRegionNo().equals("1")){
				saasRegion.setIsTop(new Byte("1"));
			}else{
				saasRegion.setIsTop(new Byte("0"));				
				if(saasRegion.getRegionName()==saasRegionService.selectById(saasRegion.getId()).getRegionName()){
					return Response.retn(MonitResultCode.CANNOT_DELETE, "不能与上级楼栋名相同");
				}
			}
			
			Room saasRoom = new Room();
			saasRoom.setOrgId(saasRegion.getOrgId());
			saasRoom.setRegionNo(saasRegion.getRegionNo());
			List<Room> saasRooms = roomService.selectByEntity(saasRoom);
			if(saasRooms != null && saasRooms.size() > 0){
				return Response.retn(MonitResultCode.CANNOT_DELETE, "楼栋下存在房间不能添加子节点！！");
			}		
			SaasRegion regionParam=new SaasRegion();
			regionParam.setRegionName(saasRegion.getRegionName());
			regionParam.setPRegionNo(saasRegion.getRegionNo());
			regionParam.setOrgId(saasRegion.getOrgId());
			List<SaasRegion> saasRegions = saasRegionService.selectByEntity(regionParam);
			if(saasRegions!=null&&saasRegions.size()>0){
				return Response.retn(MonitResultCode.CANNOT_DELETE, "已存在相同的楼栋名");
			}
			
			
			saasRegionService.insertSelect(saasRegion);
			
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getMessage(), e.getMessage());
		}
		return Response.ok();
	}
	/**
	 * 手机App网关上线使用 获取顶级区域
	 * @return
	 */
	@ApiOperation(value="获取顶级区域", notes="")
	@GetMapping("getregion")
	@ResponseBody
	public Response<?> selectregion(){
		List<SaasRegion> list=null;
		try {
			SaasRegion region = new SaasRegion();
			JwtInfo info = JwtLocal.getJwt();
			String orgId = "1";
			if(info != null){
				orgId = (String) info.getExt("orgId") == null ? orgId : (String) info.getExt("orgId");
			}
			region.setOrgId(orgId);
			region.setIsTop((byte) 1);
			list = saasRegionService.selectByEntity(region);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(list);
	}
	/**
	 * 通过父节点查询楼栋
	 * @return
	 */
	@ApiOperation(value="通过父节点查询楼栋", notes="")
	@GetMapping("{regionNo}")
	@ResponseBody
	public Response<?> selectBypRegionNo(@PathVariable String regionNo){
		List<SaasRegion> list=null;
		try {
			SaasRegion region = new SaasRegion();
			JwtInfo info = JwtLocal.getJwt();
			String orgId = "1";
			if(info != null){
				orgId = (String) info.getExt("orgId") == null ? orgId : (String) info.getExt("orgId");
			}
			region.setOrgId(orgId);
			region.setPRegionNo(regionNo);
			list = saasRegionService.selectByEntity(region);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(list);
	}
	
	@ApiOperation(value="楼栋ID查询楼栋信息", notes="")
	@GetMapping("")
	public Response<?> selectPregion(String id){
		SaasRegion region = saasRegionService.selectById(id);
		return Response.ok(region);
	}
	
	@ApiOperation(value="查询楼栋信息", notes="")
	@GetMapping("all")
	public Response<?> selectSaasRegion(SaasRegion saasRegion){
		List<SaasRegion> list=null;
		try {
			list = saasRegionService.selectSaasRegion(saasRegion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(list);
	}
	
	
	
	@ApiOperation(value = "楼栋导入", notes = "")
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
			List<Object> dataList = ReadExcleUtils.readExcel(newFile, new RegionExcel(), rowStart, cellStrart);
			List<RegionExcel> list = new ArrayList<RegionExcel>();
			// 解析后有有效数据
			if (dataList.size() > 0 && dataList != null) {
				for (int i = 0; i < dataList.size(); i++) {
					RegionExcel RegionExcel = (RegionExcel) dataList.get(i);
					
					/**
					 * 如果楼栋为一级区域，则“楼栋层级”填1，且不需要填“上级楼栋编号”。
					 * 如果楼栋为非一级区域，那么楼栋层级填相对于所在一级的层级数即可，且“上级楼栋编号”为必填
					 */
					if("1".equals(RegionExcel.getTier())){
						RegionExcel.setTier("1");
						if(StringUtils.isEmpty(RegionExcel.getpRegionNumber())){
							RegionExcel.setpRegionNumber("");
						}else{
							RegionExcel.setpRegionNumber("");
						}
					}else{
						if(StringUtils.isEmpty(RegionExcel.getpRegionNumber())){
							return Response.retn(ResultCode.PARAM_VALID_ERROR, "上级楼栋编号必填");
						}else{
							RegionExcel.setpRegionNumber(RegionExcel.getpRegionNumber());
						}
					}
					
					list.add(RegionExcel);
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
	private Map<String, Object> importResearch(List<RegionExcel> dataList) {
		// 操作员信息
		String orgId = "1";
		JwtInfo info = JwtLocal.getJwt();
		orgId = (String) info.getExt("orgId");
		// 录入人
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())){
			user = new OrgUser();
			user.setOrgId(orgId);
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 导入成功条数
		int successNum = 0;
		// 导入失败条数
		int errorNum = 0;
		// 异常记录
		List<RegionExcel> errorRecord = new ArrayList<RegionExcel>();
		for (int i = 0; i < dataList.size(); i++) {
			RegionExcel regionTemp = dataList.get(i);
			//楼栋编号
			if (StringUtils.isEmpty(regionTemp.getRegionNumber())) {
				errorNum++;
				regionTemp.setMessage("楼栋编号为空");
				errorRecord.add(regionTemp);
				continue;
			}
			
			
			
			SaasRegion saasRegion = new SaasRegion();
			saasRegion.setRegionNumber(regionTemp.getRegionNumber());
			saasRegion.setOrgId(orgId);
			List<SaasRegion> saasRegions = saasRegionService.selectByEntity(saasRegion);
			if(saasRegions != null && saasRegions.size() > 0){
				errorNum++;
				regionTemp.setMessage("楼栋编号已存在");
				errorRecord.add(regionTemp);
				continue;
			}
			//楼栋编号匹配数字下划线字母
			if(!(regionTemp.getRegionNumber()).matches(RegExp.houseExp)){
				errorNum++;
				regionTemp.setMessage("楼栋编号不匹配");
				errorRecord.add(regionTemp);
				continue;
			}
			
			
			
			// 验证楼栋名称
			if (StringUtils.isEmpty(regionTemp.getRegionName())) {
				errorNum++;
				regionTemp.setMessage("楼栋名称为空");
				errorRecord.add(regionTemp);
				continue;
			}
			//楼栋不能为特殊字符
			else if(!(regionTemp.getRegionName()).matches(RegExp.nameExp)){
				errorNum++;
				regionTemp.setMessage("楼栋名称不匹配");
				errorRecord.add(regionTemp);
				continue;
			}
			// 楼栋层级
			if (StringUtils.isEmpty(regionTemp.getTier())) {
				errorNum++;
				regionTemp.setMessage("楼栋层级为空");
				errorRecord.add(regionTemp);
				continue;
			}//楼栋层级只能为正数
			else 
				if(!(regionTemp.getTier().matches(RegExp.positiveIntegerExp))){
				errorNum++;
				regionTemp.setMessage("楼栋层级只能为正数");
				errorRecord.add(regionTemp);
				continue;
			}
			/**
			 * 是否数字校验
			 */
			/*if(!isNumericzidai(regionTemp.getTier())){
				errorNum++;
				regionTemp.setMessage("楼栋层级不为数字");
				errorRecord.add(regionTemp);
				continue;
			}*/
			// 上级编号
			if (!"1".equals(regionTemp.getTier()) && StringUtils.isEmpty(regionTemp.getpRegionNumber())) {
				errorNum++;
				regionTemp.setMessage("上级编号为空");
				errorRecord.add(regionTemp);
				continue;
			}else if(!"1".equals(regionTemp.getTier()) && !(regionTemp.getpRegionNumber().matches(RegExp.houseExp))){
				errorNum++;
				regionTemp.setMessage("上级编号不匹配");
				errorRecord.add(regionTemp);
				continue;
			}else if("1".equals(regionTemp.getTier())&&!StringUtils.isEmpty(regionTemp.getpRegionNumber())){
				errorNum++;
				regionTemp.setMessage("楼栋层级为1的时候上级编号必须为空");
				errorRecord.add(regionTemp);
				continue;
			}
			
			
			
			// 一条条执行导入
			try {
				String returnCode = saasRegionService.insertRegion(regionTemp, user);
				if (returnCode.equals("ok")) {
					successNum++;
				} else {
					errorNum++;
					regionTemp.setMessage(returnCode);
					errorRecord.add(regionTemp);
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorNum++;
				regionTemp.setMessage("未知异常");
				errorRecord.add(regionTemp);
			}
		}
		resultMap.put("successNum", successNum);
		resultMap.put("errorNum", errorNum);
		resultMap.put("errorRecord", errorRecord);
		return resultMap;
	}
	
	/**
	 * 数字校验
	 * @param str
	 * @return
	 */
	private boolean isNumericzidai(String str) {
		for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
	}
	
}
