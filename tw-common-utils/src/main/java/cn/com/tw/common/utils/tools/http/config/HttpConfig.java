package cn.com.tw.common.utils.tools.http.config;


public class HttpConfig {
	
	/**
	 * 连接池最大大小
	 */
	private int poolMaxTotal = 10;
	
	/**
	 * 连接主机最大并发数
	 */
	private int maxPerRoute = 5;

	public int getPoolMaxTotal() {
		return poolMaxTotal;
	}

	public void setPoolMaxTotal(int poolMaxTotal) {
		this.poolMaxTotal = poolMaxTotal;
	}

	public int getMaxPerRoute() {
		return maxPerRoute;
	}

	public void setMaxPerRoute(int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

	
}
