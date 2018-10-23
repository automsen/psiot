package cn.com.tw.paas.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.tw.common.core.jms.cons.PsMqCons;
import cn.com.tw.common.core.jms.kafka.KafkaMqService;
import cn.com.tw.common.enm.notify.NotifyBusTypeEm;
import cn.com.tw.common.enm.notify.NotifyLvlEm;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.notify.Notify;
import cn.com.tw.common.utils.tools.cache.ehcache.EHCache;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.auth.common.utils.RandomValidateCode;
import cn.com.tw.paas.auth.entity.User;
import cn.com.tw.paas.auth.service.SsUserService;


@Controller
public class ImageGenController {
	
	private Logger logger = LoggerFactory.getLogger(ImageGenController.class);
	
	@Autowired
	private SsUserService userService;
	
	@Autowired
	private KafkaMqService mqHandler;

	@RequestMapping(value = "/toImg")
	public String toImg() {

		return "image/image";
	}

	// 登录获取验证码
	@RequestMapping("/getSysManageLoginCode")
	@ResponseBody
	public String getSysManageLoginCode(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Set-Cookie", "name=value; HttpOnly");// 设置HttpOnly属性,防止Xss攻击
		response.setDateHeader("Expire", 0);
		RandomValidateCode randomValidateCode = new RandomValidateCode();
		try {
			randomValidateCode.getRandcode(request, response, "imagecode");// 输出图片方法
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// 验证码验证
	@RequestMapping(value = "/checkimagecode")
	@ResponseBody
	public Response<?> checkTcode(HttpServletRequest request, HttpServletResponse response) {
		String validateCode = request.getParameter("validateCode");
		String code = null;
		// 1:获取cookie里面的验证码信息
//		Cookie[] cookies = request.getCookies();
//		for (Cookie cookie : cookies) {
//			if ("imagecode".equals(cookie.getName())) {
//				code = cookie.getValue();
//				break;
//			}
//		}
		String sessionid = request.getSession().getId();
		// 1:获取session验证码的信息
		 String code1 = (String) request.getSession().getAttribute(sessionid+"imagecode");
		 logger.debug("### sessioncode:{},validateCode:{}",code1,validateCode);
		// 2:判断验证码是否正确
		if (!StringUtils.isEmpty(validateCode) && validateCode.toUpperCase().equals(code1.toUpperCase())) {
			return Response.ok();

		}
		return Response.retn(ResultCode.PARAM_VALID_ERROR);
	}
	
	//短信验证码
	@RequestMapping(value="/SMS/{userName}")
	@ResponseBody
	public Response<?> checkTSMSCode(@PathVariable String userName){
		logger.debug("get SMS code Start");
		//生成随机6位随机号
		/*for (int i = 0; i <= 100; i++){
		    int flag = new Random().nextInt(999999);
		    if (flag < 100000)
		    {
		        flag += 100000;
		    }
		}*/
		
		Response<?> res = userService.queryUserByName(userName);
		if(res == null){
			return Response.retn(ResultCode.USER_NO_LOGIN_ERROR, "用户名错误");
		}
		
		int flag = (int) ((Math.random()*9+1)*100000);
		//int flag = 123456;
		@SuppressWarnings("unchecked")
		Map<String, String> data = (Map<String, String>) res.getData();
		//塞入队列
		//"时波网络","SMS_140625141","{\"code\":\"{}\"}"
		String content = "{\"code\":\""+flag+"\"}";
		String phoneNum = data.get("mobile");
		
		//先从缓存中查询该验证码是否存在，存在就不重新走发送流程。
		EHCache cache = EHCache.build();
		User userOld = (User) cache.get("SMS_verification", phoneNum);
		if(userOld != null){
			return Response.retn(ResultCode.USER_NO_LOGIN_ERROR, "验证码未过期");
		}
		
		//塞入队列发短信
		String orgId = data.get("orgId");
		String contentStr = Notify.createAliSms(phoneNum, "SMS_140625141", content, "时波网络").setPaasBusData(NotifyBusTypeEm.login, null, null, null, orgId, NotifyLvlEm.HIGH)
		.toJsonStr();
		mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, contentStr);
		
		User user = new User();
		user.setPhone(phoneNum);
		user.setUserName(userName);
		user.setSMSCode(""+flag);
		cache.put("SMS_verification", phoneNum, user);
		return Response.ok();
	}
}