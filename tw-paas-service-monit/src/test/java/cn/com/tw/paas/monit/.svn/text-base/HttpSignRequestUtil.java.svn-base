package cn.com.tw.paas.monit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import cn.com.tw.common.utils.tools.http.HttpGetReq;
import cn.com.tw.common.utils.tools.http.HttpPostReq;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.utils.tools.sign.MapUtil;
import cn.com.tw.common.utils.tools.sign.SignatureUtil;

/**
 * pass平台   签名请求
 * @author admin
 *
 */
public class HttpSignRequestUtil {

	
	private static final String  APPKEY = "APP_5fa1b7d4a800000";
	
	private static final String SECRET = "1161f6938f2c469ea65bf77c60cdb0f1";
	
	private static final String URL = "http://api.tw-iot.cn/ps_api/directive/close1";
	
	public static String signGetRequest(String url,Map<String,String> requestMap) throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		if(requestMap == null){
			throw new Exception("requestObj can not be null!");
		}
		requestMap.put("appid", APPKEY);
		requestMap.put("timestamp", sdf.format(new Date()));
		String sign = SignatureUtil.generateSign(requestMap, SECRET);
		requestMap.put("sign", sign);
		try {
			HttpGetReq req = new HttpGetReq(url+"?"+MapUtil.mapJoin(requestMap, false, false));
			return req.excuteReturnStr();
		} catch (Exception e) {
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
	
	public static String signHeaderPostRequest(String url, Map<String, Object> requestMap) throws Exception{
		
		long time = new Date().getTime();
		
		String reqContent = JsonUtils.objectToJson(requestMap);
		// reqUrl:reqMethod:MD5(reqContentStr):timestap:appsecret
		System.out.println(DigestUtils.md5Hex(reqContent).toUpperCase());
		StringBuffer sb = new StringBuffer();
		sb.append(url).append(":").append("POST").append(":").append(DigestUtils.md5Hex(reqContent).toUpperCase())
		.append(":").append(time).append(":").append(SECRET);
		
		try {
			StringEntity entity = new StringEntity(reqContent, "utf-8");//解决中文乱码问题    
			entity.setContentEncoding("UTF-8");    
	        entity.setContentType("application/json");
	        
	        BasicHeader header = new BasicHeader("sign", APPKEY+":"+time+":"+DigestUtils.md5Hex(sb.toString()));
	        BasicHeader[] headers = new BasicHeader[1];
	        headers[0] = header;
			HttpPostReq req = new HttpPostReq(url, headers, entity);
			return req.excuteReturnStr();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String httpRequest(String requestUrl) {
		StringBuffer buffer = new StringBuffer();
		try {
		URL url = new URL(requestUrl);
		HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
		httpUrlConn.setDoOutput(false);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);

		httpUrlConn.setRequestMethod("GET");
		httpUrlConn.connect();

		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
		buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		// 释放资源
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();


		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
		}
	
	
	public static void main(String[] args) {
		//Map<String,Object> queryMap = new HashMap<String,Object>();
		List<String> equipNumbers = new ArrayList<String>();
		Map<String, String> queryMap = new HashMap<String, String>();
		/*equipNumbers.add("000000242530");
		equipNumbers.add("000000242531");
		queryMap.put("equipNumbers",equipNumbers);*/
		//queryMap.put("appKey",APPKEY);
		queryMap.put("businessNo", "124141242");
		/*queryMap.put("startTime", String.valueOf(new Date().getTime()));
		queryMap.put("endTime", String.valueOf(new Date().getTime()));
		queryMap.put("page", "1");
		queryMap.put("rows", "10");*/
		//queryMap.put("netNumber", "124141242");
		//queryMap.put("newEquipNumber", "124141242");
		
		try {
				String results = HttpSignRequestUtil.signPostRequest(URL,queryMap);
				//String results = HttpSignRequestUtil.signGetRequest(URL,queryMap);
				System.out.println(results);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
