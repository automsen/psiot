package cn.com.tw.saas.serv.controller.wechat;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.common.utils.CookieUtil;
import cn.com.tw.saas.serv.common.utils.cons.RequestType;
import cn.com.tw.saas.serv.controller.wechat.vo.LoginReq;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.service.cust.CustomerService;

@RestController
@RequestMapping("wechat")
public class LoginController {

	@Autowired
	private CustomerService customerService;

	/**
	 * 微信登录
	 *
	 * @param userName
	 * @param password
	 * @param response
	 * @return
	 */
	@PostMapping("login")
	public Response<?> login(@RequestBody LoginReq loginReq, HttpServletResponse response, HttpServletRequest request) {
//		Response<?> result = null;
		String openId = null;
		Customer customer = new Customer();
		customer.setCustomerMobile1(loginReq.getUserName());
		String reqType = request.getHeader("reqType");
		try {
			// 判断客户信息是否存在
			List<Customer> custs = customerService.selectByEntity(customer);
			if (custs == null || custs.size() == 0) {
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "账号或密码错误！");
			}
			customer = custs.get(0);
			if (!customer.getCustomerPassword().equals(DigestUtils.md5Hex(loginReq.getPassword().getBytes()))) {
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "账号或密码错误！");
			}
		} catch (Exception e) {
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统错误！");
		}
		String token = CommUtils.getUuid();

		// 如果是网页。或者微信公众号。 将token和openid写入cookie
		if (!StringUtils.isEmpty(reqType) && reqType.equals(RequestType.wxReq.getValue())) {
			openId = customer.getOpenId();
			if (!StringUtils.isEmpty(openId)) {
				CookieUtil.addCookie(response, "wxOpenId", openId, 0);
			}
			CookieUtil.addCookie(response, "token_nht", token, 0);
		} else if (!StringUtils.isEmpty(reqType) && reqType.equals(RequestType.appReq.getValue())) {
//			result.setMessage(token);  
		}
		return Response.ok(customer);
	}
	
	
	@PostMapping("changePassword")
	public Response<?> login(@RequestBody Map<String,String> requestMap ) {
		JwtInfo info = JwtLocal.getJwt();
		if(info == null ){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "登陆信息失效！");
		}
		String customerId = info.getSubject();
		String oldPassword = requestMap.get("oldPass");
		String newPassword = requestMap.get("newPass");
		if(StringUtils.isEmpty(oldPassword)|| StringUtils.isEmpty(newPassword) ){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "密码不能为空！");
		}
		try {
			/**
			 *  密码在前端已经MD5加密了。直接判断旧密码是否正确
			 */
			Customer customer = customerService.selectById(customerId);
			if(!oldPassword.equals(customer.getCustomerPassword())){
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "旧密码错误！");
			}
			// 修改密码
			customer = new Customer();
			customer.setCustomerId(customerId);
			customer.setCustomerPassword(newPassword);
			customerService.updateCustPass(customer);
		} catch (Exception e) {
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常！");
		}
		return Response.ok();
	}
}
