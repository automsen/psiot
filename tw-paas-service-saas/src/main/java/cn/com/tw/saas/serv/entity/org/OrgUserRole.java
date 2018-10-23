/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.org;

import java.util.Date;

/**
 * 用户角色关系
 * t_saas_org_user_role
 */
public class OrgUserRole {

    private String id;

    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 机构角色ID
     */
    private String roleId;

    private String roleCode;

    private String userId;

    /**
     * 区域ID
     */
    private String roomId;

    private Date createTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
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
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}