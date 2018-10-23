package cn.com.tw.saas.serv.controller.wechat.vo;

import java.io.Serializable;

/**
 * 登录用请求参数
 * 
 * @author admin
 *
 */
public class LoginReq implements Serializable {

	private static final long serialVersionUID = 6191460038974135712L;

	private String userName;

	private String password;

	private ReqToken token;

	public String getUserName() {
		return userName;
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

	public ReqToken getToken() {
		return token;
	}

	public void setToken(ReqToken token) {
		this.token = token;
	}

}
