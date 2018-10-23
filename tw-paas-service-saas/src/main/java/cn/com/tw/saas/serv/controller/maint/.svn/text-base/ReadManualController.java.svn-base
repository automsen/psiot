package cn.com.tw.saas.serv.controller.maint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.xmlbeans.impl.regex.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.DateStrUtils;
import cn.com.tw.saas.serv.common.utils.ReadExcleUtils;
import cn.com.tw.saas.serv.entity.db.read.ReadManual;
import cn.com.tw.saas.serv.entity.excel.ReadManualExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.service.maint.ReadManualService;
import cn.com.tw.saas.serv.service.org.OrgUserService;

/**
 * 运维——人工抄表
 * @author admin
 *
 */
@RestController
@RequestMapping("readManual")
@Api(description = "运维——人工抄表接口")
public class ReadManualController {

	@Autowired
	private ReadManualService readManualService;
	@Autowired
	private OrgUserService orgUserService;

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "")
	@GetMapping("page")
	public Response<?> selectByPage(Page page) {
		try {
			List<ReadManual> list = readManualService.selectByPage(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}
	
	@ApiOperation(value = "单条新增", notes = "")
	@GetMapping("app")
	public Response<?> selectAppReadManual(String regionId){
		List<ReadManual> list = readManualService.selectAppReadManual(regionId);
		return Response.ok(list);
	}

	/**
	 * 新增记录
	 * 
	 * @param saasMeter
	 * @param br
	 * @return
	 */
	@ApiOperation(value = "单条新增", notes = "")
	@PostMapping()
	public Response<?> add(@RequestBody ReadManual param) {
		JwtInfo info = JwtLocal.getJwt();
		if (!StringUtils.isEmpty(info.getSubject())){
			param.setReadStaffId(info.getSubject());
			param.setReadStaff(info.getSubName());
		}
		param.setReadTime(new Date(System.currentTimeMillis()));
		readManualService.insertOne(param);
		return Response.ok();
	}

	@ApiOperation(value = "excel导入", notes = "")
	@PostMapping("/import")
	@ResponseBody
	public Response<?> excelImport(@RequestParam MultipartFile file, HttpServletRequest request) {
		final int rowStart = 3;
		final int cellStrart = 0;
		int result = 0;
		// 文件名称
		String fileName = file.getOriginalFilename();
		if (StringUtils.isEmpty(fileName)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "fileName is null!");
		}
		if (!fileName.substring(fileName.indexOf(".")+1, fileName.length()).equals("xls")
				&&!fileName.substring(fileName.indexOf(".")+1, fileName.length()).equals("xlsx")){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "expandedName error!");
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
		// 获取操作人信息
		JwtInfo info = JwtLocal.getJwt();
		String orgId = (String)info.getExt("orgId");
		String userId = info.getSubject();
		OrgUser orgUser = orgUserService.selectById(userId);
		String userName = orgUser.getUserRealName();
		boolean isBackMesaage = false;
		StringBuffer errorMessage = new StringBuffer();
		try {
			FileCopyUtils.copy(file.getBytes(), files);
			// 解析excel文件
			List<Object> dataList = ReadExcleUtils.readExcel(newFile, new ReadManualExcel(), rowStart, cellStrart);
			List<ReadManual> importList = new ArrayList<ReadManual>();
			if (dataList.size() > 0 && dataList != null) {
				for (int i = 0; i < dataList.size(); i++) {
					int errorRow = i+1;//当前行数
					ReadManualExcel excelTemp = (ReadManualExcel) dataList.get(i);
					ReadManual temp = new ReadManual();
					temp.setOrgId(orgId);
					temp.setReadStaffId(userId);
					temp.setReadStaff(userName);
					temp.setMeterAddr(excelTemp.getMeterAddr());
					int a = new BigDecimal(excelTemp.getReadValue()).compareTo(new BigDecimal(0));
					if(a < 0){
						isBackMesaage = true;
						errorMessage.append("第"+errorRow+"行不能为负数,");
						continue;
						//return Response.retn(ResultCode.PARAM_VALID_ERROR, "不能导入负数");
					}
					temp.setReadValue(new BigDecimal(excelTemp.getReadValue()));
					Date tempDate = new Date();
					try {
						tempDate = DateStrUtils.tdToDate(excelTemp.getReadTimeStr(), DateStrUtils.dayFormat);
						Date today = new Date();
						if(tempDate.getTime()>today.getTime()){
							return Response.retn(ResultCode.PARAM_VALID_ERROR, "不能超过当前时间");
						}
					} catch (Exception e) {
						throw e;
					}
					temp.setReadTime(tempDate);
					importList.add(temp);
				}
			} else {
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "读取失败，模板格式不正确");
			}
			if (importList.size()>0){
				result = readManualService.insertList(importList);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "读取失败，模板格式不正确");
		}catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "日期格式错误");
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
		if(isBackMesaage){
			errorMessage.deleteCharAt(errorMessage.length() - 1);
			return Response.retn("111111", errorMessage.toString(), result);
		}else{
			return Response.ok(result);
		}
	}
}
