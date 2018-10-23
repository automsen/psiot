package cn.com.tw.paas.monit.controller.term;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.common.utils.ReadExcleUtils;
import cn.com.tw.paas.monit.entity.business.excel.NetEquipExcel;
import cn.com.tw.paas.monit.entity.business.org.NetEquipExpand;
import cn.com.tw.paas.monit.entity.db.baseEquipModel.BaseEquipModel;
import cn.com.tw.paas.monit.entity.db.org.NetEquip;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.sys.Dict;
import cn.com.tw.paas.monit.service.baseEquipModel.BaseEquipModelService;
import cn.com.tw.paas.monit.service.org.NetEquipService;
import cn.com.tw.paas.monit.service.org.OrgApplicationService;
import cn.com.tw.paas.monit.service.sys.DictService;

/**
 * 集抄设备
 * @author Administrator
 *
 */
@RestController
@RequestMapping("netEquip")
public class NetEquipController {

	@Autowired
	private NetEquipService netEquipService;
	@Autowired
	private DictService dictService;
	@Autowired
	private BaseEquipModelService baseEquipModelService;
	
	@Autowired
	private OrgApplicationService orgApplicationService;
	
	/**
	 * 集抄设备页面
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectNetEquipPage(Page page){
		List<NetEquipExpand> equipExpands = netEquipService.selectEquipByPage(page);
		page.setData(equipExpands);
		return Response.ok(page);
	}
	
	/**
	 * 集抄设备下拉选
	 * @return
	 */
	@GetMapping("all")
	public Response<?> selectNetEquipExpand(NetEquipExpand equipExpand){
		List<NetEquipExpand> equipExpands = netEquipService.selectNetEquipExpandAll(equipExpand);
		return Response.ok(equipExpands);
	}
	
	@GetMapping("query")
	public Response<?> selectNetEquipByAppId(@RequestParam String appKey){
		OrgApplication application = orgApplicationService.selectByAppKey(appKey);
		NetEquip param = new NetEquip();
		param.setAppId(application.getAppId());
		List<NetEquip> result = netEquipService.selectNetForApi(param);
		return Response.ok(result);
	}
	
	/**
	 * 集抄设备添加
	 * @param equipExpand
	 * @param br
	 * @return
	 */
	@PostMapping()
	public Response<?> addNetEquipExpand(@RequestBody @Valid NetEquipExpand equipExpand, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		/*if(!StringUtils.isEmpty(equipExpand.getEquipNumber())){
			if(equipExpand.getEquipNumber().length()==10||equipExpand.getEquipNumber().length()==11){
	              if(equipExpand.getEquipNumber().length()==10){
	                StringBuffer sb = new StringBuffer();
	                sb.append("00").append(equipExpand.getEquipNumber());
	                equipExpand.setEquipNumber(sb.toString());
	              } else if(equipExpand.getEquipNumber().length()==11){
	                StringBuffer sb = new StringBuffer();
	                sb.append("0").append(equipExpand.getEquipNumber());
	                equipExpand.setEquipNumber(sb.toString());
	              }
			} else{
				equipExpand.setEquipNumber(String.format("%0" + 12 + "d",Long.parseLong(equipExpand.getEquipNumber())));
			}
		}*/
		try {
			netEquipService.insertNetEquipExpand(equipExpand);
		} catch (BusinessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 集抄设备详情查询
	 * @param equipId
	 * @return
	 */
	@GetMapping("{equipId}")
	public Response<?> selectByEquipId(@PathVariable String equipId){
		NetEquipExpand equipExpand = netEquipService.selectByEquipId(equipId);
		return Response.ok(equipExpand);
	}
	
	@PutMapping()
	public Response<?> updateNetEquipExpand(@RequestBody @Valid NetEquip netEquip, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			netEquipService.updateSelect(netEquip);
		} catch (BusinessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 网关删除
	 * @param equipId
	 * @return
	 */
	@DeleteMapping("{equipId}")
	public Response<?> deleteTerminalEquip(@PathVariable String equipId){
		netEquipService.deleteById(equipId);
		return Response.ok();
	}
	
	
	
	/**
	 * 导入网关信息
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("importNetExcel")
	public Response<?> importExcel(@RequestParam MultipartFile file,HttpServletRequest request, HttpServletResponse response){
		  final int rowStart = 1;
		  final int cellStrart = 0;
		  Map<String, Object> res;
		// 文件名称
		String fileName = file.getOriginalFilename();
		if (org.springframework.util.StringUtils.isEmpty(fileName)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "importGatewayExcel uuid is null!");
		}
		String tempPath = request.getSession().getServletContext()
				.getRealPath("/")
				+ "upload";
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
			  try {
				// 解析excel文件
				List<Object> orggaList= ReadExcleUtils.readExcel(newFile, new NetEquipExcel(), rowStart, cellStrart);
			    List<NetEquipExcel> listorgExcels = new ArrayList<NetEquipExcel>();
				if(orggaList != null && orggaList.size() > 0){
			    	for (int i = 0; i < orggaList.size(); i++) {
			    		NetEquipExcel orgGatewayExcel = trimEntity((NetEquipExcel) orggaList.get(i));
			    		 listorgExcels.add(orgGatewayExcel);
			    	}
			    }else{
			    	return Response.retn(ResultCode.PARAM_VALID_ERROR, "读取失败，模板格式不正确");
			    }
			    Response<?>  resp = ImportInsert(listorgExcels);
			    res=(Map<String, Object>) resp.getData();
			  } catch (Exception e) {
				e.printStackTrace();
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "读取失败，模板格式不正确");
			}
		} catch (IOException e) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "上传出错");
		}
		 finally {
			try {
				FileUtils.forceDelete(files);
			} catch (IOException e) {
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "上传出错");
			}
		}
		if ((boolean) res.get("res")) {
			// 成功，刷新页面
			return Response.retn("000000",res.get("message").toString(),null);	
		}else {
			// 错误，跳往错误页面
		return Response.retn("111111",res.get("message").toString(),null);		
		}
	}
	 /**
	  * 导入去空格
	  * @param orgGatewayExcel
	  * @return
	  */
	 private NetEquipExcel trimEntity(NetEquipExcel orgGatewayExcel) {
		if (!StringUtils.isEmpty(orgGatewayExcel.getModelName())) {
			orgGatewayExcel.setModelName(orgGatewayExcel.getModelName().trim());
		}
		if (!StringUtils.isEmpty(orgGatewayExcel.getEquipCategCode())) {
			orgGatewayExcel.setEquipCategCode(orgGatewayExcel.getEquipCategCode().trim());
		}
		if (!StringUtils.isEmpty(orgGatewayExcel.getEquipTypeCode())) {
			orgGatewayExcel.setEquipTypeCode(orgGatewayExcel.getEquipTypeCode().trim());
		}
		if (!StringUtils.isEmpty(orgGatewayExcel.getEquipNumber())) {
			orgGatewayExcel.setEquipNumber(orgGatewayExcel.getEquipNumber().trim());
		}
		if (!StringUtils.isEmpty(orgGatewayExcel.getNetTypeCode())) {
			orgGatewayExcel.setNetTypeCode(orgGatewayExcel.getNetTypeCode().trim());
		}
		return orgGatewayExcel;
	}
	
	
	
	/**
	 * 导入数据验证
	 * @param file
	 * @return
	 */
	public Response<?> ImportInsert(@RequestBody List<NetEquipExcel> dataList) {
		  int successNumber=0;
		  int failNumber=0;
		  Map<String, Object> map = new HashMap<String, Object>();
		  //错误信息
		  StringBuilder message = new StringBuilder("");
		  map.put("message", "");
		  //提示信息
		  Boolean res = true;
		// 解析之后的excel集合
			try {
				if (dataList == null) {
					res = false;
					message.append("读取失败，模板格式不正确");
					map.put("res", false);
					map.put("message", message.toString() + "<br/>");
					return  Response.ok(map);
				}
			} catch (Exception e) {
				res = false;
				message.append("读取失败，模板格式不正确");
				map.put("message", message.toString() + "<br/>");
				map.put("res", res);
				e.printStackTrace();
				return  Response.ok(map);
			}
		  if(!res){
		    //有错误
		    map.put("res",false);
		    return Response.ok(map);
		  }
		  Boolean is=true;
		  try {
			  String regexIPPro="^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])"
			  		           + "\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
			  		           + "\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
			  		           + "\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9]):"
			  		           + "\\d{0,5}$";
			  String  regexNumber="^\\d+$";
			  Dict dict = new Dict();
			  for (int i = 0; i < dataList.size(); i++){
				  NetEquipExcel orgGatewayExcel =  dataList.get(i);
				  String uid=UUID.randomUUID().toString().replaceAll("-", "");
				  //上行网络类型
				  if(StringUtils.isEmpty(orgGatewayExcel.getNetTypeCode())){
					  message.append("第"+(i+2)+"行网关类型为空<br/>");
				        map.put("message", message.toString());
				        failNumber++;
				        continue;
				  }
				  //判断是否填写型号
				  if(StringUtils.isEmpty(orgGatewayExcel.getModelName())){
					  message.append("第"+(i+2)+"型号为空<br/>");
				        map.put("message", message.toString());
				        failNumber++;
				        continue;
				  }
				  //判断是否填写二级分类
				  if(StringUtils.isEmpty(orgGatewayExcel.getEquipTypeCode())){
					  message.append("第"+(i+2)+"二级分类为空<br/>");
				        map.put("message", message.toString());
				        failNumber++;
				        continue;
				  }
				  //判断是否填写设备一级分类
				  if(StringUtils.isEmpty(orgGatewayExcel.getEquipCategCode())){
					  message.append("第"+(i+2)+"设备一级分类为空<br/>");
				        map.put("message", message.toString());
				        failNumber++;
				        continue;
				  }
				  //判断是否填写设备编号
				  if(StringUtils.isEmpty(orgGatewayExcel.getEquipNumber())){
					  message.append("第"+(i+2)+"设备编号为空<br/>");
				        map.put("message", message.toString());
				        failNumber++;
				        continue;
				  }
				  //设备编号判重
				  String equipNumber = orgGatewayExcel.getEquipNumber();
				  if(!StringUtils.isEmpty(netEquipService.selectByEquipNumber(equipNumber))){
					  message.append("第"+(i+2)+"设备编号已存在<br/>");
				        map.put("message", message.toString());
				        failNumber++;
				        continue;
				  }
				  
				  //上行网络类型是否存在
				  dict.setDictCode(orgGatewayExcel.getNetTypeCode());
				  List<Dict> dicts = dictService.selectDictAll(dict);
				  if(dicts ==null || dicts.size() == 0){
					  message.append("第"+(i+2)+"上行网络类型不存在<br/>");
			          map.put("message", message.toString());
			          failNumber++;
			          continue;
				  }
				  //设备一级分类
				  dict.setDictCode(orgGatewayExcel.getEquipCategCode());
				  List<Dict> dicts1 = dictService.selectDictAll(dict);
				  if(dicts1 ==null || dicts1.size() == 0){
					  message.append("第"+(i+2)+"设备一级分类不存在<br/>");
			          map.put("message", message.toString());
			          failNumber++;
			          continue;
				  }
				  //设备二级分类
				  dict.setDictCode(orgGatewayExcel.getEquipTypeCode());
				  List<Dict> dicts2 = dictService.selectDictAll(dict);
				  if(dicts2 ==null || dicts2.size() == 0){
					  message.append("第"+(i+2)+"设备二级分类不存在<br/>");
			          map.put("message", message.toString());
			          failNumber++;
			          continue;
				  }
				  //型号类型
				  BaseEquipModel baseEquipModel = baseEquipModelService.selectByModelName(orgGatewayExcel.getModelName());
				  if(StringUtils.isEmpty(baseEquipModel)){
					  message.append("第"+(i+2)+"型号类型不存在<br/>");
			          map.put("message", message.toString());
			          failNumber++;
			          continue;
				  }
				  orgGatewayExcel.setModelId(baseEquipModel.getModelId());
				  
				  netEquipService.insertNetEquipExcel(orgGatewayExcel);
				  
			  }
		  }catch(Exception e){
			  e.printStackTrace();
			    is = false;
			    successNumber=0;
			    failNumber++;
			    map.put("res", is);
			    message.append("网关导入失败<br/>");
			    message.append("导入成功" + successNumber + "条。<br/>失败" + failNumber
			                + "条。");
			    map.put("message", message.toString() + "<br/>");
			    return Response.ok(map);
		  }
		  map.put("res", is);
		  message.append("导入成功"+successNumber+"条。<br/>失败" + failNumber + "条。");
		  map.put("message", message.toString());
		  return Response.ok(map);
	}
	
	/**
	 * 通过实体参数进行查询
	 * @param equip
	 * @param br
	 * @return
	 */
	@PostMapping("select")
	public Response<?> selectByFilterCondition(@RequestBody @Valid NetEquip equip, BindingResult br){
		List<NetEquip> list = netEquipService.selectByEntity(equip);
		return Response.ok(list);
	}
	
}
