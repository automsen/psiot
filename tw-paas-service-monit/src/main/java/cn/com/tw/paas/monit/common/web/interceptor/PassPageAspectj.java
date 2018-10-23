package cn.com.tw.paas.monit.common.web.interceptor;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.service.org.ApiLogService;
import cn.com.tw.paas.monit.service.org.OrgApplicationService;
import cn.com.tw.paas.monit.service.org.OrgUserService;



/**
 * api后置拦截器添加日志
 * 主要用来给机构单位查询时 在查询列表的时候加上机构过滤条件
 * @author liming
 * 2018年3月12日13:55:00
 *
 */
@Aspect   //定义一个切面
@Component
public class PassPageAspectj {
	
	public static final Logger logger = LoggerFactory.getLogger(PassPageAspectj.class);
	
	@Autowired
	private OrgUserService orgUserService;
	
	@Autowired
	private OrgApplicationService orgApplicationService;
	
	@Autowired
	private ApiLogService apiLogService;
	
	// 定义切点Pointcut
    @Pointcut("execution(* cn.com.tw.paas.monit.controller.*.*Controller.*(..))")
    public void excudeService() {
    }
    
	
	@SuppressWarnings("unchecked")
	@Around(value = "excudeService()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		JwtInfo jwt =  JwtLocal.getJwt();
		logger.debug("PassPageAspectj around, jwt = {}", jwt);
		if(jwt != null && !StringUtils.isEmpty(jwt.getSubject())){
			// 判断当前头部是否包含page
			if ((args != null) && (args.length > 0)) {
				for (Object obj : args) {
					if (obj instanceof Page) {
						Page page = (Page) obj;
						Map<String,Object> queryMap = (Map<String, Object>) page.getParamObj();
						// 根据user查询orgId
						//user = orgUserService.selectById(jwt.getSubject());
						String orgId = (String) jwt.getExt("orgId");
						if (!StringUtils.isEmpty(orgId)) {
							queryMap.put("orgId", orgId);
						}
						break;
					}
				}
			}
		}
		
		Object result = joinPoint.proceed();
		return result;
	}
 
}
