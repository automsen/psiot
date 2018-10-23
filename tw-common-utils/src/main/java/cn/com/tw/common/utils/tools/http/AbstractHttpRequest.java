package cn.com.tw.common.utils.tools.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.tools.http.config.HttpConfig;
import cn.com.tw.common.utils.tools.http.entity.HttpCode;
import cn.com.tw.common.utils.tools.http.entity.HttpRes;


/**
 * client抽象实现
 * @author tanglongsen
 *
 */
public abstract class AbstractHttpRequest {

	private CloseableHttpClient httpClient;
	
	private Header header;
	
	private boolean isProxy = false;
	
	private String hostName;
	
	private int port;
	
	private static HttpConfig httpConfig = new HttpConfig();
	
	static {
		
		InputStream resource = AbstractHttpRequest.class.getResourceAsStream("/httpclient.properties");  
	    if(resource != null){
	    	Properties prop = new Properties();
			try {
				prop.load(resource);
				httpConfig.setPoolMaxTotal(Integer.parseInt((String) prop.get("httpclient.poolMaxTotal")));
				httpConfig.setMaxPerRoute(Integer.parseInt((String) prop.get("httpclient.maxPerRoute")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }  
		
	}
	
	public AbstractHttpRequest(){
		init();
	}
	
	public abstract RequestConfig getRequestConfig();
	
	//执行
	public HttpRes excuteReturnObj() throws IOException{
		
		String returnContent = "";
		
		HttpRes res = new HttpRes();
		
		CloseableHttpResponse response = null;
		try {
			
			HttpRequestBase httpExcute = this.getHttpMethod();
			httpExcute.addHeader(header);
			
			//RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(httpConfig.getSocketTimeout()).setConnectTimeout(httpConfig.getConnectTimeout()).build();
			httpExcute.setConfig(getRequestConfig());
			response = httpClient.execute(httpExcute);
			
			//获取状态吗
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200 || statusCode == 201 || statusCode == 202 || statusCode == 203
					|| statusCode == 204 || statusCode == 205 || statusCode == 206){
				returnContent = EntityUtils.toString(response.getEntity(), "UTF-8");
				res.setCode(HttpCode.RES_SUCCESS);
				res.setData(returnContent);
				EntityUtils.consume(response.getEntity());
			}else{
				res.setCode(String.valueOf(statusCode));
				res.setMessage("request exception, response code is " + statusCode);
			}
			
		} finally {
			if(response != null){
				response.close();
			}
		}
		
		return res;
	}
	
	//执行
	public String excuteReturnStr() throws IOException{
		
		String returnContent = "";
		
		CloseableHttpResponse response = null;
		try {
			
			HttpRequestBase httpExcute = this.getHttpMethod();
			httpExcute.addHeader(header);
			
			//RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(httpConfig.getSocketTimeout()).setConnectTimeout(httpConfig.getConnectTimeout()).build();
			httpExcute.setConfig(getRequestConfig());
			response = httpClient.execute(httpExcute);
			
			//获取状态吗
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200 || statusCode == 201 || statusCode == 202 || statusCode == 203
					|| statusCode == 204 || statusCode == 205 || statusCode == 206){
				returnContent = EntityUtils.toString(response.getEntity(), "UTF-8");
				EntityUtils.consume(response.getEntity());
			}
			
		} finally {
			if(response != null){
				response.close();
			}
		}
		
		return returnContent;
	}
	
	/**
	 * 处理文件流
	 * @param writeUrl
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void excuteStream(String writePath) throws IllegalStateException, IOException{
		
		CloseableHttpResponse response = null;

		InputStream instream = null;
		
		OutputStream os = null;
		try {
			
			HttpRequestBase httpExcute = this.getHttpMethod();
			httpExcute.addHeader(header);
			response = httpClient.execute(httpExcute);
			
			//获取状态吗
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200 || statusCode == 201 || statusCode == 202 || statusCode == 203
					|| statusCode == 204 || statusCode == 205 || statusCode == 206){
				HttpEntity entity = response.getEntity();
				
				if (entity != null) {
					instream = entity.getContent();
					os = new FileOutputStream(new File(writePath));
						
					byte[] bytes = new byte[1024];
					int count;
					while((count = instream.read(bytes)) != -1){
						os.write(bytes, 0, count);
					}
					os.flush();
				}
			}else{
				
			}
			
		
		} finally {
			if(instream != null){
				instream.close();
			}
			
			if(os != null){
				os.close();
			}
			
			if(response != null){
				response.close();
			}
		}
	};
	
	public void init(){
		this.httpClient = HttpClientUtils.getHttpClient(httpConfig);
		//this.header = new BasicHeader("Content-Type", "text/html;charset=utf-8");
	}
	
	public abstract HttpRequestBase getHttpMethod() throws UnsupportedEncodingException;
	
	public boolean isProxy() {
		return isProxy;
	}

	public void setProxy(boolean isProxy) {
		this.isProxy = isProxy;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public AbstractHttpRequest setProxy(String hostName, int port) {
		this.hostName = hostName;
		this.port = port;
		if (!StringUtils.isEmpty(hostName) && port > 0) {
			this.isProxy = true;
		}
		return this;
	}

}
