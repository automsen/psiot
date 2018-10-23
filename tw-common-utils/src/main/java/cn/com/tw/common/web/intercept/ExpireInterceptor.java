package cn.com.tw.common.web.intercept;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.utils.tools.license.ExpireLocal;
import cn.com.tw.common.utils.tools.license.ExpireUtils;
import cn.com.tw.common.utils.tools.license.HardwareBinder;
import cn.com.tw.common.utils.tools.license.LicExpireHandle;
import cn.com.tw.common.utils.tools.license.License;
import cn.com.tw.common.utils.tools.license.LicenseDate;
import cn.com.tw.common.utils.tools.license.exception.LicenseException;
import cn.com.tw.common.utils.tools.license.handle.UrlInterceptHandler;
import cn.com.tw.common.web.entity.Response;

@Component
public class ExpireInterceptor extends HandlerInterceptorAdapter {

	public static final Logger logger = LoggerFactory.getLogger(ExpireInterceptor.class);
	
	@Autowired(required = false)
	private UrlInterceptHandler urlInterceptHandler;
	
	@PostConstruct
	public void init(){
		try {
			LicExpireHandle lic = new LicExpireHandle();
			ExpireUtils.setLic(lic);
			createEnFile(lic.getIssueDate());
			lic.parse();
			
		} catch (LicenseException e) {
			
			if ("L001".equals(e.getCode())){
				throw new RuntimeException("license illegal or validation failure!! , e = " + e.toString());
			}else {
				logger.warn("License has expired. Please contact the administrator, e = " + e.toString(), e);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("license file Lose or validation failure!! , e = " + e.toString(), e);
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse, Object obj,
			Exception exception) throws Exception {
		super.afterCompletion(httpservletrequest, httpservletresponse, obj,
				exception);
		
		ExpireLocal.setExpireLocal(null);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
		//获取请求地址  
        //String reqUrl =request.getRequestURL().toString(); 
        String reqUrl = request.getServletPath();
        
        if (reqUrl.contains("/expire")){
        	return true;
        }
		
        //判断handler类型是否为HandlerMethod
		if(!handler.getClass().isAssignableFrom(HandlerMethod.class)){
			return true;
		}

		//判断该方法返回类型
		HandlerMethod invocation = (HandlerMethod) handler;
		Method method = invocation.getMethod();
		
		try {
			LicExpireHandle licExpire = ExpireUtils.getLic();
			licExpire.parse();
			
			//用来判断记录的过期时间 和 当前时间比较 如果比当前时间晚，则说明修改了系统时间， 不允许执行
			String storeInfo = ExpireUtils.getInfoFormFile(new File(ExpireUtils.fileStorePath()));
			
			LicenseDate licDate = new LicenseDate(DateUtils.formatDate(new Date(), "yyyyMMdd"));
			
			if (!licDate.isLaterThan(storeInfo)){
				logger.error("license expire or illegal!!,  System time cannot be modified and can not less than expire time!!!");
				return returnResp(request, response, method, "license expire or illegal!!,  System time cannot be modified and can not less than expire time!!!");
			}
			
		} catch (LicenseException e) {
			
			logger.error("license expire or check failure, e = " + e.toString());
			
			int expireDay = 0;
			String code = e.getCode();
			if ((code.contains("L002"))){
				
				try {
					expireDay = Integer.parseInt(code.split("_")[1]);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				File fileOut = new File(ExpireUtils.fileStorePath());
				long modifyTime = fileOut.lastModified();
				String lastMoifyTime = DateUtils.formatDate(new Date(modifyTime), "yyyyMMdd");
				String curDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
				if (!lastMoifyTime.equals(curDate)){
					ExpireUtils.storeInfoInFile(curDate, new File(ExpireUtils.fileStorePath()));
				}
			}else {
				return returnResp(request, response, method, e.toString());
			}
				
			ExpireLocal.setExpireLocal(new License(true, expireDay));
			
			if (urlInterceptHandler != null) {
				
				Map<Integer, String> urlConfig = urlInterceptHandler.getUrlConfig();
				
				List<Integer> keys = new ArrayList<Integer>(urlConfig.keySet());
				
				Collections.sort(keys);
				
				for (int i = 0; i < keys.size(); i++){
					
					int keyValue = keys.get(i);
					
					if (i == (keys.size() - 1)){
						if (!(keyValue <= expireDay)){
							continue;
						}
					}else{
						if (!(keyValue <= expireDay && keys.get(i + 1) > expireDay)){
							continue;
						}
					}
					
					String urls = urlConfig.get(keyValue);
					
					if (StringUtils.isEmpty(urls)){
						continue;
					}
					
					String[] urlArray = urls.split(",");
					for(String url : urlArray){
						if (reqUrl.contains(url)){
							return returnResp(request, response, method, "license expire or illegal!! expire " + expireDay + " day");
						}
					}
					
				}
				
				return true;
				
			}
			
			return returnResp(request, response, method, "license expire or illegal!! expire " + expireDay + " day");
		} catch (Exception e) {
			logger.error("license parse Exception , e = " + e.toString(), e);
		}
		return true;
	}
	
	private boolean returnResp(HttpServletRequest request, HttpServletResponse response, Method method, String expireInfo) throws IOException{
		if(("XMLHttpRequest").equalsIgnoreCase(request.getHeader("x-requested-with"))){
			response.getWriter().write(JsonUtils.objectToJson(new Response<Object>(ResultCode.SERVICE_EXPIRE_ERROR, expireInfo, null)));
		}else if (Response.class == method.getReturnType() || "cn.com.tw.engine.core.entity.CmdResponse" == method.getReturnType().getName()){
			response.getWriter().write(JsonUtils.objectToJson(new Response<Object>(ResultCode.SERVICE_EXPIRE_ERROR, expireInfo, null)));
		} else {
			response.sendRedirect("/expire");
		}
		return false;
	}
	
	private void createEnFile(String en) throws Exception{
		File file = new File(ExpireUtils.fileStorePath());
		
		if (file.exists()){
			
			String store = ExpireUtils.getInfoFormFile(file);
			
			LicenseDate licDate = new LicenseDate(DateUtils.formatDate(new Date(), "yyyyMMdd"));
			
			if (!licDate.isLaterThan(store)){
				throw new RuntimeException("license expire or illegal!!,  System time cannot be modified and can not less than expire time!!!");
			}
		}else {
			ExpireUtils.storeInfoInFile(en, new File(ExpireUtils.fileStorePath()));
		}
		
		try {
			File macheFile = new File("machine.txt");
			
			if (macheFile.exists()){
				macheFile.delete();
			}
			
			HardwareBinder binder = new HardwareBinder();
			String machineStr = binder.getMachineIdString();
			FileCopyUtils.copy(machineStr.getBytes(), new File("machine.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
