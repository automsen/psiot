package cn.com.tw.paas.monit.entity.statis;

import java.math.BigDecimal;
import java.util.Date;

public class EquipStatis {
    private String id;

    private Integer orgId;

    private Integer appId;

    private String equipCategCode;

    private String equipTypeCode;

    private String elecEquipTypeCode;

    private String equipNumber;

    private String equipName;

    private String dayTime;

    private Integer collectNum;

    private Integer collectFreq;

    private BigDecimal collectSuccRate;

    private Integer stopExcepNum;

    private Date lastCollectTime;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getEquipCategCode() {
        return equipCategCode;
    }

    public void setEquipCategCode(String equipCategCode) {
        this.equipCategCode = equipCategCode == null ? null : equipCategCode.trim();
    }

    public String getEquipTypeCode() {
        return equipTypeCode;
    }

    public void setEquipTypeCode(String equipTypeCode) {
        this.equipTypeCode = equipTypeCode == null ? null : equipTypeCode.trim();
    }

    public String getElecEquipTypeCode() {
        return elecEquipTypeCode;
    }

    public void setElecEquipTypeCode(String elecEquipTypeCode) {
        this.elecEquipTypeCode = elecEquipTypeCode == null ? null : elecEquipTypeCode.trim();
    }

    public String getEquipNumber() {
        return equipNumber;
    }

    public void setEquipNumber(String equipNumber) {
        this.equipNumber = equipNumber == null ? null : equipNumber.trim();
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName == null ? null : equipName.trim();
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime == null ? null : dayTime.trim();
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getCollectFreq() {
        return collectFreq;
    }

    public void setCollectFreq(Integer collectFreq) {
        this.collectFreq = collectFreq;
    }

    public BigDecimal getCollectSuccRate() {
        return collectSuccRate;
    }

    public void setCollectSuccRate(BigDecimal collectSuccRate) {
        this.collectSuccRate = collectSuccRate;
    }

    public Integer getStopExcepNum() {
        return stopExcepNum;
    }

    public void setStopExcepNum(Integer stopExcepNum) {
        this.stopExcepNum = stopExcepNum;
    }

    public Date getLastCollectTime() {
        return lastCollectTime;
    }

    public void setLastCollectTime(Date lastCollectTime) {
        this.lastCollectTime = lastCollectTime;
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