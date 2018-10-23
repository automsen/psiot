package cn.com.tw.saas.serv.entity.db.rule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 附加费用规则 对应数据库表t_saas_surcharge
 * @author Administrator
 *
 */
public class Surcharge implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6611367733178629723L;

	/**
	 * 附加费ID
	 */
	private String surchargeId;

	/**
	 * 机构ID
	 */
    private String orgId;

	/**
	 * 应用ID
	 */
    private String appId;

	/**
	 * 操作者ID
	 */
    private String orgUserId;

	/**
	 * 附加费名称
	 */
    private String surchargeName;

	/**
	 * 收费类型
	 */
    private String surchargeType;

	/**
	 * 收费方式
	 */
    private String surchargeModel;

	/**
	 * 单价
	 */
    private BigDecimal unitPrice;

	/**
	 * 创建时间
	 */
    private Date createTime;

	/**
	 * 修改时间
	 */
    private Date updateTime;

    public String getSurchargeId() {
        return surchargeId;
    }

    public void setSurchargeId(String surchargeId) {
        this.surchargeId = surchargeId == null ? null : surchargeId.trim();
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

    public String getOrgUserId() {
        return orgUserId;
    }

    public void setOrgUserId(String orgUserId) {
        this.orgUserId = orgUserId == null ? null : orgUserId.trim();
    }

    public String getSurchargeName() {
        return surchargeName;
    }

    public void setSurchargeName(String surchargeName) {
        this.surchargeName = surchargeName == null ? null : surchargeName.trim();
    }

    public String getSurchargeType() {
        return surchargeType;
    }

    public void setSurchargeType(String surchargeType) {
        this.surchargeType = surchargeType == null ? null : surchargeType.trim();
    }

    public String getSurchargeModel() {
        return surchargeModel;
    }

    public void setSurchargeModel(String surchargeModel) {
        this.surchargeModel = surchargeModel == null ? null : surchargeModel.trim();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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