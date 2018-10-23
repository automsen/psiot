package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应字段t_org_push_log
 * @author Administrator
 *
 */
public class PushLog implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7021189603037099203L;

	/**
	 * 主键ID
	 */
	private String logId;

	/**
	 * 推送url
	 */
    private String pushUrl;

    /**
     * 机构id
     */
    private String orgId;
    
    /**
	 * 认证机构名称
	 */
	private String orgName;

    /**
     * 应用id
     */
    private String appId;
    
    /**
	 * 应用名称
	 */
	private String appName;

    /**
     * 推送数据
     */
    private String pushData;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 应用业务号
     */
    private String appBusinessNo;

    /**
     * 平台业务号
     */
    private String businessNo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最近推送时间
     */
    private Date lastPushTime;

    /**
     * 尝试推送次数
     */
    private Integer tryTimes;

    /**
     * 推送状态
     */
    private String pushStatus;

    /**
     * 返回状态码
     */
    private String statusCode;

    
    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId == null ? null : logId.trim();
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl == null ? null : pushUrl.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getPushData() {
        return pushData;
    }

    public void setPushData(String pushData) {
        this.pushData = pushData == null ? null : pushData.trim();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public String getAppBusinessNo() {
        return appBusinessNo;
    }

    public void setAppBusinessNo(String appBusinessNo) {
        this.appBusinessNo = appBusinessNo == null ? null : appBusinessNo.trim();
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo == null ? null : businessNo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastPushTime() {
        return lastPushTime;
    }

    public void setLastPushTime(Date lastPushTime) {
        this.lastPushTime = lastPushTime;
    }

    public Integer getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(Integer tryTimes) {
        this.tryTimes = tryTimes;
    }

    public String getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus) {
        this.pushStatus = pushStatus == null ? null : pushStatus.trim();
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode == null ? null : statusCode.trim();
    }

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}


    
    
}