package cn.com.tw.saas.serv.controller.wechat;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.saas.serv.common.utils.CookieUtil;
import cn.com.tw.saas.serv.common.utils.cons.SysCommons;
import cn.com.tw.saas.serv.controller.wechat.thirdAPI.WechatPayService;
import cn.com.tw.saas.serv.entity.org.OrgPayConfig;
import cn.com.tw.saas.serv.service.org.OrgPayConfigService;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.support.ExpireKey;
import weixin.popular.support.expirekey.DefaultExpireKey;

@Controller
@RequestMapping("callback")
public class AuthController {
	
	
	private String appId;

	private String appSecret;
	@Autowired
	private OrgPayConfigService orgPayConfigService;


	@Autowired
	private WechatPayService wXPayService;

	// 重复通知过滤
	private volatile static ExpireKey expireKey = new DefaultExpireKey();
	
	private static Logger logger = LoggerFactory.getLogger(AuthController.class);

	/**
	 * 微信授权后重定向
	 */
	@GetMapping("redirectIn/{customerId}")
	public String redirectIn(@PathVariable("customerId") String customerId, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			initPayConfig();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// EHCache ehCache = EHCache.build();
		String code = request.getParameter("code");
		String url = request.getParameter("url");
		logger.debug("auth2.0 start.code:{}", code);
		String openId = "";
		try {
			SnsToken token = SnsAPI.oauth2AccessToken(appId, appSecret, code);
			logger.debug("auth2.0 openID:{}", token.getOpenid());
			// JSONObject customer = (JSONObject)
			// ehCache.get(ServiceConfig.CACHE_USE, customerNo);
			// customer.put("openId", token.getOpenid());
			// ehCache.putLive(ServiceConfig.CACHE_USE,
			// customerNo,customer,1000*60*2);
			CookieUtil.addCookie(response, "wxOpenId", token.getOpenid(), 0);
			openId = token.getOpenid();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:" + url + "&openId=" + openId + "&customerId=" + customerId;
	}
	
	
	private void initPayConfig() throws Exception{
		JwtInfo jwt = JwtLocal.getJwt();
		if(jwt == null ){
			throw new Exception();
		}
		String orgId = (String) jwt.getExt("orgId");
		OrgPayConfig config = new OrgPayConfig();
		config.setConfigStatus(new Byte("0"));
		config.setPayType(SysCommons.payType.Wechat_Pay);
		config.setPayOrgId(orgId);
		
		List<OrgPayConfig> tempConfigs = orgPayConfigService.selectByEntity(config);
		if(tempConfigs == null || tempConfigs.size() ==0 ){
			throw new Exception();
		}
		config = tempConfigs.get(0);
		if(StringUtils.isEmpty(config.getPayConfigJson())){
			throw new Exception();
		}
		JSONObject wechatConfig = JSONObject.parseObject(config.getPayConfigJson());
		String appId = wechatConfig.getString("appId");
		String secret = wechatConfig.getString("secret");
		
		if(  StringUtils.isEmpty(appId)|| StringUtils.isEmpty(secret)){
			throw new Exception();
		}
		setAppId(appId);
		setAppSecret(secret);
	}
	

	/**
	 * 支付回调
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("rechargeCallback")
	public void rechargeCallback(HttpServletRequest request, HttpServletResponse response) {
		wXPayService.rechargeCallback(request, response, expireKey);
	}


	public String getAppId() {
		return appId;
	}


	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String getAppSecret() {
		return appSecret;
	}


	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	
}
