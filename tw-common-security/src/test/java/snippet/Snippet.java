package snippet;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import cn.com.tw.common.security.entity.JwtInfo;

public class Snippet {
	public static void main(String[] args) {
			String aa = "eyJhbGciOiJIUzUxMiJ9.eyJzY29wZXMiOiIiLCJzdWJOYW1lIjoiMTU5NDU2MTE2NTMiLCJyb2xlcyI6ImFsbCIsInBlcm1pc3MiOiJhbGwiLCJleHRlbmQiOnsib3JnTmFtZSI6bnVsbCwib3JpZ2luIjpudWxsLCJvcmdJZCI6IjIifSwiaWF0IjoxNTI3MTYxMDk0LCJleHAiOjE1MjcxNzE4OTR9.Ge9yg_ZXFnDekm_q-Z1XULisOdVoyJwOkdiGgG2qKcTp3d661v6NoLBa6DV8zpUofSho-k2lQSVq5w5JZiEm1Q";
	    	
			Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("sdflkjsdlkjweoiupo123io123ljdoiupo")).parseClaimsJws(aa).getBody();  
	        JwtInfo jwtInfo = new JwtInfo();
	        jwtInfo.setSubject(claims.getSubject());
	        jwtInfo.setExtend((Map<String, Object>) claims.get("extend"));
	        jwtInfo.setPermiss((String) claims.get("permiss"));
	        jwtInfo.setRoles((String) claims.get("roles"));
	        jwtInfo.setSubName((String) claims.get("subName"));
		}
}

