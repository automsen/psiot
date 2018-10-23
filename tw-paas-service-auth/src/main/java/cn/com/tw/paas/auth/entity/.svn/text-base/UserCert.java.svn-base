package cn.com.tw.paas.auth.entity;

public class UserCert extends Client{
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 授权类型 password  authorization_code， client_credentials
	 */
	private String grantType = "password";
	
	/**
	 * 针对authorization_code授权模式才有用
	 */
	private String code;
	
	/**
	 * 指定授权类型  
	 */
	private String scope;
	
	/**
	 * 来源
	 */
	private String origin;
	
	/**
	 * SMS验证码
	 */
	private String smsCode;
	
	public String getUserName() {
		return userName;
	}

	@Override
	public String toString() {
		return "UserCert [userName=" + userName + ", password=" + password
				+ ", grantType=" + grantType + ", code=" + code + ", scope="
				+ scope + ", origin=" + origin + ", smsCode=" + smsCode + "]";
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

}
