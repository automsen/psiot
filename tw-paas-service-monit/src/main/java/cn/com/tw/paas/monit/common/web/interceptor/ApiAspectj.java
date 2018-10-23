/*package cn.com.tw.paas.monit.common.web.interceptor;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.common.utils.SignatureUtil;
import cn.com.tw.paas.monit.common.utils.StreamUtils;
import cn.com.tw.paas.monit.common.utils.cons.ApiEnum;
import cn.com.tw.paas.monit.entity.business.org.OrgApplicationExpand;
import cn.com.tw.paas.monit.entity.db.org.ApiLog;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.service.org.ApiLogService;
import cn.com.tw.paas.monit.service.org.OrgApplicationService;



*//**
 * api后置拦截器添加日志
 * @author liming
 * 2018年3月12日13:55:00
 *
 *//*
@Aspect   //定义一个切面
@Component
public class ApiAspectj {
	
	private Logger logger = LoggerFactory.getLogger(ApiAspectj.class);
	
	
	
	@Autowired
	private OrgApplicationService orgApplicationService;
	
	@Autowired
	private ApiLogService apiLogService;
	
	// 定义切点Pointcut
    @Pointcut("execution(* cn.com.tw.paas.monit.controller.api.*Controller.*(..))")
    public void excudeService() {
    }
    @Pointcut("execution(* cn.com.tw.paas.monit.controller.directive.*Controller.*(..))")
    public void excudeDeviceService() {
    }
    
	@Around(value = "excudeDeviceService()")
	public Object deviceAround(ProceedingJoinPoint pjp) throws Throwable {
    	return around( pjp);
    }
	@SuppressWarnings("unchecked")
	@Around(value = "excudeService()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		
		HttpServletRequest request = getRequest();
		String access_token = request.getHeader("Authorization");
		*//**
		 *  前置数据验证
		 *//*
		ApiLog apiLog = new ApiLog();
		
		// 页面访问
		if(!StringUtils.isEmpty(access_token)){
			
			apiLog.setApiUrl(request.getRequestURI());
			apiLog.setCreateTime(new Date());
			apiLog.setLogId(CommUtils.getUuid());
			apiLog.setRequestorIp(request.getRemoteAddr());
			apiLog.setRequestTime(new Date());
		}else{
			// sign 验证
			Map<String, String> params = new HashMap<String, String>();
			
			if(request.getMethod().equals(RequestMethod.GET.name())){
				Enumeration<String> parameterNames = request.getParameterNames();
				while (parameterNames.hasMoreElements()) {
					String parameterName = parameterNames.nextElement();
					params.put(parameterName, request.getParameter(parameterName));
				}
			}else if(request.getMethod().equals(RequestMethod.POST.name())){
				String jsonStr = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
				params = JsonUtils.jsonToPojo(jsonStr, Map.class);
			}
			String secretKey = null;
			OrgApplication application = null;
			String appKey = params.get("appKey");
			// 必填项配置
			if(StringUtils.isEmpty(params.get("sign") )){
				return Response.retn(ApiEnum.signNull.getCode(), ApiEnum.signNull.getMsg());
			}
			String businessNo = params.get("businessNo");
			if(StringUtils.isEmpty(businessNo)){
				return Response.retn(ApiEnum.businessNoNull.getCode(), ApiEnum.businessNoNull.getMsg());
			}
			if(StringUtils.isEmpty(params.get("appKey"))){
				return Response.retn(ApiEnum.appidNull.getCode(), ApiEnum.appidNull.getMsg());
			}
			String timestamp = params.get("timestamp");
			if(StringUtils.isEmpty(timestamp)){
				return Response.retn(ApiEnum.timeNull.getCode(), ApiEnum.timeNull.getMsg());
			}
		
			// 缓存过期或未第一次访问， 先从数据库中查询
				// appid 身份验证 获取secret
			application = orgApplicationService.selectByAppKey(appKey);
			if(application == null || StringUtils.isEmpty(application.getSecretKey())){
				return Response.retn(ApiEnum.appidError.getCode(), ApiEnum.appidError.getMsg());
			}
			secretKey = application.getSecretKey();
			application.setAppBussinesNo(getBussinesNo());
			application.setBussinesNo(businessNo);
			// 加密判断是否相等
			if(!SignatureUtil.validateSign(params,secretKey)){
				return Response.retn(ApiEnum.signError.getCode(), ApiEnum.signError.getMsg());
			}
			//添加操作日志 到线程变量
			OrgApplicationExpand app = orgApplicationService.selectByppId(application.getAppId());
			apiLog.setApiUrl(request.getRequestURI());
			apiLog.setAppBusinessNo(application.getAppBussinesNo());
			apiLog.setAppId(application.getAppId());
			apiLog.setBusinessNo(application.getBussinesNo());
			apiLog.setCreateTime(new Date());
			apiLog.setLogId(CommUtils.getUuid());
			apiLog.setOrgId(application.getOrgId());
			apiLog.setRequestData(JsonUtils.objectToJson(params));
			apiLog.setRequestorIp(request.getRemoteAddr());
			apiLog.setRequestTime(new Date());
			apiLog.setAppName(app.getAppName());
			apiLog.setOrgName(app.getOrgName());
			// 将值传递到页面
			request.setAttribute("application", application);
			request.setAttribute("params",params );
		}
		Object obj = pjp.proceed();
		// 插入日志
		doAfterReturn(obj,apiLog);
		// 返回值
		return obj;
	}
 
	
	*//**
	 * 保存日志
	 * @param returnObj
	 *//*
	public void doAfterReturn(Object returnObj,ApiLog apiLog){
		logger.info(returnObj.toString());
		try {
			Response<?> result = (Response<?>) returnObj;
			// 暂时只存储1000
			apiLog.setResponseData(JsonUtils.objectToJson(result));
			if (apiLog.getResponseData().length()>1000){
				apiLog.setResponseData(apiLog.getResponseData().substring(0, 999));
			}
			apiLog.setStatusCode(result.getStatusCode());
			apiLogService.insert(apiLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private HttpServletRequest getRequest() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		return request;
	}
	
	private String getBussinesNo(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String bussinesNo = sdf.format(new Date())+(int)(Math.random()*9+1)*1000;
		return bussinesNo;
	}
	
	
}
*/