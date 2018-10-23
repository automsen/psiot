package cn.com.tw.saas.serv.entity.loadShedding;

import java.util.Date;

public class RuleElecOnTimeUnusual {
	/**
	 * 规则id
	 */
    private String ruleId;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 开始日期
     */
    private Date startDay;

    /**
     * 结束日期
     */
    private Date endDay;

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
     * 当前日期
     */
    private Date timeNow;
    
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

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
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

	public Date getTimeNow() {
		return timeNow;
	}

	public void setTimeNow(Date timeNow) {
		this.timeNow = timeNow;
	}
}