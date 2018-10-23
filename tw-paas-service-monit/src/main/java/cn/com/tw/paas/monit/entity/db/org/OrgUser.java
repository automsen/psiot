package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.util.Date;

/**
 * 机构用户 t_org_user 
 * @author Administrator
 *
 */
public class OrgUser implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8115437342455775361L;

	/**
	 * 主键ID
	 */
	private String userId;

	/**
	 * 用户名
	 */
    private String userName;

    /**
	 * 登录密码
	 */
    private String password;

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
	 *真实姓名
	 */
    private String realName;

    /**
	 * 机构ID
	 */
    private String orgId;

    /**
	 * 是否可用 1可用 0不可用
	 */
    private Byte isUsable;

    /**
	 * 创建时间
	 */
    private Date createTime;

    /**
	 * 修改时间
	 */
    private Date updateTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat == null ? null : wechat.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
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
}