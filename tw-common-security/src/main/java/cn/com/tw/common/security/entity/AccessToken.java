package cn.com.tw.common.security.entity;
public class AccessToken { 
	
	/**
	 * accessToken
	 */
    private String accessToken;
    
    /**
     * tokenType
     */
    private String tokenType;
    
    /**
     * token刷新
     */
    private String refreshToken;
    
    /**
     * 过期时间
     */
    private long expiresIn;
    
    public AccessToken(String accessToken, String refreshToken, long expiresIn){
    	this.accessToken = accessToken;
    	this.refreshToken = refreshToken;
    	this.expiresIn = expiresIn;
    	this.tokenType = "bearer";
    }

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "AccessToken [accessToken=" + accessToken + ", tokenType="
				+ tokenType + ", refreshToken=" + refreshToken + ", expiresIn="
				+ expiresIn + "]";
	} 
    
}