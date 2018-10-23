package cn.com.tw.saas.serv.controller.cust;

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
import cn.com.tw.saas.serv.entity.excel.CustomerRoomExcel;
import cn.com.tw.saas.serv.service.cust.CustomerRoomService;

@RestController
@RequestMapping("custroom")
@Api(description = "客户房间管理接口")
public class CustomerRoomTempController {
	@Autowired
	private CustomerRoomService customerRoomService;

	@ApiOperation(value = "客户房间关系列表", notes = "")
	@ResponseBody
	@GetMapping("page")
	public Response<?> selectByPage(Page page) {
		List<CustRoomParam> list = null;
		try {
			list = customerRoomService.selectByPage(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}
	
	@ApiOperation(value = "取消客户房间关系", notes = "")
	@PostMapping("cancel")
	public Response<?> cancelRelate(@RequestBody CustRoomParam param) {
		int result = 0;
		try {
			result = customerRoomService.CancelRelateCustomerRoom(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(result);
	}

	@ApiOperation(value = "新增客户房间关系", notes = "")
	@PostMapping("relate")
	public Response<?> relate(@RequestBody CustRoomParam param) {
		int result = 0;
		try {
			result = customerRoomService.RelateCustomerRoom(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(result);
	}
	
	
	@ApiOperation(value = "修改客户房间关系", notes = "")
	@PutMapping("")
	public Response<?> update(@RequestBody CustRoomParam param) {
		try {
			customerRoomService.updateCustomerRoom(param);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}

	/**
	 * 客户房间关联导入
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "客户房间关联导入", notes = "")
	@RequestMapping("/import")
	@ResponseBody
	public Response<?> importExcel(@RequestParam MultipartFile file, HttpServletRequest request,
			@RequestParam(name = "regionId") String regionId) {
		if(StringUtils.isEmpty(regionId)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "请选择楼栋");
		}
		final int rowStart = 7;
		final int cellStrart = 0;
		Map<String, Object> res;
		// 文件名称
		String fileName = file.getOriginalFilename();
		if (org.springframework.util.StringUtils.isEmpty(fileName)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "selectModelInfo uuid is null!");
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
			List<Object> dataList = ReadExcleUtils.readExcel(newFile, new CustomerRoomExcel(), rowStart, cellStrart);
			List<CustomerRoomExcel> list = new ArrayList<CustomerRoomExcel>();
			if (dataList.size() > 0 && dataList != null) {
				for (int i = 0; i < dataList.size(); i++) {
					CustomerRoomExcel customerRoomExcel = (CustomerRoomExcel) dataList.get(i);
					list.add(customerRoomExcel);
				}
			} else {
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "解析失败！无有效数据");
			}
			res = importResearch(list,regionId);
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
	private Map<String, Object> importResearch(List<CustomerRoomExcel> dataList, String regionId) {
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
			List<CustomerRoomExcel> errorRecord = new ArrayList<CustomerRoomExcel>();
			for (int i = 0; i < dataList.size(); i++) {
				CustomerRoomExcel custTemp = dataList.get(i);
				if (StringUtils.isEmpty(custTemp.getRoomNumber())) {
					errorNum++;
					custTemp.setMessage("房间号为空");
					errorRecord.add(custTemp);
					continue;
				}
				// 验证客户信息
				if (StringUtils.isEmpty(custTemp.getCustomerName())) {
					errorNum++;
					custTemp.setMessage("客户姓名为空");
					errorRecord.add(custTemp);
					continue;
				}			
				if (StringUtils.isEmpty(custTemp.getCustomerPhone())) {
					errorNum++;
					custTemp.setMessage("手机号为空");
					errorRecord.add(custTemp);
					continue;
				}
				if (!custTemp.getCustomerPhone().matches(RegExp.phoneExp)) {
					errorNum++;
					custTemp.setMessage("手机号格式错误");
					errorRecord.add(custTemp);
					continue;
				}
				// 一条条执行导入
				try {
					String returnCode = customerRoomService.importTempExcel(custTemp,regionId,orgId);
					if (returnCode.equals("ok")) {
						successNum++;
					} else {
						errorNum++;
						custTemp.setMessage(returnCode);
						errorRecord.add(custTemp);
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorNum++;
					custTemp.setMessage("未知异常");
					errorRecord.add(custTemp);
				}
			}
			resultMap.put("successNum", successNum);
			resultMap.put("errorNum", errorNum);
			resultMap.put("errorRecord", errorRecord);
			return resultMap;
		}

}
