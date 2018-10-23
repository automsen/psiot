package cn.com.tw.engine.core.handler.http.lorawarn.param;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="engine.lorawarn")
public class LoRaWanRequestParams {

	private String token;
	
	private String appeui;
	
	private String ttl;
	
	private String payloadType;
	
	private String serviceUrl;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAppeui() {
		return appeui;
	}

	public void setAppeui(String appeui) {
		this.appeui = appeui;
	}

	@Override
	public String toString() {
		return "LoRaWanRequestParams [token=" + token + ", appeui=" + appeui
				+ ", ttl=" + ttl + ", payloadType=" + payloadType
				+ ", serviceUrl=" + serviceUrl + "]";
	}

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public String getPayloadType() {
		return payloadType;
	}

	public void setPayloadType(String payloadType) {
		this.payloadType = payloadType;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
}
