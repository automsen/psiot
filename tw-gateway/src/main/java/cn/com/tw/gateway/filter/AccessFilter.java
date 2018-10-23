package cn.com.tw.gateway.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.security.core.JwtTokenFactory;
import cn.com.tw.common.security.entity.AuthEnum;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.utils.tools.sign.SignatureUtil;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.gateway.enm.ApiEnum;
import cn.com.tw.gateway.enm.ReqEum;
import cn.com.tw.gateway.service.OrgService;

import com.google.common.base.Joiner;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;

/**
 * Zuul过滤器
 * 1.验证用户身份的合法性
 * 2.验证用户对资源访问权限的合法性
 * 3.服务之间鉴权不走这里， 前期我们先不考虑
 * @author admin
 *
 */
@Component
public class AccessFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(AccessFilter.class);
	
	@Autowired
	private JwtTokenFactory jwtTokenFactory;
	
	@Value("${filter.exclude.path}")
	private String exInludePath;
	
	@Autowired
	private OrgService orgService;

	@Override
	public String filterType() {
		// 前置过滤器
		return "pre";
	}

	@Override
	public int filterOrder() {
		// 优先级，数字越大，优先级越低
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		// 是否执行该过滤器，true代表需要过滤
		return true;
	}

	@Override
	public Object run() {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		try {
			
			HttpServletRequest request = ctx.getRequest();
			String reqPath = request.getRequestURL().toString();
			log.info("send {} request to {}", request.getMethod(), reqPath);
			
			//排除请求url,如果匹配则不拦截直接让它过
			//AntPathMatcher
			String[] exPath = exInludePath.split(",");
			for (String path : exPath) {
				if (reqPath.contains(path)) {
					return null;
				}
			}
			
			//获取request中param的值
			Object params = getParams(request, ctx);
			
			log.info("request params data = {}", new Object[]{params == null ? "" : params.toString()});
			
			String auth = request.getHeader("Authorization");
			//如果auth为空，签名认证校验，如果有签名是第三方调用，如果没有签名则直接返回
			if (auth == null) {
				
				//通过消息头放入签名方式 "appKey:timestap:signStr", signStr生成：reqUrl:reqMethod:MD5(reqContentStr):timestap:appsecret
				String sign = request.getHeader("sign");
				
				//如果为空，通过第二种方式签名认证
				if (StringUtils.isEmpty(sign)) {
					
					//通过参数排序，签名认证
					JwtInfo jwtInfo = valiParam(params);
					setParams(request, ctx, params, jwtInfo);
					try {
						ctx.addZuulRequestHeader("authInfo", URLEncoder.encode(JsonUtils.objectToJson(jwtInfo),"UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					return null;
				}
				
				JwtInfo jwtInfo = valiSignHeader(sign, ctx);
				setParams(request, ctx, params, jwtInfo);
				return null;
			}
			
			//如果auth不为空，表示为浏览器客户端调用,通过jwt 解析用户信息，
			//如果是管理员信息中不包含企业和应用信息，如果是pass机构用户 或者 sass机构用户,信息中包括企业Id和appId，在后续的接口调用中可以使用
			JwtInfo jwtInfo = null;
			String[] auths = auth.split(" ");
			if (auths.length < 2) {
				retnError(ctx, JsonUtils.objectToJson(Response.retn(ResultCode.USER_NO_LOGIN_ERROR, "user valid error!")));
				return null;
			}
			String headStr = auths[0].toLowerCase();
			auth = auths[1];
			if (headStr.compareTo("adm") == 0) {
				jwtInfo = jwtTokenFactory.parseJWT(auth, AuthEnum.admin);
			} else if(headStr.compareTo("busp") == 0){
				jwtInfo = jwtTokenFactory.parseJWT(auth, AuthEnum.bus_p);
			} else if(headStr.compareTo("buss") == 0){
				jwtInfo = jwtTokenFactory.parseJWT(auth, AuthEnum.bus_s);
			} else if(headStr.compareTo("bussu") == 0){
				jwtInfo = jwtTokenFactory.parseJWT(auth, AuthEnum.buss_user);
			} else if(headStr.compareTo("sadminp") == 0){
				jwtInfo = jwtTokenFactory.parseJWT(auth, AuthEnum.sadmin_p);
			}
			if (jwtInfo == null) {
				retnError(ctx, JsonUtils.objectToJson(Response.retn(ResultCode.USER_NO_LOGIN_ERROR, "user valid error!")));
				return null;
			}
			
			//重新放入流中
			setParams(request, ctx, params, jwtInfo);
			
			ctx.addZuulRequestHeader("authInfo", URLEncoder.encode(JsonUtils.objectToJson(jwtInfo),"UTF-8"));
			
			// 如果有token，则进行路由转发
			log.info("access token ok");
			// 这里return的值没有意义，zuul框架没有使用该返回值
			return null;
		} catch (BusinessException b){
			log.error("api request error,  BusinessException = {}", b);
			retnError(ctx, JsonUtils.objectToJson(Response.retn(b.getCode(), b.getMessage())));
		} catch (Exception e) {
			log.error("api request error,  Exception = {}", e);
			retnError(ctx, JsonUtils.objectToJson(Response.retn(ResultCode.UNKNOW_ERROR, "api request error")));
		}
		
		return null;
	}
	
	/**
	 * 获取request请求参数
	 * @param request
	 * @param ctx
	 * @return
	 */
	private Object getParams(HttpServletRequest request, RequestContext ctx){
		//reqUrl:reqMethod:MD5(reqContentStr):reqContentType:timestap:appsecret
		ctx.set(ReqEum.reqUrl.name(), request.getRequestURL().toString());
		ctx.set(ReqEum.reqMethod.name(), request.getMethod().toUpperCase());
		ctx.set(ReqEum.reqContentType.name(), request.getContentType());
		
		if (ServletFileUpload.isMultipartContent(request)) {
			return null;
		}
		
		if(request.getMethod().equals(RequestMethod.POST.name()) || request.getMethod().equals(RequestMethod.PUT.name())){
			Object jsonObj = null;
			try {
				String jsonStr = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
				
				//body MD5
				if (!StringUtils.isEmpty(jsonStr)) {
					ctx.set(ReqEum.reqContent.name(), DigestUtils.md5Hex(jsonStr).toUpperCase());
				}
				
				Object json = new JSONTokener(jsonStr).nextValue();
				if(json instanceof JSONObject){  
					jsonObj = (JSONObject)json;
				}else if (json instanceof JSONArray){  
					jsonObj = (JSONArray)json;  
				}
			} catch (IOException e) {
				log.error("IOException, e = {}", e);
			} catch (JSONException e) {
				log.error("JSONException, e = {}", e);
			}
			return jsonObj;
		} else {
			JSONObject params = new JSONObject();
			Map<String, String> paramsMap = new HashMap<String, String>();
			try {
				Enumeration<String> parameterNames = request.getParameterNames();
				while (parameterNames.hasMoreElements()) {
					String parameterName = parameterNames.nextElement();
					String value = request.getParameter(parameterName);
					params.put(parameterName, value);
					paramsMap.put(parameterName, value);
				}
			} catch (JSONException e) {
				log.error("JSONException, e = {}", e);
			}
			
			//转化成URL键值对形式 MD5
			String data = Joiner.on("&").withKeyValueSeparator("=").join(paramsMap);
			
			//body MD5
			ctx.set(ReqEum.reqContent.name(), DigestUtils.md5Hex(data));
			
			return params;
		}
	}
	
	private void setParams(HttpServletRequest request, RequestContext ctx, Object paramsObj, JwtInfo jwtInfo) {
		if (paramsObj == null) {
			return;
		}
		
		String orgId = (String) jwtInfo.getExt("orgId");
		String appId = (String) jwtInfo.getExt("appId");
		String userId = (String) jwtInfo.getSubject();
		
		Object obj = null;
		if (paramsObj instanceof JSONObject) {
			JSONObject params = (JSONObject) paramsObj;
			setParamValue(params, orgId, appId, userId);
			obj = params;
		} else if (paramsObj instanceof JSONArray) {
			JSONArray params = (JSONArray) paramsObj;
			for (int i = 0 ; i < params.length(); i++) {
				try {
					setParamValue(params.getJSONObject(i), orgId, appId, userId);
				} catch (JSONException e) {
					continue;
				}
			}
			obj = params;
		}
		
		log.info("set params data = {}", new Object[]{obj == null ? "" : obj.toString()});
		if(request.getMethod().equals(RequestMethod.POST.name()) || request.getMethod().equals(RequestMethod.PUT.name())){
			if (obj == null) {
				return;
			}
			
			byte[] jsonBytes = obj.toString().getBytes();
			ctx.setRequest(new HttpServletRequestWrapper(request) {
                @Override
                public ServletInputStream getInputStream() throws IOException {
                    return new ServletInputStreamWrapper(jsonBytes);
                }

                @Override
                public int getContentLength() {
                    return jsonBytes.length;
                }

                @Override
                public long getContentLengthLong() {
                    return jsonBytes.length;
                }
            });
		} else {
			Map<String,List<String>> qpMap = new HashMap<String, List<String>>();
			
			if (obj instanceof JSONObject) {
				
				JSONObject jsonObj = (JSONObject) obj;
				
				Iterator<?> it = jsonObj.keys();
				
				while(it.hasNext()) {
					try {
						String key = (String) it.next();
						qpMap.put(key, addList((String) getValue(jsonObj, key)));
					} catch (Exception e) {
						continue;
					}
				}
				ctx.setRequestQueryParams(qpMap);
			}
			
		}
	}
	
	private String retnError(RequestContext ctx, String msg){
		log.error("access token is empty");
		// 过滤该请求，不往下级服务去转发请求，到此结束
		ctx.setSendZuulResponse(false);
		ctx.setResponseStatusCode(401);
		ctx.setResponseBody(msg);
		return null;
	}
	
	private void setParamValue(JSONObject params, String orgId, String appId, String userId){
		
		if (params == null) {
			return;
		}
		
		//重新修改添加参数
		if (isPageParam(params)) {
			if (params.has("paramObj")) {
				JSONObject paramObjMap = (JSONObject) getValue(params, "paramObj");
				if (!StringUtils.isEmpty(orgId)) {
					setValue(paramObjMap, "orgId", orgId);
				}
				
				if (!StringUtils.isEmpty(appId)) {
					setValue(paramObjMap, "appId", appId);
				}
				
				/*if (!StringUtils.isEmpty(userId)) {
					setValue(paramObjMap, "userId", userId);
				}*/
			}else {
				if (!StringUtils.isEmpty(orgId)) {
					setValue(params, "paramObj[orgId]", orgId);
				}
				
				if (!StringUtils.isEmpty(appId)) {
					setValue(params, "paramObj[appId]", appId);
				}
				
				/*if (!StringUtils.isEmpty(userId)) {
					setValue(params, "paramObj[userId]", userId);
				}*/
			}
		}else {
			
			if (!StringUtils.isEmpty(orgId)) {
				setValue(params, "orgId", orgId);
			}
			
			if (!StringUtils.isEmpty(appId)) {
				setValue(params, "appId", appId);
			}
			
			/*if (!StringUtils.isEmpty(userId)) {
				setValue(params, "userId", userId);
			}*/
		}
		
		log.debug("params = {}", params.toString());
	}
	
	/**
	 * 签名校验 reqUrl:reqMethod:MD5(reqContentStr):timestap:appsecret
	 * @param sign
	 * @param ctx
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JwtInfo valiSignHeader(String sign, RequestContext ctx) {
		
		String[] signArray = sign.split(":");
		
		if (signArray.length != 3) {
			throw new BusinessException(ApiEnum.signError.getCode(), ApiEnum.signError.getMsg());
		}
		
		String appKey = signArray[0];
		String time = signArray[1];
		String signStr = signArray[2];
		
		//缓存过期或未第一次访问， 先从数据库中查询
		// appid 身份验证 获取secret
		Response<?> response = orgService.selectByApk(appKey);
		Map<String, Object> orgInfo = (Map<String, Object>) response.getData();
		
		if(orgInfo == null || StringUtils.isEmpty(orgInfo.get("secretKey"))){
			throw new BusinessException(ApiEnum.appidError.getCode(), ApiEnum.appidError.getMsg());
		}
		
		String secretKey = (String) orgInfo.get("secretKey");
		
		StringBuffer sb = new StringBuffer();
		sb.append(ctx.get(ReqEum.reqUrl.name())).append(":").append(ctx.get(ReqEum.reqMethod.name())).append(":").append(ctx.get(ReqEum.reqContent.name()))
		.append(":").append(time).append(":").append(secretKey);
		
		if(!DigestUtils.md5Hex(sb.toString()).toUpperCase().equals(signStr)) {
			throw new BusinessException(ApiEnum.signError.getCode(), ApiEnum.signError.getMsg());
		}
		
		JwtInfo jwtInfo = new JwtInfo();
		jwtInfo.setExtend("appId", orgInfo.get("appId"));
		jwtInfo.setExtend("orgId", orgInfo.get("orgId"));
		
		try {
			ctx.addZuulRequestHeader("authInfo", URLEncoder.encode(JsonUtils.objectToJson(jwtInfo),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jwtInfo;
	}
	
	@SuppressWarnings("unchecked")
	private JwtInfo valiParam(Object paramObj) {
		
		if (paramObj == null) {
			return null;
		}
		
		if (!(paramObj instanceof JSONObject)) {
			return null;
		}
		
		JSONObject params = (JSONObject) paramObj;
		String appKey = (String) getValue(params, "appKey");
		// 必填项配置
		if(StringUtils.isEmpty((String) getValue(params, "sign"))){
			throw new BusinessException(ApiEnum.signNull.getCode(), ApiEnum.signNull.getMsg());
		}
		String businessNo = (String) getValue(params, "businessNo");
		if(StringUtils.isEmpty(businessNo)){
			throw new BusinessException(ApiEnum.businessNoNull.getCode(), ApiEnum.businessNoNull.getMsg());
		}
		if(StringUtils.isEmpty(appKey)){
			throw new BusinessException(ApiEnum.appidNull.getCode(), ApiEnum.appidNull.getMsg());
		}
		String timestamp = (String) getValue(params, "timestamp");
		if(StringUtils.isEmpty(timestamp)){
			throw new BusinessException(ApiEnum.timeNull.getCode(), ApiEnum.timeNull.getMsg());
		}

		//缓存过期或未第一次访问， 先从数据库中查询
		// appid 身份验证 获取secret
		Response<?> response = orgService.selectByApk(appKey);
		Map<String, Object> orgInfo = (Map<String, Object>) response.getData();
		
		if(orgInfo == null || StringUtils.isEmpty(orgInfo.get("secretKey"))){
			throw new BusinessException(ApiEnum.appidError.getCode(), ApiEnum.appidError.getMsg());
		}
		
		String secretKey = (String) orgInfo.get("secretKey");
		
		Map<String, String> paramStrMap = new HashMap<String, String> ();
		
		
		@SuppressWarnings("rawtypes")
		Iterator iterator = params.keys();
		while(iterator.hasNext()) {
			try {
				String key = (String) iterator.next();
				paramStrMap.put(key, params.getString(key));
			} catch (JSONException e) {
				continue;
			}
		}
		
		// 加密判断是否相等
		if(!SignatureUtil.validateSign(paramStrMap,secretKey)){
			throw new BusinessException(ApiEnum.signError.getCode(), ApiEnum.signError.getMsg());
		}
		
		JwtInfo jwtInfo = new JwtInfo();
		jwtInfo.setExtend("appId", orgInfo.get("appId"));
		jwtInfo.setExtend("orgId", orgInfo.get("orgId"));
		
		return jwtInfo;
	}
	
	private Object getValue(JSONObject params, String key) {
		if (params == null) {
			return null;
		}
		try {
			return params.get(key);
		} catch (JSONException e) {
			log.error("JSONException, e = {}", e);
			return null;
		}
	}
	
	private void setValue(JSONObject params, String key, Object value) {
		if (params == null) {
			return;
		}
		
		try {
			params.put(key, value);
		} catch (JSONException e) {
			return;
		}
	}

	private List<String> addList(String value){
		List<String> valueList = new ArrayList<String>();
		valueList.add(value);
		return valueList;
	}
	
	private boolean isPageParam(JSONObject params) {
		
		Iterator<?> it = params.keys();
		
		while(it.hasNext()) {
			String key = (String) it.next();
			if (key.contains("paramObj[")) {
				return true;
			}
		}
		
		if (params.has("paramObj")) {
			return true;
		}
		
		return false;
	}
}
