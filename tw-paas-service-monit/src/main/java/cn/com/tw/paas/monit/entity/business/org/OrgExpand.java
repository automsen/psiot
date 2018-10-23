package cn.com.tw.paas.monit.entity.business.org;

import cn.com.tw.paas.monit.entity.db.org.Org;

/**
 * 机构扩展字段 
 * @author Administrator
 *
 */
public class OrgExpand extends Org{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8946190500541301709L;

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
   // @Email
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
}