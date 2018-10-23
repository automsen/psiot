package cn.com.tw.saas.serv.entity.db.sys;

import java.util.Date;

/**
 * t_sys_notify_record
 * @author Administrator
 *
 */
public class NotifyRecord {
    private String uuid;

    private String url;

    /**
     * 被通知的手机号，微信号等等
     */
    private String notifyNo;

    /**
     * 通知操作的业务主键，比如创建哪个订单后通知，将该订单号记录下来
     */
    private String notifyBusinessNo;

    /**
     * 通知用户Id
     */
    private String notifyUserId;

    /**
     * 通知级别
     */
    private Byte notifyLevel;

    /**
     * ("创建成功", 100) ("通知成功", 000)（“最终通知失败”，111）（“请求超时,等待重发”，110）（“请求超时,等待重发”，101）
     */
    private String status;

    /**
     * 通知类型：("短信发送", 10)("微信通知"，20)
     */
    private String notifyType;

    /**
     * 通知的重复次数
     */
    private Byte notifyTimes;

    /**
     * 最大通知记录
     */
    private Byte limitNotifyTimes;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后通知时间
     */
    private Date lastNotifyTime;

    private Byte isFirst;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getNotifyNo() {
        return notifyNo;
    }

    public void setNotifyNo(String notifyNo) {
        this.notifyNo = notifyNo == null ? null : notifyNo.trim();
    }

    public String getNotifyBusinessNo() {
        return notifyBusinessNo;
    }

    public void setNotifyBusinessNo(String notifyBusinessNo) {
        this.notifyBusinessNo = notifyBusinessNo == null ? null : notifyBusinessNo.trim();
    }

    public String getNotifyUserId() {
        return notifyUserId;
    }

    public void setNotifyUserId(String notifyUserId) {
        this.notifyUserId = notifyUserId == null ? null : notifyUserId.trim();
    }

    public Byte getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(Byte notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType == null ? null : notifyType.trim();
    }

    public Byte getNotifyTimes() {
        return notifyTimes;
    }

    public void setNotifyTimes(Byte notifyTimes) {
        this.notifyTimes = notifyTimes;
    }

    public Byte getLimitNotifyTimes() {
        return limitNotifyTimes;
    }

    public void setLimitNotifyTimes(Byte limitNotifyTimes) {
        this.limitNotifyTimes = limitNotifyTimes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastNotifyTime() {
        return lastNotifyTime;
    }

    public void setLastNotifyTime(Date lastNotifyTime) {
        this.lastNotifyTime = lastNotifyTime;
    }

    public Byte getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Byte isFirst) {
        this.isFirst = isFirst;
    }
}