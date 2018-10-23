package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应t_org_push_log
 * @author Cheng Qi Peng
 *
 */
public class ApiLog implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6484769836931425154L;

	/**
     * 主键
     */
    private String logId;

    /**
     * 机构id
     */
    private String orgId;
    
    /**
     * 机构名称
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

    private String apiUrl;

    private String authtoken;

    /**
     * 来访ip
     */
    private String requestorIp;

    /**
     * 应用业务号
     */
    private String appBusinessNo;

    /**
     * 请求参数
     */
    private String requestData;

    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 平台业务号
     */
    private String businessNo;

    private String statusCode;

    /**
     * 返回数据
     */
    private String responseData;

    private Date createTime;

    public String getLogId() {
        return logId;
    }
    public void setLogId(String logId) {
        this.logId = logId == null ? null : logId.trim();
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
    public String getApiUrl() {
        return apiUrl;
    }
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl == null ? null : apiUrl.trim();
    }
    public String getAuthtoken() {
        return authtoken;
    }
    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken == null ? null : authtoken.trim();
    }
    public String getRequestorIp() {
        return requestorIp;
    }
    public void setRequestorIp(String requestorIp) {
        this.requestorIp = requestorIp == null ? null : requestorIp.trim();
    }
    public String getAppBusinessNo() {
        return appBusinessNo;
    }
    public void setAppBusinessNo(String appBusinessNo) {
        this.appBusinessNo = appBusinessNo == null ? null : appBusinessNo.trim();
    }
    public String getRequestData() {
        return requestData;
    }
    public void setRequestData(String requestData) {
        this.requestData = requestData == null ? null : requestData.trim();
    }
    public Date getRequestTime() {
        return requestTime;
    }
    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }
    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }
    public String getBusinessNo() {
        return businessNo;
    }
    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo == null ? null : businessNo.trim();
    }
    public String getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode == null ? null : statusCode.trim();
    }
    public String getResponseData() {
        return responseData;
    }
    public void setResponseData(String responseData) {
        this.responseData = responseData == null ? null : responseData.trim();
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
