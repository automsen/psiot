package cn.com.tw.paas.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
import cn.com.tw.paas.auth.entity.UserCert;
import cn.com.tw.paas.auth.service.SAdminUserService;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("sadmin")
public class SAdminController {

	
	@Autowired
	private JwtTokenFactory jwtFactory;
	
	@Autowired
	private SAdminUserService sadminUserService;
	
	@PostMapping("token")
	public Response<?> authToken(@RequestBody UserCert cert){
		
		String grantType = cert.getGrantType();
		
		GrantTypeEnum type = Enum.valueOf(GrantTypeEnum.class, grantType);
		JSONObject resultObj = new JSONObject();
		
		switch (type) {
			//密码模式，使用于用户网页登录，app登录，用户将自身的userName和password交由client，client将使用它们来申请access token，整个过程会将用户信息暴露。因此，除非client十分可靠（例如网页登录，APP登录）
			case password:
				
				Response<?> res = sadminUserService.queryUserByName(cert.getUserName());
				
				if(!res.getStatusCode().equals("000000")){
					return Response.retn(ResultCode.USER_NO_LOGIN_ERROR, "用户名密码错误");
				}
				
				@SuppressWarnings("unchecked")
				Map<String, Object> data = (Map<String, Object>) res.getData();
				
				if (data == null) {
					throw new BusinessException(ResultCode.USER_NO_LOGIN_ERROR, "用户名不存在");
				}
				
				//User user = JsonUtils.jsonToPojo(JsonUtils.objectToJson(res.getData()), User.class);
				String password = (String) data.get("password");
				String userName = (String) data.get("userName");
				String userId = (String) data.get("userId");
				String md5Password = DigestUtils.md5Hex(cert.getPassword().getBytes());
				if (!userName.equals(cert.getUserName()) || !password.equals(md5Password)){
					return Response.retn(ResultCode.USER_NO_LOGIN_ERROR, "用户名密码错误");
				}
				JwtInfo info = new JwtInfo();
				info.setSubject(userId);
				info.setSubName(userName);
				//写入 权限和角色    暂时为空
				info.setPermiss((String)data.get("permisses"));
				info.setRoles((String)data.get("roles"));
				JwtLocal.setJwt(info);
				//拼装accessToken
				AccessToken token = jwtFactory.createAccessJwtToken(AuthEnum.sadmin_p);
				resultObj.put("token", token);
				resultObj.put("user", info);
	            //返回accessToken  
				return Response.ok(resultObj);
			//Client模式，这种模式不常用，该模式下，并不存在对个体用户授权的行为，被授权的主体为client。因此，该模式可用于对某类用户进行集体授权
			case client_credentials:
				
				return null;
			//授权码模式，最常用最严谨的方式，适合第三方应用授权实现access_token调用接口访问	
			case authorization_code:
				
				return null;
			
		}
		
		return null;
	}
	
	@GetMapping("refresh")
	public Response<?> refresh(String reToken){
		JwtInfo info = jwtFactory.parseJWT(reToken, AuthEnum.sadmin_p);
		
		if (info == null) {
			return Response.retn("010998", "Token 刷新失效");
		}
		
		JwtLocal.setJwt(info);
		//拼装accessToken
		AccessToken token = jwtFactory.createAccessJwtToken(AuthEnum.sadmin_p);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("accessToken", token.getAccessToken());
		resultMap.put("refreshToken", token.getRefreshToken());
		return Response.ok(resultMap);
	}

}
