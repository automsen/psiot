package cn.com.tw.gw.service.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.common.utils.tools.cache.ehcache.EHCache;
import cn.com.tw.common.utils.tools.cache.ehcache.cons.CacheCons;
import cn.com.tw.gw.common.cons.GwCode;
import cn.com.tw.gw.common.sms.utils.ValidateUtils;

@Service
public class AliSmsService {

	public static final Logger logger = LoggerFactory.getLogger(AliSmsService.class);
	public static final String SMS_CACHE_KEY = "user:phone:sms:";
	private EHCache ehCache = EHCache.build();
	// 产品名称:云通信短信API产品,开发者无需替换
	static final String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	static final String domain = "dysmsapi.aliyuncs.com";

	// TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
	@Value("${alisms.accessKeyId}")
	private String accessKeyId;
	@Value("${alisms.accessKeySecret}")
	private String accessKeySecret;
	
	// 验证码短信缓存时间，用于限制同一号码短时间多次获取验证码
	@Value("${sms.verify.cache.seconds}")
	private int verifyCacheSeconds;
	// 通知短信缓存时间，用于防止发送相同内容重复通知
	@Value("${sms.notice.cache.seconds}")
	private int noticeCacheSeconds;
	
	// 验证码限制次数(阿里云设置，不可变更)(未使用)
	// 每分钟限制
	private int verifyLimitPerMin = 1;
	// 每小时限制
	private int verifyLimitPerHour = 5;
	// 近24小时限制
	private int verifyLimitPer24h = 10;

	/**
	 * 发送验证码短信
	 * @param phone
	 * @param signature
	 * @param templNum
	 * @param message
	 */
	public void sendVerifySMS(String phone, String signature, String templNum, String message) {
		logger.debug(">>>>>>>> send ali verify SMS start, params phone = " + (phone == null ? "null" : phone)
				+ " templNum = " + (templNum == null ? "null" : templNum));
		// 校验手机号
		boolean isMobile = ValidateUtils.isMobile(phone);
		if (!isMobile) {
			logger.debug("xxxxxxxx send ali verify SMS stop: mobile phone number wrong!");
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "Mobile phone number wrong!");
		}

		final String key = new StringBuffer(SMS_CACHE_KEY).append(phone).toString();
		// 查询缓存
		String smsInfo = (String) ehCache.get(CacheCons.CACHE_NAME, key);
		if (!StringUtils.isEmpty(smsInfo)) {
			// 最近有相同手机号的验证码，则不发送
			if (smsInfo.equals(message)) {
				logger.warn("xxxxxxxx send ali verify SMS stop: send time over limit, phone = " + phone + " message = "
						+ message);
				throw new BusinessException(GwCode.MSG_TIME_NO_SEND, "send time over limit!");
			}
		}
		// 接收发送短信接口的结果
		SendSmsResponse res;
		try {
			// 调用api发送
			res = sendSms(phone, signature, templNum, message);
			// 返回码
			String statusCode = res.getCode();
			if (res.getCode() != null && res.getCode().equals("OK")) {
				// 请求成功
				// 缓存60秒
				ehCache.putLive(CacheCons.CACHE_NAME, key, message.toString(), verifyCacheSeconds);
				logger.debug(SysCons.LOG_END + "send ali verify SMS success, code  = " + statusCode);
			} else {
				// 短信发送失败
				String statusMsg = res.getMessage();
				logger.error("xxxxxxxx send ali verify SMS failure: code = " + statusCode + ",msg = " + statusMsg);
				throw new BusinessException(statusCode, statusMsg);
			}
		} catch (ClientException e) {
			logger.error("xxxxxxxx send ali verify SMS failure: ClientException code = " + e.getErrCode() + ",msg = " + e.getErrMsg());
			throw new BusinessException(e.getErrCode(), e.getErrMsg());
		}
	}
	
	/**
	 * 发送通知短信
	 * @param phone
	 * @param signature
	 * @param templNum
	 * @param message
	 */
	public void sendNoticeSMS(String phone, String signature, String templNum, String message) {
		logger.debug(">>>>>>>> send ali notice SMS start, params phone = " + (phone == null ? "null" : phone)
				+ " templNum = " + (templNum == null ? "null" : templNum));
		// 校验手机号
		boolean isMobile = ValidateUtils.isMobile(phone);
		if (!isMobile) {
			logger.debug("xxxxxxxx send ali notice SMS stop: mobile phone number wrong!");
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "Mobile phone number wrong!");
		}

		final String key = new StringBuffer(SMS_CACHE_KEY).append(phone).toString();
		// 查询缓存
		String smsInfo = (String) ehCache.get(CacheCons.CACHE_NAME, key);
		if (!StringUtils.isEmpty(smsInfo)&&smsInfo.equals(message)) {
			// 最近有相同内容的通知，则不发送
			if (smsInfo.equals(message)) {
				logger.warn("xxxxxxxx send ali notice SMS stop: send same notice, phone = " + phone + " message = "
						+ message);
				throw new BusinessException(GwCode.MSG_TIME_NO_SEND, "send same notice!");
			}
		}
		// 接收发送短信接口的结果
		SendSmsResponse res;
		try {
			// 调用api发送
			res = sendSms(phone, signature, templNum, message);
			// 返回码
			String statusCode = res.getCode();
			if (res.getCode() != null && res.getCode().equals("OK")) {
				// 请求成功
				// 缓存60秒
				ehCache.putLive(CacheCons.CACHE_NAME, key, message.toString(), noticeCacheSeconds);
				logger.debug(SysCons.LOG_END + "send ali notice SMS success, code  = " + statusCode);
			} else {
				// 短信发送失败
				String statusMsg = res.getMessage();
				logger.error("xxxxxxxx send ali notice SMS failure: code = " + statusCode + ",msg = " + statusMsg);
				throw new BusinessException(statusCode, statusMsg);
			}
		} catch (ClientException e) {
			logger.error("xxxxxxxx send ali notice SMS failure: ClientException code = " + e.getErrCode() + ",msg = " + e.getErrMsg());
			throw new BusinessException(e.getErrCode(), e.getErrMsg());
		}
	}

	/**
	 * 调用阿里api部分
	 * 
	 * @param phone
	 * @param signature
	 * @param templNum
	 * @param message
	 * @return
	 * @throws ClientException
	 */
	private SendSmsResponse sendSms(String phone, String signature, String templNum, String message)
			throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(phone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(signature);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templNum);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam(message);

		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("yourOutId");

		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

		return sendSmsResponse;
	}
}