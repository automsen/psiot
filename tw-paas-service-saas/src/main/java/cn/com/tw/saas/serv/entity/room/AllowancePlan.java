/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.room;

import java.util.Date;

/**
 * 房间补助用量发放计划
 * 数据库实体
 * t_saas_room_allowance_plan
 */
public class AllowancePlan {

    private String id;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 机构名
     */
    private String orgName;

    /**
     * 房间数量
     */
    private Integer roomAmount;

    /**
     * 能耗类型
     */
    private String energyType;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 计划月份yyyyMM
     */
    private String month;

    /**
     * 计划执行时间
     */
    private Date executeTime;

    /**
     * 发放比例
     */
    private Integer percent;

    /**
     * 执行状态
0，等待
1，成功
2，失败
3，撤销
     */
    private Byte status;

    /**
     * 备注
     */
    private String remark;

    private String operatorId;

    private String operatorName;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
    public String getOrgName() {
        return orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }
    public Integer getRoomAmount() {
        return roomAmount;
    }
    public void setRoomAmount(Integer roomAmount) {
        this.roomAmount = roomAmount;
    }
    public String getEnergyType() {
        return energyType;
    }
    public void setEnergyType(String energyType) {
        this.energyType = energyType == null ? null : energyType.trim();
    }
    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month == null ? null : month.trim();
    }
    public Date getExecuteTime() {
        return executeTime;
    }
    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }
    public Integer getPercent() {
        return percent;
    }
    public void setPercent(Integer percent) {
        this.percent = percent;
    }
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    public String getOperatorId() {
        return operatorId;
    }
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }
    public String getOperatorName() {
        return operatorName;
    }
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
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