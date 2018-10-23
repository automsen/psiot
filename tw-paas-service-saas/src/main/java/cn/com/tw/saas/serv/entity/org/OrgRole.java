/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.org;

/**
 * 角色
 * t_saas_org_role
 */
public class OrgRole {

    private String roleId;

    private String roleCode;

    /**
     * 机构用户角色名
     */
    private String roleName;

    /**
     * 是否可用1=是0=否
     */
    private Byte isUsable;
    
    private String orgId;

    public String getRoleId() {
        return roleId;
    }
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }
    public String getRoleCode() {
        return roleCode;
    }
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }
    public Byte getIsUsable() {
        return isUsable;
    }
    public void setIsUsable(Byte isUsable) {
        this.isUsable = isUsable;
    }
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}