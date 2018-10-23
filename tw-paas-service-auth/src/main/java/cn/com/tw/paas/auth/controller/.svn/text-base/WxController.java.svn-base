package cn.com.tw.paas.auth.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.cache.ehcache.EHCache;
import cn.com.tw.common.utils.tools.http.HttpGetReq;
import cn.com.tw.common.utils.tools.http.entity.HttpRes;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.utils.tools.security.SignUtil;
import cn.com.tw.common.web.entity.Response;

@RestController
@RequestMapping("auth")
public class WxController {
	
	private Logger log = LoggerFactory.getLogger(WxController.class);
	
	@Value("${wx_appId}")
	private String appId;
	
	@Value("${wx_secret}")
	private String appsecret;
	
	@Value("${wx_http_get_access_token}")
	private String tokenHttpUrl;
	
	@Value("${wx_http_get_jsapi_ticket}")
	private String ticketHttpUrl;
	
	private int wxAuthTimeout = 3600;
	
	private final static String auth_ticket_key = "WX_AUTH_TICKETINFO";
	
	@Value("${http.proxy.hostname:}")
	private String hostName;
	
	@Value("${http.proxy.port:0}")
	private int port;
	
	private EHCache cache = EHCache.build();
	
	@SuppressWarnings("unchecked")
	@RequestMapping("wxconf")
	public Response<?> config(String url){
		
		log.debug("wx config ..... url: {}", url);
		Map<String, Object> map=new HashMap<String, Object>();//存储wx信息map
		//通过appId和appsecret  获取access_token
		//1.获取appId(第三方用户唯一凭证)
		//String appId=PropUtils.getProp("wx_appId");
		//2.获取appsecret第三方用户唯一凭证密钥
		//String appsecret=PropUtils.getProp("wx_secret");
		//3.获取时间戳
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		//4.获取随机生成随机串
		String noncestr=RandomStringUtils.randomAlphanumeric(16);
		//String noncestr = UUID.randomUUID().toString();
		//5.sha1签名的字符串
		String signature=null;
		String ticket=null;
		String accessToken=null;
		try {
			//判断缓存中是否有token和ticket
			String wxInfo = (String) cache.get("local", auth_ticket_key);
			if (StringUtils.isEmpty(wxInfo)) {
				//5.获取请求获取token
				//String tokenHttpUrl = PropUtils.getProp("wx_http_get_access_token");
				//String tokenHttpUrl = redisClient.hget("wx", "wx_http_get_access_token");
				log.debug(">> tokenHttpUrl = {}", tokenHttpUrl);
				HttpRes tokenResult = new HttpGetReq(tokenHttpUrl).setProxy(hostName, port).excuteReturnObj();
				log.debug("<< httpRes tokenResult = {}", tokenResult == null ? "" : tokenResult.toString());
				
				String data = tokenResult.getData();
				Map<String, Object> tokenJson = JsonUtils.jsonToPojo(data, Map.class);
				accessToken = (String) tokenJson.get("access_token");
				
				//6.获取jsapi_ticket
				//String ticketHttpUrl=PropUtils.getProp("wx_http_get_jsapi_ticket");
				//String ticketHttpUrl=redisClient.hget("wx", "wx_http_get_jsapi_ticket");
				log.debug(">> ticketHttpUrl = {}", ticketHttpUrl);
				HttpRes ticketTokenResult = new HttpGetReq(ticketHttpUrl.replaceAll("#0", accessToken)).setProxy(hostName, port).excuteReturnObj();
				log.debug("<< httpRes ticketTokenResult = {}", ticketTokenResult == null ? "" : ticketTokenResult.toString());
				String ticketData = ticketTokenResult.getData();
				Map<String, Object> ticketTokenJson = JsonUtils.jsonToPojo(ticketData, Map.class);
				ticket = (String) ticketTokenJson.get("ticket");
				
				//将token和ticket  放入redisClient缓存中(设置时间为3600秒  一个小时)
				cache.putLive("local", auth_ticket_key, new StringBuffer("{'accessToken':").append("\"").append(accessToken).append("\"").append(",'ticket':\"").append(ticket).append("\"}"), wxAuthTimeout);
				
			}else {
				//缓存中存在
				Map<String, Object> wxJson=JsonUtils.jsonToPojo(wxInfo, Map.class);
				ticket = (String) wxJson.get("ticket");
				accessToken = (String) wxJson.get("accessToken");
			}
			String sha1="jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
			signature=SignUtil.sha1(sha1);
/*			map.put("ticket", ticket);
			map.put("accessToken", accessToken);
*/			
			map.put("appId", appId);//(第三方用户唯一凭证)
			map.put("appsecret", appsecret);//第三方用户唯一凭证密钥
			map.put("timestamp", timestamp);//获取时间戳
			map.put("noncestr", noncestr);//获取随机生成随机串
			map.put("signature", signature);//签名的值
			return Response.ok(map);
		} catch (Exception e) {
			log.error("wx conf exception : {}", e);
			return Response.retn(ResultCode.UNKNOW_ERROR);
		}
	}

}
