package cn.com.tw.common.security.utils;

import cn.com.tw.common.security.entity.JwtInfo;

public class JwtLocal {
	
	private static ThreadLocal<JwtInfo> JwtInfoLocal = new ThreadLocal<JwtInfo>();
	
	public static void setJwt(JwtInfo obj){
		JwtInfoLocal.set(obj);
	}
	
	public static JwtInfo getJwt(){
		return JwtInfoLocal.get();
	}

}
