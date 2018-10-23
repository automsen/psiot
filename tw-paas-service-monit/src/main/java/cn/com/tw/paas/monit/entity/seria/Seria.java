package cn.com.tw.paas.monit.entity.seria;

import java.util.Date;

public class Seria {
    private String seriaKey;

    private Long seriaValue;

    private Long maxValue;

    private Date updateTime;

    public String getSeriaKey() {
        return seriaKey;
    }

    public void setSeriaKey(String seriaKey) {
        this.seriaKey = seriaKey == null ? null : seriaKey.trim();
    }

    public Long getSeriaValue() {
        return seriaValue;
    }

    public void setSeriaValue(Long seriaValue) {
        this.seriaValue = seriaValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}