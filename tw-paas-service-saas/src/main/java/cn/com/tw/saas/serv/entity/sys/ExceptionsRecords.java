package cn.com.tw.saas.serv.entity.sys;

import java.util.Date;

/**
 * 异常事件记录  t_saas_exceptions_records
 * @author Administrator
 *
 */
public class ExceptionsRecords {
    private String id;

    private String orgId;

    /**
     * 事件通知类型  详细看NotifyBusTypeEm
     */
    private String notifyBusType;

    private String meterAddr;

    private String energyType;

    private String roomId;

    private String regionId;

    private String regionNo;

    private String regionNumber;

    private String regionFullName;

    private String roomUse;

    private String roomNumber;

    private String roomName;

    private String accountId;

    private String customerId;

    private String customerNo;

    private String customerType;

    private String customerRealname;

    private String customerMobile1;

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

    public String getMeterAddr() {
        return meterAddr;
    }

    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType == null ? null : energyType.trim();
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getRegionNo() {
        return regionNo;
    }

    public void setRegionNo(String regionNo) {
        this.regionNo = regionNo == null ? null : regionNo.trim();
    }

    public String getRegionNumber() {
        return regionNumber;
    }

    public void setRegionNumber(String regionNumber) {
        this.regionNumber = regionNumber == null ? null : regionNumber.trim();
    }

    public String getRegionFullName() {
        return regionFullName;
    }

    public void setRegionFullName(String regionFullName) {
        this.regionFullName = regionFullName == null ? null : regionFullName.trim();
    }

    public String getRoomUse() {
        return roomUse;
    }

    public void setRoomUse(String roomUse) {
        this.roomUse = roomUse == null ? null : roomUse.trim();
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber == null ? null : roomNumber.trim();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo == null ? null : customerNo.trim();
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType == null ? null : customerType.trim();
    }

    public String getCustomerRealname() {
        return customerRealname;
    }

    public void setCustomerRealname(String customerRealname) {
        this.customerRealname = customerRealname == null ? null : customerRealname.trim();
    }

    public String getCustomerMobile1() {
        return customerMobile1;
    }

    public void setCustomerMobile1(String customerMobile1) {
        this.customerMobile1 = customerMobile1 == null ? null : customerMobile1.trim();
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

	public String getNotifyBusType() {
		return notifyBusType;
	}

	public void setNotifyBusType(String notifyBusType) {
		this.notifyBusType = notifyBusType;
	}

}