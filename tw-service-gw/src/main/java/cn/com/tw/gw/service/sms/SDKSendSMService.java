package cn.com.tw.gw.service.sms;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.common.utils.tools.cache.ehcache.EHCache;
import cn.com.tw.common.utils.tools.cache.ehcache.cons.CacheCons;
import cn.com.tw.gw.common.cons.GwCode;
import cn.com.tw.gw.common.sms.SMSNotify;
import cn.com.tw.gw.common.sms.utils.ValidateUtils;

@Service("sendSMService")
public class SDKSendSMService {
	
	@Value("${sms.repeat.nosend.seconds}")
	private int noSendTime;
	
	/**
	 * 用户手机号与手机短信内容
	 */
	public static final String SMS_CACHE_KEY = "user:phone:sms:";

	public static final Logger log = LoggerFactory.getLogger(SDKSendSMService.class);
	
	private EHCache ehCache = EHCache.build();

	/**
	 * 发送模板短信(通用)
	 * 
	 * @param phone
	 * @param templNum
	 * @param smsMessage
	 * @return
	 */
	public void sendTemplateSMS(String phone, String templNum, String message) {
		log.debug(SysCons.LOG_START + "send TemplateSMS, params phone = " + (phone == null ? "null" : phone)
				+ " templNum = " + (templNum == null ? "null" : templNum));
		// 校验手机号
		boolean isMobile = ValidateUtils.isMobile(phone);
		if (!isMobile) {
			log.debug(SysCons.LOG_ERROR + "send TemplateSMS is failure, mobile phone number wrong...");
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "Mobile phone number wrong...");
		}
		
		final String key = new StringBuffer(SMS_CACHE_KEY).append(phone).toString();
		//查询缓存
		String smsInfo = (String) ehCache.get(CacheCons.CACHE_NAME, key);
		if(!StringUtils.isEmpty(smsInfo)){
			//最近有相同的短信内容，则不发送
			if(smsInfo.equals(message)){
				log.warn(SysCons.LOG_ERROR + "send TemplateSMS repetitive ( in " + noSendTime + " s can not send), phone = " + phone + " message = "
						+ message);
				
				throw new BusinessException(GwCode.MSG_TIME_NO_SEND, "too high frequency( in " + noSendTime + " s can not send)!");
			}
		}
		
		String[] smsMessage=message.split(",");
		// 接收发送短信接口的结果
		Map<String, Object> map = SMSNotify.build().smsSend(phone, templNum, smsMessage);
		// 获取返回码和返回消息，返回
		String statusCode = (String) map.get("statusCode");
		if (!ResultCode.OPERATION_IS_SUCCESS.equals(statusCode)) {
			// 短信发送失败
			String statusMsg = (String) map.get("statusMsg");
			log.error(SysCons.LOG_ERROR + "send TemplateSMS is exception, result = " + map.toString());
			throw new BusinessException(statusCode, statusMsg);
		}
		
		//缓存5分钟
		ehCache.putLive(CacheCons.CACHE_NAME, key, message.toString(), noSendTime);
		
		log.debug(SysCons.LOG_END + "send TemplateSMS success, result  = " + map.toString());
		
	}
}
