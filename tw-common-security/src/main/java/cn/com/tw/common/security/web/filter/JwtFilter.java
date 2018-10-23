package cn.com.tw.common.security.web.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;

//重点
@WebFilter(filterName = "jwtFilter", urlPatterns = "/**")
public class JwtFilter implements Filter {
	
	public static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	
	//定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request,
			ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String auth = httpRequest.getHeader("authInfo");
		logger.debug("req url is {}, req authInfo is {}", httpRequest.getRequestURI(), auth);
		JwtLocal.setJwt(StringUtils.isEmpty(auth) ? new JwtInfo() : jsonToPojo(URLDecoder.decode(auth, "UTF-8"), JwtInfo.class));
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
	
	/**
    * json转对象
    * @param jsonData
    * @param beanType
    * @return
    */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
    	if(StringUtils.isEmpty(jsonData)){
    		return null;
    	}
    	
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
}