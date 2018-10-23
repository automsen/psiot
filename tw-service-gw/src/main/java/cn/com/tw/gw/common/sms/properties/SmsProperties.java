package cn.com.tw.gw.common.sms.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component()
@ConfigurationProperties(prefix="sms")
public class SmsProperties {
	
	private String serviceUrl;
	
	private String servicePort;
	
	private String accountSid;
	
	private String accountToken;
	
	private String appid;

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getServicePort() {
		return servicePort;
	}

	public void setServicePort(String servicePort) {
		this.servicePort = servicePort;
	}

	public String getAccountSid() {
		return accountSid;
	}

	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}

	public String getAccountToken() {
		return accountToken;
	}

	public void setAccountToken(String accountToken) {
		this.accountToken = accountToken;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	
	/*sms.service.url=sandboxapp.cloopen.com
			sms.service.port=8883
			sms.account.sid=8aaf07085a25bd87015a2740c5e60090
			sms.account.token=89be0cebfb9646d9b1c744447e3403fa
			sms.appid=8aaf07085a25bd87015a2740c7810096*/
}
