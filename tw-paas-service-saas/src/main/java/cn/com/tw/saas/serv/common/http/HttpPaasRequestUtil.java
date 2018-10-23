package cn.com.tw.saas.serv.common.http;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import cn.com.tw.common.utils.tools.http.HttpGetReq;
import cn.com.tw.common.utils.tools.http.HttpPostReq;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.saas.serv.common.utils.MapUtil;
import cn.com.tw.saas.serv.common.utils.SignatureUtil;

public class HttpPaasRequestUtil {
	
	private static final String  APPKEY = Env.getVal("paas.appKey");
	
	private static final String SECRET = Env.getVal("paas.secret");
	
	private static final String URL = Env.getVal("paas.url");
	
	
	
	public static String signGetRequest(String url,Map<String,String> requestMap) throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		if(requestMap == null){
			throw new Exception("requestObj can not be null!");
		}
		
		requestMap.put("appKey", APPKEY);
		requestMap.put("timestamp", sdf.format(new Date()));
		requestMap.put("businessNo", "4445");
		String sign = SignatureUtil.generateSign(requestMap, SECRET);
		requestMap.put("sign", sign);
		
		try {
			HttpGetReq req = new HttpGetReq(URL+url+"?"+MapUtil.mapJoin(requestMap, false, false));
			return req.excuteReturnStr();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String signPostRequest(String url,Map<String,String> requestMap) throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		if(requestMap == null){
			throw new Exception("requestObj can not be null!");
		}
		requestMap.put("appKey", APPKEY);
		requestMap.put("timestamp", sdf.format(new Date()));
		String sign = SignatureUtil.generateSign(requestMap, SECRET);
		requestMap.put("sign", sign);
		try {
			StringEntity entity = new StringEntity(JsonUtils.objectToJson(requestMap),"utf-8");//解决中文乱码问题    
	        entity.setContentEncoding("UTF-8");    
	        entity.setContentType("application/json");
			HttpPostReq req = new HttpPostReq(url,null,entity);
			return req.excuteReturnStr();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
