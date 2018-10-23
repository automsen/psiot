package cn.com.tw.paas.auth.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import cn.com.tw.common.utils.tools.cache.ehcache.EHCache;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.auth.common.enm.GrantTypeEnum;
import cn.com.tw.paas.auth.entity.User;
import cn.com.tw.paas.auth.entity.UserCert;
import cn.com.tw.paas.auth.service.SsUserService;

import com.alibaba.fastjson.JSONObject;

/**
 * 客户端授权实现
 * @author admin
 *
 */
@RestController
@RequestMapping("bus/auth")
public class BussController {

	@Autowired
	private SsUserService userService;
	
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
	public Response<?> token(@RequestBody UserCert cert){// 用户信息， 是否通过
		JSONObject tokenJson = authToken(cert, 1);
		return Response.ok(tokenJson);
	}
	
	/**
	 * 刷新
	 * @param token
	 * @return
	 */
	@GetMapping("refresh")
	public Response<?> refresh(String reToken){
		JwtInfo info = jwtFactory.parseJWT(reToken, AuthEnum.bus_s);
		
		if (info == null) {
			return Response.retn(ResultCode.USER_NO_LOGIN_ERROR, "Token 过期或非法");
		}
		
		JwtLocal.setJwt(info);
		//拼装accessToken
		AccessToken token = jwtFactory.createAccessJwtToken(AuthEnum.bus_s);
		
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
	private JSONObject authToken(UserCert cert, int isPass) {
		
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
				
				String userName = data.get("userName");
				String password = data.get("password");
				String orgId = data.get("orgId");
				String orgName = data.get("orgName");
				String phoneNum = data.get("mobile");
				String md5Password = DigestUtils.md5Hex(cert.getPassword().getBytes());
				if (!userName.equals(cert.getUserName()) || !password.equals(md5Password)){
					throw new BusinessException(ResultCode.USER_NO_LOGIN_ERROR, "用户名密码错误");
				}
				EHCache cache = EHCache.build();
				//缓存中更新本次登录时间
				Date dateNow = new Date();
				cache.put("last_login_time", phoneNum, dateNow);
				
				//从缓冲中取出对应信息，校验登录密码是否正确。
				if(isPass == 1){//需要通过验证
					User user = (User) cache.get("SMS_verification", phoneNum);
					if(user == null || !cert.getSmsCode().equals(user.getSMSCode())){
						throw new BusinessException(ResultCode.USER_NO_LOGIN_ERROR, "验证码错误");
					}
				}
				
				User user = new User();
				user.setUserId(data.get("userId"));
				user.setUserName(userName);
				JwtInfo info = new JwtInfo();
				info.setSubject(user.getUserId());
				info.setSubName(user.getUserName());
				info.setExtend("orgId", orgId);
				info.setExtend("orgName", orgName);
				info.setExtend("origin", cert.getOrigin());
				//写入 权限和角色    暂时为空
				info.setPermiss(data.get("permisses"));
				info.setRoles(data.get("roles"));
				info.setExtend("orgWebsite", data.get("orgWebsite"));
				info.setExtend("orgLogo", data.get("orgLogo"));
				if ("client".equals(cert.getOrigin())) {
					//一个星期
					info.setExpireTime(24 * 60 * 7);
				}
				
				JwtLocal.setJwt(info);
				//拼装accessToken
				AccessToken token = jwtFactory.createAccessJwtToken(AuthEnum.bus_s);
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/verification")
	public Response<?> SMSVerification(@RequestBody UserCert cert){
		//先做用户信息校验是否正确后再进行验证方式判断。
		Response<?> res = userService.queryUserByName(cert.getUserName());
		if(!res.getStatusCode().equals("000000")){
			throw new BusinessException(ResultCode.USER_NO_LOGIN_ERROR, "用户名密码错误");
		}
		Map<String, String> data = (Map<String, String>) res.getData();
		
		if (data == null) {
			throw new BusinessException(ResultCode.USER_NO_LOGIN_ERROR, "用户名不存在");
		}
		String userName = data.get("userName");
		String password = data.get("password");
		StringBuffer phoneNum = new StringBuffer(data.get("mobile"));
		String md5Password = DigestUtils.md5Hex(cert.getPassword().getBytes());
		String isVerification = String.valueOf(data.get("isVerification"));
		if (!userName.equals(cert.getUserName()) || !password.equals(md5Password)){
			throw new BusinessException(ResultCode.USER_NO_LOGIN_ERROR, "用户名密码错误");
		}
		//查看当前用户是否在有效期内
		EHCache cache = EHCache.build();
		Date date = (Date) cache.get("last_login_time", data.get("mobile"));
		//是否需要进一步验证（通过状态字判断）
		if("1".equals(isVerification) && date == null){//需要通过验证且不在有效期内
			String mobile = "";
			if(phoneNum != null && phoneNum.length() > 0){
				mobile = phoneNum.replace(3, 7, "****").toString();
			}else{
				throw new BusinessException(ResultCode.USER_NO_LOGIN_ERROR, "用户手机不存在");
			}
			if(null != cert.getSmsCode()){//校验验证码
				return Response.ok(authToken(cert, 1));
			}else{//需要验证没给验证码返回给前端弹窗
				return Response.retn("111111", "请填写验证码", mobile);
			}
		}else if("0".equals(isVerification) || date != null){//不需要验证
			//直接走登录流程
			return Response.ok(authToken(cert, 0));
		}else{//预留用于其他验证方式
			return null;
		}
	}
}
