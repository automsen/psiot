package cn.com.tw.paas.monit.entity.business.org;

import cn.com.tw.paas.monit.entity.db.org.OrgApplication;

/**
 * 机构应用扩展字段
 * @author Administrator
 *
 */
public class OrgApplicationExpand extends OrgApplication{

	/**
	 * 
	 */
	private static final long serialVersionUID = 396888374493411945L;

	/**
	 * 机构编号
	 */
	private String orgCode;

	/**
	 * 认证机构名称
	 */
	private String orgName;

	/**
	 * 认证状态 1正常 
	 */
	private String orgStatus;

	/**
	 * 1、普通（只能接入一个应用）
            2、代理商（可接入多个应用）
	 */
	private Integer orgLevel;

	/**
	 * 统一社会信用代码
	 */
	private String unifiedCode;

	/**
	 * 认证机构类型
	 */
	private String orgType;

	/**
	 * 认证机构地址
	 */
	private String orgAddr;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public Integer getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(Integer orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getUnifiedCode() {
		return unifiedCode;
	}

	public void setUnifiedCode(String unifiedCode) {
		this.unifiedCode = unifiedCode;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgAddr() {
		return orgAddr;
	}

	public void setOrgAddr(String orgAddr) {
		this.orgAddr = orgAddr;
	}
	
	
}
