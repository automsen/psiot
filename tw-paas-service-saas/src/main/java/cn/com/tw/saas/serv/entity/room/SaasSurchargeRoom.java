package cn.com.tw.saas.serv.entity.room;

import java.util.Date;

public class SaasSurchargeRoom {
    private String surchargeRommId;

    private String roomId;

    private String surchargeId;

    private String appId;

    private String orgId;

    private Date createTime;

    public String getSurchargeRommId() {
        return surchargeRommId;
    }

    public void setSurchargeRommId(String surchargeRommId) {
        this.surchargeRommId = surchargeRommId == null ? null : surchargeRommId.trim();
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }

    public String getSurchargeId() {
        return surchargeId;
    }

    public void setSurchargeId(String surchargeId) {
        this.surchargeId = surchargeId == null ? null : surchargeId.trim();
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}