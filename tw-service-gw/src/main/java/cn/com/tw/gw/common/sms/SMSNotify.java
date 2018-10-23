package cn.com.tw.gw.common.sms;

import java.util.Map;

import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.gw.common.sms.properties.SmsProperties;

import com.cloopen.rest.sdk.CCPRestSmsSDK;

/**
 * 
 * @author admin
 *
 */
public class SMSNotify {
	
	private CCPRestSmsSDK restSms = null;
	
	private SMSNotify(){
		this.restSms = new CCPRestSmsSDK();
		
		SmsProperties smsProperties = (SmsProperties) SpringContext.getBean("smsProperties");
		restSms.init(smsProperties.getServiceUrl(), smsProperties.getServicePort());
		restSms.setAccount(smsProperties.getAccountSid(), smsProperties.getAccountToken());
		restSms.setAppId(smsProperties.getAppid());
	}
	
	private static class SMSNotifyHolder {
		private static SMSNotify notify = null;
		static{
			notify = new SMSNotify();
		}
		
		public static SMSNotify getInstance(){
			return notify;
		}
	}
	
	public static SMSNotify build(){
		return SMSNotifyHolder.getInstance();
	}
	
	public Map<String, Object> smsSend(String phone, String templNum, String[] smsMessage){
		return restSms.sendTemplateSMS(phone, templNum , smsMessage);
	}
}
