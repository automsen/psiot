package cn.com.tw.common.web.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
    @ExceptionHandler(value = RequestParamValidException.class)
    @ResponseBody
    public Response<?> jsonErrorHandler(HttpServletRequest req, RequestParamValidException e) throws Exception {
    	logger.error("RequestParamValidException ,e = ",e);
    	Object info = e.getInfo();
    	
    	if (info == null) {
    		return Response.retn(e.getCode(), null, e.getMessage());
    	}
    	
		@SuppressWarnings("unchecked")
		List<ObjectError> errorList = (List<ObjectError>) info;
    	
    	Map<String, String> result = new HashMap<String, String>();
    	for (ObjectError objError : errorList){
    		result.put(objError.getObjectName(), objError.getDefaultMessage());
    	}
        return Response.retn(e.getCode(), null, result);
    }
    
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Response<?> busExceptionHandler(HttpServletRequest req, BusinessException e) throws Exception {
    	logger.error("BusinessException ,e = ",e);
        return Response.retn(e.getCode(), e.getMessage(), null);
    }
    
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response<?> exceptionHandler(HttpServletRequest req, Exception e) throws Exception {
    	logger.error("Exception ,e = ",e);
        return Response.retn(ResultCode.UNKNOW_ERROR, "系统异常", null);
    }
    
}
