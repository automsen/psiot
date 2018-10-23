package cn.com.tw.common.web.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.anno.log.LogDesc;
import cn.com.tw.common.web.aop.enm.LogFieldEnum;
import cn.com.tw.common.web.aop.enm.LogTypeNum;
import cn.com.tw.common.web.core.log.LogService;

/**
 * 日志切面
 * @author admin
 *
 */
@Component
@Aspect
public class LogAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);  
	
	@Autowired(required = false)
	private LogService logService;
	
	@Pointcut("@annotation(cn.com.tw.common.web.anno.log.LogDesc)")
	private void cut() {}
	
	@Around("cut()")
	public Object advice(ProceedingJoinPoint joinPoint) throws Throwable{
		
		//定义开始时间
		long startTime = System.currentTimeMillis();
		
		Method method = getMethod(joinPoint);
		
		//获取log注解
		LogDesc logDesc = getAnnoLog(joinPoint);
		
		//获取session
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		Object userSession = session.getAttribute(SysCons.USER_SESSION);
		
		//获取参数信息
		Object[] args = joinPoint.getArgs();
		String argsJson = null;
		if (args != null && args.length > 0){
			List<Object> objArray = new ArrayList<Object>();
			for (Object obj : args){
				if(obj instanceof BindingResult){
					continue;
				}
				
				if(obj instanceof HttpServletRequest){
					continue;
				}
				
				objArray.add(obj);
			}
			try {
				argsJson = JsonUtils.objectToJson(objArray);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		logger.debug(SysCons.LOG_START + "{}, {}, param:{}", new Object[]{method.getName(), logDesc.value(), argsJson});
        Object result = joinPoint.proceed();
        //结束时间
        long endTime = System.currentTimeMillis();  
        logger.debug(SysCons.LOG_START + "{}, {}, result = {}", new Object[]{method.getName(), logDesc.value(), (result == null ? "无" : JsonUtils.objectToJson(result))});
        
        if (logService != null){
        	try {
	        	Map<String, Object> params = new HashMap<String, Object>();
	        	
	        	if (logDesc.type() == LogTypeNum.DEFAULT) {
	        		String lowerMethodName = method.getName().toLowerCase();
	        		if(lowerMethodName.contains("add") || lowerMethodName.contains("insert")){
	        			params.put(LogFieldEnum.LOG_TYPE.name(), LogTypeNum.ADD.name());
	        		}else if(lowerMethodName.contains("edit") || lowerMethodName.contains("update")){
	        			params.put(LogFieldEnum.LOG_TYPE.name(), LogTypeNum.UPDATE.name());
	        		}else if(lowerMethodName.contains("query") || lowerMethodName.contains("select")){
	        			params.put(LogFieldEnum.LOG_TYPE.name(), LogTypeNum.GET.name());
	        		}else if(lowerMethodName.contains("delete")){
	        			params.put(LogFieldEnum.LOG_TYPE.name(), LogTypeNum.DELETE.name());
	        		}
	        	} else {
	        		params.put(LogFieldEnum.LOG_TYPE.name(), logDesc.type().name());
	        	}
	        	
	        	//params.put(LogFieldEnum.LOG_TYPE.name(), logDesc.type().name());
	        	params.put(LogFieldEnum.IS_OPERA_FAIL.name(), 0);
	        	params.put(LogFieldEnum.LOG_CONTENT.name(), logDesc.value());
	        	params.put(LogFieldEnum.LOG_COST_TIME.name(), endTime - startTime);
	        	params.put(LogFieldEnum.LOG_CREATE_TIME.name(), new Date());
	        	params.put(LogFieldEnum.LOG_OPERATER_SESSION.name(), userSession);
	        	params.put(LogFieldEnum.LOG_DATA.name(), argsJson);
        	
        	
				logService.insert(params);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return result;
	}
	
	private LogDesc getAnnoLog(ProceedingJoinPoint joinPoint){
		Method method = getMethod(joinPoint);
		LogDesc logDesc = method.getAnnotation(LogDesc.class);
		return logDesc;
	}
	
	private Method getMethod(ProceedingJoinPoint joinPoint){
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		return method;
	}
	
	private HttpServletRequest getRequest(){
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        return request;
	}

}
