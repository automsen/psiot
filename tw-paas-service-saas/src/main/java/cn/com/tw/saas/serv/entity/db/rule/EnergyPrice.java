/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.db.rule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 能耗价格规则
 * 数据库实体
 * 对应t_saas_rule_energy_price
 */
public class EnergyPrice implements Serializable{

	private static final long serialVersionUID = 5404791825477127460L;

	/**
     * 主键id
     */
    private String priceId;

    /**
     * (用于标记同一规则的不同历史版本)规则编号
     */
    private String priceNo;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 计价规则名称
     */
    private String priceName;

    /**
     * 能源类型
     */
    private String energyType;

    /**
     * 价格类型(字典表)
     */
    private String priceType;

    /**
     * 基准价格（尖）
     */
    private BigDecimal price1;

    /**
     * 基准价格（峰）
     */
    private BigDecimal price2;

    /**
     * 基准价格（平）
     */
    private BigDecimal price3;

    /**
     * 基准价格（谷）
     */
    private BigDecimal price4;

    /**
     * 阶梯数量
     */
    private Byte stepNum;

    /**
     * 阶梯1加价
     */
    private BigDecimal step1Price;

    /**
     * 阶梯1上边界值
     */
    private BigDecimal step1Value;

    /**
     * 阶梯2加价
     */
    private BigDecimal step2Price;

    /**
     * 阶梯2上边界值
     */
    private BigDecimal step2Value;

    /**
     * 阶梯3加价
     */
    private BigDecimal step3Price;

    /**
     * 阶梯3上边界值
     */
    private BigDecimal step3Value;

    /**
     * 阶梯4加价
     */
    private BigDecimal step4Price;

    /**
     * 是否可用（1.是0.否）
     */
    private Byte isUsable;

    /**
     * 是否历史（0，最新版本
            1，历史版本）
     */
    private Byte isHistory;

    /**
     * 操作者id
     */
    private String orgUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 是否默认1.为是0.为否
     */
    private Byte isDefault;

    public String getPriceId() {
        return priceId;
    }
    public void setPriceId(String priceId) {
        this.priceId = priceId == null ? null : priceId.trim();
    }
    public String getPriceNo() {
        return priceNo;
    }
    public void setPriceNo(String priceNo) {
        this.priceNo = priceNo == null ? null : priceNo.trim();
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
    public String getPriceName() {
        return priceName;
    }
    public void setPriceName(String priceName) {
        this.priceName = priceName == null ? null : priceName.trim();
    }
    public String getEnergyType() {
        return energyType;
    }
    public void setEnergyType(String energyType) {
        this.energyType = energyType == null ? null : energyType.trim();
    }
    public String getPriceType() {
        return priceType;
    }
    public void setPriceType(String priceType) {
        this.priceType = priceType == null ? null : priceType.trim();
    }
    public BigDecimal getPrice1() {
        return price1;
    }
    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }
    public BigDecimal getPrice2() {
        return price2;
    }
    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }
    public BigDecimal getPrice3() {
        return price3;
    }
    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }
    public BigDecimal getPrice4() {
        return price4;
    }
    public void setPrice4(BigDecimal price4) {
        this.price4 = price4;
    }
    public Byte getStepNum() {
        return stepNum;
    }
    public void setStepNum(Byte stepNum) {
        this.stepNum = stepNum;
    }
    public BigDecimal getStep1Price() {
        return step1Price;
    }
    public void setStep1Price(BigDecimal step1Price) {
        this.step1Price = step1Price;
    }
    public BigDecimal getStep1Value() {
        return step1Value;
    }
    public void setStep1Value(BigDecimal step1Value) {
        this.step1Value = step1Value;
    }
    public BigDecimal getStep2Price() {
        return step2Price;
    }
    public void setStep2Price(BigDecimal step2Price) {
        this.step2Price = step2Price;
    }
    public BigDecimal getStep2Value() {
        return step2Value;
    }
    public void setStep2Value(BigDecimal step2Value) {
        this.step2Value = step2Value;
    }
    public BigDecimal getStep3Price() {
        return step3Price;
    }
    public void setStep3Price(BigDecimal step3Price) {
        this.step3Price = step3Price;
    }
    public BigDecimal getStep3Value() {
        return step3Value;
    }
    public void setStep3Value(BigDecimal step3Value) {
        this.step3Value = step3Value;
    }
    public BigDecimal getStep4Price() {
        return step4Price;
    }
    public void setStep4Price(BigDecimal step4Price) {
        this.step4Price = step4Price;
    }
    public Byte getIsUsable() {
        return isUsable;
    }
    public void setIsUsable(Byte isUsable) {
        this.isUsable = isUsable;
    }
    public Byte getIsHistory() {
        return isHistory;
    }
    public void setIsHistory(Byte isHistory) {
        this.isHistory = isHistory;
    }
    public String getOrgUserId() {
        return orgUserId;
    }
    public void setOrgUserId(String orgUserId) {
        this.orgUserId = orgUserId == null ? null : orgUserId.trim();
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
	public Byte getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}
}