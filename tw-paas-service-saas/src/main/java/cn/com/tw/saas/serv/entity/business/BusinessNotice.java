package cn.com.tw.saas.serv.entity.business;

import java.io.Serializable;
import java.util.Date;
/**
 * 业务通知 表  t_saas_business_notice
 * @author Administrator
 *
 */
public class BusinessNotice implements Serializable{
	private static final long serialVersionUID = -4726321620495466479L;

	private String id;

    private String notifyId;

    private String orgId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 客户省份标识
     */
    private String customerNo;

    /**
     * 客户姓名
     */
    private String customerRealname;

    /**
     * 客户电话
     */
    private String customerMobile1;

    /**
     * 通知时间
     */
    private Date noticeTime;

    /**
     * 通知内容
     */
    private String noticeContent;

    /**
     * 通知状态 0.未发送 1.发送成功 2.发送失败 
     */
    private Byte noticeStatus;

    /**
     * 房间ID
     */
    private String roomId;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 房间编号
     */
    private String roomNumber;

    /**
     * 仪表编号
     */
    private String meterAddr;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 仪表类型
     */
    private String meterType;

    /**
     * 能源类型
     */
    private String energyType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId == null ? null : notifyId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName == null ? null : businessName.trim();
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

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent == null ? null : noticeContent.trim();
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
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

    public String getMeterAddr() {
        return meterAddr;
    }

    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
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

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType == null ? null : meterType.trim();
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType == null ? null : energyType.trim();
    }

	public Byte getNoticeStatus() {
		return noticeStatus;
	}

	public void setNoticeStatus(Byte noticeStatus) {
		this.noticeStatus = noticeStatus;
	}
}