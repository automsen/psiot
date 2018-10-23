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
import cn.com.tw.saas.serv.common.utils.ReadExcleUtils;
import cn.com.tw.saas.serv.common.utils.cons.RegExp;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.excel.SaasCustomerExcel;
import cn.com.tw.saas.serv.service.cust.CustomerService;

@RestController
@RequestMapping("customer")
@Api(description = "客户管理接口")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	/**
	 * 客户展示列表
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	@ApiOperation(value = "分页查询", notes = "")
	public Response<?> selectByPage(Page page) {
		List<Customer> scList = customerService.selectByPage(page);
		page.setData(scList);
		return Response.ok(page);
	}
	
	@GetMapping("room")
	@ApiOperation(value = "按房间查询", notes = "")
	public Response<?> selectByRoom(CustomerRoom param) {
		// 查询使用中的数据
		param.setStatus((byte) 1);
		List<Customer> scList = customerService.selectByRoom(param);
		return Response.ok(scList);
	}
	
	@GetMapping("like")
	@ApiOperation(value = "模糊查询", notes = "")
	public Response<?> selectByLikeEntity(Customer param) {
		List<Customer> scList = customerService.selectByLikeEntity(param);
		return Response.ok(scList);
	}
	
	@GetMapping("phone")
	@ApiOperation(value = "根据电话号码查客户", notes = "")
	public Response<?> selectCustByCustomerMobile1(Customer param){
		Customer customer = customerService.selectByPhone(param);
		return Response.ok(customer);
	}
	

	@GetMapping("phone/{customerMobile1}")
	@ApiOperation(value = "按手机号查询", notes = "")
	public Response<?>  selectByCustomerMobile1(@PathVariable String customerMobile1){
		Customer  scList=customerService.selectByCustomerMobile1(customerMobile1);
		return Response.ok(scList);
	}
	

	/**
	 * 修改客户
	 * 
	 * @param saasCustomer
	 * @param br
	 * @return
	 */
	@PutMapping()
	@ApiOperation(value = "修改客户", notes = "")
	public Response<?> updateCustomoer(@RequestBody Customer saasCustomer, BindingResult br) {
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			customerService.update(saasCustomer);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}

	/**
	 * 查询客户
	 * 
	 * @return
	 */
	@GetMapping("{customerId}")
	@ApiOperation(value = "查询客户", notes = "")
	public Response<?> selectCustomerById(@PathVariable String customerId) {
		Customer customer = customerService.selectById(customerId);
		return Response.ok(customer);
	}

	/**
	 * 添加客户
	 * 
	 * @param customer
	 * @param br
	 * @return
	 */
	@ApiOperation(value = "添加客户", notes = "")
	@PostMapping()
	public Response<?> insertCustomer(@RequestBody @Valid Customer customer, BindingResult br) {
		if (br.hasErrors()) {
			throw new RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			// 手机号匹配
			if (!customer.getCustomerMobile1().matches(RegExp.phoneExp)){
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "保存失败！手机号格式错误");
			}
			/*// 客户标识匹配
			if(!customer.getCustomerNo().matches(RegExp.wordNumberUnderlineRungExp)){
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "保存失败！客户标识格式错误");
			}*/
		   String result = customerService.insertOne(customer);
		   if (result.equals("ok")){
			   return Response.ok();
		   }
		   else{
			   return Response.retn(ResultCode.PARAM_VALID_ERROR, result);
		   }
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		
	}

	/**
	 * 删除客户
	 * 
	 * @param saasCustomerId
	 * @return
	 */
	@ApiOperation(value = "删除客户", notes = "")
	@DeleteMapping("{customerId}")
	public Response<?> deletesaasCustomerById(@PathVariable String customerId) {
		try {
			customerService.deleteById(customerId);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();

	}

	/**
	 * 客户开户导入
	 * 
	 * @param saasCustomer
	 * @return
	 */
	@ApiOperation(value = "客户开户导入", notes = "")
	@PostMapping("import")
	public Response<?> importExcelForCustomer(@RequestParam MultipartFile file, HttpServletRequest request) {
		final int rowStart = 2;
		final int cellStrart = 0;
		Map<String, Object> res;
		// 文件名称
		String fileName = file.getOriginalFilename();
		if (fileName.isEmpty()) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "selectModelInfo uuid is null!");
		}
		// String hzString = fileName.substring(fileName.indexOf("."),
		// fileName.length());
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
			List<Object> dataList = ReadExcleUtils.readExcel(newFile, new SaasCustomerExcel(), rowStart, cellStrart);
			List<SaasCustomerExcel> list = new ArrayList<SaasCustomerExcel>();
			if (dataList.size() > 0 && dataList != null) {
				for (int i = 0; i < dataList.size(); i++) {
					SaasCustomerExcel meterExcel = (SaasCustomerExcel) dataList.get(i);
					//如果身份标识为空的话，则默认手机号为身份标识
					if(StringUtils.isEmpty(meterExcel.getCustomerNo())){
						meterExcel.setCustomerNo(meterExcel.getCustomerMobile1());
					}
					//如果性别为空，则默认为1
					if(StringUtils.isEmpty(meterExcel.getCustomerSex())){
						meterExcel.setCustomerSex("1");
					}else if(!StringUtils.isEmpty(meterExcel.getCustomerSex())){
						meterExcel.setCustomerSex(meterExcel.getCustomerSex());
					}
					//客户姓名保存的时候，每个汉字前后都要去空
					if(!StringUtils.isEmpty(meterExcel.getCustomerRealname())){
						//meterExcel.setCustomerRealname(meterExcel.getCustomerRealname().replaceAll("\\s*", ""));
						meterExcel.setCustomerRealname(meterExcel.getCustomerRealname().replaceAll(" ", ""));
					}
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
	private Map<String, Object> importResearch(List<SaasCustomerExcel> dataList) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 导入成功条数
		int successNum = 0;
		// 导入失败条数
		int errorNum = 0;
		// 异常记录
		List<SaasCustomerExcel> errorRecord = new ArrayList<SaasCustomerExcel>();
		// 手机号匹配
		for (int i = 0; i < dataList.size(); i++) {
			SaasCustomerExcel custTemp = dataList.get(i);
			// 验证客户信息
			if (StringUtils.isEmpty(custTemp.getCustomerRealname())) {
				errorNum++;
				custTemp.setMessage("客户姓名为空");
				errorRecord.add(custTemp);
				continue;
			}
			//姓名的校验，只允许中文，英文和数字的校验
			if(!(custTemp.getCustomerRealname()).matches("^[\u4e00-\u9fa5_a-zA-Z0-9]+$")
					||custTemp.getCustomerRealname().length()>20){
				errorNum++;
				custTemp.setMessage("客户姓名的格式错误");
				errorRecord.add(custTemp);
				continue;
			}
			if(!StringUtils.isEmpty(custTemp.getCustomerNo())){
				if(custTemp.getCustomerNo().matches("^[\u4E00-\u9FA5]+$")
						||custTemp.getCustomerNo().length()>40){
					errorNum++;
					custTemp.setMessage("客户标识错误");
					errorRecord.add(custTemp);
					continue;
				}
				
			}
			
			
			/*if (!StringUtils.isEmpty(custTemp.getCustomerNo())&&!(custTemp.getCustomerMobile1().equals(custTemp.getCustomerNo()))) {
				errorNum++;
				custTemp.setMessage("身份标识错误");
				errorRecord.add(custTemp);
				continue;
			}*/
			/*if(!"1".equals(custTemp.getCustomerSex())||!"0".equals(custTemp.getCustomerSex())){
				errorNum++;
				custTemp.setMessage("性别错误");
				errorRecord.add(custTemp);
				continue;
			}*/
			 
			
			
			if (StringUtils.isEmpty(custTemp.getCustomerType())) {
				errorNum++;
				custTemp.setMessage("用户类型为空");
				errorRecord.add(custTemp);
				continue;
			}
			if(!"0".equals(custTemp.getCustomerType()) && !"1".equals(custTemp.getCustomerType()) && !"2".equals(custTemp.getCustomerType())){
				errorNum++;
				custTemp.setMessage("客户类型不合法");
				errorRecord.add(custTemp);
				continue;
			}
			if (StringUtils.isEmpty(custTemp.getCustomerMobile1())) {
				errorNum++;
				custTemp.setMessage("手机号为空");
				errorRecord.add(custTemp);
				continue;
			}
			if (!custTemp.getCustomerMobile1().matches(RegExp.phoneExp)) {
				errorNum++;
				custTemp.setMessage("手机号格式错误");
				errorRecord.add(custTemp);
				continue;
			}
			// 一条条执行导入
			try {
				String returnCode = customerService.importInsert(custTemp);
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
	
	
	/**
	 * 获取客户余额page页面
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("accountBalance")
	public Response<?> selectCustomerAccountBalance(Page page) {
		List<Customer> list = customerService.selectCustomerAccountBalance(page);
		page.setData(list);
		return Response.ok(page);
	}
}
