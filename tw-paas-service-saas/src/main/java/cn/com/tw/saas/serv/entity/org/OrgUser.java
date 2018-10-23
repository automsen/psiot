package cn.com.tw.saas.serv.entity.org;

import java.util.Date;

public class OrgUser {
	
    private String userId;

    private String orgId;
    
    private String orgName;

	private String userName;

    private String userRole;

    private String password;

    /**
     * 手机号
     */
    private String mobile;

    private String email;

    private Byte isUsable;

    private Date createTime;

    private Date updateTime;
    
    /**
     * 机构角色ID
     */
    private String orgRoleId;
    
    /**
     * 用户真实名称
     */
    private String userRealName;
    
    /**
     * 管理角色
     */
    private String manage;
    
    /**
     * 服务角色
     */
    private String serve;
    
    /**
     * 运维角色
     */
    private String maint;
    
    /**
     * 角色名称
     */
    private String orgRoleName;
    
    private String roles;
    
    private String permisses;
    
    /**
     * 机构官网
     */
    private String orgWebsite;
    
    private String orgLogo;
    
    /**
     * 是否短信认证
     */
    private Byte isVerification;
    
	@Override
	public String toString() {
		return "OrgUser [userId=" + userId + ", orgId=" + orgId + ", orgName="
				+ orgName + ", userName=" + userName + ", userRole=" + userRole
				+ ", password=" + password + ", mobile=" + mobile + ", email="
				+ email + ", isUsable=" + isUsable + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", orgRoleId="
				+ orgRoleId + ", userRealName=" + userRealName + ", manage="
				+ manage + ", serve=" + serve + ", maint=" + maint
				+ ", orgRoleName=" + orgRoleName + ", roles=" + roles
				+ ", permisses=" + permisses + "]";
	}

	public String getPermisses() {
		return permisses;
	}

	public void setPermisses(String permisses) {
		this.permisses = permisses;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Byte getIsUsable() {
		return isUsable;
	}

	public void setIsUsable(Byte isUsable) {
		this.isUsable = isUsable;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgRoleId() {
		return orgRoleId;
	}

	public void setOrgRoleId(String orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getManage() {
		return manage;
	}

	public void setManage(String manage) {
		this.manage = manage;
	}

	public String getServe() {
		return serve;
	}

	public void setServe(String serve) {
		this.serve = serve;
	}

	public String getMaint() {
		return maint;
	}

	public void setMaint(String maint) {
		this.maint = maint;
	}

	public String getOrgRoleName() {
		return orgRoleName;
	}

	public void setOrgRoleName(String orgRoleName) {
		this.orgRoleName = orgRoleName;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
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

	public Byte getIsVerification() {
		return isVerification;
	}

	public void setIsVerification(Byte isVerification) {
		this.isVerification = isVerification;
	}

}