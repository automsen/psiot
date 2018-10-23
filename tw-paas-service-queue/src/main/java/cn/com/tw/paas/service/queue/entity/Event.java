package cn.com.tw.paas.service.queue.entity;

public class Event {
	
	/**
	 * 终端号
	 */
	private String termNo;
	
	/**
	 * 终端所在机构Id
	 */
	private String orgId;
	
	/**
	 * 推送地址
	 */
	private String pUrl;
	
	/**
	 * 终端类型
	 */
	private String termType;

	public String getTermNo() {
		return termNo;
	}

	public void setTermNo(String termNo) {
		this.termNo = termNo;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getpUrl() {
		return pUrl;
	}

	public void setpUrl(String pUrl) {
		this.pUrl = pUrl;
	}

	public String getTermType() {
		return termType;
	}

	public void setTermType(String termType) {
		this.termType = termType;
	}

}
