package cn.com.tw.paas.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.security.core.JwtTokenFactory;
import cn.com.tw.common.security.entity.AccessToken;
import cn.com.tw.common.security.entity.AuthEnum;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.auth.common.enm.GrantTypeEnum;
import cn.com.tw.paas.auth.entity.User;
import cn.com.tw.paas.auth.entity.UserCert;
import cn.com.tw.paas.auth.service.SsCustUserService;

import com.alibaba.fastjson.JSONObject;

/**
 * 客户端授权实现
 * @author admin
 *
 */
@RestController
@RequestMapping("bususer/auth")
public class BussUserController {

	@Autowired
	private SsCustUserService userService;
	
	@Autowired
	private JwtTokenFactory jwtFactory;
	
	/**
	 * 返回授权码
	 * @param clientId 必填
	 * @param reponseType 默认为 code
	 * @param redirectUrl 回调URL
	 */
	@GetMapping("authorize")
	public void authorize(@RequestParam String clientId, @RequestParam String reponseType, @RequestParam String redirectUrl, @RequestParam(required = false) String state){
		//验证clientId 是否存在
		//生成redirectUrl?code=xxxxxx，注意code存放超时时间
		//重定向上面生成的URL
	}

	/**
	 * 返回token
	 * @param userName
	 * @param password
	 * @param grantType
	 */
	@PostMapping("token")
	public Response<?> token(@RequestBody UserCert cert){
		JSONObject tokenJson = authToken(cert);
		return Response.ok(tokenJson);
	}
	
	/**
	 * 刷新
	 * @param token
	 * @return
	 */
	@GetMapping("refresh")
	public Response<?> refresh(String reToken){
		JwtInfo info = jwtFactory.parseJWT(reToken, AuthEnum.buss_user);
		
		if (info == null) {
			return Response.retn(ResultCode.USER_NO_LOGIN_ERROR, "Token 过期或非法");
		}
		
		JwtLocal.setJwt(info);
		//拼装accessToken
		AccessToken token = jwtFactory.createAccessJwtToken(AuthEnum.buss_user);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("accessToken", token.getAccessToken());
		resultMap.put("refreshToken", token.getRefreshToken());
		return Response.ok(resultMap);
	}
	
	@RequestMapping("destroy")
	public Response<?> destroy(String token){
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject authToken(UserCert cert) {
		
		String grantType = cert.getGrantType();
		
		GrantTypeEnum type = Enum.valueOf(GrantTypeEnum.class, grantType);
		
		JSONObject resultObj = new JSONObject();
		switch (type) {
			//密码模式，使用于用户网页登录，app登录，用户将自身的userName和password交由client，client将使用它们来申请access token，整个过程会将用户信息暴露。因此，除非client十分可靠（例如网页登录，APP登录）
			case password:
				
				Response<?> res = userService.queryUserByName(cert.getUserName());
				
				if(!res.getStatusCode().equals("000000")){
					throw new BusinessException(ResultCode.USER_NO_LOGIN_ERROR, "用户名密码错误");
				}
				
				Map<String, String> data = (Map<String, String>) res.getData();
				
				if (data == null) {
					throw new BusinessException(ResultCode.USER_NO_LOGIN_ERROR, "用户名不存在");
				}
				
				String userName = data.get("customerMobile1");
				String password = data.get("customerPassword");
				String orgId = data.get("orgId");
				String orgName = data.get("orgName");
				String appId = data.get("appId");
				String md5Password = DigestUtils.md5Hex(cert.getPassword().getBytes());
				if (!userName.equals(cert.getUserName()) || !password.equals(md5Password)){
					throw new BusinessException(ResultCode.USER_NO_LOGIN_ERROR, "用户名密码错误");
				}
				
				User user = new User();
				user.setUserId(data.get("customerId"));
				user.setUserName(userName);
				
				JwtInfo info = new JwtInfo();
				info.setSubject(user.getUserId());
				info.setSubName(user.getUserName());
				info.setExtend("orgId", orgId);
				info.setExtend("orgName", orgName);
				if(!StringUtils.isEmpty(appId)){
					info.setExtend("appId", appId);
				}
				/*try {
					info.setExtend("orgName", URLEncoder.encode(orgName, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				info.setExtend("origin", cert.getOrigin());
				//写入 权限和角色    暂时为空
				info.setPermiss("all");
				info.setRoles("all");
				
				if ("client".equals(cert.getOrigin())) {
					//一个星期
					info.setExpireTime(24 * 60 * 7);
				}
				
				JwtLocal.setJwt(info);
				//拼装accessToken
				AccessToken token = jwtFactory.createAccessJwtToken(AuthEnum.buss_user);
				resultObj.put("token", token);
				resultObj.put("user", info);
				return resultObj;
			
			//Client模式，这种模式不常用，该模式下，并不存在对个体用户授权的行为，被授权的主体为client。因此，该模式可用于对某类用户进行集体授权
			case client_credentials:
				
				return null;
			//授权码模式，最常用最严谨的方式，适合第三方应用授权实现access_token调用接口访问	
			case authorization_code:
			
				return null;
		}
		
		return null;
	}
}
