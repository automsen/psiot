package cn.com.tw.common.utils.tools.http;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import cn.com.tw.common.utils.tools.http.config.HttpConfig;

/**
 * 
 * @author Administrator
 *
 */
public class HttpClientUtils {
	
	private static CloseableHttpClient httpclient;
	
	private static Object obj = new Object();
	
	/**
	 * 实现httpClient单例，
	 * @return
	 */
	public static CloseableHttpClient getHttpClient(){
		if(httpclient == null){
			synchronized (obj) {
				if(httpclient == null){
					PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
					httpclient = HttpClients.custom().setConnectionManager(cm).build();
				}
			}
		}
		
		return httpclient;
	}
	
	public static CloseableHttpClient getHttpClient(HttpConfig config){
		if(httpclient == null){
			synchronized (obj) {
				if(httpclient == null){
					PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
					cm.setMaxTotal(config.getPoolMaxTotal());
					cm.setDefaultMaxPerRoute(config.getMaxPerRoute());
					httpclient = HttpClients.custom().setConnectionManager(cm).build();
				}
			}
		}
		
		return httpclient;
	}
	
	/**
	 * 实现httpClient单例，保证线程安全
	 * @return
	 */
	public static CloseableHttpClient getHttpClient(BasicCookieStore cookieStore){
		if(httpclient == null){
			synchronized (obj) {
				if(httpclient == null){
					PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
					httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).setConnectionManager(cm).build();
				}
			}
		}
		
		return httpclient;
	}
	
	/**
	 * 实现httpClient单例，保证线程安全
	 * @return
	 */
	public static CloseableHttpClient getHttpClient(BasicCookieStore cookieStore, HttpConfig config){
		if(httpclient == null){
			synchronized (obj) {
				if(httpclient == null){
					PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
					cm.setMaxTotal(config.getPoolMaxTotal());
					cm.setDefaultMaxPerRoute(config.getMaxPerRoute());
					httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).setConnectionManager(cm).build();
				}
			}
		}
		
		return httpclient;
	}
	
}
