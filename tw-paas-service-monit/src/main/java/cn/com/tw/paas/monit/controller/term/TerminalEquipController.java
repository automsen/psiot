package cn.com.tw.paas.monit.controller.term;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.common.utils.ReadExcleUtils;
import cn.com.tw.paas.monit.common.utils.cons.DtuCommonParam;
import cn.com.tw.paas.monit.controller.api.IssueValidateController;
import cn.com.tw.paas.monit.entity.business.excel.TerminalEquipDtuExcel;
import cn.com.tw.paas.monit.entity.business.excel.TerminalEquipExcel;
import cn.com.tw.paas.monit.entity.business.ins.CommonParam;
import cn.com.tw.paas.monit.entity.business.org.TerminalEquipExpand;
import cn.com.tw.paas.monit.entity.business.replace.TermReplace;
import cn.com.tw.paas.monit.entity.db.base.Ins;
import cn.com.tw.paas.monit.entity.db.baseEquipModel.BaseEquipModel;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.NetEquip;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquipChildren;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquipParamDtu;
import cn.com.tw.paas.monit.service.baseEquipModel.BaseEquipModelService;
import cn.com.tw.paas.monit.service.inn.CmdHandleException;
import cn.com.tw.paas.monit.service.inn.CmdIssueService;
import cn.com.tw.paas.monit.service.inn.callback.CmdCallback;
import cn.com.tw.paas.monit.service.org.NetEquipService;
import cn.com.tw.paas.monit.service.org.OrgApplicationService;
import cn.com.tw.paas.monit.service.org.TerminalEquipParamDtuService;
import cn.com.tw.paas.monit.service.org.TerminalEquipService;

@RestController
@RequestMapping("terminal")
public class TerminalEquipController extends IssueValidateController{
	
	@Autowired
	private TerminalEquipService terminalEquipService;
	
	@Autowired
	private BaseEquipModelService baseEquipModelService;
	
	@Autowired
	private OrgApplicationService orgApplicationService;
	
	@Autowired
	private TerminalEquipService equipService;
	
	@Autowired
	private NetEquipService netEquipService;
	
	@Autowired
	private CmdIssueService cmdIssueService;
	
	@Resource(name="noPushCallback")
	private CmdCallback noPushCallback;
	
	@Resource(name="baseCallback")
	private CmdCallback baseCallback;
	
	@Autowired
	private TerminalEquipParamDtuService terminalEquipParamDtuService;
	
	/**
	 * 工厂机构id
	 */
	 /*@Value("${page.orgId}")
	 private String orgId;*/
	 /**
	 * 工厂应用id
	 */
	 /*@Value("${page.appId}")
	 private String appId;*/

	/**
	 * 设备页面
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectTerminalEquipPage(Page page){
		List<TerminalEquip> terminalEquips = terminalEquipService.selectByPage(page);
		page.setData(terminalEquips);
		return Response.ok(page);
	}
	
	/**
	 * 代理商页面使用  设备网络类型 统计查询
	 * @return
	 */
	@GetMapping("netTypeNum")
	public Response<?> selectNetTypeNumber(){
		List<TerminalEquipExpand> terminalEquips = terminalEquipService.selectNetTypeNumber();
		return Response.ok(terminalEquips);
	}
	
	/**
	 * 设备下拉选
	 * @return
	 */
	@GetMapping("all")
	public Response<?> selectTerminalEquip(TerminalEquip terminalEquip){
		List<TerminalEquipExpand> terminalEquips = terminalEquipService.selectByBean(terminalEquip);
		return Response.ok(terminalEquips);
	}
	
	@GetMapping("orgId")
	public Response<?> selectTerminalByOrgId(@RequestParam String orgId, String equipTypeCode){
		List<TerminalEquipExpand> terminalEquips = terminalEquipService.selectByOrgId(orgId, equipTypeCode);
		
		List<Map<String, String>> termList = new ArrayList<Map<String, String>>();
		for (TerminalEquipExpand term : terminalEquips) {
			Map<String, String> termMap = new HashMap<String, String>();
			termMap.put("equipNumber", term.getEquipNumber());
			termMap.put("equipName", term.getEquipName());
			termList.add(termMap);
		}
		
		return Response.ok(termList);
	}
	
	@GetMapping("appId")
	public Response<?> selectTerminalByAppId(@RequestParam String appId, String equipTypeCode){
		List<TerminalEquipExpand> terminalEquips = terminalEquipService.selectByAppId(appId, equipTypeCode);
		
		List<Map<String, String>> termList = new ArrayList<Map<String, String>>();
		for (TerminalEquipExpand term : terminalEquips) {
			Map<String, String> termMap = new HashMap<String, String>();
			termMap.put("equipNumber", term.getEquipNumber());
			termMap.put("equipName", term.getEquipName());
			termList.add(termMap);
		}
		
		return Response.ok(termList);
	}
	
	@GetMapping("query")
	public Response<?> queryTerminalByAppId(@RequestParam String appKey){
		OrgApplication application = orgApplicationService.selectByAppKey(appKey);
		TerminalEquip terminalEquip = new TerminalEquip();
		terminalEquip.setAppId(application.getAppId());
		List<TerminalEquip> result = equipService.selectTerminalForApi(terminalEquip);
		return Response.ok(result);
	}
	
	/**
	 * 设备添加
	 * @param terminalEquip
	 * @param br
	 * @return
	 */
	@PostMapping()
	public Response<?> addTerminalEquip(@RequestBody @Valid TerminalEquip terminalEquip, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		/*
		if(!StringUtils.isEmpty(terminalEquip.getEquipNumber())){
			if(terminalEquip.getEquipNumber().length()==10||terminalEquip.getEquipNumber().length()==11){
	              if(terminalEquip.getEquipNumber().length()==10){
	                StringBuffer sb = new StringBuffer();
	                sb.append("00").append(terminalEquip.getEquipNumber());
	                terminalEquip.setEquipNumber(sb.toString());
	              } else if(terminalEquip.getEquipNumber().length()==11){
	                StringBuffer sb = new StringBuffer();
	                sb.append("0").append(terminalEquip.getEquipNumber());
	                terminalEquip.setEquipNumber(sb.toString());
	              }
			} else{
				terminalEquip.setEquipNumber(String.format("%0" + 12 + "d",Long.parseLong(terminalEquip.getEquipNumber())));
			}
		}*/
		try {
			terminalEquipService.insertSelect(terminalEquip);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 设备详情
	 * @param equipId
	 * @return
	 */
	@GetMapping("{equipId}")
	public Response<?> selectById(@PathVariable String equipId){
		TerminalEquipExpand terminalEquip = terminalEquipService.selectById(equipId);
		return Response.ok(terminalEquip);
	}
	
	/**
	 * 通过设备编号查询
	 * @param equipNum
	 * @return
	 */
	@GetMapping("/number/{equipNum}")
	public Response<?> selectByEquipNum(@PathVariable("equipNum") String equipNum) {
		TerminalEquip equip = terminalEquipService.selectByEquipNumber(equipNum);
		return Response.ok(equip);
	}
	
	/**
	 * 设备修改
	 * @param terminalEquip
	 * @param br
	 * @return
	 */
	@PutMapping()
	public Response<?> updateOrgEquipment(@RequestBody @Valid TerminalEquip terminalEquip, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			terminalEquipService.updateSelect(terminalEquip);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	@DeleteMapping("{equipId}")
	public Response<?> deleteTerminalEquip(@PathVariable String equipId){
		terminalEquipService.deleteById(equipId);
		return Response.ok();
	}
	
	/**
	 * 导入
	 * @param file
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping("/importMeterExcel")
	@ResponseBody
	public Response<?> importExcel(@RequestParam MultipartFile file,HttpServletRequest request){
		final int rowStart = 1;
		final int cellStrart = 0;
		Map<String, Object> res;
		// 文件名称
		String fileName = file.getOriginalFilename();
		if (org.springframework.util.StringUtils.isEmpty(fileName)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "selectModelInfo uuid is null!");
		}
		String hzString=fileName.substring(fileName.indexOf("."), fileName.length());
		
		String tempPath = request.getSession().getServletContext().getRealPath("/")+ "upload";
		File dir = new File(tempPath);
		if (!tempPath.endsWith(File.separator)) {
			tempPath = tempPath + File.separator;
		}
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String newFile = tempPath + fileName;
		File files = new File(newFile);
		Map<String, Object> rst=null;
		try {
			FileCopyUtils.copy(file.getBytes(), files);
			// 解析excel文件
		    List<Object> dataList = ReadExcleUtils.readExcel(newFile, new TerminalEquipExcel(),rowStart, cellStrart);
		    List<TerminalEquipExcel> list=new ArrayList<TerminalEquipExcel>();
		    if(dataList.size()>0&&dataList!=null){
			    for (int i = 0; i < dataList.size(); i++) {
			    	TerminalEquipExcel terminalEquipExcel=trimEntity((TerminalEquipExcel) dataList.get(i));
					list.add(terminalEquipExcel);
				}
		    }else{
		    	return Response.retn(ResultCode.PARAM_VALID_ERROR, "读取失败，模板格式不正确"); 
		    }
		    Response<?> responseMap= importResearch(list);
		    res = (Map<String, Object>) responseMap.getData();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "读取失败，模板格式不正确");
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "转换出错");
		}
		 finally {
 			try {
				FileUtils.forceDelete(files);
			} catch (IOException e) {
				e.printStackTrace();
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
	public Response<?> importResearch( List<TerminalEquipExcel> dataList) {
		int successNumber = 0;
		int failNumber = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		// 错误信息
		StringBuilder message = new StringBuilder("");
		map.put("message", "");
		// 提示信息
		boolean res = true;
		// 解析之后的excel集合
		try {
			if (dataList == null || dataList.size() == 0) {
				res = false;
				message.append("读取失败，模版内容为空");
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
		if (!res) {
			// 有错误
			map.put("res", false);
			return  Response.ok(map);
		}
		boolean is = true;
		try {
			for (int i = 0; i < dataList.size(); i++) {
				String  regexNum="^\\d+$";
				TerminalEquipExcel terminalEquipExcel=dataList.get(i);
				 // 通讯地址是否为空
			     if (!StringUtils.isEmpty(terminalEquipExcel.getEquipNumber())) {
			    		//仪表地址不能大于十二位
			    	if(terminalEquipExcel.getEquipNumber().length()<=12){
			    		if(terminalEquipExcel.getEquipNumber().length()==10||terminalEquipExcel.getEquipNumber().length()==11){
					              if(terminalEquipExcel.getEquipNumber().length()==10){
					                StringBuffer sb = new StringBuffer();
					                sb.append("00").append(terminalEquipExcel.getEquipNumber());
					                terminalEquipExcel.setEquipNumber(sb.toString());
					              } else if(terminalEquipExcel.getEquipNumber().length()==11){
					                StringBuffer sb = new StringBuffer();
					                sb.append("0").append(terminalEquipExcel.getEquipNumber());
					                terminalEquipExcel.setEquipNumber(sb.toString());
					              }
			    				} else{
			    					terminalEquipExcel.setEquipNumber(String.format("%0" + 12 + "d",Long.parseLong(terminalEquipExcel.getEquipNumber())));
			    				}
			    			//通讯地址重复
			    		  if(terminalEquipService.selectByEquipNumber(terminalEquipExcel.getEquipNumber())==null){
			    			 //仪表名称不能为空
			    			  if(!StringUtils.isEmpty(terminalEquipExcel.getEquipName())){
			    				  if(!StringUtils.isEmpty(terminalEquipExcel.getEquipCategCode())){
			    					  if(!StringUtils.isEmpty(terminalEquipExcel.getEquipTypeCode())){
			    						  if(!StringUtils.isEmpty(terminalEquipExcel.getCommPwd())){
			    							  if(!StringUtils.isEmpty(terminalEquipExcel.getIsDirect())){
			    								  if(!StringUtils.isEmpty(terminalEquipExcel.getNetTypeCode())){
			    									  if(!StringUtils.isEmpty(terminalEquipExcel.getNetNumber())){
			    										  if(baseEquipModelService.selectByModelName(terminalEquipExcel.getModelName())!=null){
			    											  	if (StringUtils.isEmpty(terminalEquipExcel.getDtuWorkingMode())) {
			    											  		failNumber++;
														        	message.append("第" + (i + 2) + "行工作模式不存在" + "<br/>");
														        	map.put("message", message.toString());
														        	continue;
																}
																if (StringUtils.isEmpty(terminalEquipExcel.getDtuFunctionDigit())) {
			    											  		failNumber++;
														        	message.append("第" + (i + 2) + "行功能位不存在" + "<br/>");
														        	map.put("message", message.toString());
														        	continue;
																}
																if (StringUtils.isEmpty(terminalEquipExcel.getDtuRfBaudRate())) {
			    											  		failNumber++;
														        	message.append("第" + (i + 2) + "行射频波特率不存在" + "<br/>");
														        	map.put("message", message.toString()); 
														        	continue;
																}
																if (StringUtils.isEmpty(terminalEquipExcel.getDtuBeattim())) {
			    											  		failNumber++;
														        	message.append("第" + (i + 2) + "行心跳时间间隔不存在" + "<br/>");
														        	map.put("message", message.toString()); 
														        	continue;
																}
																if (StringUtils.isEmpty(terminalEquipExcel.getDtuEquipNum())) {
			    											  		failNumber++;
														        	message.append("第" + (i + 2) + "行设备数量不存在" + "<br/>");
														        	map.put("message", message.toString()); 
														        	continue;
																}
																if (StringUtils.isEmpty(terminalEquipExcel.getDtuReportingInterval())) {
			    											  		failNumber++;
														        	message.append("第" + (i + 2) + "行上报时间间隔不存在" + "<br/>");
														        	map.put("message", message.toString()); 
														        	continue;
																}
																terminalEquipService.inserTerminalEquipExcel(terminalEquipExcel);
																successNumber++;
			    										  }else{
			    											  failNumber++;
												              message.append("第" + (i + 2) + "行型号名称不存在" + "<br/>");
												              map.put("message", message.toString());  
			    										  }
				    								  } else{
				    									  failNumber++;
											              message.append("第" + (i + 2) + "行网络编号为空" + "<br/>");
											              map.put("message", message.toString()); 
				    								  }
			    									 
			    								  } else{
			    									  failNumber++;
										              message.append("第" + (i + 2) + "行上行网络类型为空" + "<br/>");
										              map.put("message", message.toString()); 
			    								  }
				    						  }else{
				    							  failNumber++;
									              message.append("第" + (i + 2) + "行是否直连为空" + "<br/>");
									              map.put("message", message.toString());  
				    						  }
			    						  }else{
			    							  failNumber++;
								              message.append("第" + (i + 2) + "行通信密码为空" + "<br/>");
								              map.put("message", message.toString());  
			    						  }
				    				  }else{
				    					  failNumber++;
							              message.append("第" + (i + 2) + "行二级分类为空" + "<br/>");
							              map.put("message", message.toString());  
				    				  }
			    				  }else{
			    					  failNumber++;
						              message.append("第" + (i + 2) + "行一级分类为空" + "<br/>");
						              map.put("message", message.toString());  
			    				  }
			    			  }else{
			    				  failNumber++;
					              message.append("第" + (i + 2) + "行仪表名称为空" + "<br/>");
					              map.put("message", message.toString());
			    			  }
			    		  }else{
			    			  message.append("第"+(i+2)+"行仪表地址重复<br/>");
							  map.put("message", message.toString());
							  failNumber++;
			    		  }
			    	}else{
					       failNumber++;
					       message.append("第" + (i + 2) + "行通讯地址必须小于等于12位" + "<br/>");
					       map.put("message", message.toString());
					}
				}else{
					  message.append("第"+(i+2)+"行通讯地址为空<br/>");
		              map.put("message", message.toString());
		              failNumber++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			is = false;
			successNumber=0;
			failNumber++;
			map.put("res", is);
			message.append("数据异常<br/>");
			message.append("导入成功" + successNumber + "条。<br/>" + "失败" + failNumber
					+ "条。");
			map.put("message", message.toString() + "<br/>");
			return  Response.ok(map);
		}
		map.put("res", is);
		message.append("导入成功" + successNumber + "条。<br/>" + "失败" + failNumber
				+ "条。");
		map.put("message", message.toString() + "<br/>");
		return  Response.ok(map);
	}
	private TerminalEquipExcel trimEntity(TerminalEquipExcel terminalEquipExcel){
		if(!StringUtils.isEmpty(terminalEquipExcel.getEquipNumber())){
			terminalEquipExcel.getEquipNumber().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getEquipName())){
			terminalEquipExcel.getEquipName().trim();
		}else{
			//如果设备名称为空，用设备编号替代
			terminalEquipExcel.setEquipName(terminalEquipExcel.getEquipNumber());
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getOrgId())){
			terminalEquipExcel.getOrgId().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getAppId())){
			terminalEquipExcel.getAppId().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getEquipCategCode())){
			terminalEquipExcel.getEquipCategCode().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getEquipTypeCode())){
			terminalEquipExcel.getEquipTypeCode().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getModelName())){
			terminalEquipExcel.getModelName().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getSoftVersoin())){
			terminalEquipExcel.getSoftVersoin().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getCommPwd())){
			terminalEquipExcel.getCommPwd().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getIsDirect())){
			terminalEquipExcel.getIsDirect().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getLinkTypeCode())){
			terminalEquipExcel.getLinkTypeCode().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getNetEquipNumber())){
			terminalEquipExcel.getNetEquipNumber().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getNetTypeCode())){
			terminalEquipExcel.getNetTypeCode().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getNetNumber())){
			terminalEquipExcel.getNetTypeCode().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getElecMeterTypeCode())){
			terminalEquipExcel.getElecMeterTypeCode().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getElecVoltageRating())){
			terminalEquipExcel.getElecVoltageRating().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getElecCurrentRating())){
			terminalEquipExcel.getElecVoltageRating().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getElecPowerRating())){
			terminalEquipExcel.getElecPowerRating().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getElecCurrentMax())){
			terminalEquipExcel.getElecCurrentMax().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getElecPowerMax())){
			terminalEquipExcel.getElecPowerMax().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getElecPt())){
			terminalEquipExcel.getElecPt().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getElecCt())){
			terminalEquipExcel.getElecCt().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getIsUsable())){
			terminalEquipExcel.getIsUsable().trim();
		}
		if(!StringUtils.isEmpty(terminalEquipExcel.getIsHistory())){
			terminalEquipExcel.getIsHistory().trim();
		}
		return terminalEquipExcel;
	}
	
	/**
	 * 获取工厂下的所有仪表-APP
	 * @return
	 */
	/*@PostMapping("app")
	public Page selectEquipByOrgIdToAPP(@RequestBody Page page){
		TerminalEquip te = new TerminalEquip();
		@SuppressWarnings("unchecked")
		Map<String,Object> queryMap = (Map<String, Object>) page.getParamObj();
		//此方法不需要aop自带机构参数。
		te.setOrgId("1001");
		if(null != queryMap.get("search")){
			te.setSearch(queryMap.get("search").toString());
		}
		page.setParamObj(te);
		List<TerminalEquip> list = terminalEquipService.selectByPage(page);
		page.setData(list);
		return page;
	}*/
	
	@GetMapping("app/{equipNum}/{equipOrg}")
	public Response<?> selectEquipByThisOrgOrSourceOrg(@PathVariable String equipNum, @PathVariable String equipOrg){
		//现根据当前orgId查询
		TerminalEquip newTe = new TerminalEquip();
		newTe.setEquipNumber(equipNum);
		newTe.setOrgId(equipOrg);
		try {
			List<TerminalEquipExpand> equip = terminalEquipService.selectByBean(newTe);
			if(equip.size() > 0){
				return Response.retn("000", null, equip);
			}else{
				newTe.setOrgId("1008");
				List<TerminalEquipExpand> equip2 = terminalEquipService.selectByBean(newTe);
				if(equip2.size() > 0){
					return Response.retn("111", null, equip2);
				}else{
					return Response.retn("111111", "输入有误或该仪表与其他机构绑定", null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.UNKNOW_ERROR, "系统未知异常");
		}
	}
	/**
	 * 修改设备绑定应用
	 * @return
	 */
	@PostMapping("app/update")
	public Response<?> updateEquipByAPP(@RequestBody TerminalEquip terminalEquip){
		try {
			terminalEquip.setOperationRecordingTime(new Date());
			terminalEquipService.updateByPrimaryKeySelective(terminalEquip);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**O
	 * saas对paas调用换货的接口
	 * 如果有表坏掉 saas需要调用paas的该接口变更表信息
	 * oldEquipNumber 老表
	 * newEquipNumber 新表
	 * @return
	 */
	@PostMapping("replace")
	public Response<?> replaceNewTermNo(@RequestBody @Valid TermReplace termReplace, BindingResult br){
			
		if(br.hasErrors()){
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		
		if (StringUtils.isEmpty(termReplace.getAppId())) {
			throw new RequestParamValidException("appId can not null");
		}
		terminalEquipService.replaceTerm(termReplace);
		return Response.ok();
	}
	
	/**
	 * 多重分组查询各个扇区使用数
	 * @param netEquipNumber
	 * @return
	 */
	@GetMapping("sectors/{netEquipNumber}")
	public Response<?> selectByNetEquipNumberAndSectors(@PathVariable String netEquipNumber){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<TerminalEquip> list = terminalEquipService.selectByNetEquipNumberAndSectors(netEquipNumber);
			NetEquip netEquip = netEquipService.selectByEquipNumber(netEquipNumber);
			map.put("list", list);
			map.put("netEquip", netEquip);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Response.ok(map);
	}
	
	/**
	 * 分页回显工厂下设备
	 */
	@GetMapping("plant/page")
	public Response<?> selectMeterByPlant(Page page){
		/*TerminalEquip terminalEquip = new TerminalEquip();
		terminalEquip.setOrgId(orgId);
		terminalEquip.setAppId(appId);
		page.setParamObj(terminalEquip);*/
		List<TerminalEquip> list = terminalEquipService.selectMeterByPlant(page);
		page.setData(list);
		return Response.ok(page);
	}
	
	/**
	 * 从工厂机构下转移到其他机构
	 */
	@PostMapping("plant")
	public Response<?> updatePlantToOtherOrg(@RequestBody TerminalEquip terminalEquip){
		int res = terminalEquipService.updatePlantToOtherOrg(terminalEquip);
		if(res == 1){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "参数错误");
		}else if(res == 2){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "没有选中参数");
		}else if(res ==0){
			return Response.ok();
		}
		return Response.retn(ResultCode.UNKNOW_ERROR, "未知错误");
	}
	
	/**
	 * 从DAAS插入数据
	 * 
	 * @param terminalEquip
	 * @return
	 */
	@PostMapping("dtu")
	public Response<?> insertForDaas(@RequestBody TerminalEquip terminalEquip) {
		if (terminalEquip.getDteList() != null && terminalEquip.getDteList().size() > 0) {
			terminalEquipService.insertForDaas(terminalEquip);
		} else {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "下联设备为空");
		}
		return Response.ok();
	}

	/**
	 * 查询dtu分页(下联单个设备用该接口)
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("dtu/page")
	public Response<?> selectDaasByPage(Page page) {
		List<TerminalEquip> list = terminalEquipService.selectDaasByPage(page);
		page.setData(list);
		return Response.ok(page);
	}

	/**
	 * 查询dtu分页(新：下联多个设备用该接口)
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("dtu/children/page")
	public Response<?> selectDtuAndChildrenByPage(Page page) {
		List<TerminalEquip> list = terminalEquipService.selectDtuAndChildrenByPage(page);
		page.setData(list);
		return Response.ok(page);
	}

	/**
	 * 编辑回显查询
	 * 
	 * @param equipId
	 * @return
	 */
	@GetMapping("dtu/{equipId}")
	public Response<?> selectDaasByPrimaryKey(@PathVariable String equipId) {
		TerminalEquip te = terminalEquipService.selectDaasByPrimaryKey(equipId);
		return Response.ok(te);
	}

	/**
	 * 查询接口
	 * 
	 * @param equipNumber
	 * @return
	 */
	@GetMapping("dtu/number/{equipNumber}")
	public Response<?> selectDaasByPage(@PathVariable String equipNumber) {
		TerminalEquip te = terminalEquipService.selectDaasByEquipNumber(equipNumber);
		return Response.ok(te);
	}

	/**
	 * dut删除
	 * 
	 * @param equipId
	 * @return
	 */
	@DeleteMapping("dtu/{equipId}")
	public Response<?> deleteDaasEquip(@PathVariable String equipId) {
		terminalEquipService.deleteDaasById(equipId);
		return Response.ok();
	}

	/**
	 * dtu修改
	 * 
	 * @param terminalEquip
	 * @return
	 */
	@PutMapping("dtu")
	public Response<?> updateDtu(@RequestBody TerminalEquipParamDtu tepd) {
		int res = terminalEquipService.updateDtu(tepd);
		if (res == 2) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "设备信息有误");
		} else if (res == 1) {
			return Response.retn(ResultCode.UNKNOW_ERROR, "未知错误");
		}
		return Response.ok();
	}

	/**
	 * dtu下发组装
	 */
	@PostMapping("dtu/send/{equipId}/{composite}")
	public Response<?> assemblyAndSend(@PathVariable String equipId,@PathVariable int composite) {
		try {
			TerminalEquip newTe = terminalEquipService.selectDaasByPrimaryKey(equipId);
			CommonParam param = new CommonParam();
			TerminalEquipParamDtu terminalEquipParamDtu = new TerminalEquipParamDtu();
			param.setCmdCode("write_gehua");
			param.setEquipNumber(newTe.getEquipNumber());
			param.setOrgId(newTe.getOrgId());
			param.setAppId(newTe.getAppId());
			terminalEquipParamDtu.setEquipNumber(newTe.getEquipNumber());
			terminalEquipParamDtu.setSendStatus((byte) 3);
			terminalEquipParamDtuService.updateSelect(terminalEquipParamDtu);
			// 0.同时下发, 1. 单独执行设备, 2. 单独执行dtu参数下发
			if (composite == 0) {
				assemblyDtuParam(newTe, param);
				sendCmd(param);
				if (newTe.getDteList() != null && newTe.getDteList().size() > 0) {
					for (int i = 0; i < newTe.getDteList().size(); i++) {
						int num = 0;
						if (i == 0) {
							num = 6;
						}
						assemblyChildrenParam(newTe.getDteList().get(i), param, DtuCommonParam.AddArray[10 + num]);
						sendCmd(param);
					}
				}
				return Response.ok();
			} else if (composite == 1) {
				for (int i = 0; i < newTe.getDteList().size(); i++) {
					int num = 0;
					if (i == 0) {
						num = 6;
					}
					assemblyChildrenParam(newTe.getDteList().get(i), param, DtuCommonParam.AddArray[10 + num]);
					sendCmd(param);
				}
				return Response.ok();
			} else {
				assemblyDtuParam(newTe, param);
				return sendCmd(param);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return Response.retn(ResultCode.UNKNOW_ERROR, "下发失败");
		}
	}

	/**
	 * 组装dut参数
	 * 
	 * @param newTe
	 * @return
	 */
	private void assemblyDtuParam(TerminalEquip newTe, CommonParam param) {
		StringBuffer sb = new StringBuffer("0A,");
		sb.append(newTe.getWorkingMode() + ",");
		sb.append(newTe.getFunctionDigit() + ",");
		sb.append(newTe.getRfBaudRate() + ",");
		sb.append(newTe.getBeattim() + ",");
		sb.append(newTe.getChildrenNum() + ",");
		sb.append(newTe.getGatherHz() + ",");
		sb.append("0");
		param.setBusinessNo("DTU_" + (CommUtils.getUuid()).substring(0, 11));
		param.setParams(sb.toString());
	}

	/**
	 * 组装下联设备参数
	 * 
	 * @param newTe
	 * @param param
	 */
	private void assemblyChildrenParam(TerminalEquipChildren children, CommonParam param, String subscript) {
		StringBuffer sb = new StringBuffer(subscript + ",");
		sb.append(children.getManufacturer() + ",");
		sb.append(children.getEquipModel() + ",");
		sb.append(children.getChildrenEquipNumber() + ",");
		sb.append(children.getSoftVersoin() + ",");
		sb.append(children.getProtocolVersoin() + ",");
		sb.append("0");
		param.setBusinessNo("DTU_" + (CommUtils.getUuid()).substring(0, 11));
		param.setParams(sb.toString());
	}

	private Response<?> sendCmd(CommonParam param) {
		Boolean isApplicationRequest = false;
		String cmdCode = param.getCmdCode();
		String equipNumber = param.getEquipNumber();

		// 验证仪表是否存在
		TerminalEquip terminalEquip = valiedateEquipAndOrgId(equipNumber, param.getOrgId());
		if (terminalEquip == null) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "meterAddr not find!");
		}
		// 判断数据库中是否包含相关命令和指令
		// TODO: handle exception modelId 暂时给空值
		List<Ins> ins = getCmdIns(terminalEquip.getEquipTypeCode(), null, terminalEquip.getModelId(), cmdCode);
		if (null == ins) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR);
		}
		// 组装命令指令集
		CmdExe cmd = getCmdExe(cmdCode, terminalEquip.getEquipNumber(),
				terminalEquip.getAppId(), param.getBusinessNo(),
				param.getOrgId(), terminalEquip.getEquipTypeCode(),
				terminalEquip.getNetTypeCode(), terminalEquip.getNetNumber(),
				terminalEquip.getCommPwd());
		if (null == cmd) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR);
		}
		if (StringUtils.isEmpty(param.getParams())) {
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins);
		} else {
			loadCmdIns(cmd, terminalEquip.getProtocolType(), ins,
					param.getParams());
		}

		try {
			if (!isApplicationRequest) {
				cmdIssueService.cmdIssue(cmd, noPushCallback);
			} else {
				cmdIssueService.cmdIssue(cmd, baseCallback);
			}
		} catch (CmdHandleException e) {
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}

	@PutMapping("dtu/cmd")
	public Response<?> updateAndSendCmd(@RequestBody TerminalEquipParamDtu tepd) {
		terminalEquipService.updateDtu(tepd);
		return assemblyAndSend(tepd.getEquipId(), 0);
	}
	
	/**
	 * 导入设备信息
	 * @param file
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("dtu/importMeterExcel")
	public Response<?> importMeterExcelByDtu(@RequestParam MultipartFile file, HttpServletRequest request) {
		final int rowStart = 1;
		final int cellStrart = 0;
		Map<String, Object> res;
		// 文件名称
		String fileName = file.getOriginalFilename();
		if (org.springframework.util.StringUtils.isEmpty(fileName)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "importDtuExcel uuid is null!");
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
			List<Object> dataList = ReadExcleUtils.readExcel(newFile, new TerminalEquipDtuExcel(), rowStart, cellStrart);
			List<TerminalEquipDtuExcel> tedeList = new ArrayList<TerminalEquipDtuExcel>();
			if (dataList != null && dataList.size() > 0) {
				for (int i = 0; i < dataList.size(); i++) {
					TerminalEquipDtuExcel tede = (TerminalEquipDtuExcel) dataList.get(i);
					tedeList.add(tede);
				}
			} else {
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "读取失败，模板格式不正确");
			}
			Response<?> resp = importVerify(tedeList);
			res = (Map<String, Object>) resp.getData();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "读取失败，模板格式不正确");
		} finally {
			try {
				FileUtils.forceDelete(files);
			} catch (IOException e) {
				e.printStackTrace();
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "上传出错");
			}
		}
		if ((boolean) res.get("res")) {
			// 成功，刷新页面
			return Response.retn("000000", res.get("message").toString(), null);
		} else {
			// 错误，跳往错误页面
			return Response.retn("111111", res.get("message").toString(), null);
		}
	}

	private Response<?> importVerify(List<TerminalEquipDtuExcel> dtuList) {
		int successNumber = 0;
		int failNumber = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		// 错误信息
		StringBuilder message = new StringBuilder("");
		map.put("message", "");
		// 提示信息
		Boolean res = true;
		// 解析之后的excel集合
		try {
			if (dtuList == null) {
				res = false;
				message.append("读取失败，模板格式不正确");
				map.put("res", false);
				map.put("message", message.toString() + "<br/>");
				return Response.ok(map);
			}
		} catch (Exception e) {
			res = false;
			message.append("读取失败，模板格式不正确");
			map.put("message", message.toString() + "<br/>");
			map.put("res", res);
			e.printStackTrace();
			return Response.ok(map);
		}
		if (!res) {
			// 有错误
			map.put("res", false);
			return Response.ok(map);
		}
		Boolean is = true;
			for (int i = 0; i < dtuList.size(); i++) {
				try {
					TerminalEquipDtuExcel terminalEquipDtuExcel = dtuList.get(i);
					//准备逐条导入并记录错误信息
					if (StringUtils.isEmpty(terminalEquipDtuExcel.getEquipNumber())) {
						message.append("第" + (i + 2) + "行dtu编号为空<br/>");
						map.put("message", message.toString());
						failNumber++;
						continue;
					}
					if (StringUtils.isEmpty(terminalEquipDtuExcel.getEquipName())) {
						message.append("第" + (i + 2) + "行dtuSN码为空<br/>");
						map.put("message", message.toString());
						failNumber++;
						continue;
					}
					
					TerminalEquip terminalEquip = terminalEquipService.selectByEquipNumber(terminalEquipDtuExcel.getEquipNumber());
					TerminalEquipParamDtu terminalEquipParamDtu = terminalEquipParamDtuService.selectById(terminalEquipDtuExcel.getEquipNumber());
					if (!StringUtils.isEmpty(terminalEquip) || !StringUtils.isEmpty(terminalEquipParamDtu)) {
						//throw new BusinessException(MonitResultCode.PARAM_EXIST_ERROR, "设备编号重复！");
						message.append("第" + (i + 2) + "行设备编号重复<br/>");
						map.put("message", message.toString());
						failNumber++;
						continue;
					}
					
					if (StringUtils.isEmpty(terminalEquipDtuExcel.getModelName())) {
						message.append("第" + (i + 2) + "行dtu型号为空<br/>");
						map.put("message", message.toString());
						failNumber++;
						continue;
					}else{
						BaseEquipModel baseEquipModel = baseEquipModelService.selectByModelName(terminalEquipDtuExcel.getModelName());
						if (baseEquipModel == null) {
							message.append("第" + (i + 2) + "行dtu型号不存在<br/>");
							map.put("message", message.toString());
							failNumber++;
							continue;
						}else{
							terminalEquipDtuExcel.setModelId(baseEquipModel.getModelId());
						}
					}
					if (StringUtils.isEmpty(terminalEquipDtuExcel.getModelName())) {
						message.append("第" + (i + 2) + "行上行网络类型为空<br/>");
						map.put("message", message.toString());
						failNumber++;
						continue;
					}
					terminalEquipService.insertNetEquipExcel(terminalEquipDtuExcel);
					//成功数+1
					successNumber++;
				} catch (Exception e) {
					e.printStackTrace();
					is = false;
					failNumber++;
					continue;
				}
			}
		map.put("res", is);
		message.append("导入成功" + successNumber + "条。<br/>" + "失败" + failNumber + "条。");
		map.put("message", message.toString());
		return Response.ok(map);
	}
}
