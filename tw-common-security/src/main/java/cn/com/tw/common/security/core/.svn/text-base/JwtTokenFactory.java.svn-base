package cn.com.tw.common.security.core;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.com.tw.common.security.entity.AccessToken;
import cn.com.tw.common.security.entity.AuthEnum;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;

@Component
public class JwtTokenFactory {
	
	public static final Logger logger = LoggerFactory.getLogger(JwtTokenFactory.class);

	@Value("${jwt.busp.expire_time:360}")
	private int jwtExpireTime;
	
	@Value("${jwt.busp.secret:reipwurhsdflj}")
	private String jwtSecret;
	
	@Value("${jwt.admin.expire_time:360}")
	private int adminJwtExpireTime;
	
	@Value("${jwt.admin.secret:ghkjslfjsldfhgslkd}")
	private String adminJwtSecret;
	
	@Value("${jwt.buss.expire_time:360}")
	private int busJwtExpireTime;
	
	@Value("${jwt.buss.secret:fsuiuosd12iuosdufopsdhsdhjlkjl}")
	private String busJwtSecret;
	
	@Value("${jwt.bussuser.expire_time:360}")
	private int busUserJwtExpireTime;
	
	@Value("${jwt.bussuser.secret:sdflkjsdlkjweoiupo123io123ljdoiupo}")
	private String busUserJwtSecret;
	
	@Value("${jwt.sadminp.expire_time:360}")
	private int sadminpUserJwtExpireTime;
	
	@Value("${jwt.sadminp.secret:sdfsdfsdwerwerjlsdfjli31238bvbvb8frskuoqweifdlkajsdasdf}")
	private String sadminpUserJwtSecret;
	
	@SuppressWarnings("unchecked")
	public JwtInfo parseJWT(String jsonWebToken, AuthEnum authEnum){  
        try{
        	String secret = null;
        	switch (authEnum) {
				case admin:
					secret = adminJwtSecret;
					break;
				case bus_p:
					secret = jwtSecret;
					break;
				case bus_s:
					secret = busJwtSecret;
					break;
				case buss_user:
					secret = busUserJwtSecret;
					break;
				case sadmin_p:
					secret = sadminpUserJwtSecret;
					break;
				default:
					return null;
        	}
        	
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(jsonWebToken).getBody();  
            JwtInfo jwtInfo = new JwtInfo();
            jwtInfo.setSubject(claims.getSubject());
            jwtInfo.setExtend((Map<String, Object>) claims.get("extend"));
            jwtInfo.setPermiss((String) claims.get("permiss"));
            jwtInfo.setRoles((String) claims.get("roles"));
            jwtInfo.setSubName((String) claims.get("subName"));
            return jwtInfo;  
        }catch(Exception ex){
        	logger.error("parseJWT,exception = {}", ex);
            return null;
        }  
    }
	
    /**
     * Factory method for issuing new JWT Tokens.
     * 
     * @param username
     * @param roles
     * @return
     */
    public AccessToken createAccessJwtToken(AuthEnum authEnum) {
    	
    	String secret = null;
    	int expireTime = 0;
    	switch (authEnum) {
			case admin:
				secret = adminJwtSecret;
				expireTime = adminJwtExpireTime;
				break;
			case bus_p:
				secret = jwtSecret;
				expireTime = jwtExpireTime;
				break;
			case bus_s:
				secret = busJwtSecret;
				expireTime = busJwtExpireTime;
				break;
			case buss_user:
				secret = busUserJwtSecret;
				expireTime = busUserJwtExpireTime;
				break;
			case sadmin_p:
				secret = sadminpUserJwtSecret;
				expireTime = sadminpUserJwtExpireTime;
				break;
			default:
				throw new IllegalArgumentException("Cannot create JWT Token without enum is no exists");
    	}
    	
    	JwtInfo jwtInfo = JwtLocal.getJwt();
    	
        if (jwtInfo == null) {
        	throw new IllegalArgumentException("Cannot create JWT Token without user info");
        }

       /* if (jwtInfo.getPermiss() == null || jwtInfo.getRoles() == null) {
        	throw new IllegalArgumentException("User doesn't have any privileges");
        } */

        Claims claims = Jwts.claims().setSubject(jwtInfo.getSubject());
        claims.put("scopes", "");
        claims.put("subName", jwtInfo.getSubName());
        claims.put("roles", jwtInfo.getRoles());
        claims.put("permiss", jwtInfo.getPermiss());
        claims.put("extend", jwtInfo.getExtend());
        DateTime currentTime = new DateTime();

        
        expireTime = jwtInfo.getExpireTime() == 0 ? expireTime : jwtInfo.getExpireTime();
        
        String token = Jwts.builder()
          .setClaims(claims)
          //.setIssuer("")
          .setIssuedAt(currentTime.toDate())
          .setExpiration(currentTime.plusMinutes(expireTime).toDate())
          .signWith(SignatureAlgorithm.HS512, secret)
          .compact();
        
        String refreshToken = Jwts.builder()
	        .setClaims(claims)
	        //.setIssuer("")
	        .setIssuedAt(currentTime.toDate())
	        .setExpiration(currentTime.plusMinutes(expireTime + 20).toDate())
	        .signWith(SignatureAlgorithm.HS512, secret)
	        .compact();

        return new AccessToken(token, refreshToken, expireTime);
    }
}