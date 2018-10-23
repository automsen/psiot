package cn.com.tw.paas.job.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 机构应用 t_org_application
 * @author Administrator
 *
 */
public class OrgApplication implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1990321680805246535L;

	/**
	 * 应用id
	 */
	private String appId;

	/**
	 * 机构id
	 */
    private String orgId;

    /**
     * 应用名称
     */
    private String appName;
    
    /**
     * 公开的应用标识
     */
    private String appKey;
    
    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 
     */
    private String authtoken;

    /**
     * 回调url
     */
    private String callbackUrl;

    /**
     * 是否可用 1可用 0不可用
     */
    private Byte isUsable;
    
    private String appBussinesNo;
    
    private String bussinesNo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
    
    /***
     * 服务到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date serviceTime;
    

    //扩展字段
    /**
     * 机构名称
     */
    private String orgName;
    
    /**
     * 不包含的机构id
     */
    private String notIn;
    
	public String getBussinesNo() {
		return bussinesNo;
	}

	public void setBussinesNo(String bussinesNo) {
		this.bussinesNo = bussinesNo;
	}

	public String getAppBussinesNo() {
		return appBussinesNo;
	}

	public void setAppBussinesNo(String appBussinesNo) {
		this.appBussinesNo = appBussinesNo;
	}

	public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken == null ? null : authtoken.trim();
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl == null ? null : callbackUrl.trim();
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

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Date getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Date serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getNotIn() {
		return notIn;
	}

	public void setNotIn(String notIn) {
		this.notIn = notIn;
	}
}