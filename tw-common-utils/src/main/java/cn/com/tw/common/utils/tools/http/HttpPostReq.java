package cn.com.tw.common.utils.tools.http;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

public class HttpPostReq extends AbstractHttpRequest{

	//请求URL地址
	private String url;
	
	//消息头设置
	private BasicHeader[] header;
	
	//请求体
	private List<BasicNameValuePair> params;
	
	//请求体，可以使用二进制
	private HttpEntity reqEntity;
	
	/**
	 * 连接超时等待时间
	 */
	private int connectTimeout = 2000;
	
	/**
	 * socket连接等待时间
	 */
	private int socketTimeout = 2000;
	
	public HttpPostReq(String url){
		this.url = url;
	}
	
	public HttpPostReq(String url, BasicHeader[] header){
		this.url = url;
		this.header = header;
	}
	
	public HttpPostReq(String url, BasicHeader[] header, List<BasicNameValuePair> params){
		this.url = url;
		this.header = header;
		this.params = params;
	}
	
	public HttpPostReq(String url, BasicHeader[] header, HttpEntity reqEntity){
		this.url = url;
		this.header = header;
		this.reqEntity = reqEntity;
	}
	
	public HttpPostReq(String url, BasicHeader[] header, FileBody bin){
		this.url = url;
		this.header = header;
		this.reqEntity = MultipartEntityBuilder.create().addPart("file", bin).build();
	}
	
	public HttpPostReq(String url, BasicHeader[] header, File file){
		this.url = url;
		this.header = header;
		this.reqEntity = MultipartEntityBuilder.create().addPart("file", new FileBody(file)).build();
	}
	
	public HttpPostReq(String url, BasicHeader[] header, File file, @SuppressWarnings("rawtypes") Map params){
		this.url = url;
		this.header = header;
		
		MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create().addPart("file", new FileBody(file));
		
		if(params != null && !params.isEmpty()){
			for(Object key : params.keySet()){
				multipartEntity.addPart((String) key, new StringBody((String) params.get(key),ContentType.APPLICATION_JSON));
			}
		}
		this.reqEntity = multipartEntity.build();
	}
	
	public HttpPostReq(String url, BasicHeader[] header, List<BasicNameValuePair> params, int socketTimeout,  int connectTimeout){
		this.url = url;
		this.header = header;
		this.params = params;
		this.socketTimeout = socketTimeout;
		this.connectTimeout = connectTimeout;
	}
	
	public HttpPostReq(String url, BasicHeader[] header, HttpEntity reqEntity, int socketTimeout,  int connectTimeout){
		this.url = url;
		this.header = header;
		this.reqEntity = reqEntity;
		this.socketTimeout = socketTimeout;
		this.connectTimeout = connectTimeout;
	}

	@Override
	public HttpRequestBase getHttpMethod() throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		if(params != null && !params.isEmpty()){
			httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
		}
		
		if(reqEntity != null){
			httpPost.setEntity(reqEntity);
		}
		httpPost.setHeaders(header);
		return httpPost;
	}

	@Override
	public RequestConfig getRequestConfig() {
		
		Builder builder = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout);
		
		if (isProxy()) {
			builder.setProxy(new HttpHost(getHostName(), getPort()));
		}
		
		return builder.build();
	}
	
}
