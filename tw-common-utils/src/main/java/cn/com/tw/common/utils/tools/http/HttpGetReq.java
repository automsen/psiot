package cn.com.tw.common.utils.tools.http;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicHeader;

public class HttpGetReq extends AbstractHttpRequest{

	// 请求URL地址
	private String url;

	// 消息头设置
	private BasicHeader[] header;
	
	/**
	 * 连接超时等待时间
	 */
	private int connectTimeout = 2000;
	
	/**
	 * socket连接等待时间
	 */
	private int socketTimeout = 2000;

	public HttpGetReq(String url) {
		this.url = url;
	}

	public HttpGetReq(String url, BasicHeader[] header) {
		this.url = url;
		this.header = header;
	}
	
	public HttpGetReq(String url, int socketTimeout,  int connectTimeout) {
		this.url = url;
		this.socketTimeout = socketTimeout;
		this.connectTimeout = connectTimeout;
	}

	public HttpGetReq(String url, BasicHeader[] header, int socketTimeout,  int connectTimeout) {
		this.url = url;
		this.header = header;
		this.socketTimeout = socketTimeout;
		this.connectTimeout = connectTimeout;
	}

	@Override
	public HttpRequestBase getHttpMethod() throws UnsupportedEncodingException {
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeaders(header);
		return httpGet;
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
