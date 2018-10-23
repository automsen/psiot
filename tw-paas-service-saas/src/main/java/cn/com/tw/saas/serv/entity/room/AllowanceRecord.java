/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.room;

import java.math.BigDecimal;
import java.util.Date;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.saas.serv.entity.org.OrgUser;

/**
 * 房间计划用量记录
 * 数据库实体
 * t_saas_room_allowance_record
 */
public class AllowanceRecord {

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
     * 房间id
     */
    private String roomId;

    /**
     * 房间账户id
     */
    private String roomAccountId;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 房间编号
     */
    private String roomNumber;

    /**
     * 房间完整名称
     */
    private String roomFullName;

    /**
     * 能耗类型
     */
    private String energyType;

    private String orderTypeCode;

    private String orderId;
    /**
     * 计划执行时间
     */
    private Date executeTime;

    /**
     * 变动额
     */
    private BigDecimal volume;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 状态
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

    private String details;

    private String billTemplateId;

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
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }
    public String getRoomAccountId() {
        return roomAccountId;
    }
    public void setRoomAccountId(String roomAccountId) {
        this.roomAccountId = roomAccountId == null ? null : roomAccountId.trim();
    }
    public String getRoomName() {
        return roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }
    public String getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber == null ? null : roomNumber.trim();
    }
    public String getRoomFullName() {
        return roomFullName;
    }
    public void setRoomFullName(String roomFullName) {
        this.roomFullName = roomFullName == null ? null : roomFullName.trim();
    }
    public String getEnergyType() {
        return energyType;
    }
    public void setEnergyType(String energyType) {
        this.energyType = energyType == null ? null : energyType.trim();
    }
    public String getOrderTypeCode() {
        return orderTypeCode;
    }
    public void setOrderTypeCode(String orderTypeCode) {
        this.orderTypeCode = orderTypeCode == null ? null : orderTypeCode.trim();
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }
    public Date getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}
	public BigDecimal getVolume() {
        return volume;
    }
    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details == null ? null : details.trim();
    }
    public String getBillTemplateId() {
        return billTemplateId;
    }
    public void setBillTemplateId(String billTemplateId) {
        this.billTemplateId = billTemplateId == null ? null : billTemplateId.trim();
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
    
    /**
     * 预发放记录
     * @param room
     * @param plan
     * @param user
     * @param volume
     * @return
     */
	public static AllowanceRecord generateRecord(Room room ,OrgUser user,BigDecimal volume){
		AllowanceRecord record = new AllowanceRecord();
		record.setId(CommUtils.getUuid());
		record.setOrgId(room.getOrgId());
//		record.setOrgName("");
		record.setRoomAccountId(room.getAccountId());
		record.setRoomId(room.getRoomId());
		if(null!=room){
			record.setRoomName(room.getRoomName());
			record.setRoomNumber(room.getRoomNumber());
			record.setRoomFullName(room.getRegionFullName()+room.getRoomName());
//			record.setRoomUse(room.getRoomUse());
		}
//		record.setSubAccountType(subAcc.getSubAccountType());
//		record.setOrderTypeCode("");
//		record.setOrderId("");
//		record.setPayModeCode("");
		record.setVolume(volume);
//		record.setRemark("");
//		record.setDetails("");

		if (null!=user){
			record.setOperatorId(user.getUserId());
			record.setOperatorName(user.getUserName());
		}
		return record;
	}
}