package cn.com.tw.paas.auth.entity;

import java.util.Date;

public class User {
	
	private String userId;
	
	private String userName;
	
	private String password;
	
	private String roles;
	
	private String permisses;
	
	
	 /**
     * 注册手机号
     */
    private String phone;

    /**
     * 注册邮箱
     */
    private String email;

    /**
     * 关联qq
     */
    private String qq;

    /**
     * 关联微信
     */
    private String wechat;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 是否可用 1.是  0.否
     */
    private Byte isUsable;
    
    /**
     * 机构名称
     */
    private String orgName;
    
    
    private String orgLevel;

    /**
   	 * 创建时间
   	 */
    private Date createTime;
	
    
    private String orgId;
    
    /**
     * 机构官网
     */
    private String orgWebsite;
    
    /**
     * 机构logo
     */
    private String orgLogo;
    
	/**
	 * SMS Code
	 */
    private String SMSCode;
    
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Byte getIsUsable() {
		return isUsable;
	}

	public void setIsUsable(Byte isUsable) {
		this.isUsable = isUsable;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName
				+ ", password=" + password + ", roles=" + roles
				+ ", permisses=" + permisses + ", phone=" + phone + ", email="
				+ email + ", qq=" + qq + ", wechat=" + wechat + ", realName="
				+ realName + ", isUsable=" + isUsable + ", orgName=" + orgName
				+ ", orgLevel=" + orgLevel + ", createTime=" + createTime
				+ ", orgId=" + orgId + ", orgWebsite=" + orgWebsite
				+ ", orgLogo=" + orgLogo + "]";
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getPermisses() {
		return permisses;
	}

	public void setPermisses(String permisses) {
		this.permisses = permisses;
	}

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



	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgWebsite() {
		return orgWebsite;
	}

	public void setOrgWebsite(String orgWebsite) {
		this.orgWebsite = orgWebsite;
	}

	public String getOrgLogo() {
		return orgLogo;
	}

	public void setOrgLogo(String orgLogo) {
		this.orgLogo = orgLogo;
	}

	public String getSMSCode() {
		return SMSCode;
	}

	public void setSMSCode(String sMSCode) {
		SMSCode = sMSCode;
	}

}
