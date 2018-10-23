package cn.com.tw.saas.serv.entity.loadShedding;

import java.util.Date;

public class RuleElecOnTimeUsual {
	/**
	 * 规则id
	 */
    private String ruleId;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 星期几
     */
    private Integer dayOfWeek;

    /**
     * 供电时间（hhmm）
     */
    private String onTime;

    /**
     * 断电时间（hhmm）
     */
    private String offTime;

    /**
     * 受控制的开关
     */
    private String switchs;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
    
    //扩展字段
    /**
     * 控1
     */
    private String illumination;
    
    /**
     * 控2
     */
    private String socket;
    
    /**
     * 控3
     */
    private String airConditioning;
    
    public String getIllumination() {
		return illumination;
	}

	public void setIllumination(String illumination) {
		this.illumination = illumination;
	}

	public String getSocket() {
		return socket;
	}

	public void setSocket(String socket) {
		this.socket = socket;
	}

	public String getAirConditioning() {
		return airConditioning;
	}

	public void setAirConditioning(String airConditioning) {
		this.airConditioning = airConditioning;
	}

	public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId == null ? null : ruleId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getOnTime() {
        return onTime;
    }

    public void setOnTime(String onTime) {
        this.onTime = onTime == null ? null : onTime.trim();
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime == null ? null : offTime.trim();
    }

    public String getSwitchs() {
        return switchs;
    }

    public void setSwitchs(String switchs) {
        this.switchs = switchs == null ? null : switchs.trim();
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