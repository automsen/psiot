package cn.com.tw.paas.job.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 机构实体 对应t_org
 * @author Administrator
 *
 */
public class Org implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 5552511214107307390L;

	/**
	 * 主键、机构id
	 */
	private String orgId;

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
	
	/**
	 * 机构官网
	 */
	private String orgWebsite;
	
	/**
	 * 机构logo
	 */
	private String orgLogo;

	/**
	 * 是否可用 1是 0否
	 */
	private Byte isUsable;

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	private String parentOrgId;

	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	
	

	public String getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId == null ? null : orgId.trim();
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode == null ? null : orgCode.trim();
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName == null ? null : orgName.trim();
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus == null ? null : orgStatus.trim();
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
		this.unifiedCode = unifiedCode == null ? null : unifiedCode.trim();
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType == null ? null : orgType.trim();
	}

	public String getOrgAddr() {
		return orgAddr;
	}

	public void setOrgAddr(String orgAddr) {
		this.orgAddr = orgAddr == null ? null : orgAddr.trim();
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

	public String getOrgWebsite() {
		return orgWebsite;
	}

	public void setOrgWebsite(String orgWebsite) {
		this.orgWebsite = orgWebsite;
	}

	public String getOrgLogo() {
		return orgLogo;
	}

	public void setOrgLogo(String orgLogo) {
		this.orgLogo = orgLogo;
	}
}