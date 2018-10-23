package cn.com.tw.paas.monit.entity.business.base;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class BaseParam implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String businessNo;
	
	private String appId;
	
	@NotEmpty(message="orgId can not null")
	private String orgId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	@Override
	public String toString() {
		return "BaseParam [businessNo=" + businessNo + ", appId=" + appId
				+ ", orgId=" + orgId + "]";
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	

}
