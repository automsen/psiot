package cn.com.tw.common.security.entity;

import java.util.HashMap;
import java.util.Map;

public class JwtInfo {
	
	/**
	 * userId clientId
	 */
	private String subject;
	
	private String subName;
	
	private String roles;
	
	private String permiss;
	
	private int expireTime;
	
	private String origin;
	
	private Map<String, Object> extend;

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getPermiss() {
		return permiss;
	}

	public void setPermiss(String permiss) {
		this.permiss = permiss;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setExtend(String key, Object value) {
		
		if (extend == null) {
			extend = new HashMap<String, Object>();
		}
		
		extend.put(key, value);
	}

	@Override
	public String toString() {
		return "JwtInfo [subject=" + subject + ", subName=" + subName
				+ ", roles=" + roles + ", permiss=" + permiss + ", expireTime="
				+ expireTime + ", origin=" + origin + ", extend=" + extend
				+ "]";
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}
	
	public Object getExt(String name) {
		if (extend == null) {
			return null;
		}
		return extend.get(name);
	}

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
}
