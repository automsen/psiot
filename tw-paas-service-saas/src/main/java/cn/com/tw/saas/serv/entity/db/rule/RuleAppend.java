package cn.com.tw.saas.serv.entity.db.rule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 附加费实体 对应表t_saas_rule_append
 * @author Administrator
 *
 */
public class RuleAppend implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8598265939706035087L;

	/**
	 * 附加费ID
	 */
	private String appendId;

	/**
	 * 机构Id
	 */
    private String orgId;

	/**
	 * 合约名称
	 */
    private String contName;

	/**
	 * 是否默认1.为是0.为否
	 */
    private Byte isDefault;

	/**
	 * 收费类型 对应字典库
	 */
    private String chargeType;

	/**
	 * 单价
	 */
    private BigDecimal unitPrice;

	/**
	 * 收费方式  1.面积 2.房间户
	 */
    private String chargeModel;

	/**
	 * 收费周期 1.月 2.季 3.半年 4.年
	 */
    private String chargePeriodicity;

	/**
	 * 创建时间
	 */
    private Date createTime;

	/**
	 * 修改时间
	 */
    private Date updateTime;

    public String getAppendId() {
        return appendId;
    }

    public void setAppendId(String appendId) {
        this.appendId = appendId == null ? null : appendId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName == null ? null : contName.trim();
    }

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType == null ? null : chargeType.trim();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getChargeModel() {
        return chargeModel;
    }

    public void setChargeModel(String chargeModel) {
        this.chargeModel = chargeModel == null ? null : chargeModel.trim();
    }

    public String getChargePeriodicity() {
        return chargePeriodicity;
    }

    public void setChargePeriodicity(String chargePeriodicity) {
        this.chargePeriodicity = chargePeriodicity == null ? null : chargePeriodicity.trim();
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