package cn.com.tw.saas.serv.entity.db.rule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 预警透支规则 对应数据库表t_saas_rule_alarm
 * @author Administrator
 *
 */
public class RuleAlarm implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6564600008145453098L;

	/**
	 * 主键id
	 */
	private String alarmId;

	/**
	 * 应用ID
	 */
    private String appId;

    /**
	 * 机构ID
	 */
    private String orgId;

    /**
	 * 预警规则名称
	 */
    private String ruleName;

    /**
	 * 设备二级分类（字典表）
	 */
    private String equipType;

    /**
	 * 初次预警金额（元）
	 */
    private BigDecimal alarm1Value;

    /**
	 * 初次预警方式
	 */
    private String alarm1Type;

    /**
	 * 再次预警金额（元）
	 */
    private BigDecimal alarm2Value;

    /**
	 * 再次预警方式
	 */
    private String alarm2Type;

    /**
	 * 透支额度（元）
	 */
    private BigDecimal limitValue;

    /**
	 * 是否拉闸（1.是0.否）
	 */
    private Byte isBlackout;

    /**
	 * 是否被删除（1.是0.否）
	 */
    private Byte isDelete;

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

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId == null ? null : alarmId.trim();
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

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName == null ? null : ruleName.trim();
    }

    public String getEquipType() {
        return equipType;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType == null ? null : equipType.trim();
    }

    public BigDecimal getAlarm1Value() {
        return alarm1Value;
    }

    public void setAlarm1Value(BigDecimal alarm1Value) {
        this.alarm1Value = alarm1Value;
    }

    public String getAlarm1Type() {
        return alarm1Type;
    }

    public void setAlarm1Type(String alarm1Type) {
        this.alarm1Type = alarm1Type == null ? null : alarm1Type.trim();
    }

    public BigDecimal getAlarm2Value() {
        return alarm2Value;
    }

    public void setAlarm2Value(BigDecimal alarm2Value) {
        this.alarm2Value = alarm2Value;
    }

    public String getAlarm2Type() {
        return alarm2Type;
    }

    public void setAlarm2Type(String alarm2Type) {
        this.alarm2Type = alarm2Type == null ? null : alarm2Type.trim();
    }

    public BigDecimal getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(BigDecimal limitValue) {
        this.limitValue = limitValue;
    }

    public Byte getIsBlackout() {
        return isBlackout;
    }

    public void setIsBlackout(Byte isBlackout) {
        this.isBlackout = isBlackout;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
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